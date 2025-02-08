package ru.fav.petcaregroomingsalon.servlet.pet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.fav.petcaregroomingsalon.entity.Appointment;
import ru.fav.petcaregroomingsalon.entity.Pet;
import ru.fav.petcaregroomingsalon.service.AppointmentService;
import ru.fav.petcaregroomingsalon.service.PetService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/petProfile")
public class PetProfileServlet extends HttpServlet {
    private PetService petService;
    private AppointmentService appointmentService;

    public void init(){
        this.petService = (PetService) getServletContext().getAttribute("petService");
        this.appointmentService = (AppointmentService) getServletContext().getAttribute("appointmentService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int petId = Integer.parseInt(request.getParameter("petId"));

        try {
            Pet pet = petService.findById(petId);

            List<Appointment> upcomingAppointments = appointmentService.findUpcomingByPet(pet);

            request.setAttribute("upcomingAppointments", upcomingAppointments);
            request.setAttribute("pet", pet);
            request.getRequestDispatcher("WEB-INF/views/pet/petProfile.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
