package com.rafael.sales.domain.service;

import com.rafael.sales.domain.exception.SaleException;
import com.rafael.sales.domain.model.Product;
import com.rafael.sales.domain.model.ProductSale;
import com.rafael.sales.domain.model.Sale;
import com.rafael.sales.domain.model.StatusSale;
import com.rafael.sales.domain.repository.ProductRepository;
import com.rafael.sales.domain.repository.ProductSaleRepository;
import com.rafael.sales.domain.repository.SaleRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class RegisterSaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final ProductSaleRepository productSaleRepository;

    @Transactional
    public Sale save(Sale sale) {
        sale.setDateRegister(OffsetDateTime.now());

        sale.getItems().forEach(itemSale -> {
            Optional<Product> product = productRepository.findById(itemSale.getProduct().getId());
            if (product.get().getQuantity().compareTo(itemSale.getQuantity()) < 1)
                throw new SaleException("Não há estóque suficiente para a venda do produto " + product.get().getName() + ", quantidade em estoque: " + product.get().getQuantity());

            product.get().setQuantity(product.get().getQuantity().subtract(itemSale.getQuantity()));
            productRepository.save(product.get());
        });
        return saleRepository.save(sale);
    }

    @Transactional
    public void edit(Sale sale) {
        Optional<Sale> saleEdit = saleRepository.findById(sale.getId());
        List<ProductSale> productSale = productSaleRepository.findAll();

        if (sale.getStatus() != null) {
            saleEdit.get().setStatus(sale.getStatus());
        }

        System.out.println(sale.toString());

//        for (ProductSale item : productSale) {
////            if (item.getId().equals())
//            System.out.println("1");
//        }

    }
}
