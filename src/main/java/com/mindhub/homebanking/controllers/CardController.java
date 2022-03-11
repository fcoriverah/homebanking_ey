package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

@RestController
public class CardController {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ClientRepository clientRepository;

    @PostMapping( "api/clients/current/cards")
    public ResponseEntity<Object> registerCard(
            @RequestParam CardColor cardColor, @RequestParam CardType cardType, Authentication authentication
    ) {
        Client client = clientRepository.findByEmail(authentication.getName());

        String numTarjeta;
        int cvv;
        int cantidadTarjetas = cardRepository.countByClientAndCardType(client, cardType);

        if (cantidadTarjetas >= 3) {
            return new ResponseEntity<>("No puedes tener mas de 3 tarjetas del mismo tipo", HttpStatus.FORBIDDEN);
        } else {

            cvv = (int) Math.floor(Math.random() * (999 - 100 + 1) + 100);

            numTarjeta = String.valueOf((new Random()).nextInt(5999) + 4000) + "-" +
                    String.valueOf((new Random()).nextInt(9000) + 1000) + "-" +
                    String.valueOf((new Random()).nextInt(9000) + 1000) + "-" +
                    String.valueOf((new Random()).nextInt(9000) + 1000);

            cardRepository.save(new Card(
                    cardColor, cardType, numTarjeta, cvv,
                    client.getFirstName() + " " + client.getLastName(),
                    LocalDateTime.now().plusYears(5), LocalDateTime.now(), client));
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }
}
