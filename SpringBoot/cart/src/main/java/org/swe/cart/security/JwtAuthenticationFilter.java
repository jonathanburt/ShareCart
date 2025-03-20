package org.swe.cart.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.swe.cart.services.CustomUserDetailsService;

import com.auth0.jwt.exceptions.JWTCreationException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        } //TODO not sure if returning here is ok

        String token = authHeader.substring(7);

        if(token == null || token.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT in Bearer Header");
            filterChain.doFilter(request, response);
            return;
        }
        
        if (jwtUtil.isTokenExpired(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT expired");
            filterChain.doFilter(request, response);
            return;
        }

        try{
            String username = jwtUtil.validateTokenAndRetrieveUsername(token);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
            if(SecurityContextHolder.getContext().getAuthentication() == null){
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (JWTCreationException e){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token");
        }

        filterChain.doFilter(request, response);

    }
}
