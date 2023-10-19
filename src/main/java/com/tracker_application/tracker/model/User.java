package com.tracker_application.tracker.model;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
@Table(name = "EMP_MASTER")
public class User {

    @Id
    @Column(name = "EMP_ID", length = 55, unique = true, nullable = false)
    private String id;

    @Column(name = "EMP_NAME")
    private String empName;

    @Column(name = "EMP_NUMBER")
    private String mobileNumber;

    @Column(name = "VERTICAL_HEAD_ID")
    private int verticalId;

    @Column(name = "EMP_PASSWORD")
    private String password;

    @OneToMany(mappedBy = "user")
    private List<TaskTracker> trackers;

    @OneToMany(mappedBy = "user")
    private Set<UserRole> userRosle;

}
