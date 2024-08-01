package com.rafael.sales.domain.service;

import com.rafael.sales.api.assembler.SaleModelAssembler;
import com.rafael.sales.api.assembler.SaleModelDisassembler;
import com.rafael.sales.api.model.SaleModel;
import com.rafael.sales.api.model.input.SaleInput;
import com.rafael.sales.domain.exception.*;
import com.rafael.sales.domain.model.Product;
import com.rafael.sales.domain.model.Sale;
import com.rafael.sales.domain.model.StatusSale;
import com.rafael.sales.domain.model.User;
import com.rafael.sales.domain.repository.ProductRepository;
import com.rafael.sales.domain.repository.SaleRepository;
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
    public static final String SALE_NOT_FOUND = "Venda de código %s não foi encontrada ou não existe!";
    public static final String SALE_ALREADY_CANCELED = "A venda de código %s já foi cancelada!";

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;

    private final RegisterProductService registerProductService;
    private final RegisterUserService registerUserService;

    private SaleModelAssembler saleModelAssembler;
    private SaleModelDisassembler saleModelDisassembler;

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
        if (saleInput.getStatus() == StatusSale.EMITIR) {
            saleInput.getItems().forEach(itemSale -> {
                if (itemSale.getProduct().getId() == null)
                    throw new EntityNotFoundException(PRODUCT_NOT_FOUND);

                Product product = registerProductService.findProductById(itemSale.getProduct().getId());

                if (product.getQuantity().compareTo(itemSale.getQuantity()) < 1) {
                    throw new InsufficientStockException(String.format(INSUFFICIENT_STOCK, product.getName(), product.getQuantity()));
                }

                product.setQuantity(product.getQuantity().subtract(itemSale.getQuantity()));
                productList.add(product);
            });
            productRepository.saveAll(productList);
        }

        Sale sale = saleRepository.save(saleModelDisassembler.toDomainObject(saleInput));
        return saleModelAssembler.toModel(sale);
    }

    @Transactional
    public SaleModel edit(String saleCode, SaleInput saleInput) {
        Sale saleEdit = this.findSale(saleCode);
        if (saleEdit.getStatus() == StatusSale.CANCELED) {
            throw new BusinessException("Não é possivel editar uma venda cancelada!");
        }


        saleEdit.getItems().forEach(item -> {
            var inputItemNotExists = saleInput.getItems().stream().filter(i -> i.getProduct().getId().equals(item.getProduct().getId())).findFirst();
            var product = registerProductService.findProductById(item.getProduct().getId());

            if (inputItemNotExists.isEmpty() && saleInput.getStatus() == StatusSale.EMITIR) {
                product.setQuantity(item.getQuantity().add(product.getQuantity()));
                productRepository.save(product);
            }
        });

        if (saleInput.getStatus() == StatusSale.EMITIR) {
            saleInput.getItems().forEach(item -> {
                var product = registerProductService.findProductById(item.getProduct().getId());
                var productSale = saleEdit.getItems().stream().filter(i -> i.getProduct().getId().equals(item.getProduct().getId())).findFirst();


// TODO QUANDO UMA VENDA É LANÇADA COMO "ORÇAMENTO" NÃO É DESCONTADO OS PRODUTOS DO ESTOQUE
// TODO QUANDO UMA VENDA É PASSADA COM EMITIR ELA DEVE DESCONTAR NO ESTOQUE
//  E CASO SE TIVER MENOS UNITARIO DO QUE NA VENDA JÁ SALVA ELA DESCONTE JÁ ATUALIZANDO A DIFERENÇA E VICE VERSE!

// TODO                PROXIMO PASSO É VERIFICAR COMO ESTÁ CHEGANDO NESSE BREAKPOINT
                var quantity = item.getQuantity().subtract(productSale.get().getQuantity());
                product.setQuantity(product.getQuantity().subtract(quantity));


                productRepository.save(product);
                saleEdit.setStatus(StatusSale.EMITIDA);
            });
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