package com.PetShop.springsecurity.Scheduling;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/Appointment")
public class AppointmentController {
	
	private final AppointmentService appointmentService;

	public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

	@PostMapping("/createAppointment")
    public com.PetShop.springsecurity.Response.ResponseEntity<String> createAppointment(@RequestBody @Valid Appointment appointment) {
    	try 
		{
    		return appointmentService.createAppointment(appointment);
    	}
    	catch(Exception ex) 
    	{
    		throw ex;
    	}
    }

	@GetMapping("/allAppointments")
    public List<Appointment> getAllAppointments() throws Exception {
    	try 
		{
    		return appointmentService.getAllAppointments();
    	}
    	catch(Exception ex) 
    	{
    		throw ex;
    	}
    }
    
    @GetMapping("/appointmentById/{id}")
    public Appointment getAppointmentById(@PathVariable Long id) {
    	
    	try 
		{
    		return appointmentService.getAppointmentById(id);
    	}
    	catch(Exception ex) 
    	{
    		throw ex;
    	}
    }
    
    @PutMapping("/updateAppointment/{id}")
    public com.PetShop.springsecurity.Response.ResponseEntity<String> updateAppointment(@PathVariable Long id, @RequestBody Appointment appointment) {
    	
    	try 
		{
    		return appointmentService.updateAppointment(id, appointment);
    		
    	}
    	catch(Exception ex) 
    	{
    		throw ex;
    	}
    }
    
    @DeleteMapping("/deleteAppointment/{id}")
    public com.PetShop.springsecurity.Response.ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
    	try 
		{
    		return appointmentService.deleteAppointment(id);
    	}
    	catch(Exception ex) 
    	{
    		throw ex;
    	}
    }
    
}
