package io.muhammadyaseen.github.flumeplugins.event;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.flume.Event;

import org.apache.commons.lang3.SerializationUtils;

/*
 * This class represents the schema in which the source is throwing events
 */
public class MetricsEvent implements Event, Serializable {

	private static final long serialVersionUID = 1L;
	
	private String 	asset_id;
	private long 	timestamp;
	private String 	output;
	
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
	
	public String getStringRepresentation() {
		return asset_id + DELIMITER + output + DELIMITER + Long.toString(timestamp);
	}
	
	private byte[] getByteRepresentation() {
		return SerializationUtils.serialize(this);
	}
		
	public String getServer() {
		return this.asset_id;
	}
	
	public String getOutput() {
		return this.output;
	}
	
	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	// this method is also an example of how events can be modified in the event handler
	// thus eliminates need of interceptor in most cases as far as HTTP source is
	// concerned
	public void updateHeaders() {
		// to convert the epoch time in seconds to epoch time in milliseconds
		headers.put("timestamp", Long.toString(timestamp) + "000" );
		
	}
			
}
