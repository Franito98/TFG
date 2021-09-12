package com.tfg.ws.rest.TFGREST.Ciudservice;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Consent.ConsentState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.ws.rest.TFGREST.DAO.InterfazAgenteDAO;
import com.tfg.ws.rest.TFGREST.DAO.InterfazCiudDAO;
import com.tfg.ws.rest.TFGREST.DAO.InterfazConsentDAO;
import com.tfg.ws.rest.TFGREST.RecursosExt.Consen;
import com.tfg.ws.rest.TFGREST.RecursosExt.Paciente;
import com.tfg.ws.rest.TFGREST.objetos.Agentes;
import com.tfg.ws.rest.TFGREST.objetos.Ciudadanos;
import com.tfg.ws.rest.TFGREST.objetos.Consentimientos;

import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.StringDt;

@Service
public class ImplCiudService implements CiudService {
	
	@Autowired
	private InterfazCiudDAO intCiudDAO;
	
	@Autowired
	private InterfazAgenteDAO intAgenteDAO;
	
	@Autowired
	private InterfazConsentDAO intConsentDAO;
	
	@Override
	public String accederCiud(String dni) {
		// TODO Auto-generated method stub
		Ciudadanos ciud = new Ciudadanos();
		ciud = intCiudDAO.accederCiud(dni);
		String cod;
		
		if(ciud == null) {
			cod = "{\n  \"codigo\": 400\n}";
		}
		else {
			if (ciud.getUsu() == null) {
				cod = "{\n  \"codigo\": 300\n}";
			}
			else {
				cod = "{\n  \"codigo\": 200,  \n"
						+ "  \"usu\": " + ciud.getUsu() + "\n}";
			}
		}
		return cod;
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
					
					Ciudadanos ciudadano = new Ciudadanos(paciente.getIdentifier().get(0).getId(),
							paciente.getNameFirstRep().getText(),paciente.getUsu().getValue(),
							paciente.getTarjsanitaria().getValue(),Integer.valueOf(paciente.getTelecom().get(0).getValue()),
							null);
					intCiudDAO.actualizarCiud(paciente,ciudadano);
					
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
		
		List<Consentimientos> listaconsent = intConsentDAO.getidsconsentC(dni);
		
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
	public String modificarConsent(String estado, Consen consentimiento) {
		String cod;
		
		consentimiento.setStatus(ConsentState.fromCode(estado));
		intConsentDAO.modificarConsent(consentimiento);
		
		Bundle response = intConsentDAO.getconsent(consentimiento.getId());
		Consent consen = (Consent) response.getEntry().get(0).getResource();
		
		if(consen.getStatus() == Consent.ConsentState.fromCode(estado)) {
			cod = "{\n  \"codigo\": {\n  \"valueInteger\": 200\n  }\n}";
		} else {
			cod = "{\n  \"codigo\": {\n  \"valueInteger\": 400\n  }\n}";
		}
		return cod;
	}
	
	@Override
	public String actualizarAviso(Consen consentimiento) {
		
		String cod;
		
		consentimiento.setAviso(new BooleanDt(false));
		intConsentDAO.actualizarAviso(consentimiento);
		
		Bundle response = intConsentDAO.getconsent(consentimiento.getId());
		Consent consen = (Consent) response.getEntry().get(0).getResource();
		
		if(consen.getExtension().get(3).getValueAsPrimitive().getValue().equals(false)) {
			cod = "{\n  \"codigo\": {\n  \"valueInteger\": 200\n  }\n}";
		} else {
			cod = "{\n  \"codigo\": {\n  \"valueInteger\": 400\n  }\n}";
		}
		return cod;	
	}
	
	@Override
	public List<Consen> getavisosCiud(String dni) {
		List<Consentimientos> listaconsent = intConsentDAO.getidsconsentC(dni);
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
			
			if(extension.get(3).getValueAsPrimitive().getValue().equals(true)) {
				consentimiento.setDatos(new StringDt(extension.get(0).getValueAsPrimitive().getValueAsString()));
				consentimiento.setDuracion(new StringDt(extension.get(1).getValueAsPrimitive().getValueAsString()));
				consentimiento.setCond(new StringDt(extension.get(2).getValueAsPrimitive().getValueAsString()));
				if(extension.get(3).getValueAsPrimitive().getValue().equals(true)) {
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
	public String getHospital(String dni) {
		
		String hospital = "{\n  \"hospital\": "+intAgenteDAO.getAgDNI(dni).getHospital()+"\n}";
		
		return hospital;
	}

	@Override
	public Agentes getNombre(String dni) {
		Agentes agente = intAgenteDAO.getAgDNI(dni);
		
		return agente;
	}
	
}
