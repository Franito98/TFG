package com.tfg.ws.rest.TFGREST.RecursosExt;

import org.hl7.fhir.r4.model.Practitioner;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.primitive.StringDt;

@ResourceDef(name = "Practitioner")
public class Practicante extends Practitioner implements java.io.Serializable{

	public Practicante() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** * This is a basic extension, with a DataType value (in this case, StringDt) */ 
	//Atributo extendido Usuario
	@Child(name = "usuario") 
	@Description(shortDefinition = "Contiene el nombre de usuario con el que se registró el agente") 
	@Extension(url = "localhost:8080/extensión/practitioner/usuario", isModifier = false, definedLocally = true) 
	private StringDt usu;
	
	/** * This is a basic extension, with a DataType value (in this case, StringDt) */ 
	//Atributo extendido Hospital
	@Child(name = "hospital") 
	@Description(shortDefinition = "Contiene el nombre del hospital en el que trabaja el agente") 
	@Extension(url = "localhost:8080/extensión/practitioner/hospital", isModifier = false, definedLocally = true) 
	private StringDt hospital;
	
	/** * This is a basic extension, with a DataType value (in this case, StringDt) */ 
	//Atributo extendido Departamento
	@Child(name = "departamento") 
	@Description(shortDefinition = "Contiene el departamento al que pertenece el agente") 
	@Extension(url = "localhost:8080/extensión/practitioner/departamento", isModifier = false, definedLocally = true) 
	private StringDt depart;
	
	/** * This is a basic extension, with a DataType value (in this case, StringDt) */ 
	//Atributo extendido Código
	@Child(name = "codigo") 
	@Description(shortDefinition = "Contiene código con el que se registra el agente") 
	@Extension(url = "localhost:8080/extensión/practitioner/codigo", isModifier = false, definedLocally = true) 
	private StringDt codigo;

	public StringDt getUsu() {
		return usu;
	}

	public void setUsu(StringDt usu) {
		this.usu = usu;
	}

	public StringDt getHospital() {
		return hospital;
	}

	public void setHospital(StringDt hospital) {
		this.hospital = hospital;
	}

	public StringDt getDepart() {
		return depart;
	}

	public void setDepart(StringDt depart) {
		this.depart = depart;
	}

	public StringDt getCodigo() {
		return codigo;
	}

	public void setCodigo(StringDt codigo) {
		this.codigo = codigo;
	}
}
