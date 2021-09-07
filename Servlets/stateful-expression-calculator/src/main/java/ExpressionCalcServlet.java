import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet(name = "Expression")

public class ExpressionCalcServlet extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        BufferedReader in = req.getReader();
        HttpSession session = req.getSession();

        StringBuilder expression = new StringBuilder();
        int symbol;
        while ((symbol = in.read()) != -1) {
            expression.append((char) symbol);
        }

        if (Pattern.compile("[A-z]{2,}").matcher(expression).find()) {
            resp.setStatus(400);
        }
        else {
            if (session.getAttribute("expression") == null) {
                resp.setStatus(201);
            } else {
                resp.setStatus(200);
            }
            session.setAttribute("expression", expression.toString());
        }

        in.close();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        if (session.getAttribute("expression") != null) {
            session.setAttribute("expression", null);
            resp.setStatus(204);
        }
    }
}