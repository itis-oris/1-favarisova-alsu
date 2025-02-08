package ru.fav.petcaregroomingsalon.exception;

public class DayInFutureException extends RuntimeException {
    public DayInFutureException(String message) {
        super(message);
    }
}