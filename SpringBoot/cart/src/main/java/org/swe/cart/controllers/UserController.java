package org.swe.cart.controllers;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swe.cart.entities.GlobalRole;
import org.swe.cart.entities.GroupInvite;
import org.swe.cart.entities.User;
import org.swe.cart.payload.AuthResponseDTO;
import org.swe.cart.payload.GroupInviteDTO2;
import org.swe.cart.payload.LoginDTO;
import org.swe.cart.payload.RegisterResponseDTO;
import org.swe.cart.payload.SignUpDTO;
import org.swe.cart.repositories.GroupInviteRepository;
import org.swe.cart.repositories.UserRepository;
import org.swe.cart.security.CustomUserDetails;
import org.swe.cart.security.JwtUtil;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Autowired
    private GroupInviteRepository groupInviteRepository;

    @PostMapping("/auth/signin")
    public ResponseEntity<AuthResponseDTO> authenticateUser(@RequestBody LoginDTO loginDTO) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(
            loginDTO.getUsername(), loginDTO.getPassword()));
        String token = jwtUtil.generateToken(loginDTO.getUsername());
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        System.out.println(user.getId());
        return new ResponseEntity<>(new AuthResponseDTO(token, user.getId(), user.getEmail(), formatInstantToHTTP(user.getCreatedAt())), HttpStatus.OK);
    }

    //TODO
    @PostMapping("/auth/signup")
    public ResponseEntity<RegisterResponseDTO> registerUser(@RequestBody SignUpDTO signUpDTO) {
        if(userRepository.existsByUsername(signUpDTO.getUsername())){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpDTO.getEmail())){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(signUpDTO.getUsername());
        user.setEmail(signUpDTO.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        user.setGlobalRole(GlobalRole.USER);

        user = userRepository.save(user);

        return new ResponseEntity<>(new RegisterResponseDTO(user.getId(), formatInstantToHTTP(user.getCreatedAt())), HttpStatus.OK);

    }

    @GetMapping("/users/{userId}/invites/get") //TODO
    public ResponseEntity<List<GroupInviteDTO2>> getInvites(@PathVariable Integer userId) {
        try {
            if(userId != ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); //Make sure that the provieded userId is the user that is making the request
            User user = userRepository.findById(userId).orElseThrow();
            List<GroupInvite> invites = groupInviteRepository.findAllByUser(user);
            List<GroupInviteDTO2> inviteDTO2s = invites.stream().map(invite -> new GroupInviteDTO2(userId, invite.getGroup().getName(), formatInstantToHTTP(invite.getCreated_at()))).collect(Collectors.toList());
            return ResponseEntity.ok(inviteDTO2s);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/users/{userId}/remove")
    public String deleteUser(@PathVariable Integer userId){ //Maybe change to void return type, also maybe require additional password validation?
        //TODO
        return "";
    }

    private String formatInstantToHTTP(Instant instant) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(Date.from(instant));
    }

}
