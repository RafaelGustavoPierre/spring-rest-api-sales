package com.rafael.sales.domain.service;

import com.rafael.sales.domain.exception.*;
import com.rafael.sales.domain.model.Product;
import com.rafael.sales.domain.model.Sale;
import com.rafael.sales.domain.model.StatusSale;
import com.rafael.sales.domain.repository.ProductRepository;
import com.rafael.sales.domain.repository.ProductSaleRepository;
import com.rafael.sales.domain.repository.SaleRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
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

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final ProductSaleRepository productSaleRepository;

    @Transactional
    public Sale save(Sale sale) {
        sale.setDateRegister(OffsetDateTime.now());
        sale.setStatus(StatusSale.EMITIDA);

        if (sale.getItems().isEmpty()) {
            throw new BusinessException(NO_PRODUCT_LINKED);
        }

        List<Product> productList = new ArrayList<>();
        sale.getItems().forEach(itemSale -> {
            if (itemSale.getProduct().getId() == null)
                throw new EntityNotFoundException(PRODUCT_NOT_FOUND);

            Optional<Product> product = productRepository.findById(itemSale.getProduct().getId());

            if (product.get().getQuantity().compareTo(itemSale.getQuantity()) < 1) {
                throw new InsufficientStockException(String.format(INSUFFICIENT_STOCK, product.get().getName(), product.get().getQuantity()));
            }

            product.get().setQuantity(product.get().getQuantity().subtract(itemSale.getQuantity()));
            productList.add(product.get());
        });
        productRepository.saveAll(productList);
        return saleRepository.save(sale);
    }

    @Transactional
    public void edit(Sale sale) {
        Optional<Sale> saleEdit = saleRepository.findById(sale.getId());
        if (saleEdit.get().getStatus() == StatusSale.CANCELED) {
            throw new BusinessException("Não é possivel editar uma venda cancelada!");
        }

        saleEdit.ifPresent(value -> value.setStatus(sale.getStatus()));

        saleEdit.get().getItems().forEach(item -> {
            var ps = sale.getItems().stream().filter(i -> i.getProduct().getId().equals(item.getProduct().getId())).findFirst();
            if (ps.isPresent()) {
                if (!ps.get().getQuantity().equals(item.getQuantity())) {
                    Optional<Product> product = productRepository.findById(ps.get().getProduct().getId());
                    if (item.getQuantity().add(product.get().getQuantity()).compareTo(ps.get().getQuantity()) < 0) {
                        throw new InsufficientStockException("Quantidade de estoque insuficiente");
                    }

                    if (item.getQuantity().compareTo(ps.get().getQuantity()) != 0) {
                        if (item.getQuantity().compareTo(ps.get().getQuantity()) < 0) {
                            var quantity = ps.get().getQuantity().subtract(item.getQuantity());
                            product.get().setQuantity(product.get().getQuantity().subtract(quantity));
                        } else {
                            var quantity = item.getQuantity().subtract(ps.get().getQuantity());
                            product.get().setQuantity(quantity.add(product.get().getQuantity()));
                        }
                    }
                    System.out.println(item.getPrice());
                    item.setQuantity(ps.get().getQuantity());
                    productRepository.save(product.get());
                }
            }
        });

        Sale prod = saleRepository.save(saleEdit.get());
    }

    public Sale cancel(Sale sale) {
        Optional<Sale> saleCancel = saleRepository.findById(sale.getId());
        if (saleCancel.isEmpty() || saleCancel.get().getStatus().equals(StatusSale.CANCELED)) {
            throw new BusinessException("Está venda não existe!");
        }

        saleCancel.get().getItems().forEach(item -> {
            var ps = saleCancel.get().getItems().stream().filter(i -> i.getProduct().getId().equals(item.getProduct().getId())).findAny();
            Optional<Product> product = productRepository.findById(ps.get().getProduct().getId());
            product.get().setQuantity(product.get().getQuantity().add(ps.get().getQuantity()));

            productRepository.save(product.get());
        });

        saleCancel.get().setStatus(StatusSale.CANCELED);
        return saleRepository.save(saleCancel.get());
    }

}