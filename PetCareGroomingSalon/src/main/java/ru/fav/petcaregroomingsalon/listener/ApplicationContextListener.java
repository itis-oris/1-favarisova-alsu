package ru.fav.petcaregroomingsalon.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.fav.petcaregroomingsalon.config.DataSourceConfiguration;
import ru.fav.petcaregroomingsalon.config.EncoderConfiguration;
import ru.fav.petcaregroomingsalon.dao.*;
import ru.fav.petcaregroomingsalon.filter.LoggerFilter;
import ru.fav.petcaregroomingsalon.service.*;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


@WebListener
public class ApplicationContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        migrateFlyway();


        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("application.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        DataSourceConfiguration configuration =
                new DataSourceConfiguration(properties);
        DataSource dataSource = configuration.hikariDataSource();


        EncoderConfiguration encoderConfiguration = new EncoderConfiguration();
        PasswordEncoder passwordEncoder = encoderConfiguration.bCryptPasswordEncoder();

        Logger logger = LogManager.getLogger(LoggerFilter.class);


        ServiceDAO serviceDAO = new ServiceDAO(dataSource);
        GroomerDAO groomerDAO = new GroomerDAO(dataSource);
        BreedDAO breedDAO = new BreedDAO(dataSource);
        ClientDAO clientDAO = new ClientDAO(dataSource);
        TimeSlotDAO timeSlotDAO = new TimeSlotDAO(dataSource, groomerDAO);
        PetDAO petDAO = new PetDAO(dataSource, clientDAO, breedDAO);
        ServicePriceDAO servicePriceDAO = new ServicePriceDAO(dataSource, petDAO, serviceDAO);
        AppointmentDAO appointmentDAO = new AppointmentDAO(dataSource, serviceDAO, servicePriceDAO, timeSlotDAO, petDAO, groomerDAO);


        Map<String, Integer> rememberedClients = new HashMap<>();


        AppointmentService appointmentService = new AppointmentService(appointmentDAO, servicePriceDAO);
        ServicePriceService servicePriceService = new ServicePriceService(servicePriceDAO);
        TimeSlotService timeSlotService = new TimeSlotService(timeSlotDAO);
        PetService petService = new PetService(petDAO);
        ServiceService serviceService = new ServiceService(serviceDAO);
        ClientService clientService = new ClientService(clientDAO, passwordEncoder, rememberedClients);
        BreedService breedService = new BreedService(breedDAO);




        ServletContext servletContext = sce.getServletContext();

        servletContext.setAttribute("logger", logger);
        servletContext.setAttribute("appointmentService", appointmentService);
        servletContext.setAttribute("servicePriceService", servicePriceService);
        servletContext.setAttribute("timeSlotService", timeSlotService);
        servletContext.setAttribute("petService", petService);
        servletContext.setAttribute("serviceService", serviceService);
        servletContext.setAttribute("clientService", clientService);
        servletContext.setAttribute("breedService", breedService);
        servletContext.setAttribute("rememberedClients", rememberedClients);
    }

    public void migrateFlyway() {
        Properties properties = new Properties();
        try {
            properties.load(Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("flyway.conf"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Flyway flyway = Flyway.configure()
                .configuration(properties)
                .load();

        flyway.migrate();
    }

}
