/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.AgendaFinalBot.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ale_m
 */
@Entity
@Table(name = "contacto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Contacto.findAll", query = "SELECT c FROM Contacto c"),
    @NamedQuery(name = "Contacto.findByIdContacto", query = "SELECT c FROM Contacto c WHERE c.idContacto = :idContacto"),
    @NamedQuery(name = "Contacto.findByChatId", query = "SELECT c FROM Contacto c WHERE c.chatId = :chatId"),
    @NamedQuery(name = "Contacto.findByNombres", query = "SELECT c FROM Contacto c WHERE c.nombres = :nombres"),
    @NamedQuery(name = "Contacto.findByApellidos", query = "SELECT c FROM Contacto c WHERE c.apellidos = :apellidos"),
    @NamedQuery(name = "Contacto.findByCorreo", query = "SELECT c FROM Contacto c WHERE c.correo = :correo"),
    @NamedQuery(name = "Contacto.findByFechaNacimiento", query = "SELECT c FROM Contacto c WHERE c.fechaNacimiento = :fechaNacimiento"),
    @NamedQuery(name = "Contacto.findByTxuser", query = "SELECT c FROM Contacto c WHERE c.txuser = :txuser"),
    @NamedQuery(name = "Contacto.findByTxhost", query = "SELECT c FROM Contacto c WHERE c.txhost = :txhost"),
    @NamedQuery(name = "Contacto.findByTxdate", query = "SELECT c FROM Contacto c WHERE c.txdate = :txdate"),
    @NamedQuery(name = "Contacto.findByStatus", query = "SELECT c FROM Contacto c WHERE c.status = :status")})
public class Contacto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_contacto")
    private Integer idContacto;
    @Basic(optional = false)
    @Column(name = "chat_id")
    private String chatId;
    @Basic(optional = false)
    @Column(name = "nombres")
    private String nombres;
    @Basic(optional = false)
    @Column(name = "apellidos")
    private String apellidos;
    @Basic(optional = false)
    @Column(name = "correo")
    private String correo;
    @Basic(optional = false)
    @Column(name = "fecha_nacimiento")
    private String fechaNacimiento;
    @Basic(optional = false)
    @Column(name = "txuser")
    private String txuser;
    @Basic(optional = false)
    @Column(name = "txhost")
    private String txhost;
    @Basic(optional = false)
    @Column(name = "txdate")
    @Temporal(TemporalType.DATE)
    private Date txdate;
    @Basic(optional = false)
    @Column(name = "status")
    private int status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idContacto", fetch = FetchType.LAZY)
    private Collection<Numero> numeroCollection;

    public Contacto() {
    }

    public Contacto(Integer idContacto) {
        this.idContacto = idContacto;
    }

    public Contacto(Integer idContacto, String chatId, String nombres, String apellidos, String correo, String fechaNacimiento, String txuser, String txhost, Date txdate, int status) {
        this.idContacto = idContacto;
        this.chatId = chatId;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.fechaNacimiento = fechaNacimiento;
        this.txuser = txuser;
        this.txhost = txhost;
        this.txdate = txdate;
        this.status = status;
    }

    public Integer getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(Integer idContacto) {
        this.idContacto = idContacto;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
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

    @XmlTransient
    public Collection<Numero> getNumeroCollection() {
        return numeroCollection;
    }

    public void setNumeroCollection(Collection<Numero> numeroCollection) {
        this.numeroCollection = numeroCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idContacto != null ? idContacto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Contacto)) {
            return false;
        }
        Contacto other = (Contacto) object;
        if ((this.idContacto == null && other.idContacto != null) || (this.idContacto != null && !this.idContacto.equals(other.idContacto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.example.AgendaFinalBot.domain.Contacto[ idContacto=" + idContacto + " ]";
    }
    
}
