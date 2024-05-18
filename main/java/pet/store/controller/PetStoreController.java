package pet.store.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreCustomer;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.service.PetStoreService;


//Using the annotations below to tell Spring that this is a REST controller so it will expect JSON requests/responses.
//Tells Spring that the URI of every request via HTTP must begin with "/pet_store" because of the mapping
//Used the lombok logger 
@RestController
@RequestMapping("/pet_store")
@Slf4j
public class PetStoreController {
	
	//Created an instance variable and used the Autowire annotation
	@Autowired
	private PetStoreService petStoreService;
	
	
	
	 @DeleteMapping("/{petStoreId}")
	 @Transactional 
	 public Map<String, String> deletePetStoreById(@PathVariable Long petStoreId) { 
		 petStoreService.deletePetStoreById(petStoreId);
		 Map<String, String> response = new HashMap<>(); 
		 response.put("message", "Pet Store deleted successfully.");
	 
	 return response; 
	 }
	

	
	@GetMapping("/{petStoreId}")
	public PetStoreData getPetStoreData(@PathVariable Long petStoreId) {
		return petStoreService.retrievePetStoreById(petStoreId);
	}
	
	
	@GetMapping
	public List<PetStoreData> listAllPetStores() {
		return petStoreService.retrieveAllPetStores();
	}
	
	
	@PostMapping("/{petStoreId}/customer")
	@ResponseStatus(HttpStatus.CREATED)
	public PetStoreCustomer addCustomerToPetStore(@PathVariable Long petStoreId, @RequestBody PetStoreCustomer customer) {
		log.info("Adding customer to pet store with ID = {}", petStoreId);
		return petStoreService.saveCustomer(petStoreId, customer);
	}
	
	//Method to add an employee to the employee table
	@PostMapping("/{petStoreId}/employee")
	@ResponseStatus(HttpStatus.CREATED)
	public PetStoreEmployee addEmployeeToPetStore(@PathVariable Long petStoreId, @RequestBody PetStoreEmployee employee) {
		log.info("Adding employee to pet store with ID = {}", petStoreId);
		return petStoreService.saveEmployee(petStoreId, employee);
	}
	
	//I was getting an error from the ARC when trying to post, but I forgot to add the PostMapping
	@PostMapping("/pet_store")
	@ResponseStatus(HttpStatus.CREATED)
	public PetStoreData createPetStore(@RequestBody PetStoreData petStoreData) {
		log.info("Request to create a new pet store: {}", petStoreData);
		return petStoreService.savePetStore(petStoreData);
	}
	
	//Method for Modification using the PUT
	@PutMapping("/{storeId}")
	public PetStoreData updatePetStore(@PathVariable Long storeId, @RequestBody PetStoreData petStoreData) {
		log.info("Update to pet store with ID {}: {}", storeId, petStoreData);
		//Set the pet Store ID from the path variab;e
		petStoreData.setPetStoreId(storeId);
		
		return petStoreService.savePetStore(petStoreData);
	}

}
