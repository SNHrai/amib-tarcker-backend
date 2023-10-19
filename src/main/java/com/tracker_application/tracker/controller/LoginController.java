package com.tracker_application.tracker.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tracker_application.tracker.TrackerApplication;
import com.tracker_application.tracker.dto.PasswordResetRequestDto;
import com.tracker_application.tracker.model.AuthCredentialRequest;
import com.tracker_application.tracker.model.Mail;
import com.tracker_application.tracker.model.MailRequestBody;
import com.tracker_application.tracker.model.MessageDetails;
import com.tracker_application.tracker.model.ResponseModel;
import com.tracker_application.tracker.model.User;
import com.tracker_application.tracker.repository.UserRepository;
import com.tracker_application.tracker.service.EmailSenderService;
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
    int[] num = { 1, 2, 3, 4, 5, 6 };

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    public LoginController(UserServiceApplication serviceApplication,
            EntityManagerFactory entityManagerFactory, UserRepository userRepository,
            EmailSenderService emailSenderService, TwilioOtpService otpService) {

        this.serviceApplication = serviceApplication;
        this.managerFactory = entityManagerFactory;
        this.userRepository = userRepository;
        this.emailService = emailSenderService;
        this.otpService = otpService;

    }

    private String USER_NOT_FOUND_MSG = "USER NOT FOUND";

    @PostMapping("/login")
    public ResponseEntity<ResponseModel> loginDemo(@RequestBody AuthCredentialRequest authCredentialRequest) {
        ResponseModel arrayResponseModel = serviceApplication.loginUserValidation(authCredentialRequest);
        return new ResponseEntity(arrayResponseModel, HttpStatus.OK);
    }

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

    // @PostMapping("/sms/users")
    // public boolean postUserSMS(@RequestBody MessageDetails details) {
    // try {
    // MessageDetails messageDetails = new MessageDetails();
    // String messBody = "Your Otp mess";
    // messageDetails.setMessageBody(messBody);
    // messageDetails.setListId(details.getListId());
    // sendSmsService.sendOtpSMStoUsers(messageDetails);
    // return true;

    // } catch (Exception e) {
    // return false;
    // }
    // }

    // @PostMapping("/api/sms1000")
    // public ResponseEntity<String> sendSMStoUpto1000Numbers(@RequestBody
    // MessageDetails messageDetails) {

    // SmsApi smsApi = new SmsApi(clickSendConfig);

    // SmsMessage smsMessage = new SmsMessage();
    // smsMessage.body(messageDetails.getMessageBody());
    // smsMessage.listId(messageDetails.getListId());
    // smsMessage.source(messageDetails.getSendingSource());

    // List<SmsMessage> smsMessageList = new ArrayList<>();
    // smsMessageList.add(smsMessage);

    // // SmsMessageCollection | SmsMessageCollection model
    // SmsMessageCollection smsMessages = new SmsMessageCollection();
    // smsMessages.messages(smsMessageList);
    // try {
    // String result = smsApi.smsSendPost(smsMessages);
    // return new ResponseEntity<>(result, HttpStatus.OK);
    // } catch (ApiException e) {
    // e.printStackTrace();
    // }

    // return new ResponseEntity<>("Exception when calling SmsApi#smsSendPost",
    // HttpStatus.BAD_REQUEST);
    // }

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
