package ru.fav.petcaregroomingsalon.servlet.appointment;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.fav.petcaregroomingsalon.entity.Appointment;
import ru.fav.petcaregroomingsalon.entity.Client;
import ru.fav.petcaregroomingsalon.service.AppointmentService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/cancelAppointment")
public class AppointmentCancellationServlet extends HttpServlet {
    private AppointmentService appointmentService;

    @Override
    public void init() {
        this.appointmentService = (AppointmentService) getServletContext().getAttribute("appointmentService");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));

        try {
            Appointment appointment = appointmentService.findById(appointmentId);

            appointmentService.cancel(appointment);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        response.sendRedirect("clientProfile");
    }
}
