package com.tracker_application.tracker.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.tracker_application.tracker.constants.ApiConstants;
import com.tracker_application.tracker.model.EmployeeInfoResponse;
import com.tracker_application.tracker.model.LeaveResponse;
import com.tracker_application.tracker.model.LeavesModel;
import com.tracker_application.tracker.model.ResponseModel;
import com.tracker_application.tracker.model.User;
import com.tracker_application.tracker.model.UserResponseModel;

import oracle.jdbc.OracleTypes;

@Repository
public class DaoRepository {

    String TAG = "DaoRepository";

    private static final Logger logger = LoggerFactory.getLogger(DaoRepository.class);

    Connection connection;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public ResponseModel userValidationDao(String userId, String password) {
        logger.info("user id", userId, password);
        ResponseModel arrayResponseModel = new ResponseModel();
        UserResponseModel userResponseModel = new UserResponseModel();
        getConnection();
        CallableStatement callableStatement = null;

        String callProcedure = "{call EMP_MASTER_USER(?,?,?,?,?,?,?)}";
        try {
            callableStatement = connection.prepareCall(callProcedure);
            callableStatement.setString(1, userId);
            callableStatement.setString(2, password);
            callableStatement.registerOutParameter(3, java.sql.Types.VARCHAR);
            callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR);
            callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
            callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
            callableStatement.registerOutParameter(7, java.sql.Types.INTEGER);
            callableStatement.executeUpdate();

            userResponseModel.setUserName(callableStatement.getString(5));
            userResponseModel.setEmployeeRole(callableStatement.getString(6));
            userResponseModel.setVerticleHeadId(callableStatement.getInt(7));

            arrayResponseModel.setData(userResponseModel);
            arrayResponseModel.setErrorMsg(callableStatement.getString(3));
            arrayResponseModel.setErrorCode(callableStatement.getString(4));

        } catch (SQLException exception) {
            arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
            arrayResponseModel.setErrorMsg(exception.toString());
            exception.printStackTrace();
        } finally {
            CloseConnection(callableStatement, connection);
        }
        return arrayResponseModel;
    }

    public User getUserByPassword(String responsePassword) {
        logger.info("user id", responsePassword);
        User user = new User();

        getConnection();
        CallableStatement callableStatement = null;
        String callProcedure = "{call EMP_UPDATE_PASSWORD(?,?)}";
        try {
            callableStatement = connection.prepareCall(callProcedure);
            callableStatement.setString(1, responsePassword);
            callableStatement.registerOutParameter(2, OracleTypes.CURSOR);
            callableStatement.executeUpdate();

            ResultSet rs = (ResultSet) callableStatement.getObject(2);
            while (rs.next()) {
                user.setEmpName(rs.getString(2));
                user.setEmpRole(rs.getString(3));
                user.setMobileNumber(rs.getString(4));
            }

        } catch (Exception e) {
            logger.info(TAG + "getDescription :: exception :" + e);
            e.printStackTrace();
        } finally {
            CloseConnection(callableStatement, connection);
        }

        return user;
    }

    public ResponseModel leaveDataDao() {
        logger.info(TAG, "ArrayResponseModel list of leaves");

        getConnection();
        ResponseModel arrayResponseModel = new ResponseModel();
        CallableStatement callableStatement = null;
        String callProcedure = "{call EMP_MASTER_LEAVES(?,?,?)}";
        ArrayList<LeavesModel> leave = new ArrayList<LeavesModel>();
        try {
            callableStatement = connection.prepareCall(callProcedure);
            logger.info(TAG, "callable statement executing");
            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.registerOutParameter(2, java.sql.Types.VARCHAR);
            callableStatement.registerOutParameter(3, java.sql.Types.VARCHAR);
            callableStatement.executeUpdate();

            ResultSet rs = (ResultSet) callableStatement.getObject(1);
            while (rs.next()) {
                LeavesModel leavesModel = new LeavesModel();
                leavesModel.setId(rs.getInt(1));
                leavesModel.setAllLeaves(rs.getString(2));
                leave.add(leavesModel);
            }
            arrayResponseModel.setDataArray(leave);
        } catch (SQLException e) {
            arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
            arrayResponseModel.setErrorMsg(e.toString());
            logger.info(TAG + "getDescription :: exception :" + e);
            e.printStackTrace();
        } finally {
            CloseConnection(callableStatement, connection);
        }
        return arrayResponseModel;
    }

    public ResponseModel getAllEmployeeData(String leaves) {
        logger.info(TAG, "getAllEmployeeData");
        ResponseModel arrayResponseModel = new ResponseModel();
        ArrayList<EmployeeInfoResponse> list = new ArrayList<>();
        getConnection();
        CallableStatement callableStatement = null;
        String callProcedure = "{call EMP_MASTER_DAILY_UPDATE(?,?)}";

        try {
            callableStatement = connection.prepareCall(callProcedure);
            logger.info(TAG, "callable statement executing");
            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.setString(2, leaves);
            // callableStatement.registerOutParameter(3, java.sql.Types.VARCHAR);
            // callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR);

            ResultSet rs = (ResultSet) callableStatement.getObject(1);
            logger.info("rs statement", rs);
            while (rs.next()) {
                EmployeeInfoResponse data = new EmployeeInfoResponse();
                data.setName(rs.getString(1));
                data.setTaskDetails(rs.getString(2));
                list.add(data);

            }
            // arrayResponseModel.setErrorCode(callableStatement.getString(3));
            // arrayResponseModel.setErrorMsg(callableStatement.getString(4));

            arrayResponseModel.setDataArray(list);

        } catch (Exception e) {
            arrayResponseModel.setErrorCode(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
            arrayResponseModel.setErrorMsg(e.toString());
            logger.info(TAG + "getDescription :: exception :" + e);
            e.printStackTrace();
        } finally {
            CloseConnection(callableStatement, connection);
        }

        return arrayResponseModel;
    }

    private Connection getConnection() {
        try {
            if (null == connection || connection.isClosed()) {
                connection = jdbcTemplate.getDataSource().getConnection();
            }
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    private void CloseConnection(CallableStatement callableStatement, Connection connection) {
        try {
            if (callableStatement != null) {
                callableStatement.close();
            }

            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
