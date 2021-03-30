package com.tfg.ws.rest.TFGREST.DAO;

import java.util.List;

import com.tfg.ws.rest.TFGREST.RecursosExt.Consen;
import com.tfg.ws.rest.TFGREST.objetos.Consentimientos;

public interface InterfazConsentDAO {

	public void crearconsent(String contra, Consen consentimiento, String ciud);
	
	public List<Consentimientos> getconsentestadoA(String contra, String estado);
	
	public List<Consentimientos> getconsentestadoC(String dni, String estado);
	
	public Integer eliminarConsent(Consen consentimiento);

	public Integer modificarConsent(String estado, Consen consentimiento);
	
	public Integer actualizarAlerta(Consen consentimiento);

	public List<Consentimientos> getalertas(String dni);
}
