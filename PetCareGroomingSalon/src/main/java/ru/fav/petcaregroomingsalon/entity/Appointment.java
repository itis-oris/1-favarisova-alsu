package ru.fav.petcaregroomingsalon.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Appointment {
    private int id;
    private Pet pet;
    private Groomer groomer;
    private Service service;
    private int price;
    private java.sql.Timestamp date;
    private String notes;

    public Appointment(Pet pet, Groomer groomer, Service service, int price, java.sql.Timestamp date, String notes) {
        this.pet = pet;
        this.groomer = groomer;
        this.service = service;
        this.price = price;
        this.date = date;
        this.notes = notes;
    }

    public String getDateAndTime () {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        return date.toLocalDateTime().format(dateTimeFormatter);
    }
}