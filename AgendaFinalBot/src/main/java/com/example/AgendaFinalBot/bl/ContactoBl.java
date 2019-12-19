package com.example.AgendaFinalBot.bl;

import com.example.AgendaFinalBot.app.AgendaFinalBot;
import com.example.AgendaFinalBot.dao.ContactoRepository;
import com.example.AgendaFinalBot.domain.Contacto;
import com.example.AgendaFinalBot.domain.Usuario;
import com.example.AgendaFinalBot.dto.Status;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ContactoBl {

    ContactoRepository contactoRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactoBl.class);


    @Autowired
    public ContactoBl(ContactoRepository contactoRepository) {
        this.contactoRepository = contactoRepository;
    }

    public Contacto findContactobyIdContacto(int id){
        Contacto contacto = contactoRepository.findContactoByIdContacto(id);
        return contacto;
    }

    public Contacto crearContacto(User user){
        Contacto nuevoContacto = new Contacto();
        nuevoContacto.setChatId(String.valueOf(user.getId()));
        nuevoContacto.setNombres("temporal");
        nuevoContacto.setApellidos("temporal");
        nuevoContacto.setFechaNacimiento("temporal");
        nuevoContacto.setCorreo("temporal");
        nuevoContacto.setImagen("temporal");
        nuevoContacto.setTxdate(new Date());
        nuevoContacto.setTxhost("localhost");
        nuevoContacto.setTxuser("admin");
        nuevoContacto.setStatus(Status.ACTIVE.getStatus());
        contactoRepository.save(nuevoContacto);

        return nuevoContacto;
    }

    public void mandarfoto(String chatid, String idimagen){
        AgendaFinalBot agendaFinalBot = new AgendaFinalBot();
        agendaFinalBot.mandarfoto(chatid,idimagen);
    }

    public List<Contacto> findAllByChatId(String chat){
        List<Contacto> contactoList = contactoRepository.findAllByChatId(chat);
        if(!contactoList.isEmpty()){
            return contactoList;
        }else{
            LOGGER.info("Sin contactos");
        }
        return null;
    }

    public List<Contacto> findAllByChatIdAndNombresContains (String chat, String nombre){
        List<Contacto> contactoList = contactoRepository.findAllByChatIdAndNombresContains(chat,nombre);
        if (contactoList != null){
            return contactoList;
        }else{
            return null;
        }
    }

    public List<Contacto> findAllByChatIdAndApellidosContains (String chat, String apellidos){
        List<Contacto> contactoList = contactoRepository.findAllByChatIdAndApellidosContains(chat,apellidos);
        if (contactoList != null){
            return contactoList;
        }else{
            return null;
        }
    }

}
