package ru.fav.petcaregroomingsalon.servlet.pet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.fav.petcaregroomingsalon.entity.Client;
import ru.fav.petcaregroomingsalon.entity.Pet;
import ru.fav.petcaregroomingsalon.service.AppointmentService;
import ru.fav.petcaregroomingsalon.service.BreedService;
import ru.fav.petcaregroomingsalon.service.PetService;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Date;
import java.util.Objects;

@WebServlet("/editPet")
public class PetEditServlet extends HttpServlet {
    private PetService petService;
    private BreedService breedService;
    private AppointmentService appointmentService;

    public void init(){
        this.petService = (PetService) getServletContext().getAttribute("petService");
        this.breedService = (BreedService) getServletContext().getAttribute("breedService");
        this.appointmentService = (AppointmentService) getServletContext().getAttribute("appointmentService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int petId = Integer.parseInt(request.getParameter("petId"));

        try {
            Pet pet = petService.findById(petId);

            request.setAttribute("pet", pet);

            if ("собака".equalsIgnoreCase(pet.getSpecies())) {
                request.setAttribute("breeds", breedService.findAll());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.getRequestDispatcher("WEB-INF/views/pet/editPet.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int petId = Integer.parseInt(request.getParameter("petId"));
        String name = request.getParameter("name");
        Date birthDate = Date.valueOf(request.getParameter("birthDate"));

        try {
            Pet pet = petService.findById(petId);
            pet.setName(name);
            pet.setBirthDate(birthDate);

            if (!Objects.equals(pet.getSpecies(), "кошка")) {
                int oldBreedId = pet.getBreed().getId();
                int newBreedId = Integer.parseInt(request.getParameter("breedId"));

                pet.setBreed(breedService.findById(newBreedId));

                petService.update(pet);

                if (!breedService.ifEqualBreedSize(newBreedId, oldBreedId)) {
                    appointmentService.updateAppointmentPricesForPet(pet);
                }
            }

            else petService.update(pet);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        response.sendRedirect("petProfile?petId=" + petId);
    }
}