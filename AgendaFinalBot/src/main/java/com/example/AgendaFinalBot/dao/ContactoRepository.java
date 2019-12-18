package com.example.AgendaFinalBot.dao;

import com.example.AgendaFinalBot.domain.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

public interface ContactoRepository extends JpaRepository<Contacto,Integer> {
    Contacto findContactoByChatId(String chatid);
    Contacto findContactoByIdContacto(int id);
    List<Contacto> findAllByChatId(String chatid);
    List<Contacto> findAllByChatIdAndNombresContains(String chat, String nombre);
    List<Contacto> findAllByChatIdAndApellidosContains(String chat, String apellido);
    Contacto findContactoByIdContactoAndChatId(int id, String chat);


}
