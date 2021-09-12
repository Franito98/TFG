package com.tfg.ws.rest.TFGREST.DAO;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tfg.ws.rest.TFGREST.RecursosExt.Practicante;
import com.tfg.ws.rest.TFGREST.objetos.Agentes;
import com.tfg.ws.rest.TFGREST.objetos.Consentimientos;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.PerformanceOptionsEnum;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;

@Repository
public class ImplAgenteDAO implements InterfazAgenteDAO {

	private final static Logger LOGGER = Logger.getLogger("logger");
	
	@Autowired
	private EntityManager entityManager;
	
	FhirContext ctx = FhirContext.forR4();
	String serverBase = "http://hapi.fhir.org/baseR4";
	
	@Override
	public Agentes accederAgente(String contra) {
		// TODO Auto-generated method stub
		Session currentSession = entityManager.unwrap(Session.class);
		Agentes agente = new Agentes();
		
		try {
		agente = currentSession.get(Agentes.class,contra);
		} catch (HibernateException exc){
			LOGGER.setLevel(Level.WARNING);
			LOGGER.warning("Objeto no encontrado");
		} finally {
			entityManager.close();
		}
		return agente;
	}
	
	@Override
	public void actualizarAg(Practicante prac, Agentes ag) {
		
		ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		ctx.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);
		
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		client.setPrettyPrint(true);
		client.setEncoding(EncodingEnum.JSON);
		
		MethodOutcome outcome = client.create().resource(prac).execute();
		
		if (outcome.getCreated() == true) {
			LOGGER.setLevel(Level.WARNING);
			LOGGER.warning(outcome.getId().toString());
			LOGGER.warning("Creado");
		} else {
			LOGGER.setLevel(Level.WARNING);
			LOGGER.warning("Error");
		}
		
		Session currentSession = entityManager.unwrap(Session.class);
		Transaction t = currentSession.beginTransaction();
		
		try {	
			currentSession.merge("Agentes", ag);
			t.commit();
		} catch (HibernateException exc){
			t.rollback();
			LOGGER.setLevel(Level.WARNING);
			LOGGER.warning("Error al actualizar el agente");
		} finally {
			entityManager.close();
		}
	}
	
	@Override
	public void actualizarHospital(Organization hospital) {
		
		ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		ctx.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);
		
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		client.setPrettyPrint(true);
		client.setEncoding(EncodingEnum.JSON);
		
		MethodOutcome outcome = client.create().resource(hospital).execute();
		
		if (outcome.getCreated() == true) {
			LOGGER.setLevel(Level.WARNING);
			LOGGER.warning(outcome.getId().toString());
			LOGGER.warning("Creado");
		} else {
			LOGGER.setLevel(Level.WARNING);
			LOGGER.warning("Error");
		}
	}
	
	@Override
	public void actualizarDepart(PractitionerRole depart) {
		
		ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		ctx.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);
		
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		client.setPrettyPrint(true);
		client.setEncoding(EncodingEnum.JSON);
		
		MethodOutcome outcome = client.create().resource(depart).execute();
		
		if (outcome.getCreated() == true) {
			LOGGER.setLevel(Level.WARNING);
			LOGGER.warning(outcome.getId().toString());
			LOGGER.warning("Creado");
		} else {
			LOGGER.setLevel(Level.WARNING);
			LOGGER.warning("Error");
		}
	}
	
	@Override
	public Agentes getAgLogin(String login) {
		// TODO Auto-generated method stub
		Session currentSession = entityManager.unwrap(Session.class);
		Transaction t = currentSession.beginTransaction();
		Agentes agente = new Agentes();
		
		try {
			Query<Agentes> query = currentSession.createQuery("from Agentes A where A.usu= :login "
					, Agentes.class);
			
			query.setParameter("login", login);
					
			agente = query.uniqueResult();	
			
			t.commit();
		} catch (HibernateException exc){
			t.rollback();
			LOGGER.setLevel(Level.WARNING);
			LOGGER.warning("Error al obtener la contrasena");
		} finally {
			entityManager.close();
		}
		
		return agente;
	}
	
	public Agentes getAgDNI(String dni){
		Session currentSession = entityManager.unwrap(Session.class);
		Transaction t = currentSession.beginTransaction();
		Agentes agente = new Agentes();
		
		try {
			Query<Agentes> query = currentSession.createQuery("from Agentes A where A.dni= :dni "
					, Agentes.class);
			
			query.setParameter("dni", dni);
					
			agente = query.getSingleResult();	
			
			t.commit();
		} catch (HibernateException exc){
			t.rollback();
			LOGGER.setLevel(Level.WARNING);
			LOGGER.warning("Error al obtener el nombre del agente");
		} finally {
			entityManager.close();
		}
		
		return agente;
	}
	
}
