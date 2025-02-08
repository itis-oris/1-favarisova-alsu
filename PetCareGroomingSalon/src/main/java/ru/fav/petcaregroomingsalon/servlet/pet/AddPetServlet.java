package ru.fav.petcaregroomingsalon.servlet.pet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.fav.petcaregroomingsalon.entity.Breed;
import ru.fav.petcaregroomingsalon.entity.Client;
import ru.fav.petcaregroomingsalon.entity.Pet;
import ru.fav.petcaregroomingsalon.service.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

@WebServlet("/addPet")
public class AddPetServlet extends HttpServlet {
    private PetService petService;
    private BreedService breedService;

    public void init(){
        this.petService = (PetService) getServletContext().getAttribute("petService");
        this.breedService = (BreedService) getServletContext().getAttribute("breedService");
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServletException {
        try {
            request.setAttribute("breeds", breedService.findAll());
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка загрузки списка пород", e);
        }

        request.getRequestDispatcher("WEB-INF/views/pet/addPet.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);

        Client client = (Client) session.getAttribute("client");
        String name = request.getParameter("name");
        Date birthDate = Date.valueOf(request.getParameter("birthDate"));
        String species = request.getParameter("species");

        Breed breed = null;
        if ("собака".equals(species)) {
            int breedId = Integer.parseInt(request.getParameter("breedId"));
            try {
                breed = breedService.findById(breedId);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        Pet pet = new Pet(name, species, breed, birthDate, client);
        try {
            petService.create(pet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        response.sendRedirect("clientProfile");
    }
}
