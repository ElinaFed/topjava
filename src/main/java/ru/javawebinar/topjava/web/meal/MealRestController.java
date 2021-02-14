package ru.javawebinar.topjava.web.meal;

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

    @Autowired
    private MealService service;

    // Отдать свою еду (для отображения в таблице, формат List<MealTo>), запрос БЕЗ параметров
    public List<MealTo> getAll() {
        List<Meal> mealList = service.getAll(authUserId());
        return MealsUtil.getTos(mealList, authUserCaloriesPerDay());
    }

    //  Отдать свою еду, отфильтрованную по startDate, startTime, endDate, endTime
    public List<MealTo> getFiltered(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        List<Meal> filteredMeals = service.getFiltered(authUserId(), startDate, endDate);
        return MealsUtil.getFilteredTos(filteredMeals
                , authUserCaloriesPerDay()
                , (startTime != null) ? startTime : LocalTime.MIN
                , (endTime != null) ? endTime.minusNanos(1) : LocalTime.MAX);
    }

    //Отдать свою еду по id, параметр запроса - id еды. Если еда с этим id чужая или отсутствует - NotFoundException
    public Meal get(int id) {
        return service.get(authUserId(), id);
    }

    //удалить свою еду по id, параметр запроса - id еды. Если еда с этим id чужая или отсутствует - NotFoundException
    public boolean delete(int id) {
        return service.delete(authUserId(), id);
    }

    //Сохранить/обновить еду, параметр запроса - Meal. Если обновляемая еда с этим id чужая или отсутствует - NotFoundException
    public Meal create(Meal meal) {
        checkNew(meal);
        return service.create(authUserId(), meal);
    }

    // В концепции REST при update дополнительно принято передавать id (см. AdminRestController.update)
    public Meal update(Meal meal, int id) {
        assureIdConsistent(meal, id);
        return service.update(authUserId(), meal);
    }
}