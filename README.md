# Flume Pluggables (aka Plugins)
[![Build Status](https://travis-ci.org/muhammadyaseen/flume-plugins.svg?branch=master)](https://travis-ci.org/muhammadyaseen/flume-plugins)

## About Apache Flume
> "Apache Flume is a distributed, reliable, and available system for efficiently collecting, aggregating and moving large amounts of log data from many different sources to a centralized data store." 
(from [Flume User Guide](https://flume.apache.org/FlumeUserGuide.html)).

## About this repo
Flume is a flexible piece of software and allows plugging in custom code for many of its components. Most probable candidates for custom code are Flume's Interceptors, Event Handlers (for [HTTP Source](https://flume.apache.org/FlumeUserGuide.html#http-source)), and Serializers (e.g. Avro Serializer). Less likely but still important components that can be swapped with custom implementations are Channel Selectors, JMS Message Converters, and custom Sink Processors.

This repo aims to provide a collection of custom Apache Flume Serializers, Handlers, and Interceptors which can be used as a reference point for implementing your own. The code assumes an HTTP Source and HDFS Sink. Code works with both File and Memory Channels. If you are only going to use the Memory Channel see the NOTE in `README.md` file in `eventserializer` folder.

## Flume Agent Configuration
Agent configuration files are in `./conf` directory

### Events
* MetricsEvent
* ProbeReportEvent
* TrackingEvent

### Event Handlers
* MetricsEventHandler
* ProbeReportEventHandler
* TrackingEventHandler

### Interceptors
* MetricsServerInterceptor
* TrackingDeviceTypeInterceptor

### Avro Serializers
* MetricsEventAvroSerializer
* ProbeReportEventAvroSerializer
* TrackingEventAvroSerializer

### Event Parser
*  MetricsEventParser

### Event Model
* MetricsModel
* TrackingErrorEventModel

## Flume Event Flow
```


           +-------------+   +-------------+   +---------------+   +-------------+   +-------------+
 Events    | HTTP Source |   |Interceptor  |   |File / Memory  |   |Avro         |   |Hadoop / HDFS|
+--------->+ Handler     +-->|(optional)   +-->|Channel        |-->|Serializer   |-->|  Sink       |
           +-------------+   +-------------+   +---------------+   +--+-----+----+   +-------------+
                                                                       |     ^
                                                                       |     |
                                                                       v     |
                                                                    +--------+----+
                                                                    | Event Parser|
                                                                    | (optional)  |
                                                                    +-------------+
```
(ASCII diagram created via: [ASCIIFlow](http://asciiflow.com/))

## Dev Environment Setup
This code can be bundled in a single `.jar` file. If you want to setup Eclipse to make changes and compile yourself, check my post for [How to write Custom Flume components](#)
