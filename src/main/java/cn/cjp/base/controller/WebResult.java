package cn.cjp.base.controller;

import java.util.List;

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

	private List<Object> objectList;
	
	private Object object;
	
	private String message;
	
	private String error;
	
	private String resultUrl;

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

	/**
	 * @return the objectList
	 */
	public List<Object> getObjectList() {
		return objectList;
	}

	/**
	 * @param objectList the objectList to set
	 */
	public void setObjectList(List<Object> objectList) {
		this.objectList = objectList;
	}

	/**
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * @param object the object to set
	 */
	public void setObject(Object object) {
		this.object = object;
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
