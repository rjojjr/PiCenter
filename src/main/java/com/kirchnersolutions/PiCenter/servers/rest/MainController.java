package com.kirchnersolutions.PiCenter.servers.rest;

import com.kirchnersolutions.PiCenter.dev.DebuggingService;
import com.kirchnersolutions.PiCenter.entites.AppUser;
import com.kirchnersolutions.PiCenter.servers.beans.*;
import com.kirchnersolutions.PiCenter.services.CSVService;
import com.kirchnersolutions.PiCenter.services.StatService;
import com.kirchnersolutions.PiCenter.services.UserService;
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

    private UserService userService;
    private StatService statService;
    private DebuggingService debuggingService;
    private CSVService csvService;

    @Autowired
    public MainController(UserService userService, StatService statService, DebuggingService debuggingService, CSVService csvService) {
        this.userService = userService;
        this.statService = statService;
        this.debuggingService = debuggingService;
        this.csvService = csvService;
    }

    @GetMapping("/loading")
    public RestResponse initClient(HttpServletResponse response) throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        //ServletWebRequest servletWebRequest=new ServletWebRequest(request);
        //HttpServletResponse response=servletWebRequest.getResponse();
        HttpSession httpSession = cookie(request, response);
        if (httpSession.getAttribute("username") == null) {
            return new RestResponse();
        }
        return new RestResponse(userService.getRestUser((String) httpSession.getAttribute("username")));
    }

    @PostMapping("/login")
    public RestResponse login(HttpServletResponse response, @RequestBody LogonForm logonForm) throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        HttpSession httpSession = cookie(request, response);
        if (httpSession.getAttribute("username") == null) {
            AppUser user = userService.logOn(logonForm.getUsername(), logonForm.getPassword(), request.getRemoteAddr());
            if (user != null) {
                httpSession.setAttribute("username", user.getUserName());
                return new RestResponse(userService.getRestUser(user.getUserName()));
            }
            return new RestResponse("{ body: 'Wrong username or password' }");
        }
        return new RestResponse(userService.getRestUser((String) httpSession.getAttribute("username")));
    }

    @GetMapping("/logout")
    public RestResponse logoutClient(HttpServletResponse response) throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        //ServletWebRequest servletWebRequest=new ServletWebRequest(request);
        //HttpServletResponse response=servletWebRequest.getResponse();
        HttpSession httpSession = cookie(request, response);
        if (httpSession.getAttribute("username") == null) {
            return new RestResponse();
        }
        if (userService.logOff((String) httpSession.getAttribute("username"))) {
            debuggingService.trace("user logged off");
            httpSession.setAttribute("username", null);
            if (httpSession.getAttribute("username") == null) {
                debuggingService.trace("HTTP user is null ");
            }
            return new RestResponse("{body: logged off}");
        }
        debuggingService.trace("Failed to log off user " + (String) httpSession.getAttribute("username"));
        return new RestResponse("{body: log off failed}");
    }

    @GetMapping("/summary")
    public RestResponse showSummary(HttpServletResponse response, @RequestParam String userId) throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        HttpSession httpSession = cookie(request, response);
        if (httpSession.getAttribute("username") == null) {
            return new RestResponse();
        }
        if (userId == null || userId.toCharArray().length < 5) {
            userService.systemInvalidateUser((String) httpSession.getAttribute("username"), "unauthentic session");
            return new RestResponse("{body: 'error', error: 'invalid token'}");
        }
        if (!updateSession((String) httpSession.getAttribute("username"), userId, request.getRemoteAddr(), "/summary")) {
            return new RestResponse("{body: 'error', error: 'unauthentic session'}", new RestUser());
        }
        return new RestResponse("{body: 'success'}", userService.getRestUser((String) httpSession.getAttribute("username")), statService.getRoomSummaries(2));
    }

    @GetMapping("data/csv")
    public RestResponse getCSV(HttpServletResponse response, @RequestParam String userId, @RequestParam String table) throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        HttpSession httpSession = cookie(request, response);
        if (httpSession.getAttribute("username") == null) {
            return new RestResponse();
        }
        if (userId == null || userId.toCharArray().length < 5) {
            userService.systemInvalidateUser((String) httpSession.getAttribute("username"), "unauthentic session");
            return new RestResponse("{body: 'error', error: 'invalid token'}");
        }
        if (!updateSession((String) httpSession.getAttribute("username"), userId, request.getRemoteAddr(), "/data/csv")) {
            return new RestResponse("{body: 'error', error: 'unauthentic session'}", new RestUser());
        }
        if (!userService.isAdmin((String) httpSession.getAttribute("username"))) {
            return new RestResponse("{body: 'failed not authorized'}", userService.getRestUser((String) httpSession.getAttribute("username")));
        }
        if (table != null) {
            if (csvService.generateDownload(table, false)) {
                /*String url = "http://picenter.kirchnerbusinesssolutions.com/download/backup";
                response.setHeader("Location", url);
                response.setStatus(302);*/
                return new RestResponse("{body: 'success'}", userService.getRestUser((String) httpSession.getAttribute("username")));
            }
        }
        return new RestResponse("{body: 'failed to generate package'}", userService.getRestUser((String) httpSession.getAttribute("username")));
    }

    @GetMapping("data/backup")
    public RestResponse getCSV(HttpServletResponse response, @RequestParam String userId) throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        HttpSession httpSession = cookie(request, response);
        if (httpSession.getAttribute("username") == null) {
            return new RestResponse();
        }
        if (userId == null || userId.toCharArray().length < 5) {
            userService.systemInvalidateUser((String) httpSession.getAttribute("username"), "unauthentic session");
            return new RestResponse("{body: 'error', error: 'invalid token'}");
        }
        if (!updateSession((String) httpSession.getAttribute("username"), userId, request.getRemoteAddr(), "/data/backup")) {
            return new RestResponse("{body: 'error', error: 'unauthentic session'}", new RestUser());
        }
        if (!userService.isAdmin((String) httpSession.getAttribute("username"))) {
            return new RestResponse("{body: 'failed not authorized'}", userService.getRestUser((String) httpSession.getAttribute("username")));
        }
        if (csvService.generateManualBackup()) {
                /*String url = "http://picenter.kirchnerbusinesssolutions.com/download/backup";
                response.setHeader("Location", url);
                response.setStatus(302);*/
            return new RestResponse("{body: 'success'}", userService.getRestUser((String) httpSession.getAttribute("username")));
        }
        return new RestResponse("{body: 'failed to generate package'}", userService.getRestUser((String) httpSession.getAttribute("username")));
    }

    @PostMapping("/update")
    public RestResponse updateSession(HttpServletResponse response, @RequestParam String userId, @RequestBody SessionUpdate sessionUpdate) throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        HttpSession httpSession = cookie(request, response);
        if (httpSession.getAttribute("username") == null) {
            return new RestResponse();
        }
        if (userId == null || userId.toCharArray().length < 5) {
            userService.systemInvalidateUser((String) httpSession.getAttribute("username"), "unauthentic session");
            return new RestResponse("{body: 'error', error: 'invalid token'}");
        }
        if (!updateSession((String) httpSession.getAttribute("username"), userId, request.getRemoteAddr(), sessionUpdate.getPage())) {
            return new RestResponse("{body: 'error', error: 'unauthentic session'}", new RestUser());
        }
        return new RestResponse("{body: 'success updating session'}", userService.getRestUser((String) httpSession.getAttribute("username")));
    }

    @PostMapping("/users/create")
    public RestResponse creatUser(HttpServletResponse response, @RequestParam String token, @RequestBody CreateUser createUser) throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        HttpSession httpSession = cookie(request, response);
        if (httpSession.getAttribute("username") == null) {
            return new RestResponse();
        }
        if (token == null) {
            userService.systemInvalidateUser((String) httpSession.getAttribute("username"), "unauthentic session");
            return new RestResponse("{body: 'error', error: 'invalid token'}");
        }
        if (!updateSession((String) httpSession.getAttribute("username"), token, request.getRemoteAddr(), "/users")) {
            return new RestResponse("{body: 'error', error: 'unauthentic session'}", new RestUser());
        }
        if (userService.createUser((String) httpSession.getAttribute("username"), createUser)) {
            return new RestResponse("{body: 'success'}", userService.getRestUser((String) httpSession.getAttribute("username")));
        }
        return new RestResponse("{body: 'error', error; 'failed to create user'}", userService.getRestUser((String) httpSession.getAttribute("username")));
    }

    private boolean updateSession(String username, String token, String ip, String page) throws Exception {
        debuggingService.trace("Update username: " + username + " token: " + token + " ip: " + ip + " page: " + page);
        if (userService.updateSession(username, token, ip, page) == null) {
            userService.systemInvalidateUser(username, "unauthentic session");
            return false;
        }
        return true;
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
        } else if (session != null) {
            String sessionId = session.getId();
            Cookie userCookie = new Cookie("JSESSIONID", sessionId);
            userCookie.setMaxAge(30 * 60 + 1);
            response.addCookie(userCookie);
            return session;
        } else {
            session = getSession();
            String sessionId = session.getId();
            Cookie userCookie = new Cookie("JSESSIONID", sessionId);
            userCookie.setMaxAge(30 * 60 + 1);
            response.addCookie(userCookie);
            return session;
        }
    }

}
