package com.tracker_application.tracker.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tracker_application.tracker.TrackerApplication;
import com.tracker_application.tracker.constants.ApiConstants;
import com.tracker_application.tracker.dto.PasswordResetRequestDto;
import com.tracker_application.tracker.exception.ResourceNotFoundException;
import com.tracker_application.tracker.model.JwtRequest;
import com.tracker_application.tracker.model.JwtResponse;
import com.tracker_application.tracker.model.AuthenticationResponse;
import com.tracker_application.tracker.model.Mail;
import com.tracker_application.tracker.model.MailRequestBody;
import com.tracker_application.tracker.model.MessageDetails;
import com.tracker_application.tracker.model.ResponseModel;
import com.tracker_application.tracker.model.User;
import com.tracker_application.tracker.model.UserResponseModel;
import com.tracker_application.tracker.repository.UserRepository;
import com.tracker_application.tracker.service.EmailSenderService;
import com.tracker_application.tracker.service.JwtService;
import com.tracker_application.tracker.service.TwilioOtpService;
import com.tracker_application.tracker.service.UserServiceApplication;

@RestController
public class LoginController {
    private String TAG = "LoginController";
    private UserServiceApplication serviceApplication;
    private UserRepository userRepository;
    private EntityManagerFactory managerFactory;
    private EmailSenderService emailService;
    private TwilioOtpService otpService;
    private JwtService jwtService;
    int[] num = { 1, 2, 3, 4, 5, 6 };

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    public LoginController(UserServiceApplication serviceApplication,
            EntityManagerFactory entityManagerFactory, UserRepository userRepository,
            EmailSenderService emailSenderService, TwilioOtpService otpService, JwtService jwtService) {

        this.serviceApplication = serviceApplication;
        this.managerFactory = entityManagerFactory;
        this.userRepository = userRepository;
        this.emailService = emailSenderService;
        this.otpService = otpService;
        this.jwtService = jwtService;

    }

    @PostMapping("/authenticate")
    public JwtResponse createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        return jwtService.createJwtToken(jwtRequest);
    }

    private String USER_NOT_FOUND_MSG = "USER NOT FOUND";

    // @PostMapping("/login")
    // public ResponseEntity<ResponseModel> loginDemo(@RequestBody JwtRequest authCredentialRequest) {
    //     ResponseModel arrayResponseModel = serviceApplication.loginUserValidation(authCredentialRequest);
    //     return new ResponseEntity(arrayResponseModel, HttpStatus.OK);
    // }


    @PostMapping("/otp/mess")
    public ResponseEntity<MailRequestBody> sendOtpToUser(@RequestBody MailRequestBody requestBody) {
        Map<String, Object> model = new HashMap<>();

        String email = requestBody.getMailTo();
        String newPassword = null;
        boolean isValidEmail = serviceApplication.verifyEmail(email);

        try {
            if (isValidEmail) {
                Mail mail = new Mail();
                mail.setSubject("Your New Password");
                mail.setFrom("sr35285@gmail.com");
                mail.setMailTo(requestBody.getMailTo());
                userRepository.updateEmailById(email, requestBody.getUserid());
                String password = serviceApplication.generateRandomNumber(num, 3);
                String username = userRepository.findUserNameByUserId(requestBody.getUserid());
                User user = userRepository.findByUserName(username);

                if (username != null) {
                    String[] newUser = username.trim().split(" ");
                    String newUsername = newUser[0];
                    newPassword = newUsername + password;
                } else {
                    newPassword = password;
                }

                serviceApplication.passwordChanger(requestBody.getUserid(), newPassword);

                model.put("username", username);
                model.put("password", newPassword);
                model.put("type", "PASSWORD");
                mail.setProps(model);

                emailService.sendEmail(mail); // For Email Service

                PasswordResetRequestDto dto = new PasswordResetRequestDto();
                if (user != null) {
                    String newMobileNumber = "+91 " + user.getMobileNumber();
                    dto.setPhoneNumber(newMobileNumber);
                    dto.setOtp(newPassword);
                    dto.setUserName(username);
                }
                otpService.sendOtpForPasswordReset(dto); // For Mess Service

                log.info("Password sent successfully.....");
                requestBody.setResp("EMAIL_SENT_SUCCESSFULLY");
            } else {
                log.info(TAG, "Email error");
                requestBody.setResp("INVALID_EMAIL");
            }
        } catch (Exception e) {
            e.printStackTrace();
            requestBody.setResp("SOMETHING_WENT_WRONG_ERROR_IN_SEND_OTP");
            log.error("An error occurred while sending OTP:", e);
            return new ResponseEntity<>(requestBody, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(requestBody, HttpStatus.OK);
    };

    @PostMapping("/retrieve/email/{userId}")
    public ResponseEntity<String> recoverPassword(@PathVariable(value = "userId") String userId) {
        Map<String, Object> model = new HashMap<>();
        try {

            String password = "";
            User user = userRepository.findAllUserByUserId(userId);
            if (user != null) {
                if (user.getPassword() != null) {

                    password = user.getPassword();
                    if (user.getEmail() != null) {
                        Mail mail = new Mail();
                        mail.setMailTo(user.getEmail());
                        mail.setFrom("sr35285@gmail.com");
                        mail.setSubject("Your Recovered password");
                        model.put("username", user.getEmpName());
                        model.put("password", password);
                        model.put("type", "PASSWORD");
                        mail.setProps(model);
                        emailService.sendEmail(mail);
                    }
                    return new ResponseEntity<String>(password, HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>("NO PASSWORD FOUND FOR THIS ID PLEASE CREATE ONE",
                            HttpStatus.OK);
                }

            } else {
                return new ResponseEntity<String>(password, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<String>(e.toString(), HttpStatus.BAD_REQUEST);
        }
    }

}
