package com.tracker_application.tracker.model;

public class LeavesModel {
    private int id;
    private String allLeaves;

    public LeavesModel() {
    }

    public LeavesModel(String allLeaves, int id) {
        this.allLeaves = allLeaves;
        this.id= id;
    }

    public String getAllLeaves() {
        return allLeaves;
    }

    public void setAllLeaves(String allLeaves) {
        this.allLeaves = allLeaves;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    

    
}
