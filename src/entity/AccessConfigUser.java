/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Alvaro Monsalve
 */
@Entity
@Table(name = "access_config_user")
@NamedQueries({
    @NamedQuery(name = "AccessConfigUser.findAll", query = "SELECT a FROM AccessConfigUser a")})
public class AccessConfigUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "estado")
    private Integer estado;
    @JoinColumn(name = "id_perfiles", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AccessPerfiles idPerfiles;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ConfigDecripcionLogin idUsuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idConfigUsuario")
    private List<AccessEspeciales> accessEspecialesList;

    public AccessConfigUser() {
    }

    public AccessConfigUser(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public AccessPerfiles getIdPerfiles() {
        return idPerfiles;
    }

    public void setIdPerfiles(AccessPerfiles idPerfiles) {
        this.idPerfiles = idPerfiles;
    }

    public ConfigDecripcionLogin getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(ConfigDecripcionLogin idUsuario) {
        this.idUsuario = idUsuario;
    }

    public List<AccessEspeciales> getAccessEspecialesList() {
        return accessEspecialesList;
    }

    public void setAccessEspecialesList(List<AccessEspeciales> accessEspecialesList) {
        this.accessEspecialesList = accessEspecialesList;
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
        if (!(object instanceof AccessConfigUser)) {
            return false;
        }
        AccessConfigUser other = (AccessConfigUser) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.AccessConfigUser[ id=" + id + " ]";
    }

}
