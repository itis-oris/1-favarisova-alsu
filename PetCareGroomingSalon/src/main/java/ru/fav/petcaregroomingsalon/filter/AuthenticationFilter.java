package ru.fav.petcaregroomingsalon.filter;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.fav.petcaregroomingsalon.entity.Client;
import ru.fav.petcaregroomingsalon.service.ClientService;

import java.io.IOException;
import java.sql.SQLException;

@WebFilter({"/clientProfile", "/confirmAppointment", "/selectAppointmentOptions", "/availableSlots",
        "/selectTimeSlot", "/appointmentDetails", "/cancelAppointment", "/addPet",
        "/addPetInfo", "/petProfile", "/selectPetType",
        "/editPet"})
public class AuthenticationFilter implements Filter {
    private ClientService clientService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        Client client = session != null ? (Client) session.getAttribute("client") : null;

        if (client == null) {
            Cookie[] cookies = httpRequest.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("rememberMe".equals(cookie.getName())) {
                        String token = cookie.getValue();
                        try {
                            client = clientService.findByRememberToken(token);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                        if (client != null) {
                            httpRequest.getSession().setAttribute("client", client);
                        }
                        break;
                    }
                }
            }

            if (client == null) {
                httpResponse.sendRedirect("login");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.clientService = (ClientService) filterConfig.getServletContext().getAttribute("clientService");
    }

    @Override
    public void destroy() {}
}