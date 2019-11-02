package com.kirchnersolutions.PiCenter.servers.rest;

import com.kirchnersolutions.PiCenter.servers.beans.LogonForm;
import com.kirchnersolutions.PiCenter.Wrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class HTTPController {

    @GetMapping("/")
    public String home(Model model, HttpServletResponse response) throws Exception{
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        //ServletWebRequest servletWebRequest=new ServletWebRequest(request);
        //HttpServletResponse response=servletWebRequest.getResponse();
        HttpSession httpSession = cookie(request, response);
        if(httpSession.getAttribute("username") == null){
            model.addAttribute("form", new LogonForm());
            return "logon";
        }
        /*
        Users users = Users.getInstance();
        List<List<Wrapper>> values = users.getTempList((String)httpSession.getAttribute("username"));
        model.addAttribute("lrlist", values.get(2));
        model.addAttribute("oflist", values.get(0));
        model.addAttribute("brlist", values.get(3));
        model.addAttribute("srlist", values.get(1));

         */
        return "home";
    }

    @PostMapping("/logon")
    public String logon(Model model, LogonForm form, HttpServletResponse response) throws Exception{
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        //ServletWebRequest servletWebRequest=new ServletWebRequest(request);
        //HttpServletResponse response=servletWebRequest.getResponse();
        HttpSession httpSession = cookie(request, response);
        /*
        Users users = Users.getInstance();
        if(httpSession.getAttribute("username") == null){
            if(users.logon(form.getUsername(), form.getPassword())){
                httpSession.setAttribute("username", form.getUsername());
                List<List<Wrapper>> values = users.getTempList((String)httpSession.getAttribute("username"));
                model.addAttribute("lrlist", values.get(2));
                model.addAttribute("oflist", values.get(0));
                model.addAttribute("brlist", values.get(3));
                model.addAttribute("srlist", values.get(1));
                return "home";
            }else{
                model.addAttribute("form", new LogonForm());
                return "logon";
            }
        }
        httpSession.setAttribute("username", form.getUsername());
        List<List<Wrapper>> values = users.getTempList((String)httpSession.getAttribute("username"));
        model.addAttribute("lrlist", values.get(2));
        model.addAttribute("oflist", values.get(0));
        model.addAttribute("brlist", values.get(3));
        model.addAttribute("srlist", values.get(1));

         */
        return "home";
    }

    @GetMapping("/logout")
    public String logout(Model model, HttpServletResponse response) throws Exception{
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        //ServletWebRequest servletWebRequest=new ServletWebRequest(request);
        //HttpServletResponse response=servletWebRequest.getResponse();
        /*
        Users users = Users.getInstance();
        HttpSession httpSession = cookie(request, response);
        if(httpSession.getAttribute("username") == null){
            model.addAttribute("form", new LogonForm());
            return "logon";
        }
        if(users.logout((String)httpSession.getAttribute("username"))){
            model.addAttribute("form", new LogonForm());
            httpSession.setAttribute("username", null);
            return "logon";
        }
        List<List<Wrapper>> values = users.getTempList((String)httpSession.getAttribute("username"));
        model.addAttribute("lrlist", values.get(2));
        model.addAttribute("oflist", values.get(0));
        model.addAttribute("brlist", values.get(3));
        model.addAttribute("srlist", values.get(1));

         */
        return "home";
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
