package com.PetShop.springsecurity.Scheduling;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.PetShop.springsecurity.Response.ResponseEntity;
import com.PetShop.springsecurity.Response.ResponseEntity.ResponseEntityError;
import com.PetShop.springsecurity.Response.ResponseEntity.ResponseEntitySuccess;

@Service
public class EmployeeService {
	
	LocalDateTime Now = LocalDateTime.now();

    DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String dataFormatada = formatterData.format(Now);
	
    @Autowired
    private EmployeeData employeeData;
    
	@Autowired
    private EmployeeRepository employeeRepository;

	public ResponseEntity<String> createEmployee(Employee employee) {
		ResponseEntitySuccess<String> responseEntity;
		//var Employee = new Employee();
		
        if (employee == null) {
            String mensagem = "You need to fill in a field to save the new employee.";
            return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
        }

        if (!StringUtils.hasText(employee.getName())) {
            String mensagem = "The name cannot be blank.";
            return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
        }

        if (!StringUtils.hasText(employee.getPosition())) {
            String mensagem = "The position cannot be blank.";
            return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
        }

        if (!StringUtils.hasText(employee.getEmail()) || !isValidEmail(employee.getEmail())) {
            String mensagem = "Invalid email format.";
            return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
        }

        if (!StringUtils.hasText(employee.getPhone()) || !isValidPhone(employee.getPhone())) {
            String mensagem = "Invalid phone format.";
            return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
        }

        if (employee.getSalary() <= 0) {
            String mensagem = "The salary must be greater than zero.";
            return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
        }

        if (!StringUtils.hasText(employee.getSocial_Security()) || !isValidSocialSecurity(employee.getSocial_Security())){
            String mensagem = "Invalid Social Security number.";
            return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
        }
		
        employeeRepository.save(employee);
        
		responseEntity = new ResponseEntitySuccess<>(true, HttpStatus.valueOf(200) ,"Registration Created Successfully", dataFormatada);
        return responseEntity;
    }
	
	private boolean isValidEmail(String email) {
		if (email == null) return false;
		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private boolean isValidPhone(String phone) {
    	if (phone == null) return false;
    	String phoneRegex = "^(\\+\\d{1,3}[- ]?)?\\d{10}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        return pattern.matcher(phone).matches();
    }

    private boolean isValidSocialSecurity(String socialSecurity) {
    	if (socialSecurity == null) return false;
        String ssnRegex = "^(?!000|666|9\\d{2})\\d{3}-(?!00)\\d{2}-(?!0000)\\d{4}$";
        Pattern pattern = Pattern.compile(ssnRegex);
        return pattern.matcher(socialSecurity).matches();
    }
	
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    } 
    
    public ResponseEntity<String> getEmployeeById(Long id) {
    	if(id == null || id < 0) {
    		String mensagem = "O valor do ID nao pode ser menor que zero ou vazio.";
            return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
    	}
        return employeeData.getEmployeeById(id);
    }
    
    public ResponseEntity<String> updateEmployee(Long id, Employee employeeDetails) {
    	try 
    	{
    		if(id == null || id < 0) {
        		String mensagem = "O valor do ID nao pode ser menor que zero ou vazio.";
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
        	}
        	
        	if (employeeDetails == null || isEmployeeDetailsEmpty(employeeDetails)) {
                String mensagem = "Os detalhes do empregado não podem ser nulos ou vazios.";
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
            }
    	}
    	catch(Exception ex) {
    		throw ex;
    	}
    	
    	ResponseEntity<String> validationResponse = validateEmployee(employeeDetails);
        if (validationResponse != null) {
            return validationResponse;
        }
    	
    	//validateEmployee(employeeDetails);
    	
        return employeeData.updateEmployee(id, employeeDetails);
    }
    
    private boolean isEmployeeDetailsEmpty(Employee employeeDetails) {
        return (employeeDetails.getName() == null || employeeDetails.getName().isEmpty()) &&
               (employeeDetails.getPosition() == null || employeeDetails.getPosition().isEmpty()) &&
               (employeeDetails.getEmail() == null || employeeDetails.getEmail().isEmpty()) &&
               (employeeDetails.getPhone() == null || employeeDetails.getPhone().isEmpty()) &&
               (employeeDetails.getSalary() == 0) &&
               (employeeDetails.getSocial_Security() == null || employeeDetails.getSocial_Security().isEmpty());
    }
    
    private ResponseEntity<String> validateEmployee(Employee employee) {
        try 
        {
        	if (employee.getSocial_Security() != null || !employee.getSocial_Security().isEmpty())
			{
        		if (!isValidSocialSecurity(employee.getSocial_Security()))
            	{
                    String mensagem = "Número de segurança social inválido.";
                    return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
                }
        	}
        	
        	if (!(employee.getEmail() == null || employee.getEmail().isEmpty())) 
        	{
        		if (!isValidEmail(employee.getEmail()))
                {
                    String mensagem = "Email inválido.";
                    return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
                }        		
        	}

        	if (!(employee.getPhone() == null || employee.getPhone().isEmpty())) 
        	{
        		if (!isValidPhone(employee.getPhone()))
                {
                    String mensagem = "Número de telefone inválido.";
                    return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
                }
        	}
            
            return null;
        }
        catch(Exception ex) {
        	throw ex;
        }
    	
    }

    public ResponseEntity<String> deleteEmployeeById(Long id) {
        if (id == null || id < 0) {
            String mensagem = "O valor do ID não pode ser menor que zero ou vazio.";
            return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
        }
        
        try {
            return employeeData.deleteEmployee(id);
        } catch (Exception ex) {
            String mensagem = "Erro ao excluir empregado: " + ex.getMessage();
            return new ResponseEntityError<>(false, HttpStatus.INTERNAL_SERVER_ERROR, mensagem, dataFormatada);
        }
    }
}
