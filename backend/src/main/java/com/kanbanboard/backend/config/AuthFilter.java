package com.kanbanboard.backend.config;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.kanbanboard.backend.service.Jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Servlet is Java's old low lvl way of handling web req -> Tomcat
// it builds an HttpServletRequest / HttpServletResponse for every request that comes in.

@Component //generic bean
public class AuthFilter extends OncePerRequestFilter{ //plugs into request pipeline and guarantees run once per req
    private final Jwt jwt;
    public AuthFilter (Jwt jwt) {
        this.jwt = jwt;
    }

    // Spring will automatically call this method
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null) {
            String userId = jwt.isTokenValid(token);
            // this specific request belongs to this specific user
            if (userId != null) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, null, List.of()); // [who, proof, permission] -> [principal, credentials, authorities]
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        //w/o this line, request will hang
        filterChain.doFilter(request, response);
    }
}
