package com.tfg.ws.rest.TFGREST.DAO;

import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.PractitionerRole;

import com.tfg.ws.rest.TFGREST.RecursosExt.Practicante;
import com.tfg.ws.rest.TFGREST.objetos.Agentes;

public interface InterfazAgenteDAO {

	public Agentes accederAgente(String contra);
	
	public void actualizarAg(Practicante prac, Agentes ag);
	
	public void actualizarHospital(Organization hospital);

	public void actualizarDepart(PractitionerRole depart);

	public Agentes getAgDNI(String dni);

	public Agentes getAgLogin(String login);
	
}
