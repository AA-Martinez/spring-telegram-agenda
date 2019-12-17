package com.example.AgendaFinalBot.bl;

import com.example.AgendaFinalBot.dao.UsuarioRepository;
import com.example.AgendaFinalBot.domain.Usuario;
import com.example.AgendaFinalBot.dto.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UsuarioBl {

    UsuarioRepository usuarioRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioBl.class);

    public UsuarioBl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario crearUsuario(Update update){
        Usuario usuario =usuarioRepository.findUsuarioByChatId(String.valueOf(update.getMessage().getChatId()));
        if(usuario == null){
            Usuario usuario1 = new Usuario();
            usuario1.setChatId(String.valueOf(update.getMessage().getFrom().getId()));
            usuario1.setLastMessageSent(update.getMessage().getText());
            usuario1.setLastMessageReceived("");
            usuario1.setTxhost("localhost");
            usuario1.setTxdate(new Date());
            usuario1.setTxuser("admin");
            usuario1.setStatus(Status.ACTIVE.getStatus());
            usuarioRepository.save(usuario1);
            return usuario1;
        }
        return usuario;
    }

    public void setlastMessageReceived(Update update, String messageReceived){
        User user = update.getMessage().getFrom();
        Usuario usuario = usuarioRepository.findUsuarioByChatId(String.valueOf(user.getId()));
        usuario.setLastMessageReceived(messageReceived);
    }

    public void setlastMessageSent(Update update, String messageSent){
        User user = update.getMessage().getFrom();
        Usuario usuario = usuarioRepository.findUsuarioByChatId(String.valueOf(user.getId()));
        usuario.setLastMessageSent(messageSent);
    }

    public void setlastCallbackReceived(Update update, String messageReceived){
        User user = update.getCallbackQuery().getFrom();
        Usuario usuario = usuarioRepository.findUsuarioByChatId(String.valueOf(user.getId()));
        usuario.setLastMessageReceived(messageReceived);
    }

    public void setlastCallbackSent(Update update, String messageSent){
        User user = update.getCallbackQuery().getFrom();
        Usuario usuario = usuarioRepository.findUsuarioByChatId(String.valueOf(user.getId()));
        usuario.setLastMessageSent(messageSent);
    }


    public SendMessage processUpdate(Update update){
        LOGGER.info("update {} ", update);
        Usuario usuario = crearUsuario(update);
        setlastMessageSent(update,update.getMessage().getText());
        SendMessage conversacion = continueChatWithUser(update, usuario);
        setlastMessageReceived(update, conversacion.getText());
        return conversacion;
    }

    private SendMessage continueChatWithUser(Update update,Usuario lastMessage) {

        SendMessage chatResponse = null;
        if (lastMessage == null){
            chatResponse.setChatId(lastMessage.getChatId()).setText("1");
        }else{
            String lastSent = lastMessage.getLastMessageSent();
            switch (lastSent){
                case "/start":
                    chatResponse = new SendMessage()
                            .setChatId(lastMessage.getChatId())
                            .setText("Bienvenido: "+ update.getMessage().getFrom().getId());

                    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                    List<InlineKeyboardButton> rowInline = new ArrayList<>();
                    rowInline.add(new InlineKeyboardButton().setText("Registrar mi numero").setCallbackData("minumero"));

                    rowsInline.add(rowInline);
                    markupInline.setKeyboard(rowsInline);
                    chatResponse.setReplyMarkup(markupInline);
                    break;
            }
            switch (lastMessage.getLastMessageReceived()){

            }
        }
        return chatResponse;
    }

    public EditMessageText processCallback(Update update){
        LOGGER.info("update {} ", update);
        Usuario usuario =usuarioRepository.findUsuarioByChatId(String.valueOf(update.getCallbackQuery().getFrom().getId()));
        setlastCallbackSent(update,update.getCallbackQuery().getData());
        EditMessageText conversacion = continueCallback (update, usuario);
        setlastCallbackReceived(update, conversacion.getText());
        return conversacion;
    }

    private EditMessageText continueCallback(Update update, Usuario usuario){
        Usuario lastMessage = usuarioRepository.findLastChatByUserId(usuario.getIdUsuario());
        EditMessageText chatResponse = null;
        if(lastMessage == null){
            chatResponse.setChatId(lastMessage.getChatId()).setText("1");
        }else{
            String lastSent = update.getCallbackQuery().getData();
            switch (lastSent){
                case "minumero":
                    chatResponse = new EditMessageText()
                            .setChatId(lastMessage.getChatId())
                            .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                            .setText("prueba");
                    break;
            }
        }
        return chatResponse;


    }




    }
