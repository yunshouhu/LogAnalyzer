package loganalyzer.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AnalyzeletConfig {

	/**
	 * 如果指定为true之后，在分析的过程中遇到错误，那么将会在执行onError后立即停止
	 * @return
	 */
	boolean onErrorStop() default false;
}
