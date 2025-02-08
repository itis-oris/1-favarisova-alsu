package ru.fav.petcaregroomingsalon.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Groomer {
    private int id;
    private String firstName;
    private String lastName;
    private java.sql.Date careerStart;
    private String email;
    private String phone;


    public Groomer(String firstName, String lastName, java.sql.Date careerStart, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.careerStart = careerStart;
        this.email = email;
        this.phone = phone;
    }
}