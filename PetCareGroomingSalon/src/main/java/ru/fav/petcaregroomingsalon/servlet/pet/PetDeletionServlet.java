package ru.fav.petcaregroomingsalon.servlet.pet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.fav.petcaregroomingsalon.entity.Pet;
import ru.fav.petcaregroomingsalon.service.AppointmentService;
import ru.fav.petcaregroomingsalon.service.PetService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/deletePet")
public class PetDeletionServlet extends HttpServlet {
    private PetService petService;
    private AppointmentService appointmentService;

    public void init(){
        this.petService = (PetService) getServletContext().getAttribute("petService");
        this.appointmentService = (AppointmentService) getServletContext().getAttribute("appointmentService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int petId = Integer.parseInt(request.getParameter("petId"));

        try {
            Pet pet = petService.findById(petId);

            if (!appointmentService.findUpcomingByPet(pet).isEmpty()) {
                response.sendRedirect("petProfile?petId=" + petId + "&error=cancel_appointments_first");
                return;
            }
            petService.delete(pet);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        response.sendRedirect("clientProfile");
    }
}
