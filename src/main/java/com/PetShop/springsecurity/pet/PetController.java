package com.PetShop.springsecurity.pet;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.PetShop.springsecurity.Response.ResponseEntity;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }
    
    @PostMapping("/create")
    public com.PetShop.springsecurity.Response.ResponseEntity<String> createPet(@RequestBody @Validated Pet pet) {
        
    	try 
    	{
    		return petService.createPet(pet);
    	}
    	catch(Exception ex)
    	{
    		throw ex;
    	}
    }
    
    @GetMapping("/read")
    public ResponseEntity<List<Pet>> getAllPets() {
        
    	try 
    	{
    		return petService.getAllPets();
    	}
    	catch(Exception ex)
    	{
    		throw ex;
    	}
    }

    @GetMapping("/getPetById/{id}")
    public ResponseEntity<String> getPetById(@PathVariable Long id) {
        
    	try 
    	{
    		return petService.getPetById(id);
    	}
    	catch(Exception ex)
    	{
    		throw ex;
    	}
    }
    
    @PutMapping("/updatePet/{id}")
    public ResponseEntity<String> updatePet(@PathVariable Long id, @RequestBody @Validated Pet petRequest) {
    	try 
    	{
    		return petService.UpDatePet(id,petRequest);
    	}
    	catch(Exception ex)
    	{
    		throw ex;
    	}
    }
     
    @DeleteMapping("/deletePet/{id}")
    public ResponseEntity<String> deletePet(@PathVariable Long id) {
    	try 
    	{
    		return petService.deletePet(id);
    	}
    	catch(Exception ex)
    	{
    		throw ex;
    	}
    }

}

