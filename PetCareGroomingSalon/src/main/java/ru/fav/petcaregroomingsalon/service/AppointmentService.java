package ru.fav.petcaregroomingsalon.service;

import lombok.AllArgsConstructor;
import ru.fav.petcaregroomingsalon.dao.AppointmentDAO;
import ru.fav.petcaregroomingsalon.dao.ServicePriceDAO;
import ru.fav.petcaregroomingsalon.entity.Appointment;
import ru.fav.petcaregroomingsalon.entity.Client;
import ru.fav.petcaregroomingsalon.entity.Pet;

import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
public class AppointmentService {
    AppointmentDAO appointmentDAO;
    ServicePriceDAO servicePriceDAO;

    public void create(Appointment appointment) throws SQLException {
        appointmentDAO.create(appointment);
    }

    public Appointment findById(int id) throws SQLException {
        return appointmentDAO.findById(id).orElse(null);
    }

    public void cancel(Appointment appointment) throws SQLException {
        appointmentDAO.delete(appointment.getId());
    }

    public List<Appointment> findUpcomingByClient(Client client) throws SQLException {
        return appointmentDAO.findUpcomingByClientId(client.getId());
    }

    public List<Appointment> findUpcomingByPet(Pet pet) throws SQLException {
        return appointmentDAO.findUpcomingByPetId(pet.getId());
    }

    public void updateAppointmentPricesForPet(Pet pet) throws SQLException {
        appointmentDAO.updateAppointmentPricesForPet(pet.getId());
    }
}
