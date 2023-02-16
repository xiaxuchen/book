package org.originit.analyze.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@WebFilter("/*")
@Component
public class LoginFilter implements Filter {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) servletRequest;

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            Cookie[] cookies = req.getCookies();
            if (cookies == null) {
                cookies = new Cookie[0];
            }
            final Optional<Cookie> token = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("token")).findFirst();
            if (token.isPresent()) {
                final String result = restTemplate.getForObject("http://auth.xxc.com:8080/auth/userInfo?token=" + token.get().getValue(), String.class);
                if (!Objects.equals(result, "err")) {
                    session = req.getSession();
                    session.setAttribute("userId",result);
                    filterChain.doFilter(req,servletResponse);
                    return;
                }
            }
            ((HttpServletResponse) servletResponse).sendRedirect("http://auth.xxc.com:8080/auth/login?redirectUrl=" +
                    URLEncoder.encode(req.getRequestURL().toString(),"utf8").replace("+", "%20"));

        }
    }
}
