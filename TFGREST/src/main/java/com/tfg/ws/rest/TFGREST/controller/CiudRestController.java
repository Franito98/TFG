package com.tfg.ws.rest.TFGREST.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.ws.rest.TFGREST.Ciudservice.CiudService;
import com.tfg.ws.rest.TFGREST.RecursosExt.Paciente;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
@RestController
@RequestMapping("/ciud")
public class CiudRestController {

	@Autowired
	private CiudService ciudService;
	
	@GetMapping("/acceder")
	public Paciente accederCiud(@RequestParam(value = "dni") String dni){
		//FhirContext prop = new FhirContext(FhirVersionEnum.R4);
		Paciente p = ciudService.accederCiud(dni);
		return p;
		//return prop.newJsonParser();
	}
}
