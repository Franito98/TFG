package com.tfg.ws.rest.TFGREST.Ciudservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.Consent.ConsentState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.ws.rest.TFGREST.DAO.InterfazCiudDAO;
import com.tfg.ws.rest.TFGREST.DAO.InterfazConsentDAO;
import com.tfg.ws.rest.TFGREST.RecursosExt.Consen;
import com.tfg.ws.rest.TFGREST.RecursosExt.Paciente;
import com.tfg.ws.rest.TFGREST.objetos.Ciudadanos;
import com.tfg.ws.rest.TFGREST.objetos.Consentimientos;

import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;

@Service
public class ImplCiudService implements CiudService {

	@Autowired
	private InterfazCiudDAO intCiudDAO;
	
	@Autowired
	private InterfazConsentDAO intConsentDAO;
	
	@Override
	public Paciente accederCiud(String dni) {
		// TODO Auto-generated method stub
		Ciudadanos ciud = new Ciudadanos();
		ciud = intCiudDAO.accederCiud(dni);
		Paciente paciente = new Paciente();
		
		if(ciud == null) {
			paciente.setCodmensaje(new IntegerDt(400));
		}
		else {
			if (ciud.getUsu() == null) {
				paciente.setCodmensaje(new IntegerDt(300));
			}
			else {
					
				Identifier id = new Identifier();
				id.setId(ciud.getDni());
				id.setSystemElement(new UriType("http://localhost:8080/TFGREST/ciud/" + dni));
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
					
				paciente.setCodmensaje(new IntegerDt(200));
			}
		}
		return paciente;
	}
	
	@Override
	public String regCiud(String dni, Paciente paciente) {
		// TODO Auto-generated method stub
		Ciudadanos ciud = new Ciudadanos();
		ciud = intCiudDAO.accederCiud(dni);
		String cod;
		
		if(ciud == null) {
			cod = "{\n  \"codigo\": {\n  \"valueInteger\": 400\n  }\n}";
		}
		else {
			if(ciud.getUsu() != null) {
				cod = "{\n  \"codigo\": {\n  \"valueInteger\": 200\n  }\n}";
			}
			else {
				if(ciud.getTarjsanitaria() != paciente.getTarjsanitaria().getValue()) {
					cod = "{\n  \"codigo\": {\n  \"valueInteger\": 500\n  }\n}";
				}
				else {
					intCiudDAO.actualizarCiud(paciente);
					
					ciud = intCiudDAO.accederCiud(dni);
					
					if(ciud.getUsu() == paciente.getUsu().getValue() &&
							ciud.getNombre() == paciente.getNameFirstRep().getText() &&
							ciud.getTarjsanitaria() == paciente.getTarjsanitaria().getValue() &&
							ciud.getTelefono().equals(Integer.valueOf(paciente.getTelecom().get(0).getValue()))) {
					
						cod = "{\n  \"codigo\": {\n  \"valueInteger\": 300\n  }\n}";
					}
					else {
						cod = "{\n  \"codigo\": {\n  \"valueInteger\": 100\n  }\n}";
					}
				}
			}
		}
		return cod;
	}

	@Override
	public List<Consen> getconsentestado(String dni, String estado) {
		
		List<Consentimientos> listconsent = intConsentDAO.getconsentestadoC(dni,estado);
		List<Consen> listaconsentimientos = new ArrayList<Consen>();
		
		for(int i = 0; i<listconsent.size(); i++) {
			
			Consen consentimiento = new Consen();
			
			Identifier id = new Identifier();
	        id.setId(listconsent.get(i).getAgentes().getContra());
	        id.setSystemElement(new UriType("http://localhost:8080/TFGREST/consentimiento/" + dni));
	        consentimiento.addIdentifier(id);

	        consentimiento.addPerformer(new Reference().setReference(listconsent.get(i).getUsuDatos()));

	        consentimiento.addOrganization(new Reference().setReference(listconsent.get(i).getUbiDatos()));

	        consentimiento.addCategory().setText(listconsent.get(i).getCatDatos());

	        StringDt datos = new StringDt();
	        datos.setValueAsString(listconsent.get(i).getDatos());
	        consentimiento.setDatos(datos);

	        StringDt accion = new StringDt();
	        accion.setValueAsString(listconsent.get(i).getAccion());
	        consentimiento.setAccion(accion);

	        consentimiento.setPatient(new Reference().setReference(listconsent.get(i).getCiudadanos().getDni()));

	        consentimiento.setScope(new CodeableConcept().setText(listconsent.get(i).getMotivo()));

	        consentimiento.setDateTime(new Date());
	        StringDt dur = new StringDt();
	        dur.setValueAsString(listconsent.get(i).getDur());
	        consentimiento.setDuracion(dur);

	        StringDt cond = new StringDt();
	        cond.setValueAsString(listconsent.get(i).getCond());
	        consentimiento.setCond(cond);

	        if(listconsent.get(i).getEstado().equals("draft")) {
	        	consentimiento.setStatus(ConsentState.DRAFT);
	        }
	        else {
	        	if(listconsent.get(i).getEstado().equals("rejected")) {
	        		consentimiento.setStatus(ConsentState.REJECTED);
	        	}
	        	else {
	        		if(listconsent.get(i).getEstado().equals("active")) {
		        		consentimiento.setStatus(ConsentState.ACTIVE);
	        		}
	        	}
	        }
	        consentimiento.setAlerta(new BooleanDt(listconsent.get(i).getAlerta()));
	        
	        listaconsentimientos.add(consentimiento);
		}
		return listaconsentimientos;
	}
	
	@Override
	public String modificarConsent(String estado, Consen consentimiento) {
		String cod;
		Integer row = intConsentDAO.modificarConsent(estado,consentimiento);
		
		if(row == 1) {
			cod = "{\n  \"codigo\": {\n  \"valueInteger\": 200\n  }\n}";
		} else {
			cod = "{\n  \"codigo\": {\n  \"valueInteger\": 400\n  }\n}";
		}
		return cod;
	}
	
	@Override
	public String actualizarAlerta(Consen consentimiento) {
		String cod;
		Integer row = intConsentDAO.actualizarAlerta(consentimiento);
		
		if(row == 1) {
			cod = "{\n  \"codigo\": {\n  \"valueInteger\": 200\n  }\n}";
		} else {
			cod = "{\n  \"codigo\": {\n  \"valueInteger\": 400\n  }\n}";
		}
		return cod;	
	}
	
	@Override
	public List<Consen> getalertasCiud(String dni) {
		
		List<Consentimientos> listconsent = intConsentDAO.getalertas(dni);
		List<Consen> listaconsentimientos = new ArrayList<Consen>();
		
		for(int i = 0; i<listconsent.size(); i++) {
			
			Consen consentimiento = new Consen();
			
			Identifier id = new Identifier();
	        id.setId(listconsent.get(i).getAgentes().getContra());
	        id.setSystemElement(new UriType("http://localhost:8080/TFGREST/consentimiento/" + dni));
	        consentimiento.addIdentifier(id);

	        consentimiento.addPerformer(new Reference().setReference(listconsent.get(i).getUsuDatos()));

	        consentimiento.addOrganization(new Reference().setReference(listconsent.get(i).getUbiDatos()));

	        consentimiento.addCategory().setText(listconsent.get(i).getCatDatos());

	        StringDt datos = new StringDt();
	        datos.setValueAsString(listconsent.get(i).getDatos());
	        consentimiento.setDatos(datos);

	        StringDt accion = new StringDt();
	        accion.setValueAsString(listconsent.get(i).getAccion());
	        consentimiento.setAccion(accion);

	        consentimiento.setPatient(new Reference().setReference(listconsent.get(i).getCiudadanos().getDni()));

	        consentimiento.setScope(new CodeableConcept().setText(listconsent.get(i).getMotivo()));

	        consentimiento.setDateTime(new Date());
	        StringDt dur = new StringDt();
	        dur.setValueAsString(listconsent.get(i).getDur());
	        consentimiento.setDuracion(dur);

	        StringDt cond = new StringDt();
	        cond.setValueAsString(listconsent.get(i).getCond());
	        consentimiento.setCond(cond);

	        if(listconsent.get(i).getEstado().equals("draft")) {
	        	consentimiento.setStatus(ConsentState.DRAFT);
	        }
	        else {
	        	if(listconsent.get(i).getEstado().equals("rejected")) {
	        		consentimiento.setStatus(ConsentState.REJECTED);
	        	}
	        	else {
	        		if(listconsent.get(i).getEstado().equals("active")) {
		        		consentimiento.setStatus(ConsentState.ACTIVE);
	        		}
	        	}
	        }
	        consentimiento.setAlerta(new BooleanDt(listconsent.get(i).getAlerta()));
	        
	        listaconsentimientos.add(consentimiento);
		}
		return listaconsentimientos;
	}

}
