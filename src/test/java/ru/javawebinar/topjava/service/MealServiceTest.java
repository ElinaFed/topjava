package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.MEAL_ID_GET;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(MEAL_ID_GET, USER_ID);
        assertMatch(meal, MealTestData.mealTest);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID_GET, ADMIN_ID));
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(ADMIN_ID);
        MealTestData.assertMatch(all, MealTestData.mealAdminTest2, MealTestData.mealAdminTest1);
    }

    @Test
    public void delete() {
        service.delete(MEAL_ID_GET, USER_ID);
        assertThrows(NotFoundException.class,
                () -> service.get(MEAL_ID_GET, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class,
                () -> service.delete(MEAL_ID_GET, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> filtered = service.getBetweenInclusive(LocalDate.of(2021, Month.FEBRUARY, 12),
                LocalDate.of(2021, Month.FEBRUARY, 12),
                ADMIN_ID);
        MealTestData.assertMatch(filtered, MealTestData.mealAdminTest2);
    }

    @Test
    public void update() {
        Meal meal = MealTestData.getUpdated();
        service.update(meal, USER_ID);
        assertMatch(service.get(MEAL_ID_GET, USER_ID), meal);
    }

    @Test
    public void updateNotFound() {
        assertThrows(NotFoundException.class, () -> service.update(MealTestData.mealTest, ADMIN_ID));
    }

    @Test
    public void create() {
        Meal mealInserted = service.create(MealTestData.getNew(), ADMIN_ID);
        Integer mealId = mealInserted.getId();
        Meal mealTest = MealTestData.getNew();
        mealTest.setId(mealId);
        assertMatch(mealInserted, mealTest);
        assertMatch(service.get(mealId, ADMIN_ID), mealTest);
    }

    @Test
    public void duplicatedDateTimeForUser() {
        assertThrows(DataAccessException.class, () -> service.create(MealTestData.getDuplicate(), USER_ID));
    }
}