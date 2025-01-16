package com.rivandra.mrms_api.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class AuthController {
    
    @GetMapping("/")
    public String andra(@RequestParam String param) {
        return "MRSM API";
    }

    @PostMapping("auth/login")
    public String login(@RequestBody String entity) {
        return entity;
    }

    @GetMapping("/auth/start")
    public String andra2(@RequestParam String param) {
        return "auth start";
    }
    
}
