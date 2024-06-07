package com.PetShop.springsecurity.authentication;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PetShop.springsecurity.Response.ResponseEntity;
import com.PetShop.springsecurity.user.User;


import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;


    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

	@SuppressWarnings("rawtypes")
	@PostMapping("/register")
    public com.PetShop.springsecurity.Response.ResponseEntity Register(@Valid @RequestBody User request){

    	try 
    	{
    		return authenticationService.register(request);	
    	}
    	catch(Exception ex) 
    	{
    		throw ex;
    	}
    }
	
    @SuppressWarnings("rawtypes")
	@PostMapping("/authenticate")
    public com.PetShop.springsecurity.Response.ResponseEntity Authenticate(@Valid @RequestBody AuthenticationRequest request){
        
    	try
    	{
    		return authenticationService.login(request);
    	}
    	catch(Exception ex) 
    	{
    		throw ex;
    	}
    }
    
    @SuppressWarnings("rawtypes")
	@GetMapping("/readAll")
    public com.PetShop.springsecurity.Response.ResponseEntity ReadAll()
    {
    	try
    	{
    		return authenticationService.readAll();
    	}
    	catch(Exception ex) 
    	{
    		throw ex;
    	}
    }
    
    @SuppressWarnings("rawtypes")
	@GetMapping("/socialSecurityNumber/{socialSecurityNumber}")
    public com.PetShop.springsecurity.Response.ResponseEntity readSocialSecurityNumber(@PathVariable  String socialSecurityNumber) 
    {
        try
    	{
        	ResponseEntity<List<User>> user = authenticationService.getUserBySocialSecurityNumber(socialSecurityNumber);
        	return user;
    	}
    	catch(Exception ex) 
    	{
    		throw ex;
    	}
    }
    
    @SuppressWarnings("rawtypes")
    @DeleteMapping("/user/{id}")
    public ResponseEntity deleteUserById(@PathVariable Long id) {
        try {
            return authenticationService.deleteUserById(id);
        } catch(Exception ex) {
            throw ex;
        }
    }
    
    @SuppressWarnings("rawtypes")
    @PutMapping("/userUpdate/{socialSecurityNumber}")
	public com.PetShop.springsecurity.Response.ResponseEntity Update(@Valid @RequestBody User request, @PathVariable String socialSecurityNumber)
    {
    	try
    	{
    		return authenticationService.updateUser(socialSecurityNumber,request);
    	}
    	catch(Exception ex) 
    	{
    		throw ex;
    	}	
    }
    
    @SuppressWarnings("rawtypes")
    @DeleteMapping("/userDelete/{socialSecurityNumber}")
	public com.PetShop.springsecurity.Response.ResponseEntity deleteUserBySocialSecurityNumber(@PathVariable String socialSecurityNumber) 
    {    
		try 
		{
			return authenticationService.deleteUserBySocialSecurityNumber(socialSecurityNumber);
	    }
	    catch(Exception ex)
	    {
	    	throw ex;
	    }
	}
    
}
