package com.tracker_application.tracker.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.http.HttpRequest;
import org.apache.http.impl.bootstrap.HttpServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tracker_application.tracker.jwtUtil.JwtUtil;
import com.tracker_application.tracker.model.JwtRequest;
import com.tracker_application.tracker.model.JwtResponse;
import com.tracker_application.tracker.model.ResponseModel;
import com.tracker_application.tracker.model.User;
import com.tracker_application.tracker.repository.DaoRepository;
import com.tracker_application.tracker.repository.UserRepository;

@Service
public class JwtService implements UserDetailsService {
    @Autowired
    private UserRepository userReop;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private DaoRepository daoRepository;

    // public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
    // String userId = jwtRequest.getUserId();
    // String userPassword = jwtRequest.getPassword();
    // User user = userReop.findAllUserByUserId(userId);
    // if (user.getPassword() != null) {
    // authenticate(userId, userPassword);
    // final UserDetails userDetaisl = loadUserByUsername(userId);
    // String newGeneratedToken = jwtUtil.generateToken(userDetaisl);
    // // User user = userReop.findAllUserByUserId(userId);
    // return new JwtResponse(user, newGeneratedToken);
    // } else {
    // ResponseModel responseModel = daoRepository.resetUserPassword(userId,
    // encoder.encode(userPassword));
    // if (responseModel.getErrorCode().equalsIgnoreCase("SUCCESS")) {
    // authenticate(userId, userPassword);
    // final UserDetails userDetaisl = loadUserByUsername(userId);
    // String newGeneratedToken = jwtUtil.generateToken(userDetaisl);

    // return new JwtResponse(user, newGeneratedToken);
    // } else {
    // throw new InternalError("not able to update the password");
    // }
    // }
    // }

    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
        String userId = jwtRequest.getUserId();
        String userPassword = jwtRequest.getPassword();
        User user = userReop.findAllUserByUserId(userId);

        if (user != null) {
            if (user.getPassword() != null) {
                // User has a password, reset the password with the new one
                ResponseModel responseModel = daoRepository.resetUserPassword(userId, encoder.encode(userPassword));

                if (responseModel.getErrorCode().equalsIgnoreCase("SUCCESS")) {
                    // Authentication with the updated password
                    authenticate(userId, userPassword);

                    final UserDetails userDetaisl = loadUserByUsername(userId);
                    String newGeneratedToken = jwtUtil.generateToken(userDetaisl);

                    // Return the JWT response
                    return new JwtResponse(user, newGeneratedToken);
                } else {
                    throw new InternalError("Not able to update the password");
                }
            } else {
                // User doesn't have a password, you can set the password directly
                user.setPassword(encoder.encode(userPassword));
                userReop.save(user);

                // Authentication with the updated password
                authenticate(userId, userPassword);

                final UserDetails userDetaisl = loadUserByUsername(userId);
                String newGeneratedToken = jwtUtil.generateToken(userDetaisl);

                // Return the JWT response
                return new JwtResponse(user, newGeneratedToken);
            }
        } else {
            throw new InternalError("User not found");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userReop.findById(username).get();
        if (user != null) {
            return new org.springframework.security.core.userdetails.User(user.getId(), user.getPassword(),
                    getAuthorities(user));
        } else {
            throw new UsernameNotFoundException("Username is not valid");
        }
    }

    private Set<SimpleGrantedAuthority> getAuthorities(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getUserRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRole()));
        });
        return authorities;
    }

    private void authenticate(String userId, String userPassword) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userId, userPassword));
        } catch (DisabledException e) {
            throw new Exception("User is disabled");
        } catch (BadCredentialsException e) {
            throw new Exception("Bad credentials from user");
        }
    }

}