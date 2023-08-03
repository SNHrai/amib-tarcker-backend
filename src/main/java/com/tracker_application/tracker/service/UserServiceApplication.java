package com.tracker_application.tracker.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tracker_application.tracker.exception.ResourceNotFoundException;
import com.tracker_application.tracker.model.AuthCredentialRequest;
import com.tracker_application.tracker.model.EmployeeInfoResponse;
import com.tracker_application.tracker.model.LeaveResponse;
import com.tracker_application.tracker.model.LeavesModel;
import com.tracker_application.tracker.model.PasswordResponse;
import com.tracker_application.tracker.model.ResponseModel;
import com.tracker_application.tracker.model.TaskTracker;
import com.tracker_application.tracker.model.User;
import com.tracker_application.tracker.repository.DaoRepository;
import com.tracker_application.tracker.repository.UserRepository;

@Service
public class UserServiceApplication {

  private EntityManagerFactory managerFactory;
  private DaoRepository daoRepository;
  private UserRepository userRepository;

  private String errCD = "NO_EMPLOYEE_FOUND";
  private String errMSG = "No employee found!";
  private static final Logger logger = LoggerFactory.getLogger(UserServiceApplication.class);

  private String TAG = "user service application";

  @Autowired
  public UserServiceApplication(DaoRepository daoRepository, UserRepository userRepository) {
    this.daoRepository = daoRepository;
    this.userRepository = userRepository;
  }

  public ResponseModel loginUserValidation(AuthCredentialRequest userIdAuthCredentialRequest) {
    ResponseModel arrayResponseModel = new ResponseModel();
    logger.info(TAG, "user_id", userIdAuthCredentialRequest);
    if (userIdAuthCredentialRequest != null) {

      arrayResponseModel = daoRepository.userValidationDao(userIdAuthCredentialRequest.getUserId(),
          userIdAuthCredentialRequest.getPassword());
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

  public ResponseModel getEmployeeInfo(String leaves) {
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

}
