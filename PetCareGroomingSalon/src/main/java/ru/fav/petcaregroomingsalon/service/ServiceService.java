package ru.fav.petcaregroomingsalon.service;

import lombok.AllArgsConstructor;
import ru.fav.petcaregroomingsalon.dao.ServiceDAO;
import ru.fav.petcaregroomingsalon.entity.Service;

import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
public class ServiceService {
    private ServiceDAO serviceDAO;

    public Service findById(int id) throws SQLException {
        return serviceDAO.findById(id).orElse(null);
    }

    public List<Service> findAll() throws SQLException {
        return serviceDAO.findAll();
    }
}
