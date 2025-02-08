package ru.fav.petcaregroomingsalon.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.fav.petcaregroomingsalon.entity.Client;
import ru.fav.petcaregroomingsalon.service.ClientService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private ClientService clientService;

    public void init(){
        this.clientService = (ClientService) getServletContext().getAttribute("clientService");
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Client client = (Client) session.getAttribute("client");

        if (client != null) {

            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("rememberMe".equals(cookie.getName())) {
                        String token = cookie.getValue();

                        clientService.logout(token);

                    }
                }
            }


            Cookie cookie = new Cookie("rememberMe", "");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath());
    }
}