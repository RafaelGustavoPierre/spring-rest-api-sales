package com.rafael.sales.domain.repository;

import com.rafael.sales.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
