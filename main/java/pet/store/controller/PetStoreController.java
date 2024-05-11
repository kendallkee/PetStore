package pet.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreData;
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
