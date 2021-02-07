package ru.javawebinar.topjava.web;

import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.text.SimpleDateFormat;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static String INSERT_OR_EDIT = "/mealChange.jsp";
    private static String LIST_MEAL = "/meals.jsp";
    private MealDAO mealDAO;
    private DateTimeFormatter dateFormat;
    private LocalTime timeStart;
    private LocalTime timeEnd;

    public MealServlet() {
        mealDAO = new MealSetDAO();
        dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        timeStart = LocalTime.of(0,0, 0);
        timeEnd = LocalTime.of(23,59, 59);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward="";
        String formAction =  "";
        String action = request.getParameter("action");
        if (action == null)
        {
            forward = LIST_MEAL;
            List<MealTo> mealToList = mealDAO.getFilteredMeals(timeStart,timeEnd);
            request.setAttribute("listMeals",mealToList);
        }
        else if (action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            formAction = "edit";
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            request.setAttribute("meal", mealDAO.getbyID(mealId));
            request.setAttribute("typeAction",formAction);
        }
        else if (action.equalsIgnoreCase("delete")){
            forward = LIST_MEAL;
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            mealDAO.delete(mealDAO.getbyID(mealId));
            List<MealTo> mealToList = mealDAO.getFilteredMeals(timeStart,timeEnd);
            request.setAttribute("listMeals",mealToList);
        }
        else {
            formAction = "insert";
            forward = INSERT_OR_EDIT;
            request.setAttribute("typeAction",formAction);
        }

        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (request.getParameter("submit").equals("Save")) {

            String caloriesStr = request.getParameter("calories");
            int calories = (caloriesStr != null && caloriesStr.length() > 0) ? Integer.parseInt(request.getParameter("calories")) : 0;
            String idStr = request.getParameter("id");
            if (idStr == null || idStr.length() == 0) {
                mealDAO.add(LocalDateTime.parse(request.getParameter("date"), dateFormat),
                        request.getParameter("description"),
                        calories);
            } else {
                long id = Long.parseLong(idStr);
                mealDAO.edit(new Meal(id,
                        LocalDateTime.parse(request.getParameter("date"), dateFormat),
                        request.getParameter("description"),
                        calories));
            }
        }
        request.setAttribute("listMeals",
                            mealDAO.getFilteredMeals(timeStart,timeEnd));
        request.getRequestDispatcher(LIST_MEAL).forward(request, response);
    }
}
