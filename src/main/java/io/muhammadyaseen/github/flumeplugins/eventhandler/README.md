## HTTP Source Event Handler

Apache Flume supports a JSON schema of `{ "headers" : { } , "body" : "text body" }` out of the box. But in most cases this is not expressive enough. Also, in most real world scenarios you do not have control over the format of data the source is going to send. In such case, it is necessary to write a custom event handler by implementing the `HTTPSourceHandler` class.
[JSON Handler](https://github.com/apache/flume/blob/release-1.7.0/flume-ng-core/src/main/java/org/apache/flume/source/http/JSONHandler.java)

3 use cases have been provided in this package.

### 1. Metrics Event Handler

### 2. Probe Report Event Handler

### 3. Tracking Event Handler





