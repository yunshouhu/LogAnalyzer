package loganalyzer.core.test;

import java.net.URL;

import junit.framework.TestCase;
import loganalyzer.core.AnalyzeTaskConfig;
import loganalyzer.core.AnalyzerContext;
import loganalyzer.core.AnalyzerLogTaskExecutor;
import loganalyzer.core.BasicAnalyzerLogTaskExecutor;

public class BasicExecutorTest extends TestCase{

	public void testAll() throws Exception{
		AnalyzerLogTaskExecutor executor = BasicAnalyzerLogTaskExecutor.getInstance();
		
		URL url=Thread.currentThread().getContextClassLoader().getResource("test_task.yaml");
		System.out.println(url.getPath());
		
		
		AnalyzeTaskConfig taskConfig = AnalyzeTaskConfig.loadFromConfigFile(url.getPath());
		AnalyzerContext context = new AnalyzerContext();
		context.addParam(taskConfig.getInitParams());
		executor.execute(taskConfig, context);
	}
}
