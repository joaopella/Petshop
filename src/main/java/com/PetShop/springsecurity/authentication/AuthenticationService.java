package com.PetShop.springsecurity.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.PetShop.springsecurity.Response.ResponseEntity;
import com.PetShop.springsecurity.Response.ResponseEntity.ResponseEntityError;
import com.PetShop.springsecurity.Response.ResponseEntity.ResponseEntitySuccess;
import com.PetShop.springsecurity.Response.ResponseEntity.ResponseEntitySuccessToken;
import com.PetShop.springsecurity.Response.ResponseEntity.ResponseEntitySuccessValuesUsers;
import com.PetShop.springsecurity.config.JwtService;
import com.PetShop.springsecurity.user.User;
import com.PetShop.springsecurity.user.UserData;
import com.PetShop.springsecurity.user.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

@Service
public class AuthenticationService {

   @Autowired
   private AuthenticationManager authenticationManager;

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private JwtService jwtService;

   @Autowired
   private DataSource dataSource;
   
   private final PasswordEncoder passwordEncoder;

   LocalDateTime Now = LocalDateTime.now();

   DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
   String dataFormatada = formatterData.format(Now);
   
    public AuthenticationService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<String> register(User request)
    {
        var user = new User();
        ResponseEntitySuccessToken<String> responseEntity;
        try 
        {
        	if (request.getName() == null || request.getName().isEmpty()) 
            {
                String mensagem =  "A name cannot be blank.";
                
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
            } 
            else 
            {
                user.setName(request.getName());
            }
            
            if (request.getUsername() == null || request.getUsername().isEmpty()) 
            {
                String mensagem =  "A Username cannot be blank.";
                
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
            } 
            else 
            {
            	 user.setUsername(request.getUsername());
            }
            
            if (request.getPassword() == null || request.getPassword().isEmpty()) 
            {
                String mensagem =  "A Password cannot be blank.";
                
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
            } 
            else 
            {
            	user.setPassword(passwordEncoder.encode(request.getPassword()));
            }
            
            if (request.getSocial_Security() == null || request.getSocial_Security().isEmpty()) 
            {
                String mensagem =  "A Social Security cannot be blank.";
                
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
            } 
            else 
            {
            	user.setSocial_Security(request.getSocial_Security());
            }
            user.setEmail(request.getEmail());
            user.setPhone(request.getPhone());
            user.setBirthday(request.getBirthday());
            user.setRole(request.getRole());
            userRepository.save(user);
            
            String token = jwtService.generateToken(user, generateExtraClaims(user));
            responseEntity = new ResponseEntitySuccessToken<>(true, HttpStatus.valueOf(200) ,"Registration Created Successfully", dataFormatada, null, token);
            return responseEntity;
        }
        catch(Exception ex)
        {
        	throw ex;
        }
        
    }

    public ResponseEntity<String> login(AuthenticationRequest authenticationRequest) {
    	
    	ResponseEntitySuccessToken<String> responseEntity = null;
        try 
    	{
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()
            );
            authenticationManager.authenticate(authToken);
            User user = userRepository.findByUsername(authenticationRequest.getUsername()).orElse(null);
            
            if(user == null) {
                String mensagem = "Please check if the username and password are correct.";
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
            }
            
            String jwt = jwtService.generateToken(user, generateExtraClaims(user));
            
            if(jwt.isEmpty()) {
                String mensagem ="Unable to create token, please verify if the credentials are correct.";
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
            }
            
            responseEntity = new ResponseEntitySuccessToken<>(true, HttpStatus.OK ,"Access Successfully", dataFormatada, null, jwt);
            return responseEntity;
        } 
    	catch (AuthenticationException ex) 
    	{
            String mensagem = "Invalid username or password.";
            return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
        } catch(Exception ex) 
    	{
            String mensagem = "An error occurred while processing your request.";
            return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
        }
    }
    
    public ResponseEntity<List<User>> readAll(){
    	
    	try 
    	{
    		ResponseEntity<List<User>> responseEntity = null;
        	List<User> userList = userRepository.findAll();
        	responseEntity = new ResponseEntitySuccessValuesUsers<>(true, HttpStatus.valueOf(200) ,"Values return sucessfuly", dataFormatada, null, null, userList);
            return responseEntity;
    	}
    	catch(Exception ex)
    	{
    		throw ex;
    	}
    	
    }
    
    public ResponseEntity<List<User>> getUserBySocialSecurityNumber(String socialSecurityNumber){

    	UserData userData = new UserData(dataSource);
    	try 
    	{
    		if(socialSecurityNumber.isEmpty()) 
    		{
    			String mensagem =  "A socialSecurityNumber cannot be blank.";
                
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
    		}
    		
    		//List<User> userList = userRepository.findBySocial_Security(socialSecurityNumber);
    		List<User> userList = userData.findBySocialSecurityNumber(socialSecurityNumber);
    		if (!userList.isEmpty() && userList != null) 
    		{
    			User user = userList.get(0); // Assuming there is only one user with the provided social security number
                return new ResponseEntitySuccessValuesUsers<>(true, HttpStatus.valueOf(200) ,"Nome do usuário com o número de seguro social " + socialSecurityNumber + " é: " + user.getName(), dataFormatada, null, null, userList);
            } 
    		else 
    		{
            	String mensagem =  "Unable to find user by Social Security Number.";
                
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
            }
    	}
    	catch (Exception ex)
    	{
    		throw ex;
    	}
    	
    }
    
    public ResponseEntity<String> deleteUserById(Long userId) {
        try {
            
            if (userId == null) {
                String mensagem = "O ID do usuário não pode ser nulo.";
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
            }

            User optionalUser = userRepository.findById(userId).orElse(null);
            if (optionalUser != null ) {

                userRepository.deleteById(userId);

                String mensagem = "Usuário excluído com sucesso.";
                return new ResponseEntitySuccess<>(true, HttpStatus.OK, mensagem, dataFormatada);
            }
            else 
            {
                String mensagem = "Usuário não encontrado com o ID fornecido.";
                return new ResponseEntityError<>(false, HttpStatus.NOT_FOUND, mensagem, dataFormatada);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public ResponseEntity<String> updateUser(String socialSecurityNumber, User updatedUser) {
    	UserData userData = new UserData(dataSource);
    	try 
    	{
            // Verifica se o número do seguro social é nulo ou vazio
            if (socialSecurityNumber == null || socialSecurityNumber.isEmpty()) {
                String mensagem = "O número do seguro social não pode ser nulo ou vazio.";
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
            }

            // Verifica se o usuário a ser atualizado é nulo
            if (updatedUser == null) {
                String mensagem = "O usuário não pode ser nulo.";
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
            }

            // Verifica se o número do seguro social fornecido corresponde ao número do seguro social do usuário atualizado
            if (socialSecurityNumber.equals(updatedUser.getSocial_Security())) {
                String mensagem = "O número do seguro social fornecido não corresponde ao número do seguro social do usuário atualizado.";
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
            }
            
            return userData.updateUserData(socialSecurityNumber, updatedUser);
            
        } catch (Exception ex) {
            throw ex;
        }
    }
    
    public ResponseEntity<String> deleteUserBySocialSecurityNumber(String socialSecurityNumber) {
    	UserData userData = new UserData(dataSource);
    	try 
    	{
			if (socialSecurityNumber == null || socialSecurityNumber.isEmpty()) 
			{
				String mensagem = "O número do seguro social não pode ser nulo ou vazio.";
				return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
			}
			
			return userData.deleteUserBySocialSecurityNumber(socialSecurityNumber);
        } 
		catch (Exception ex) 
		{
			throw ex;
		}
    }
    
    
    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getName());
        extraClaims.put("role", user.getRole().name());
        return extraClaims;
    }
}
