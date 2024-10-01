package com.rafael.sales.domain.repository;

import com.rafael.sales.domain.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

////    @Query("from Client WHERE email = :email")
//    Optional<Client> findByEmail(String email);

}
