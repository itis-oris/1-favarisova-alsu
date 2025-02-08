package ru.fav.petcaregroomingsalon.service;

import lombok.AllArgsConstructor;
import ru.fav.petcaregroomingsalon.dao.TimeSlotDAO;
import ru.fav.petcaregroomingsalon.entity.TimeSlot;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class TimeSlotService {
    private TimeSlotDAO timeSlotDAO;

    public TimeSlot findById(int id) throws SQLException {
        return timeSlotDAO.findById(id).orElse(null);
    }

    public Map<LocalDate, List<TimeSlot>> findAvailableTimeSlots() throws SQLException {
        return timeSlotDAO.findAvailableTimeSlots();
    }

    public void setTaken(TimeSlot timeSlot) throws SQLException {
        timeSlotDAO.setTaken(timeSlot.getId());
    }

    public void setEmpty(TimeSlot timeSlot) throws SQLException {
        timeSlotDAO.setEmpty(timeSlot.getId());
    }
}
