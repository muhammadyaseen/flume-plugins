package io.muhammadyaseen.github.flumeplugins.eventserializer;

import java.io.OutputStream;

import org.apache.avro.Schema;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.serialization.AbstractAvroEventSerializer;
import org.apache.flume.serialization.EventSerializer;

import io.muhammadyaseen.github.flumeplugins.event.MetricsEvent;
import io.muhammadyaseen.github.flumeplugins.eventmodel.MetricsModel;
import io.muhammadyaseen.github.flumeplugins.eventparser.MetricsEventParser;

/* Flume sources of relevant classes for reference:
 * https://github.com/apache/flume/blob/release-1.7.0/flume-ng-core/src/main/java/org/apache/flume/serialization/FlumeEventAvroEventSerializer.java
 * https://github.com/apache/flume/blob/release-1.7.0/flume-ng-core/src/main/java/org/apache/flume/serialization/AvroEventSerializerConfigurationConstants.java
 * https://github.com/apache/flume/blob/release-1.7.0/flume-ng-core/src/main/java/org/apache/flume/serialization/AbstractAvroEventSerializer.java
 */

public class MetricsEventAvroSerializer extends AbstractAvroEventSerializer<MetricsModel> {
	
	private static final Schema SCHEMA = new Schema.Parser().parse(
			"{ \"type\": \"record\", \"name\": \"MetricsModel\", \"namespace\": \"io.muhammadyaseen.github.flumeplugins.event\", \"fields\": [" +
				    "{ \"name\": \"server\",         \"type\": \"string\",	default: \"\" }," +
				    "{ \"name\": \"metric_time\",    \"type\": \"string\",	default: \"\" }," +
				    "{ \"name\": \"metric\",         \"type\": {\"type\": \"map\", \"values\": \"float\" } } ] }");
	
	private final OutputStream out;

	private MetricsEventAvroSerializer(OutputStream out) {
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
	protected MetricsModel convert(Event event) {
			
		MetricsEvent se = (MetricsEvent)SerializationUtils.deserialize(event.getBody());
		
		// we invoke the event parser here to convert the event from the form in which it was received 
		// to a more refined and useful form for storing. 
		return se == null ? new MetricsModel(): MetricsEventParser.Process(se.getServer(), se.getOutput());
		
	}
	
	public static class Builder implements EventSerializer.Builder {

		@Override
		public EventSerializer build(Context context, OutputStream out) {
			
			MetricsEventAvroSerializer writer = new MetricsEventAvroSerializer(out);
			writer.configure(context);
			return writer;
		
		}
		
	}
}
