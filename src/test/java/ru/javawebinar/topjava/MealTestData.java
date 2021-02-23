package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int MEAL_ID_STARTED = START_SEQ + 2;

    public static final int MEAL_ID_GET = MEAL_ID_STARTED+1;

    public static final int MEAL_ID_ADMIN1 = MEAL_ID_STARTED+3;
    public static final int MEAL_ID_ADMIN2 = MEAL_ID_STARTED+4;

    public static final Meal mealTest = new Meal(MEAL_ID_GET,LocalDateTime.of(2021, Month.FEBRUARY, 11,19,0),
                                "Ужин",1200);

    public static final Meal mealAdminTest1 = new Meal(MEAL_ID_ADMIN1,
                                            LocalDateTime.of(2021, Month.FEBRUARY, 11,10,00),
                                    "Завтрак",300);

    public static final Meal mealAdminTest2 = new Meal(MEAL_ID_ADMIN2,
                                            LocalDateTime.of(2021, Month.FEBRUARY, 12,13,30),
                                    "Обед",2300);

    public static Meal getNew() {
        return new Meal( LocalDateTime.of(2021, Month.FEBRUARY, 13, 11, 30),
                        "Обед", 1200);
    }

    public static Meal getDuplicate() {
        return new Meal(LocalDateTime.of(2021, Month.FEBRUARY, 11,19,0),
                "Ужин",1200);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static Meal getUpdated() {
        Meal meal = new Meal(mealTest);
        meal.setCalories(666);
        meal.setDescription("Сверхжрачь");
        return meal;
    }



}
