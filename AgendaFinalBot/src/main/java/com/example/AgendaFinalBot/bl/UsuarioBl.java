package com.example.AgendaFinalBot.bl;

import com.example.AgendaFinalBot.dao.ContactoRepository;
import com.example.AgendaFinalBot.dao.UsuarioRepository;
import com.example.AgendaFinalBot.domain.Contacto;
import com.example.AgendaFinalBot.domain.Numero;
import com.example.AgendaFinalBot.domain.Usuario;
import com.example.AgendaFinalBot.dto.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UsuarioBl {

    UsuarioRepository usuarioRepository;
    ContactoBl contactoBl;
    NumeroBl numeroBl;
    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioBl.class);

    @Autowired
    public UsuarioBl(UsuarioRepository usuarioRepository, ContactoBl contactoBl, NumeroBl numeroBl) {
        this.usuarioRepository = usuarioRepository;
        this.contactoBl = contactoBl;
        this.numeroBl = numeroBl;
    }

    public Usuario crearUsuario(Update update){
        Usuario usuario =usuarioRepository.findUsuarioByChatId(String.valueOf(update.getMessage().getChatId()));
        if(usuario == null){
            Usuario usuario1 = new Usuario();
            usuario1.setChatId(String.valueOf(update.getMessage().getFrom().getId()));
            usuario1.setLastMessageSent(update.getMessage().getText());
            usuario1.setLastMessageReceived("");
            usuario1.setTxhost("localhost");
            usuario1.setTxdate(new Date());
            usuario1.setTxuser("admin");
            usuario1.setStatus(Status.ACTIVE.getStatus());
            usuarioRepository.save(usuario1);
            return usuario1;
        }
        return usuario;
    }

    public void setlastMessageReceived(Update update, String messageReceived){
        User user = update.getMessage().getFrom();
        Usuario usuario = usuarioRepository.findUsuarioByChatId(String.valueOf(user.getId()));
        usuario.setLastMessageReceived(messageReceived);
    }

    public void setlastMessageSent(Update update, String messageSent){
        User user = update.getMessage().getFrom();
        Usuario usuario = usuarioRepository.findUsuarioByChatId(String.valueOf(user.getId()));
        usuario.setLastMessageSent(messageSent);
    }

    public void setlastCallbackReceived(Update update, String messageReceived){
        User user = update.getCallbackQuery().getFrom();
        Usuario usuario = usuarioRepository.findUsuarioByChatId(String.valueOf(user.getId()));
        usuario.setLastMessageReceived(messageReceived);
    }

    public void setlastCallbackSent(Update update, String messageSent){
        User user = update.getCallbackQuery().getFrom();
        Usuario usuario = usuarioRepository.findUsuarioByChatId(String.valueOf(user.getId()));
        usuario.setLastMessageSent(messageSent);
    }


    public SendMessage processUpdate(Update update){
        LOGGER.info("update {} ", update);
        Usuario usuario = crearUsuario(update);
        setlastMessageSent(update,update.getMessage().getText());
        SendMessage conversacion = continueChatWithUser(update, usuario);
        setlastMessageReceived(update, conversacion.getText());
        return conversacion;
    }

    private SendMessage continueChatWithUser(Update update,Usuario lastMessage) {

        SendMessage chatResponse = null;
        if (lastMessage == null){
            chatResponse.setChatId(lastMessage.getChatId()).setText("1");
        }else{
            String lastSent = lastMessage.getLastMessageSent();
            switch (lastSent){
                case "/start":
                    chatResponse = new SendMessage()
                            .setChatId(lastMessage.getChatId())
                            .setText("Bienvenido: "+ update.getMessage().getFrom().getId());

                    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                    List<InlineKeyboardButton> rowInline = new ArrayList<>();
                    rowInline.add(new InlineKeyboardButton().setText("Registrar mi numero").setCallbackData(";minumero"));

                    rowsInline.add(rowInline);
                    markupInline.setKeyboard(rowsInline);
                    chatResponse.setReplyMarkup(markupInline);
                    break;
                case "/agenda":
                    chatResponse = new SendMessage()
                            .setChatId(lastMessage.getChatId())
                            .setText("Que desea hacer?");

                    InlineKeyboardMarkup markupInlineagenda = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsInlineagenda = new ArrayList<>();
                    List<InlineKeyboardButton> rowInlineagenda = new ArrayList<>();
                    List<InlineKeyboardButton> rowInlineagenda1 = new ArrayList<>();
                    List<InlineKeyboardButton> rowInlineagenda2 = new ArrayList<>();

                    rowInlineagenda.add(new InlineKeyboardButton().setText("Ver todos los contactos").setCallbackData("verTodo"));
                    rowInlineagenda.add(new InlineKeyboardButton().setText("Agregar contacto").setCallbackData("minumero"));
                    rowInlineagenda1.add(new InlineKeyboardButton().setText("Buscar contacto").setCallbackData("buscar"));
                    rowInlineagenda1.add(new InlineKeyboardButton().setText("Modificar contacto").setCallbackData("modificar"));
                    rowInlineagenda2.add(new InlineKeyboardButton().setText("Borrar contacto").setCallbackData("borrar"));


                    rowsInlineagenda.add(rowInlineagenda);
                    rowsInlineagenda.add(rowInlineagenda1);
                    rowsInlineagenda.add(rowInlineagenda2);

                    markupInlineagenda.setKeyboard(rowsInlineagenda);
                    chatResponse.setReplyMarkup(markupInlineagenda);

                    break;
            }
            switch (lastMessage.getLastMessageReceived()){

            }
            if (lastMessage.getLastMessageReceived().contains("-")){
                String []conversacion = lastMessage.getLastMessageReceived().split("-");
                switch (conversacion[1]){
                    case "Ingrese nombres: ":
                        Contacto contactoNombre = this.contactoBl.crearContacto(update.getMessage().getFrom());
                        contactoNombre.setNombres(update.getMessage().getText());
                        chatResponse = new SendMessage()
                                .setChatId(lastMessage.getChatId())
                                .setText(contactoNombre.getIdContacto()+"-Ingrese apellidos: ");
                        break;
                    case "Ingrese apellidos: ":
                        Contacto contactoApellido = this.contactoBl.findContactobyIdContacto(Integer.parseInt(conversacion[0]));
                        contactoApellido.setApellidos(update.getMessage().getText());
                        chatResponse = new SendMessage()
                                .setChatId(lastMessage.getChatId())
                                .setText(contactoApellido.getIdContacto()+"-Ingrese fecha de nacimiento: (aaaa/mm/dd) ");
                        break;
                    case "Ingrese fecha de nacimiento: (aaaa/mm/dd) ":
                        Contacto contactoFecha = this.contactoBl.findContactobyIdContacto(Integer.parseInt(conversacion[0]));
                        contactoFecha.setFechaNacimiento(update.getMessage().getText());
                        chatResponse = new SendMessage()
                                .setChatId(lastMessage.getChatId())
                                .setText(contactoFecha.getIdContacto()+"-Ingrese correo electronico: ");
                        break;
                    case "Ingrese correo electronico: ":
                        Contacto contactoCorreo = this.contactoBl.findContactobyIdContacto(Integer.parseInt(conversacion[0]));
                        contactoCorreo.setCorreo(update.getMessage().getText());
                        chatResponse = new SendMessage()
                                .setChatId(lastMessage.getChatId())
                                .setText(contactoCorreo.getIdContacto()+"-Ingrese numero telefonico: ");
                        break;
                    case "Ingrese numero telefonico: ":
                        Contacto contactoNumero = this.contactoBl.findContactobyIdContacto(Integer.parseInt(conversacion[0]));
                        Numero nuevoNumero = this.numeroBl.crearNumero(contactoNumero.getIdContacto(),update.getMessage().getText());
                        chatResponse = new SendMessage()
                                .setChatId(lastMessage.getChatId())
                                .setText(contactoNumero.getIdContacto()+"Desea ingresar un nuevo numero? ");
                        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                        List<InlineKeyboardButton> rowInline = new ArrayList<>();
                        rowInline.add(new InlineKeyboardButton().setText("Si").setCallbackData(";nuevoNumero;"+conversacion[0]));
                        rowInline.add(new InlineKeyboardButton().setText("No").setCallbackData(";noNuevoNumero"));

                        rowsInline.add(rowInline);
                        markupInline.setKeyboard(rowsInline);
                        chatResponse.setReplyMarkup(markupInline);
                        break;
                    case "Ingrese nombre del contacto a buscar: ":
                        List<Contacto> contactoList = this.contactoBl.findAllByChatIdAndNombresContains(String.valueOf(update.getMessage().getFrom().getId()),update.getMessage().getText());
                        chatResponse = new SendMessage()
                                .setChatId(lastMessage.getChatId())
                                .setText("Contactos encontrados: ");
                        InlineKeyboardMarkup markupInlineNombres = new InlineKeyboardMarkup();
                        List<List<InlineKeyboardButton>> rowsInlineNombres = new ArrayList<>();
                        List<InlineKeyboardButton> rowInlineNombres = new ArrayList<>();
                        for (Contacto contacto : contactoList){
                            rowInlineNombres.add(new InlineKeyboardButton().setText(contacto.getNombres()+" "+contacto.getApellidos()).setCallbackData(";contactoSeleccionado;"+contacto.getIdContacto()));
                        }
                        rowsInlineNombres.add(rowInlineNombres);
                        markupInlineNombres.setKeyboard(rowsInlineNombres);
                        chatResponse.setReplyMarkup(markupInlineNombres);
                        break;
                    case "Ingrese apellidos del contacto a buscar: ":
                        List<Contacto> contactoList1 = this.contactoBl.findAllByChatIdAndApellidosContains(String.valueOf(update.getMessage().getFrom().getId()),update.getMessage().getText());
                        chatResponse = new SendMessage()
                                .setChatId(lastMessage.getChatId())
                                .setText("Contactos encontrados: ");
                        InlineKeyboardMarkup markupInlineApellidos = new InlineKeyboardMarkup();
                        List<List<InlineKeyboardButton>> rowsInlineApellidos = new ArrayList<>();
                        List<InlineKeyboardButton> rowInlineApellidos = new ArrayList<>();
                        for (Contacto contacto : contactoList1){
                            rowInlineApellidos.add(new InlineKeyboardButton().setText(contacto.getNombres()+" "+contacto.getApellidos()).setCallbackData(";contactoSeleccionado;"+contacto.getIdContacto()));
                        }
                        rowsInlineApellidos.add(rowInlineApellidos);
                        markupInlineApellidos.setKeyboard(rowsInlineApellidos);
                        chatResponse.setReplyMarkup(markupInlineApellidos);
                        break;
                    case "Ingrese numero telefonico del contacto a buscar: ":
                        List<Contacto> contactoList2 = this.contactoBl.findAllByChatId(String.valueOf(update.getMessage().getFrom().getId()));
                        List<Numero> numeroList = this.numeroBl.findAllByTelefonoStartsWith(update.getMessage().getText());
                        chatResponse = new SendMessage()
                                .setChatId(lastMessage.getChatId())
                                .setText("Contactos encontrados: ");

                        System.out.println(contactoList2);
                        System.out.println(numeroList);

                        InlineKeyboardMarkup markupInlineNumeros = new InlineKeyboardMarkup();
                        List<List<InlineKeyboardButton>> rowsInlineNumeros = new ArrayList<>();
                        List<InlineKeyboardButton> rowInlineNumeros = new ArrayList<>();
                        for (Numero numero : numeroList){
                            System.out.println(numero.getTelefono());
                            System.out.println(numero.getIdContacto().getChatId());
                            if (numero.getIdContacto().getChatId().equals(String.valueOf(update.getMessage().getFrom().getId()))){
                                rowInlineNumeros.add(new InlineKeyboardButton().setText(numero.getTelefono().toString()+" "+numero.getIdContacto().getNombres()+" "+numero.getIdContacto().getApellidos()).setCallbackData(";contactoSeleccionado;"+numero.getIdContacto().getIdContacto()));

                            }

                        }
                        rowsInlineNumeros.add(rowInlineNumeros);
                        markupInlineNumeros.setKeyboard(rowsInlineNumeros);
                        chatResponse.setReplyMarkup(markupInlineNumeros);

                        break;
                }
            }
        }
        return chatResponse;
    }

    public EditMessageText processCallback(Update update){
        LOGGER.info("update {} ", update);
        Usuario usuario =usuarioRepository.findUsuarioByChatId(String.valueOf(update.getCallbackQuery().getFrom().getId()));
        setlastCallbackSent(update,update.getCallbackQuery().getData());
        EditMessageText conversacion = continueCallback (update, usuario);
        setlastCallbackReceived(update, conversacion.getText());
        return conversacion;
    }

    private EditMessageText continueCallback(Update update, Usuario usuario){
        Usuario lastMessage = usuarioRepository.findLastChatByUserId(usuario.getIdUsuario());
        EditMessageText chatResponse = null;
        if(lastMessage == null){
            chatResponse.setChatId(lastMessage.getChatId()).setText("1");
        }else{
            String lastSent = update.getCallbackQuery().getData();
            if (lastSent.contains(";")){
                String []conversacion =lastSent.split(";");
                switch (conversacion[1]){
                    case "nuevoNumero":
                        chatResponse = new EditMessageText()
                                .setChatId(lastMessage.getChatId())
                                .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                                .setText(conversacion[2]+"-Ingrese numero telefonico: ");
                        break;
                    case "noNuevoNumero":
                        chatResponse = new EditMessageText()
                                .setChatId(lastMessage.getChatId())
                                .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                                .setText("Contacto registrado con exito.");
                        break;
                    case "contactoSeleccionado":
                        chatResponse = new EditMessageText()
                                .setChatId(lastMessage.getChatId())
                                .setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                        String datos = "Datos del contacto: \n";
                        Contacto contacto = this.contactoBl.findContactobyIdContacto(Integer.parseInt(conversacion[2]));
                        datos+="Nombre: "+contacto.getNombres()+ " "+contacto.getApellidos()+"\nCorreo: "+contacto.getCorreo()+"\nFecha nacimiento: "+contacto.getFechaNacimiento();
                        List<Numero> numeroList = this.numeroBl.findAllByIdContacto(contacto);
                        for (Numero numero : numeroList){
                            datos += "\nNumero: "+numero.getTelefono();
                        }
                        chatResponse.setText(datos);

                }
            }
            switch (lastSent){
                case "minumero":
                    chatResponse = new EditMessageText()
                            .setChatId(lastMessage.getChatId())
                            .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                            .setText("-Ingrese nombres: ");
                    break;
                case "verTodo":
                    List<Contacto> contactoList = contactoBl.findAllByChatId(String.valueOf(update.getCallbackQuery().getFrom().getId()));
                    chatResponse = new EditMessageText()
                            .setChatId(update.getCallbackQuery().getMessage().getChatId())
                            .setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                    if(contactoList == null){
                        chatResponse.setText("No cuenta con contactos.");
                    }else{
                        chatResponse.setText("Contactos: ");
                        InlineKeyboardMarkup markupInlineLista = new InlineKeyboardMarkup();
                        List<List<InlineKeyboardButton>> rowsInlineLista = new ArrayList<>();
                        List<InlineKeyboardButton> rowInlineLista = new ArrayList<>();
                        for (Contacto contacto : contactoList){
                            rowInlineLista.add(new InlineKeyboardButton().setText(contacto.getNombres()+" "+contacto.getApellidos()).setCallbackData(";contactoSeleccionado;"+contacto.getIdContacto()));

                        }
                        rowsInlineLista.add(rowInlineLista);

                        markupInlineLista.setKeyboard(rowsInlineLista);
                        chatResponse.setReplyMarkup(markupInlineLista);
                    }

                    break;
                case "buscar":
                    chatResponse = new EditMessageText()
                            .setChatId(update.getCallbackQuery().getMessage().getChatId())
                            .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                            .setText("Como desea buscar: ");

                    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                    List<InlineKeyboardButton> rowInline = new ArrayList<>();
                    rowInline.add(new InlineKeyboardButton().setText("Por nombres").setCallbackData("buscarNombres"));
                    rowInline.add(new InlineKeyboardButton().setText("Por apellidos").setCallbackData("buscarApellidos"));
                    rowInline.add(new InlineKeyboardButton().setText("Por numero").setCallbackData("buscarNumero"));

                    rowsInline.add(rowInline);
                    markupInline.setKeyboard(rowsInline);
                    chatResponse.setReplyMarkup(markupInline);
                    break;
                case "buscarNombres":
                    chatResponse = new EditMessageText()
                            .setChatId(update.getCallbackQuery().getMessage().getChatId())
                            .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                            .setText("-Ingrese nombre del contacto a buscar: ");
                    break;
                case "buscarApellidos":
                    chatResponse = new EditMessageText()
                            .setChatId(update.getCallbackQuery().getMessage().getChatId())
                            .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                            .setText("-Ingrese apellidos del contacto a buscar: ");
                    break;
                case "buscarNumero":
                    chatResponse =  new EditMessageText()
                            .setChatId(update.getCallbackQuery().getMessage().getChatId())
                            .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                            .setText("-Ingrese numero telefonico del contacto a buscar: ");
                    break;
            }
        }
        return chatResponse;


    }




    }
