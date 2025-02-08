package ru.fav.petcaregroomingsalon.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Pet {
    private int id;
    private String name;
    private String species;
    private Breed breed;
    private java.sql.Date birthDate;
    private Client owner;

    public Pet(String name, String species, Breed breed, java.sql.Date birthDate, Client owner) {
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.birthDate = birthDate;
        this.owner = owner;
    }
}