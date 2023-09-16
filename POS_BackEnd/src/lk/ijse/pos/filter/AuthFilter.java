package lk.ijse.pos.filter;

import lk.ijse.pos.util.ResponseUtil;

import javax.json.JsonObject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
@WebFilter(urlPatterns = "/*", filterName = "AuthFilter")
*/
public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Auth init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, IOException {
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        HttpServletRequest req = (HttpServletRequest) servletRequest;

        String auth = req.getHeader("Auth");

        if (auth != null && auth.equals("user=admin,pass=admin")) {
            filterChain.doFilter(servletRequest, servletResponse);
            System.out.println("Auth doFil");
        } else {
            res.addHeader("Content-Type", "application/json");
            JsonObject authenticationFail = ResponseUtil.genJson("Auth-Error", "Authentication fail");
            res.getWriter().print(authenticationFail);

        }
    }

    @Override
    public void destroy() {

    }


}
