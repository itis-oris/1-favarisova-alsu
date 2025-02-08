package ru.fav.petcaregroomingsalon.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.fav.petcaregroomingsalon.dao.ClientDAO;
import ru.fav.petcaregroomingsalon.entity.Client;
import ru.fav.petcaregroomingsalon.exception.EmailAlreadyExistsException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


@AllArgsConstructor
public class ClientService {
    private ClientDAO clientDAO;
    private PasswordEncoder passwordEncoder;
    private Map<String, Integer> rememberedClients;

    public Client login(String email, String password) throws SQLException {
        return clientDAO.findByEmail(email)
                .filter(client -> passwordEncoder.matches(password, client.getPassword()))
                .orElse(null);
    }

    public void register(Client client) throws SQLException {

        if (clientDAO.findByEmail(client.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Клиент с почтой " + client.getEmail() + " уже существует.");
        }

        String hashedPassword = passwordEncoder.encode(client.getPassword());
        client.setPassword(hashedPassword);
        clientDAO.create(client);
    }

    public Client findById(int id) throws SQLException {
        return clientDAO.findById(id).orElse(null);
    }

    public Client findByEmail(String email) throws SQLException {
        return clientDAO.findByEmail(email).orElse(null);
    }

    public void delete(Client client) throws SQLException {
        int clientId = client.getId();
        clientDAO.delete(clientId);
    }

    public void update(Client client, boolean changePassword) throws SQLException {
        if (clientDAO.findByEmail(client.getEmail()).isPresent()
                && clientDAO.findByEmail(client.getEmail()).get().getId() != client.getId()) {
            throw new EmailAlreadyExistsException("Клиент с почтой " + client.getEmail() + " уже существует.");
        }

        if (changePassword) {
            String hashedPassword = passwordEncoder.encode(client.getPassword());
            client.setPassword(hashedPassword);
        }

        clientDAO.update(client);
    }

    public void rememberClient(Client client, String rememberToken) {
        rememberedClients.put(rememberToken, client.getId());
    }

    public void logout(String token){
        rememberedClients.remove(token);
    }

    public Client findByRememberToken(String token) throws SQLException {
        int id = rememberedClients.get(token);

        return clientDAO.findById(id).orElse(null);
    }
}
