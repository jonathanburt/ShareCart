package org.swe.cart.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.swe.cart.entities.User;
import org.swe.cart.payload.AuthResponseDTO;
import org.swe.cart.payload.LoginDTO;
import org.swe.cart.payload.SignUpDTO;
import org.swe.cart.repositories.UserRepository;
import org.swe.cart.security.CustomUserDetails;
import org.swe.cart.security.JwtUtil;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class UserController {
    //TODO Move this to CustomUserDetailsService
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/auth/signin")
    public ResponseEntity<AuthResponseDTO> authenticateUser(@RequestBody LoginDTO loginDTO) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(
            loginDTO.getUsername(), loginDTO.getPassword()));
        String token = jwtUtil.generateToken(loginDTO.getUsername());
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        System.out.println(user.getId());
        return new ResponseEntity<>(new AuthResponseDTO(token, user.getId()), HttpStatus.OK);   
    }

    //TODO
    @PostMapping("/auth/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignUpDTO signUpDTO) {
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

    @GetMapping("/users/invites/get") //TODO
    public String getInvites(@RequestParam String param) {
        return new String();
    }

    @DeleteMapping("/users/remove")
    public String deleteUser(){ //Maybe change to void return type, also maybe require password validation?
        //TODO
        return "";
    }

}
