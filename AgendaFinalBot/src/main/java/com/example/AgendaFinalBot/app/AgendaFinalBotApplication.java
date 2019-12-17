package com.example.AgendaFinalBot.app;

import com.example.AgendaFinalBot.bl.ContactoBl;
import com.example.AgendaFinalBot.bl.NumeroBl;
import com.example.AgendaFinalBot.bl.UsuarioBl;
import com.example.AgendaFinalBot.domain.Numero;
import com.example.AgendaFinalBot.domain.Usuario;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;


@Component
public class AgendaFinalBotApplication{

    ContactoBl contactoBl;
    NumeroBl numeroBl;
    UsuarioBl usuarioBl;

    @Autowired
    public AgendaFinalBotApplication(ContactoBl contactoBl, NumeroBl numeroBl, UsuarioBl usuarioBl) {
        this.contactoBl = contactoBl;
        this.numeroBl = numeroBl;
        this.usuarioBl = usuarioBl;

    }

    @PostConstruct
    public void init() {

        // TODO Initialize Api Context
        ApiContextInitializer.init();

        // TODO Instantiate Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();

        // TODO Register our bot
        try {
            botsApi.registerBot(new AgendaFinalBot(contactoBl,numeroBl,usuarioBl));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}