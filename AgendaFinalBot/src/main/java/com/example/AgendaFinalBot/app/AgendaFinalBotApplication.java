package com.example.AgendaFinalBot.app;

import com.example.AgendaFinalBot.bl.ContactoBl;
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

    @Autowired
    public AgendaFinalBotApplication() {
    }

    @PostConstruct
    public void init() {

        // TODO Initialize Api Context
        ApiContextInitializer.init();

        // TODO Instantiate Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();

        // TODO Register our bot
        try {
            botsApi.registerBot(new AgendaFinalBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}