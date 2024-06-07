package com.PetShop.springsecurity.Scheduling;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.PetShop.springsecurity.Response.ResponseEntity.ResponseEntitySuccessValuesService;

@Repository
public class ServiceData {

    private DataSource dataSource;

    LocalDateTime Now = LocalDateTime.now();
    DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String dataFormatada = formatterData.format(Now);

    public ServiceData(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ResponseEntity<String> createService(Service service) {
    	String query = "INSERT INTO service (employee_name, notes, payment_method, service_cost, service_name, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

        	pst.setString(1, service.getEmployeeName());
            pst.setString(2, service.getNotes());
            pst.setString(3, service.getPaymentMethod());
            pst.setDouble(4, service.getServiceCost());
            pst.setString(5, service.getServiceName());
            pst.setString(6, service.getStatus());

            int rowsInserted = pst.executeUpdate();
            if (rowsInserted > 0) {
                String mensagem = "Serviço criado com sucesso.";
                return new ResponseEntitySuccess<>(true, HttpStatus.OK, mensagem, dataFormatada);
            } else {
                String mensagem = "Erro ao criar o serviço.";
                return new ResponseEntityError<>(false, HttpStatus.INTERNAL_SERVER_ERROR, mensagem, dataFormatada);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            String mensagem = "Erro ao criar o serviço: " + e.getMessage();
            return new ResponseEntityError<>(false, HttpStatus.INTERNAL_SERVER_ERROR, mensagem, dataFormatada);
        }
    }

    public List<Service> getAllServices() {
        String query = "SELECT * FROM service";
        List<Service> serviceList = new ArrayList<>();
        
        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Service service = new Service();
                service.setId(rs.getLong("id"));
                service.setEmployeeName(rs.getString("employee_name"));
                service.setNotes(rs.getString("notes"));
                service.setPaymentMethod(rs.getString("payment_method"));
                service.setServiceCost(rs.getDouble("service_cost"));
                service.setServiceName(rs.getString("service_name"));
                service.setStatus(rs.getString("status"));
                serviceList.add(service);
            }
        } 
        catch (SQLException e) 
        {
            //e.printStackTrace();
            //String mensagem = "Erro ao buscar serviços: " + e.getMessage();
            //return new ResponseEntityError<>(false, HttpStatus.INTERNAL_SERVER_ERROR, mensagem, dataFormatada);
        }
        
        //String mensagem = "Busca efetuada com sucesso";
        //eturn new response<>(true, HttpStatus.OK , mensagem, dataFormatada, serviceList);
        return serviceList;
    }


    public ResponseEntity<Service> getServiceById(Long id) {
        String query = "SELECT * FROM service WHERE id = ?";
        Service service = null;

        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    service = new Service();
                    service.setId(rs.getLong("id"));
                    service.setEmployeeName(rs.getString("employee_name"));
                    service.setNotes(rs.getString("notes"));
                    service.setPaymentMethod(rs.getString("payment_method"));
                    service.setServiceCost(rs.getDouble("service_cost"));
                    service.setServiceName(rs.getString("service_name"));
                    service.setStatus(rs.getString("status"));
                }
            }
        } catch (SQLException e) {
        	e.printStackTrace();
            String mensagem = "Erro ao buscar Serviço: " + e.getMessage();
            return new ResponseEntityError<>(false, HttpStatus.INTERNAL_SERVER_ERROR, mensagem, dataFormatada);
        }
        
        if (service != null) {
        	String mensagem = "Busca com sucesso";
            return new ResponseEntitySuccessValuesService<>(true, HttpStatus.OK, mensagem, dataFormatada, null,null,null, service);
        } else {
        	String mensagem = "Empregado não encontrado com o ID fornecido.";
            return new ResponseEntityError<>(false, HttpStatus.NOT_FOUND, mensagem, dataFormatada);
        }
    }


    public ResponseEntity<String> updateService(Long id, Service serviceDetails) {
        String query = "UPDATE service SET employee_name = ?, notes = ?, payment_method = ?, service_cost = ?, service_name = ?, status = ? WHERE id = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, serviceDetails.getEmployeeName());
            pst.setString(2, serviceDetails.getNotes());
            pst.setString(3, serviceDetails.getPaymentMethod());
            pst.setDouble(4, serviceDetails.getServiceCost());
            pst.setString(5, serviceDetails.getServiceName());
            pst.setString(6, serviceDetails.getStatus());
            pst.setLong(7, id);

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                String mensagem = "Serviço atualizado com sucesso.";
                return new ResponseEntitySuccess<>(true, HttpStatus.OK, mensagem, dataFormatada);
            } else {
                String mensagem = "Serviço não encontrado com o ID fornecido.";
                return new ResponseEntityError<>(false, HttpStatus.INTERNAL_SERVER_ERROR, mensagem, dataFormatada);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            String mensagem = "Erro ao atualizar serviço: " + e.getMessage();
            return new ResponseEntityError<>(false, HttpStatus.INTERNAL_SERVER_ERROR, mensagem, dataFormatada);
        }
    }


    public ResponseEntity<String> deleteService(Long id) {
        String query = "DELETE FROM Service WHERE id = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setLong(1, id);

            int rowsDeleted = pst.executeUpdate();

            if (rowsDeleted > 0) {
                String mensagem = "Serviço excluído com sucesso.";
                return new ResponseEntitySuccess<>(true, HttpStatus.OK, mensagem, dataFormatada);
            } else {
                String mensagem = "Serviço não encontrado com o ID fornecido.";
                return new ResponseEntityError<>(false, HttpStatus.NOT_FOUND, mensagem, dataFormatada);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            String mensagem = "Erro ao excluir serviço: " + e.getMessage();
            return new ResponseEntityError<>(false, HttpStatus.INTERNAL_SERVER_ERROR, mensagem, dataFormatada);
        }
    }

}
