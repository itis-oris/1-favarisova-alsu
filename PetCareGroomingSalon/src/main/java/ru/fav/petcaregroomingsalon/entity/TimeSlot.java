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
public class TimeSlot {
    private int id;
    private Groomer groomer;
    private java.sql.Timestamp startTime;
    private java.sql.Timestamp endTime;
    private boolean taken;

    public TimeSlot(Groomer groomer, java.sql.Timestamp startTime, java.sql.Timestamp endTime, boolean taken) {
        this.groomer = groomer;
        this.startTime = startTime;
        this.endTime = endTime;
        this.taken = taken;
    }

    public String getDateAndTime () {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        return startTime.toLocalDateTime().format(dateTimeFormatter);
    }

    public String getTime () {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return startTime.toLocalDateTime().format(dateTimeFormatter);
    }
}
