package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealCollectionDao;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static String INSERT_OR_EDIT = "/mealChange.jsp";
    private static String LIST_MEAL = "/meals.jsp";
    private static final int MAX_CALORIES_DAY = 2000;
    private MealDao mealDAO;
    private DateTimeFormatter dateFormat;
    private LocalTime timeStart;
    private LocalTime timeEnd;


    public void init() throws ServletException {
        mealDAO = new MealCollectionDao();
        dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        timeStart = LocalTime.MIN;
        timeEnd = LocalTime.MAX;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward = "";
        String formAction = "";
        String action = request.getParameter("action");
        if (action == null) {

            forward = LIST_MEAL;
            request.setAttribute("listMeals", MealsUtil.filteredByStreams(mealDAO.getList(), timeStart, timeEnd, MAX_CALORIES_DAY));

        } else if (action.equalsIgnoreCase("edit")) {

            forward = INSERT_OR_EDIT;
            formAction = "edit";
            long mealId = Long.parseLong(request.getParameter("mealId"));
            request.setAttribute("meal", mealDAO.getByID(mealId));
            request.setAttribute("typeAction", formAction);

        } else if (action.equalsIgnoreCase("delete")) {

            long mealId = Long.parseLong(request.getParameter("mealId"));
            mealDAO.delete(mealId);
            response.sendRedirect("meals");
            return;

        } else {

            formAction = "insert";
            forward = INSERT_OR_EDIT;
            request.setAttribute("typeAction", formAction);

        }
        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (request.getParameter("submit").equals("Save")) {

            String caloriesStr = request.getParameter("calories");
            int calories = (caloriesStr != null && !caloriesStr.isEmpty()) ? Integer.parseInt(request.getParameter("calories")) : 0;
            String idStr = request.getParameter("id");
            if (idStr == null || idStr.isEmpty()) {
                mealDAO.add(new Meal(
                        LocalDateTime.parse(request.getParameter("date"), dateFormat),
                        request.getParameter("description"),
                        calories));
            } else {
                long id = Long.parseLong(idStr);
                mealDAO.edit(new Meal(id,
                        LocalDateTime.parse(request.getParameter("date"), dateFormat),
                        request.getParameter("description"),
                        calories));
            }
        }
        response.sendRedirect("meals");
    }
}
