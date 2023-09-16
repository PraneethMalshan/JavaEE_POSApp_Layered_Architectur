package lk.ijse.pos.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
Meeka liyanne resource sharing waladi headers tikak hama thissema repeat wenawa. Anna eka wisada ganna thamayi mee lk.ijse.pos.filter eka hadanne.
*/

//@WebFilter(urlPatterns = "/*")
public class CORSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("CORS init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        filterChain.doFilter(servletRequest,servletResponse);
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        System.out.println("crossfilter");
        resp.setStatus(200);
        resp.addHeader("Access-Control-Allow-Origin","*");
        resp.addHeader("Access-Control-Allow-Methods","DELETE,PUT");
        resp.addHeader("Access-Control-Allow-Headers","Content-Type");
        resp.setContentType("application/json");



    }

    @Override
    public void destroy() {

    }
}
