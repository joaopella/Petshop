package com.PetShop.springsecurity.Scheduling;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PetShop.springsecurity.Response.ResponseEntity;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/Service")
public class ServiceController {

	@Autowired
    private ServiceService serviceService;
	
	public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/createService")
    public com.PetShop.springsecurity.Response.ResponseEntity createService(@RequestBody @Valid Service service) {
        
		try 
		{
            return serviceService.createService(service);
        
		} catch (Exception ex) {
            throw ex;
        }
    }

    @GetMapping("/AllServices")
    public List<Service> getAllServices() {
        try 
        {
            return serviceService.getAllServices();
        
        } catch (Exception ex) {
            throw ex;
        }
    }

    @GetMapping("/ServiceById/{id}")
    public ResponseEntity<Service> getServiceById(@PathVariable Long id) {
        try 
        {
            return serviceService.getServiceById(id);
        
        } catch (Exception ex) {
            throw ex;
        }
    }

    @PutMapping("/updateService/{id}")
    public ResponseEntity<String> updateService(@PathVariable Long id, @RequestBody @Valid Service serviceDetails) {
        try 
        {
            return serviceService.updateService(id, serviceDetails);
        
        } catch (Exception ex) 
        {
            throw ex;
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteService(@PathVariable Long id) {
        try 
        {
            return serviceService.deleteService(id);
        
        } catch (Exception ex) {
            throw ex;
        }
    }
}
