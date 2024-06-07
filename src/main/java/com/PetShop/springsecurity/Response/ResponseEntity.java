package com.PetShop.springsecurity.Response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.PetShop.springsecurity.Scheduling.Employee;
import com.PetShop.springsecurity.pet.Pet;
import com.PetShop.springsecurity.user.User;
import com.fasterxml.jackson.annotation.JsonInclude;

@Service
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseEntity<T>{
	
	LocalDateTime Now = LocalDateTime.now();

	   DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	   String dataFormatada = formatterData.format(Now);
	
	private boolean success;
    private String message;
    private String data;
    private String token;
    private int status;
    private List<Pet> values;
    private List<User> users;
    private Employee employee;
    private com.PetShop.springsecurity.Scheduling.Service service;
    
    public ResponseEntity() {
    }

    // Construtor para sucesso com token
    public ResponseEntity(boolean success, HttpStatus status, String message, String data, List<Pet> values,String token, List<User> users, Employee employee, com.PetShop.springsecurity.Scheduling.Service service) {
        this.success = success;
        this.status = status.value();
        this.message = message;
        this.data = data;
        this.token = token;
        this.values = values;
        this.users = users;
        this.employee = employee;
        this.service = service;
    }

	// Subclasse para sucesso sem token
    public static class ResponseEntitySuccess<T> extends ResponseEntity<T> {
        public ResponseEntitySuccess(boolean success, HttpStatus status, String message, String data) {
        	super(success, status, message, data, null, null, null, null, null);
        }
    }   
    public static class ResponseEntitySuccessValuesPet<T> extends ResponseEntity<T> {
        public ResponseEntitySuccessValuesPet(boolean success, HttpStatus status, String message, String data, List<Pet> Values, String token, List<User> users) {
        	super(success, status, message, data, Values, null, null, null, null);
        }
    }
    
    public static class ResponseEntitySuccessValuesEmployee<T> extends ResponseEntity<T> {
        public ResponseEntitySuccessValuesEmployee(boolean success, HttpStatus status, String message, String data, List<Pet> Values, String token, List<User> users, Employee employee) {
        	super(success, status, message, data, null, null, null, employee, null);
        }
    }
    
    public static class ResponseEntitySuccessValuesUsers<T> extends ResponseEntity<T> {
        public ResponseEntitySuccessValuesUsers(boolean success, HttpStatus status, String message, String data, List<Pet> Values, String token, List<User> users) {
        	super(success, status, message, data, null, null, users, null, null);
        }
    }
    
    public static class ResponseEntitySuccessValuesService<T> extends ResponseEntity<T> {
        public ResponseEntitySuccessValuesService(boolean success, HttpStatus status, String message, String data, List<Pet> Values, String token, List<User> users, com.PetShop.springsecurity.Scheduling.Service service) {
        	super(success, status, message, data, null, null, null, null, service);
        }
    }
    
    
    // Subclasse para sucesso com token
    public static class ResponseEntitySuccessToken<T> extends ResponseEntity<T> {
        public ResponseEntitySuccessToken(boolean success, HttpStatus status, String message, String data, List<Pet> Values, String token) {
        	super(success, status, message, data, null, token, null, null, null);
        }
    }

    // Subclasse para erro
    public static class ResponseEntityError<T> extends ResponseEntity<T> {
        public ResponseEntityError(boolean Error, HttpStatus status, String message, String data) {
            super(Error, status, message, data, null, null, null, null, null);
        }
    }

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status.value();
	}

	public List<Pet> getValues() {
		return values;
	}

	public void setValues(List<Pet> values) {
		this.values = values;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public com.PetShop.springsecurity.Scheduling.Service getService() {
		return service;
	}

	public void setService(com.PetShop.springsecurity.Scheduling.Service service) {
		this.service = service;
	}    
}
