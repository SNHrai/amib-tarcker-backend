package com.tracker_application.tracker.model;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class DataRequest {
    private String name;
    private Timestamp date;
    private String leave;

    public DataRequest() {
    }

    public DataRequest(String name, Timestamp date, String leave) {
        this.name = name;
        this.date = date;
        this.leave = leave;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getLeave() {
        return leave;
    }

    public void setLeave(String leave) {
        this.leave = leave;
    }

    @Override
    public String toString() {
        return "DataRequest [name=" + name + ", date=" + date + ", leave=" + leave + "]";
    }

}
