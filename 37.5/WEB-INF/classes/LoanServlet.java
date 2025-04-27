import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.text.NumberFormat;

public class LoanServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        try {
            double loanAmount = Double.parseDouble(request.getParameter("loanAmount"));
            double annualInterestRate = Double.parseDouble(request.getParameter("annualInterestRate"));
            int numberOfYears = Integer.parseInt(request.getParameter("numberOfYears"));
            
            Loan loan = new Loan(annualInterestRate, numberOfYears, loanAmount);
            
            double monthlyPayment = loan.getMonthlyPayment();
            double totalPayment = loan.getTotalPayment();
            
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
            String formattedMonthlyPayment = currencyFormat.format(monthlyPayment);
            String formattedTotalPayment = currencyFormat.format(totalPayment);
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Loan Payment Results</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
            out.println("h1 { color: #333; }");
            out.println("table { border-collapse: collapse; width: 400px; }");
            out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
            out.println("th { background-color: #f2f2f2; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Loan Payment Results</h1>");
            out.println("<table>");
            out.println("<tr><th>Loan Amount</th><td>" + currencyFormat.format(loanAmount) + "</td></tr>");
            out.println("<tr><th>Annual Interest Rate</th><td>" + annualInterestRate + "%</td></tr>");
            out.println("<tr><th>Number of Years</th><td>" + numberOfYears + "</td></tr>");
            out.println("<tr><th>Monthly Payment</th><td>" + formattedMonthlyPayment + "</td></tr>");
            out.println("<tr><th>Total Payment</th><td>" + formattedTotalPayment + "</td></tr>");
            out.println("</table>");
            out.println("<br><a href='index.html'>Back to Loan Calculator</a>");
            out.println("</body>");
            out.println("</html>");
        } catch (NumberFormatException e) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><title>Error</title></head>");
            out.println("<body>");
            out.println("<h2>Error: Invalid input</h2>");
            out.println("<p>Please enter valid numeric values for loan amount, interest rate, and number of years.</p>");
            out.println("<a href='index.html'>Back to Loan Calculator</a>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
} 