package com.rafael.sales.domain.service;

import com.rafael.sales.domain.exception.EntityNotFoundException;
import com.rafael.sales.domain.model.User;
import com.rafael.sales.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegisterUserService {

    public static final String USER_NOT_FOUND = "Usuario de código %s não foi encontrado";
    private UserRepository userRepository;

    public User userExists(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(String.format(USER_NOT_FOUND, userId)));
    }

}
