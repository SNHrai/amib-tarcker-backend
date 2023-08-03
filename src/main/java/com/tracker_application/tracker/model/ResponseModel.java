package com.tracker_application.tracker.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
public class ResponseModel {
    private ArrayList<?> dataArray;
    private UserResponseModel data;
    private String errorMsg;
    private String errorCode;
	public ResponseModel() {
		super();
		
	}
	public ResponseModel(ArrayList<?> dataArray, UserResponseModel data, String errorMsg, String errorCode) {
		super();
		this.dataArray = dataArray;
		this.data = data;
		this.errorMsg = errorMsg;
		this.errorCode = errorCode;
	}
	public ArrayList<?> getDataArray() {
		return dataArray;
	}
	public void setDataArray(ArrayList<?> dataArray) {
		this.dataArray = dataArray;
	}
	public UserResponseModel getData() {
		return data;
	}
	public void setData(UserResponseModel data) {
		this.data = data;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
    
    

}
