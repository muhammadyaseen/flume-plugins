package io.muhammadyaseen.github.flumeplugins.interceptor;

import java.util.List;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.apache.commons.lang3.SerializationUtils;

import io.muhammadyaseen.github.flumeplugins.event.MetricsEvent;

/**
 * This interceptor adds a new header key 'server' by parsing it from the MetricsEvent
 * This key helps in routing via Hadoop fs paths 
 */
public class MetricsServerInterceptor implements Interceptor {

	private MetricsServerInterceptor() {
		// private constructor bcz only the Builder class is allowed to build the instances
	}
	
	@Override
	public void initialize() {
		// no op
	}

	@Override
	public Event intercept(Event event) {
		
		event.getHeaders().put(
			"server",
			((MetricsEvent)SerializationUtils.deserialize(event.getBody())).getServer().toLowerCase()
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
	
	public static class Builder implements Interceptor.Builder {

		// additional config. options that can be set via the flume config file. 
		// can be accessed via the Context class
		@Override
		public void configure(Context context) {
			
		}

		@Override
		public Interceptor build() {
			return new MetricsServerInterceptor();
		}

	}

}
