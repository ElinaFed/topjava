package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Meal save(int userId, Meal meal) {
        log.info("save {}", userId);
        if (meal.isNew()) {

            meal.setId(counter.incrementAndGet());
            Map<Integer, Meal>  userMeals = repository.computeIfAbsent(userId, integer -> new ConcurrentHashMap<>());
            userMeals.putIfAbsent(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals != null) {
            return userMeals.replace(meal.getId(), meal);
        }
        return null;
    }

    @Override
    public boolean delete(int userId, int id) {
        log.info("delete {}", userId + " idMeal = " + id);
        Map<Integer, Meal> userMeals = repository.get(userId);
        return userMeals != null && (userMeals.remove(id) != null);
    }

    @Override
    public Meal get(int userId, int id) {
        log.info("get {} idMeal = {}", userId, id);
        Map<Integer, Meal> userMeals = repository.get(userId);
        return (userMeals != null) ? userMeals.get(id) : null;
    }

    @Override
    public List getAll(int userId) {
        log.info("getAll {}", userId);
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals != null) {
            return getFiltered(userMeals, ml -> true);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<Meal> getFiltered(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getFiltered {} startDate ={} endDate = {}", userId, startDate, endDate);
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals != null) {
            Predicate<Meal> filter = ml -> DateTimeUtil.isBetweenHalfOpen(ml.getDate(), startDate, endDate);
            return getFiltered(userMeals, filter);
        }
        return Collections.EMPTY_LIST;
    }

    private List<Meal> getFiltered(Map<Integer, Meal> userMeals, Predicate<Meal> filter) {
        Comparator<Meal> compareByDateTime
                = Comparator.comparing(Meal::getDateTime)
                .reversed();
        return userMeals.values().stream()
                .filter(filter)
                .sorted(compareByDateTime)
                .collect(Collectors.toList());
    }
}

