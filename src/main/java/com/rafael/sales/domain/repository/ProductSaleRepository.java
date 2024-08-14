package com.rafael.sales.domain.repository;

import com.rafael.sales.domain.model.ProductSale;
import com.rafael.sales.domain.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSaleRepository extends JpaRepository<ProductSale, Long> {

    List<ProductSale> findBySale(Sale sale);

}