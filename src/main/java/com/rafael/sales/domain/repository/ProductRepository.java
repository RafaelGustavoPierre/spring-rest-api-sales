package com.rafael.sales.domain.repository;

import com.rafael.sales.domain.model.Product;
import com.rafael.sales.domain.model.ProductSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
