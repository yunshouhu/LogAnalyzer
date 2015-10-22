package loganalyzer.core;
/**
 * 实现该接口可以用于定义Log文件名的生成规则
 * @author hongze.chi@gmail.com
 *
 */
public interface LogFileNameGenerator {

	public String generateLogFileName();
}
