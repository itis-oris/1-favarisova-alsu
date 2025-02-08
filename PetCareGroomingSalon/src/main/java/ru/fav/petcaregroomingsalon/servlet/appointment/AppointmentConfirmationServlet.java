package ru.fav.petcaregroomingsalon.servlet.appointment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.fav.petcaregroomingsalon.entity.*;
import ru.fav.petcaregroomingsalon.service.AppointmentService;
import ru.fav.petcaregroomingsalon.service.ServicePriceService;
import ru.fav.petcaregroomingsalon.service.TimeSlotService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/confirmAppointment")
public class AppointmentConfirmationServlet extends HttpServlet {
    private AppointmentService appointmentService;
    private ServicePriceService servicePriceService;
    private TimeSlotService timeSlotService;

    public void init(){
        this.appointmentService = (AppointmentService) getServletContext().getAttribute("appointmentService");
        this.servicePriceService = (ServicePriceService) getServletContext().getAttribute("servicePriceService");
        this.timeSlotService = (TimeSlotService) getServletContext().getAttribute("timeSlotService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Pet pet = (Pet) request.getSession().getAttribute("selectedPet");
        Service service = (Service) request.getSession().getAttribute("selectedService");

        try {
            int price = servicePriceService.findPriceForPetAndService(pet, service);
            request.setAttribute("price", price);
            request.getRequestDispatcher("WEB-INF/views/appointment/confirmAppointment.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Pet pet = (Pet) request.getSession().getAttribute("selectedPet");
        Service service = (Service) request.getSession().getAttribute("selectedService");
        TimeSlot timeSlot = (TimeSlot) request.getSession().getAttribute("selectedTimeSlot");
        int price = Integer.parseInt(request.getParameter("price"));

        Appointment appointment = new Appointment();
        appointment.setPet(pet);
        appointment.setGroomer(timeSlot.getGroomer());
        appointment.setService(service);
        appointment.setPrice(price);
        appointment.setDate(timeSlot.getStartTime());

        try {
            appointmentService.create(appointment);
            timeSlotService.setTaken(timeSlot);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        response.sendRedirect("clientProfile");
    }
}