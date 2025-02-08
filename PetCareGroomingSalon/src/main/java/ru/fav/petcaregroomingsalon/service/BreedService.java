package ru.fav.petcaregroomingsalon.service;

import lombok.AllArgsConstructor;
import ru.fav.petcaregroomingsalon.dao.BreedDAO;
import ru.fav.petcaregroomingsalon.entity.Breed;

import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
public class BreedService {
    BreedDAO breedDAO;

    public Breed findById(int id) throws SQLException {
        return breedDAO.findById(id).orElse(null);
    }

    public List<Breed> findAll() throws SQLException {
        return breedDAO.findAll();
    }

    public boolean ifEqualBreedSize(int breedId1, int breedId2) throws SQLException {
        try {
            return breedDAO.findById(breedId1).get().getBreedType()
                    .equals(breedDAO.findById(breedId2).get().getBreedType());
        }
        catch (Exception e) {
            return false;
        }
    }
}
