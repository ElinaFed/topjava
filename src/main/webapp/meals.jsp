<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
    <head>
        <title>Meal list</title>
    </head>
    <body>
        <h2>Meals</h2>
        <p><a href="meals?action=insert">Add Meal</a></p>
        <table border="1">
            <thead>
                <tr>
                    <th>Date</th>
                    <th>Description</th>
                    <th>Calories</th>
                    <th></th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${listMeals}" var="meal">
                    <tr style="color:${meal.isOver() ? 'red' : 'green'}">
                        <th>
                            <fmt:parseDate value="${meal.getDateTime()}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                            <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${parsedDateTime}" />
                        </th>
                        <th>${meal.getDescription()}</th>
                        <th>${meal.getCalories()}</th>
                        <td><a href="meals?action=edit&mealId=<c:out value="${meal.getID()}"/>">Update</a></td>
                        <td><a href="meals?action=delete&mealId=<c:out value="${meal.getID()}"/>">Delete</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>
