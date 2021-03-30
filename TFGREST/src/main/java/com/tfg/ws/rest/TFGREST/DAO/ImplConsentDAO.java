package com.tfg.ws.rest.TFGREST.DAO;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tfg.ws.rest.TFGREST.RecursosExt.Consen;
import com.tfg.ws.rest.TFGREST.objetos.Agentes;
import com.tfg.ws.rest.TFGREST.objetos.Ciudadanos;
import com.tfg.ws.rest.TFGREST.objetos.Consentimientos;

@Repository
public class ImplConsentDAO implements InterfazConsentDAO {
	
	private final static Logger LOGGER = Logger.getLogger("logger");
	
	@Autowired
	private EntityManager entityManager;
	
	@Override
	public void crearconsent(String contra, Consen consentimiento, String ciud) {
		Session currentSession = entityManager.unwrap(Session.class);
	
		Agentes agente = new Agentes();
		agente.setContra(contra);
		Ciudadanos ciudadano = new Ciudadanos();
		ciudadano.setDni(ciud);
		
		Consentimientos consent = new Consentimientos(consentimiento.getDatos().getValue(), 
				agente, ciudadano, consentimiento.getPerformerFirstRep().getReference().toString(), 
				consentimiento.getOrganizationFirstRep().getReference().toString(), consentimiento.getCategoryFirstRep().getText().toString(), 
				consentimiento.getAccion().getValue(), consentimiento.getScope().getText(), 
				consentimiento.getDuracion().getValue(), consentimiento.getCond().getValue(),
				consentimiento.getStatus().toCode(), consentimiento.getAlerta().getValue());
		
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
	
	public List<Consentimientos> getconsentestadoA(String contra, String estado){
		
		Session currentSession = entityManager.unwrap(Session.class);
		List<Consentimientos> listaconsent = null;
		Transaction t = currentSession.beginTransaction();
		
		try {
			Query<Consentimientos> query = currentSession.createQuery("from Consentimientos C where C.agentes.contra= :contra "
					+ "and C.estado= :estado", Consentimientos.class);
			
			query.setParameter("contra", contra);
			query.setParameter("estado", estado);
					
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
	
	public List<Consentimientos> getconsentestadoC(String dni, String estado){
		
		Session currentSession = entityManager.unwrap(Session.class);
		List<Consentimientos> listaconsent = null;
		Transaction t = currentSession.beginTransaction();
		
		try {
			Query<Consentimientos> query = currentSession.createQuery("from Consentimientos C where C.ciudadanos.dni= :dni "
					+ "and C.estado= :estado", Consentimientos.class);
			
			query.setParameter("dni", dni);
			query.setParameter("estado", estado);
					
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
	public Integer eliminarConsent(Consen consentimiento) {
		Session currentSession = entityManager.unwrap(Session.class);
		
		Integer row;
		
		Transaction t = currentSession.beginTransaction();
		try {	
			@SuppressWarnings("rawtypes")
			Query query = currentSession.createQuery("delete from Consentimientos C where C.agentes.contra= :contra "
					+ "and C.ciudadanos.dni= :dest" + " and C.usuDatos= :usudatos" + " and C.datos= :datos");
			query.setParameter("contra", consentimiento.getIdentifier().get(0).getId());
			query.setParameter("dest", consentimiento.getPatient().getReference());
			query.setParameter("usudatos", consentimiento.getPerformerFirstRep().getReference());
			query.setParameter("datos", consentimiento.getDatos().getValue());
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
		return row;
	}
	
	@Override
	public Integer modificarConsent(String estado, Consen consentimiento) {
		Session currentSession = entityManager.unwrap(Session.class);
		
		Integer row;
		
		Transaction t = currentSession.beginTransaction();
		try {	
			@SuppressWarnings("rawtypes")
			Query query = currentSession.createQuery("update Consentimientos C set estado= :estado where C.agentes.contra= :contra "
					+ "and C.ciudadanos.dni= :dest" + " and C.usuDatos= :usudatos" + " and C.datos= :datos");
			query.setParameter("contra", consentimiento.getIdentifier().get(0).getId());
			query.setParameter("dest", consentimiento.getPatient().getReference());
			query.setParameter("usudatos", consentimiento.getPerformerFirstRep().getReference());
			query.setParameter("datos", consentimiento.getDatos().getValue());
			query.setParameter("estado", estado);
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
		return row;
	}
	
	@Override
	public Integer actualizarAlerta(Consen consentimiento) {
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
		return row;
	}
	
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
}
