package io.muhammadyaseen.github.flumeplugins.event;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.flume.Event;

import io.muhammadyaseen.github.flumeplugins.platform.FlumeConstants;

//TODO: change params to some other less suggestive params
public class ProbeReportEvent implements Event, Serializable {

	private static final long serialVersionUID = 1L;
	
	private	String	asset_id;
	private	long	probe_time;
	private	int	serviceA_check;
	private	int	serviceB_check;
	private	int	serviceC_check;
	
	private transient String charset = "UTF-8";
	private transient String DELIMITER = ",";
	
	//this is not transient bcz we want to preserve header info during serialization
	private Map<String, String> headers = new HashMap<String, String>();
	
	@Override
	public Map<String, String> getHeaders() {
		return headers;
	}

	@Override
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	@Override
	public byte[] getBody() {
		
		try {			
			return  getByteRepresentation();
		} catch (Exception e) {
			
			return new byte[0];
		}

	}

	@Override
	public void setBody(byte[] body) {
		
	}
	
	public String getStringRepresentation() {
		
		return 	asset_id;
	}
	
	private byte[] getByteRepresentation() {
		
		return 	SerializationUtils.serialize(this);
	}
	
	public void updateHeaders() {		
		// add timestamp header for flume routing
		try {
		
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        
	        Date d = df.parse(added_on);
	        
	        long ts = (long)d.getTime(); // get epoch time in milli-seconds
	        
	        headers.put("timestamp", Long.toString(ts));
			
		} catch (Exception ex) {
			
			headers.put("timestamp", "0");
		}
	}

}
