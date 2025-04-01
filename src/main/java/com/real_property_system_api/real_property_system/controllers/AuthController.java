package com.real_property_system_api.real_property_system.controllers;

import org.springframework.web.bind.annotation.RestController;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import com.real_property_system_api.real_property_system.bodies.LoginBody;
import com.real_property_system_api.real_property_system.bodies.PrettyError;
import com.real_property_system_api.real_property_system.bodies.RegisterBodyWithRole;
import com.real_property_system_api.real_property_system.models.ServerLog;
import com.real_property_system_api.real_property_system.models.User;
import com.real_property_system_api.real_property_system.models.UserRole;
import com.real_property_system_api.real_property_system.responses.AuthResponse;
import com.real_property_system_api.real_property_system.responses.Codes;
import com.real_property_system_api.real_property_system.responses.FieldErrorsResponse;
import com.real_property_system_api.real_property_system.responses.JwtError;
import com.real_property_system_api.real_property_system.responses.JwtResponse;
import com.real_property_system_api.real_property_system.services.JsonConverter;
import com.real_property_system_api.real_property_system.services.JwtManager;
import com.real_property_system_api.real_property_system.services.RegisterBodyValidator;
import com.real_property_system_api.real_property_system.services.ServerLogService;
import com.real_property_system_api.real_property_system.services.UserRoleService;
import com.real_property_system_api.real_property_system.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtManager jwtManager;

    @Autowired
    private ServerLogService logService;

    @Autowired
    private MeterRegistry meterRegistry;

    // Metrics counters
    private final Counter loginSuccessCounter;
    private final Counter loginFailureCounter;
    private final Counter registrationSuccessCounter;
    private final Counter registrationFailureCounter;
    private final Counter abc;
    private final Timer loginTimer;
    private final Timer loginIntervalTimer; // Новая метрика для интервалов между логинами
    private Instant prevLoginTime = Instant.now(); // Время последнего логина

    final Map<String, String> codesToRole = Map.of(
            "93ead6b82ce7da62e20b455e62c295e514a4d99d299b9bf046e68c2591b77741", "ADMIN",
            "b7dd0bf00e0cbe5febee9d89d10f3b28d587e7c211f657fcb82414f82cfc004b", "MANAGER",
            "13d248adee00de77a9a70f2be674f7ae75cf143d993ad88208d3e6acb826b71d", "REALTOR");

    // Constructor to initialize metrics
    public AuthController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize login metrics
        this.loginSuccessCounter = Counter.builder("auth.login.success")
                .description("Number of successful login attempts")
                .register(meterRegistry);
        this.loginFailureCounter = Counter.builder("auth.login.failure")
                .description("Number of failed login attempts")
                .register(meterRegistry);
        this.loginTimer = Timer.builder("auth.login.duration")
                .description("Time taken for login operations")
                .register(meterRegistry);

        // Initialize registration metrics
        this.registrationSuccessCounter = Counter.builder("auth.registration.success")
                .description("Number of successful registrations")
                .register(meterRegistry);
        this.registrationFailureCounter = Counter.builder("auth.registration.failure")
                .description("Number of failed registrations")
                .register(meterRegistry);

        this.abc = Counter.builder("abc.adb.ccc")
                .description("!!!!!ABC!!!!!")
                .register(meterRegistry);

        // Новая метрика для интервалов между успешными логинами
        this.loginIntervalTimer = Timer.builder("auth.login.interval")
                .description("Time between consecutive successful logins")
                .register(meterRegistry);
    }

    @PostMapping("/login")
    public ResponseEntity<String> doLogin(@RequestBody LoginBody loginRequest, final HttpServletResponse response) {
        long startTime = System.nanoTime();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        var userHolder = userService.getUserByLogin(loginRequest.getLogin());
        if (!userHolder.isPresent()) {
            AuthResponse noSuchLogin = new AuthResponse();
            noSuchLogin.authCode = Codes.AuthLoginNotFound;
            noSuchLogin.message = "Пользователь не найден";

            logService.Error(ServerLog.fmt("User %1$s not found on the server", loginRequest.getLogin()));
            loginFailureCounter.increment();

            Pair<Boolean, String> values = JsonConverter.doSaveConvert(noSuchLogin);
            loginTimer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
            return new ResponseEntity<>(values.getFirst() ? values.getSecond() : null, headers,
                    values.getFirst() ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR);
        }

        User user = userHolder.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            AuthResponse incorrectPasswordResponse = new AuthResponse();
            incorrectPasswordResponse.authCode = Codes.AuthPasswordIncorrect;
            incorrectPasswordResponse.message = "Неправильный пароль";

            logService.Error(ServerLog.fmt("Passwords were not matched for user %1$s", loginRequest.getLogin()));
            loginFailureCounter.increment();

            Pair<Boolean, String> values = JsonConverter.doSaveConvert(incorrectPasswordResponse);
            loginTimer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
            return new ResponseEntity<>(values.getFirst() ? values.getSecond() : null, headers,
                    values.getFirst() ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String accessToken = jwtManager.generateAccessToken(user, 15L);
        String refreshToken = jwtManager.generateRefreshToken(user, 60L);

        JwtResponse jwtResponse = new JwtResponse(Codes.NoError, accessToken, refreshToken);
        String json = null;
        try {
            json = JsonConverter.toJson(jwtResponse);
        } catch (IOException ex) {
            jwtResponse.setError(Codes.ServiceError);
            loginFailureCounter.increment();
            loginTimer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
            return new ResponseEntity<>(null, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String fmt = ServerLog.fmt("The login was performed by user '%1$s'", loginRequest.getLogin());
        logService.Info(fmt);
        loginSuccessCounter.increment();

        // Запись интервала между успешными логинами
        Instant loginTime = Instant.now();
        long seconds = Math.abs(Duration.between(loginTime, prevLoginTime).get(ChronoUnit.SECONDS));
        loginIntervalTimer.record(seconds, TimeUnit.SECONDS); // Используем Timer вместо Gauge
        prevLoginTime = loginTime;

        loginTimer.record(System.nanoTime() - startTime, TimeUnit.NANOSECONDS);
        return new ResponseEntity<>(json, headers, HttpStatus.OK);
    }

    @PostMapping("/create_account")
    public ResponseEntity<String> doRegister(@Valid @RequestBody RegisterBodyWithRole registerBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        RegisterBodyValidator validator = new RegisterBodyValidator();
        var errors = validator.validateObject(registerBody);

        if (errors.hasErrors()) {
            FieldErrorsResponse fieldErrorsResponse = new FieldErrorsResponse();
            fieldErrorsResponse.getMetadata().code = Codes.FieldErrors;
            fieldErrorsResponse.getMetadata().message = "Проверьте корректность заполненных полей";

            logService.Error("Oops, there was error in fields, somebody wanted to register");
            registrationFailureCounter.increment();

            List<PrettyError> convenientErrorList = new ArrayList<>();
            for (FieldError error : errors.getFieldErrors()) {
                convenientErrorList.add(new PrettyError(error.getField(), error.getCode(), error.getDefaultMessage()));
            }

            fieldErrorsResponse.setErrors(convenientErrorList);

            var data = JsonConverter.doSaveConvert(fieldErrorsResponse);
            return new ResponseEntity<>(data.getFirst() ? data.getSecond() : null, headers, HttpStatus.BAD_REQUEST);
        }

        String login = registerBody.getLogin();

        if (userService.isUserExists(login)) {
            AuthResponse errorExists = new AuthResponse();
            errorExists.authCode = Codes.RegisterFailedAccountAlreadyExists;
            errorExists.message = "Такой пользователь уже существует";

            String logMessage = ServerLog.fmt("Already exists '%1$s'", login);
            logService.Error(logMessage);
            registrationFailureCounter.increment();

            var data = JsonConverter.doSaveConvert(errorExists);
            String json = data.getSecond();

            return new ResponseEntity<>(json, headers, HttpStatus.CONFLICT);
        }

        User user = new User();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(registerBody.getPassword()));
        String code = registerBody.getCode();
        String assignedRole = "CLIENT"; // Default role

        if (code == null || code.isEmpty() || code.equals("")) {
            var userRole = userRoleService.getBySuffix("CLIENT");
            if (userRole == null) {
                var addable = new UserRole();
                addable.setRoleName("Клиент");
                addable.setSuffix("CLIENT");
                var added = userRoleService.addNewUserRole(addable);
                user.setUserRole(added);
            } else {
                user.setUserRole(userRole);
            }
        } else {
            var values = codesToRole.entrySet();
            String roleName = null;

            for (var entry : values) {
                String firstCodeValues = entry.getKey().substring(0, 15);
                if (firstCodeValues.equals(code)) {
                    roleName = entry.getValue();
                    assignedRole = roleName;
                    break;
                }
            }

            if (roleName != null) {
                var found = userRoleService.getBySuffix(roleName);
                if (found == null) {
                    String prettyName;
                    switch (roleName) {
                        case "ADMIN": prettyName = "Администратор"; break;
                        case "MANAGER": prettyName = "Менеджер"; break;
                        case "REALTOR": prettyName = "Риелтор"; break;
                        case "CLIENT": default: prettyName = "Клиент"; break;
                    }
                    UserRole NewRole = new UserRole();
                    NewRole.setRoleName(prettyName);
                    NewRole.setSuffix(roleName);
                    var added = userRoleService.addNewUserRole(NewRole);
                    user.setUserRole(added);
                } else {
                    user.setUserRole(found);
                }
            } else {
                AuthResponse invalidCodeResponse = new AuthResponse();
                invalidCodeResponse.authCode = Codes.InvalidCode;
                invalidCodeResponse.message = "Неправильный код";

                String fmt = ServerLog.fmt("Incorrect employee code for registrating user '%1$s'", login);
                logService.Error(fmt);
                registrationFailureCounter.increment();

                var convertResult = JsonConverter.doSaveConvert(invalidCodeResponse);
                var json = convertResult.getSecond();
                return new ResponseEntity<>(json, headers, HttpStatus.BAD_REQUEST);
            }
        }

        var added = userService.CreateNewUser(user);
        var convertResult = JsonConverter.doSaveConvert(added);
        var json = convertResult.getSecond();

        String msg = ServerLog.fmt("Created user: '%1$s'; account time expiring: %2$d minutes", added.getLogin(), 15L);
        logService.Info(msg);

        registrationSuccessCounter.increment();
        return new ResponseEntity<>(json, headers, HttpStatus.CREATED);
    }

    @PostMapping("/refresh_token")
    public JwtResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("RefreshToken");

        if (token == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return JwtResponse.Invalid();
        }

        JwtResponse jwtResponse = new JwtResponse(Codes.NoError, null, null);
        switch (jwtManager.checkForValidity(token)) {
            case JwtError.Expired: {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jwtResponse.setError(Codes.AuthTokenExpired);
                break;
            }
            case JwtError.InvalidSignature: {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jwtResponse.setError(Codes.AuthTokenInvalid);
                break;
            }
            case JwtError.NoError: {
                var djwt = JwtManager.getDecodedJwt(token);
                var login = JwtManager.getLoginFromToken(djwt);
                var user = userService.getUserByLogin(login).orElse(null);
                jwtResponse.setAccess_token(jwtManager.generateAccessToken(user, 15L));
                jwtResponse.setRefresh_token(jwtManager.generateRefreshToken(user, 60L));
            }
        }
        return jwtResponse;
    }
}