package com.tracker_application.tracker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tracker_application.tracker.config.TwilioConfig;
import com.tracker_application.tracker.dto.PasswordResetRequestDto;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class TwilioOtpService {

    private static Logger logger = LoggerFactory.getLogger(TwilioOtpService.class);

    @Autowired
    private TwilioConfig config;

    public void sendOtpForPasswordReset(PasswordResetRequestDto requestDto) {
        try {
            PhoneNumber to = new PhoneNumber(requestDto.getPhoneNumber());
            PhoneNumber from = new PhoneNumber(config.getTrailNumber());
            String otp = requestDto.getOtp();
            String otpMess = "Hi," + requestDto.getUserName() + " your Otp is " + otp + " Thank You!!";
            Message message = Message.creator(
                    to, // to
                    from, // from
                    otpMess)
                    .create();

            logger.info(StatusType.SUCCESS.mess);
        } catch (Exception e) {
            logger.info(StatusType.FAILED.mess, e);
        }
    }

    public enum StatusType {
        SUCCESS("Successfully sent the sms"),
        FAILED("Failed to sent the sms");

        private String mess;

        private StatusType(String mess) {
            this.mess = mess;
        }

    }

    // Message message = Message.creator(
    // new com.twilio.type.PhoneNumber("+15559991111"), //to
    // new com.twilio.type.PhoneNumber("+15557771212"), //from
    // "Ahoy! This message was sent from my Twilio phone number!")
    // .create();
    // }
}
