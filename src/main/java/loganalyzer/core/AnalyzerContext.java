package loganalyzer.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Log分析上下文，包含命令行传入参数，配置参数，用户临时信息的保存等
 * @author hongze.chi@gmail.com
 *
 */
public class AnalyzerContext implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private final Map<String, Object> contextMap = new HashMap<String, Object>();

	public AnalyzerContext() {
		
	}
	
	public void addParam(String key, Object value){
		this.contextMap.put(key, value);
	}
	
	public void addParam(Map<String, ? extends Object> paramMap) {
		this.contextMap.putAll(contextMap);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getParam(String key) {
		return (T)contextMap.get(key);
	}
	
	public void remove(String key) {
		contextMap.remove(key);
	}
}
