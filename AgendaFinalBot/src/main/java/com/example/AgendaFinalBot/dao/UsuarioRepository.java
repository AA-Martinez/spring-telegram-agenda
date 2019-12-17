package com.example.AgendaFinalBot.dao;

import com.example.AgendaFinalBot.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {
    Usuario findUsuarioByChatId(String chatid);
    @Query(value = "SELECT * FROM usuario where  id_usuario = ? ORDER BY chat_id DESC LIMIT 1", nativeQuery = true)
    Usuario findLastChatByUserId(Integer userId);
}
