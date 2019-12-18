package com.example.AgendaFinalBot.bl;

import com.example.AgendaFinalBot.dao.ContactoRepository;
import com.example.AgendaFinalBot.domain.Contacto;
import com.example.AgendaFinalBot.domain.Usuario;
import com.example.AgendaFinalBot.dto.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Date;

@Service
@Transactional
public class ContactoBl {

    ContactoRepository contactoRepository;

    @Autowired
    public ContactoBl(ContactoRepository contactoRepository) {
        this.contactoRepository = contactoRepository;
    }

    public Contacto findContactobyIdContacto(int id){
        Contacto contacto = contactoRepository.findContactoByIdContacto(id);
        return contacto;
    }

    public Contacto crearContacto(User user){
        Contacto contacto = contactoRepository.findContactoByChatId(String.valueOf(user.getId()));
        if(contacto == null){
            Contacto nuevoContacto = new Contacto();
            nuevoContacto.setChatId(String.valueOf(user.getId()));
            nuevoContacto.setNombres("temporal");
            nuevoContacto.setApellidos("temporal");
            nuevoContacto.setFechaNacimiento("temporal");
            nuevoContacto.setCorreo("temporal");
            nuevoContacto.setTxdate(new Date());
            nuevoContacto.setTxhost("localhost");
            nuevoContacto.setTxuser("admin");
            nuevoContacto.setStatus(Status.ACTIVE.getStatus());
            contactoRepository.save(nuevoContacto);

            return nuevoContacto;
        }
        return null;
    }
}
