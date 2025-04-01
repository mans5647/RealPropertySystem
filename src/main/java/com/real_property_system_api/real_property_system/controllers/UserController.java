package com.real_property_system_api.real_property_system.controllers;




import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.ICSVParser;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.opencsv.validators.LineValidator;
import com.real_property_system_api.real_property_system.bodies.FilterBody;
import com.real_property_system_api.real_property_system.bodies.PrettyError;
import com.real_property_system_api.real_property_system.bodies.SingleValue;
import com.real_property_system_api.real_property_system.bodies.UserBody;
import com.real_property_system_api.real_property_system.models.Passport;
import com.real_property_system_api.real_property_system.models.User;
import com.real_property_system_api.real_property_system.models.UserRole;
import com.real_property_system_api.real_property_system.responses.Codes;
import com.real_property_system_api.real_property_system.responses.FieldErrorsResponse;
import com.real_property_system_api.real_property_system.services.JsonConverter;
import com.real_property_system_api.real_property_system.services.PassportService;
import com.real_property_system_api.real_property_system.services.UserRoleService;
import com.real_property_system_api.real_property_system.services.UserService;
import com.real_property_system_api.real_property_system.services.UserValidator;
import com.real_property_system_api.real_property_system.utils.CustomCsvValidator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/resources")
public class UserController 
{
    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PassportService passportService;

    @GetMapping("/users/all")
    public List<User> getAllUsers()
    {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{login}")
    public User getUserByLogin(@PathVariable("login") String login)
    {
        return userService.getUserByLogin(login).orElse(null);
    }

    @RequestMapping(method = RequestMethod.HEAD, path = "/users/check_existance")
    public ResponseEntity<Void> checkForUserExistance(@RequestParam("login") String login)
    {

        if (userService.isUserExists(login))
        {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/users/ch")
    public List<User> getChunkedUserList(@RequestParam(defaultValue = "0", name = "page") int page, 
                                         @RequestParam(defaultValue = "5", name = "size") int size,
                                         @RequestParam(required = false, name = "except_ids") List<Long> ids)
    {

        Pageable pageable = PageRequest.of(page, size);

        Page<User> users = userService.GetPageable(pageable);

        if (ids != null && !ids.isEmpty())
        {
            if (users.hasContent())
            {
                return ExceptIds(ids, users);
            }
        }

        

        return users.hasContent() ? users.getContent() : Collections.emptyList();
    }

    @GetMapping("/users/filtered")
    public List<User> getUsersChunkedAndFiltered(
        @RequestParam(defaultValue = "0", name = "page") int page, 
        @RequestParam(defaultValue = "5", name = "size") int size,
        @RequestParam(name = "except_ids", required = false) List<Long> ids,
        FilterBody filterBody
        )
    {
        Pageable request = PageRequest.of(page, size);
        
        Page<User> users = userService.GetFiltered(request, filterBody);

        return ExceptIds(ids, users);

    }

    @GetMapping("/users/total")
    public SingleValue<Long> getTotalUsers()
    {
        return SingleValue.of(userService.GetCount());
    }

    @GetMapping("/users/total_pages")
    public SingleValue<Integer> getCount()
    {
        Pageable p = PageRequest.of(0, 5);
        var pc = userService.GetPageable(p);
        return SingleValue.of(pc.getTotalPages());

    }

    public List<User> ExceptIds(List<Long> ids, Page<User> users)
    {
        if (ids != null && !ids.isEmpty())
        {
            if (users.hasContent())
            {
                //var itr = ids.iterator();
                
                List<User> modifableList = new ArrayList<>(users.getContent());

                modifableList.removeIf((user) ->
                {
                    for (Long id : ids)
                    {
                        if (id == user.getId().longValue()) return true;
                    }

                    return false;
                });

                return modifableList;
            }
        }

        return users.getContent();
    } 
    

    @PutMapping("/users/{login}")
    public ResponseEntity<String> updateUserByLogin(@PathVariable("login") String login, @RequestBody UserBody data)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        UserValidator validator = new UserValidator();
        var errors = validator.validateObject(data.getUser());

        if (errors.hasErrors())
        {
            FieldErrorsResponse response = new FieldErrorsResponse();

            response.getMetadata().code = Codes.FieldErrors;
            response.getMetadata().message = "Проверьте корректность заполненных полей";

            List<PrettyError> convenientErrorList = new ArrayList<>();
            for (FieldError error : errors.getFieldErrors())
            {
                convenientErrorList.add(new PrettyError(error.getField(), error.getCode(), error.getDefaultMessage()));
            }

            response.setErrors(convenientErrorList);

            var convertResult = JsonConverter.doSaveConvert(response);

            return new ResponseEntity<>(convertResult.getSecond(), headers, HttpStatus.BAD_REQUEST);
        }

        User updatedUserFromBody = data.getUser();

        String encoded = passwordEncoder.encode(updatedUserFromBody.getPassword());

        updatedUserFromBody.setPassword(encoded);

        UserRole role = userRoleService.getById(data.getRoleId());

        updatedUserFromBody.setUserRole(role);

        if (data.getPassportId() != -1)
        {
            Optional<Passport> passportHolder = passportService.GetById(data.getPassportId());
            if (passportHolder.isPresent())
            {
                updatedUserFromBody.setPassport(passportHolder.get());
            }
        }

        

        User updatedUserFromDb = userService.UpdateUser(updatedUserFromBody);

        var convertResult = JsonConverter.doSaveConvert(updatedUserFromDb);
        System.out.println(convertResult.getFirst());
        return new ResponseEntity<>(convertResult.getSecond(), headers, HttpStatus.OK);
    }


    @PostMapping("/users/add")
    public ResponseEntity<String> AddUser(@RequestBody User user)
    {

        UserValidator userValidator = new UserValidator();

        var errs = userValidator.validateObject(user);

        if (errs.hasErrors())
        {
            FieldErrorsResponse response = new FieldErrorsResponse();

            response.getMetadata().code = Codes.FieldErrors;
            response.getMetadata().message = "Проверьте корректность заполненных полей";

            List<PrettyError> convenientErrorList = new ArrayList<>();
            for (FieldError error : errs.getFieldErrors())
            {
                convenientErrorList.add(new PrettyError(error.getField(), error.getCode(), error.getDefaultMessage()));
            }

            response.setErrors(convenientErrorList);

            var convertResult = JsonConverter.doSaveConvert(response);

            return ResponseEntity.badRequest()
                .body(convertResult.getSecond());
        }

        if (userService.isUserExists(user.getLogin()))
        {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(null);
        }

        UserRole r = userRoleService.getBySuffix("CLIENT");
        
        user.setUserRole(r);

        var cuser = userService.CreateNewUser(user);
        
        return ResponseEntity.ok()
            .build();
    }


    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<String> DeleteUser(@PathVariable("id") Long id)
    {
        if (!userService.existsById(id))
        {
            return ResponseEntity.notFound()
                .build();
        }

        userService.DeleteUserById(id);

        return ResponseEntity.ok()
            .build();
    }

    @PostMapping("/users/checkforvalidity")
    public ResponseEntity<String> checkForUserValidity(@RequestBody User user)
    {
        return internalCheckForUserValidity(user);
    }

    @PostMapping(path = "/users/export_as_csv")
    public List<User> exportCsv(@RequestBody String data, HttpServletRequest req, HttpServletResponse resp)
    {
        var usersList = internalParseUserCsvData(data);
        
        if (!usersList.isEmpty())
        {
            List<User> addedUserListWithIds = new ArrayList<>();
            for (User user : usersList)
            {
                if (userService.isUserExists(user.getLogin()))
                {
                    continue;
                }

                if (user.getUserRole() == null)
                {
                    var _role = userRoleService.addOrReturn("CLIENT");
                    user.setUserRole(_role);
                }

                Passport passportData = user.getPassport();

                if (passportData != null)
                {
                    Passport dbPassport = passportService.Save(passportData);
                    user.setPassport(dbPassport);
                }


                var _value = userService.CreateNewUser(user);
                
                addedUserListWithIds.add(_value);
            }

            if (addedUserListWithIds.isEmpty())
            {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                return Collections.emptyList();
            }

            resp.setStatus(HttpServletResponse.SC_OK);
            return addedUserListWithIds;
        }

        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        return Collections.emptyList();
    }

    @GetMapping(path = "/users/export_reversed")
    public String importUsersCsv(HttpServletResponse resp)
    {
        resp.setContentType("text/csv; charset=utf-8");
        return doImportCsvAll();
    }
    

    private String doImportCsvAll()
    {
        List<User> all = userService.getAllUsers();
        final String COMMA = ",";
        StringBuilder csvRawString = new StringBuilder();
        for (User user : all)
        {
            String fname = user.getFirstName();
            String lname = user.getLastName();
            String log = user.getLogin();
            String pwd = user.getPassword();
            String date = null;

            if (user.getBirthDate() != null)
            {
                date = user.getBirthDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy 00:00:00"));
            }
            
            
            String jpassport = null, jrole = null;

            var encoder = Base64.getEncoder();

            if (user.getPassport() != null)
            {
                jpassport = JsonConverter.doSaveConvert(user.getPassport()).getSecond();
                
                byte[] buf = encoder.encode(jpassport.getBytes(StandardCharsets.UTF_8));

                jpassport = new String(buf, StandardCharsets.UTF_8);

            }

            if (user.getUserRole() != null)
            {
                jrole = JsonConverter.doSaveConvert(user.getUserRole()).getSecond();

                byte[] buf = encoder.encode(jrole.getBytes(StandardCharsets.UTF_8));

                jrole = new String(buf, StandardCharsets.UTF_8);
            }

            
            String fmt = "";

            fmt += (fname != null) ? fname : "";
            fmt += COMMA;
            fmt += (lname != null) ? lname : "";
            fmt += COMMA;
            fmt += (log != null) ? log : "";
            fmt += COMMA;
            fmt += (pwd != null) ? pwd : "";
            fmt += COMMA;

            fmt += (date != null) ? date : "";
            fmt += COMMA;

            fmt += (jpassport != null) ? jpassport : "";
            fmt += COMMA;
            fmt += (jrole != null) ? jrole : "";

            fmt += "\r\n";
            csvRawString.append(fmt);
            
        }

        return csvRawString.toString();
    }

    private List<User> internalParseUserCsvData(String data)
    {
        CSVParser parser = new CSVParserBuilder()
            .withSeparator(',')
            .build();
        CSVReader reader = new CSVReaderBuilder(new StringReader(data))
            .withCSVParser(parser)
            .build();

        try
        {
            List<User> newUsers = new ArrayList<>();
            String[] line = reader.readNext();

            while (line != null)
            {
                User user = new User();
                String firstname = line[0];
                String lastname = line[1];
                String login = line[2];
                String password = line[3];

                String dataBirthDate = line[4];

                if (dataBirthDate != null && !dataBirthDate.isEmpty())
                {
                    LocalDateTime date = LocalDateTime.parse(line[4], DateTimeFormatter.ofPattern("dd.MM.yyyy H:mm:ss"));
                    user.setBirthDate(date.toLocalDate());
                }
                
                if (line.length > 6)
                {
                    String encodedAsBase64_passport = line[5];
                    String encodedAsBase64_role = line[6];

                    byte[] utfBytes_passport = Base64.getDecoder().decode(encodedAsBase64_passport);
                    byte[] utfBytes_role = Base64.getDecoder().decode(encodedAsBase64_role);

                    String jsonPassport = new String(utfBytes_passport, StandardCharsets.UTF_8);
                    String jsonRole = new String(utfBytes_role, StandardCharsets.UTF_8);

                    ObjectMapper jsonMapper = new ObjectMapper().findAndRegisterModules()
                        .setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

                    
                    if (jsonPassport != null && !jsonPassport.isEmpty())
                    {
                        var passport = jsonMapper.readValue(jsonPassport, Passport.class);
                        user.setPassport(passport);
                    }

                    if (jsonRole != null && !jsonRole.isEmpty())
                    {
                        var role = jsonMapper.readValue(jsonRole, UserRole.class);
                        user.setUserRole(role);
                    }
                }

                

                user.setFirstName(firstname);
                user.setLastName(lastname);
                user.setLogin(login);
                
                String encoded = passwordEncoder.encode(password);

                user.setPassword(encoded);

                newUsers.add(user);
                line = reader.readNext();
            }

            return newUsers;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return new ArrayList<>();
    }

    private ResponseEntity<String> internalCheckForUserValidity(User data)
    {
        UserValidator validator = new UserValidator();
        HttpHeaders headers = new HttpHeaders();
        var errors = validator.validateObject(data);

        headers.setContentType(MediaType.APPLICATION_JSON);
        
        if (errors.hasErrors())
        {
            FieldErrorsResponse response = new FieldErrorsResponse();

            response.getMetadata().code = Codes.FieldErrors;
            response.getMetadata().message = "Проверьте корректность заполненных полей";

            List<PrettyError> convenientErrorList = new ArrayList<>();
            for (FieldError error : errors.getFieldErrors())
            {
                convenientErrorList.add(new PrettyError(error.getField(), error.getCode(), error.getDefaultMessage()));
            }

            response.setErrors(convenientErrorList);

            var convertResult = JsonConverter.doSaveConvert(response);

            return new ResponseEntity<>(convertResult.getSecond(), headers, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().body(null);
    }

}
