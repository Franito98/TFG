package com.tfg.ws.rest.TFGREST.Agenteservice;

import com.tfg.ws.rest.TFGREST.RecursosExt.Practicante;

public interface AgenteService {
	
	public Practicante accederAgente(String contra);
	
	public Boolean alertasAgente(String usu, String contra);
}
