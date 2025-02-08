package ru.fav.petcaregroomingsalon.dao;


import lombok.AllArgsConstructor;
import ru.fav.petcaregroomingsalon.entity.Groomer;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class GroomerDAO {
    private final DataSource dataSource;


    public void create(Groomer groomer) throws SQLException {
        String sql = "INSERT INTO groomer (first_name, last_name, career_start, email, phone) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, groomer.getFirstName());
            statement.setString(2, groomer.getLastName());
            statement.setDate(3, groomer.getCareerStart());
            statement.setString(4, groomer.getEmail());
            statement.setString(5, groomer.getPhone());
            statement.executeUpdate();
        }
    }

    public Optional<Groomer> findById(int id) throws SQLException {
        String sql = "SELECT * FROM groomer WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of( new Groomer(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getDate("career_start"),
                        resultSet.getString("email"),
                        resultSet.getString("phone")
                ));
            }
        }
        return Optional.empty();
    }

    public List<Groomer> findAll() throws SQLException {
        List<Groomer> groomers = new ArrayList<>();
        String sql = "SELECT * FROM groomer";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                groomers.add(new Groomer(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getDate("career_start"),
                        resultSet.getString("email"),
                        resultSet.getString("phone")
                ));
            }
        }
        return groomers;
    }

    public void update(Groomer groomer) throws SQLException {
        String sql = "UPDATE groomer SET first_name = ?, last_name = ?, career_start = ?, email = ?, phone = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, groomer.getFirstName());
            statement.setString(2, groomer.getLastName());
            statement.setDate(3, groomer.getCareerStart());
            statement.setString(4, groomer.getEmail());
            statement.setString(5, groomer.getPhone());
            statement.setInt(6, groomer.getId());
            statement.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM groomer WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}