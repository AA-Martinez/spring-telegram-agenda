/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.AgendaFinalBot.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ale_m
 */
@Entity
@Table(name = "usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findByIdUsuario", query = "SELECT u FROM Usuario u WHERE u.idUsuario = :idUsuario"),
    @NamedQuery(name = "Usuario.findByChatId", query = "SELECT u FROM Usuario u WHERE u.chatId = :chatId"),
    @NamedQuery(name = "Usuario.findByLastMessageSent", query = "SELECT u FROM Usuario u WHERE u.lastMessageSent = :lastMessageSent"),
    @NamedQuery(name = "Usuario.findByLastMessageReceived", query = "SELECT u FROM Usuario u WHERE u.lastMessageReceived = :lastMessageReceived"),
    @NamedQuery(name = "Usuario.findByTxuser", query = "SELECT u FROM Usuario u WHERE u.txuser = :txuser"),
    @NamedQuery(name = "Usuario.findByTxhost", query = "SELECT u FROM Usuario u WHERE u.txhost = :txhost"),
    @NamedQuery(name = "Usuario.findByTxdate", query = "SELECT u FROM Usuario u WHERE u.txdate = :txdate"),
    @NamedQuery(name = "Usuario.findByStatus", query = "SELECT u FROM Usuario u WHERE u.status = :status")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_usuario")
    private Integer idUsuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "chat_id")
    private String chatId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "last_message_sent")
    private String lastMessageSent;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "last_message_received")
    private String lastMessageReceived;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "txuser")
    private String txuser;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "txhost")
    private String txhost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "txdate")
    @Temporal(TemporalType.DATE)
    private Date txdate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private int status;

    public Usuario() {
    }

    public Usuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usuario(Integer idUsuario, String chatId, String lastMessageSent, String lastMessageReceived, String txuser, String txhost, Date txdate, int status) {
        this.idUsuario = idUsuario;
        this.chatId = chatId;
        this.lastMessageSent = lastMessageSent;
        this.lastMessageReceived = lastMessageReceived;
        this.txuser = txuser;
        this.txhost = txhost;
        this.txdate = txdate;
        this.status = status;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getLastMessageSent() {
        return lastMessageSent;
    }

    public void setLastMessageSent(String lastMessageSent) {
        this.lastMessageSent = lastMessageSent;
    }

    public String getLastMessageReceived() {
        return lastMessageReceived;
    }

    public void setLastMessageReceived(String lastMessageReceived) {
        this.lastMessageReceived = lastMessageReceived;
    }

    public String getTxuser() {
        return txuser;
    }

    public void setTxuser(String txuser) {
        this.txuser = txuser;
    }

    public String getTxhost() {
        return txhost;
    }

    public void setTxhost(String txhost) {
        this.txhost = txhost;
    }

    public Date getTxdate() {
        return txdate;
    }

    public void setTxdate(Date txdate) {
        this.txdate = txdate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.example.AgendaFinalBot.domain.Usuario[ idUsuario=" + idUsuario + " ]";
    }
    
}
