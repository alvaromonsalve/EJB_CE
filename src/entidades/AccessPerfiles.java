/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Alvaro Monsalve
 */
@Entity
@Table(name = "access_perfiles")
@NamedQueries({
    @NamedQuery(name = "AccessPerfiles.findAll", query = "SELECT a FROM AccessPerfiles a")})
public class AccessPerfiles implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Lob
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Lob
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "estado")
    private Integer estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPerfiles")
    private List<AccessConfigUser> accessConfigUserList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPerfil")
    private List<AccessRoles> accessRolesList;

    public AccessPerfiles() {
    }

    public AccessPerfiles(Integer id) {
        this.id = id;
    }

    public AccessPerfiles(Integer id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public List<AccessConfigUser> getAccessConfigUserList() {
        return accessConfigUserList;
    }

    public void setAccessConfigUserList(List<AccessConfigUser> accessConfigUserList) {
        this.accessConfigUserList = accessConfigUserList;
    }

    public List<AccessRoles> getAccessRolesList() {
        return accessRolesList;
    }

    public void setAccessRolesList(List<AccessRoles> accessRolesList) {
        this.accessRolesList = accessRolesList;
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
        if (!(object instanceof AccessPerfiles)) {
            return false;
        }
        AccessPerfiles other = (AccessPerfiles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.AccessPerfiles[ id=" + id + " ]";
    }

}
