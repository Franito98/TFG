package com.tfg.ws.rest.TFGREST.Agenteservice;

import java.util.List;

import com.tfg.ws.rest.TFGREST.RecursosExt.Consen;
import com.tfg.ws.rest.TFGREST.RecursosExt.Practicante;

public interface AgenteService {
	
	public Practicante accederAgente(String contra);
	
	public String regAg(String contra, Practicante prac);
	
	public String solconsent(String contra, Consen consentimiento);
	
	public Boolean alertasAgente(String usu, String contra);
	
	public List<Consen> getconsentestado(String contra, String estado);
	
	public String eliminarConsent(Consen consentimiento);
}
