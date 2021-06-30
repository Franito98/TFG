package com.tfg.ws.rest.TFGREST.Agenteservice;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.UriType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.ws.rest.TFGREST.DAO.InterfazAgenteDAO;
import com.tfg.ws.rest.TFGREST.DAO.InterfazCiudDAO;
import com.tfg.ws.rest.TFGREST.DAO.InterfazConsentDAO;
import com.tfg.ws.rest.TFGREST.RecursosExt.Consen;
import com.tfg.ws.rest.TFGREST.RecursosExt.Practicante;
import com.tfg.ws.rest.TFGREST.objetos.Agentes;
import com.tfg.ws.rest.TFGREST.objetos.Ciudadanos;

import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.api.MethodOutcome;

@Service
public class ImplAgenteService implements AgenteService {
	
	@Autowired
	private InterfazAgenteDAO intAgenteDAO;
	
	@Autowired
	private InterfazCiudDAO intCiudDAO;
	
	@Autowired
	private InterfazConsentDAO intConsentDAO;
	
	@Override
	public Practicante accederAgente(String contra) {
		
		Agentes agente = new Agentes();
		agente = intAgenteDAO.accederAgente(contra);
		Practicante prac = new Practicante();
		
		if(agente == null) {
			prac.setCodmensaje(new IntegerDt(400));
		}
		else {
			if (agente.getUsu() == null) {
				prac.setCodmensaje(new IntegerDt(300));
			}
			else {
				
				Identifier id = new Identifier();
				id.setValue(agente.getContra());
				id.setSystemElement(new UriType("http://localhost:8080/TFGREST/agente/" + agente.getDni()));
				prac.addIdentifier(id);
				
				HumanName nombre = new HumanName();
				nombre.setText(agente.getNombre());
				prac.addName(nombre);
				
				StringDt usu = new StringDt();
				usu.setValueAsString(agente.getUsu());
				prac.setUsu(usu);
				
				StringDt contrasena = new StringDt();
	            contrasena.setValueAsString(agente.getContra());
	            prac.setContra(contrasena);
	            
				StringDt hosp = new StringDt();
				hosp.setValueAsString(agente.getHospital());
				prac.setHospital(hosp);
				
				StringDt depart = new StringDt();
				depart.setValueAsString(agente.getDepart());
				prac.setDepart(depart);
				
				StringDt cod = new StringDt();
				cod.setValueAsString(agente.getCodigo());
				prac.setCodigo(cod);
				
				prac.setCodmensaje(new IntegerDt(200));
			}
		}
		return prac;
	}

	@Override
	public String regAg(String contra, Practicante prac) {
		
		Agentes agente = new Agentes();
		agente = intAgenteDAO.accederAgente(contra);
		String cod;
		
		if(agente == null) {
			cod = "{\n  \"codigo\": {\n  \"valueInteger\": 400\n  }\n}";
		}
		else {
			if(agente.getUsu() != null) {
				cod = "{\n  \"codigo\": {\n  \"valueInteger\": 200\n  }\n}";
			}
			else {
				if(!agente.getCodigo().equals(prac.getCodigo().getValue())) {
					cod = "{\n  \"codigo\": {\n  \"valueInteger\": 500\n  }\n}";
				}
				else {
					if(!agente.getHospital().equals(prac.getHospital().getValue()) ||
							!agente.getDepart().equals(prac.getDepart().getValue())) {
						cod = "{\n  \"codigo\": {\n  \"valueInteger\": 600\n  }\n}";
					}
					else {
						intAgenteDAO.actualizarAg(prac);
						
						agente = intAgenteDAO.accederAgente(contra);
						
						if(agente.getUsu() == prac.getUsu().getValue() &&
								agente.getNombre() == prac.getNameFirstRep().getText() &&
								agente.getHospital() == prac.getHospital().getValue() &&
								agente.getDepart() == prac.getDepart().getValue() &&
								agente.getCodigo() == prac.getCodigo().getValue()) {
						
							cod = "{\n  \"codigo\": {\n  \"valueInteger\": 300\n  }\n}";
						}
						else {
							cod = "{\n  \"codigo\": {\n  \"valueInteger\": 100\n  }\n}";
						}
					}
				}
			}
		}
		return cod;
	}
	
	@Override
	public String solconsent(String contra, Consen consentimiento) {
		
		Agentes agente = new Agentes();
		agente = intAgenteDAO.accederAgente(contra);
		String cod;
		
		if(!agente.getNombre().equals(consentimiento.getIdentifier().get(0).getId())) {
			cod = "{\n  \"codigo\": {\n  \"valueInteger\": 600\n  }\n}";
		}
		else {
			Ciudadanos ciud = new Ciudadanos();
			
			if(consentimiento.getPatient().getReference().equals("todos")) {
				List<Ciudadanos> listaciuds = intCiudDAO.obtenerciuds();
				int contador = 0;
				for(int i = 0; i<listaciuds.size(); i++) {
					
					Reference referencia = new Reference();
		            referencia.setReference("http://hapi.fhir.org/Patient");
		            referencia.setType("Patient");
		            referencia.setIdentifier(new Identifier().setValue(listaciuds.get(i).getDni()));
		            
					consentimiento.setPatient(referencia);
					intConsentDAO.crearconsent(contra, consentimiento, listaciuds.get(i).getDni());
					contador++;
				}
				if(contador != listaciuds.size()) {
					cod = "{\n  \"codigo\": {\n  \"valueInteger\": 500\n  }\n}";
				}
				else {
					cod = "{\n  \"codigo\": {\n  \"valueInteger\": 200\n  }\n}";
				}
			}
			else {
				ciud = intCiudDAO.accederCiud(consentimiento.getPatient().getIdentifier().getValue());
				
				if(ciud == null) {
					cod = "{\n  \"codigo\": {\n  \"valueInteger\": 400\n  }\n}";
				}
				else {
					if(ciud.getUsu() == null) {
						cod = "{\n  \"codigo\": {\n  \"valueInteger\": 100\n  }\n}";
					}
					else {
						intConsentDAO.crearconsent(contra, consentimiento, ciud.getDni());
						cod = "{\n  \"codigo\": {\n  \"valueInteger\": 200\n  }\n}";
					}
				}
			}
		}
		
		return cod;
	}
	
	@Override
	public List<Consen> getconsentestado(String login, String estado) {
		
		String dni = intAgenteDAO.getDNI(login);
		Bundle response = intConsentDAO.getconsentA(dni);
		
		List<Consen> listaconsentimientos = new ArrayList<Consen>();
		
		List<BundleEntryComponent> listentries = response.getEntry();
		
		for(int i = 0; i<listentries.size(); i++) {
			BundleEntryComponent entry = listentries.get(i);
			Consent consent = (Consent) entry.getResource();
			Consen consentimiento = new Consen();
			
			List<Extension> extension = consent.getExtension();
			
			if(consent.getStatus() == Consent.ConsentState.fromCode(estado)) {
				consentimiento.setUsudatos(new StringDt(extension.get(0).getValueAsPrimitive().getValueAsString()));
				consentimiento.setUbidatos(new StringDt(extension.get(1).getValueAsPrimitive().getValueAsString()));
				consentimiento.setDatos(new StringDt(extension.get(2).getValueAsPrimitive().getValueAsString()));
				consentimiento.setAccion(new StringDt(extension.get(3).getValueAsPrimitive().getValueAsString()));
				consentimiento.setDuracion(new StringDt(extension.get(4).getValueAsPrimitive().getValueAsString()));
				consentimiento.setCond(new StringDt(extension.get(5).getValueAsPrimitive().getValueAsString()));
				if(extension.get(6).getValueAsPrimitive().getValueAsString().equals(true)) {
					consentimiento.setAviso(new BooleanDt(true));
				} else {
					consentimiento.setAviso(new BooleanDt(false));
				}
				consentimiento.setIdentifier(consent.getIdentifier());
				consentimiento.setStatus(consent.getStatus());
				consentimiento.setScope(consent.getScope());
				consentimiento.setCategory(consent.getCategory());
				consentimiento.setPatient(consent.getPatient());
				consentimiento.setDateTime(consent.getDateTime());
				
				consentimiento.setId(consent.getId());
				
				listaconsentimientos.add(consentimiento);
			}
		}
		
		
		/*
		List<Consentimientos> listconsent = intConsentDAO.getconsentestadoA(contra,estado);
		List<Consen> listaconsentimientos = new ArrayList<Consen>();
		
		for(int i = 0; i<listconsent.size(); i++) {
			
			Consen consentimiento = new Consen();
			
			Identifier id = new Identifier();
	        id.setId(listconsent.get(i).getAgentes().getContra());
	        id.setSystemElement(new UriType("http://localhost:8080/TFGREST/consentimiento/" + contra));
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
		*/
		return listaconsentimientos;
	}
	
	@Override
	public String eliminarConsent(Consen consentimiento) {
		String cod;
		MethodOutcome response = intConsentDAO.eliminarConsent(consentimiento);
		
		if(response.getOperationOutcome() != null) {
			cod = "{\n  \"codigo\": {\n  \"valueInteger\": 200\n  }\n}";
		} else {
			cod = "{\n  \"codigo\": {\n  \"valueInteger\": 400\n  }\n}";
		}
		return cod;
	}
	
	@Override
	public Boolean alertasAgente(String usu, String contra) {
		return null;
	}

}
