package com.uw.TrainerWorkloadService.config;

import com.uw.TrainerWorkloadService.config.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

/**
 * Security configuration class for the Trainer Workload Service.
 * Configures JWT authentication, CORS, and other security settings.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

      private final JwtRequestFilter jwtRequestFilter;

      /**
       * Constructor to inject JwtRequestFilter.
       *
       * @param jwtRequestFilter the JWT request filter
       */
      @Autowired
      public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
            this.jwtRequestFilter = jwtRequestFilter;
      }

      /**
       * Configures the security filter chain.
       *
       * @param http the HttpSecurity object to configure
       * @return the configured SecurityFilterChain
       * @throws Exception if an error occurs during configuration
       */
      @Bean
      public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .cors().configurationSource(corsConfigurationSource()).and()
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/actuator/**", "/h2-console").permitAll()
                            .anyRequest().authenticated()
                    )
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Add the filter

            return http.build();
      }

      /**
       * Configures CORS settings.
       *
       * @return the configured CorsConfigurationSource
       */
      @Bean
      public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(List.of("*")); // Adjust the URL as needed
            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
            configuration.setAllowCredentials(true);
            configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
      }

      /**
       * Configures the password encoder.
       *
       * @return the password encoder
       */
      @Bean
      public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
      }

      /**
       * Configures the authentication manager.
       *
       * @param authenticationConfiguration the authentication configuration
       * @return the authentication manager
       * @throws Exception if an error occurs during configuration
       */
      @Bean
      public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
      }
}