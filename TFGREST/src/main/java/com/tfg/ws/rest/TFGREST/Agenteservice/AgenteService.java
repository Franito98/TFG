package com.tfg.ws.rest.TFGREST.Agenteservice;

import java.util.List;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.PractitionerRole;

import com.tfg.ws.rest.TFGREST.RecursosExt.Consen;
import com.tfg.ws.rest.TFGREST.RecursosExt.Practicante;

public interface AgenteService {
	
	public String accederAgente(String contra);
	
	public String regAg(String contra, Practicante prac);
	
	public String reghospital(String contra, Organization hospital);

	public String regdepart(String contra, PractitionerRole depart);
	
	public String solconsent(String contra, Consen consentimiento);
	
	public List<Consen> getconsentestado(String contra, String estado);
	
	public String eliminarConsent(Consen consentimiento);

	public String getHospital(String login);

}
