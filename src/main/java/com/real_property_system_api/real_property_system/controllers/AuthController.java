package com.real_property_system_api.real_property_system.controllers;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.real_property_system_api.real_property_system.bodies.LoginBody;
import com.real_property_system_api.real_property_system.bodies.PrettyError;
import com.real_property_system_api.real_property_system.bodies.RegisterBodyWithRole;
import com.real_property_system_api.real_property_system.models.User;
import com.real_property_system_api.real_property_system.models.UserRole;
import com.real_property_system_api.real_property_system.responses.AuthResponse;
import com.real_property_system_api.real_property_system.responses.Codes;
import com.real_property_system_api.real_property_system.responses.FieldErrorsResponse;
import com.real_property_system_api.real_property_system.responses.GenericMessage;
import com.real_property_system_api.real_property_system.responses.JwtResponse;
import com.real_property_system_api.real_property_system.services.JsonConverter;
import com.real_property_system_api.real_property_system.services.JwtManager;
import com.real_property_system_api.real_property_system.services.RegisterBodyValidator;
import com.real_property_system_api.real_property_system.services.UserRoleService;
import com.real_property_system_api.real_property_system.services.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/public")
public class AuthController 
{

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtManager jwtManager;

    final Gson gson = new Gson();

    @PostMapping("/login")
    public ResponseEntity<String> DoLoginIntoPropertySystem(@RequestBody LoginBody loginRequest, final HttpServletResponse response)
    {
        var userHolder = userService.getUserByLogin(loginRequest.getLogin());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        if (!userHolder.isPresent()) 
        {
            AuthResponse no_such_login = new AuthResponse();
            no_such_login.authCode = Codes.AuthLoginNotFound;
            no_such_login.message = "Пользователь не найден";
            
            Pair<Boolean, String> values = JsonConverter.doSaveConvert(no_such_login);
            return new ResponseEntity<>(values.getFirst() ? values.getSecond() : null, headers, values.getFirst() ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR);
        }

        User user = userHolder.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
        {
            AuthResponse incorrectPasswordResponse = new AuthResponse();
            incorrectPasswordResponse.authCode = Codes.AuthPasswordIncorrect;
            incorrectPasswordResponse.message = "Неправильный пароль";
            Pair<Boolean, String> values = JsonConverter.doSaveConvert(incorrectPasswordResponse);
            return new ResponseEntity<>(values.getFirst()? values.getSecond() : null, headers, values.getFirst() ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String accessToken = jwtManager.generateAccessToken(user, 15L);
        String refreshToken = jwtManager.generateRefreshToken(15L);

        JwtResponse jwtResponse = new JwtResponse(Codes.NoError, accessToken, refreshToken);
        String json = null;
        try 
        {
            json = JsonConverter.toJson(jwtResponse);
        }
        catch (IOException ex)
        {
            jwtResponse.setError(Codes.ServiceError);
            return new ResponseEntity<>(null,null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return new ResponseEntity<>(json, headers, HttpStatus.OK);

    }

    @PostMapping("/create_account")
    public ResponseEntity<String> DoRegisterIntoPropertySystem(@Valid @RequestBody RegisterBodyWithRole registerBodyWithRole)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        
        RegisterBodyValidator validator = new RegisterBodyValidator();
        var errors = validator.validateObject(registerBodyWithRole);

        if (errors.hasErrors())
        {
            FieldErrorsResponse fieldErrorsResponse = new FieldErrorsResponse();

            fieldErrorsResponse.getMetadata().code = Codes.FieldErrors;
            fieldErrorsResponse.getMetadata().message = "Проверьте корректность заполненных полей";



            ArrayList<PrettyError> _convenientErrorList = new ArrayList<>();
            for (FieldError error : errors.getFieldErrors())
            {
                _convenientErrorList.add(new PrettyError(error.getField(), error.getCode(), error.getDefaultMessage()));
            }

            fieldErrorsResponse.setErrors(_convenientErrorList);

            var data = JsonConverter.doSaveConvert(fieldErrorsResponse);
            return new ResponseEntity<>(data.getFirst() ? data.getSecond() : null, headers, HttpStatus.BAD_REQUEST);
        }
        
        String login = registerBodyWithRole.getLogin();

        


        HttpStatus createdStatus = HttpStatus.CREATED;
        if (userService.isUserExists(login))
        {
            AuthResponse error_exists = new AuthResponse();
            error_exists.authCode = Codes.RegisterFailedAccountAlreadyExists;
            error_exists.message = "Такой пользователь уже существует";

            var data = JsonConverter.doSaveConvert(error_exists);

            String json = data.getFirst() ? data.getSecond() : null;

            return new ResponseEntity<>(json, headers, HttpStatus.CONFLICT);
        }
        
        User user = new User();
        UserRole uRole = null;
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(registerBodyWithRole.getPassword()));
        
        if (!userRoleService.IsRoleExists(registerBodyWithRole.getRoleSuffix()))
        {
            UserRole userRole = new UserRole();
            String roleNameBySuffix = "";

            switch (registerBodyWithRole.getRoleSuffix())
            {
                case "ADMIN": roleNameBySuffix = "Администратор"; break;
                case "MANAGER": roleNameBySuffix = "Менеджер"; break;
                case "CLIENT": roleNameBySuffix = "Клиент"; break;
                default:
                    roleNameBySuffix = "Риелтор";
            }

            userRole.setRoleName(roleNameBySuffix);
            userRole.setSuffix(registerBodyWithRole.getRoleSuffix());
            uRole = userRoleService.addNewUserRole(userRole);
        }
        else uRole = userRoleService.getBySuffix(registerBodyWithRole.getRoleSuffix());

        user.setUserRole(uRole);
        
        var created = userService.CreateNewUser(user);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try 
        {
            json = objectMapper.writeValueAsString(created);
        }
        catch (IOException ex)
        {
            
        }


        return new ResponseEntity<>(json, headers, createdStatus);
    }
}
