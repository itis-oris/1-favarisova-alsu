package ru.fav.petcaregroomingsalon.dao;



import lombok.AllArgsConstructor;
import ru.fav.petcaregroomingsalon.entity.Appointment;
import ru.fav.petcaregroomingsalon.entity.TimeSlot;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class AppointmentDAO {
    private final DataSource dataSource;
    private final ServiceDAO serviceDAO;
    private final ServicePriceDAO servicePriceDAO;
    private final TimeSlotDAO timeSlotDAO;
    private final PetDAO petDAO;
    private final GroomerDAO groomerDAO;


    public void create(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO appointment (pet_id, groomer_id, service_id, price, date, notes) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, appointment.getPet().getId());
            statement.setInt(2, appointment.getGroomer().getId());
            statement.setInt(3, appointment.getService().getId());
            statement.setInt(4, appointment.getPrice());
            statement.setTimestamp(5, appointment.getDate());
            statement.setString(6, appointment.getNotes());
            statement.executeUpdate();
        }
    }

    public Optional<Appointment> findById(int id) throws SQLException {
        Appointment appointment = null;
        String sql = "SELECT * FROM appointment WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                appointment = new Appointment();
                appointment.setId(resultSet.getInt("id"));
                appointment.setPet(petDAO.findById(resultSet.getInt("pet_id")).orElse(null));
                appointment.setGroomer(groomerDAO.findById(resultSet.getInt("groomer_id")).orElse(null));
                appointment.setService(serviceDAO.findById(resultSet.getInt("service_id")).orElse(null));
                appointment.setPrice(resultSet.getInt("price"));
                appointment.setDate(resultSet.getTimestamp("date"));
                appointment.setNotes(resultSet.getString("notes"));
            }
        }
        return Optional.ofNullable(appointment);
    }

    public List<Appointment> findAll() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointment";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Appointment appointment = new Appointment();
                appointment.setId(resultSet.getInt("id"));
                appointment.setPet(petDAO.findById(resultSet.getInt("pet_id")).orElse(null));
                appointment.setGroomer(groomerDAO.findById(resultSet.getInt("groomer_id")).orElse(null));
                appointment.setService(serviceDAO.findById(resultSet.getInt("service_id")).orElse(null));
                appointment.setPrice(resultSet.getInt("price"));
                appointment.setDate(resultSet.getTimestamp("date"));
                appointment.setNotes(resultSet.getString("notes"));
                appointments.add(appointment);
            }
        }
        return appointments;
    }


    public List<Appointment> findUpcomingByClientId(int clientId) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.* FROM appointment a " +
                "JOIN pet p ON a.pet_id = p.id " +
                "WHERE p.owner_id = ? AND a.date > NOW()";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, clientId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Appointment appointment = new Appointment();
                appointment.setId(resultSet.getInt("id"));
                appointment.setPet(petDAO.findById(resultSet.getInt("pet_id")).orElse(null));
                appointment.setGroomer(groomerDAO.findById(resultSet.getInt("groomer_id")).orElse(null));
                appointment.setService(serviceDAO.findById(resultSet.getInt("service_id")).orElse(null));
                appointment.setPrice(resultSet.getInt("price"));
                appointment.setDate(resultSet.getTimestamp("date"));
                appointment.setNotes(resultSet.getString("notes"));
                appointments.add(appointment);
            }
        }
        return appointments;
    }

    public List<Appointment> findUpcomingByPetId(int petId) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointment WHERE pet_id = ? AND date > NOW()";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, petId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Appointment appointment = new Appointment();
                appointment.setId(resultSet.getInt("id"));
                appointment.setPet(petDAO.findById(petId).orElse(null));
                appointment.setGroomer(groomerDAO.findById(resultSet.getInt("groomer_id")).orElse(null));
                appointment.setService(serviceDAO.findById(resultSet.getInt("service_id")).orElse(null));
                appointment.setPrice(resultSet.getInt("price"));
                appointment.setDate(resultSet.getTimestamp("date"));
                appointment.setNotes(resultSet.getString("notes"));
                appointments.add(appointment);
            }
        }
        return appointments;
    }


    public void update(Appointment appointment) throws SQLException {
        String sql = "UPDATE appointment SET pet_id = ?, groomer_id = ?, service_id = ?, price = ?, date = ?, notes = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, appointment.getPet().getId());
            statement.setInt(2, appointment.getGroomer().getId());
            statement.setInt(3, appointment.getService().getId());
            statement.setInt(4, appointment.getPrice());
            statement.setTimestamp(5, appointment.getDate());
            statement.setString(6, appointment.getNotes());
            statement.setInt(7, appointment.getId());
            statement.executeUpdate();
        }
    }


    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM appointment WHERE id = ?";

        Optional<Appointment> optionalAppointment = findById(id);
        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();
            Optional<TimeSlot> optionalTimeSlot = timeSlotDAO.findByStartTimeAndGroomerId(appointment.getDate(), appointment.getGroomer().getId());

            timeSlotDAO.setEmpty(optionalTimeSlot.get().getId());
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public void updateAppointmentPricesForPet(int petId) throws SQLException {
        String sql = "UPDATE appointment SET price = ? WHERE id = ?";

        List<Appointment> appointments = findUpcomingByPetId(petId);

        Connection connection = dataSource.getConnection();

        try (connection) {
            connection.setAutoCommit(false);

            for (Appointment appointment : appointments) {
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    int newPrice = servicePriceDAO.findPriceForPetAndService(petId, appointment.getService().getId());

                    statement.setInt(1, newPrice);
                    statement.setInt(2, appointment.getId());
                    statement.executeUpdate();
                }
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }
}