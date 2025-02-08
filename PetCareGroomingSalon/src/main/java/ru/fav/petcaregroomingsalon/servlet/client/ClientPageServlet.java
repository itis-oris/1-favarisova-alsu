package ru.fav.petcaregroomingsalon.servlet.client;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpSession;
import ru.fav.petcaregroomingsalon.entity.Appointment;
import ru.fav.petcaregroomingsalon.entity.Client;
import ru.fav.petcaregroomingsalon.entity.Pet;
import ru.fav.petcaregroomingsalon.service.AppointmentService;
import ru.fav.petcaregroomingsalon.service.PetService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


@WebServlet("/clientProfile")
public class ClientPageServlet extends HttpServlet {
    private PetService petService;
    private AppointmentService appointmentService;

    public void init(){
        this.appointmentService = (AppointmentService) getServletContext().getAttribute("appointmentService");
        this.petService = (PetService) getServletContext().getAttribute("petService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Client client = (Client) session.getAttribute("client");

        List<Pet> pets;
        List<Appointment> upcomingAppointments;
        try {
            upcomingAppointments = appointmentService.findUpcomingByClient(client);
            pets = petService.findAllByOwner(client);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        request.setAttribute("pets", pets);
        request.setAttribute("upcomingAppointments", upcomingAppointments);
        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/views/client/clientProfile.jsp");
        dispatcher.forward(request, response);

    }
}
