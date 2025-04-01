package com.real_property_system_api.real_property_system.configs;

import java.net.http.HttpRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import com.real_property_system_api.real_property_system.JwtFilter;
import com.real_property_system_api.real_property_system.bodies.AccessDeniedHandlerImpl;
import com.real_property_system_api.real_property_system.services.AuthProvider;
import com.real_property_system_api.real_property_system.services.DatabaseUserDetails;
import com.real_property_system_api.real_property_system.services.JwtManager;
import com.real_property_system_api.real_property_system.services.UserService;

import jakarta.servlet.http.HttpServletResponse;


@Configuration
@EnableWebSecurity
public class SecurityConfig 
{
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http
            .authorizeHttpRequests(x ->
            x
            .requestMatchers("/actuator/**").permitAll()
            .requestMatchers(HttpMethod.POST,"/api/public/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/public/**").permitAll()

            .requestMatchers(HttpMethod.POST, "/api/resources/users/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/resources/users/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET,"/api/resources/users/**").hasAnyRole("ADMIN", "CLIENT", "REALTOR", "MANAGER")
            .requestMatchers(HttpMethod.PUT, "/api/resources/users/**").hasAnyRole("ADMIN", "CLIENT", "REALTOR", "MANAGER")

            .requestMatchers(HttpMethod.POST, "/api/resources/passports/**").hasAnyRole("ADMIN", "CLIENT", "MANAGER")
            .requestMatchers(HttpMethod.PUT, "/api/resources/passports/**").hasAnyRole("ADMIN", "CLIENT", "MANAGER")
            .requestMatchers(HttpMethod.GET, "/api/resources/passports/**").hasAnyRole("ADMIN", "CLIENT", "MANAGER")
            
            .requestMatchers("/api/resources/land_plots/**").hasAnyRole("ADMIN", "CLIENT", "MANAGER")

            .anyRequest().authenticated()
            
            );


        http.exceptionHandling(x -> 
        {
            x.accessDeniedHandler(new AccessDeniedHandlerImpl());
        });


        http.exceptionHandling(x -> 
        x.authenticationEntryPoint((res, re, q) ->
        {
            re.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            re.getOutputStream().println("No auth!");
        }
        )
        
        );

        

        http.addFilterAt(new JwtFilter(jwtManager, authenticationManager(), userService), AuthorizationFilter.class);
        http.csrf(x ->
        x.disable());

        return http.build();
    }

    
    @Bean 
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    
    @Bean
    public DatabaseUserDetails userDetailsService()
    {
        return new DatabaseUserDetails();
    }

    @Bean 
    public AuthenticationManager authenticationManager()
    {
        AuthProvider authProvider = new AuthProvider();
        authProvider.setEncoder(passwordEncoder());
        authProvider.setService(userDetailsService());
        return new ProviderManager(authProvider);
    }


    @Autowired
    private JwtManager jwtManager;

    @Autowired
    private UserService userService;

}
