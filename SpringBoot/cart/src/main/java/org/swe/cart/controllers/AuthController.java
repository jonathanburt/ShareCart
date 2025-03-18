package org.swe.cart.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swe.cart.entities.User;
import org.swe.cart.payload.AuthResponseDTO;
import org.swe.cart.payload.LoginDTO;
import org.swe.cart.payload.SignUpDTO;
import org.swe.cart.repositories.UserRepository;
import org.swe.cart.security.JwtUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signin")
    public ResponseEntity<AuthResponseDTO> authenticateUser(@RequestBody LoginDTO loginDTO) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(
            loginDTO.getUsernameOrEmail(), loginDTO.getPassword()));
        String token = jwtUtil.generateToken(loginDTO.getUsernameOrEmail());
        return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);   
    }

    //TODO
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDTO signUpDTO) {
        if(userRepository.existsByUsername(signUpDTO.getUsername())){
            return new ResponseEntity<>("Username is already in use", HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpDTO.getEmail())){
            return new ResponseEntity<>("An account is already tied to this email address", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(signUpDTO.getUsername());
        user.setEmail(signUpDTO.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));

        userRepository.save(user);

        return new ResponseEntity<>("User registered succesfully!", HttpStatus.OK);
        
    }
    
    

}
