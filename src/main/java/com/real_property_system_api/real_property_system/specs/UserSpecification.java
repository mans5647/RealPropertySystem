package com.real_property_system_api.real_property_system.specs;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.real_property_system_api.real_property_system.bodies.FilterBody;
import com.real_property_system_api.real_property_system.models.User;
import com.real_property_system_api.real_property_system.models.UserRole;

public class UserSpecification 
{
    public static final String LOGIN = "login";
    public static final String FIRSTNAME = "firstName";
    public static final String LASTNAME = "lastName";
    public static final String BIRTH = "birthDate";
    public static final String ROLE = "roleName";
    
    
    public static Specification<User> filterBy(FilterBody filterBody)
    {
        return Specification.where(hasLogin(filterBody.getLogin()))
            .and(hasFirstname(filterBody.getFirstname()))
            .and(hasLastname(filterBody.getLastname()))
            .and(betweenDates(filterBody.getBirthDateMin(), filterBody.getBirthDateMax()))
            .and(hasRole(filterBody.getRole()));
    }

    public static Specification<User> hasLogin(String login)
    {
        return (root, query, cb) -> {

            return login == null || login.isEmpty() ? cb.conjunction() : cb.equal(root.get(LOGIN), login);
        };
    }

    public static Specification<User> hasFirstname(String firstname)
    {
        return (root, query, cb) -> {

            return firstname == null || firstname.isEmpty() ? cb.conjunction() : cb.like(root.get(FIRSTNAME), firstname);
        };
    }

    public static Specification<User> hasLastname(String lastName)
    {
        return (root, query, cb) -> {

            return lastName == null || lastName.isEmpty() ? cb.conjunction() : cb.like(root.get(LASTNAME), lastName);
        };
    }

    public static Specification<User> betweenDates(LocalDate dateMin, LocalDate dateMax)
    {
        return (root, query, cb) -> {

            return dateMin == null || dateMax == null ? cb.conjunction() : cb.between(root.get(BIRTH), dateMin, dateMax);
        };
    }

    public static Specification<User> hasRole(String rolename)
    {
        return (root, query, cb) -> {

            return rolename == null || rolename.isEmpty() ? cb.conjunction() : (cb.equal(root.join("userRole").get(ROLE), rolename));
        };
    }
}
