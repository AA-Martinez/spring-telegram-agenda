package com.example.AgendaFinalBot.dto;

import com.example.AgendaFinalBot.domain.Numero;

public class NumeroDto {

    int idNumero;
    String telefono;
    int Contacto_idContacto;
    int status;

    public NumeroDto(){}

    public NumeroDto(Numero numero) {
        this.idNumero = numero.getIdNumero();
        this.telefono = numero.getTelefono();
        Contacto_idContacto = numero.getIdContacto().getIdContacto();
        this.status = numero.getStatus();
    }

    public int getIdNumero() {
        return idNumero;
    }

    public void setIdNumero(int idNumero) {
        this.idNumero = idNumero;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getContacto_idContacto() {
        return Contacto_idContacto;
    }

    public void setContacto_idContacto(int contacto_idContacto) {
        Contacto_idContacto = contacto_idContacto;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
