package com.tfg.ws.rest.TFGREST.DAO;

import com.tfg.ws.rest.TFGREST.RecursosExt.Practicante;
import com.tfg.ws.rest.TFGREST.objetos.Agentes;

public interface InterfazAgenteDAO {

	public Agentes accederAgente(String contra);
	
	public void actualizarAg(Practicante prac);

	public String getDNI(String login);
	
}
