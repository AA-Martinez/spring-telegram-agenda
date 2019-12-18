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
                            if (contacto.getStatus()==1){
                                rowInlineNombres.add(new InlineKeyboardButton().setText(contacto.getNombres()+" "+contacto.getApellidos()).setCallbackData(";contactoSeleccionado;"+contacto.getIdContacto()));
                            }
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
                            if (contacto.getStatus()==1){
                                rowInlineApellidos.add(new InlineKeyboardButton().setText(contacto.getNombres()+" "+contacto.getApellidos()).setCallbackData(";contactoSeleccionado;"+contacto.getIdContacto()));
                            }
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
                            if (numero.getIdContacto().getChatId().equals(String.valueOf(update.getMessage().getFrom().getId())) && numero.getIdContacto().getStatus() == 1){
                                rowInlineNumeros.add(new InlineKeyboardButton().setText(numero.getTelefono().toString()+" "+numero.getIdContacto().getNombres()+" "+numero.getIdContacto().getApellidos()).setCallbackData(";contactoSeleccionado;"+numero.getIdContacto().getIdContacto()));

                            }

                        }
                        rowsInlineNumeros.add(rowInlineNumeros);
                        markupInlineNumeros.setKeyboard(rowsInlineNumeros);
                        chatResponse.setReplyMarkup(markupInlineNumeros);

                        break;
                    case "Ingrese nuevos nombres: ":
                        Contacto contacto = this.contactoBl.findContactobyIdContacto(Integer.parseInt(conversacion[0]));
                        contacto.setNombres(update.getMessage().getText());
                        chatResponse = new SendMessage()
                                .setChatId(lastMessage.getChatId())
                                .setText("Nombre modificado exitosamente");
                        break;
                    case "Ingrese nuevos apellidos: ":
                        Contacto contactoNuevoApellido = this.contactoBl.findContactobyIdContacto(Integer.parseInt(conversacion[0]));
                        contactoNuevoApellido.setApellidos(update.getMessage().getText());
                        chatResponse = new SendMessage()
                                .setChatId(lastMessage.getChatId())
                                .setText("Apellido modificado exitosamente");
                        break;
                    case "Ingrese nuevo correo: ":
                        Contacto contactoNuevoCorreo = this.contactoBl.findContactobyIdContacto(Integer.parseInt(conversacion[0]));
                        contactoNuevoCorreo.setCorreo(update.getMessage().getText());
                        chatResponse = new SendMessage()
                                .setChatId(lastMessage.getChatId())
                                .setText("Correo modificado exitosamente");
                        break;
                    case "Ingrese nuevo fecha de nacimiento: ":
                        Contacto contactoNuevoFecha = this.contactoBl.findContactobyIdContacto(Integer.parseInt(conversacion[0]));
                        contactoNuevoFecha.setFechaNacimiento(update.getMessage().getText());
                        chatResponse = new SendMessage()
                                .setChatId(lastMessage.getChatId())
                                .setText("Fecha de nacimiento modificada exitosamente");
                        break;
                    case "Ingrese nuevo numero: ":
                        Numero numero = this.numeroBl.findNumeroByIdNumero(Integer.parseInt(conversacion[0]));
                        numero.setTelefono(update.getMessage().getText());
                        chatResponse = new SendMessage()
                                .setChatId(lastMessage.getChatId())
                                .setText("Numero modificado exitosamente.");
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
                        break;
                    case "contactoSeleccionadoBorrar":
                        Contacto contacto1 = this.contactoBl.findContactobyIdContacto(Integer.parseInt(conversacion[2]));
                        contacto1.setStatus(Status.INACTIVE.getStatus());
                        chatResponse = new EditMessageText()
                                .setChatId(lastMessage.getChatId())
                                .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                                .setText("Contacto eliminado.");
                        break;
                    case "contactoSeleccionadoModificar":
                        chatResponse = new EditMessageText()
                                .setChatId(lastMessage.getChatId())
                                .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                                .setText("Que desea modificar?");

                        InlineKeyboardMarkup markupInlineModificar = new InlineKeyboardMarkup();
                        List<List<InlineKeyboardButton>> rowsInlineModificar = new ArrayList<>();
                        List<InlineKeyboardButton> rowInlineModificar = new ArrayList<>();
                        List<InlineKeyboardButton> rowInlineModificar1 = new ArrayList<>();
                        List<InlineKeyboardButton> rowInlineModificar2 = new ArrayList<>();

                        rowInlineModificar.add(new InlineKeyboardButton().setText("Nombres").setCallbackData(";modificarNombre;"+conversacion[2]));
                        rowInlineModificar.add(new InlineKeyboardButton().setText("Apellidos").setCallbackData(";modificarApellidos;"+conversacion[2]));
                        rowInlineModificar1.add(new InlineKeyboardButton().setText("Correo").setCallbackData(";modificarCorreo;"+conversacion[2]));
                        rowInlineModificar1.add(new InlineKeyboardButton().setText("Fecha nacimiento").setCallbackData(";modificarFechaNacimiento;"+conversacion[2]));
                        rowInlineModificar2.add(new InlineKeyboardButton().setText("Numero(s)").setCallbackData(";modificarNumero;"+conversacion[2]));

                        rowsInlineModificar.add(rowInlineModificar);
                        rowsInlineModificar.add(rowInlineModificar1);
                        rowsInlineModificar.add(rowInlineModificar2);

                        markupInlineModificar.setKeyboard(rowsInlineModificar);
                        chatResponse.setReplyMarkup(markupInlineModificar);
                        break;
                    case "modificarNombre":
                        chatResponse = new EditMessageText()
                                .setChatId(lastMessage.getChatId())
                                .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                                .setText(conversacion[2]+"-Ingrese nuevos nombres: ");
                        break;
                    case "modificarApellidos":
                        chatResponse = new EditMessageText()
                                .setChatId(lastMessage.getChatId())
                                .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                                .setText(conversacion[2]+"-Ingrese nuevos apellidos: ");
                        break;
                    case "modificarCorreo":
                        chatResponse = new EditMessageText()
                                .setChatId(lastMessage.getChatId())
                                .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                                .setText(conversacion[2]+"-Ingrese nuevo correo: ");
                        break;
                    case "modificarFechaNacimiento":
                        chatResponse = new EditMessageText()
                                .setChatId(lastMessage.getChatId())
                                .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                                .setText(conversacion[2]+"-Ingrese nuevo fecha de nacimiento: ");
                        break;
                    case "modificarNumero":
                        chatResponse = new EditMessageText()
                                .setChatId(lastMessage.getChatId())
                                .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                                .setText("Seleccione el numero a modificar: ");
                        Contacto contacto2 = this.contactoBl.findContactobyIdContacto(Integer.parseInt(conversacion[2]));
                        List<Numero> numeroList1 = this.numeroBl.findAllByIdContacto(contacto2);

                        InlineKeyboardMarkup markupInlineModificarNumeros = new InlineKeyboardMarkup();
                        List<List<InlineKeyboardButton>> rowsInlineModificarNumeros = new ArrayList<>();
                        List<InlineKeyboardButton> rowInlineModificarNumeros = new ArrayList<>();
                        for(Numero numero : numeroList1){
                            rowInlineModificarNumeros.add(new InlineKeyboardButton().setText(numero.getTelefono()).setCallbackData(";modificarNumeroPro;"+numero.getIdNumero()));
                        }
                        rowsInlineModificarNumeros.add(rowInlineModificarNumeros);

                        markupInlineModificarNumeros.setKeyboard(rowsInlineModificarNumeros);
                        chatResponse.setReplyMarkup(markupInlineModificarNumeros);
                        break;
                    case "modificarNumeroPro":
                        chatResponse = new EditMessageText()
                                .setChatId(lastMessage.getChatId())
                                .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                                .setText(conversacion[2]+"-Ingrese nuevo numero: ");
                        break;

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
                            if (contacto.getStatus()==1){
                                rowInlineLista.add(new InlineKeyboardButton().setText(contacto.getNombres()+" "+contacto.getApellidos()).setCallbackData(";contactoSeleccionado;"+contacto.getIdContacto()));

                            }
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
                case "borrar":
                    List<Contacto> contactoList1 = contactoBl.findAllByChatId(String.valueOf(update.getCallbackQuery().getFrom().getId()));
                    chatResponse = new EditMessageText()
                            .setChatId(update.getCallbackQuery().getMessage().getChatId())
                            .setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                    if(contactoList1 == null){
                        chatResponse.setText("No cuenta con contactos.");
                    }else{
                        chatResponse.setText("Contactos: ");
                        InlineKeyboardMarkup markupInlineLista = new InlineKeyboardMarkup();
                        List<List<InlineKeyboardButton>> rowsInlineLista = new ArrayList<>();
                        List<InlineKeyboardButton> rowInlineLista = new ArrayList<>();
                        for (Contacto contacto : contactoList1){
                            if (contacto.getStatus()==1){
                                rowInlineLista.add(new InlineKeyboardButton().setText(contacto.getNombres()+" "+contacto.getApellidos()).setCallbackData(";contactoSeleccionadoBorrar;"+contacto.getIdContacto()));

                            }
                        }
                        rowsInlineLista.add(rowInlineLista);

                        markupInlineLista.setKeyboard(rowsInlineLista);
                        chatResponse.setReplyMarkup(markupInlineLista);
                    }
                    break;
                case "modificar":
                    List<Contacto> contactoList2 = contactoBl.findAllByChatId(String.valueOf(update.getCallbackQuery().getFrom().getId()));
                    chatResponse = new EditMessageText()
                            .setChatId(update.getCallbackQuery().getMessage().getChatId())
                            .setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                    if(contactoList2 == null){
                        chatResponse.setText("No cuenta con contactos.");
                    }else{
                        chatResponse.setText("Contactos: ");
                        InlineKeyboardMarkup markupInlineLista = new InlineKeyboardMarkup();
                        List<List<InlineKeyboardButton>> rowsInlineLista = new ArrayList<>();
                        List<InlineKeyboardButton> rowInlineLista = new ArrayList<>();
                        for (Contacto contacto : contactoList2){
                            if (contacto.getStatus()==1){
                                rowInlineLista.add(new InlineKeyboardButton().setText(contacto.getNombres()+" "+contacto.getApellidos()).setCallbackData(";contactoSeleccionadoModificar;"+contacto.getIdContacto()));

                            }
                        }
                        rowsInlineLista.add(rowInlineLista);

                        markupInlineLista.setKeyboard(rowsInlineLista);
                        chatResponse.setReplyMarkup(markupInlineLista);
                    }
                    break;
            }
        }
        return chatResponse;


    }




    }
