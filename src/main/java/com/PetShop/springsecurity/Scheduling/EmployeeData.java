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
import com.PetShop.springsecurity.Response.ResponseEntity.ResponseEntitySuccessValuesEmployee;

@Repository
public class EmployeeData {

    private DataSource dataSource;

    LocalDateTime Now = LocalDateTime.now();
    DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String dataFormatada = formatterData.format(Now);

    public EmployeeData(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public ResponseEntity<String> getEmployeeById(Long id) {
        String query = "SELECT * FROM Employee WHERE id = ?";
        Employee employee = null;

        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    employee = new Employee();
                    employee.setId(rs.getLong("id"));
                    employee.setName(rs.getString("name"));
                    employee.setPosition(rs.getString("position"));
                    employee.setEmail(rs.getString("email"));
                    employee.setPhone(rs.getString("phone"));
                    employee.setSalary(rs.getDouble("salary"));
                    employee.setSocial_Security(rs.getString("social_security"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            String mensagem = "Erro ao buscar empregado: " + e.getMessage();
            return new ResponseEntityError<>(false, HttpStatus.INTERNAL_SERVER_ERROR, mensagem, dataFormatada);
        }

        if (employee != null) {
        	String mensagem = "Busca com sucesso";
            return new ResponseEntitySuccessValuesEmployee<>(true, HttpStatus.OK, mensagem, dataFormatada, null,null,null, employee);
        } else {
            String mensagem = "Empregado não encontrado com o ID fornecido.";
            return new ResponseEntityError<>(false, HttpStatus.NOT_FOUND, mensagem, dataFormatada);
        }
    }

    public ResponseEntity<String> updateEmployee(Long id, Employee updatedEmployee) {
        String query = "UPDATE Employee SET ";
        List<String> updates = new ArrayList<>();

        if (updatedEmployee.getName() != null && !updatedEmployee.getName().isEmpty()) {
            updates.add("name = ?");
        }
        if (updatedEmployee.getPosition() != null && !updatedEmployee.getPosition().isEmpty()) {
            updates.add("position = ?");
        }
        if (updatedEmployee.getEmail() != null && !updatedEmployee.getEmail().isEmpty()) {
            updates.add("email = ?");
        }
        if (updatedEmployee.getPhone() != null && !updatedEmployee.getPhone().isEmpty()) {
            updates.add("phone = ?");
        }
        if (updatedEmployee.getSalary() != 0) {
            updates.add("salary = ?");
        }
        if (updatedEmployee.getSocial_Security() != null && !updatedEmployee.getSocial_Security().isEmpty()) {
            updates.add("social_security = ?");
        }

        if (updates.isEmpty()) {
            String mensagem = "Nenhum campo para atualizar foi fornecido.";
            return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
        }

        query += String.join(", ", updates);
        query += " WHERE id = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            int parameterIndex = 1;
            if (updatedEmployee.getName() != null && !updatedEmployee.getName().isEmpty()) {
                pst.setString(parameterIndex++, updatedEmployee.getName());
            }
            if (updatedEmployee.getPosition() != null && !updatedEmployee.getPosition().isEmpty()) {
                pst.setString(parameterIndex++, updatedEmployee.getPosition());
            }
            if (updatedEmployee.getEmail() != null && !updatedEmployee.getEmail().isEmpty()) {
                pst.setString(parameterIndex++, updatedEmployee.getEmail());
            }
            if (updatedEmployee.getPhone() != null && !updatedEmployee.getPhone().isEmpty()) {
                pst.setString(parameterIndex++, updatedEmployee.getPhone());
            }
            if (updatedEmployee.getSalary() != 0) {
                pst.setDouble(parameterIndex++, updatedEmployee.getSalary());
            }
            if (updatedEmployee.getSocial_Security() != null && !updatedEmployee.getSocial_Security().isEmpty()) {
                pst.setString(parameterIndex++, updatedEmployee.getSocial_Security());
            }
            pst.setLong(parameterIndex, id);

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                String mensagem = "Empregado atualizado com sucesso.";
                return new ResponseEntitySuccess<>(true, HttpStatus.OK, mensagem, dataFormatada);
            } else {
                String mensagem = "Empregado não encontrado com o ID fornecido.";
                return new ResponseEntityError<>(false, HttpStatus.NOT_FOUND, mensagem, dataFormatada);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            String mensagem = "Erro ao atualizar empregado: " + e.getMessage();
            return new ResponseEntityError<>(false, HttpStatus.INTERNAL_SERVER_ERROR, mensagem, dataFormatada);
        }
    }
    
    public ResponseEntity<String> deleteEmployee(Long id) {
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

