package com.jiepu;

import java.io.Serializable;

/*

"message": "127.0.0.1 - - [19/Oct/2015:17:03:49 +0800] "GET /essearch/s/easyui/jquery.easyui.min.js HTTP/1.1" 304 -
",
"@version": "1",
"@timestamp": "2015-10-19T09:32:26.169Z",
"host": "xiaowu-PC",
"path": "D:/server/apache-tomcat-8.0.15-windows-2014/apache-tomcat-8.0.15/logs/localhost_access_log.2015-10-19.txt",
"type": "nginx_access"
 */
public class LogEntity implements Serializable{

	private String id;
	//日志路径
	private String path;
	//日志主机
	private String host;
	//时间戳
	private String timestamp;
	//版本
	private String version;
	//原数据
	private String message;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "LogEntity [id=" + id + ", path=" + path + ", host=" + host
				+ ", timestamp=" + timestamp + ", version=" + version
				+ ", message=" + message + "]";
	}
	
	
	
	
	
}
