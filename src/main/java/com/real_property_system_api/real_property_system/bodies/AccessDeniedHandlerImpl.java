package com.real_property_system_api.real_property_system.bodies;

import java.io.IOException;

import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.real_property_system_api.real_property_system.responses.Codes;
import com.real_property_system_api.real_property_system.responses.GenericMessage;
import com.real_property_system_api.real_property_system.services.JsonConverter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class AccessDeniedHandlerImpl implements AccessDeniedHandler
{
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException 
    {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        GenericMessage genericMessage = new GenericMessage();
        genericMessage.code = Codes.AccessDeniedErr;
        genericMessage.message = "Запрещен доступ";
        response.getWriter().print(JsonConverter.doSaveConvert(genericMessage).getSecond());
    }
}