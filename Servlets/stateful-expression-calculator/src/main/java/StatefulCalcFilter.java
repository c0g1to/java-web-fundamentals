import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "HelpMePlease")

public class StatefulCalcFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

// IllegalStateException
        if (servletRequest instanceof HttpServletRequest) {
            String url = ((HttpServletRequest)servletRequest).getRequestURL().toString();
            String page = url.substring(url.lastIndexOf('/') + 1);
            if (!page.equals("result") && !page.equals("expression")) {
                servletRequest.getRequestDispatcher("/calc/variables?" + page).forward(servletRequest, servletResponse);
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
