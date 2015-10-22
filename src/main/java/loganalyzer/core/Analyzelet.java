package loganalyzer.core;

/**
 * 定义一个日志分析逻辑的声明周期
 * @author hongze.chi@gmail.com
 *
 */
public interface Analyzelet {

	/**
	 * 分析之前要处理的事务,相当于awk的begin操作块
	 * @param analyzerContext
	 */
	public void begin(AnalyzerContext analyzerContext);
	
	/**
	 * 处理每一行log的逻辑
	 * @param line
	 * @param analyzerContext
	 */
	public void doLine(String line, AnalyzerContext analyzerContext);
	
	/**
	 * 最终结束的逻辑，相当于awk的end操作，注意，如果分析中途发生错误，那么不会执行此方法，而是执行onError方法。
	 * @param analyzerContext
	 */
	public void end(AnalyzerContext analyzerContext);
	
	/**
	 * 错误处理逻辑
	 * @param analyzerContext
	 */
	public void onError(AnalyzerContext analyzerContext, Throwable t);
	
	/**
	 * 遇到错误是否立即停止
	 * @return
	 */
	public boolean onErrorStop();
}
