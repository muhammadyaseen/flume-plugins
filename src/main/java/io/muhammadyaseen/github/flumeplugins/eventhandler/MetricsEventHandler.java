package io.muhammadyaseen.github.flumeplugins.eventhandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.lang.reflect.Type;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.source.http.HTTPBadRequestException;
import org.apache.flume.source.http.HTTPSourceHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.muhammadyaseen.github.flumeplugins.event.MetricsEvent;


public class MetricsEventHandler implements HTTPSourceHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(MetricsEventHandler.class);
	private final Type listType = new TypeToken<List<MetricsEvent>>(){}.getType();
	private final Gson gson;

	public MetricsEventHandler() {

		gson = new GsonBuilder().disableHtmlEscaping().create();

	}

	public void configure(Context context) {
		// no op
	}

	@Override
	public List<Event> getEvents(HttpServletRequest request) throws Exception {
		
		BufferedReader reader = request.getReader();
		String charset = request.getCharacterEncoding();
		
		if (charset == null) {
			LOG.debug("Charset is null, default charset of UTF-8 will be used.");
			charset = "UTF-8";
		} else if (!(charset.equalsIgnoreCase("utf-8") || charset.equalsIgnoreCase("utf-16") || charset.equalsIgnoreCase("utf-32"))) {
			
			LOG.error("Unsupported character set in request {}. JSON handler supports UTF-8, UTF-16 and UTF-32 only.", charset);
			
			throw new UnsupportedCharsetException("JSON handler supports UTF-8, " + "UTF-16 and UTF-32 only.");
		}

		List<Event> eventList = new ArrayList<Event>(0);
		
		try {
			eventList = gson.fromJson(reader, listType);
		} catch (JsonSyntaxException ex) {
			throw new HTTPBadRequestException("Request has invalid JSON Syntax.", ex);
		}

		for (Event e : eventList) {
			
			((MetricsEvent) e).setCharset(charset);
			((MetricsEvent) e).updateHeaders();
			//LOG.debug("[JSON]: " + ((MetricsEvent) e).getStringRepresentation());
		}
		
		return eventList;
	}

}
