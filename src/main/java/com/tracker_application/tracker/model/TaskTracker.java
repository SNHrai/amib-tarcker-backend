package com.tracker_application.tracker.model;

import java.util.Date;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EMP_MASTER_TRACKER")
public class TaskTracker {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TASK_NUMBER",nullable = false, updatable = false)
    private Long trackerId;

    @Column(name = "LOGIN_TIME")
    private Date date;

    @Column(name="EMP_LEAVES")
    private String empLeaves;

    @Column(name = "TASK_DETAILS", length = 5000)
    private String taskDetails;
  
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EMP_ID", nullable = false)
    @JsonIgnore
    private User user;

}
