package com.PetShop.springsecurity.pet;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.PetShop.springsecurity.Response.ResponseEntity;
import com.PetShop.springsecurity.Response.ResponseEntity.ResponseEntityError;
import com.PetShop.springsecurity.Response.ResponseEntity.ResponseEntitySuccess;
import com.PetShop.springsecurity.Response.ResponseEntity.ResponseEntitySuccessValuesPet;

@Service
public class PetService {
	
	@Autowired
	private PetRepository petRepository;
	
	@Autowired
    private DataSource dataSource;
	
    LocalDateTime Now = LocalDateTime.now();

    DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String dataFormatada = formatterData.format(Now);
    
	public ResponseEntity<String> createPet(Pet petRequest) {

        
		Pet pet = new Pet();
		ResponseEntitySuccess<String> responseEntity = null;
        
        try 
        {
        	if (petRequest.getName() == null || petRequest.getName().isEmpty())
            {
            	String mensagem =  "A name cannot be blank.";
                
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
            }
            else 
            {
            	pet.setName(petRequest.getName());
            }
            if(petRequest.getAge() == null || petRequest.getAge() == 0)
            {
            	String mensagem =  "A Age cannot be blank.";
                
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
            }
            else 
            {
            	pet.setAge(petRequest.getAge());
            }
            if(petRequest.getAnimalType() == null || petRequest.getAnimalType().isEmpty())
            {
            	String mensagem =  "A Animal Type cannot be blank.";
                
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
            }
            else 
            {
            	pet.setAnimalType(petRequest.getAnimalType());
            }
            if(petRequest.getColor() == null || petRequest.getColor().isEmpty())
            {
            	String mensagem =  "A Color cannot be blank.";
                
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
            }
            else 
            {
            	pet.setColor(petRequest.getColor());
            }
            if(petRequest.getSpecies() == null || petRequest.getSpecies().isEmpty())
            {
            	String mensagem =  "A Species cannot be blank.";
                
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
            }
            else 
            {
            	pet.setSpecies(petRequest.getSpecies());
            }
            
            pet.setHeight(petRequest.getHeight());
            pet.setWeight(petRequest.getWeight());
            petRepository.save(pet);
            responseEntity = new ResponseEntitySuccess<>(true, HttpStatus.valueOf(200) ,"Registration Created Successfully", dataFormatada);
            return responseEntity;
        }
        catch(Exception ex)
        {
        	throw ex;
        }
    }

	public ResponseEntity<List<Pet>> getAllPets() {
		ResponseEntitySuccessValuesPet<List<Pet>> responseEntity = null;
	    List<Pet> pets = petRepository.findAll();
	    String mensagem =  "A name cannot be blank.";
	    responseEntity = new ResponseEntitySuccessValuesPet<>(true, HttpStatus.OK ,mensagem, dataFormatada, pets,null, null);
	    return responseEntity;
	}
	
	public ResponseEntity<String> getPetById(Long Id){
		PetData userData = new PetData(dataSource);
		
		if(Id <= 0) {
			String mensagem =  "the ID field must be filled in.";
            
            return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
		}
		var IdPet = userData.findByPetId(Id);
		if (IdPet != null ) {

            String mensagem = "User found successfully.";
            return new ResponseEntitySuccessValuesPet<>(true, HttpStatus.OK, mensagem, dataFormatada, IdPet, null, null);
        }
        else 
        {
            String mensagem = "User not found with the provided ID.";
            return new ResponseEntityError<>(false, HttpStatus.NOT_FOUND, mensagem, dataFormatada);
        }
		
	}

	public ResponseEntity<String> deletePet(Long Id){
		PetData userData = new PetData(dataSource);
		
		if(Id <= 0) {
			String mensagem =  "the ID field must be filled in.";
            
            return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
		}
		return userData.deletePetById(Id);
	}

	public ResponseEntity<String> UpDatePet(Long Id, Pet petRequest){
		PetData userData = new PetData(dataSource);
		try 
		{
			if (Id == null || Id < 0) {
			    String mensagem = "O ID deve ser um número positivo válido.";
			    return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
			}
			
			if (petRequest == null) {
			    String mensagem = "Os campos de atualização precissam ser atualizados.";
			    return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
			}
			
			return userData.UpDatePet(Id, petRequest);
		}
		catch(Exception ex) 
		{
			throw ex;
		}
	}
	
}
