package com.tfg.ws.rest.TFGREST.controller;

import java.util.List;

import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.ws.rest.TFGREST.Agenteservice.AgenteService;
import com.tfg.ws.rest.TFGREST.RecursosExt.Practicante;
import com.tfg.ws.rest.TFGREST.RecursosExt.Consen;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;

@RestController
@RequestMapping("/agente")
public class AgenteRestController {
	
	@Autowired
	private AgenteService agenteService;
	
	FhirContext ctx = new FhirContext(FhirVersionEnum.R4);
	
	@GetMapping("/acceder")
	public String accederAgente(@RequestParam(value = "contra") String contra){
		
		//return ctx.newJsonParser().setPrettyPrint(true).
			//encodeResourceToString(agenteService.accederAgente(contra));
		return agenteService.accederAgente(contra);
	}
	
	@PutMapping("/reg/{contra}")
	public String regAg(@PathVariable(value = "contra") String contra, @RequestBody String practicante){
		
		Practicante prac = ctx.newJsonParser().setPrettyPrint(true).parseResource(Practicante.class, practicante);
		
		return agenteService.regAg(contra, prac);
	}
	
	@PutMapping("/reg/hospital/{contra}")
	public String reghospital(@PathVariable(value = "contra") String contra, @RequestBody String hosp){
		
		Organization hospital = ctx.newJsonParser().setPrettyPrint(true).parseResource(Organization.class, hosp);
		
		return agenteService.reghospital(contra, hospital);
	}
	
	@PutMapping("/reg/depart/{contra}")
	public String regdepart(@PathVariable(value = "contra") String contra, @RequestBody String depart){
		
		PractitionerRole rol = ctx.newJsonParser().setPrettyPrint(true).parseResource(PractitionerRole.class, depart);
		
		return agenteService.regdepart(contra, rol);
	}
	
	@PostMapping("/solicitud/{login}")
	public String solconsent(@PathVariable(value = "login") String login,
			@RequestBody String consentim){
		
		Consen consen = ctx.newJsonParser().setPrettyPrint(true).parseResource(Consen.class, consentim);
		
		return agenteService.solconsent(login, consen);
	}
	
	@GetMapping("/{login}")
	public String getconsentestado(@PathVariable(value = "login") String login, @RequestParam(value = "estado") String estado){
		
		List<Consen> listaconsentimientos = agenteService.getconsentestado(login,estado);
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
	
	@GetMapping("/hospital/{login}")
	public String gethospital(@PathVariable(value = "login") String login){
		
		return agenteService.getHospital(login);
	}
	
	@PutMapping("/consentimiento/eliminar")
	public String eliminarConsent(@RequestBody String consentimiento){
		
		Consen consent = ctx.newJsonParser().setPrettyPrint(true).parseResource(Consen.class, consentimiento);
		
		return agenteService.eliminarConsent(consent);
	}
}
