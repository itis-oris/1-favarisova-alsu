package ru.fav.petcaregroomingsalon.dao;

import lombok.AllArgsConstructor;
import ru.fav.petcaregroomingsalon.entity.Breed;
import ru.fav.petcaregroomingsalon.entity.BreedTypeEnum;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class BreedDAO {
    private DataSource dataSource;


    public void create(Breed breed) throws SQLException {
        String sql = "INSERT INTO breed (name, breed_type) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, breed.getName());
            statement.setString(2, breed.getBreedType().name());
            statement.executeUpdate();
        }
    }

    public Optional<Breed> findById(int id) throws SQLException {
        String sql = "SELECT * FROM breed WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of( new Breed(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        BreedTypeEnum.valueOf(resultSet.getString("breed_type"))
                ));
            }
        }
        return Optional.empty();
    }

    public List<Breed> findAll() throws SQLException {
        List<Breed> breeds = new ArrayList<>();
        String sql = "SELECT * FROM breed";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                breeds.add(new Breed(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        BreedTypeEnum.valueOf(resultSet.getString("breed_type"))
                ));
            }
        }
        return breeds;
    }

    public void update(Breed breed) throws SQLException {
        String sql = "UPDATE breed SET name = ?, breed_type = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, breed.getName());
            statement.setString(2, breed.getBreedType().name());
            statement.setInt(3, breed.getId());
            statement.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM breed WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}