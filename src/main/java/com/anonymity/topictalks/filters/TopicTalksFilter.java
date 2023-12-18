package com.anonymity.topictalks.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * @author de140172 - author
 * @version 1.1 - version of software
 * - Package Name: com.anonymity.topictalks.filters
 * - Created At: 21-09-2023 10:54:03
 * @since 1.0 - version of class
 */

@Component
@WebFilter(urlPatterns = { "/*" }, filterName = "TopicTalksFilter")
public class TopicTalksFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(TopicTalksFilter.class);
    private final String REQUEST_ID = "request_id";
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("filter init ...");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, request.getHeader("Origin"));
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, GET, OPTIONS, DELETE,PUT");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,"Origin, X-Requested-With, Content-Type, Accept, Authorization");
        //Request unique id
        String uid =  UUID.randomUUID().toString();
        MDC.put(REQUEST_ID,uid);
        request.setAttribute(REQUEST_ID,uid);
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        logger.info("filter destroy ...");
    }

}
