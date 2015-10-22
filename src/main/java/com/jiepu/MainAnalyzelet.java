package com.jiepu;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loganalyzer.core.AnalyseLogTaskLauncher;
import loganalyzer.core.AnalyzerContext;
import loganalyzer.core.BasicAnalyzelet;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;

import com.alibaba.fastjson.JSON;

//参考elk logstash

public class MainAnalyzelet extends BasicAnalyzelet {
	public static String REQUEST = "Request: ";
	public static String HOST = "host : ";
	public static String HEADER = "header: ";

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public void doLine(String line, AnalyzerContext analyzerContext) {
		// logger.info("line:" + line);
		try {
			// http://blog.sina.com.cn/s/blog_6400e5c50101qrhk.html
			String data = new String(line.getBytes("ISO-8859-1"), "utf-8");
			// System.out.println(data);
			// 2015-10-20 10:55:21,571 Sending Request: GET
			// /homep_ad_300x250_2.html
			int index = data.indexOf(REQUEST);
			if (index != -1) {
				analyzerRequest(index, data, analyzerContext);
			} else if ((index = data.indexOf(HOST)) != -1) {
				analyzerHost(index, data, analyzerContext);
			} else if ((index = data.indexOf(HEADER)) != -1) {
				analyzerHeader(index, data, analyzerContext);
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	private void analyzerHeader(int index, String data,
			AnalyzerContext analyzerContext) {
		String message = data;
		String time = message.split(",")[0];
		// System.out.println(time);
		String header = message.substring(index + HOST.length());
		// System.out.println(header);

		String[] headers = header.split(":");
		if (headers.length > 1) {
			String key = headers[0];
			String value = headers[1];
			// System.out.println("key="+key.trim()+",value="+value);

		}

		LogEntity entity = new LogEntity();
		entity.setHost(Tool.getHostName());
		entity.setMessage(message);

		entity.setPath(AnalyseLogTaskLauncher.path);
		entity.setTimestamp(time);
		entity.setVersion("1");
		;
		System.out.println(entity);
		buildindex(entity);

	}

	private void analyzerHost(int index, String data,
			AnalyzerContext analyzerContext) {
		String message = data;
		String time = message.split(",")[0];
		// System.out.println(time);
		String host = message.substring(index + HOST.length());
		// System.out.println(host);

		LogEntity entity = new LogEntity();
		entity.setHost(Tool.getHostName());
		entity.setMessage(message);
		entity.setPath(AnalyseLogTaskLauncher.path);
		entity.setTimestamp(time);
		entity.setVersion("1");
		;
		System.out.println(entity);
		buildindex(entity);

	}

	private void analyzerRequest(int index, String data,
			AnalyzerContext analyzerContext) {
		String message = data;
		String time = message.split(",")[0];
		// System.out.println(time);
		String get = message.substring(index + REQUEST.length());

		String gets[] = get.split("\\?");
		String requesturl = gets[0];
		if (gets.length > 1) {
			String param = gets[1];
			// System.out.println(requesturl);
			String[] params = param.split("&");
			Map<String, String> paramMaps = new HashMap<String, String>();
			for (String str : params) {
				String[] strs = str.split("=");
				if (strs.length > 1) {
					paramMaps.put(strs[0], strs[1]);
				}

			}
			// System.out.println(paramMaps);
		}

		LogEntity entity = new LogEntity();
		entity.setHost(Tool.getHostName());
		entity.setMessage(message);
		entity.setPath(AnalyseLogTaskLauncher.path);
		entity.setTimestamp(time);
		entity.setVersion("1");
		;
		System.out.println(entity);
		buildindex(entity);
	}

	private void buildindex(LogEntity entity) {

		// https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/bulk.html
		// logstash-2015.10.19
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		String indexname = "logstash-" + dateFormat.format(new Date());

		BulkRequestBuilder builder = Tool.getJavaClient().prepareBulk();

		IndexRequestBuilder indexRequestBuilder = Tool.getJavaClient()
				.prepareIndex(indexname, "logstash_type");

		indexRequestBuilder.setSource(JSON.toJSONString(entity));

		builder.add(indexRequestBuilder);

		BulkResponse bulkResponse = builder.execute().actionGet();

		if (bulkResponse.hasFailures()) {
			System.out.println("出错了");
			// System.out.println(JSON.toJSONString(bulkResponse.getItems()));
			System.out.println(bulkResponse.buildFailureMessage());
		} else {
			System.out.println("ok");
		}

	}

}
