package com.mca.beans;

public class ErrorDetails {

	String din;
	String errorMsg;
	
	public String getDin() {
		return din;
	}
	public void setDin(String din) {
		this.din = din;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	@Override
	public String toString() {
		return "ErrorDetails [din=" + din + ", errorMsg=" + errorMsg + "]";
	}
}
