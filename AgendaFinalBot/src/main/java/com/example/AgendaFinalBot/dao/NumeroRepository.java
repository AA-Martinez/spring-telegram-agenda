package com.example.AgendaFinalBot.dao;

import com.example.AgendaFinalBot.domain.Numero;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NumeroRepository extends JpaRepository<Numero,Integer> {
    Numero findNumeroByIdContacto(int idContacto);
    Numero findNumeroByIdNumero(int idnumero);
}
