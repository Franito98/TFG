package com.tfg.ws.rest.TFGREST.Agenteservice;

import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.ws.rest.TFGREST.DAO.InterfazAgenteDAO;
import com.tfg.ws.rest.TFGREST.RecursosExt.Practicante;
import com.tfg.ws.rest.TFGREST.objetos.Agentes;

import ca.uhn.fhir.model.primitive.StringDt;

@Service
public class ImplAgenteService implements AgenteService {

	@Autowired
	private InterfazAgenteDAO intAgenteDAO;
	
	@Override
	public Practicante accederAgente(String contra) {
		// TODO Auto-generated method stub
		Agentes agente = intAgenteDAO.accederAgente(contra);
		
		Practicante prac = new Practicante();
		
		Identifier id = new Identifier();
		id.setValue(agente.getContra());
		id.setSystem("http://localhost:8080/TFGREST/ciud/acceder?contra=" + contra);
		prac.addIdentifier(id);
		
		HumanName nombre = new HumanName();
		nombre.setText(agente.getNombre());
		prac.addName(nombre);
		
		StringDt usu = new StringDt();
		usu.setValueAsString(agente.getUsu());
		prac.setUsu(usu);
		
		StringDt hosp = new StringDt();
		hosp.setValueAsString(agente.getHospital());
		prac.setHospital(hosp);
		
		StringDt depart = new StringDt();
		depart.setValueAsString(agente.getDepart());
		prac.setDepart(depart);
		
		StringDt cod = new StringDt();
		cod.setValueAsString(agente.getCodigo());
		prac.setCodigo(cod);
		
		return prac;
	}

	@Override
	public Boolean alertasAgente(String usu, String contra) {
		// TODO Auto-generated method stub
		return null;
	}

}
