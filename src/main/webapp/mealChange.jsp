<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>mealChange</title>
</head>
<body>
<form method="POST" action='meals'>

    <h2>${(typeAction == 'edit') ? 'Edit' : 'Add'} meal</h2>
    Calories :  <input type="text" name="calories" value="${meal.getCalories()}" /> <br />
    <input type="hidden" name="id" value="${meal.getID()}" /> <br />
    Description:  <input type="text" name="description" value="${meal.getDescription()}" /> <br />
    DateTime: <input type="datetime-local" name="date" value="${meal.getDateTime()}"/> <br />
    <input type="submit" name="submit" value="Save" />
    <input type="submit" name="submit" value="Cancel" />
</form>
</body>
</html>
