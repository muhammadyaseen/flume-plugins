package io.muhammadyaseen.github.flumeplugins.eventmodel;

import java.util.HashMap;
import java.util.Map;

import io.muhammadyaseen.github.flumeplugins.platform.FlumeConstants;


public class MetricsModel {

	private Map<String, Float> metric;
	private String server;
	private String metric_time;
	
	// a parameterless constructor is required for deserialization utilities.
	public MetricsModel() {
		
		this.metric = new HashMap<String, Float>();
		metric.put("test", FlumeConstants.FLOAT_DEFAULT);		
		this.server = 		FlumeConstants.STRING_DEFAULT;
		this.metric_time = 	FlumeConstants.DATETIME_EPOCH_REP_DEFAULT;
	}
	
	// this parameterized constructor is used by MetricsEventParser class to construct object after it has parsed the raw event
	// see MetricsEventParser for usage details
	public MetricsModel(Map<String, Float> metrics, String server, String metric_time) {
		
		this.metric = metrics;
		this.server = server;
		this.metric_time = metric_time;
		 
	}

	public String getServer() {
		return this.server;
	}
	
}
