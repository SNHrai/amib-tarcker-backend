package com.tracker_application.tracker.controller;

import java.util.Date;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tracker_application.tracker.exception.ResourceNotFoundException;
import com.tracker_application.tracker.model.PasswordResponse;
import com.tracker_application.tracker.model.TaskTracker;
import com.tracker_application.tracker.repository.TaskTrackerRepository;
import com.tracker_application.tracker.repository.UserRepository;
import com.tracker_application.tracker.service.UserServiceApplication;


@RestController
@RequestMapping("/user")
public class UserController {

    private EntityManagerFactory managerFactory;
    private UserRepository userRepository;
    private TaskTrackerRepository trackerRepository;
    private UserServiceApplication serviceApplication;

    @Autowired
    public UserController(UserRepository employeeRepository, TaskTrackerRepository trackerRepository,
            EntityManagerFactory managerFactory, UserServiceApplication serviceApplication) {
        this.userRepository = employeeRepository;
        this.trackerRepository = trackerRepository;
        this.managerFactory = managerFactory;
        this.serviceApplication = serviceApplication;
    }

    @PostMapping("/{employeeId}")
    public ResponseEntity<TaskTracker> createTask(@PathVariable(value = "employeeId") String employeeId,
            @RequestBody TaskTracker taskTracker) {
            TaskTracker tracker =(TaskTracker) userRepository.findById(employeeId).map(user -> {
            taskTracker.setUser(user);
            taskTracker.setDate(new Date());
            return trackerRepository.save(taskTracker);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found employee with id = " + employeeId));

        return new ResponseEntity<>(tracker, HttpStatus.CREATED);
    }

    @PostMapping("/resetpassword/{employeeId}")
    public boolean getResetPassword(@PathVariable(value = "employeeId") String employeeId, @RequestBody PasswordResponse passwordResponse){
     String password = passwordResponse.getResponsePassword();  
     boolean val =   serviceApplication.passwordChanger(employeeId, password);
     return val;
    }

}
