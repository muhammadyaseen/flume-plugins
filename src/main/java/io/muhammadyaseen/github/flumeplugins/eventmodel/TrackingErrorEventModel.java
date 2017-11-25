package io.muhammadyaseen.github.flumeplugins.eventmodel;

public class TrackingErrorEventModel {

	private String	event_time;
	private String 	user_id;
	private String 	app_version;
	private int 	error_code;
	
	// a parameterless constructor is required for deserialization utilities.
	public TrackingErrorEventModel() {
		
	}
	
	public String getTimestamp() {
		return event_time;
	}
	
	public void setTimestamp(String event_time) {
		
		this.event_time = event_time;
	}
	
	public String getUserID() {
		return user_id;
	}
	
	public void setUserID(String user_id) {
		this.user_id = user_id;
	}
	
	public String getAppVersion() {
		return app_version;
	}
	
	public void setAppVersion(String app_version) {
		this.app_version = app_version;
	}
	
	public int getErrorCode() {
		return error_code;
	}
	
	public void setErrorCode(int error_code) {
		this.error_code = error_code;
	}
	
	
}
