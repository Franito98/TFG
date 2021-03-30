package com.tfg.ws.rest.TFGREST.DAO;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tfg.ws.rest.TFGREST.RecursosExt.Practicante;
import com.tfg.ws.rest.TFGREST.objetos.Agentes;

@Repository
public class ImplAgenteDAO implements InterfazAgenteDAO {

	private final static Logger LOGGER = Logger.getLogger("logger");
	
	@Autowired
	private EntityManager entityManager;
	
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
	public void actualizarAg(Practicante prac) {
		Session currentSession = entityManager.unwrap(Session.class);
	
		Agentes ag = new Agentes(prac.getIdentifier().get(0).getId(),
				prac.getNameFirstRep().getText(),prac.getUsu().getValue(),
				prac.getHospital().getValue(), prac.getDepart().getValue(),
				prac.getCodigo().getValue(), null);
		
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
	public Boolean alertasAgente() {
		// TODO Auto-generated method stub
		return null;
	}
}
