package com.tracker_application.tracker.model;

public class EmployeeInfoResponse {
    String name;
    String taskDetails;

    public EmployeeInfoResponse() {
    }
    
    public EmployeeInfoResponse(String name, String taskDetails) {
        this.name = name;
        this.taskDetails = taskDetails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getTaskDetails() {
        return taskDetails;
    }
    
        public void setTaskDetails(String taskDetails) {
            this.taskDetails = taskDetails;
        }

    @Override
    public String toString() {
        return "EmployeeInfoResponse [name=" + name + ", TaskDetails=" + taskDetails + "]";
    }

    
}
