package com.tfg.ws.rest.TFGREST.DAO;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tfg.ws.rest.TFGREST.RecursosExt.Consen;
import com.tfg.ws.rest.TFGREST.objetos.Agentes;
import com.tfg.ws.rest.TFGREST.objetos.Ciudadanos;
import com.tfg.ws.rest.TFGREST.objetos.Consentimientos;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.PerformanceOptionsEnum;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;

@Repository
public class ImplConsentDAO implements InterfazConsentDAO {
	
	private final static Logger LOGGER = Logger.getLogger("logger");
	
	@Autowired
	private EntityManager entityManager;
	
	FhirContext ctx = FhirContext.forR4();
	String serverBase = "http://hapi.fhir.org/baseR4";
	
	@Override
	public void crearconsent(String agdni, Consen consentimiento, String ciuddni) {

		ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		ctx.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);
		
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		client.setPrettyPrint(true);
		client.setEncoding(EncodingEnum.JSON);
		
		MethodOutcome outcome = client.create().resource(consentimiento).execute();
		
		if (outcome.getCreated() == true) {
			LOGGER.setLevel(Level.WARNING);
			LOGGER.warning(outcome.getId().getIdPart());
			LOGGER.warning("Creado");
		} else {
			LOGGER.setLevel(Level.WARNING);
			LOGGER.warning("Error");
		}
		
		Session currentSession = entityManager.unwrap(Session.class);
		
		Agentes agente = new Agentes();
		agente.setContra(agdni);
		Ciudadanos ciudadano = new Ciudadanos();
		ciudadano.setDni(ciuddni);
		
		Consentimientos consent = new Consentimientos(agente,ciudadano,outcome.getId().getIdPart());
		
		Transaction t = currentSession.beginTransaction();
		try {	
			currentSession.save("Consentimientos", consent);
			t.commit();
		} catch (HibernateException exc){
			t.rollback();
			LOGGER.setLevel(Level.WARNING);
			LOGGER.warning(exc.toString());
		} finally {
			entityManager.close();
		}
	}
	
	public Bundle getconsent(String id){
		
		ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		ctx.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);
		
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		client.setPrettyPrint(true);
		client.setEncoding(EncodingEnum.JSON);
		

		Bundle response = client.search().forResource(Consent.class)
				.where(Consent.RES_ID.exactly().identifier(id)).returnBundle(Bundle.class).execute();
		
		return response;
	}

	public List<Consentimientos> getidsconsentC(String dni) {
		
		Session currentSession = entityManager.unwrap(Session.class);
		List<Consentimientos> listaconsent = null;
		Transaction t = currentSession.beginTransaction();
		
		try {
			Query<Consentimientos> query = currentSession.createQuery("from Consentimientos C where C.ciudadanos.dni= :dni "
					, Consentimientos.class);
			
			query.setParameter("dni", dni);
					
			listaconsent = query.list();	
			
			t.commit();
		} catch (HibernateException exc){
			t.rollback();
			LOGGER.setLevel(Level.WARNING);
			LOGGER.warning("Error al obtener los consentimientos");
		} finally {
			entityManager.close();
		}
		return listaconsent;
	}
	
	@Override
	public MethodOutcome eliminarConsent(Consen consentimiento) {
		
		ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		ctx.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);
		
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		client.setPrettyPrint(true);
		client.setEncoding(EncodingEnum.JSON);
		
		MethodOutcome outcome = client.delete().resourceById(new IdType("Consent", consentimiento.getId().substring(8))).execute();
		
		Session currentSession = entityManager.unwrap(Session.class);
		
		Transaction t = currentSession.beginTransaction();
		
		try {	
			@SuppressWarnings("rawtypes")
			Query query = currentSession.createQuery("delete from Consentimientos C where C.idconsent= :id_consent");
			query.setParameter("id_consent", consentimiento.getId().substring(8));
			
			query.executeUpdate();
			
			t.commit();
		} catch (HibernateException exc){
			t.rollback();
			LOGGER.setLevel(Level.WARNING);
			LOGGER.warning(exc.toString());
		} finally {
			entityManager.close();
		}
		
		return outcome;
	}
	
	@Override
	public void modificarConsent(Consen consentimiento) {
		
		ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		ctx.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);
		
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		client.setPrettyPrint(true);
		client.setEncoding(EncodingEnum.JSON);
		
		MethodOutcome outcome = client.update().resource(consentimiento).execute();
		
	}
	
	@Override
	public void actualizarAviso(Consen consentimiento) {
		
		ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		ctx.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);
		
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		client.setPrettyPrint(true);
		client.setEncoding(EncodingEnum.JSON);
		
		MethodOutcome outcome = client.update().resource(consentimiento).execute();
		
		/*
		Session currentSession = entityManager.unwrap(Session.class);
		
		Integer row;
		
		Transaction t = currentSession.beginTransaction();
		try {	
			@SuppressWarnings("rawtypes")
			Query query = currentSession.createQuery("update Consentimientos C set alerta= :alerta where C.agentes.contra= :contra "
					+ "and C.ciudadanos.dni= :dest" + " and C.usuDatos= :usudatos" + " and C.datos= :datos");
			query.setParameter("contra", consentimiento.getIdentifier().get(0).getId());
			query.setParameter("dest", consentimiento.getPatient().getReference());
			query.setParameter("usudatos", consentimiento.getPerformerFirstRep().getReference());
			query.setParameter("datos", consentimiento.getDatos().getValue());
			query.setParameter("alerta", false);
			row = query.executeUpdate();
			
			t.commit();
		} catch (HibernateException exc){
			t.rollback();
			LOGGER.setLevel(Level.WARNING);
			LOGGER.warning(exc.toString());
			row = 0;
		} finally {
			entityManager.close();
		}
		*/
	}
	/*
	@Override
	public List<Consentimientos> getalertas(String dni){
		
		Session currentSession = entityManager.unwrap(Session.class);
		List<Consentimientos> listaconsent = null;
		Transaction t = currentSession.beginTransaction();
		
		try {
			Query<Consentimientos> query = currentSession.createQuery("from Consentimientos C where C.ciudadanos.dni= :dni "
					+ "and C.alerta= :alerta", Consentimientos.class);
			
			query.setParameter("dni", dni);
			query.setParameter("alerta", true);
					
			listaconsent = query.list();	
			
			t.commit();
		} catch (HibernateException exc){
			t.rollback();
			LOGGER.setLevel(Level.WARNING);
			LOGGER.warning("Error al obtener los consentimientos en alerta");
		} finally {
			entityManager.close();
		}
		return listaconsent;
	}
*/
	@Override
	public List<Consentimientos> getidsconsentA(String contra) {
		Session currentSession = entityManager.unwrap(Session.class);
		List<Consentimientos> listaconsent = null;
		Transaction t = currentSession.beginTransaction();
		
		try {
			Query<Consentimientos> query = currentSession.createQuery("from Consentimientos C where C.agentes.contra= :contra "
					, Consentimientos.class);
			
			query.setParameter("contra", contra);
					
			listaconsent = query.list();	
			
			t.commit();
		} catch (HibernateException exc){
			t.rollback();
			LOGGER.setLevel(Level.WARNING);
			LOGGER.warning("Error al obtener los consentimientos");
		} finally {
			entityManager.close();
		}
		
		return listaconsent;
	}
}
