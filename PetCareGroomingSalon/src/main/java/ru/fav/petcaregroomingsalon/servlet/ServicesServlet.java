package ru.fav.petcaregroomingsalon.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.fav.petcaregroomingsalon.entity.*;
import ru.fav.petcaregroomingsalon.service.ClientService;
import ru.fav.petcaregroomingsalon.service.ServicePriceService;
import ru.fav.petcaregroomingsalon.service.ServiceService;

import java.io.IOException;
import java.security.Provider;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/services")
public class ServicesServlet extends HttpServlet {
    private ServiceService serviceService;
    private ServicePriceService servicePriceService;


    public void init(){
        this.servicePriceService = (ServicePriceService) getServletContext().getAttribute("servicePriceService");
        this.serviceService = (ServiceService) getServletContext().getAttribute("serviceService");
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {
        try {
            List<Service> services = serviceService.findAll();
            List<ServicePrice> servicePrices = servicePriceService.findAll();

            request.setAttribute("services", services);
            request.setAttribute("servicePrices", servicePrices);

            request.getRequestDispatcher("WEB-INF/views/services.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}