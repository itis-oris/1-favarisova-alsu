package ru.fav.petcaregroomingsalon.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.fav.petcaregroomingsalon.entity.Service;
import ru.fav.petcaregroomingsalon.entity.ServicePrice;
import ru.fav.petcaregroomingsalon.service.ServicePriceService;
import ru.fav.petcaregroomingsalon.service.ServiceService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("")
public class MainServlet extends HttpServlet {
    private ServiceService serviceService;


    public void init(){
        this.serviceService = (ServiceService) getServletContext().getAttribute("serviceService");
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            List<Service> services = serviceService.findAll();

            request.setAttribute("services", services);

            request.getRequestDispatcher("WEB-INF/views/index.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
