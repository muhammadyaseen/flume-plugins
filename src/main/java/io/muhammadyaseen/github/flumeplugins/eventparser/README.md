## Event Parser
The parser in this implementation uses simple regex rules to extract information from the output of different telemetry and reporting scripts. Any arbitarily complex logic can be used for parsing.

### Metrics Event Parser
Metrics event parser processes the new-line delimited data. Each line in output is a tab separated list containing 3 entities: the name of metric, the value of metric, and the timestamp. 
We use this info to develop regex rules which extract the metric name, value and timestamp and use this info to construct a `MetricsModel` object, that is used for final storage.
