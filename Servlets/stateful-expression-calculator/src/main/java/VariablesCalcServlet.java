import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet(name = "Variables")

public class VariablesCalcServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

//        String variableName = getVariableNameFromRequest(req);
//        if (!variableName.equals("expression") && !variableName.equals("result")) {

        BufferedReader in = req.getReader();
        HttpSession session = req.getSession();
        String variableName = req.getParameterNames().nextElement();

        StringBuilder numberBuilder = new StringBuilder();
        int digit;
        while ((digit = in.read()) != -1) {
            numberBuilder.append((char) digit);
        }

        if (session.getAttribute(variableName) == null) {
            resp.setStatus(201);
        } else {
            resp.setStatus(200);
        }

        String number = numberBuilder.toString();
        if (Pattern.compile("[A-z]").matcher(number).find()
                || (Integer.parseInt(number) >= -10000 && Integer.parseInt(number) <= 10000)) {
            session.setAttribute(variableName, number.toString());
        } else {
            resp.setStatus(403);
        }

        in.close();
//        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String variableName = getVariableNameFromRequest(req);
        HttpSession session = req.getSession();
        if (!variableName.equals("expression") && !variableName.equals("result")) {
            if (session.getAttribute(variableName) != null) {
                session.setAttribute(variableName, null);
            }
            resp.setStatus(204);
        }
    }

    private String getVariableNameFromRequest(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        return url.substring(url.lastIndexOf('/') + 1);
    }
}