package ru.fav.petcaregroomingsalon.dao;

import lombok.AllArgsConstructor;
import ru.fav.petcaregroomingsalon.entity.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class ServiceDAO {
    private final DataSource dataSource;


    public void create(Service service) throws SQLException {
        String sql = "INSERT INTO service (name, description) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, service.getName());
            statement.setString(2, service.getDescription());
            statement.executeUpdate();
        }
    }

    public Optional<Service> findById(int id) throws SQLException {
        String sql = "SELECT * FROM service WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of( new Service(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description")
                ));
            }
        }
        return Optional.empty();
    }

    public List<Service> findAll() throws SQLException {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM service";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                services.add(new Service(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description")
                ));
            }
        }
        return services;
    }

    public void update(Service service) throws SQLException {
        String sql = "UPDATE service SET name = ?, description = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, service.getName());
            statement.setString(2, service.getDescription());
            statement.setInt(3, service.getId());
            statement.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM service WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}
