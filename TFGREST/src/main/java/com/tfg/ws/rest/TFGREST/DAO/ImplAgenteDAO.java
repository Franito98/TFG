package com.tfg.ws.rest.TFGREST.DAO;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tfg.ws.rest.TFGREST.objetos.Agentes;

@Repository
public class ImplAgenteDAO implements InterfazAgenteDAO {

	@Autowired
	private EntityManager entityManager;
	
	@Override
	public Agentes accederAgente(String contra) {
		// TODO Auto-generated method stub
		Session currentSession = entityManager.unwrap(Session.class);

		Agentes agente = currentSession.get(Agentes.class,contra);
		
		return agente;
	}
	
	@Override
	public Boolean alertasAgente() {
		// TODO Auto-generated method stub
		return null;
	}
}
