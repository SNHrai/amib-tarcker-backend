package com.tracker_application.tracker.model;


public class UserResponseModel {
    private String userName;
    private String employeeRole;
    private int verticleHeadId;

    public UserResponseModel() {
    }

    public UserResponseModel(String userName, String employeeRole, int verticleHeadId
           ) {
        this.userName = userName;
        this.employeeRole = employeeRole;
        this.verticleHeadId = verticleHeadId;
     
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmployeeRole(String employeeRole) {
        this.employeeRole = employeeRole;
    }
    public void setVerticleHeadId(int verticleHeadId) {
        this.verticleHeadId = verticleHeadId;
    }


    public String getUserName() {
        return userName;
    }

    public String getEmployeeRole() {
        return employeeRole;
    }

    public int getVerticleHeadId() {
        return verticleHeadId;
    }

}
