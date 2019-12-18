package com.example.AgendaFinalBot.bl;

import com.example.AgendaFinalBot.dao.ContactoRepository;
import com.example.AgendaFinalBot.dao.NumeroRepository;
import com.example.AgendaFinalBot.domain.Contacto;
import com.example.AgendaFinalBot.domain.Numero;
import com.example.AgendaFinalBot.dto.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class NumeroBl {

    NumeroRepository numeroRepository;
    ContactoRepository contactoRepository;

    @Autowired
    public NumeroBl(NumeroRepository numeroRepository, ContactoRepository contactoRepository) {
        this.numeroRepository = numeroRepository;
        this.contactoRepository = contactoRepository;
    }

    public Numero crearNumero(int idcontacto , String telefono){
        Contacto contacto = contactoRepository.findContactoByIdContacto(idcontacto);
        Numero numero = new Numero();
        numero.setIdContacto(contacto);
        numero.setTelefono(telefono);
        numero.setStatus(Status.ACTIVE.getStatus());
        numero.setTxdate(new Date());
        numero.setTxhost("localhost");
        numero.setTxuser("admin");
        numeroRepository.save(numero);
        return numero;
    }

    public List<Numero> findAllByIdContacto (Contacto contacto){
        List<Numero> numeroList = numeroRepository.findAllByIdContacto(contacto);
        if (numeroList != null){
            return numeroList;
        }
        return null;
    }

    public List<Numero> findAllByTelefonoStartsWith(String num){
        List<Numero> numeroList = numeroRepository.findAllByTelefonoStartsWith(num);
        if(numeroList!=null){
            return numeroList;
        }
        return null;
    }

    public Numero findNumeroByIdNumero(int num){
        Numero numero = numeroRepository.findNumeroByIdNumero(num);
        return numero;
    }
}
