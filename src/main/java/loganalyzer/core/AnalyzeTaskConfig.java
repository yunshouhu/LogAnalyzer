package loganalyzer.core;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.ho.yaml.Yaml;
/**
 * Log分析任务配置定义
 * @author hongze.chi@gmail.com
 *
 */
public class AnalyzeTaskConfig implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * Log分析任务名称
	 */
	private String taskName;
	
	/**
	 * 指针文件
	 */
	private String pointerName;
	
	/**
	 * 从命令行接受的参数名
	 */
	private List<String> cmdOptions;
	
	/**
	 * 在配置文件中指定的初始化参数
	 */
	private Map<String, String> initParams;
	
	/**
	 * 指针文件重置时间,单位微秒
	 */
	private long resetPtrFileTime;
	
	/**
	 * 该任务下的analyzelets
	 */
	private List<String> analyzelets;
	
	/**
	 * 要分析的log文件名
	 */
	private String logFileName;
	
	/**
	 * log文件名生成器
	 */
	private String logFileNameGenerator;
	
	private List<String> es_ips;
	
	private String es_cluster_name;

	public AnalyzeTaskConfig() {
		
	}
	
	public List<String> getEs_ips() {
		return es_ips;
	}

	public void setEs_ips(List<String> es_ips) {
		this.es_ips = es_ips;
	}

	public String getEs_cluster_name() {
		return es_cluster_name;
	}

	public void setEs_cluster_name(String es_cluster_name) {
		this.es_cluster_name = es_cluster_name;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getPointerName() {
		return pointerName;
	}

	public void setPointerName(String pointerName) {
		this.pointerName = pointerName;
	}

	public List<String> getCmdOptions() {
		return cmdOptions;
	}

	public void setCmdOptions(List<String> cmdOptions) {
		this.cmdOptions = cmdOptions;
	}

	public Map<String, String> getInitParams() {
		return initParams;
	}

	public void setInitParams(Map<String, String> initParams) {
		this.initParams = initParams;
	}

	public long getResetPtrFileTime() {
		return resetPtrFileTime;
	}

	public void setResetPtrFileTime(long resetPtrFileTime) {
		this.resetPtrFileTime = resetPtrFileTime;
	}

	public List<String> getAnalyzelets() {
		return analyzelets;
	}

	public void setAnalyzelets(List<String> analyzelets) {
		this.analyzelets = analyzelets;
	}
	
	public String getLogFileName() {
		return logFileName;
	}

	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}

	public String getLogFileNameGenerator() {
		return logFileNameGenerator;
	}

	public void setLogFileNameGenerator(String logFileNameGenerator) {
		this.logFileNameGenerator = logFileNameGenerator;
	}

	public static AnalyzeTaskConfig loadFromConfigFile(String configFile) throws Exception{
		AnalyzeTaskConfig taskConfig = Yaml.loadType(new File(configFile), AnalyzeTaskConfig.class);
		return taskConfig;
	}
	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}
