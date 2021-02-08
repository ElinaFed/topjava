package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.time.temporal.ChronoUnit.SECONDS;

public class MealCollectionDao implements MealDao {
    private ConcurrentMap<Long, Meal> mealSet;
    private static final LocalDateTime universalTime = LocalDateTime.of(2000, Month.JANUARY, 1, 0, 0, 0);
    private static AtomicInteger counter;

    {
        counter = new AtomicInteger(0);
    }

    public MealCollectionDao() {
        init();
    }

    private void init() {
        mealSet = new ConcurrentSkipListMap<>();
        List<Meal> mealList = MealsUtil.generateList();
        for (Meal item : mealList) {
            if (item.getID() == 0)
                item.setID(generateID(item.getDateTime()));
            mealSet.put(item.getID(), item);
        }
    }

    @Override
    public Meal getByID(long id) {
        return mealSet.get(id);
    }

    @Override
    public Meal add(Meal meal) {
        Meal mealToadd = meal;
        if (mealToadd.getID() == 0) {
            long idNew = generateID(mealToadd.getDateTime());
            mealToadd.setID(idNew);
        }
        mealSet.put(mealToadd.getID(), mealToadd);
        return mealToadd;
    }

    @Override
    public Meal edit(Meal meal) {
        Meal mealPrev = mealSet.get(meal.getID());
        if (mealPrev.getDateTime().compareTo(meal.getDateTime()) == 0)
            mealSet.replace(meal.getID(), meal);
        else {
            mealSet.remove(meal.getID());
            long id = generateID(meal.getDateTime());
            Meal mealNew = new Meal(id,
                    meal.getDateTime(),
                    meal.getDescription(),
                    meal.getCalories());
            mealSet.put(id, mealNew);
        }
        return mealSet.get(meal.getID());
    }

    @Override
    public boolean delete(long id) {
        Meal deleted = mealSet.remove(id);
        return (deleted == null);
    }

    @Override
    public Collection<Meal> getList() {
        return mealSet.values();
    }


    private static long generateID(LocalDateTime dateTime) {
        long cntSec = universalTime.until(dateTime, SECONDS) << 32;
        return cntSec + counter.getAndIncrement();
    }

}
