package com.tfg.ws.rest.TFGREST.DAO;

import java.util.List;

import org.hl7.fhir.r4.model.Bundle;

import com.tfg.ws.rest.TFGREST.RecursosExt.Consen;
import com.tfg.ws.rest.TFGREST.objetos.Consentimientos;

import ca.uhn.fhir.rest.api.MethodOutcome;

public interface InterfazConsentDAO {

	public void crearconsent(String contra, Consen consentimiento, String ciud);
	
	public Bundle getconsentA(String contra);
	
	public List<Consentimientos> getidsconsent(String dni);
	
	public Bundle getconsentC(String id);
	
	public MethodOutcome eliminarConsent(Consen consentimiento);

	public void modificarConsent(Consen consentimiento);
	
	public void actualizarAlerta(Consen consentimiento);

	public List<Consentimientos> getalertas(String dni);
}
