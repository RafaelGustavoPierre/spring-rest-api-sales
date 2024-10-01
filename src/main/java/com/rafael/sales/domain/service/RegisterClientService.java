package com.rafael.sales.domain.service;

import com.rafael.sales.domain.exception.EntityNotFoundException;
import com.rafael.sales.domain.model.Client;
import com.rafael.sales.domain.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegisterClientService {

    public static final String CLIENT_NOT_FOUND = "Usuario de email %s não foi encontrado";
    private ClientRepository clientRepository;

    public Client findClientById(Long clientId) {
        return clientRepository.findById(clientId).orElseThrow(() -> new EntityNotFoundException(String.format(CLIENT_NOT_FOUND, clientId)));
    }

}
