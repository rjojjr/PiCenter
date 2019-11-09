package com.kirchnersolutions.PiCenter.servers.rest;

import com.kirchnersolutions.PiCenter.entites.AppUser;
import com.kirchnersolutions.PiCenter.servers.services.UserService;
import com.kirchnersolutions.PiCenter.servers.beans.LogonForm;
import com.kirchnersolutions.PiCenter.servers.beans.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class MainController {

    @Autowired
    private UserService userService;

    @GetMapping("/loading")
    public RestResponse initClient(HttpServletResponse response) throws Exception{
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        //ServletWebRequest servletWebRequest=new ServletWebRequest(request);
        //HttpServletResponse response=servletWebRequest.getResponse();
        HttpSession httpSession = cookie(request, response);
        if(httpSession.getAttribute("username") == null){
            return new RestResponse();
        }
        return new RestResponse(userService.getRestUser((String)httpSession.getAttribute("username")));
    }

    @PostMapping("/login")
    public RestResponse home(HttpServletResponse response, @RequestBody LogonForm logonForm) throws Exception{
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        HttpSession httpSession = cookie(request, response);
        if(httpSession.getAttribute("username") == null){
            AppUser user = userService.logOn(logonForm.getUsername(), logonForm.getPassword(), request.getRemoteAddr());
            if(user != null){
                httpSession.setAttribute("username", user.getUserName());
                return new RestResponse(userService.getRestUser(user.getUserName()));
            }
            return new RestResponse("{ body: 'Wrong username or password' }");
        }
        return new RestResponse(userService.getRestUser((String)httpSession.getAttribute("username")));
    }

    private static HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true); // true == allow create
    }

    private static HttpSession cookie(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null && request.getParameter("JSESSIONID") != null) {
            Cookie userCookie = new Cookie("JSESSIONID", request.getParameter("JSESSIONID"));
            userCookie.setMaxAge(30 * 60 + 1);
            response.addCookie(userCookie);
            return session;
        } else if(session != null){
            String sessionId = session.getId();
            Cookie userCookie = new Cookie("JSESSIONID", sessionId);
            userCookie.setMaxAge(30 * 60 + 1);
            response.addCookie(userCookie);
            return session;
        }else{
            session = getSession();
            String sessionId = session.getId();
            Cookie userCookie = new Cookie("JSESSIONID", sessionId);
            userCookie.setMaxAge(30 * 60 + 1);
            response.addCookie(userCookie);
            return session;
        }
    }

}
