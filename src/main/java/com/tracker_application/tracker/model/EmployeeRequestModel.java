package com.tracker_application.tracker.model;

public class EmployeeRequestModel {
    private int id;
    private String leaves;
    
    public EmployeeRequestModel() {
    }

    public EmployeeRequestModel(int id, String leaves) {
        this.id = id;
        leaves = leaves;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLeaves() {
        return leaves;
    }

    public void setLeaves(String leaves) {
        this.leaves = leaves;
    }

   

    
}
