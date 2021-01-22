package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;

public class UserMeal
{
    private final LocalDateTime dateTime;
    private final String description;
    private final int calories;

    public UserMeal(LocalDateTime dateTime, String description, int calories)
    {
                this.dateTime = dateTime;
                this.description = description;
                this.calories = calories;
    }

    public LocalDateTime getDateTime()
    {
        return dateTime;
    }

    public LocalDate getDate()
    {
        return LocalDate.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth());
    }

    public LocalTime getTime()
    {
        return LocalTime.of(dateTime.getHour(),dateTime.getMinute(),dateTime.getSecond());
    }
    
    public String getDescription()
    {
        return description;
    }

    public int getCalories()
    {
        return calories;
    }
}
