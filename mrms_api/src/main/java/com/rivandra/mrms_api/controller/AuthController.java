package com.rivandra.mrms_api.controller;

import org.springframework.web.bind.annotation.RestController;

import com.rivandra.mrms_api.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class AuthController {
    
    @Autowired 
    private AuthService service;

    @GetMapping("/")
    public String index(@RequestParam String param) {
        return "MRSM API";
    }

    @PostMapping("/auth/login")
    public String login(@RequestBody User user, HttpServletResponse response) {

        String generatedToken = service.login(user.getUserId(), user.getPassword());

        // Create a secure cookie for the JWT token
        Cookie jwtCookie = new Cookie("jwtToken", generatedToken);
        jwtCookie.setHttpOnly(true);    // Make it inaccessible to JavaScript
        jwtCookie.setSecure(true);      // Ensure it's sent only over HTTPS
        jwtCookie.setPath("/");         // Make it available to the entire app
        jwtCookie.setMaxAge(7 * 24 * 60 * 60); // Expire after 7 days

        // Add the cookie to the response
        response.addCookie(jwtCookie);

        return "successfuly logged in";
    }
    
}
