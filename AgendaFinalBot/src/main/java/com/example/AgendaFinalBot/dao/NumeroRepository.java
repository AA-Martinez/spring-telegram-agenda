package com.example.AgendaFinalBot.dao;

import com.example.AgendaFinalBot.domain.Contacto;
import com.example.AgendaFinalBot.domain.Numero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NumeroRepository extends JpaRepository<Numero,Integer> {
    Numero findNumeroByIdContacto(int idContacto);
    Numero findNumeroByIdNumero(int idnumero);
    List<Numero> findAllByIdContacto(Contacto contacto);
    List<Numero> findAllByTelefonoStartsWithAndStatusIs(String num, int numero);
    Numero findNumeroByIdContactoAndIdNumero(int idContacto, int idnumero);

}
