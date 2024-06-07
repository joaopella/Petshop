package com.PetShop.springsecurity.Scheduling;

import java.time.LocalDateTime;

import com.PetShop.springsecurity.pet.Pet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "appointment")
public class Appointment {

	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @ManyToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name = "pet_id", referencedColumnName = "id")
	    private Pet pet;

	    @ManyToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name = "service_id", referencedColumnName = "id")
	    private Service service;

	    @ManyToOne(fetch = FetchType.EAGER)
	    @JoinColumn(name = "employee_id", referencedColumnName = "id")
	    private Employee employee;

	    @Column(name = "scheduled_time")
	    private LocalDateTime scheduledTime;

	    //@Column(name = "appointment_date_time")
	    //private LocalDateTime appointmentDateTime;

	    @Column(name = "client_name")
	    private String clientName;

	    @Column(name = "pet_name")
	    private String petName;

	    @Column(name = "return_date_time")
	    private LocalDateTime returnDateTime;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public LocalDateTime getScheduledTime() {
		return scheduledTime;
	}

	public void setScheduledTime(LocalDateTime scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

	/*public LocalDateTime getAppointmentDateTime() {
		return appointmentDateTime;
	}

	public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
		this.appointmentDateTime = appointmentDateTime;
	}*/

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public LocalDateTime getReturnDateTime() {
		return returnDateTime;
	}

	public void setReturnDateTime(LocalDateTime returnDateTime) {
		this.returnDateTime = returnDateTime;
	}
    
    
}
