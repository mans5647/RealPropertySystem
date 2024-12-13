package com.real_property_system_api.real_property_system;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.gson.Gson;
import com.real_property_system_api.real_property_system.responses.AuthResponse;
import com.real_property_system_api.real_property_system.responses.Codes;
import com.real_property_system_api.real_property_system.responses.JwtError;
import com.real_property_system_api.real_property_system.services.JwtManager;

import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



public class JwtFilter extends OncePerRequestFilter {

    
    private JwtManager jwtManager;

    private AuthenticationManager authenticationManager;

    final Gson gson = new Gson();


    public JwtFilter(JwtManager jwtManager, AuthenticationManager authenticationManager) {
        this.jwtManager = jwtManager;
        this.authenticationManager = authenticationManager;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException 
    {
        String path = request.getServletPath();
        if (path.startsWith("/api/public") || path.startsWith("/api/auth"))
        {
            filterChain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = request.getHeader("Authorization");
        AuthResponse authResponse = new AuthResponse();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer "))
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            authResponse.authCode = Codes.AuthErrorNoHeader;
            authResponse.message = "Provide \"Authorization\" header or check if it has \"Bearer\" type";

            response.getOutputStream().println(gson.toJson(authResponse));
            return;
        }

        final String token = authorizationHeader.substring(authorizationHeader.indexOf(' ') + 1);
        var validation_error = jwtManager.checkForValidity(token);

        String authMessageExpl = null;
        Integer authCode = Codes.NoError;
        switch (validation_error)
        {
            case JwtError.Expired:
                authCode = Codes.AuthTokenExpired;
                authMessageExpl = "Token expired";
                break;
            case JwtError.InvalidSignature:
                authCode = Codes.AuthTokenInvalid;
                authMessageExpl = "Token invalid signature";
                break;
            default: break;
        }

        authResponse.authCode = authCode;
        authResponse.message = authMessageExpl;

        if (authCode == Codes.NoError)
        {
            boolean hasLoginError = true;
            try 
            {

                var decodedJWT = JwtManager.getDecodedJwt(token);

                String username = JwtManager.getLoginFromToken(decodedJWT);
                String password = JwtManager.getPasswordFromToken(decodedJWT);

                
                var authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);

                var m_AuthResponse = authenticationManager.authenticate(authRequest);

                SecurityContextHolder.getContext().setAuthentication(m_AuthResponse);
                hasLoginError = false;
                filterChain.doFilter(request, response);
            }

            catch (UsernameNotFoundException ex)
            {
                authResponse.message = "Пользователь не найден";
                authResponse.authCode = Codes.AuthLoginNotFound;
            }

            catch (BadCredentialsException ex)
            {
                authResponse.message = "Неправильный пароль";
                authResponse.authCode = Codes.AuthPasswordIncorrect;
            }

            if (hasLoginError)
            {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getOutputStream().println(gson.toJson(authResponse));
            }

        }
        else 
        {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().print(gson.toJson(authResponse));
        }

    }

    
    
}
