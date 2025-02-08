package ru.fav.petcaregroomingsalon.dao;

import lombok.AllArgsConstructor;
import ru.fav.petcaregroomingsalon.entity.Groomer;
import ru.fav.petcaregroomingsalon.entity.TimeSlot;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
public class TimeSlotDAO {
    private final DataSource dataSource;
    private final GroomerDAO groomerDao;


    public void create(TimeSlot timeSlot) throws SQLException {
        String sql = "INSERT INTO time_slot (groomer_id, start_time, end_time, taken) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, timeSlot.getGroomer().getId());
            statement.setTimestamp(2, timeSlot.getStartTime());
            statement.setTimestamp(3, timeSlot.getEndTime());
            statement.setBoolean(4, timeSlot.isTaken());
            statement.executeUpdate();
        }
    }

    public Optional<TimeSlot> findById(int id) throws SQLException {
        String sql = "SELECT * FROM time_slot where id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Optional<Groomer> groomer = groomerDao.findById(resultSet.getInt("groomer_id"));
                return Optional.of( new TimeSlot(
                        resultSet.getInt("id"),
                        groomer.orElse(null),
                        resultSet.getTimestamp("start_time"),
                        resultSet.getTimestamp("end_time"),
                        resultSet.getBoolean("taken")
                ));
            }
        }
        return Optional.empty();
    }

    public Optional<TimeSlot> findByStartTimeAndGroomerId(Timestamp startTime, int groomerId) throws SQLException {
        String sql = "SELECT * FROM time_slot where start_time = ? and groomer_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setTimestamp(1, startTime);
            statement.setInt(2, groomerId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Groomer groomer = groomerDao.findById(groomerId).orElse(null);
                return Optional.of( new TimeSlot(
                        resultSet.getInt("id"),
                        groomer,
                        startTime,
                        resultSet.getTimestamp("end_time"),
                        resultSet.getBoolean("taken")
                ));
            }
        }
        return Optional.empty();
    }


    public List<TimeSlot> findAll() throws SQLException {
        List<TimeSlot> timeSlots = new ArrayList<>();
        String sql = "SELECT * FROM time_slot";
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Groomer groomer = groomerDao.findById(resultSet.getInt("groomer_id")).orElse(null);
                TimeSlot timeSlot = new TimeSlot(
                        resultSet.getInt("id"),
                        groomer,
                        resultSet.getTimestamp("start_time"),
                        resultSet.getTimestamp("end_time"),
                        resultSet.getBoolean("taken")
                );
                timeSlots.add(timeSlot);
            }
        }
        return timeSlots;
    }

    public void update(TimeSlot timeSlot) throws SQLException {
        String sql = "UPDATE time_slot SET groomer_id = ?, start_time = ?, end_time = ?, taken = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, timeSlot.getGroomer().getId());
            statement.setTimestamp(2, timeSlot.getStartTime());
            statement.setTimestamp(3, timeSlot.getEndTime());
            statement.setBoolean(4, timeSlot.isTaken());
            statement.setInt(5, timeSlot.getId());
            statement.executeUpdate();
        }
    }

    public void delete(TimeSlot timeSlot) throws SQLException {
        String sql = "DELETE FROM time_slot WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, timeSlot.getId());
            statement.executeUpdate();
        }
    }

    public void setTaken(int id) throws SQLException {
        String sql = "UPDATE time_slot SET taken = true WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public void setEmpty(int id) throws SQLException {
        String sql = "UPDATE time_slot SET taken = false WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public Map<LocalDate, List<TimeSlot>> findAvailableTimeSlots() throws SQLException {
        Map<LocalDate, List<TimeSlot>> availableSlots = new TreeMap<>();
        String sql = "SELECT * FROM time_slot WHERE taken = FALSE AND start_time > CURRENT_TIMESTAMP";

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                LocalDate date = resultSet.getTimestamp("start_time").toLocalDateTime().toLocalDate();
                TimeSlot timeSlot = new TimeSlot();
                timeSlot.setId(resultSet.getInt("id"));
                timeSlot.setGroomer(groomerDao.findById(resultSet.getInt("groomer_id")).orElse(null));
                timeSlot.setStartTime(resultSet.getTimestamp("start_time"));
                timeSlot.setEndTime(resultSet.getTimestamp("end_time"));
                timeSlot.setTaken(resultSet.getBoolean("taken"));

                availableSlots.computeIfAbsent(date, k -> new ArrayList<>()).add(timeSlot);
            }
        }

        return availableSlots;
    }
}