package ru.job4j.hibernate.myitems.filter;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter (urlPatterns = "/process")
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest sreq, ServletResponse sresp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) sreq;
        HttpServletResponse resp = (HttpServletResponse) sresp;
        if (req.getSession().getAttribute("user") == null) {
            resp.setStatus(401);
            return;
        }
        chain.doFilter(sreq, sresp);
    }
}
