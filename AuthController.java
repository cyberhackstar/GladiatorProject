package com.vehicleloanhub.controller;

import com.vehicleloanhub.model.User;
import com.vehicleloanhub.repository.UserRepository;
import com.vehicleloanhub.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(user.getUsername());

        Optional<User> loggedInUser = userRepository.findByUsername(user.getUsername());
        if (loggedInUser.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("username", loggedInUser.get().getUsername());
            response.put("role", loggedInUser.get().getRole());
            response.put("userId", loggedInUser.get().getId());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body("Invalid login attempt");
    }
}
