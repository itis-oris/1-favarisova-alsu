package ru.fav.petcaregroomingsalon.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Breed {
    private int id;
    private String name;
    private BreedTypeEnum breedType;

    public Breed(String name, BreedTypeEnum breedType) {
        this.name = name;
        this.breedType = breedType;
    }
}