package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealDao {
    Meal add(Meal meal);

    Meal edit(Meal meal);

    boolean delete(long id);

    Meal getByID(long id);

    Collection<Meal> getList();
}
