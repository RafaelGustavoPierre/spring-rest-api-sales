package com.rafael.sales.domain.repository;

import com.rafael.sales.domain.model.Product;
import com.rafael.sales.domain.model.ProductMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductMediaRepository extends JpaRepository<ProductMedia, Long> {

    @Query("SELECT pm.fileName FROM ProductMedia pm WHERE pm.product.id = :productId")
    String findByProductId(@Param("productId") Long productId);

}
