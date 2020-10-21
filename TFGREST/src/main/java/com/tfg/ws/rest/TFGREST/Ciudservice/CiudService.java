package com.tfg.ws.rest.TFGREST.Ciudservice;

import com.tfg.ws.rest.TFGREST.RecursosExt.Paciente;
	
public interface CiudService {
		
	public Paciente accederCiud(String dni);
	
	public Boolean alertasCiud(String usu, String dni);
			
}