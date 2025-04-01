package com.real_property_system_api.real_property_system.services;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.real_property_system_api.real_property_system.models.User;
import com.real_property_system_api.real_property_system.responses.ValidationConstraints;

public class UserValidator implements Validator 
{
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        
        User value = (User)target;

        if (value.getLogin() != null)
        {
            if (value.getLogin().trim().length() < ValidationConstraints.MinLen)
            {
                errors.rejectValue("login", "minlen", "Логин должен быть минимум 8 символов");
            }

            else if (!hasAnyLetter(value.getLogin().trim()))
            {
                errors.rejectValue("login", "no_letter", "Логин должен содержать буквы");
            }
        }
        else 
        {
            errors.rejectValue("login", "no_instance", "Заполните логин");
        }


        if (value.getPassword() != null)
        {

            if (value.getPassword().trim().length() < ValidationConstraints.MinLen)
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

        }

        else 
        {
            errors.rejectValue("password", "no_instance", "Заполните пароль");

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
