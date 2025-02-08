package ru.fav.petcaregroomingsalon.servlet.client;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.fav.petcaregroomingsalon.entity.Client;
import ru.fav.petcaregroomingsalon.service.AppointmentService;
import ru.fav.petcaregroomingsalon.service.ClientService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/deleteClient")
public class ClientDeletionServlet extends HttpServlet {
    private ClientService clientService;
    private AppointmentService appointmentService;

    public void init(){
        this.clientService = (ClientService) getServletContext().getAttribute("clientService");
        this.appointmentService = (AppointmentService) getServletContext().getAttribute("appointmentService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        Client client = (Client) session.getAttribute("client");


        try {
            System.out.println(appointmentService.findUpcomingByClient(client));
            if (!appointmentService.findUpcomingByClient(client).isEmpty()) {
                response.sendRedirect("clientProfile?error=cancel_appointments_first");
                return;
            }
            clientService.delete(client);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/views/index.jsp");
        dispatcher.forward(request, response);
    }
}
