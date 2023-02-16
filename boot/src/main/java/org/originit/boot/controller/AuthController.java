package org.originit.boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/login")
    public String login(@RequestParam String redirectUrl, Model model) {
        // 登录成功后跳转地址
        model.addAttribute("redirect", redirectUrl);
        return "login";
    }

    @ResponseBody
    @GetMapping("/hello")
    public String hello(HttpServletRequest request) {
        System.out.println(request.isUserInRole("admin"));
        System.out.println(request.getClass());
        return "console.log('hello')";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                        @RequestParam String redirectUrl, RedirectAttributes redirectAttributes, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(request.isUserInRole("admin"));
        System.out.println(request.getClass());
        String authToken = "123123";
        if (username.equals("xxc") && password.equals("123456")) {
            Cookie cookie = new Cookie("token",authToken);
            cookie.setDomain("xxc.com");
            cookie.setPath("/");
            cookie.setMaxAge(36000);
            response.addCookie(cookie);
            redirectAttributes.addAttribute("token",authToken);
            return "redirect:" + redirectUrl;
        }
        model.addAttribute("error","用户名或密码错误");
        return "login";
    }

    @GetMapping("/userInfo")
    @ResponseBody
    public String getUserId(@RequestParam String token) {
        if (token.equals("123123")) {
            return "1";
        }
        return "err";
    }
}
