package com.tracker_application.tracker.model;

public enum EmployeeData {
    EMPLOYEE1("Riddhi Madhu"),
    EMPLOYEE2("Lalit Tanver"),
    EMPLOYEE3("Vijay Yennam"),
    EMPLOYEE4("Vaibhav Jadhav"),
    EMPLOYEE5("Niraj Bhiwasane"),
    EMPLOYEE6("Shubham Rai"),
    EMPLOYEE7("Vishal Patil"),
    EMPLOYEE8("Sumathy"),
    EMPLOYEE9("Subodh Bhoir"),
    EMPLOYEE10("");
    // EMPLOYEE11(""),
    // EMPLOYEE12(""),
    // EMPLOYEE13(""),
    // EMPLOYEE14(""),
    // EMPLOYEE15(""),
    // EMPLOYEE16(""),
    // EMPLOYEE17("");

    private String employeeName;

    private EmployeeData(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    
    
}
