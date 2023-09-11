package com.capstone.shri.controller.interceptor;

import com.capstone.shri.entity.LoginTicket;
import com.capstone.shri.entity.User;
import com.capstone.shri.service.UserService;
import com.capstone.shri.util.CookieUtil;
import com.capstone.shri.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = CookieUtil.getCookieValue(request, "ticket");

        // check whether the status of the login ticket is login, logout or expired
        if (ticket != null) {
            LoginTicket loginTicket = userService.getLoginTicket(ticket);
            if (loginTicket != null && loginTicket.getLoginStatus() == 0 && loginTicket.getExpired().after(new Date(System.currentTimeMillis()))) {
                User user = userService.getUserById(loginTicket.getUserId());
                hostHolder.setUser(user);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.remove();
    }
}