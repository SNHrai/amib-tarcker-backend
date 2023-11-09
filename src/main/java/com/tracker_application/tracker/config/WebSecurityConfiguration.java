package com.tracker_application.tracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tracker_application.tracker.config.jwt.JwtAuthenticationEntityPoint;
import com.tracker_application.tracker.config.jwt.JwtRequestFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@Deprecated
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntityPoint jwtAuthenticationEntityPoint;
    private final JwtRequestFilter jwtRequestFilter;

    // private final UserDetailsService jwtService;

    // @Autowired
    // public WebSecurityConfiguration(JwtAuthenticationEntityPoint
    // jwtAuthenticationEntityPoint, @Lazy JwtRequestFilter jwtRequestFilter,
    // UserDetailsService jwtService) {
    // this.jwtAuthenticationEntityPoint = jwtAuthenticationEntityPoint;
    // this.jwtRequestFilter = jwtRequestFilter;
    // this.jwtService = jwtService;
    // }

    // @Bean
    // @Override
    // public AuthenticationManager authenticationManagerBean() throws Exception {
    // return super.authenticationManagerBean();
    // }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf(csrf -> csrf.disable())
                .authorizeRequests(requests -> requests
                        .antMatchers("/authenticate", "/users").permitAll()
                        .antMatchers(HttpHeaders.ALLOW).permitAll()
                        .antMatchers("task/**").hasRole("Employee")
                        .anyRequest().authenticated())
                .exceptionHandling(handling -> handling.authenticationEntryPoint(jwtAuthenticationEntityPoint))
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    // @Bean
    // public PasswordEncoder passwordEncoder() {
    // return new BCryptPasswordEncoder();
    // }

    // @Autowired
    // public void configureGlobal(AuthenticationManagerBuilder builder) throws
    // Exception {
    // builder.userDetailsService(jwtService).passwordEncoder(passwordEncoder());
    // }
}
