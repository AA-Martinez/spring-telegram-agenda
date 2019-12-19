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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "numero")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Numero.findAll", query = "SELECT n FROM Numero n"),
    @NamedQuery(name = "Numero.findByIdNumero", query = "SELECT n FROM Numero n WHERE n.idNumero = :idNumero"),
    @NamedQuery(name = "Numero.findByTelefono", query = "SELECT n FROM Numero n WHERE n.telefono = :telefono"),
    @NamedQuery(name = "Numero.findByTxuser", query = "SELECT n FROM Numero n WHERE n.txuser = :txuser"),
    @NamedQuery(name = "Numero.findByTxhost", query = "SELECT n FROM Numero n WHERE n.txhost = :txhost"),
    @NamedQuery(name = "Numero.findByTxdate", query = "SELECT n FROM Numero n WHERE n.txdate = :txdate"),
    @NamedQuery(name = "Numero.findByStatus", query = "SELECT n FROM Numero n WHERE n.status = :status")})
public class Numero implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_numero")
    private Integer idNumero;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "telefono")
    private String telefono;
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
    @JoinColumn(name = "id_contacto", referencedColumnName = "id_contacto")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Contacto idContacto;

    public Numero() {
    }

    public Numero(Integer idNumero) {
        this.idNumero = idNumero;
    }

    public Numero(Integer idNumero, String telefono, String txuser, String txhost, Date txdate, int status) {
        this.idNumero = idNumero;
        this.telefono = telefono;
        this.txuser = txuser;
        this.txhost = txhost;
        this.txdate = txdate;
        this.status = status;
    }

    public Integer getIdNumero() {
        return idNumero;
    }

    public void setIdNumero(Integer idNumero) {
        this.idNumero = idNumero;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    public Contacto getIdContacto() {
        return idContacto;
    }

    public void setIdContacto(Contacto idContacto) {
        this.idContacto = idContacto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idNumero != null ? idNumero.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Numero)) {
            return false;
        }
        Numero other = (Numero) object;
        if ((this.idNumero == null && other.idNumero != null) || (this.idNumero != null && !this.idNumero.equals(other.idNumero))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.example.AgendaFinalBot.domain.Numero[ idNumero=" + idNumero + " ]";
    }
    
}
