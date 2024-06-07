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
import com.PetShop.springsecurity.Response.ResponseEntity.ResponseEntitySuccess;

@Service
public class ServiceService {

	@Autowired
    private ServiceData serviceData;

	LocalDateTime Now = LocalDateTime.now();

    DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String dataFormatada = formatterData.format(Now);
	
	
    public ResponseEntity<String> createService(com.PetShop.springsecurity.Scheduling.Service service) {
        
    	ResponseEntitySuccess<String> responseEntity;
    	
    	if (service == null) {
            String mensagem = "You need to fill in a field to save the new employee.";
            return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
        }

        if (!StringUtils.hasText(service.getServiceName())) {
            String mensagem = "The name cannot be blank.";
            return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
        }

        if (!StringUtils.hasText(service.getNotes())) {
            String mensagem = "The position cannot be blank.";
            return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
        }

        if (!StringUtils.hasText(service.getPaymentMethod())){
            String mensagem = "Invalid email format.";
            return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
        }

        if (!StringUtils.hasText(service.getServiceName())) {
            String mensagem = "Invalid phone format.";
            return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
        }

        if (service.getServiceCost() <= 0) {
            String mensagem = "The service must be greater than zero.";
            return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
        }

        if (!StringUtils.hasText(service.getStatus())){
            String mensagem = "Invalid Social Security number.";
            return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
        }
		
        serviceData.createService(service);
        
		responseEntity = new ResponseEntitySuccess<>(true, HttpStatus.valueOf(200) ,"Registration Created Successfully", dataFormatada);
        return responseEntity;
    	
    	
    }

    public List<com.PetShop.springsecurity.Scheduling.Service> getAllServices() {
        return serviceData.getAllServices();
    }

    public ResponseEntity<com.PetShop.springsecurity.Scheduling.Service> getServiceById(Long id) {
        return serviceData.getServiceById(id);
    }

    public ResponseEntity<String> updateService(Long id, com.PetShop.springsecurity.Scheduling.Service serviceDetails) {
        return serviceData.updateService(id, serviceDetails);
    }

    public ResponseEntity<String> deleteService(Long id) {
        return serviceData.deleteService(id);
    }
    
}
