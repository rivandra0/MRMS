package com.rivandra.mrms_api.controller;

import org.springframework.web.bind.annotation.RestController;

import com.rivandra.mrms_api.service.AuthService;
import com.rivandra.mrms_api.service.JwtService;
import com.rivandra.mrms_api.service.PageAuthorizationService;

import org.springframework.http.ResponseCookie;

import jakarta.servlet.http.HttpServletResponse;
import model.CommonDTO;
import model.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpHeaders;


@RestController
public class AuthController {
    
    @Autowired 
    private AuthService _authService;

    @Autowired 
    private JwtService _jwtService;

    @Autowired 
    private PageAuthorizationService _pageAuthorizationService;

    @GetMapping("/")
    public String index(@RequestParam String param) {
        return "MRSM API";
    }

    @PostMapping("/auth/login")
    public CommonDTO login(@RequestBody User user, HttpServletResponse response) {

        try{
            User usr = _authService.login(user.getUserId(), user.getPassword());

            String generatedToken = _jwtService.generateToken(usr);

            List<String> authorizedPages = _pageAuthorizationService.getAuthorizedPagesByUser(usr.getRole());
            usr.setAuthorizedPages(String.join(",", authorizedPages));
            
            // Create a secure cookie for the JWT token
            ResponseCookie jwtCookie = ResponseCookie.from("jwtToken", generatedToken)
                .httpOnly(true)                 // Make it inaccessible to JavaScript
                // .secure(true)                  // Ensure it's sent only over HTTPS
                .path("/")                     // Make it available to the entire app
                .maxAge(7 * 24 * 60 * 60)      // Expire after 7 days
                .sameSite("None")              // Allow cross-origin cookies
                .build();

            // Add the cookie to the response headers
            response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

            CommonDTO res = new CommonDTO();
            res.setMessage(generatedToken);
            res.setData(usr);
            res.setStatus("Success");

            return res;
        }
        catch(Throwable ex) {
             throw ex;
        }
        
    }
    
}
