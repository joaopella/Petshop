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
@RequestMapping("/Employee")
public class EmployeeController {
	
	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

	@SuppressWarnings("rawtypes")
	@PostMapping("/createEmployee")
    public com.PetShop.springsecurity.Response.ResponseEntity addEmployee(@RequestBody @Valid Employee employee) {
    	try 
		{
    		return employeeService.createEmployee(employee);
    	}
    	catch(Exception ex) 
    	{
    		throw ex;
    	}
    }
	
	@GetMapping("/readAll")
    public List<Employee> getAllEmployees() {
		
		try 
		{
    		return employeeService.getAllEmployees();
    	}
    	catch(Exception ex) 
    	{
    		throw ex;
    	}
    }
	
	@SuppressWarnings("rawtypes")
	@GetMapping("/getEmployeeBy/{id}")
    public com.PetShop.springsecurity.Response.ResponseEntity getEmployeeById(@PathVariable Long id) {
		
		try 
		{
    		return employeeService.getEmployeeById(id);
    	}
    	catch(Exception ex) 
    	{
    		throw ex;
    	}
    }
	
	@SuppressWarnings("rawtypes")
	@PutMapping("/updateEmployee/{id}")
    public com.PetShop.springsecurity.Response.ResponseEntity updateEmployee(@PathVariable Long id, @RequestBody @Valid Employee employeeDetails) {
		try 
		{
    		return employeeService.updateEmployee(id, employeeDetails);
    	}
    	catch(Exception ex) 
    	{
    		throw ex;
    	}
    }
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/delete/{id}")
	public com.PetShop.springsecurity.Response.ResponseEntity deleteEmployee(@PathVariable Long id) {
		try 
		{
			return employeeService.deleteEmployeeById(id);
    	}
    	catch(Exception ex) 
    	{
    		throw ex;
    	}
    }
    
}
