package com.real_property_system_api.real_property_system;

import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import com.real_property_system_api.real_property_system.responses.AuthResponse;
import com.real_property_system_api.real_property_system.responses.Codes;
import com.real_property_system_api.real_property_system.responses.JwtError;
import com.real_property_system_api.real_property_system.services.JsonConverter;
import com.real_property_system_api.real_property_system.services.JwtManager;
import com.real_property_system_api.real_property_system.services.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



public class JwtFilter extends OncePerRequestFilter {

    
    private JwtManager jwtManager;

    private AuthenticationManager authenticationManager;


    private UserService userService;

    public JwtFilter(JwtManager jwtManager, AuthenticationManager authenticationManager, UserService userService) {
        this.jwtManager = jwtManager;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException 
    {
        String path = request.getServletPath();
        
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        if (path.startsWith("/api/public") || path.startsWith("/api/auth")
            || path.startsWith("/actuator"))
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
            authResponse.message = "Provide 'Authorization' header or check if it has 'Bearer' type";

            var convertResult = JsonConverter.doSaveConvert(authResponse);
            response.getWriter().println(convertResult.getSecond());
            return;
        }

        final String token = authorizationHeader.substring(authorizationHeader.indexOf(' ') + 1);
        var validationError = jwtManager.checkForValidity(token);

        String authMessageExpl = null;
        Integer authCode = Codes.NoError;
        switch (validationError)
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

                String login = JwtManager.getLoginFromToken(decodedJWT);
                
                String password = userService.getUserByLogin(login).get().getPassword();
                
                var authRequest = UsernamePasswordAuthenticationToken.unauthenticated(login, password);

                var auth = authenticationManager.authenticate(authRequest);

                SecurityContextHolder.getContext().setAuthentication(auth);
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

                var convertResult = JsonConverter.doSaveConvert(authResponse);

                response.getWriter().println(convertResult.getSecond());
            }

        }
        else 
        {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            var convertResult = JsonConverter.doSaveConvert(authResponse);
            response.getWriter().println(convertResult.getSecond());
        }

    }

    
    
}
