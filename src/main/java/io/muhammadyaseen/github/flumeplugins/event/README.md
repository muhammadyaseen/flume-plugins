## Event
JSON data received by HTTP source handler is converted to a flume event internally by Apache Flume. The default [JSON Event](https://github.com/apache/flume/blob/release-1.7.0/flume-ng-sdk/src/main/java/org/apache/flume/event/JSONEvent.java) class is representative of the default schema supported by Flume i.e. `{ "headers" : { } , "body" : "text body" }`. More often than enough, the default schema does not fullfil the application needs. To overcome this, we can write custome classes which represent our desired JSON event schema. All event classes must implement the [Event](https://github.com/apache/flume/blob/release-1.7.0/flume-ng-sdk/src/main/java/org/apache/flume/Event.java) interface.
 
The following snippent sets up a Gson parser to parse JSON schema represented by `TrackingEvent` class.
```
private final Type listType = new TypeToken<List<TrackingEvent>>(){}.getType();
eventList = gson.fromJson(reader, listType);
```

3 example schemas and their corresponding event classes have been provided.

### 1. Metrics Event

```
{ 
    "asset_id"	:	"string id",    // unique id of the sender asset
	"timestamp"	:	1507795617,     // unix epoch timestamp
	"output"	:	"Some Form of Output from your telemetry services"
}
```

### 2. Probe Report Event

```
{ 
    "asset_id"          :   "string id",    		// unique id of the sender asset
    "probe_time"        :   "2017-01-01 11:11:11",  // unix epoch timestamp when prob was executed
	"serviceA_check"    :   628,            // example return code of service A check
	"serviceB_check"    :   0,              // example return code of service A check
	"serviceC_check"    :   900             // example return code of service A check
}
```

### 3. Tracking Event

```
{
	"event" : "event_name_or_unique id",    // unique id or name of the event
	"properties" : {		                // variable number of context properties 
		"property1"	: "value1",
		"property2"	: "value2",             // names could be arbitary
		.
		.
		.
		"propertyN" : "valueN"	
	}
}
```
