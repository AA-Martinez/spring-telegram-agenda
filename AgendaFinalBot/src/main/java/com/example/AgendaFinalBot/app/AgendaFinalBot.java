package com.example.AgendaFinalBot.app;

import com.example.AgendaFinalBot.bl.ContactoBl;
import com.example.AgendaFinalBot.bl.NumeroBl;
import com.example.AgendaFinalBot.bl.UsuarioBl;
import com.example.AgendaFinalBot.domain.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class AgendaFinalBot extends TelegramLongPollingBot{

    ContactoBl contactoBl;
    NumeroBl numeroBl;
    UsuarioBl usuarioBl;

    @Autowired
    public AgendaFinalBot(ContactoBl contactoBl, NumeroBl numeroBl, UsuarioBl usuarioBl) {
        this.contactoBl = contactoBl;
        this.numeroBl = numeroBl;
        this.usuarioBl = usuarioBl;

    }

    public AgendaFinalBot(){}

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = usuarioBl.processUpdate(update);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if(update.hasCallbackQuery()) {
            System.out.println(update.getCallbackQuery().getMessage().getText());
            EditMessageText messageText = usuarioBl.processCallback(update);
            try {
                execute(messageText);
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
