package com.real_property_system_api.real_property_system.services;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.real_property_system_api.real_property_system.CredentialsStorage;
import com.real_property_system_api.real_property_system.models.User;
import com.real_property_system_api.real_property_system.responses.JwtError;


@Service
public class JwtManager 
{

    @Autowired
    private CredentialsStorage credentialsStorage;

    private final String issuer = "api_server";

    public String generateAccessToken(User user, Long Minutes)
    {
        Algorithm algorithm = Algorithm.HMAC256(credentialsStorage.getSecret());
        String token = JWT.create()
            .withIssuer(issuer)
            .withClaim("login", user.getLogin())
            .withClaim("password", user.getPassword())
            .withClaim("role", user.getUserRole().getSuffix())
            .withExpiresAt(Instant.now().plusSeconds(Minutes * 60l))
            .withIssuedAt(Instant.now())
            .sign(algorithm);
        
        return token;
    }

    public String generateRefreshToken(Long Minutes)
    {
        Algorithm algorithm = Algorithm.HMAC256(credentialsStorage.getSecret());
        String token = JWT.create()
            .withIssuer(issuer)
            .withExpiresAt(Instant.now().plusSeconds(Minutes * 60l))
            .withIssuedAt(Instant.now())
            .sign(algorithm);
        
        return token;
    }

    public JwtError checkForValidity(String accessToken)
    {
        JwtError error = JwtError.NoError;
        try 
        {
            Algorithm algorithm = Algorithm.HMAC256(credentialsStorage.getSecret());
            JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
            
            DecodedJWT decodedJWT = JWT.decode(accessToken);
            verifier.verify(decodedJWT);
        } 
        
        catch (TokenExpiredException ex)
        {
            error = JwtError.Expired;
        }

        catch (JWTVerificationException exception) 
        {
            error = JwtError.InvalidSignature;
        } 
        

        return error;
    }

    public String getRoleSuffix(String token)
    {
        return JWT.decode(token).getClaim("role").asString();
    }
    
    public static String getLoginFromToken(DecodedJWT jwt)
    {
        return jwt.getClaim("login").asString();
    }

    public static String getPasswordFromToken(DecodedJWT jwt)
    {
        return jwt.getClaim("password").asString();
    }

    public static DecodedJWT getDecodedJwt(String token)
    {
        return JWT.decode(token);
    }

}
