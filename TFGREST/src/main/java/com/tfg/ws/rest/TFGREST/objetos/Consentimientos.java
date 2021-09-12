package com.tfg.ws.rest.TFGREST.objetos;
// Generated 7 oct. 2020 18:24:47 by Hibernate Tools 5.4.18.Final

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Consentimientos generated by hbm2java
 */

@Entity
@Table(name = "Consentimientos")
public class Consentimientos implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="seqgen",sequenceName="MY_SEQ_GEN",initialValue=1, allocationSize=1)
	@GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seqgen")
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	/*
	@Column(name = "datos", unique = false, nullable = false)
	private String datos;
	*/
	@ManyToOne
	@JoinColumn(name="contraag")
	private Agentes agentes;
	
	@ManyToOne
	@JoinColumn(name="dniciud")
	private Ciudadanos ciudadanos;
	
	@Column(name = "id_consent", nullable = false)
	private String idconsent;
	
	/*
	@Column(name = "usu_datos", nullable = false)
	private String usuDatos;
	
	@Column(name = "ubi_datos", nullable = false)
	private String ubiDatos;
	
	@Column(name = "cat_datos", nullable = false)
	private String catDatos;
	
	@Column(name = "accion", nullable = false)
	private String accion;
	
	@Column(name = "motivo", nullable = false)
	private String motivo;
	
	@Column(name = "dur", nullable = false)
	private String dur;
	
	@Column(name = "cond")
	private String cond;
	
	@Column(name = "estado", nullable = false)
	private String estado;
	
	@Column(name = "alerta", nullable = false)
	private Boolean alerta;
*/
	
	public Consentimientos() {
	}
	
	public Consentimientos(Agentes agentes, Ciudadanos ciudadanos, String idconsent) {
		super();
		this.agentes = agentes;
		this.ciudadanos = ciudadanos;
		this.idconsent = idconsent;
	}
	
	
/*
	public Consentimientos(String datos, String usuDatos, String ubiDatos, String catDatos, String accion,
			String motivo, String dur, String estado) {
		this.datos = datos;
		this.usuDatos = usuDatos;
		this.ubiDatos = ubiDatos;
		this.catDatos = catDatos;
		this.accion = accion;
		this.motivo = motivo;
		this.dur = dur;
		this.estado = estado;
	}

	public Consentimientos(String datos, Agentes agentes, Ciudadanos ciudadanos, String usuDatos, String ubiDatos,
			String catDatos, String accion, String motivo, String dur, String cond, String estado, Boolean alerta) {
		this.datos = datos;
		this.agentes = agentes;
		this.ciudadanos = ciudadanos;
		this.usuDatos = usuDatos;
		this.ubiDatos = ubiDatos;
		this.catDatos = catDatos;
		this.accion = accion;
		this.motivo = motivo;
		this.dur = dur;
		this.cond = cond;
		this.estado = estado;
		this.alerta = alerta;
	}

	public String getDatos() {
		return this.datos;
	}

	public void setDatos(String datos) {
		this.datos = datos;
	}
*/
	@JsonBackReference
	public Agentes getAgentes() {
		return this.agentes;
	}

	public void setAgentes(Agentes agentes) {
		this.agentes = agentes;
	}

	@JsonBackReference
	public Ciudadanos getCiudadanos() {
		return this.ciudadanos;
	}

	public void setCiudadanos(Ciudadanos ciudadanos) {
		this.ciudadanos = ciudadanos;
	}
	
	public String getIdconsent() {
		return idconsent;
	}
	public void setIdconsent(String idconsent) {
		this.idconsent = idconsent;
	}

	@Override
	public int hashCode() {
		return Objects.hash(agentes, ciudadanos, idconsent);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Consentimientos other = (Consentimientos) obj;
		return Objects.equals(agentes, other.agentes) && Objects.equals(ciudadanos, other.ciudadanos)
				&& Objects.equals(idconsent, other.idconsent);
	}
	
/*
	public String getUsuDatos() {
		return this.usuDatos;
	}

	public void setUsuDatos(String usuDatos) {
		this.usuDatos = usuDatos;
	}

	public String getUbiDatos() {
		return this.ubiDatos;
	}

	public void setUbiDatos(String ubiDatos) {
		this.ubiDatos = ubiDatos;
	}

	public String getCatDatos() {
		return this.catDatos;
	}

	public void setCatDatos(String catDatos) {
		this.catDatos = catDatos;
	}

	public String getAccion() {
		return this.accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getMotivo() {
		return this.motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getDur() {
		return this.dur;
	}

	public void setDur(String dur) {
		this.dur = dur;
	}

	public String getCond() {
		return this.cond;
	}

	public void setCond(String cond) {
		this.cond = cond;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Boolean getAlerta() {
		return this.alerta;
	}

	public void setAlerta(Boolean alerta) {
		this.alerta = alerta;
	}

	@Override
	public int hashCode() {
		return Objects.hash(accion, agentes, alerta, catDatos, ciudadanos, cond, datos, dur, estado, motivo, ubiDatos,
				usuDatos);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Consentimientos other = (Consentimientos) obj;
		return Objects.equals(accion, other.accion) && Objects.equals(agentes, other.agentes)
				&& Objects.equals(alerta, other.alerta) && Objects.equals(catDatos, other.catDatos)
				&& Objects.equals(ciudadanos, other.ciudadanos) && Objects.equals(cond, other.cond)
				&& Objects.equals(datos, other.datos) && Objects.equals(dur, other.dur)
				&& Objects.equals(estado, other.estado) && Objects.equals(motivo, other.motivo)
				&& Objects.equals(ubiDatos, other.ubiDatos) && Objects.equals(usuDatos, other.usuDatos);
	}
*/
	
}
