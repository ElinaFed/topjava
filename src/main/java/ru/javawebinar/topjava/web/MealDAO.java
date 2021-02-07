package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface MealDAO {
    public boolean add(LocalDateTime dateTime,String description, int calories);

    public boolean edit(Meal meal);

    public boolean delete(Meal meal);

    public Meal getbyID(int id);

    public List<MealTo> getFilteredMeals(LocalTime startTime, LocalTime endTime);
}
