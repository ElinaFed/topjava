package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    @Autowired
    private MealService service;

    public List<MealTo> getAll() {
        log.info("getAll");
        List<Meal> mealList = service.getAll(authUserId());
        return MealsUtil.getTos(mealList, authUserCaloriesPerDay());
    }

    public List<MealTo> getFiltered(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        log.info("getFiltered startDate={} endDate={} startTime={} endTime={}",startDate,endDate,startTime,endTime);
        List<Meal> filteredMeals = service.getFiltered(authUserId(),
                                                      (startDate != null) ? startDate : LocalDate.MIN,
                                                      (endDate != null) ? endDate : LocalDate.MAX);
        return MealsUtil.getFilteredTos(filteredMeals,
                authUserCaloriesPerDay(),
                (startTime != null) ? startTime : LocalTime.MIN,
                (endTime != null) ? endTime.minusNanos(1) : LocalTime.MAX);
    }

    public Meal get(int id) {
        log.info("get id={}", id);
        return service.get(authUserId(), id);
    }

    public boolean delete(int id) {
        log.info("delete id = {} ", id);
        return service.delete(authUserId(), id);
    }

    public Meal create(Meal meal) {
        log.info("create");
        checkNew(meal);
        return service.create(authUserId(), meal);
    }

    public void update(Meal meal, int id) {
        log.info("update id = {} ", id);
        assureIdConsistent(meal, id);
        service.update(authUserId(), meal);
    }
}