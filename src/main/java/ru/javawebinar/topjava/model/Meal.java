package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Meal implements Comparable<Meal> {
    private long id;

    private LocalDateTime dateTime;

    private String description;

    private int calories;

    public Meal(long id, LocalDateTime dateTime, String description, int calories) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this.id = 0;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public long getID() {
        return id;
    }

    public void setID(long id) {
        this.id = id;
    }


        @Override
        public boolean equals(Object o) {
            return (o instanceof Meal) && (this.id == ((Meal) o).getID());
        }

        @Override
        public int compareTo(Meal o)
        {
            return (this.id == o.getID()) ? 0 : (this.id > o.getID()) ? 1 : -1;
        }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }
}
