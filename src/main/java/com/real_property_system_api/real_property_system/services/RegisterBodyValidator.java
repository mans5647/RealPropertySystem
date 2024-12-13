package com.real_property_system_api.real_property_system.services;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.real_property_system_api.real_property_system.bodies.RegisterBodyWithRole;
import com.real_property_system_api.real_property_system.responses.ValidationConstraints;

public class RegisterBodyValidator implements Validator
{

    @Override
    public boolean supports(Class<?> clazz) {
        return RegisterBodyWithRole.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        
        RegisterBodyWithRole value = (RegisterBodyWithRole)target;

        if (value.getLogin().trim().length() < ValidationConstraints.MinLen)
        {
            errors.rejectValue("login", "minlen", "Логин должен быть минимум 8 символов");
        }

        else if (!hasAnyLetter(value.getLogin().trim()))
        {
            errors.rejectValue("login", "no_letter", "Логин должен содержать буквы");
        }

        else if (value.getPassword().trim().length() < ValidationConstraints.MinLen)
        {
            errors.rejectValue("password", "minlen", "Пароль должен быть минимум 8 символов");
        }

        else if (!hasAnyDigit(value.getPassword().trim()))
        {
            errors.rejectValue("password", "no_digit", "Пароль должен содержать цифры");    
        }

        else if (!hasAtLeastOneUpperLetter(value.getPassword().trim()))
        {
            errors.rejectValue("password", "no_upper", "Пароль должен содержать минимум 1 заглавную букву");
        }

        else if (!hasLowerLetters(value.getPassword().trim()))
        {
            errors.rejectValue("password", "no_lower", "Пароль должен содержать минимум 1 заглавную букву");
        }

        String role = value.getRoleSuffix();

        if (role == null)
        {
            errors.rejectValue("RoleSuffix", "no_value", "Роль пуста");
        }
    }
    

    private boolean hasAtLeastOneUpperLetter(String password)
    {
        int count = 0;

        for (char i : password.toCharArray())
        {
            if (Character.isUpperCase(i)) count++;
        }

        return count > 0;

    }

    private boolean hasLowerLetters(String password)
    {
        int count = 0;

        for (char i : password.toCharArray())
        {
            if (Character.isLowerCase(i)) count++;
        }

        return count > 0;
    }

    private boolean hasAnyLetter(String value)
    {
        int count = 0;
        for (char i : value.toCharArray())
        {
            if (Character.isLetter(i)) count++;
        }

        return count > 0;
    }

    private boolean hasAnyDigit(String value)
    {
        int count = 0;

        for (char i : value.toCharArray())
        {
            if (Character.isDigit(i)) count++;
        }

        return count > 0;
    }

}
