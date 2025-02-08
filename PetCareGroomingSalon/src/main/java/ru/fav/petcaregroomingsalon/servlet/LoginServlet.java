package ru.fav.petcaregroomingsalon.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.fav.petcaregroomingsalon.entity.Client;
import ru.fav.petcaregroomingsalon.service.ClientService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private ClientService clientService;

    public void init(){
        this.clientService = (ClientService) getServletContext().getAttribute("clientService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            request.getRequestDispatcher("WEB-INF/views/login.jsp?error=empty").forward(request, response);
            return;
        }


        Client client;
        try {
            client = clientService.login(email, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (client != null) {
            if ("on".equals(rememberMe)) {
                String rememberToken = UUID.randomUUID().toString();
                clientService.rememberClient(client, rememberToken);
                Cookie rememberCookie = new Cookie("rememberMe", rememberToken);
                rememberCookie.setMaxAge(7 * 24 * 60 * 60);
                response.addCookie(rememberCookie);
            }

            HttpSession session = request.getSession();
            session.setAttribute("client", client);
            response.sendRedirect("clientProfile");
        } else {
            request.getRequestDispatcher("WEB-INF/views/login.jsp?error=invalid").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("WEB-INF/views/login.jsp").forward(request, response);

    }
}