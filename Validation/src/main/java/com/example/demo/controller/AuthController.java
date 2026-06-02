package com.example.demo.controller;

import com.example.demo.security.AuthRequest;
import com.example.demo.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login(
            @RequestBody AuthRequest request
    ) {

        if (
                request.getUsername().equals("admin")
                        &&
                request.getPassword().equals("admin123")
        ) {

            return jwtUtil.generateToken(
                    request.getUsername()
            );
        }

        return "Invalid Username or Password";
    }
}
