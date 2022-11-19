package com.example.common.exception;

import com.example.common.dto.response.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) throws IOException, ServletException {
        ObjectMapper objectMapper = new ObjectMapper();
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatusname(HttpStatus.FORBIDDEN.name());
        baseResponse.setStatus(HttpStatus.FORBIDDEN.value());
        baseResponse.setMessage("Access denied");
        baseResponse.setPayload(null);
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(200);
        res.getWriter().write(objectMapper.writeValueAsString(baseResponse));
    }
}
