package ru.fav.petcaregroomingsalon.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebFilter("/*")
public class LoggerFilter extends HttpFilter {
    Logger logger;

    @Override
    public void init() throws ServletException {
        super.init();

        logger = (Logger) getServletContext().getAttribute("logger");
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        logger.info(" Request: {} {}{}", req.getMethod(), req.getRequestURI(), req.getQueryString() != null ? "/" + req.getQueryString() : "");

        chain.doFilter(req, res);

        logger.info(" Response: {}", res.getStatus());
    }
}
