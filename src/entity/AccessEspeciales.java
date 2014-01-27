/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Alvaro Monsalve
 */
@Entity
@Table(name = "access_especiales")
@NamedQueries({
    @NamedQuery(name = "AccessEspeciales.findAll", query = "SELECT a FROM AccessEspeciales a")})
public class AccessEspeciales implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Lob
    @Column(name = "ruta")
    private String ruta;
    @Basic(optional = false)
    @Lob
    @Column(name = "acceso")
    private String acceso;
    @Column(name = "estado")
    private Integer estado;
    @JoinColumn(name = "id_config_usuario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AccessConfigUser idConfigUsuario;

    public AccessEspeciales() {
    }

    public AccessEspeciales(Integer id) {
        this.id = id;
    }

    public AccessEspeciales(Integer id, String ruta, String acceso) {
        this.id = id;
        this.ruta = ruta;
        this.acceso = acceso;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getAcceso() {
        return acceso;
    }

    public void setAcceso(String acceso) {
        this.acceso = acceso;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public AccessConfigUser getIdConfigUsuario() {
        return idConfigUsuario;
    }

    public void setIdConfigUsuario(AccessConfigUser idConfigUsuario) {
        this.idConfigUsuario = idConfigUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccessEspeciales)) {
            return false;
        }
        AccessEspeciales other = (AccessEspeciales) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.AccessEspeciales[ id=" + id + " ]";
    }

}
