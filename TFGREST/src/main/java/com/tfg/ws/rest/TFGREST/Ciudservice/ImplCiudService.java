package com.tfg.ws.rest.TFGREST.Ciudservice;

import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.UriType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.ws.rest.TFGREST.DAO.InterfazCiudDAO;
import com.tfg.ws.rest.TFGREST.RecursosExt.Paciente;
import com.tfg.ws.rest.TFGREST.objetos.Ciudadanos;

import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;

@Service
public class ImplCiudService implements CiudService {

	@Autowired
	private InterfazCiudDAO intCiudDAO;
	
	@Override
	public Paciente accederCiud(String dni) {
		// TODO Auto-generated method stub
		Ciudadanos ciud = intCiudDAO.accederCiud(dni);
		
		Paciente paciente = new Paciente();
		
		Identifier id = new Identifier();
		id.setId(ciud.getDni());
		id.setSystemElement(new UriType("http://localhost:8080/TFGREST/ciud/acceder?dni=" + dni));
		paciente.addIdentifier(id);
		
		HumanName nombre = new HumanName();
		nombre.setText(ciud.getNombre());
		paciente.addName(nombre);
		
		StringDt usu = new StringDt();
		usu.setValueAsString(ciud.getUsu());
		paciente.setUsu(usu);
		
		IntegerDt tarj = new IntegerDt();
		tarj.setValue(ciud.getTarjsanitaria());
		paciente.setTarjsanitaria(tarj);;
		
		ContactPoint com = new ContactPoint();
		com.setSystem(ContactPointSystem.PHONE);
		com.setValue(ciud.getTelefono().toString());
		com.setUse(ContactPointUse.MOBILE);
		paciente.addTelecom(com);
		
		return paciente;
	}

	@Override
	public Boolean alertasCiud(String usu, String dni) {
		// TODO Auto-generated method stub
		return null;
	}

}
