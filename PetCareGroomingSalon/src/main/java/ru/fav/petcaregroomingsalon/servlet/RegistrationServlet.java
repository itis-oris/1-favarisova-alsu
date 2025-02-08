package ru.fav.petcaregroomingsalon.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.fav.petcaregroomingsalon.entity.Client;
import ru.fav.petcaregroomingsalon.service.ClientService;
import ru.fav.petcaregroomingsalon.exception.EmailAlreadyExistsException;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    private ClientService clientService;

    public void init(){
        this.clientService = (ClientService) getServletContext().getAttribute("clientService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
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

        if (password == null || password.isEmpty()) {
            request.setAttribute("errorPassword", "Пароль не может быть пустым.");
            hasErrors = true;
        } else if (!password.equals(confirmPassword)) {
            request.setAttribute("errorConfirmPassword", "Пароли не совпадают.");
            hasErrors = true;
        }

        if (hasErrors) {
            request.getRequestDispatcher("WEB-INF/views/registration.jsp").forward(request, response);
            return;
        }

        try {
            Client client = new Client(firstName, lastName, email, phone, password);
            clientService.register(client);

            client = clientService.findByEmail(email);

            HttpSession session = request.getSession();
            session.setAttribute("client", client);
            response.sendRedirect("clientProfile");

        } catch (EmailAlreadyExistsException e) {
            request.setAttribute("errorEmail", e.getMessage());
            request.getRequestDispatcher("WEB-INF/views/registration.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("WEB-INF/views/registration.jsp").forward(request, response);
    }
}