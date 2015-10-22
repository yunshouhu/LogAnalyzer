package loganalyzer.core;

/**
 * 用于执行分析任务的facade接口
 * @author hongze.chi@gmail.com
 *
 */
public interface AnalyzerLogTaskExecutor {

	public void execute(AnalyzeTaskConfig config, AnalyzerContext context) throws LogAnalyzeException;
}
