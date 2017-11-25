package io.muhammadyaseen.github.flumeplugins.event;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.flume.Event;

import io.muhammadyaseen.github.flumeplugins.platform.FlumeConstants;

public class TrackingEvent implements Event, Serializable {

	private static final long serialVersionUID = 1L;
	
	// event uuid or name
	private String event;

	// holds key,value pairs of context variables
	private Map<String, String> properties;
	
	private transient String charset = "UTF-8";
	private transient String DELIMITER = ",";
	
	//this is not transient bcz we want to preserve header info during serialization
	private Map<String, String> headers = new HashMap<String, String>();

	@Override
	public Map<String, String> getHeaders() {
		return this.headers;
	}

	@Override
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	@Override
	public byte[] getBody() {
		
		try {
			return getByteRepresentation();
		} catch (Exception e) {
			return new byte[0];
		}
	}

	@Override
	public void setBody(byte[] body) {
		
	}
	
	public Map<String, String> getProperties() {
		return properties;
	}
	
	public String getEvent() {
		return this.event;
	}
	
	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	public String getStringRepresentation() {
		
		String keysRep = "event: " + this.event;
		
		for (Map.Entry<String, String> entry: properties.entrySet()) {
			keysRep += " [" + entry.getKey() + ":" + entry.getValue() + "] ";
		}
		
		return keysRep;
	}
	
	private byte[] getByteRepresentation() {
		return SerializationUtils.serialize(this);
	}
	
	public void updateHeaders() {
		// append 3 zeros to convert sec to mS epoch		
		headers.put("timestamp", getPropertyFromKey("time") + "000");
	}
	
	public String getPropertyFromKey(String key) {
		
		if ( properties.containsKey(key) ) 
			return properties.get(key);
		else 
			return key == "time" ? FlumeConstants.DATETIME_EPOCH_REP_DEFAULT : FlumeConstants.STRING_DEFAULT;
	}

}
