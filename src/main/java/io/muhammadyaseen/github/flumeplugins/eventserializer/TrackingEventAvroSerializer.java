package io.muhammadyaseen.github.flumeplugins.eventserializer;

import java.io.OutputStream;

import org.apache.avro.Schema;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.serialization.AbstractAvroEventSerializer;
import org.apache.flume.serialization.EventSerializer;

import io.muhammadyaseen.github.flumeplugins.event.TrackingEvent;
import io.muhammadyaseen.github.flumeplugins.eventmodel.TrackingErrorEventModel;

public class TrackingEventAvroSerializer extends AbstractAvroEventSerializer<TrackingErrorEventModel> {

	private static final Schema SCHEMA = new Schema.Parser().parse(
			"{ \"type\": \"record\", \"name\": \"TrackingEvent\", \"namespace\": \"io.muhammadyaseen.github.flumeplugins.event\", \"fields\": [" +
				    "{ \"name\": \"event_time\",     	\"type\": \"string\" }," +
				    "{ \"name\": \"user_id\",    		\"type\": \"string\" }," +
				    "{ \"name\": \"app_version\",   	\"type\": \"string\" }," +
				    "{ \"name\": \"error_code\",		\"type\": \"int\" } ] }");
	
	private final OutputStream out;

	private TrackingEventAvroSerializer(OutputStream out) {
		this.out = out;
	}
	@Override
	protected OutputStream getOutputStream() {
		return out;
	}

	@Override
	protected Schema getSchema() {
		
		return SCHEMA;
	}

	@Override
	// this method receives a flume event and returns a TrackingErrorEventModel object
	// by extracting relevant fields from raw event.
	protected TrackingErrorEventModel convert(Event event) {
		
		// recover event data from body
		TrackingEvent trk_event = (TrackingEvent)SerializationUtils.deserialize(event.getBody());
				
		// convert event data to event model for storage in avro format
		TrackingErrorEventModel trk_error_model = new TrackingErrorEventModel();
			
		trk_error_model.setUserID(trk_event.getPropertyFromKey("user_id"));
		trk_error_model.setAppVersion(trk_event.getPropertyFromKey("app_version"));
		trk_error_model.setErrorCode(Integer.parseInt(trk_event.getPropertyFromKey("error_code")));
		trk_error_model.setTimestamp(trk_event.getPropertyFromKey("time"));

		// return the event model. this will get serialized into avro format by serializer downstream
		return trk_error_model;
	}
	
	public static class Builder implements EventSerializer.Builder {

		@Override
		public EventSerializer build(Context context, OutputStream out) {
			
			TrackingEventAvroSerializer writer = new TrackingEventAvroSerializer(out);
			writer.configure(context);
			return writer;
		
		}
		
	}

}
