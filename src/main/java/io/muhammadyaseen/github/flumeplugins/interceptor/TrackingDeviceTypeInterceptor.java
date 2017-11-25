package io.muhammadyaseen.github.flumeplugins.interceptor;

import java.util.List;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.apache.commons.lang3.SerializationUtils;


import io.muhammadyaseen.github.flumeplugins.event.TrackingEvent;

public class TrackingDeviceTypeInterceptor implements Interceptor {

	private TrackingDeviceTypeInterceptor() {
		// private constructor bcz only the Builder class is allowed to build the instances
	}
	
	@Override
	public void initialize() {
		// no op
	}

	@Override
	public Event intercept(Event event) {
		
		event.getHeaders().put(
			"device_type",
			((TrackingEvent)SerializationUtils.deserialize(event.getBody())).getPropertyFromKey("device_type").toLowerCase()
		);
		
		return event;
	}

	@Override
	public List<Event> intercept(List<Event> events) {
		
		for (Event e: events) {
			intercept(e);
		}
		
		return events;
	}

	@Override
	public void close() {
		// no op
	}
	
	/**
	 * Builder which builds new instance of the this interceptor.
	 */
	public static class Builder implements Interceptor.Builder {

		// I think the following params represent additional config. options that can be set via the flume config file. 
		// this might be useful to knowledge and might be used somewhere

		@Override
		public void configure(Context context) {
			
		}

		@Override
		public Interceptor build() {
			return new TrackingDeviceTypeInterceptor();
		}

	}

}
