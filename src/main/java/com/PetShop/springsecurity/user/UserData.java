package com.PetShop.springsecurity.user;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.http.HttpStatus;

import com.PetShop.springsecurity.Response.ResponseEntity;
import com.PetShop.springsecurity.Response.ResponseEntity.ResponseEntityError;
import com.PetShop.springsecurity.Response.ResponseEntity.ResponseEntitySuccess;


public class UserData {
	
	LocalDateTime Now = LocalDateTime.now();
    DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String dataFormatada = formatterData.format(Now);

    private DataSource dataSource;

    public UserData(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<User> findBySocialSecurityNumber(String socialSecurityNumber) {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM _user WHERE social_security = ?";
        
        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, socialSecurityNumber);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getLong("id"));
                    user.setUsername(rs.getString("username"));
                    user.setName(rs.getString("name"));
                    //user.setPassword(rs.getString("password"));
                    user.setEmail(rs.getString("email"));
                    user.setBirthday(rs.getDate("birthday").toLocalDate());
                    user.setPhone(rs.getString("phone"));
                    user.setSocial_Security(rs.getString("social_security"));
                    
                    userList.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }
    
    public ResponseEntity<String> updateUserData(String socialSecurityNumber, User updatedUser) {
        try {
            String query = "UPDATE _user SET ";
            List<String> updates = new ArrayList<>();

            if (updatedUser.getName() != null && !updatedUser.getName().isEmpty()) {
                updates.add("name = ?");
            }
            if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
                updates.add("email = ?");
            }
            if (updatedUser.getBirthday() != null) {
                updates.add("birthday = ?");
            }
            //if (updatedUser.getRole() != null) {
            //    updates.add("role = ?");
            //}
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                updates.add("password = ?");
            }
            if (updatedUser.getPhone() != null && !updatedUser.getPhone().isEmpty()) {
                updates.add("phone = ?");
            }
            if (updatedUser.getUsername() != null && !updatedUser.getUsername().isEmpty()) {
                updates.add("username = ?");
            }

            if (updates.isEmpty()) {
                String mensagem = "Nenhum campo para atualizar foi fornecido.";
                return new ResponseEntityError<>(false, HttpStatus.BAD_REQUEST, mensagem, dataFormatada);
            }

            query += String.join(", ", updates);
            query += " WHERE social_security = ?";

            try (Connection con = dataSource.getConnection();
                 PreparedStatement pst = con.prepareStatement(query)) {

                int parameterIndex = 1;
                if (updatedUser.getName() != null && !updatedUser.getName().isEmpty()) {
                    pst.setString(parameterIndex++, updatedUser.getName());
                }
                if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
                    pst.setString(parameterIndex++, updatedUser.getEmail());
                }
                if (updatedUser.getBirthday() != null) {
                    pst.setDate(parameterIndex++, Date.valueOf(updatedUser.getBirthday()));
                }
//                if (updatedUser.getRole() != null) {
 //                   pst.setString(parameterIndex++, updatedUser.getRole());
               // }
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    pst.setString(parameterIndex++, updatedUser.getPassword());
                }
                if (updatedUser.getPhone() != null && !updatedUser.getPhone().isEmpty()) {
                    pst.setString(parameterIndex++, updatedUser.getPhone());
                }
                if (updatedUser.getUsername() != null && !updatedUser.getUsername().isEmpty()) {
                    pst.setString(parameterIndex++, updatedUser.getUsername());
                }
                pst.setString(parameterIndex, socialSecurityNumber);

                int rowsUpdated = pst.executeUpdate();

                if (rowsUpdated > 0) {
                    String mensagem = "Usuário atualizado com sucesso.";
                    return new ResponseEntitySuccess<>(true, HttpStatus.OK, mensagem, dataFormatada);
                } else {
                    String mensagem = "Usuário não encontrado com o número de seguridade social fornecido.";
                    return new ResponseEntityError<>(false, HttpStatus.NOT_FOUND, mensagem, dataFormatada);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                String mensagem = "Erro ao atualizar usuário: " + e.getMessage();
                return new ResponseEntityError<>(false, HttpStatus.INTERNAL_SERVER_ERROR, mensagem, dataFormatada);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            String mensagem = "Erro ao atualizar usuário: " + ex.getMessage();
            return new ResponseEntityError<>(false, HttpStatus.INTERNAL_SERVER_ERROR, mensagem, dataFormatada);
        }
    }

    public ResponseEntity<String> deleteUserBySocialSecurityNumber(String socialSecurityNumber) {
        try {
            String query = "DELETE FROM _user WHERE social_security = ?";
            
            try (Connection con = dataSource.getConnection();
                 PreparedStatement pst = con.prepareStatement(query)) {

                pst.setString(1, socialSecurityNumber);
                int rowsDeleted = pst.executeUpdate();

                if (rowsDeleted > 0) {
                    String mensagem = "Usuário excluído com sucesso.";
                    return new ResponseEntitySuccess<>(true, HttpStatus.OK, mensagem, dataFormatada);
                } else {
                    String mensagem = "Usuário não encontrado com o número do seguro social fornecido.";
                    return new ResponseEntityError<>(false, HttpStatus.NOT_FOUND, mensagem, dataFormatada);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                String mensagem = "Erro ao excluir usuário: " + e.getMessage();
                return new ResponseEntityError<>(false, HttpStatus.INTERNAL_SERVER_ERROR, mensagem, dataFormatada);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            String mensagem = "Erro ao excluir usuário: " + ex.getMessage();
            return new ResponseEntityError<>(false, HttpStatus.INTERNAL_SERVER_ERROR, mensagem, dataFormatada);
        }
    }
	
}
