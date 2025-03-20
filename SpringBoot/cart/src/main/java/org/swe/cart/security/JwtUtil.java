package org.swe.cart.security;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "your-secret-key"; // Use environment variables in production
    private final String ISSUER = "ShareCartApplicationServer";
    //TODO Redo all of this because it is shit and doesnt work at all
    public String generateToken(String username) throws IllegalArgumentException, JWTCreationException{
        return JWT.create()
                .withSubject("User Details")
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours validity
                .withIssuer(ISSUER)
                .sign(Algorithm.HMAC256(SECRET_KEY)); //TODO Change encryption algorithm to something more secure
    }

    public boolean isTokenExpired(String token) {
        try {
            Date expiration = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                    .build()
                    .verify(token)
                    .getExpiresAt();
            return expiration.before(new Date()); // Returns true if token is expired
        } catch (Exception e) {
            return true; // Treat invalid token as expired
        }
    }

    public String validateTokenAndRetrieveUsername(String token) throws JWTVerificationException{
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .withSubject("User Details")
                .withIssuer(ISSUER)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }
}
