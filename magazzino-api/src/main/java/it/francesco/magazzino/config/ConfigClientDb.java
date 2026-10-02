package it.francesco.magazzino.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.francesco.database.ArticoliDbUtil;
import it.francesco.database.LocazioniDbUtil;
import it.francesco.database.MovimentoDbUtil;
import it.francesco.database.WArticoliPresentiDbUtil;
import it.francesco.database.WOccupazioneLocazioneDbUtil;

@Configuration

public class ConfigClientDb {
    @Value("${db.host}")
    private String host;
    @Value("${db.dbName}")
    private String dbName;
    @Value("${db.username}")
    private String username;
    @Value("${db.password}")
    private String password;
    @Value("${db.portNumber}")
    private int portNumber;

    @Bean
    public ArticoliDbUtil articoliDbUtil() {
        return new ArticoliDbUtil(host, dbName, username, password, portNumber);
    }

    @Bean
    public WOccupazioneLocazioneDbUtil wOccupazioneLocazioneDbUtil() {
        return new WOccupazioneLocazioneDbUtil(host, dbName, username, password, portNumber);

    }

    @Bean
    public WArticoliPresentiDbUtil wArticoliPresentiDbUtil() {
        return new WArticoliPresentiDbUtil(host, dbName, username, password, portNumber);

    }

    @Bean
    public MovimentoDbUtil movimentoDbUtil() {
        return new MovimentoDbUtil(host, dbName, username, password, portNumber);

    }

    @Bean
    public LocazioniDbUtil locazioniDbUtil() {
        return new LocazioniDbUtil(host, dbName, username, password, portNumber);

    }

}
