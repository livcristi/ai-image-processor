package org.ubb.image_handler_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ubb.image_handler_service.dto.auth.AuthResponse;
import org.ubb.image_handler_service.dto.auth.LoginRequest;
import org.ubb.image_handler_service.dto.auth.RegisterRequest;
import org.ubb.image_handler_service.model.UserEntity;
import org.ubb.image_handler_service.service.security.JwtService;
import org.ubb.image_handler_service.service.security.UserService;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController
{
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(JwtService jwtService, UserService userService)
    {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request)
    {
        userService.registerUser(request.username(), request.password());
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request)
    {
        userService.authenticateUser(request);
        UserEntity userEntity = userService.getUserByUsername(request.username());
        String token = jwtService.generateToken(userEntity);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
