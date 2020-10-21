package com.tfg.ws.rest.TFGREST.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.ws.rest.TFGREST.Agenteservice.AgenteService;
import com.tfg.ws.rest.TFGREST.RecursosExt.Practicante;

@RestController
@RequestMapping("/agente")
public class AgenteRestController {

	@Autowired
	private AgenteService agenteService;
	
	@GetMapping("/acceder")
	public Practicante accederAgente(@RequestParam(value = "contra") String contra){
		
		return agenteService.accederAgente(contra);
	}
}
