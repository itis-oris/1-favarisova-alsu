package ru.fav.petcaregroomingsalon.dao;

import lombok.AllArgsConstructor;
import ru.fav.petcaregroomingsalon.entity.Client;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class ClientDAO {
    private final DataSource dataSource;


    public void create(Client client) throws SQLException {
        String sql = "INSERT INTO client (first_name, last_name, email, phone, password) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, client.getFirstName());
            statement.setString(2, client.getLastName());
            statement.setString(3, client.getEmail());
            statement.setString(4, client.getPhone());
            statement.setString(5, client.getPassword());
            statement.executeUpdate();
        }
    }

    public Optional<Client> findById(int id) throws SQLException {
        String sql = "SELECT * FROM client WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of( new Client(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("password")
                ));
            }
        }
        return Optional.empty();
    }


    public Optional<Client> findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM client WHERE email = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of( new Client(
                            resultSet.getInt("id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            email,
                            resultSet.getString("phone"),
                            resultSet.getString("password")
                    ));
                }
            }
        }
        return Optional.empty();
    }


    public List<Client> findAll() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                clients.add(new Client(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("password")
                ));
            }
        }
        return clients;
    }

    public void update(Client client) throws SQLException {
        String sql = "UPDATE client SET first_name = ?, last_name = ?, email = ?, phone = ?, password = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, client.getFirstName());
            statement.setString(2, client.getLastName());
            statement.setString(3, client.getEmail());
            statement.setString(4, client.getPhone());
            statement.setString(5, client.getPassword());
            statement.setInt(6, client.getId());
            statement.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM client WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}