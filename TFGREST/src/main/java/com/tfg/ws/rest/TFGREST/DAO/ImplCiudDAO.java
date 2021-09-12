package com.tfg.ws.rest.TFGREST.DAO;

import java.util.List;
import java.util.logging.*;

import javax.persistence.EntityManager;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tfg.ws.rest.TFGREST.RecursosExt.Paciente;
import com.tfg.ws.rest.TFGREST.objetos.Ciudadanos;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.PerformanceOptionsEnum;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;

@Repository
public class ImplCiudDAO implements InterfazCiudDAO {

	private final static Logger LOGGER = Logger.getLogger("logger");
	
	@Autowired
	private EntityManager entityManager;
	
	FhirContext ctx = FhirContext.forR4();
	String serverBase = "http://hapi.fhir.org/baseR4";
	
	@Override
	public Ciudadanos accederCiud(String dni) {
		// TODO Auto-generated method stub
		Session currentSession = entityManager.unwrap(Session.class);
		Ciudadanos ciud = new Ciudadanos();
		
		try {
		ciud = currentSession.get(Ciudadanos.class,dni);
		} catch (HibernateException exc){
			LOGGER.setLevel(Level.WARNING);
			LOGGER.warning("Objeto no encontrado");
		} finally {
			entityManager.close();
		}
		return ciud;
	}

	@Override
	public void actualizarCiud(Paciente paciente, Ciudadanos ciud) {
		
		ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		ctx.setPerformanceOptions(PerformanceOptionsEnum.DEFERRED_MODEL_SCANNING);
		
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		client.setPrettyPrint(true);
		client.setEncoding(EncodingEnum.JSON);
		
		MethodOutcome outcome = client.create().resource(paciente).execute();
		
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
			currentSession.merge("Ciudadanos", ciud);
			t.commit();
		} catch (HibernateException exc){
			t.rollback();
			LOGGER.setLevel(Level.WARNING);
			LOGGER.warning("Error al actualizar el ciudadano");
		} finally {
			entityManager.close();
		}
	}
	
	@Override
	public List<Ciudadanos> obtenerciuds(){
		Session currentSession = entityManager.unwrap(Session.class);
		List<Ciudadanos> listaciuds = null;
		Transaction t = currentSession.beginTransaction();
		
		try {
			Query<Ciudadanos> query = currentSession.createQuery("from Ciudadanos C where "
					+ "C.nombre != :nombre ",Ciudadanos.class);
			query.setParameter("nombre", "");
			
			listaciuds = query.list();
			
			t.commit();
		} catch (HibernateException exc){
			t.rollback();
			LOGGER.setLevel(Level.WARNING);
			LOGGER.warning("Error al obtener los ciudadanos");
		} finally {
			entityManager.close();
		}
		return listaciuds;
	}

}
