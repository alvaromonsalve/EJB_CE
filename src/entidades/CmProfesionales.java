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
@Table(name = "cm_profesionales")
@NamedQueries({
    @NamedQuery(name = "CmProfesionales.findAll", query = "SELECT c FROM CmProfesionales c")})
public class CmProfesionales implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "tarjeta_profesional")
    private String tarjetaProfesional;
    @Column(name = "t_vinculacion")
    private Character tVinculacion;
    @Column(name = "estado")
    private Character estado;
    @JoinColumn(name = "id_descripcion_login", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ConfigDecripcionLogin idDescripcionLogin;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProfesional")
    private List<PypAdmControlProfesionales> pypAdmControlProfesionalesList;

    public CmProfesionales() {
    }

    public CmProfesionales(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTarjetaProfesional() {
        return tarjetaProfesional;
    }

    public void setTarjetaProfesional(String tarjetaProfesional) {
        this.tarjetaProfesional = tarjetaProfesional;
    }

    public Character getTVinculacion() {
        return tVinculacion;
    }

    public void setTVinculacion(Character tVinculacion) {
        this.tVinculacion = tVinculacion;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    public ConfigDecripcionLogin getIdDescripcionLogin() {
        return idDescripcionLogin;
    }

    public void setIdDescripcionLogin(ConfigDecripcionLogin idDescripcionLogin) {
        this.idDescripcionLogin = idDescripcionLogin;
    }

    public List<PypAdmControlProfesionales> getPypAdmControlProfesionalesList() {
        return pypAdmControlProfesionalesList;
    }

    public void setPypAdmControlProfesionalesList(List<PypAdmControlProfesionales> pypAdmControlProfesionalesList) {
        this.pypAdmControlProfesionalesList = pypAdmControlProfesionalesList;
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
        if (!(object instanceof CmProfesionales)) {
            return false;
        }
        CmProfesionales other = (CmProfesionales) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.CmProfesionales[ id=" + id + " ]";
    }

}
