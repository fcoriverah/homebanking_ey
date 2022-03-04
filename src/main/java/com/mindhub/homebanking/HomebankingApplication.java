package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;


@SpringBootApplication
public class HomebankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository) {
        return (args) -> {
            //guardar cliente en el repositorio
            Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
            clientRepository.save(client1);

            Client client2 = new Client("Francisco", "Rivera", "franciscorivera@gmail.com");
            clientRepository.save(client2);

            //guardar cuentas en el repositorio
            Account account1 = new Account("VIN001", LocalDateTime.now(),5000, client1);
            accountRepository.save(account1);

            Account account2 = new Account("VIN002", LocalDateTime.now().plusDays(1),7500, client1);
            accountRepository.save(account2);

            Account account3 = new Account("VIN232", LocalDateTime.now(),2300, client2);
            accountRepository.save(account3);

            Account account4 = new Account("VIN324", LocalDateTime.now().plusDays(4),3000, client2);
            accountRepository.save(account4);

            Account account5 = new Account("VIN945", LocalDateTime.now(),8000, client2);
            accountRepository.save(account5);
        };
    }

}
