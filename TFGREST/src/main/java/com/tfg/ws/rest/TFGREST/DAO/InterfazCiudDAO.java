package com.tfg.ws.rest.TFGREST.DAO;

import com.tfg.ws.rest.TFGREST.objetos.Ciudadanos;

public interface InterfazCiudDAO {

	public Ciudadanos accederCiud(String dni);
	
	public Boolean alertasCiud(String usu, String dni);
	
}
