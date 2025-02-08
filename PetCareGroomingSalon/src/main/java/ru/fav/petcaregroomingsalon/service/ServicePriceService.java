package ru.fav.petcaregroomingsalon.service;

import lombok.AllArgsConstructor;
import ru.fav.petcaregroomingsalon.dao.ServicePriceDAO;
import ru.fav.petcaregroomingsalon.entity.Pet;
import ru.fav.petcaregroomingsalon.entity.Service;
import ru.fav.petcaregroomingsalon.entity.ServicePrice;

import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
public class ServicePriceService {
    private ServicePriceDAO servicePriceDAO;

    public List<ServicePrice> findAll() throws SQLException {
        return servicePriceDAO.findAll();
    }

    public int findPriceForPetAndService(Pet pet, Service service) throws SQLException {
        return servicePriceDAO.findPriceForPetAndService(pet.getId(), service.getId());
    }
}
