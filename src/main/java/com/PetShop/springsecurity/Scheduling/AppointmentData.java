package com.PetShop.springsecurity.Scheduling;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import com.PetShop.springsecurity.Response.ResponseEntity;
import com.PetShop.springsecurity.Response.ResponseEntity.ResponseEntityError;
import com.PetShop.springsecurity.Response.ResponseEntity.ResponseEntitySuccess;

@Repository
public class AppointmentData {

	private DataSource dataSource;

    LocalDateTime Now = LocalDateTime.now();
    DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String dataFormatada = formatterData.format(Now);

    public AppointmentData(DataSource dataSource) {
        this.dataSource = dataSource;
    }
	
	public ResponseEntity<String> createAppointment(Appointment appointment) {
	    String query = "INSERT INTO appointment (pet_id, service_id, employee_id, scheduled_time, client_name, pet_name, return_date_time) " +
	                   "VALUES (?, ?, ?, ?, ?, ?, ?)";

	    try (Connection con = dataSource.getConnection();
	         PreparedStatement pst = con.prepareStatement(query)) {

	        pst.setLong(1, appointment.getPet().getId());
	        pst.setLong(2, appointment.getService().getId());
	        pst.setLong(3, appointment.getEmployee().getId());
	        pst.setTimestamp(4, Timestamp.valueOf(appointment.getScheduledTime()));
	        pst.setString(5, appointment.getClientName());
	        pst.setString(6, appointment.getPetName());
	        pst.setTimestamp(7, Timestamp.valueOf(Now));

	        int rowsInserted = pst.executeUpdate();
	        if (rowsInserted > 0) {
	            String mensagem = "Appointment created successfully.";
	            return new ResponseEntitySuccess<>(true, HttpStatus.OK, mensagem, dataFormatada);
	        } else {
	            String mensagem = "Failed to create appointment.";
	            return new ResponseEntityError<>(false, HttpStatus.INTERNAL_SERVER_ERROR, mensagem, dataFormatada);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        String mensagem = "Failed to create appointment: " + e.getMessage();
	        return new ResponseEntityError<>(false, HttpStatus.INTERNAL_SERVER_ERROR, mensagem, dataFormatada);
	    }
	}
	
	public List<Appointment> getAllAppointments() throws Exception {
        String query = "SELECT * FROM appointment";

        List<Appointment> appointments = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setId(rs.getLong("id"));
                // Defina outros atributos do compromisso conforme necessário
                appointments.add(appointment);
            }

            return appointments;
        } 
        catch (Exception ex) 
        {
            throw ex;
        }
    }
	
	public ResponseEntity<String> updateAppointment(Long id, Appointment appointmentDetails) {
	    String query = "UPDATE appointment SET ";
	    List<String> updates = new ArrayList<>();
	    
	    // Adiciona os campos a serem atualizados se não forem nulos ou vazios
	    if (appointmentDetails.getScheduledTime() != null) {
	        updates.add("scheduled_time = ?");
	    }
	    if (appointmentDetails.getService() != null && appointmentDetails.getService().getId() != null) {
	        updates.add("service_id = ?");
	    }
	    if (appointmentDetails.getEmployee() != null && appointmentDetails.getEmployee().getId() != null) {
	        updates.add("employee_id = ?");
	    }
	    if (appointmentDetails.getClientName() != null && !appointmentDetails.getClientName().isEmpty()) {
	        updates.add("client_name = ?");
	    }
	    if (appointmentDetails.getPetName() != null && !appointmentDetails.getPetName().isEmpty()) {
	        updates.add("pet_name = ?");
	    }
	    if (appointmentDetails.getReturnDateTime() != null) {
	        updates.add("return_date_time = ?");
	    }

	    // Verifica se há campos para atualizar
	    if (updates.isEmpty()) {
	        String mensagem = "Nenhum campo para atualizar foi fornecido.";
	        return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
	    }

	    // Constrói a query SQL
	    query += String.join(", ", updates);
	    query += " WHERE id = ?";

	    try (Connection con = dataSource.getConnection();
	         PreparedStatement pst = con.prepareStatement(query)) {

	        int parameterIndex = 1;
	        
	        // Define os valores dos parâmetros na PreparedStatement
	        if (appointmentDetails.getScheduledTime() != null) {
	            pst.setTimestamp(parameterIndex++, Timestamp.valueOf(appointmentDetails.getScheduledTime()));
	        }
	        if (appointmentDetails.getService() != null && appointmentDetails.getService().getId() != null) {
	            pst.setLong(parameterIndex++, appointmentDetails.getService().getId());
	        }
	        if (appointmentDetails.getEmployee() != null && appointmentDetails.getEmployee().getId() != null) {
	            pst.setLong(parameterIndex++, appointmentDetails.getEmployee().getId());
	        }
	        if (appointmentDetails.getClientName() != null && !appointmentDetails.getClientName().isEmpty()) {
	            pst.setString(parameterIndex++, appointmentDetails.getClientName());
	        }
	        if (appointmentDetails.getPetName() != null && !appointmentDetails.getPetName().isEmpty()) {
	            pst.setString(parameterIndex++, appointmentDetails.getPetName());
	        }
	        if (appointmentDetails.getReturnDateTime() != null) {
	            pst.setTimestamp(parameterIndex++, Timestamp.valueOf(appointmentDetails.getReturnDateTime()));
	        }
	        
	        // Define o ID do compromisso na PreparedStatement
	        pst.setLong(parameterIndex, id);

	        int rowsUpdated = pst.executeUpdate();

	        if (rowsUpdated > 0) {
	            String mensagem = "Compromisso atualizado com sucesso.";
	            return new ResponseEntitySuccess<>(true, HttpStatus.OK, mensagem, dataFormatada);
	        } else {
	            String mensagem = "Compromisso não encontrado com o ID fornecido.";
	            return new ResponseEntityError<>(false, HttpStatus.NOT_FOUND, mensagem, dataFormatada);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        String mensagem = "Erro ao atualizar compromisso: " + e.getMessage();
	        return new ResponseEntityError<>(false, HttpStatus.INTERNAL_SERVER_ERROR, mensagem, dataFormatada);
	    }
	}

	public ResponseEntity<String> deleteAppointment(Long id) {
        String query = "DELETE FROM Employee WHERE id = ?";
        
        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setLong(1, id);

            int rowsDeleted = pst.executeUpdate();

            if (rowsDeleted > 0) {
                String mensagem = "Empregado excluído com sucesso.";
                return new ResponseEntitySuccess<>(true, HttpStatus.OK, mensagem, dataFormatada);
            } else {
                String mensagem = "Empregado não encontrado com o ID fornecido.";
                return new ResponseEntityError<>(false, HttpStatus.NOT_FOUND, mensagem, dataFormatada);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            String mensagem = "Erro ao excluir empregado: " + e.getMessage();
            return new ResponseEntityError<>(false, HttpStatus.INTERNAL_SERVER_ERROR, mensagem, dataFormatada);
        }
    }
	
}
