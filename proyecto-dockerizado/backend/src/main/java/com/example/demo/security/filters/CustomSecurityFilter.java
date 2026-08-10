package com.example.demo.security.filters;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomSecurityFilter extends OncePerRequestFilter {


    private final boolean maintenanceMode = false;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // rutas públicas que no deben bloquearse
        if (path.startsWith("/mvc/auth/login")
                || path.startsWith("/css/")
                || path.startsWith("/error")
                || path.startsWith("/h2-console")) {

            filterChain.doFilter(request, response);
            return;
        }

        // si está en mantenimiento
        if (maintenanceMode) {
            response.sendError(
                    HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "Esta página está en mantenimiento"
            );
            return;
        }

        // continuar flujo normal
        filterChain.doFilter(request, response);
    }
    }

