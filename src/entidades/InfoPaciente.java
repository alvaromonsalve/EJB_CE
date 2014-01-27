/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Alvaro Monsalve
 */
@Entity
@Table(name = "info_paciente")
@NamedQueries({
    @NamedQuery(name = "InfoPaciente.findAll", query = "SELECT i FROM InfoPaciente i")})
public class InfoPaciente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "tipo_doc")
    private String tipoDoc;
    @Basic(optional = false)
    @Column(name = "num_doc")
    private String numDoc;
    @Basic(optional = false)
    @Column(name = "hc")
    private String hc;
    @Basic(optional = false)
    @Column(name = "nombre1")
    private String nombre1;
    @Column(name = "nombre2")
    private String nombre2;
    @Basic(optional = false)
    @Column(name = "apellido1")
    private String apellido1;
    @Column(name = "apellido2")
    private String apellido2;
    @Basic(optional = false)
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaNacimiento;
    @Column(name = "genero")
    private Character genero;
    @Column(name = "estado_civil")
    private String estadoCivil;
    @Column(name = "id_pais_nac")
    private Integer idPaisNac;
    @Column(name = "id_pais_res")
    private Integer idPaisRes;
    @Column(name = "id_dpto_nac")
    private String idDptoNac;
    @Column(name = "id_dpto_res")
    private String idDptoRes;
    @Column(name = "id_mun_nac")
    private String idMunNac;
    @Column(name = "id_mun_res")
    private String idMunRes;
    @Column(name = "barrio_o_vereda")
    private String barrioOVereda;
    @Column(name = "zona_res")
    private Character zonaRes;
    @Lob
    @Column(name = "direccion_ppal")
    private String direccionPpal;
    @Lob
    @Column(name = "ocupacion")
    private String ocupacion;
    @Column(name = "estrato_se")
    private Character estratoSe;
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "num_celular")
    private String numCelular;
    @Column(name = "mail")
    private String mail;
    @Column(name = "raza")
    private String raza;
    @Column(name = "etnia")
    private String etnia;
    @Column(name = "tipo_afiliacion")
    private Character tipoAfiliacion;
    @Column(name = "nivel")
    private Character nivel;
    @Column(name = "estado")
    private Character estado;
    @Column(name = "fecha_hora_ingreso_datos")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraIngresoDatos;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPaciente")
    private List<PypAdmAgend> pypAdmAgendList;
    @JoinColumn(name = "contratante", referencedColumnName = "id")
    @ManyToOne
    private InfoEntidades contratante;

    public InfoPaciente() {
    }

    public InfoPaciente(Integer id) {
        this.id = id;
    }

    public InfoPaciente(Integer id, String tipoDoc, String numDoc, String hc, String nombre1, String apellido1, Date fechaNacimiento) {
        this.id = id;
        this.tipoDoc = tipoDoc;
        this.numDoc = numDoc;
        this.hc = hc;
        this.nombre1 = nombre1;
        this.apellido1 = apellido1;
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getNumDoc() {
        return numDoc;
    }

    public void setNumDoc(String numDoc) {
        this.numDoc = numDoc;
    }

    public String getHc() {
        return hc;
    }

    public void setHc(String hc) {
        this.hc = hc;
    }

    public String getNombre1() {
        return nombre1;
    }

    public void setNombre1(String nombre1) {
        this.nombre1 = nombre1;
    }

    public String getNombre2() {
        return nombre2;
    }

    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Character getGenero() {
        return genero;
    }

    public void setGenero(Character genero) {
        this.genero = genero;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public Integer getIdPaisNac() {
        return idPaisNac;
    }

    public void setIdPaisNac(Integer idPaisNac) {
        this.idPaisNac = idPaisNac;
    }

    public Integer getIdPaisRes() {
        return idPaisRes;
    }

    public void setIdPaisRes(Integer idPaisRes) {
        this.idPaisRes = idPaisRes;
    }

    public String getIdDptoNac() {
        return idDptoNac;
    }

    public void setIdDptoNac(String idDptoNac) {
        this.idDptoNac = idDptoNac;
    }

    public String getIdDptoRes() {
        return idDptoRes;
    }

    public void setIdDptoRes(String idDptoRes) {
        this.idDptoRes = idDptoRes;
    }

    public String getIdMunNac() {
        return idMunNac;
    }

    public void setIdMunNac(String idMunNac) {
        this.idMunNac = idMunNac;
    }

    public String getIdMunRes() {
        return idMunRes;
    }

    public void setIdMunRes(String idMunRes) {
        this.idMunRes = idMunRes;
    }

    public String getBarrioOVereda() {
        return barrioOVereda;
    }

    public void setBarrioOVereda(String barrioOVereda) {
        this.barrioOVereda = barrioOVereda;
    }

    public Character getZonaRes() {
        return zonaRes;
    }

    public void setZonaRes(Character zonaRes) {
        this.zonaRes = zonaRes;
    }

    public String getDireccionPpal() {
        return direccionPpal;
    }

    public void setDireccionPpal(String direccionPpal) {
        this.direccionPpal = direccionPpal;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public Character getEstratoSe() {
        return estratoSe;
    }

    public void setEstratoSe(Character estratoSe) {
        this.estratoSe = estratoSe;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNumCelular() {
        return numCelular;
    }

    public void setNumCelular(String numCelular) {
        this.numCelular = numCelular;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getEtnia() {
        return etnia;
    }

    public void setEtnia(String etnia) {
        this.etnia = etnia;
    }

    public Character getTipoAfiliacion() {
        return tipoAfiliacion;
    }

    public void setTipoAfiliacion(Character tipoAfiliacion) {
        this.tipoAfiliacion = tipoAfiliacion;
    }

    public Character getNivel() {
        return nivel;
    }

    public void setNivel(Character nivel) {
        this.nivel = nivel;
    }

    public Character getEstado() {
        return estado;
    }

    public void setEstado(Character estado) {
        this.estado = estado;
    }

    public Date getFechaHoraIngresoDatos() {
        return fechaHoraIngresoDatos;
    }

    public void setFechaHoraIngresoDatos(Date fechaHoraIngresoDatos) {
        this.fechaHoraIngresoDatos = fechaHoraIngresoDatos;
    }

    public List<PypAdmAgend> getPypAdmAgendList() {
        return pypAdmAgendList;
    }

    public void setPypAdmAgendList(List<PypAdmAgend> pypAdmAgendList) {
        this.pypAdmAgendList = pypAdmAgendList;
    }

    public InfoEntidades getContratante() {
        return contratante;
    }

    public void setContratante(InfoEntidades contratante) {
        this.contratante = contratante;
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
        if (!(object instanceof InfoPaciente)) {
            return false;
        }
        InfoPaciente other = (InfoPaciente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.InfoPaciente[ id=" + id + " ]";
    }

}
