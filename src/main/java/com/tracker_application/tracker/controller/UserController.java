package com.tracker_application.tracker.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tracker_application.tracker.exception.ResourceNotFoundException;
import com.tracker_application.tracker.model.EmployeeInfoResponse;
import com.tracker_application.tracker.model.EmployeeNames;
import com.tracker_application.tracker.model.LeaveResponse;
import com.tracker_application.tracker.model.PasswordResponse;
import com.tracker_application.tracker.model.ResponseModel;
import com.tracker_application.tracker.model.TaskTracker;
// import com.tracker_application.tracker.model.UserName;
import com.tracker_application.tracker.repository.TaskTrackerRepository;
import com.tracker_application.tracker.repository.UserRepository;
//import com.tracker_application.tracker.service.MongoServiceApplication;
import com.tracker_application.tracker.service.UserServiceApplication;

@RestController
@RequestMapping("/user")
public class UserController {

    private EntityManagerFactory managerFactory;
    private UserRepository userRepository;
    private TaskTrackerRepository trackerRepository;
    private UserServiceApplication serviceApplication;
    // private MongoServiceApplication mongoServiceApplication;

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserRepository employeeRepository, TaskTrackerRepository trackerRepository,
            EntityManagerFactory managerFactory, UserServiceApplication serviceApplication) {
        this.userRepository = employeeRepository;
        this.trackerRepository = trackerRepository;
        this.managerFactory = managerFactory;
        this.serviceApplication = serviceApplication;
        // mongoServiceApplication = mongoServiceApplication;
    }

    @PostMapping("/{employeeId}")
    public ResponseEntity<?> createTask(@PathVariable(value = "employeeId") String employeeId,
            @RequestBody TaskTracker taskTracker) {
        int dataExists = trackerRepository.findByEmployeeId(employeeId); // Rename the variable for clarity

        if (dataExists != 1) {
            // Create a new taskTracker and save it
            TaskTracker tracker = userRepository.findById(employeeId)
                    .map(user -> {
                        taskTracker.setUser(user);
                        taskTracker.setDate(new Date());
                        return trackerRepository.save(taskTracker);
                    })
                    .orElseThrow(() -> new ResourceNotFoundException("Not found employee with id = " + employeeId));

            return new ResponseEntity<>(tracker, HttpStatus.CREATED);
        } else {
            // Data already exists, return an appropriate response as a String
            String message = "Data for this employee already exists";
            return new ResponseEntity<>(message, HttpStatus.CONFLICT); // Change HttpStatus.OK to HttpStatus.CONFLICT
        }
    }

    @PostMapping("/resetpassword/{employeeId}")
    public boolean getResetPassword(@PathVariable(value = "employeeId") String employeeId,
            @RequestBody PasswordResponse passwordResponse) {
        String password = passwordResponse.getResponsePassword();
        boolean val = serviceApplication.passwordChanger(employeeId, password);
        return val;
    }

    @GetMapping("/employee/allemployeenames")
    public ResponseEntity<EmployeeNames> getAllTheEmployees() {
        try {

            EmployeeNames names = serviceApplication.getAllTheEmployeeName();
            return new ResponseEntity<EmployeeNames>(names, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // @GetMapping("/username")
    // public ResponseEntity<List<UserName>> getUsername() {
    // try {
    // List<UserName> list = mongoServiceApplication.getAllUser();
    // return new ResponseEntity<List<UserName>>(list, HttpStatus.OK);
    // } catch (Exception e) {
    // return new ResponseEntity<>(HttpStatus.OK);

    // }
    // }

    // @PostMapping("/addUser")
    // public ResponseEntity<UserName> addUsername(@RequestBody UserName userName) {
    // try {
    // UserName list = mongoServiceApplication.addNewUser(userName);
    // return new ResponseEntity<UserName>(list, HttpStatus.OK);
    // } catch (Exception e) {
    // return new ResponseEntity<>(HttpStatus.OK);

    // }
    // }

    @GetMapping("/all/employee/data")
    public ResponseEntity<ResponseModel> getAllEmployeeName() {
        try {

            ResponseModel responseModel = serviceApplication.getEmployeeModelData();
            return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
