package com.tracker_application.tracker.model;

public enum Error {

    IVALID_EMAIL("EMIAL_IS_INVALID"),
    ERROR_IN_EMAIL_METHOD("SOMETHING_WENT_WRONG");

    private String errMes;

    private Error(String errMes) {
        this.errMes = errMes;
    }

    public String errMess() {
        return errMes;
    }

}
