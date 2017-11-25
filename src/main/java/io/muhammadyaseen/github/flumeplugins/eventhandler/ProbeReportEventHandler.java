package io.muhammadyaseen.github.flumeplugins.eventhandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import io.muhammadyaseen.github.flumeplugins.event.ProbeReportEvent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.source.http.HTTPBadRequestException;
import org.apache.flume.source.http.HTTPSourceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProbeReportEventHandler implements HTTPSourceHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ProbeReportEventHandler.class);
	private final Type listType = new TypeToken<List<ProbeReportEvent>>(){}.getType();
	private final Gson gson;
	
	public ProbeReportEventHandler() {
		gson = new GsonBuilder().disableHtmlEscaping().create();
	}
	
	@Override
	public void configure(Context context) {
		
	}

	@Override
	public List<Event> getEvents(HttpServletRequest request) throws HTTPBadRequestException, Exception {
		
		BufferedReader reader = request.getReader();
		String charset = request.getCharacterEncoding();
		long cl = request.getContentLength();
		
		//LOG.info("content-length: " + Long.toString(cl));
		
		if (charset == null) {
			LOG.debug("Charset is null, default charset of UTF-8 will be used.");
			charset = "UTF-8";
		} else if (!(charset.equalsIgnoreCase("utf-8") || charset.equalsIgnoreCase("utf-16") || charset.equalsIgnoreCase("utf-32"))) {
			LOG.error("Unsupported character set in request {}. JSON handler supports UTF-8, UTF-16 and UTF-32 only.", charset);			
			throw new UnsupportedCharsetException("JSON handler supports UTF-8, " + "UTF-16 and UTF-32 only.");
		}

		List<Event> eventList = new ArrayList<Event>(0);
		
		try {
			// the following lines handles the actual "conversion"
			// it maps the incoming JSON data as ProbeReportEvent class objects
			eventList = gson.fromJson(reader, listType);
		} catch (JsonSyntaxException ex) {
			throw new HTTPBadRequestException("Request has invalid JSON Syntax.", ex);
		}

		for (Event e : eventList) {
			((ProbeReportEvent) e).updateHeaders();
			// LOG.info("[JSON]: " + ((ProbeReportEvent) e).getStringRepresentation());	
		}
		
		return eventList;
	}

}
