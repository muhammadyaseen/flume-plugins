## Interceptors
Interceptors are a very useful feature of Flume, they can be used to modify events "in-flight". Some key features offered by Flume depend on the values/keys set in event headers e.g. Hadoop routing, Hive partitioning, Date/Time based routing, Channel selectors etc. An interceptor can be used to populate the `header` field with right key-value pairs. It can also be used to do some light weight processing on the contents of the event e.g. `body`. For example, you might apply some regex to filter some events, or use the information in event to create the right header for routing.

2 reference implementations have been provided.

### Tracking Device Type Interceptor
This interceptors extracts the device type information and puts this information in the header. This can be used to, for instance, route the events to a different sink / path based on device type.

### Metrics Server Interceptor
This interceptors extracts the server id information and puts this information in the header. This can be used to in Hadoop routing (Flume HDFS Sink), so that data for a server always gets stored in the same directory. If using Hive as sink, this information can be used to deliver the data to the correct Hive table parition.
