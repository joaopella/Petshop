package com.PetShop.springsecurity.Scheduling;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.PetShop.springsecurity.Response.ResponseEntity;
import com.PetShop.springsecurity.Response.ResponseEntity.ResponseEntityError;
import com.PetShop.springsecurity.pet.Pet;
import com.PetShop.springsecurity.pet.PetRepository;

@Service
public class AppointmentService {
	
	@Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;
	
    @Autowired
    private AppointmentData appointmentData;
    
	LocalDateTime Now = LocalDateTime.now();

    DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String dataFormatada = formatterData.format(Now);
	
    public ResponseEntity<String> createAppointment(Appointment appointment) {
    	
    	try 
    	{
    		if (appointment == null) {
                String mensagem = "You need to fill in a field to save the new employee.";
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
            }
        	
        	if(appointment.getPet().getId() == null) 
        	{
        		
        	}
        	
            if (appointment.getScheduledTime() == null) {
                String mensagem = "The name cannot be blank.";
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
            }

            if (appointment.getService().getId() == null) {
                String mensagem = "The position cannot be blank.";
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
            }

            if (appointment.getClientName() == null){
                String mensagem = "Invalid email format.";
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
            }

            if (!StringUtils.hasText(appointment.getPetName())) {
                String mensagem = "Invalid phone format.";
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
            }
    		
         // Fetch and set the related entities if IDs are provided
            if (appointment.getPet() != null && appointment.getPet().getId() != null) {
                Pet pet = petRepository.findById(appointment.getPet().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid pet ID"));
                appointment.setPet(pet);
            }

            if (appointment.getService() != null && appointment.getService().getId() != null) {
            	com.PetShop.springsecurity.Scheduling.Service service = serviceRepository.findById(appointment.getService().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid service ID"));
                appointment.setService(service);
            }

            if (appointment.getEmployee() != null && appointment.getEmployee().getId() != null) {
                Employee employee = employeeRepository.findById(appointment.getEmployee().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid employee ID"));
                appointment.setEmployee(employee);
            }
            
            return appointmentData.createAppointment(appointment);
    	} 
    	catch(Exception ex) 
    	{
    		throw ex;
    	}
    }

    public List<Appointment> getAllAppointments() throws Exception {
       try 
       {
    	   
    	   return appointmentRepository.findAll();
       }
       catch(Exception ex) 
       {
    	   throw ex;
       }
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    public ResponseEntity<String> updateAppointment(Long id, Appointment appointment) {
    	try 
    	{
        	if(id == null || id < 0) 
        	{
        		String mensagem = "The Id cannot be blank.";
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
        	}
        	
        	if(appointment == null) 
        	{
        		String mensagem = "You need to fill in a field to create new appointment. ";
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
        	}
        	
            Appointment existingAppointment = appointmentRepository.findById(id).orElse(null);
            
            if (existingAppointment == null) {
            	String mensagem = "ID invalidated or not found.";
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
            }

            return appointmentData.updateAppointment(id, appointment);
	
    	}
    	catch(Exception ex) 
    	{
    		throw ex;
    	}
    	    }

    public ResponseEntity<String> deleteAppointment(Long id) {
        
    	try 
        {
        	if(id == null || id < 0) 
        	{
        		String mensagem = "The Id cannot be blank.";
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
        	}
        	
        	if (appointmentRepository.existsById(id)) 
        	{
        		return appointmentData.deleteAppointment(id);
            }
        }
        catch(Exception ex) 
    	{
        	throw ex;
        }
		return null;
        
    }

}
