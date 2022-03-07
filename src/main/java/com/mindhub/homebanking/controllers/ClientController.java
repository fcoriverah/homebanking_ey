package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {

        ClientDTO client;
        client = clientRepository.findById(id).map(ClientDTO::new).orElse(null);
        return client;
    }

    @RequestMapping("/clients")
    public List<ClientDTO> getClients() {

        List<ClientDTO> clients;
        clients = clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
        return clients;
    }

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts() {

        List<AccountDTO> accounts;
        accounts = accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
        return accounts;
    }

}
