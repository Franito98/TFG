package com.tfg.ws.rest.TFGREST.Agenteservice;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.PractitionerRole;
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
import com.tfg.ws.rest.TFGREST.objetos.Consentimientos;

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
	public String accederAgente(String contra) {
		
		Agentes agente = new Agentes();
		agente = intAgenteDAO.accederAgente(contra);
		String cod;
		
		if(agente == null) {
			cod = "{\n  \"codigo\": 400\n}";
		}
		else {
			if (agente.getUsu() == null) {
				cod = "{\n  \"codigo\": 300\n}";
			}
			else {
				
				cod = "{\n  \"codigo\": 200,  \n"
						+ "\"usu\": " + agente.getUsu() + "\n}";
			}
		}
		return cod;
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
					Agentes ag = new Agentes(prac.getContra().getValue(),prac.getIdentifier().get(0).getId(),
							prac.getNameFirstRep().getText(),prac.getUsu().getValue(),
							agente.getHospital(),agente.getDepart(),
							prac.getCodigo().getValue(), null);
					intAgenteDAO.actualizarAg(prac,ag);
						
					agente = intAgenteDAO.accederAgente(contra);
						
					if(agente.getUsu() == prac.getUsu().getValue() &&
							agente.getNombre() == prac.getNameFirstRep().getText() &&
							agente.getCodigo() == prac.getCodigo().getValue()) {
						
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
	public String reghospital(String contra, Organization hospital) {
		
		Agentes agente = new Agentes();
		agente = intAgenteDAO.accederAgente(contra);
		String cod;
					
		if(!agente.getHospital().equals(hospital.getName()))  {
			cod = "{\n  \"codigo\": {\n  \"valueInteger\": 600\n  }\n}";
			}
			else {
				intAgenteDAO.actualizarHospital(hospital);
						
				cod = "{\n  \"codigo\": {\n  \"valueInteger\": 200\n  }\n}";
			}
		return cod;
	}
	
	@Override
	public String regdepart(String contra, PractitionerRole depart) {
		
		Agentes agente = new Agentes();
		agente = intAgenteDAO.accederAgente(contra);
		String cod;
		
		if(!agente.getDepart().equals(depart.getCodeFirstRep().getText()))  {
			cod = "{\n  \"codigo\": {\n  \"valueInteger\": 600\n  }\n}";
			}
			else {
				intAgenteDAO.actualizarDepart(depart);
						
				cod = "{\n  \"codigo\": {\n  \"valueInteger\": 200\n  }\n}";
			}
		
		return cod;
	}
	@Override
	public String solconsent(String login, Consen consentimiento) {
		
		Agentes agente = new Agentes();
		agente = intAgenteDAO.getAgLogin(login);
		String cod;
		
		Reference refprac = new Reference();
        refprac.setReference("http://hapi.fhir.org/Practitioner");
        refprac.setType("Practitioner");
        refprac.setIdentifier(new Identifier().setValue(agente.getDni()));
        consentimiento.addPerformer(refprac);
        
        Reference refubi = consentimiento.getOrganizationFirstRep();
        refubi.setIdentifier(new Identifier().setValue(agente.getDni()));
        consentimiento.addOrganization(refubi);
        
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
				intConsentDAO.crearconsent(agente.getContra(), consentimiento, listaciuds.get(i).getDni());
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
					intConsentDAO.crearconsent(agente.getContra(), consentimiento, ciud.getDni());
					cod = "{\n  \"codigo\": {\n  \"valueInteger\": 200\n  }\n}";
				}
			}
		}
		return cod;
	}
	
	@Override
	public List<Consen> getconsentestado(String login, String estado) {
		String contra = intAgenteDAO.getAgLogin(login).getContra();
		List<Consentimientos> listaconsent= intConsentDAO.getidsconsentA(contra);		
		
		List<Consen> listaconsentimientos = new ArrayList<Consen>();
		
		List<Consent> listconsent = new ArrayList<Consent>();
		
		for(int i = 0; i<listaconsent.size(); i++) {
			Bundle response = intConsentDAO.getconsent(listaconsent.get(i).getIdconsent());
			BundleEntryComponent entry = response.getEntry().get(0);
			
			listconsent.add((Consent) entry.getResource());
			
		}
		
		for(int i = 0; i<listconsent.size(); i++) {
			Consen consentimiento = new Consen();
			
			List<Extension> extension = listconsent.get(i).getExtension();
			
			if(listconsent.get(i).getStatus() == Consent.ConsentState.fromCode(estado)) {
				consentimiento.setDatos(new StringDt(extension.get(0).getValueAsPrimitive().getValueAsString()));
				consentimiento.setDuracion(new StringDt(extension.get(1).getValueAsPrimitive().getValueAsString()));
				consentimiento.setCond(new StringDt(extension.get(2).getValueAsPrimitive().getValueAsString()));
				if(extension.get(3).getValueAsPrimitive().getValueAsString().equals(true)) {
					consentimiento.setAviso(new BooleanDt(true));
				} else {
					consentimiento.setAviso(new BooleanDt(false));
				}
				consentimiento.addOrganization(listconsent.get(i).getOrganizationFirstRep());
				consentimiento.addPerformer(listconsent.get(i).getPerformerFirstRep());
				consentimiento.setProvision(listconsent.get(i).getProvision());
				consentimiento.setStatus(listconsent.get(i).getStatus());
				consentimiento.setScope(listconsent.get(i).getScope());
				consentimiento.setCategory(listconsent.get(i).getCategory());
				consentimiento.setPatient(listconsent.get(i).getPatient());
				consentimiento.setDateTime(listconsent.get(i).getDateTime());
				
				consentimiento.setId(listconsent.get(i).getId());
				
				listaconsentimientos.add(consentimiento);
			}
		}
		
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
	public String getHospital(String login) {
		
		String hospital = "{\n  \"hospital\": "+intAgenteDAO.getAgLogin(login).getHospital()+"\n}";
		
		return hospital;
	}

}
