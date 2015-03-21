package cn.cjp.base.controller;

import java.util.List;

import cn.cjp.base.model.BaseEntityModel;

/**
 * 
 * @author SucreCui
 *
 */
public class WebResult {
	
	/**
	 * 结果码
	 */
	private int code;

	private List<BaseEntityModel> modelList;
	
	private BaseEntityModel model;
	
	private String message;
	
	private String error;
	
	private String resultUrl;

	/**
	 * @return the modelList
	 */
	public List<BaseEntityModel> getModelList() {
		return modelList;
	}

	/**
	 * @param modelList the modelList to set
	 */
	public void setModelList(List<BaseEntityModel> modelList) {
		this.modelList = modelList;
	}

	/**
	 * @return the model
	 */
	public BaseEntityModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(BaseEntityModel model) {
		this.model = model;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the resultUrl
	 */
	public String getResultUrl() {
		return resultUrl;
	}

	/**
	 * @param resultUrl the resultUrl to set
	 */
	public void setResultUrl(String resultUrl) {
		this.resultUrl = resultUrl;
	}

	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}
}

enum WebResultCode{
	
	SUCCESS(200), ERROR(500);
	
	private int code;
	
	WebResultCode(int code){
		this.setCode(code);
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}
	
	
}
