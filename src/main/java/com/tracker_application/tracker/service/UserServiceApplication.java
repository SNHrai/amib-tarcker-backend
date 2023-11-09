package com.tracker_application.tracker.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tracker_application.tracker.exception.ResourceNotFoundException;
import com.tracker_application.tracker.model.JwtRequest;
import com.tracker_application.tracker.model.DataRequest;
import com.tracker_application.tracker.model.EmployeeData;
import com.tracker_application.tracker.model.EmployeeInfoResponse;
import com.tracker_application.tracker.model.EmployeeNames;
import com.tracker_application.tracker.model.LeaveResponse;
import com.tracker_application.tracker.model.LeavesModel;
import com.tracker_application.tracker.model.PasswordResponse;
import com.tracker_application.tracker.model.ResponseModel;
import com.tracker_application.tracker.model.TaskTracker;
import com.tracker_application.tracker.model.User;
// import com.tracker_application.tracker.model.UserName;
import com.tracker_application.tracker.repository.DaoRepository;
// import com.tracker_application.tracker.repository.MongoUserRepository;
import com.tracker_application.tracker.repository.TaskTrackerRepository;
import com.tracker_application.tracker.repository.TrackerRepository;
import com.tracker_application.tracker.repository.UserRepository;

@Service
public class UserServiceApplication {

  private EntityManagerFactory managerFactory;
  private DaoRepository daoRepository;
  private UserRepository userRepository;
  private TaskTrackerRepository taskTracker;
  private TrackerRepository trackerRepository;
  // private MongoUserRepository mongoUserRepository;
  // private MongoTemplate mongoTemplate;

  private boolean isUpdatedData;

  private String errCD = "NO_EMPLOYEE_FOUND";
  private String errMSG = "No employee found!";
  private static final Logger logger = LoggerFactory.getLogger(UserServiceApplication.class);

  private String TAG = "user service application";

  @Autowired
  public UserServiceApplication(DaoRepository daoRepository, UserRepository userRepository,
      TaskTrackerRepository taskTracker, TrackerRepository trackerRepository) {
    this.daoRepository = daoRepository;
    this.userRepository = userRepository;
    this.taskTracker = taskTracker;
    this.trackerRepository = trackerRepository;
  }

  public ResponseModel loginUserValidation(JwtRequest userIdAuthCredentialRequest) {
    ResponseModel arrayResponseModel = new ResponseModel();

    logger.info(TAG, "user_info", userIdAuthCredentialRequest);

    User user = userRepository.findAllUserByUserId(userIdAuthCredentialRequest.getUserId());
    if (user != null) {
      if (user.getPassword() != null) {
        arrayResponseModel = daoRepository.userValidationDao(userIdAuthCredentialRequest.getUserId(),
            userIdAuthCredentialRequest.getPassword());
      } else {
        arrayResponseModel = daoRepository.resetUserPassword(userIdAuthCredentialRequest.getUserId(),
            userIdAuthCredentialRequest.getPassword());
      }
    }

    return arrayResponseModel;

  }

  public List<LeavesModel> getAllLeaves() {
    ResponseModel arrayResponseModel = daoRepository.leaveDataDao();
    if (arrayResponseModel.getErrorCode() != null || arrayResponseModel.getErrorMsg() != null) {
      new ResourceNotFoundException(arrayResponseModel.getErrorMsg());
    }
    List<LeavesModel> leavesModels = (List) arrayResponseModel.getDataArray();
    return leavesModels;
  }

  public boolean passwordChanger(String id, String password) {
    logger.info(TAG, "passwordChanger", id, password);
    userRepository.resetPassword(id, password);
    User user = daoRepository.getUserByPassword(password);
    if (user.getEmpName() != null) {
      return true;
    } else {
      return false;
    }

  }

  public ResponseModel getAllData(String leaves) {
    logger.info(TAG, "getAllData", leaves);

    // ArrayList<EmployeeInfoResponse> empData = new ArrayList<>();
    ResponseModel responseModel = new ResponseModel();

    if (leaves != null) {
      responseModel = daoRepository.getAllEmployeeData(leaves);
    } else {
      new ResourceNotFoundException("Not found employee  = " + leaves);
    }

    // responseModel.setDataArray(empData);

    return responseModel;
  }

  public boolean updateUserLoginDateAndTime(Date date, String leaves, String id) {
    EntityManager manager = managerFactory.createEntityManager();
    EntityTransaction transaction = manager.getTransaction();

    try {
      transaction.begin();

      Query query = manager.createQuery(
          "UPDATE TaskTracker t " +
              "SET t.date = :newDate, t.empLeaves = :newLeaves " +
              "WHERE t.id = :id AND FUNC('TO_CHAR', t.date, 'dd-Mon-yy') = FUNC('TO_CHAR', CURRENT_DATE, 'dd-Mon-yy')");

      query.setParameter("newDate", date);
      query.setParameter("newLeaves", leaves);
      query.setParameter("id", Long.parseLong(id));

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
  }

  // public boolean updateUserLoginDateAndTime(Date date, String leaves, String
  // id) {

  // DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
  // EntityManager manager = managerFactory.createEntityManager();
  // Query query = manager
  // .createQuery("update " + "TaskTracker t" + "set , " + "t.date = '" + date +
  // "', t.empLeaves = '" + leaves
  // + "' where " + "t.id =" + id + " and to_char(t.date, 'dd-Mon-yy') =
  // to_char(sysdate,'dd-Mon-yy') '");
  // List<TaskTracker> trackers = (List<TaskTracker>) query.getResultList();
  // logger.info(TAG, trackers);
  // manager.close();
  // if (trackers != null) {
  // return true;
  // } else {
  // return false;
  // }
  // }

  public boolean existsByDate(
      @DateTimeFormat(pattern = "dd-MMM-yy") java.util.Date date)

      throws ParseException {
    logger.info(TAG, date);
    DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
    EntityManager manager = managerFactory.createEntityManager();
    Query query = manager
        .createQuery("select " + "e.empName, " + "t.taskDetails," + "t.date from User e, "
            + "TaskTracker t where e.id = t.user "
            + "and t.date >= '" + dateFormat.format(date) + "' ORDER BY t.date DESC ");
    List<TaskTracker> trackers = (List<TaskTracker>) query.getResultList();
    logger.info(TAG, trackers);
    manager.close();
    if (trackers != null) {
      return true;
    } else {
      return false;
    }

  }

  public ResponseModel getEmployeeModelData() {
    ResponseModel responseModel = daoRepository.getEmployeeDataFromDB();
    return responseModel;
  }

  public ResponseModel getEmployeeInfo(String leaves) {
    logger.info(TAG, leaves);
    EntityManager manager = managerFactory.createEntityManager();
    ResponseModel model = new ResponseModel();
    Query query = manager.createQuery("select " + "e.userName, " + "t.taskDetails from User e, "
        + "TaskTracker t where e.id = t.user " + "and t.leaves = " + leaves
        + " and to_char(t.date, 'dd-Mon-yy') = to_char(sysdate,'dd-Mon-yy')");
    ArrayList<EmployeeInfoResponse> trackers = (ArrayList<EmployeeInfoResponse>) query.getResultList();

    if (trackers != null) {
      model.setDataArray(trackers);
    } else {
      new ResourceNotFoundException("Employee Not Found");
      model.setErrorCode(errCD);
      model.setErrorMsg(errMSG);
    }
    manager.close();
    return model;
  }

  // public ResponseModel updateTimeAndDate(DataRequest dataRequest) {

  // // Date date = convertUtilToSqlDate(dataRequest.getDate());
  // // Timestamp timestamp = convertUtilToTimestamp(dataRequest.getDate());

  // logger.info(TAG, date);

  // ResponseModel responseModel = daoRepository.getUpdatedDataFromDb(dataRequest,
  // date);

  // return responseModel;

  // }

  public boolean updateUserFromDb(DataRequest dataRequest) {
    String username = dataRequest.getName();
    // Date date = convertUtilToSqlDate(dataRequest.getDate());
    String employeeId;
    boolean isDataUpdated = false;
    if (username != null) {
      employeeId = userRepository.findUserIdByUsername(username);

      if (employeeId != null) {
        // trackerRepository.updateLoginTime(date, employeeId, username);
        // isDataUpdated = updateUserLoginDateAndTime(date, dataRequest.getLeave(),
        // employeeId);
      }
      // try {
      // isDataUpdated = existsByDate(dataRequest.getDate());
      // } catch (ParseException e) {
      // // TODO Auto-generated catch block
      // e.printStackTrace();
      // }

    }

    return isDataUpdated;
  }
  // public boolean updateUserFromDb(String id, DataRequest dataRequest) {

  // boolean isDataUpdated =
  // trackerRepository.updateLoginTime(dataRequest.getDate(),
  // dataRequest.getLeave(), id);
  // if (isDataUpdated) {
  // isDataUpdated = taskTracker.findByDate(dataRequest.getDate());
  // }

  // return isDataUpdated;
  // }

  public boolean verifyEmail(String email) {
    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
        "[a-zA-Z0-9_+&*-]+)*@" +
        "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
        "A-Z]{2,7}$";

    Pattern pat = Pattern.compile(emailRegex);
    if (email == null)
      return false;
    return pat.matcher(email).matches();
  }

  public String generateRandomNumber(int[] digits, int length) {
    Random random = new Random();
    StringBuilder stringBuilder = new StringBuilder(length);

    for (int i = 0; i < length; i++) {
      int randomIndex = random.nextInt(digits.length);
      stringBuilder.append(digits[randomIndex]);
    }

    return stringBuilder.toString();
  }

  public static java.sql.Date convertUtilToSqlDate(java.util.Date utilDate) {
    if (utilDate == null) {
      return null;
    }
    return new java.sql.Date(utilDate.getTime());
  }

  public static java.sql.Timestamp convertUtilToTimestamp(java.util.Date utilDate) {
    if (utilDate == null) {
      return null;
    }
    return new java.sql.Timestamp(utilDate.getTime());
  }

  public String dateFormatConverter(java.util.Date utilDate) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String formattedDate = sdf.format(utilDate);
    return formattedDate;
  }

  public static Timestamp convertStringToTimestamp(String dateString, String format) throws ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    Date parsedDate = (Date) dateFormat.parse(dateString);
    return new Timestamp(parsedDate.getTime());
  }

  public EmployeeNames getAllTheEmployeeName() {

    EmployeeNames employeeNames = new EmployeeNames();
    List<String> list = Stream.of(EmployeeData.values()).map(EmployeeData::getEmployeeName)
        .collect(Collectors.toList());
    employeeNames.setList(list);
    return employeeNames;
  }

}
