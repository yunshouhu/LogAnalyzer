package loganalyzer.core.test;

import java.net.URL;

import junit.framework.TestCase;
import loganalyzer.core.AnalyzeTaskConfig;
/**
 * loganalyzer.core.AnalyzerTaskConfig的测试用例
 * @author hongze.chi@gmail.com
 *
 */
public class AnalyzerTaskConfigTest extends TestCase{

	/** 测试载入配置文件 **/
	public void testLoad() throws Exception{
		URL url=Thread.currentThread().getContextClassLoader().getResource("test_task.yaml");
		System.out.println(url.getPath());
		AnalyzeTaskConfig analyzeTaskConfig = AnalyzeTaskConfig.loadFromConfigFile(url.getPath());
		System.out.println(analyzeTaskConfig);
	}
}
