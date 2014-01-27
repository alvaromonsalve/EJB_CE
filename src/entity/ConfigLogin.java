/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Alvaro Monsalve
 */
@Entity
@Table(name = "config_login")
@NamedQueries({
    @NamedQuery(name = "ConfigLogin.findAll", query = "SELECT c FROM ConfigLogin c")})
public class ConfigLogin implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "loGin")
    private String loGin;
    @Column(name = "pasSword")
    private String pasSword;
    @Column(name = "estado")
    private Boolean estado;
    @OneToMany(mappedBy = "idLogin")
    private List<ConfigDecripcionLogin> configDecripcionLoginList;

    public ConfigLogin() {
    }

    public ConfigLogin(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoGin() {
        return loGin;
    }

    public void setLoGin(String loGin) {
        this.loGin = loGin;
    }

    public String getPasSword() {
        return pasSword;
    }

    public void setPasSword(String pasSword) {
        this.pasSword = pasSword;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public List<ConfigDecripcionLogin> getConfigDecripcionLoginList() {
        return configDecripcionLoginList;
    }

    public void setConfigDecripcionLoginList(List<ConfigDecripcionLogin> configDecripcionLoginList) {
        this.configDecripcionLoginList = configDecripcionLoginList;
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
        if (!(object instanceof ConfigLogin)) {
            return false;
        }
        ConfigLogin other = (ConfigLogin) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.ConfigLogin[ id=" + id + " ]";
    }

}
