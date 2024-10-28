package com.rafael.sales.domain.repository;

import com.rafael.sales.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("from User WHERE email = :email")
    Optional<User> findByEmail(String email);

}
