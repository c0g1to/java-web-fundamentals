import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "Result")

public class ResultCalcServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter out = resp.getWriter();
        HttpSession session = req.getSession();

        if (session.getAttribute("expression") != null) {
            String expression = session.getAttribute("expression").toString().replaceAll(" ", "");
            String replacedExpression = replaceVariables(expression, session);
            if (!replacedExpression.contains("~")) {
                out.print(calcLoop(replacedExpression, false));
                resp.setStatus(200);
            }
            else {
                resp.setStatus(409);
            }
        }
        else {
            resp.setStatus(409);
        }

        out.flush();
        out.close();
    }

    private String replaceVariables(String exp, HttpSession ses) {
        Matcher matcher = Pattern.compile("[A-z]").matcher(exp);
        while (matcher.find()) {
            if (ses.getAttribute(matcher.group()) != null) {
                exp = matcher.replaceFirst(ses.getAttribute(matcher.group()).toString());
            }
            else {
                exp = matcher.replaceFirst("~");
            }
            matcher = Pattern.compile("[A-z]").matcher(exp);
        }
        return exp;
    }

    private String calcLoop(String exp, boolean simpleExp) {
        if (simpleExp) {
            Matcher matcher = Pattern.compile("\\d[\\*\\/\\+\\-]").matcher(exp);
            matcher.find();
            int leftValue = Integer.parseInt(exp.substring(0, matcher.start() + 1));
            int rightValue = Integer.parseInt(exp.substring(matcher.start() + 2));
            int result = 0;
            switch (exp.charAt(matcher.start() + 1)) {
                case '*':
                    result = leftValue * rightValue;
                    break;

                case '/':
                    result = leftValue / rightValue;
                    break;

                case '+':
                    result = leftValue + rightValue;
                    break;

                case '-':
                    result = leftValue - rightValue;
                    break;
            }
            return String.valueOf(result);
        } else {
            Matcher matcher = Pattern.compile("[\\(\\)]").matcher(exp);
            while (matcher.find()) {
                int opBr = matcher.start();
                int enBr = calcEndingBracket(matcher);
                exp = exp.substring(0, opBr) + calcLoop(exp.substring(opBr + 1, enBr), false) + exp.substring(enBr + 1);
                matcher = Pattern.compile("[\\(\\)]").matcher(exp);
            }

            matcher = Pattern.compile("\\-?\\d+\\s*[\\*\\/]\\s*\\-?\\d+").matcher(exp);
            while (matcher.find()) {
                exp = matcher.replaceFirst(calcLoop(matcher.group(), true));
                matcher = Pattern.compile("\\-?\\d+\\s*[\\*\\/]\\s*\\-?\\d+").matcher(exp);
            }

            matcher = Pattern.compile("\\-?\\d+\\s*[\\+\\-]\\s*\\-?\\d+").matcher(exp);
            while (matcher.find()) {
                exp = matcher.replaceFirst(calcLoop(matcher.group(), true));
                matcher = Pattern.compile("\\-?\\d+\\s*[\\+\\-]\\s*\\-?\\d+").matcher(exp);
            }
        }
        return exp;
    }

    private int calcEndingBracket(Matcher matcher) {
        int bracketFlag = 1;
        while (bracketFlag != 0) {
            matcher.find();
            if (matcher.group().equals("(")) {
                ++bracketFlag;
            } else {
                --bracketFlag;
            }
        }
        return matcher.start();
    }

}