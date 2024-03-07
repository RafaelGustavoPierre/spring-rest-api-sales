package com.rafael.sales.domain.repository;

import com.rafael.sales.domain.model.ProductSale;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSaleRepository extends JpaRepository<ProductSale, Long> {

//    @Query("SELECT * FROM product_sale WHERE id_sale = :idSale;")
//    List<ProductSale> findByIdSale(Long idSale);

}
