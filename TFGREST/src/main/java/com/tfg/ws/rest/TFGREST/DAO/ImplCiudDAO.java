package com.tfg.ws.rest.TFGREST.DAO;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tfg.ws.rest.TFGREST.objetos.Ciudadanos;

@Repository
public class ImplCiudDAO implements InterfazCiudDAO {

	@Autowired
	private EntityManager entityManager;
	
	@Override
	public Ciudadanos accederCiud(String dni) {
		// TODO Auto-generated method stub
		Session currentSession = entityManager.unwrap(Session.class);

		Ciudadanos ciud = currentSession.get(Ciudadanos.class,dni);
		
		return ciud;
	}

	@Override
	public Boolean alertasCiud(String usu, String dni) {
		// TODO Auto-generated method stub
		return null;
	}

}
