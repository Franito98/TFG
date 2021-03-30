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

@Repository
public class ImplCiudDAO implements InterfazCiudDAO {

	private final static Logger LOGGER = Logger.getLogger("logger");
	
	@Autowired
	private EntityManager entityManager;
	
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
	public void actualizarCiud(Paciente paciente) {
		Session currentSession = entityManager.unwrap(Session.class);
	
		Ciudadanos ciud = new Ciudadanos(paciente.getIdentifier().get(0).getId(),
				paciente.getNameFirstRep().getText(),paciente.getUsu().getValue(),
				paciente.getTarjsanitaria().getValue(),Integer.valueOf(paciente.getTelecom().get(0).getValue()),
				null);
		
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
	
	@Override
	public void alertasCiud(Ciudadanos ciud) {
		// TODO Auto-generated method stub
		Session currentSession = entityManager.unwrap(Session.class);
		
		currentSession.evict(ciud);
		
		//return null;
	}

}
