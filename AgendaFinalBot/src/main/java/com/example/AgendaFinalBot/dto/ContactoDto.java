package com.example.AgendaFinalBot.dto;

import com.example.AgendaFinalBot.domain.Contacto;

public class ContactoDto {

    int idContacto;
    String chatid;
    String nombres;
    String apellidos;
    String correo;
    String fecha_nacimiento;
    int status;

    public ContactoDto(){}

    public ContactoDto(Contacto contacto) {
        this.idContacto = contacto.getIdContacto();
        this.chatid = contacto.getChatId();
        this.nombres = contacto.getNombres();
        this.apellidos = contacto.getApellidos();
        this.correo = contacto.getCorreo();
        this.fecha_nacimiento = contacto.getFechaNacimiento();
        this.status = contacto.getStatus();
    }

    public int getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(int idContacto) {
        this.idContacto = idContacto;
    }

    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
