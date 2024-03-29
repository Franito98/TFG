package com.tfg.ws.rest.TFGREST.objetos;
// Generated 7 oct. 2020 18:24:47 by Hibernate Tools 5.4.18.Final

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Ciudadanos generated by hbm2java
 */

@Entity
@Table(name = "Ciudadanos")
public class Ciudadanos implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "dni", unique = true, nullable = false)
	private String dni;
	
	@Column(name = "nombre")
	private String nombre;
	
	@Column(name = "usu")
	private String usu;
	
	@Column(name = "tarjsanitaria", unique = true, nullable = false)
	private Integer tarjsanitaria;
	
	@Column(name = "telefono")
	private Integer telefono;
	
	@OneToMany(mappedBy = "ciudadanos")
	private Set<Consentimientos> consentimientoses = new HashSet<Consentimientos>(0);

	public Ciudadanos() {
	}

	public Ciudadanos(String dni, int tarjsanitaria) {
		this.dni = dni;
		this.tarjsanitaria = tarjsanitaria;
	}

	public Ciudadanos(String dni, String nombre, String usu, Integer tarjsanitaria, Integer telefono,
			Set<Consentimientos> consentimientoses) {
		this.dni = dni;
		this.nombre = nombre;
		this.usu = usu;
		this.tarjsanitaria = tarjsanitaria;
		this.telefono = telefono;
		this.consentimientoses = consentimientoses;
	}

	public String getDni() {
		return this.dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUsu() {
		return this.usu;
	}

	public void setUsu(String usu) {
		this.usu = usu;
	}

	public Integer getTarjsanitaria() {
		return this.tarjsanitaria;
	}

	public void setTarjsanitaria(Integer tarjsanitaria) {
		this.tarjsanitaria = tarjsanitaria;
	}

	public Integer getTelefono() {
		return this.telefono;
	}

	public void setTelefono(Integer telefono) {
		this.telefono = telefono;
	}

	@JsonManagedReference
	public Set<Consentimientos> getConsentimientoses() {
		return this.consentimientoses;
	}

	public void setConsentimientoses(Set<Consentimientos> consentimientoses) {
		this.consentimientoses = consentimientoses;
	}

}
