package com.rafael.sales.domain.service;

import com.rafael.sales.api.assembler.SaleModelAssembler;
import com.rafael.sales.api.assembler.SaleModelDisassembler;
import com.rafael.sales.api.model.SaleModel;
import com.rafael.sales.api.model.input.SaleInput;
import com.rafael.sales.domain.service.SendEmailService.Message;
import com.rafael.sales.domain.exception.*;
import com.rafael.sales.domain.model.*;
import com.rafael.sales.domain.repository.ProductRepository;
import com.rafael.sales.domain.repository.SaleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class RegisterSaleService {

    private static final String PRODUCT_NOT_FOUND = "Produto de não encontrado, informe um código valído!";
    private static final String NO_PRODUCT_LINKED = "Nenhum produto vinculado a venda";
    private static final String INSUFFICIENT_STOCK = "Não há estoque suficiente para a venda do produto %s, " +
            "quantidade em estoque: %s";
    public static final String SALE_NOT_FOUND = "Venda de código '%s' não foi encontrada ou não existe!";
    public static final String SALE_ALREADY_CANCELED = "A venda de código %s já foi cancelada!";
    public static final String SALE_EMITIDA = "A venda de código '%s' já foi emitida.";
    public static final String SALE_CANCELED = "A venda de código '%s' está cancelada e não pode ser emitida!";

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;

    private final RegisterProductService registerProductService;

    private SaleModelAssembler saleModelAssembler;
    private SaleModelDisassembler saleModelDisassembler;

    private SendEmailService emailService;

    @PersistenceContext
    private EntityManager entityManager;

    public Sale findSale(String saleCode) {
        return saleRepository.findByCode(saleCode).orElseThrow(() -> new BusinessException(String.format(SALE_NOT_FOUND, saleCode)));
    }

    @Transactional
    public SaleModel save(SaleInput saleInput) {
        if (saleInput.getItems().isEmpty()) {
            throw new BusinessException(NO_PRODUCT_LINKED);
        }

        saleInput.setDateRegister(OffsetDateTime.now());

        List<Product> productList = new ArrayList<>();
            saleInput.getItems().forEach(itemSale -> {
                if (itemSale.getProduct().getId() == null)
                    throw new EntityNotFoundException(PRODUCT_NOT_FOUND);

                Product product = registerProductService.findProductById(itemSale.getProduct().getId());
                itemSale.getProduct().setName(product.getName());

                if (product.getQuantity().compareTo(itemSale.getQuantity()) < 1) {
                    throw new InsufficientStockException(String.format(INSUFFICIENT_STOCK, product.getName(), product.getQuantity()));
                }

                if (saleInput.getStatus() == StatusSale.EMITIR) {
                    product.setQuantity(product.getQuantity().subtract(itemSale.getQuantity()));
                    saleInput.setStatus(StatusSale.EMITIDA);
                }
                productList.add(product);
            });
        productRepository.saveAll(productList);

        var sale = saleRepository.save(saleModelDisassembler.toDomainObject(saleInput));
        entityManager.refresh(sale);

        if (sale.getStatus().equals(StatusSale.EMITIDA)) {
            mailSending(sale);
        }
        return saleModelAssembler.toModel(sale);
    }

    private void mailSending(Sale sale) {
        String subject = String.format("%s - Venda", sale.getUser().getName());

        Message message = Message.builder()
                        .receiver(sale.getUser().getEmail())
                        .subject(subject)
                        .body("sale-emitida.html")
                        .variable("sale", sale)
                        .build();
        emailService.send(message);
    }

    @Transactional
    public SaleModel edit(String saleCode, SaleInput saleInput) {
        Sale saleEdit = this.findSale(saleCode);

        if (saleEdit.getStatus().equals(StatusSale.EMITIDA)) {
            throw new EntityConflictException(String.format(SALE_EMITIDA, saleCode));
        } else if (saleEdit.getStatus().equals(StatusSale.CANCELED)) {
            throw new EntityConflictException(String.format(SALE_CANCELED, saleCode));
        }


        if (saleInput.getStatus().equals(StatusSale.EMITIR)) {
            saleEdit.getItems().forEach(item -> {
                var productRemove = saleInput.getItems().stream().filter(i -> i.getProduct().getId().equals(item.getProduct().getId())).findFirst();

                if (productRemove.isEmpty()) {
                    Product product = registerProductService.findProductById(item.getProduct().getId());
                    saleInput.getItems().remove(product);
                }
            });

            saleInput.getItems().forEach(itemInput -> {
                var produtoDaVenda = saleEdit.getItems().stream().filter(i -> i.getProduct().getId().equals(itemInput.getProduct().getId())).findFirst();
                var product = registerProductService.findProductById(itemInput.getProduct().getId());

                if (produtoDaVenda.isPresent()) {
                    if (produtoDaVenda.get().getQuantity().compareTo(itemInput.getQuantity()) < 0) {
                        var calculateStock = produtoDaVenda.get().getQuantity().subtract(itemInput.getQuantity());
                        product.setQuantity(product.getQuantity().add(calculateStock));
                    } else if (produtoDaVenda.get().getQuantity().compareTo(itemInput.getQuantity()) > 0) {
                        var calculateStock = produtoDaVenda.get().getQuantity().subtract(itemInput.getQuantity());
                        product.setQuantity(product.getQuantity().add(calculateStock));
                    } else {
                        product.setQuantity(product.getQuantity().subtract(itemInput.getQuantity()));
                    }
                } else {
                    product.setQuantity(product.getQuantity().subtract(itemInput.getQuantity()));
                }
            });
            saleEdit.setStatus(StatusSale.EMITIDA);
        }

        var saleDomain = saleModelDisassembler.toDomainObject(saleInput);

        saleEdit.getItems().clear();
        saleEdit.getItems().addAll(saleDomain.getItems());
        saleEdit.prePersist();

        return saleModelAssembler.toModel(saleRepository.save(saleEdit));
    }

    public void cancel(String saleCode) {
        var saleCancel = this.findSale(saleCode);
        if (saleCancel.getStatus().equals(StatusSale.CANCELED) || saleCancel == null) {
            throw new BusinessException(String.format(SALE_ALREADY_CANCELED, saleCode));
        }

        saleCancel.getItems().forEach(item -> {
            var ps = saleCancel.getItems().stream().filter(i -> i.getProduct().getId().equals(item.getProduct().getId())).findAny();
            Optional<Product> product = productRepository.findById(ps.get().getProduct().getId());
            product.get().setQuantity(product.get().getQuantity().add(ps.get().getQuantity()));

            productRepository.save(product.get());
        });

        saleCancel.setStatus(StatusSale.CANCELED);
        saleRepository.save(saleCancel);
    }

}