package com.example.AgendaFinalBot.dto;

import com.example.AgendaFinalBot.domain.Usuario;

public class UsuarioDto {

    int idUsuario;
    String chatid;
    String lastMessageSent;
    String lastMessageReceived;
    int status;

    public UsuarioDto(){}

    public UsuarioDto(Usuario usuario) {
        this.idUsuario = usuario.getIdUsuario();
        this.chatid = usuario.getChatId();
        this.lastMessageSent = usuario.getLastMessageSent();
        this.lastMessageReceived = usuario.getLastMessageReceived();
        this.status = usuario.getStatus();
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    public String getLastMessageSent() {
        return lastMessageSent;
    }

    public void setLastMessageSent(String lastMessageSent) {
        this.lastMessageSent = lastMessageSent;
    }

    public String getLastMessageReceived() {
        return lastMessageReceived;
    }

    public void setLastMessageReceived(String lastMessageReceived) {
        this.lastMessageReceived = lastMessageReceived;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
