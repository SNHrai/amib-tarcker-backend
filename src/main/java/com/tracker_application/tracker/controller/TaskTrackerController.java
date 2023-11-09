package com.tracker_application.tracker.controller;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tracker_application.tracker.exception.ResourceNotFoundException;
import com.tracker_application.tracker.model.DataRequest;
import com.tracker_application.tracker.model.EmployeeInfoResponse;
import com.tracker_application.tracker.model.EmployeeRequestModel;
import com.tracker_application.tracker.model.LeaveResponse;
import com.tracker_application.tracker.model.LeavesModel;
import com.tracker_application.tracker.model.ResponseModel;
import com.tracker_application.tracker.model.TaskTracker;
import com.tracker_application.tracker.model.User;
import com.tracker_application.tracker.repository.TaskTrackerRepository;
import com.tracker_application.tracker.repository.UserRepository;
import com.tracker_application.tracker.service.UserServiceApplication;

@RestController
@RequestMapping("/task")
public class TaskTrackerController {

  private String errCD = "NO_EMPLOYEE_FOUND";
  private String errMSG = "No employee found!";
  private EntityManagerFactory managerFactory;
  private UserRepository userRepository;
  private TaskTrackerRepository trackerRepository;
  private UserServiceApplication serviceApplication;

  private static Logger logger = LoggerFactory.getLogger(TaskTrackerController.class);

  @Autowired
  public TaskTrackerController(UserRepository employeeRepository, TaskTrackerRepository trackerRepository,
      EntityManagerFactory managerFactory, UserServiceApplication serviceApplication) {
    this.userRepository = employeeRepository;
    this.trackerRepository = trackerRepository;
    this.managerFactory = managerFactory;
    this.serviceApplication = serviceApplication;
  }

  // @PostMapping("/tasktracker/{employeeId}")
  // public ResponseEntity<TaskTracker> createTask(@PathVariable(value =
  // "employeeId") Long employeeId, @RequestBody TaskTracker taskTracker) {
  // TaskTracker tracker = userRepository.findById(employeeId).map(user -> {
  // taskTracker.setUser(user);
  // taskTracker.setDate(new Date());
  // return trackerRepository.save(taskTracker);
  // }).orElseThrow(() -> new ResourceNotFoundException("Not found employee with
  // id = " + employeeId));

  // return new ResponseEntity<>(tracker, HttpStatus.CREATED);
  // }

  // @PostMapping("enterTask/{employeeId}")
  // public ResponseEntity<TaskTracker> createTask(@PathVariable(value =
  // "employeeId") Long employeeId, @RequestBody TaskTracker taskTracker) {
  // TaskTracker tracker = userRepository.findById(employeeId).map(user -> {
  // taskTracker.setUser(user);
  // taskTracker.setDate(new Date());
  // return trackerRepository.save(taskTracker);
  // }).orElseThrow(() -> new ResourceNotFoundException("Not found employee with
  // id = " + employeeId));

  // return new ResponseEntity<>(tracker, HttpStatus.CREATED);
  // }

  @GetMapping("/headId/{verticleHeadId}")
  public ResponseEntity<List<TaskTracker>> findAllByVerticleHeadId(
      @PathVariable(value = "verticleHeadId") int verticalId) {
    EntityManager manager = managerFactory.createEntityManager();
    Query query = manager.createQuery("select " + "e.empName, " + "t.empLeaves from User e, "
        + "TaskTracker t where e.id = t.user " + "and e.verticalId = " + verticalId + "");
    // if(employeeRepository.findByVerticleHeadId(verticleHeadId).equals(null)){
    // throw new ResourceNotFoundException("Not found with head ID : " +
    // verticleHeadId);
    // }
    List<TaskTracker> trackers = (List<TaskTracker>) query.getResultList();
    manager.close();
    return new ResponseEntity(trackers, HttpStatus.OK);
  }

  @GetMapping("/bydate/{date}/{dateSecond}")
  public ResponseEntity<List<TaskTracker>> findAllByDate(
      @PathVariable(value = "date") @DateTimeFormat(pattern = "dd-MMM-yy") Date date,
      @PathVariable(value = "dateSecond") @DateTimeFormat(pattern = "dd-MMM-yy") Date dateSecond)
      throws ParseException {
    // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");

    // date = Calendar.getInstance().getTime();

    // dateFormat.format(date);
    EntityManager manager = managerFactory.createEntityManager();
    Query query = manager
        .createQuery("select " + "e.empName, " + "t.empLeaves," + "t.date from User e, "
            + "TaskTracker t where e.id = t.user "
            + "and t.date >= '" + dateFormat.format(date) + "' and t.date <= '" + dateFormat.format(dateSecond)
            + "' ORDER BY t.date DESC ");
    // if(employeeRepository.findByVerticleHeadId(verticleHeadId).equals(null)){
    // throw new ResourceNotFoundException("Not found with head ID : " +
    // verticleHeadId);
    // }
    List<TaskTracker> trackers = (List<TaskTracker>) query.getResultList();
    manager.close();
    // SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd");
    // formatter6.;
    return new ResponseEntity(trackers, HttpStatus.OK);

  }

  @GetMapping("/currentdate")
  public ResponseEntity<List<TaskTracker>> getTaskBycurrentDate() throws ParseException {
    // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
    // date = Calendar.getInstance().getTime();

    // dateFormat.format(date);
    EntityManager manager = managerFactory.createEntityManager();
    Query query = manager.createQuery("select " + "e.empName, " + "t.empLeaves, " + "t.date from User e, "
        + "TaskTracker t where e.id = t.user " + "and to_char(t.date, 'dd-Mon-yy') = to_char(sysdate, 'dd-Mon-yy')");
    // if(employeeRepository.findByVerticleHeadId(verticleHeadId).equals(null)){
    // throw new ResourceNotFoundException("Not found with head ID : " +
    // verticleHeadId);
    // }
    List<TaskTracker> trackers = (List<TaskTracker>) query.getResultList();
    manager.close();
    // SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd");
    // formatter6.;
    return new ResponseEntity(trackers, HttpStatus.OK);

  }


  @GetMapping("/leaves")
  public ResponseEntity<List<LeavesModel>> getAllLeavesData() {
    List<LeavesModel> leavesModels = serviceApplication.getAllLeaves();
    return new ResponseEntity(leavesModels, HttpStatus.OK);
  }

  @PostMapping("/employeeinfo")
  public ResponseEntity<ResponseModel> getEmployeeData(@RequestBody LeaveResponse response) {
    String leaves = response.getLeaves();
    // ResponseModel data = serviceApplication.getAllData(leaves);
    EntityManager manager = managerFactory.createEntityManager();
    ResponseModel model = new ResponseModel();
    Query query = manager.createQuery("select " + "e.empName, " + "t.empLeaves from User e, "
        + "TaskTracker t where e.id = t.user " + "and t.empLeaves = '" + leaves
        + "' and to_char(t.date, 'dd-Mon-yy') = to_char(sysdate,'dd-Mon-yy')");
    ArrayList<EmployeeInfoResponse> trackers = (ArrayList<EmployeeInfoResponse>) query.getResultList();
    if (trackers != null || !trackers.isEmpty() || trackers.size() != 0) {
      model.setDataArray(trackers);
    } else {
      new ResourceNotFoundException("Employee Not Found");
      model.setErrorCode(errCD);
      model.setErrorMsg(errMSG);
    }
    manager.close();
    return new ResponseEntity(model, HttpStatus.FOUND);

  }

  // @PostMapping("/updateData")
  // public ResponseEntity<ResponseModel> updateEmployeeData(@RequestBody
  // DataRequest dataRequest) {
  // // ResponseModel responseModel =
  // serviceApplication.updateTimeAndDate(dataRequest);
  // // return new ResponseEntity<ResponseModel>(responseModel, HttpStatus.OK);
  // }

  // @PostMapping("/update/userdata")
  // public boolean updateLoginDate(@RequestBody DataRequest dataRequest) {
  // String id;
  // boolean isDataUpdated = false;
  // User user = userRepository.findByUserName(dataRequest.getName());
  // if (user != null) {
  // isDataUpdated = serviceApplication.updateUserFromDb(dataRequest);
  // }

  // return isDataUpdated;

  // }

  // @PostMapping("/update/userdata")
  // public boolean updateUserLoginDateAndTime(@RequestBody DataRequest
  // dataRequest) throws ParseException {

  // String username = dataRequest.getName();
  // String leaves = dataRequest.getLeave();
  // String id = userRepository.findUserIdByUsername(username);
  // Date date = dataRequest.getDate();

  // // Timestamp timestamp = serviceApplication.convertStringToTimestamp(date,
  // // format);
  // // java.sql.Date date1 = serviceApplication.convertUtilToSqlDate(date);
  // // Timestamp timestamp = serviceApplication.convertUtilToTimestamp(date);
  // // String formatedDate = serviceApplication.dateFormatConverter(date);
  // // logger.info("time_stamp", timestamp.toString());
  // // logger.info("sql_date", date1.toString());
  // if (id != null) {
  // EntityManager manager = managerFactory.createEntityManager();
  // EntityTransaction transaction = manager.getTransaction();

  // try {
  // transaction.begin();

  // Query query = manager.createQuery(
  // "UPDATE TaskTracker " +
  // "SET date = :newDate, empLeaves = :newLeaves " +
  // "WHERE emp_id = :id AND to_char(date, 'dd-Mon-yy') =
  // to_char(sysdate,'dd-Mon-yy')");

  // query.setParameter("newDate", date);
  // query.setParameter("newLeaves", leaves);
  // query.setParameter("id", id);

  // int updatedCount = query.executeUpdate();

  // transaction.commit();

  // return updatedCount > 0;
  // } catch (Exception e) {
  // if (transaction != null && transaction.isActive()) {
  // transaction.rollback();
  // }
  // e.printStackTrace();
  // return false;
  // } finally {
  // manager.close();
  // }

  // } else {
  // return false;
  // }
  // }

  // Your UserRepository

  // @PostMapping("/update/userdata")
  // public boolean updateUserLoginDateAndTime(
  // @RequestBody DataRequest dataRequest) {

  // String username = dataRequest.getName();
  // String leaves = dataRequest.getLeave();
  // String id = userRepository.findUserIdByUsername(username);
  // Timestamp loginDate = dataRequest.getDate(); // Current date and time

  // if (id != null) {
  // try {
  // EntityManager manager = managerFactory.createEntityManager();
  // EntityTransaction transaction = manager.getTransaction();

  // try {
  // transaction.begin();

  // Query query = manager.createQuery(
  // "UPDATE TaskTracker " +
  // "SET date = :newDate, empLeaves = :newLeaves " +
  // "WHERE emp_id = :id AND to_char(date, 'dd-Mon-yy') =
  // to_char(sysdate,'dd-Mon-yy')");

  // query.setParameter("newDate", loginDate); // Convert Date to Timestamp
  // query.setParameter("newLeaves", leaves);
  // query.setParameter("id", id);

  // int updatedCount = query.executeUpdate();

  // transaction.commit();

  // return updatedCount > 0;
  // } catch (Exception e) {
  // if (transaction != null && transaction.isActive()) {
  // transaction.rollback();
  // }
  // e.printStackTrace();
  // return false;
  // } finally {
  // manager.close();
  // }

  // } catch (Exception e) {
  // e.printStackTrace();
  // return false;
  // }
  // } else {
  // return false;
  // }
  // }
  
  @PostMapping("/update/userdata")
  public boolean updateUserLoginDateAndTime(
      @RequestBody DataRequest dataRequest) {

    String username = dataRequest.getName();
    String leaves = dataRequest.getLeave();
    String id = userRepository.findUserIdByUsername(username);
    Timestamp loginDate = dataRequest.getDate(); // Current date and time

    if (id != null) {
      try {
        EntityManager manager = managerFactory.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();

        try {
          transaction.begin();

          Query query = manager.createQuery(
              "UPDATE TaskTracker " +
                  "SET empLeaves = :newLeaves " +
                  "WHERE emp_id = :id AND to_char(date, 'dd-Mon-yy') = to_char(sysdate,'dd-Mon-yy')");

          // query.setParameter("newDate", loginDate); // Convert Date to Timestamp
          query.setParameter("newLeaves", leaves);
          query.setParameter("id", id);

          int updatedCount = query.executeUpdate();

          transaction.commit();

          return updatedCount > 0;
        } catch (Exception e) {
          if (transaction != null && transaction.isActive()) {
            transaction.rollback();
          }
          e.printStackTrace();
          return false;
        } finally {
          manager.close();
        }

      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    } else {
      return false;
    }
  }

  // @GetMapping("employeeByLeaves/{empLeaves}")
  // public ResponseEntity<List<?>> getAllEmployeeByLeaves(@PathVariable(value =
  // "empLeaves") String leaves) {
  // try {
  // List<TaskTracker> listByDate = trackerRepository.findByCurrentDate();

  // List<TaskTracker> list = listByDate.stream()
  // .filter(x -> x.getEmpLeaves().equals(leaves))
  // .collect(Collectors.toList());

  // List<?> list2 = list.stream().map(x ->
  // userRepository.findAllById(x.getUser())).collect(Collectors.toList());
  // return new ResponseEntity<>(list2, HttpStatus.OK);
  // } catch (Exception exception) {
  // logger.error("EXCEPTION IN TRACKER CONTROLLER", exception);
  // return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
  // }
  // }

  @QueryMapping
  public List<TaskTracker> getAllTaskTracker() {
    return trackerRepository.findAll();
  }

}
