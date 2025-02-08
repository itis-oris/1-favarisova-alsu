package ru.fav.petcaregroomingsalon.dao;

import lombok.AllArgsConstructor;
import ru.fav.petcaregroomingsalon.entity.BreedTypeEnum;
import ru.fav.petcaregroomingsalon.entity.Pet;
import ru.fav.petcaregroomingsalon.entity.ServicePrice;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class ServicePriceDAO {
    private final DataSource dataSource;
    private final PetDAO petDAO;
    private final ServiceDAO serviceDAO;


    public void create(ServicePrice servicePrice) throws SQLException {
        String sql = "INSERT INTO service_price (service_id, species, breed_type, price) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, servicePrice.getService().getId());
            statement.setString(2, servicePrice.getSpecies());
            statement.setString(3, servicePrice.getBreedType() != null ? servicePrice.getBreedType().name() : null);
            statement.setInt(4, servicePrice.getPrice());
            statement.executeUpdate();
        }
    }

    public Optional<ServicePrice> findById(int id) throws SQLException {
        String sql = "SELECT * FROM service_price WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of( new ServicePrice(
                        resultSet.getInt("id"),
                        serviceDAO.findById(resultSet.getInt("service_id")).orElse(null),
                        resultSet.getString("species"),
                        resultSet.getString("breed_type") != null ? BreedTypeEnum.valueOf(resultSet.getString("breed_type")) : null,
                        resultSet.getInt("price")
                ));
            }
        }
        return Optional.empty();
    }

    public List<ServicePrice> findAll() throws SQLException {
        List<ServicePrice> servicePrices = new ArrayList<>();
        String sql = "SELECT * FROM service_price";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                servicePrices.add(new ServicePrice(
                        resultSet.getInt("id"),
                        serviceDAO.findById(resultSet.getInt("service_id")).orElse(null),
                        resultSet.getString("species"),
                        resultSet.getString("breed_type") != null ? BreedTypeEnum.valueOf(resultSet.getString("breed_type")) : null,
                        resultSet.getInt("price")
                ));
            }
        }
        return servicePrices;
    }

    public void update(ServicePrice servicePrice) throws SQLException {
        String sql = "UPDATE service_price SET service_id = ?, species = ?, breed_type = ?, price = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, servicePrice.getService().getId());
            statement.setString(2, servicePrice.getSpecies());
            statement.setString(3,  servicePrice.getBreedType() != null ? servicePrice.getBreedType().name() : null);
            statement.setInt(4, servicePrice.getPrice());
            statement.setInt(5, servicePrice.getId());
            statement.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM service_price WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public int findPriceForPetAndService(int petId, int serviceId) throws SQLException {
        Optional<Pet> optionalPet = petDAO.findById(petId);
        Pet pet = optionalPet.orElse(null);
        if (pet == null) {
            throw new SQLException("Pet not found");
        }

        String species = pet.getSpecies();
        String breedType;

        if ("собака".equalsIgnoreCase(species) && pet.getBreed() != null) {
            breedType = pet.getBreed().getBreedType().name();
        } else if ("кошка".equalsIgnoreCase(species)) {
            breedType = null;
        } else {
            throw new IllegalArgumentException("Unsupported pet species: " + species);
        }

        String sql = """
            SELECT *
            FROM service_price
            WHERE service_id = ?
            AND species = ?
            AND (breed_type = ?::breed_type_enum OR breed_type IS NULL)
        """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, serviceId);
            statement.setString(2, species);
            statement.setString(3, breedType);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("price");
            }
        }
        return -1;
    }
}