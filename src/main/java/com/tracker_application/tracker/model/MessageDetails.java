package com.tracker_application.tracker.model;

import lombok.Data;

@Data
public class MessageDetails {
    private String messageBody;
    private int listId;
    private String sendingSource;
    private String smsCampaignName;
}
