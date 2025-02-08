package ru.fav.petcaregroomingsalon.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServicePrice {
    private int id;
    private Service service;
    private String species;
    private BreedTypeEnum breedType;
    private int price;

    public ServicePrice(Service service, String species, BreedTypeEnum breedType, int price) {
        this.service = service;
        this.species = species;
        this.breedType = breedType;
        this.price = price;
    }
}