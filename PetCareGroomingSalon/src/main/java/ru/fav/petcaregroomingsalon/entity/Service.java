package ru.fav.petcaregroomingsalon.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Service {
    private int id;
    private String name;
    private String description;

    public Service(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
