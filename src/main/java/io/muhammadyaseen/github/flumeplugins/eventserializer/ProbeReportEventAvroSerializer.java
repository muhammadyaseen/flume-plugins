package io.muhammadyaseen.github.flumeplugins.eventserializer;

import java.io.OutputStream;

import org.apache.avro.Schema;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.serialization.AbstractAvroEventSerializer;
import org.apache.flume.serialization.EventSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.SerializationUtils;

import io.muhammadyaseen.github.flumeplugins.event.ProbeReportEvent;
import io.muhammadyaseen.github.flumeplugins.eventhandler.ProbeReportEventHandler;

// All avro serializer classes must inherit from AbstractAvroEventSerializer
public class ProbeReportEventAvroSerializer extends AbstractAvroEventSerializer<ProbeReportEvent> {

	private static final Schema SCHEMA = new Schema.Parser().parse(
			"{ \"type\": \"record\", \"name\": \"ProbeReportEvent\", \"namespace\": \"io.muhammadyaseen.github.flumeplugins.event\", \"fields\": [" +
				    "{ \"name\": \"asset_id\",            \"type\": \"string\", \"default\": \"\" }," +
				    "{ \"name\": \"probe_time\",          \"type\": \"string\", \"default\": \"\" }," +
				    "{ \"name\": \"serviceA_check\",      \"type\": \"int\", \"default\": 0 }," +
				    "{ \"name\": \"serviceB_check\",      \"type\": \"int\", \"default\": 0 }," +
				    "{ \"name\": \"serviceC_check\",      \"type\": \"int\", \"default\": 0 } ] }");
	
	private static final Logger LOG = LoggerFactory.getLogger(AbstractAvroEventSerializer.class);
	
	private final OutputStream out;

	private ProbeReportEventAvroSerializer(OutputStream out) {
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
	protected ProbeReportEvent convert(Event event) {
		ProbeReportEvent ac_event = SerializationUtils.deserialize(event.getBody());
		//  Can use LOG for debugging, this comes in very handy on occasions  
		//LOG.info(ac_event.getStringRepresentation());
		return ac_event;
	}
	
	public static class Builder implements EventSerializer.Builder {

		@Override
		public EventSerializer build(Context context, OutputStream out) {
			
			ProbeReportEventAvroSerializer writer = new ProbeReportEventAvroSerializer(out);
			writer.configure(context);
			return writer;
		
		}
		
	}

}
