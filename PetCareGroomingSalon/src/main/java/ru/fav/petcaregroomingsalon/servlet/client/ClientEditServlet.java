package ru.fav.petcaregroomingsalon.servlet.client;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.fav.petcaregroomingsalon.exception.EmailAlreadyExistsException;
import ru.fav.petcaregroomingsalon.service.ClientService;
import ru.fav.petcaregroomingsalon.entity.Client;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/editProfile")
public class ClientEditServlet extends HttpServlet {
    private ClientService clientService;

    @Override
    public void init() {
        this.clientService = (ClientService) getServletContext().getAttribute("clientService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("client") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Client client = (Client) session.getAttribute("client");
        request.setAttribute("client", client);
        request.getRequestDispatcher("WEB-INF/views/client/clientEdit.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("client") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Client client = (Client) session.getAttribute("client");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        boolean changePassword = "on".equals(request.getParameter("changePassword"));
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        boolean hasErrors = false;

        if (firstName == null || firstName.isEmpty()) {
            request.setAttribute("errorFirstName", "Имя не может быть пустым.");
            hasErrors = true;
        }

        if (lastName == null || lastName.isEmpty()) {
            request.setAttribute("errorLastName", "Фамилия не может быть пустой.");
        }

        if (email == null || email.isEmpty()) {
            request.setAttribute("errorEmail", "Email не может быть пустым.");
            hasErrors = true;
        }

        if (changePassword) {
            if (password == null || password.isEmpty()) {
                request.setAttribute("errorPassword", "Пароль не может быть пустым.");
                hasErrors = true;
            } else if (!password.equals(confirmPassword)) {
                request.setAttribute("errorConfirmPassword", "Пароли не совпадают.");
                hasErrors = true;
            }
        }

        if (hasErrors) {
            request.setAttribute("client", client);
            request.getRequestDispatcher("WEB-INF/views/client/clientEdit.jsp").forward(request, response);
            return;
        }

        try {
            client.setFirstName(firstName);
            client.setLastName(lastName);
            client.setPhone(phone);
            client.setEmail(email);

            if (changePassword) {
                client.setPassword(password);
            }

            clientService.update(client, changePassword);

            session.setAttribute("client", client);
            response.sendRedirect("clientProfile");

        } catch (EmailAlreadyExistsException e) {
            request.setAttribute("errorEmail", e.getMessage());
            request.getRequestDispatcher("WEB-INF/views/client/clientEdit.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении профиля клиента", e);
        }
    }
}