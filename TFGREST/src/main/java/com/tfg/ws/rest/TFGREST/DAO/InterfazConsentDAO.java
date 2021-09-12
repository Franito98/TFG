package com.tfg.ws.rest.TFGREST.DAO;

import java.util.List;

import org.hl7.fhir.r4.model.Bundle;

import com.tfg.ws.rest.TFGREST.RecursosExt.Consen;
import com.tfg.ws.rest.TFGREST.objetos.Agentes;
import com.tfg.ws.rest.TFGREST.objetos.Consentimientos;

import ca.uhn.fhir.rest.api.MethodOutcome;

public interface InterfazConsentDAO {

	public void crearconsent(String contra, Consen consentimiento, String ciud);
	
	public Bundle getconsent(String id);
	
	public List<Consentimientos> getidsconsentC(String dni);
	
	public MethodOutcome eliminarConsent(Consen consentimiento);

	public void modificarConsent(Consen consentimiento);
	
	public void actualizarAviso(Consen consentimiento);

	public List<Consentimientos> getidsconsentA(String contra);

}
