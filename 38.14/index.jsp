<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Random" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Addition Quiz</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
        }
        h1 {
            color: #333;
        }
        .question {
            margin-bottom: 10px;
        }
        input[type=number] {
            width: 60px;
            padding: 5px;
        }
        input[type=submit] {
            margin-top: 20px;
            padding: 8px 16px;
        }
    </style>
</head>
<body>
    <h1>Addition Quiz</h1>
    
    <form action="result.jsp" method="post">
        <%
            Random random = new Random();
            for (int i = 0; i < 5; i++) {
                int number1 = random.nextInt(100);
                int number2 = random.nextInt(100);
                int correctAnswer = number1 + number2;
        %>
            <div class="question">
                <%= i + 1 %>. What is <%= number1 %> + <%= number2 %>? 
                <input type="number" name="answer<%= i %>" required>
                <input type="hidden" name="number1_<%= i %>" value="<%= number1 %>">
                <input type="hidden" name="number2_<%= i %>" value="<%= number2 %>">
                <input type="hidden" name="correctAnswer<%= i %>" value="<%= correctAnswer %>">
            </div>
        <%
            }
        %>
        <input type="submit" value="Submit">
    </form>
</body>
</html> 