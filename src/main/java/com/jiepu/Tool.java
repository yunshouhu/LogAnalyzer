package com.jiepu;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import loganalyzer.core.AnalyseLogTaskLauncher;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class Tool {
	private static Client javaClient = null;
	
	public static Client getJavaClient() {
		if (javaClient == null) {
			try {
				
				String es_cluster_name=AnalyseLogTaskLauncher.taskConfig.getEs_cluster_name();

				ImmutableSettings.Builder builder = ImmutableSettings.settingsBuilder().put("cluster.name", es_cluster_name);
				Settings settings = builder.build();

				List<String> cluster_ips = AnalyseLogTaskLauncher.taskConfig.getEs_ips();
				TransportClient transportClient = new TransportClient(settings);
				for (String string : cluster_ips) {
					String[] strs=string.split(":");
					transportClient.addTransportAddress(new InetSocketTransportAddress(strs[0], Integer.parseInt(strs[1])));
				}
				javaClient = transportClient;
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return javaClient;
	}
	
	public static String getHostNameForLiunx() {
		try {
			return (InetAddress.getLocalHost()).getHostName();
		} catch (UnknownHostException uhe) {
			String host = uhe.getMessage(); // host = "hostname: hostname"
			if (host != null) {
				int colon = host.indexOf(':');
				if (colon > 0) {
					return host.substring(0, colon);
				}
			}
			return "UnknownHost";
		}
	}


	public static String getHostName() {
		if (System.getenv("COMPUTERNAME") != null) {
			return System.getenv("COMPUTERNAME");
		} else {
			return getHostNameForLiunx();
		}
	}
}
