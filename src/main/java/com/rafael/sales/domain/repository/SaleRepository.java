package com.rafael.sales.domain.repository;

import com.rafael.sales.domain.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    public List<Sale> findTop20ByOrderByIdDesc();

}
