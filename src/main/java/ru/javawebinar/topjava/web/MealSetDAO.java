package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class MealSetDAO implements MealDAO{
    Set<Meal> mealSet;
    private int limitCaloriesDay;
    private final int MAX_CALORIES_DAY = 2000;

    public MealSetDAO(){
        init();
    }

    private void init() {
        limitCaloriesDay = MAX_CALORIES_DAY;
        mealSet = new ConcurrentSkipListSet<>(MealsUtil.generateList());
    }

    @Override
    public Meal getbyID(int id){
        Iterator<Meal> iter = mealSet.iterator();
        while(iter.hasNext())
        {
            Meal curr = iter.next();
            if (curr.getID() == id)
                return curr;
        }
        return null;
    }

    @Override
    public boolean add(LocalDateTime dateTime, String description, int calories) {
        Meal meal = new Meal(generateID(dateTime), dateTime, description, calories);
        mealSet.add(meal);
        return true;
    }

    @Override
    public boolean edit(Meal meal){
        mealSet.remove(meal);
        Meal mealNew = new Meal(generateID(meal.getDateTime()), meal.getDateTime(), meal.getDescription(),meal.getCalories());
        mealSet.add(mealNew);
        return true;
    }

    @Override
    public boolean delete(Meal meal){
        mealSet.remove(meal);
        return true;
    }

    public List<MealTo> getFilteredMeals(LocalTime startTime, LocalTime endTime){
        return MealsUtil.filteredByStreams(mealSet,startTime,endTime,limitCaloriesDay);
    }

    private long generateID(LocalDateTime dateTime) {
        return dateTime.toEpochSecond(ZoneOffset.UTC);
    }

}
