package com.rafael.sales.domain.repository;

import com.rafael.sales.domain.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("from Sale WHERE code = :code")
    Optional<Sale> findByCode(@Param("code") String code);

    @Query("from Sale WHERE status = 'EMITIDA'")
    List<Sale> findEmitidos();

    boolean existsByCode(String code);

}
