package ru.fav.petcaregroomingsalon.service;

import lombok.AllArgsConstructor;
import ru.fav.petcaregroomingsalon.dao.PetDAO;
import ru.fav.petcaregroomingsalon.entity.Client;
import ru.fav.petcaregroomingsalon.entity.Pet;
import ru.fav.petcaregroomingsalon.exception.DayInFutureException;
import ru.fav.petcaregroomingsalon.exception.EmailAlreadyExistsException;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
public class PetService {
    private PetDAO petDAO;

    public void create(Pet pet) throws SQLException {
        if (pet.getBirthDate().after(new Date())) {
            throw new DayInFutureException("Этот день еще не наступил");
        }

        petDAO.create(pet);
    }

    public Pet findById(int id) throws SQLException {
        return petDAO.findById(id).orElse(null);
    }

    public List<Pet> findAllByOwner(Client owner) throws SQLException {
        return petDAO.findAllByOwnerId(owner.getId());
    }

    public void update(Pet pet) throws SQLException {
        if (pet.getBirthDate().after(new Date())) {
            throw new DayInFutureException("Этот день еще не наступил");
        }

        petDAO.update(pet);
    }

    public void delete(Pet pet) throws SQLException {
        petDAO.delete(pet.getId());
    }
}
