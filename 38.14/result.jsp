<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quiz Results</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
        }
        h1 {
            color: #333;
        }
        .result {
            margin-bottom: 10px;
        }
        .correct {
            color: green;
        }
        .incorrect {
            color: red;
        }
        .score {
            font-size: 1.2em;
            font-weight: bold;
            margin-top: 20px;
        }
        .restart {
            margin-top: 20px;
            padding: 8px 16px;
            background-color: white;
            color: black;
            border: 1px solid black;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
        }
    </style>
</head>
<body>
    <h1>Quiz Results</h1>
    
    <%
        int totalCorrect = 0;
        
        for (int i = 0; i < 5; i++) {
            int number1 = Integer.parseInt(request.getParameter("number1_" + i));
            int number2 = Integer.parseInt(request.getParameter("number2_" + i));
            int correctAnswer = Integer.parseInt(request.getParameter("correctAnswer" + i));
            
            String userAnswerParam = request.getParameter("answer" + i);
            int userAnswer = 0;
            
            try {
                userAnswer = Integer.parseInt(userAnswerParam);
            } catch (NumberFormatException e) {
            }
            
            boolean isCorrect = (userAnswer == correctAnswer);
            if (isCorrect) {
                totalCorrect++;
            }
    %>
        <div class="result <%= isCorrect ? "correct" : "incorrect" %>">
            <p><%= i + 1 %>. <%= number1 %> + <%= number2 %> = <%= userAnswer %></p>
            <% if (!isCorrect) { %>
                <p>The correct answer is <%= correctAnswer %></p>
            <% } %>
        </div>
    <%
        }
    %>
    
    <div class="score">
        Score: <%= totalCorrect %> out of 5
    </div>
    
    <a href="index.jsp" class="restart">Try Again</a>
</body>
</html> 