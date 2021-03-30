package com.tfg.ws.rest.TFGREST.Ciudservice;

import java.util.List;

import com.tfg.ws.rest.TFGREST.RecursosExt.Consen;
import com.tfg.ws.rest.TFGREST.RecursosExt.Paciente;
	
public interface CiudService {
		
	public Paciente accederCiud(String dni);
	
	public String regCiud(String dni, Paciente paciente);
	
	public List<Consen> getconsentestado(String dni, String estado);
	
	public String modificarConsent(String estado, Consen consent);
	
	public String actualizarAlerta(Consen consentimiento);
	
	public List<Consen> getalertasCiud(String dni);
}