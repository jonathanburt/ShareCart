package org.swe.cart.configs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.swe.cart.security.JwtAuthenticationFilter;
import org.swe.cart.services.CustomUserDetailsService;


@Configuration
@EnableMethodSecurity
@EnableWebSecurity(debug = true)
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    /**
     * @author Jonah Lorenzo jbl113@case.edu
     * @return returns the CustomUserDetailsService Bean used for authentication
     */
    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService();  // Use CustomUserDetailsService instead of default
    }

    /**
     * @author Jonah Lorenzo jbl113@case.edu
     * @return returns the password encoder used to store passwords in DB using BCrypt
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * @author Jonah Lorenzo jbl113@case.edu
     * @param userDetailsService User Details Service used to create the DaoAuthenticationProvider
     * @param passwordEncoder Password Encoder used to create the DaoAuthenticationProvider
     * @return returns the Auth manager used for user authentication
     */
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(List.of(authProvider));  // Use Spring Security's authentication provider
    }

    /**
     * @author Jonah Lorenzo jbl113@case.edu
     * @param http The HttpSecurity Object that is configured
     * @return The SecurityFilterChain object built form http
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/users/**").authenticated()
                .requestMatchers("/api/group/**").authenticated()
                .anyRequest().authenticated())
            .userDetailsService(customUserDetailsService())
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
