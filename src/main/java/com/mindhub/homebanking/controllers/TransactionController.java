package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("api/transactions")
    public List<TransactionDTO> getTransactions() {
        return transactionRepository.findAll().stream().map(TransactionDTO::new).collect(toList());
    }

    @GetMapping("api/transactions/{id}")
    public TransactionDTO getTransaction(@PathVariable Long id) {
        return transactionRepository.findById(id).map(TransactionDTO::new).orElse(null);
    }

    @Transactional
    @PostMapping("api/transactions")
    public ResponseEntity<Object> makeTransaction(
            @RequestParam Double amount, @RequestParam String description,
            @RequestParam String fromAccountNumber, @RequestParam String toAccountNumber,
            Authentication authentication
    ) {
        Client client = clientRepository.findByEmail(authentication.getName());
        Account cuentaOrigen = accountRepository.findByNumber(fromAccountNumber);
        Account cuentaDestino = accountRepository.findByNumber(toAccountNumber);

        //Verificamos si todos los datos vienen correctamente, en caso contrario se devuelve un error
        if (amount.isNaN() || description.isEmpty() || fromAccountNumber.isEmpty() || toAccountNumber.isEmpty() ||
                fromAccountNumber.equals(toAccountNumber) || cuentaOrigen == null || cuentaDestino == null ||
                cuentaOrigen.getBalance() < amount || cuentaOrigen.getClient() != client
        ) {
            return new ResponseEntity<>("Datos incompletos, verificar", HttpStatus.FORBIDDEN);
        } else {
            transactionRepository.save(new Transaction(TransactionType.DEBIT, -amount,
                    description + " " + toAccountNumber, LocalDateTime.now(), cuentaOrigen));
            //Actualizamos el saldo de la cuenta origen
            cuentaOrigen.setBalance(cuentaOrigen.getBalance() - amount);

            transactionRepository.save(new Transaction(TransactionType.CREDIT, amount,
                    description + " " + fromAccountNumber, LocalDateTime.now(), cuentaDestino));
            //Actualizamos el saldo de la cuenta de destino
            cuentaDestino.setBalance(cuentaDestino.getBalance() + amount);

            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }
}
