package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    Meal findByIdAndUserId(int id, int userId);

    List<Meal> findByUserIdOrderByIdDesc(int userId);

    List<Meal> findByUserIdAndDateTimeGreaterThanEqualAndDateTimeLessThanOrderByIdDesc(int userId,
                                                                                       LocalDateTime startDateTime,
                                                                                       LocalDateTime endDateTime);


    @Modifying
    @Transactional
    int deleteByIdAndUserId(int id, int userid);
}
