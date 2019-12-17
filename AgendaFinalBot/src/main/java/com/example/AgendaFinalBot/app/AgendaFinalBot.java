package com.example.AgendaFinalBot.app;

import com.example.AgendaFinalBot.bl.ContactoBl;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class AgendaFinalBot extends TelegramLongPollingBot{

    ContactoBl contactoBl;

    @Autowired
    public AgendaFinalBot(ContactoBl contactoBl) {
        this.contactoBl = contactoBl;
    }

    public AgendaFinalBot(){}

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage()
                    .setChatId(update.getMessage().getChatId())
                    .setText("Hola");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "AgendaTelegramBot";
    }

    @Override
    public String getBotToken() {
        return "907718123:AAFcuvsA5F1d_WrLNtWCo18yk-enTFX1a3E";
    }
}
