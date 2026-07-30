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
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7); //Ignoring the "Bearer "
            String username = jwt.isTokenValid(token);

            if (username != null) {
                // this specific request belongs to this specific user
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, List.of()); // [who, proof, permission] -> [principal, credentials, authorities]
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        //w/o this line, request will hang
        filterChain.doFilter(request, response);
    }
}
