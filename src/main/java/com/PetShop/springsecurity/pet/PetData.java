package com.PetShop.springsecurity.pet;

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

import com.PetShop.springsecurity.Response.ResponseEntity;
import com.PetShop.springsecurity.Response.ResponseEntity.ResponseEntityError;
import com.PetShop.springsecurity.Response.ResponseEntity.ResponseEntitySuccess;


public class PetData {
	
	LocalDateTime Now = LocalDateTime.now();
    DateTimeFormatter formatterData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String dataFormatada = formatterData.format(Now);
	
	private DataSource dataSource;

    public PetData(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Pet> findByPetId(long id) {
        List<Pet> petList = new ArrayList<>();
        String query = "SELECT * FROM PET WHERE id = ?";
        
        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Pet pet = new Pet();
                    pet.setId(rs.getLong("id"));
                    pet.setAge(rs.getInt("age"));
                    pet.setAnimalType(rs.getString("animal_type"));
                    pet.setName(rs.getString("name"));
                    pet.setSpecies(rs.getString("species"));
                    pet.setColor(rs.getString("color"));
                    pet.setHeight(rs.getString("height"));
                    pet.setWeight(rs.getString("weight"));
                    
                    petList.add(pet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return petList;
    }

    public ResponseEntity<String> deletePetById(long id) {
        try 
        {
    	String query = "DELETE FROM PET WHERE id = ?";
        
        try (Connection con = dataSource.getConnection();
                PreparedStatement pst = con.prepareStatement(query)) {

               pst.setLong(1, id);
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

    public ResponseEntity<String> UpDatePet(Long Id, Pet petRequest) {

        // Monta a query de atualização
        StringBuilder queryBuilder = new StringBuilder("UPDATE PET SET ");
        List<Object> params = new ArrayList<>();

        if (petRequest.getAge() != null) {
            queryBuilder.append("age = ?, ");
            params.add(petRequest.getAge());
        }
        if (petRequest.getAnimalType() != null) {
            queryBuilder.append("animal_type = ?, ");
            params.add(petRequest.getAnimalType());
        }
        if (petRequest.getName() != null) {
            queryBuilder.append("name = ?, ");
            params.add(petRequest.getName());
        }
        if (petRequest.getSpecies() != null) {
            queryBuilder.append("species = ?, ");
            params.add(petRequest.getSpecies());
        }
        if (petRequest.getColor() != null) {
            queryBuilder.append("color = ?, ");
            params.add(petRequest.getColor());
        }
        if (petRequest.getHeight() != null) {
            queryBuilder.append("height = ?, ");
            params.add(petRequest.getHeight());
        }
        if (petRequest.getWeight() != null) {
            queryBuilder.append("weight = ?, ");
            params.add(petRequest.getWeight());
        }

        // Remove a vírgula extra no final da query
        queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());

        // Adiciona a condição WHERE para o ID
        queryBuilder.append(" WHERE id = ?");
        params.add(Id);

        // Monta a query final
        String query = queryBuilder.toString();

        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            // Configura os parâmetros da query com os valores dos campos atualizados do objeto Pet
            int index = 1;
            for (Object param : params) {
                pst.setObject(index++, param);
            }

            // Executa a query de atualização
            int rowsUpdated = pst.executeUpdate();

            // Verifica se algum registro foi atualizado
            if (rowsUpdated > 0) {
                String mensagem = "Pet atualizado com sucesso.";
                return new ResponseEntitySuccess<>(true, HttpStatus.OK, mensagem, dataFormatada);
            } else {
                String mensagem = "Pet não encontrado com o ID fornecido.";
                return new ResponseEntityError<>(false, HttpStatus.NOT_FOUND, mensagem, dataFormatada);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            String mensagem = "Erro ao atualizar pet: " + e.getMessage();
            return new ResponseEntityError<>(false, HttpStatus.INTERNAL_SERVER_ERROR, mensagem, dataFormatada);
        }
    }

}
