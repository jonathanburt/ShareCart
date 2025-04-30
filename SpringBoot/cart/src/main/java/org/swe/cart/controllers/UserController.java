package org.swe.cart.controllers;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;
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

    /**
     * This is the primary authentication layer, it generates Authentication tokens for later use by the client
     * @author Jonah Lorenzo jbl113@case.edu
     * @param loginDTO A data transfer object that contains login information for the user: a String username and a String password
     * @return A formatted HTTP response containing the JWT authentication token and some user details: the user ID, email and join timestamp
     */
    @PostMapping("/auth/signin")
    public ResponseEntity<AuthResponseDTO> authenticateUser(@RequestBody LoginDTO loginDTO) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(
            loginDTO.getUsername(), loginDTO.getPassword()));
        String token = jwtUtil.generateToken(loginDTO.getUsername());
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        return new ResponseEntity<>(new AuthResponseDTO(token, user.getId(), user.getEmail(), formatInstantToHTTP(user.getCreatedAt())), HttpStatus.OK);
    }

    /**
     * This is where user account creation takes place. This will recieve new user information and create a new DB entry.
     * @author Jonah Lorenzo jbl113@case.edu
     * @param signUpDTO A data transfer object containing a String username, String email, and String password. The email and username must be unique.
     * @return A formatted HTTP response containing either the new user ID and creation timestamp, or nothing if registration fails
     */
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

    /**
     * This is how the requester gets a list of all groups they have been invited to
     * @author Jonah Lorenzo jbl113@case.edu
     * @param userId The ID corresponding to the user logged in on the client device. This is provided in the request URL.
     * @return A formatted HTTP response containing list of data transfer objects with detail of a GroupInvite: an Integer group ID, String group name, and creation timestamp.
     */
    @GetMapping("/users/{userId}/invites/get")
    public ResponseEntity<List<GroupInviteDTO2>> getInvites(@PathVariable Integer userId) {
        try {
            Optional<User> optUser = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if(optUser.isEmpty()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); //Make sure that the provieded userId is the user that is making the request
            if(optUser.get().getId() != userId) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            User user = optUser.get();
            List<GroupInvite> invites = groupInviteRepository.findAllByUser(user);
            List<GroupInviteDTO2> inviteDTO2s = invites.stream().map(invite -> new GroupInviteDTO2(invite.getGroup().getId(), invite.getGroup().getName(), formatInstantToHTTP(invite.getCreated_at()))).collect(Collectors.toList());
            return ResponseEntity.ok(inviteDTO2s);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * This method deletes a User object from the DB, and all corresponding GroupMember, GroupInvite, and ListItem entries.
     * @author Jonah Lorenzo jbl113@case.edu
     * @param userId The ID of the user to be removed, must match the user ID of the requester
     * @return NOT FUNCTIONAL
     */
    @DeleteMapping("/users/{userId}/remove")
    public String deleteUser(@PathVariable Integer userId){ //Maybe change to void return type, also maybe require additional password validation?
        //TODO
        return "";
    }

    /**
     * Converts a Java Instant object to a String formatted according to ISO 8601 standards
     * @author Jonah Lorenzo jbl113@case.edu
     * @param instant The Instant object to be converted
     * @return A String object with the information of instant in ISO 8601 format in GMT
     */
    private String formatInstantToHTTP(Instant instant) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(Date.from(instant));
    }

}
