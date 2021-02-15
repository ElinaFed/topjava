package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MealsUtil {
    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static void initStartList(MealRestController controller)
    {
        SecurityUtil.setAuthUserId(1);
        controller.create(new Meal(LocalDateTime.of(2021, Month.FEBRUARY, 10, 10, 0), "Завтрак", 500)) ;
        controller.create(new Meal(LocalDateTime.of(2021, Month.FEBRUARY, 10, 13, 0), "Обед", 1000)) ;
        controller.create(new Meal(LocalDateTime.of(2021, Month.FEBRUARY, 10, 19, 0), "Ужин", 200)) ;
        controller.create(new Meal(LocalDateTime.of(2021, Month.FEBRUARY, 11, 9, 0), "Завтрак", 500)) ;
        controller.create(new Meal(LocalDateTime.of(2021, Month.FEBRUARY, 11, 14, 0), "Обед", 1200)) ;
        controller.create(new Meal(LocalDateTime.of(2021, Month.FEBRUARY, 11, 20, 0), "Ужин", 430)) ;
        SecurityUtil.setAuthUserId(2);
        controller.create(new Meal(LocalDateTime.of(2021, Month.FEBRUARY, 10, 10, 0), "Завтрак", 500)) ;
        controller.create(new Meal(LocalDateTime.of(2021, Month.FEBRUARY, 10, 13, 0), "Обед", 1100)) ;
        controller.create(new Meal(LocalDateTime.of(2021, Month.FEBRUARY, 10, 19, 0), "Ужин", 300)) ;
        controller.create(new Meal(LocalDateTime.of(2021, Month.FEBRUARY, 10, 23, 0), "Ужин", 200)) ;
    }

    public static List<MealTo> getTos(Collection<Meal> meals, int caloriesPerDay) {
        return filterByPredicate(meals, caloriesPerDay, meal -> true);
    }

    public static List<MealTo> getFilteredTos(Collection<Meal> meals, int caloriesPerDay, LocalTime startTime, LocalTime endTime) {
        return filterByPredicate(meals, caloriesPerDay, meal -> DateTimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime));
    }

    public static List<MealTo> filterByPredicate(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(filter)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
    }
}
