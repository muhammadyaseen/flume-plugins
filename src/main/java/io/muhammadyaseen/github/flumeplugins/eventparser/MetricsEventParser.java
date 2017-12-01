package io.muhammadyaseen.github.flumeplugins.eventparser;

import java.util.regex.Pattern;

import io.muhammadyaseen.github.flumeplugins.eventmodel.MetricsModel;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public final class MetricsEventParser {

	private static final String METRIC_CPU_UTIL_CHECK 						= "proc_util_info.percent";
	private static final String METRIC_MEMORY_UTIL_CHECK 					= "memory_util_info.used.percent";
	private static final String METRIC_BW_UTIL_IN_CHECK 					= "bw_util_info.in.bytes"; 
	private static final String METRIC_SRV_1_CHECK 							= "service1.status.code";
	
	private MetricsEventParser() { }
	
	public static MetricsModel Process(String server, String telemetry_output) {
		
		MetricsModel metricsModel_default = new MetricsModel();

		if (server == null  || telemetry_output == null) {
			return metricsModel_default;
		}
			
		MetricsModel sm = new MetricsModel();
		
		if ( telemetry_output.contains(METRIC_CPU_UTIL_CHECK) )	
			sm = ParseAsMertic_CPU(server, telemetry_output);
		
		if ( telemetry_output.contains(METRIC_MEMORY_UTIL_CHECK) )
			sm =  ParseAsMertic_Memory(server, telemetry_output);
		
		if ( telemetry_output.contains(METRIC_BW_UTIL_IN_CHECK) )
			sm = ParseAsMertic_Bandwidth(server, telemetry_output);
		
		if ( telemetry_output.contains(METRIC_SRV_1_CHECK) )
			sm = ParseAsMertic_ServiceStatus(server, telemetry_output);
	
		if ( sm.getServer() != "BAD" ) return sm;
	
		// if output doesn't match predefined checks, store the default model
		return metricsModel_default;	
	}
	
	public static MetricsModel ParseAsMertic_CPU(String server, String telemetry_output) {
		/* Fields of interest:
		 * Infra.<server_id>.proc_util_info.percent
		 */
		
		Map<String, Float> returnMap = new HashMap<String, Float>();

		String ts = "0";
		String val;
		
		// Get `proc_util_info.percent`
		
		Pattern p = Pattern.compile("Infra.*.proc_util_info.percent\\t(\\d+(.\\d+)?)\\t(\\d+)");
		Matcher m = p.matcher(telemetry_output);
		
		if ( m.find() ) {
		
			val = m.group(1);
			ts = m.group(3);
			
			returnMap.put("processor_util_pct", Float.parseFloat(val));
		}	
		return new MetricsModel(returnMap, server, ts);
	}

	public static MetricsModel ParseAsMertic_Memory(String server, String telemetry_output) {
		/* Fields of interest:
		 * Infra.<server_id>.memory_util_info.used.percent
		 */
		
		Map<String, Float> returnMap = new HashMap<String, Float>();
		String ts = "0";
		String val;
		
		// Get `available_bytes`
		Pattern p = Pattern.compile("Infra.*.memory_util_info.used.percent\\t(\\d+)\\t(\\d+)");
		Matcher m = p.matcher(telemetry_output);
		
		if ( m.find() ) {
			
			val = m.group(1);
			ts = m.group(2);
			
			returnMap.put("memory_used_pct", Float.parseFloat(val));
		}
		return new MetricsModel(returnMap, server, ts);
	}

	public static MetricsModel ParseAsMertic_Bandwidth(String server, String telemetry_output) {
		/* Fields of interest:
		 * Infra.<server_id>.bw_util_info.in.bytes
		 * Infra.<server_id>.bw_util_info.out.bytes
		 */
		
		Map<String, Float> returnMap = new HashMap<String, Float>();
		String ts = "0";
		String val;
		
		Pattern p = Pattern.compile("Infra.*.bw_util_info.in.bytes\\t(\\d+)\\t(\\d+)");
		Matcher m = p.matcher(telemetry_output);
		
		if ( m.find() ) {
			val = m.group(1);
			ts = m.group(3);
			
			returnMap.put("bytes_in", Float.parseFloat(val));
		}
		
		p = Pattern.compile("Infra.*.bw_util_info.out.bytes\\t(\\d+)\\t(\\d+)");
		m = p.matcher(telemetry_output);
		
		if ( m.find() ) {
			val = m.group(1);
			ts = m.group(3);
			
			returnMap.put("bytes_out", Float.parseFloat(val));
		}

		return new MetricsModel(returnMap, server, ts);		
	}
	
	public static MetricsModel ParseAsMertic_ServiceStatus(String server, String telemetry_output) {
		/* Fields of interest:
		 * Infra.<server_id>.service1.status.code
		 * Infra.<server_id>.service2.status.code
		 * Infra.<server_id>.service3.status.code
		 */
		
		Map<String, Float> returnMap = new HashMap<String, Float>();

		String ts = "0";
		String val;
		
		Pattern p = Pattern.compile("Infra.*.service1.status.code\\t(\\d+)\\t(\\d+)");
		Matcher m = p.matcher(telemetry_output);
		
		if ( m.find() ) {
		
			val = m.group(1);
			ts = m.group(2);
			
			returnMap.put("s1_status", Float.parseFloat(val));
		}
				
		p = Pattern.compile("Infra.*.service2.status.code\\t(\\d+)\\t(\\d+)");
		m = p.matcher(telemetry_output);
		
		if ( m.find() ) {
		
			val = m.group(1);
			ts = m.group(2);
			
			returnMap.put("s2_status", Float.parseFloat(val));
		}
		
		p = Pattern.compile("Infra.*.service3.status.code\\t(\\d+)\\t(\\d+)");
		m = p.matcher(telemetry_output);
		
		if ( m.find() ) {
		
			val = m.group(1);
			ts = m.group(2);
			
			returnMap.put("s3_status", Float.parseFloat(val));
		}
		return new MetricsModel(returnMap, server, ts);
		
	}
}
