package com.example.demo.service;

import org.apache.coyote.BadRequestException;
import com.example.demo.controller.rest.dto.LoginRequest;
import com.example.demo.controller.rest.dto.TokenResponse;

public interface IAuthService {
    TokenResponse login(LoginRequest request) throws BadRequestException;
}
