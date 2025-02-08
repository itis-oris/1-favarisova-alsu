package ru.fav.petcaregroomingsalon.servlet.appointment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.fav.petcaregroomingsalon.dao.PetDAO;
import ru.fav.petcaregroomingsalon.dao.ServiceDAO;
import ru.fav.petcaregroomingsalon.entity.Client;
import ru.fav.petcaregroomingsalon.entity.Pet;
import ru.fav.petcaregroomingsalon.entity.Service;
import ru.fav.petcaregroomingsalon.service.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/selectAppointmentOptions")
public class AppointmentOptionsSelectionServlet extends HttpServlet {
    private PetService petService;
    private ServiceService serviceService;

    public void init(){
        this.petService = (PetService) getServletContext().getAttribute("petService");
        this.serviceService = (ServiceService) getServletContext().getAttribute("serviceService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        Client client = (Client) session.getAttribute("client");

        List<Pet> pets;
        List<Service> services;
        try {
            pets = petService.findAllByOwner(client);
            services = serviceService.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        request.setAttribute("pets", pets);
        request.setAttribute("services", services);
        request.getRequestDispatcher("WEB-INF/views/appointment/selectAppointmentOptions.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Pet pet = petService.findById(Integer.parseInt(request.getParameter("petId")));
            Service service = serviceService.findById(Integer.parseInt(request.getParameter("serviceId")));

            request.getSession().setAttribute("selectedPet", pet);
            request.getSession().setAttribute("selectedService", service);

            response.sendRedirect("selectTimeSlot");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}