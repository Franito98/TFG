package com.tfg.ws.rest.TFGREST.DAO;

import java.util.List;

import com.tfg.ws.rest.TFGREST.RecursosExt.Paciente;
import com.tfg.ws.rest.TFGREST.objetos.Ciudadanos;

public interface InterfazCiudDAO {

	public Ciudadanos accederCiud(String dni);
	
	public void actualizarCiud(Paciente paciente);
	
	public List<Ciudadanos> obtenerciuds();
	
	public void alertasCiud(Ciudadanos ciud);
	
}
