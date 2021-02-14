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

            Map<Integer, Meal> mealsUser = repository.get(userId);
            if (mealsUser != null) {
                mealsUser.putIfAbsent(meal.getId(), meal);
            } else {
                mealsUser = new ConcurrentHashMap<>();
                mealsUser.putIfAbsent(meal.getId(), meal);
                repository.putIfAbsent(userId, mealsUser);
            }
            return meal;
        }
        // handle case: update, but not present in storage
        Map<Integer, Meal> mealsUser = repository.get(userId);
        if (mealsUser != null && mealsUser.containsKey(meal.getId())) {
            mealsUser.replace(meal.getId(), meal);
            return meal;
        }
        return null;
    }

    @Override
    public boolean delete(int userId, int id) {
        log.info("delete {}", userId + " idMeal = " + id);
        Map<Integer, Meal> mealsUser = repository.get(userId);
        return (mealsUser != null) ? (mealsUser.remove(id) != null) : false;
    }

    @Override
    public Meal get(int userId, int id) {
        log.info("get {}", userId + " idMeal = " + id);
        Map<Integer, Meal> mealsUser = repository.get(userId);
        return (mealsUser != null) ? mealsUser.get(id) : null;
    }

    @Override
    public List getAll(int userId) {
        log.info("getAll {}", userId);
        Map<Integer, Meal> mealsUser = repository.get(userId);
        if (mealsUser != null) {
            return getFiltered(mealsUser, ml -> true);
        }
        return Collections.EMPTY_LIST;
    }

    public List<Meal> getFiltered(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getFiltered {}", userId
                + " startDate = " + startDate
                + " endDate = " + endDate);
        Map<Integer, Meal> mealsUser = repository.get(userId);
        if (mealsUser != null) {
            final LocalDate start = (startDate == null) ? LocalDate.MIN : startDate;
            final LocalDate end = (endDate == null) ? LocalDate.MAX : endDate;
            Predicate<Meal> filter = ml -> DateTimeUtil.isBetweenHalfOpen(ml.getDate(), start, end);
            return getFiltered(mealsUser, filter);
        }
        return Collections.EMPTY_LIST;
    }

    private List<Meal> getFiltered(Map<Integer, Meal> mealsUser, Predicate<Meal> filter) {
        Comparator<Meal> compareByDateTime
                = Comparator.comparing(Meal::getDateTime)
                .reversed();
        return mealsUser.values().stream()
                .filter(filter)
                .sorted(compareByDateTime)
                .collect(Collectors.toList());
    }
}

