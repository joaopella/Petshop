package com.PetShop.springsecurity.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.PetShop.springsecurity.user.Permission;

@Configuration
@EnableWebSecurity
public class SecurityFilter {


    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
        .csrf(csrfConfig -> csrfConfig.disable())
        .sessionManagement(sessionMangConfig -> sessionMangConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .authorizeHttpRequests( authConfig -> {
        	//Products
            authConfig.requestMatchers(HttpMethod.GET, "/products").hasAuthority(Permission.READ_ALL_PRODUCTS.name());
            authConfig.requestMatchers(HttpMethod.POST, "/products").hasAuthority(Permission.SAVE_ONE_PRODUCT.name());
            
            //Pets
            authConfig.requestMatchers(HttpMethod.GET, "/pets/create").hasAuthority(Permission.READ_ALL_PRODUCTS.name());
            authConfig.requestMatchers(HttpMethod.POST, "/pets/create").hasAuthority(Permission.SAVE_ONE_PRODUCT.name());
            authConfig.requestMatchers(HttpMethod.GET, "/pets/read").hasAuthority(Permission.READ_ALL_PRODUCTS.name());
            authConfig.requestMatchers(HttpMethod.POST, "/pets/read").hasAuthority(Permission.SAVE_ONE_PRODUCT.name());
            authConfig.requestMatchers(HttpMethod.GET, "/pets/getPetById/{id}").hasAuthority(Permission.READ_ALL_PRODUCTS.name());
            authConfig.requestMatchers(HttpMethod.DELETE, "/pets/deletePet/{id}}").hasAuthority(Permission.READ_ALL_PRODUCTS.name());
            authConfig.requestMatchers(HttpMethod.PUT, "/pets/updatePet/{id}").permitAll();
            
            //Auth
            authConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
            authConfig.requestMatchers(HttpMethod.POST, "/auth/register").permitAll();
            authConfig.requestMatchers(HttpMethod.GET, "/auth/readAll").permitAll();
            authConfig.requestMatchers(HttpMethod.GET, "/auth/socialSecurityNumber/{socialSecurityNumber}").permitAll();
            authConfig.requestMatchers(HttpMethod.DELETE, "/auth/user/{id}").permitAll();
            authConfig.requestMatchers(HttpMethod.PUT, "/auth//user/{socialSecurityNumber}").permitAll();
            authConfig.requestMatchers(HttpMethod.DELETE, "/auth/user/{socialSecurityNumber}").permitAll();
            
            //Appointment
            authConfig.requestMatchers(HttpMethod.POST, "/Appointment/createAppointment").permitAll();
            authConfig.requestMatchers(HttpMethod.GET, "/Appointment/allAppointments").permitAll();
            authConfig.requestMatchers(HttpMethod.POST, "/deleteAppointment/{id}").permitAll();
            authConfig.requestMatchers(HttpMethod.GET, "/updateAppointment/{id}").permitAll();
            authConfig.requestMatchers(HttpMethod.GET, "/appointmentById/{id}").permitAll();
            
            //Service	
            authConfig.requestMatchers(HttpMethod.POST, "/Service/createService").permitAll();
            authConfig.requestMatchers(HttpMethod.GET, "/Service/AllServices").permitAll();
            authConfig.requestMatchers(HttpMethod.GET, "/Service/ServiceById/{id}").permitAll();
            authConfig.requestMatchers(HttpMethod.PUT, "/Service/updateService/{id}").permitAll();
            authConfig.requestMatchers(HttpMethod.DELETE, "/Service/delete/{id}").permitAll();
            
            authConfig.anyRequest().permitAll();
        });

        return http.build();

    }
    
    
}
