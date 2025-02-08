package ru.fav.petcaregroomingsalon.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.fav.petcaregroomingsalon.entity.Appointment;
import ru.fav.petcaregroomingsalon.entity.Client;
import ru.fav.petcaregroomingsalon.entity.Pet;
import ru.fav.petcaregroomingsalon.service.AppointmentService;
import ru.fav.petcaregroomingsalon.service.PetService;

import java.io.IOException;
import java.sql.SQLException;

@WebFilter({"/deletePet", "/editPet", "/petProfile"})
public class PetFilter implements Filter {
    private PetService petService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        Client client = (Client) session.getAttribute("client");
        int petId = Integer.parseInt(request.getParameter("petId"));

        try {
            Pet pet = petService.findById(petId);

            if(pet == null || pet.getOwner().getId() != client.getId()){
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
        this.petService = (PetService) filterConfig.getServletContext().getAttribute("petService");
    }

    @Override
    public void destroy() {}
}
