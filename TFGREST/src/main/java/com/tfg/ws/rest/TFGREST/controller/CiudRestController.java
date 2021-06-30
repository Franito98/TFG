package com.tfg.ws.rest.TFGREST.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.ws.rest.TFGREST.Ciudservice.CiudService;
import com.tfg.ws.rest.TFGREST.RecursosExt.Consen;
import com.tfg.ws.rest.TFGREST.RecursosExt.Paciente;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;

@RestController
@RequestMapping("/ciud")
public class CiudRestController {

	@Autowired
	private CiudService ciudService;
	
	FhirContext ctx = new FhirContext(FhirVersionEnum.R4);
	
	@GetMapping("/acceder")
	public String accederCiud(@RequestParam(value = "dni") String dni){
		
		return ctx.newJsonParser().setPrettyPrint(true).
				encodeResourceToString(ciudService.accederCiud(dni));
	}
	
	@PutMapping("/reg/{dni}")
	public String regCiud(@PathVariable(value = "dni") String dni, @RequestBody String paciente){
		
		Paciente pac = ctx.newJsonParser().setPrettyPrint(true).parseResource(Paciente.class, paciente);
		
		return ciudService.regCiud(dni, pac);
		
	}
	
	@GetMapping("/{dni}")
	public String getconsentestado(@PathVariable(value = "dni") String dni, @RequestParam(value = "estado") String estado){
		
		List<Consen> listaconsentimientos = ciudService.getconsentestado(dni,estado);
		String lista = "{\n  \"consentimientos\": [\n\t";
		int contador;
		for(contador=0; contador<listaconsentimientos.size(); contador++) {
			lista = lista + ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(listaconsentimientos.get(contador));
			if(contador!=listaconsentimientos.size()-1) {
				lista = lista + ",\n\t";
			}
		}
		lista = lista + "\n\t]\n}";
		return lista;
	}
	
	@PutMapping("/consentimiento/modificar")
	public String modificarConsent(@RequestParam(value = "estado") String estado, @RequestBody String consentimiento){
		
		Consen consent = ctx.newJsonParser().setPrettyPrint(true).parseResource(Consen.class, consentimiento);
		
		return ciudService.modificarConsent(estado,consent);
	}
	
	@PutMapping("/consentimiento/actualizaralerta")
	public String actualizarAlerta(@RequestBody String consentimiento){
		
		Consen consent = ctx.newJsonParser().setPrettyPrint(true).parseResource(Consen.class, consentimiento);
		
		return ciudService.actualizarAlerta(consent);
	}
	
	@GetMapping("/{dni}/alertas")
	public String getconsentAlertas(@PathVariable(value = "dni") String dni){
		
		List<Consen> listaconsentimientos = ciudService.getalertasCiud(dni);
		String lista = "{\n  \"consentimientos\": [\n\t";
		int contador;
		for(contador=0; contador<listaconsentimientos.size(); contador++) {
			lista = lista + ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(listaconsentimientos.get(contador));
			if(contador!=listaconsentimientos.size()-1) {
				lista = lista + ",\n\t";
			}
		}
		lista = lista + "\n\t]\n}";
		return lista;
	}
}
