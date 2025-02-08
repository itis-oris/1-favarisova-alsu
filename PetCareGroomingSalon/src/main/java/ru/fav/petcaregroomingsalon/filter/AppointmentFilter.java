package ru.fav.petcaregroomingsalon.filter;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.fav.petcaregroomingsalon.entity.Appointment;
import ru.fav.petcaregroomingsalon.entity.Client;
import ru.fav.petcaregroomingsalon.service.AppointmentService;

import java.io.IOException;
import java.sql.SQLException;

@WebFilter({"/cancelAppointment", "/appointmentDetails"})
public class AppointmentFilter implements Filter {
    private AppointmentService appointmentService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        Client client = (Client) session.getAttribute("client");
        int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));

        try {
            Appointment appointment = appointmentService.findById(appointmentId);

            if (appointment == null || appointment.getPet().getOwner().getId() != client.getId()) {
                httpResponse.sendRedirect("clientProfile");
            }
            else {
                chain.doFilter(request, response);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.appointmentService = (AppointmentService) filterConfig.getServletContext().getAttribute("appointmentService");
    }

    @Override
    public void destroy() {}
}