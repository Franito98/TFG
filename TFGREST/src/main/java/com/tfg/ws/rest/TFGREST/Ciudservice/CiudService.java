package com.tfg.ws.rest.TFGREST.Ciudservice;

import java.util.List;

import org.hl7.fhir.r4.model.Organization;

import com.tfg.ws.rest.TFGREST.RecursosExt.Consen;
import com.tfg.ws.rest.TFGREST.RecursosExt.Paciente;
import com.tfg.ws.rest.TFGREST.objetos.Agentes;
	
public interface CiudService {
		
	public String accederCiud(String dni);
	
	public String regCiud(String dni, Paciente paciente);
	
	public List<Consen> getconsentestado(String dni, String estado);
	
	public String modificarConsent(String estado, Consen consent);
	
	public String actualizarAviso(Consen consentimiento);
	
	public List<Consen> getavisosCiud(String dni);

	public String getHospital(String dni);

	public Agentes getNombre(String dni);
}