package com.uw.TrainerWorkloadService.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

      private JwtUtil jwtUtil;

      @Autowired
      public JwtRequestFilter(JwtUtil jwtUtil) {
            this.jwtUtil = jwtUtil;
      }

      @Override
      protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
              throws ServletException, IOException {

            final String authorizationHeader = request.getHeader("Authorization");

            String username = null;
            String jwt = null;

            // Extract the token from the header
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                  jwt = authorizationHeader.substring(7);
                  username = jwtUtil.extractUsername(jwt);  // Extract username from the JWT
            }

            // Validate the token and set the authentication if it's valid
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                  if (jwtUtil.validateToken(jwt)) {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                username, null, null);  // No authorities are being set here
                        // Set the authentication in the context
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                  }
            }

            filterChain.doFilter(request, response);
      }
}
