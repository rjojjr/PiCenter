package com.kirchnersolutions.PiCenter.servers.events;

import com.kirchnersolutions.PiCenter.dev.DebuggingService;
import com.kirchnersolutions.PiCenter.servers.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.http.*;

@DependsOn({"debuggingService", "userService"})
@Configuration
public class HttpSessionEventConfig {

    @Autowired
    private UserService userService;
    @Autowired
    private DebuggingService debuggingService;

    @Bean
    public HttpSessionListener httpSessionListener() {
        return new HttpSessionListener() {
            @Override
            public void sessionCreated(HttpSessionEvent se) {
                System.out.println("Session Created with session id+" + se.getSession().getId());
                se.getSession().setMaxInactiveInterval(30 * 60);
            }

            @Override
            public void sessionDestroyed(HttpSessionEvent se) {
                System.out.println("Session Destroyed, Session id:" + se.getSession().getId());
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Bean
    public HttpSessionAttributeListener httpSessionAttributeListener() {
        return new HttpSessionAttributeListener() {
            @Override
            public void attributeAdded(HttpSessionBindingEvent se) {
                System.out.println("Attribute added the following information");
                System.out.println("Attribute Name:" + se.getName());
                System.out.println("Attribute Old Value:" + se.getValue());
            }

            @Override
            public void attributeRemoved(HttpSessionBindingEvent se) {
                System.out.println("Attribute removed the following information");
                System.out.println("Attribute Name:" + se.getName());
                System.out.println("Attribute Old Value:" + se.getValue());
            }

            @Override
            public void attributeReplaced(HttpSessionBindingEvent se) {
                System.out.println("Attribute Replaced following information");
                System.out.println("Attribute Name:" + se.getName());
                System.out.println("Attribute Old Value:" + se.getValue());
            }
        };
    }

    private static HttpSession cookie(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        if (request.getParameter("JSESSIONID") != null) {
            Cookie userCookie = new Cookie("JSESSIONID", request.getParameter("JSESSIONID"));
            response.addCookie(userCookie);
            return session;
        } else {
            String sessionId = session.getId();
            Cookie userCookie = new Cookie("JSESSIONID", sessionId);
            response.addCookie(userCookie);
            return session;
        }
    }
}