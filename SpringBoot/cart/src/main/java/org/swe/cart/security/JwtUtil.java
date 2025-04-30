package org.swe.cart.security;

import java.util.Date;

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

    /**
     * Creates a new JWT used for authentication.
     * @param username The username to be included in the JWT.
     * @return A String JWT containing the username in the User Details subject, an issue date, an expiry date, an issuer, and a server signature.
     * @throws IllegalArgumentException
     * @throws JWTCreationException
     */
    public String generateToken(String username) throws IllegalArgumentException, JWTCreationException{
        return JWT.create()
                .withSubject("User Details")
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours validity
                .withIssuer(ISSUER)
                .sign(Algorithm.HMAC256(SECRET_KEY)); //TODO Change encryption algorithm to something more secure
    }

    /**
     * Verifies the expiration time of the JWT.
     * @param token The JWT to be verified, as a String.
     * @return A boolean indicating true if the expiration time on the token has passed.
     */
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

    /**
     * Validates a JWT and finds the username.
     * @param token The String JWT to be verified.
     * @return A String username extracted from the JWT if verification succeeds.
     * @throws JWTVerificationException
     */
    public String validateTokenAndRetrieveUsername(String token) throws JWTVerificationException{
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .withSubject("User Details")
                .withIssuer(ISSUER)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }
}
