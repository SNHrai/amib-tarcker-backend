package com.tracker_application.tracker.dto;

import lombok.Data;

@Data
public class PasswordResetRequestDto {
    private String phoneNumber; //destination
    private String userName;
    private String Otp;
}
