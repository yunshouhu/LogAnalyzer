package loganalyzer.core;

public class LogAnalyzeException extends Exception{

	private static final long serialVersionUID = 1L;

	public LogAnalyzeException(String msg) {
		super(msg);
	}
	
	public LogAnalyzeException(Throwable t) {
		super(t);
	}
	
	public LogAnalyzeException(String msg, Throwable t) {
		super(msg, t);
	}
}
