package com.tracker_application.tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tracker_application.tracker.model.AuthCredentialRequest;
import com.tracker_application.tracker.model.ResponseModel;
import com.tracker_application.tracker.repository.UserRepository;
import com.tracker_application.tracker.service.UserServiceApplication;



@RestController
public class LoginController {

    private UserRepository userRepository;

    private UserServiceApplication serviceApplication;

    

    @Autowired
    public LoginController(UserRepository userRepository, UserServiceApplication serviceApplication) {
        this.userRepository = userRepository;
        this.serviceApplication = serviceApplication;
    }

    private String USER_NOT_FOUND_MSG = "USER NOT FOUND";

    @PostMapping("/login")
    public ResponseEntity<ResponseModel> loginDemo(@RequestBody AuthCredentialRequest authCredentialRequest) {
        ResponseModel arrayResponseModel = serviceApplication.loginUserValidation(authCredentialRequest);
        return new ResponseEntity(arrayResponseModel, HttpStatus.OK);
    }

    // @PostMapping("/login")
    // public UserResponseModel login(@RequestBody AuthCredentialRequest request) {

    // UserResponseModel userResponseModel = new UserResponseModel();

    // if (request.getUserName() != null && request.getPassword() != null) {
    // // User user = userRepository.findByUserName(request.getUserName());
    // User user = userRepository.findByUserNameandPassword(request.getUserName(),
    // request.getPassword());

    // if (user.getUserName() != null) {
    // userResponseModel.setEmployeeRole(user.getEmployeeRole());
    // userResponseModel.setUserName(user.getUserName());
    // userResponseModel.setVerticleHeadId(user.getVerticleHeadId());
    // userResponseModel.setId(user.getId());

    // } else {
    // new ResourceNotFoundException(USER_NOT_FOUND_MSG + request.getUserName());
    // }

    // } else {
    // new ResourceNotFoundException(USER_NOT_FOUND_MSG + request.getUserName());
    // }
    // ;

    // return userResponseModel;
    // }

}
