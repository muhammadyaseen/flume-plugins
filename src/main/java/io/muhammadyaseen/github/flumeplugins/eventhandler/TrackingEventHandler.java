package io.muhammadyaseen.github.flumeplugins.eventhandler;

import java.io.BufferedReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.source.http.HTTPBadRequestException;
import org.apache.flume.source.http.HTTPSourceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import io.muhammadyaseen.github.flumeplugins.event.TrackingEvent;

public class TrackingEventHandler implements HTTPSourceHandler {

	private static final Logger LOG = LoggerFactory.getLogger(TrackingEventHandler.class);
	private final Type listType = new TypeToken<List<TrackingEvent>>(){}.getType();
	private final Gson gson;
	
	public TrackingEventHandler() {

		gson = new GsonBuilder().disableHtmlEscaping().create();

	}
	
	@Override
	public void configure(Context context) {
	}

	@Override
	public List<Event> getEvents(HttpServletRequest request) throws Exception {
		
		BufferedReader reader = request.getReader();
		String charset = request.getCharacterEncoding();

		List<Event> eventList = new ArrayList<Event>(0);
		
		try {
			eventList = gson.fromJson(reader, listType);
		} catch (JsonSyntaxException ex) {
			throw new HTTPBadRequestException("Request has invalid JSON Syntax.", ex);
		}

		for (Event e : eventList) {
			((TrackingEvent) e).setCharset(charset);
			((TrackingEvent) e).updateHeaders();
			//LOG.info(((TrackingEvent) e).getStringRepresentation());
		}
		
		return eventList;
	}

}
