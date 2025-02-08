package ru.fav.petcaregroomingsalon.servlet.appointment;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.fav.petcaregroomingsalon.dao.TimeSlotDAO;
import ru.fav.petcaregroomingsalon.entity.TimeSlot;
import ru.fav.petcaregroomingsalon.service.AppointmentService;
import ru.fav.petcaregroomingsalon.service.ServicePriceService;
import ru.fav.petcaregroomingsalon.service.TimeSlotService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@WebServlet("/selectTimeSlot")
public class TimeSlotSelectionServlet extends HttpServlet {
    private TimeSlotService timeSlotService;

    public void init(){
        this.timeSlotService = (TimeSlotService) getServletContext().getAttribute("timeSlotService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<LocalDate, List<TimeSlot>> availableSlots;
        try {
            availableSlots = timeSlotService.findAvailableTimeSlots();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("availableSlots", availableSlots);
        request.getRequestDispatcher("WEB-INF/views/appointment/selectTimeSlot.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            TimeSlot timeSlot = timeSlotService.findById(Integer.parseInt(request.getParameter("timeSlotId")));
            request.getSession().setAttribute("selectedTimeSlot", timeSlot);
            response.sendRedirect("confirmAppointment");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
