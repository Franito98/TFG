package com.tfg.ws.rest.TFGREST.DAO;

import com.tfg.ws.rest.TFGREST.objetos.Agentes;

public interface InterfazAgenteDAO {

	public Agentes accederAgente(String contra);
	
	public Boolean alertasAgente();
	
}
