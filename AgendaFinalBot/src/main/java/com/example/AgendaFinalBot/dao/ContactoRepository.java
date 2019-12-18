package com.example.AgendaFinalBot.dao;

import com.example.AgendaFinalBot.domain.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactoRepository extends JpaRepository<Contacto,Integer> {
    Contacto findContactoByChatId(String chatid);
    Contacto findContactoByIdContacto(int id);
}
