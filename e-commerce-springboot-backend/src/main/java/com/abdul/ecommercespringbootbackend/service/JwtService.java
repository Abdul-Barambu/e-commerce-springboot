package com.abdul.ecommercespringbootbackend.service;

import com.abdul.ecommercespringbootbackend.model.LocalUser;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.algorithm.key}")
    private String algorithmKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiryInSeconds}")
    private int expiryInSeconds;

    private Algorithm algorithm;

    public static final String USERNAME_KEY = "USERNAME";

    @PostConstruct
    public void postConstruct(){
        algorithm = Algorithm.HMAC256(algorithmKey);
    }

    public String generateJwt(LocalUser user){
        try {
            String jwtToken = JWT.create()
                    .withClaim(USERNAME_KEY, user.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + (1000 * expiryInSeconds)))
                    .withIssuer(issuer)
                    .sign(algorithm);
            return jwtToken;
        } catch (JWTVerificationException exception){
            // Log the exception for debugging
            exception.printStackTrace();
            return null;
        }
    }
}
