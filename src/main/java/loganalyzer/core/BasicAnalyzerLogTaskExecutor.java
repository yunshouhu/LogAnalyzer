package loganalyzer.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;


/**
 * 
 * @author hongze.chi@gmail.com
 *
 */
public class BasicAnalyzerLogTaskExecutor implements AnalyzerLogTaskExecutor{
	
	private static final AnalyzerLogTaskExecutor _instance = new BasicAnalyzerLogTaskExecutor();
	
	public static AnalyzerLogTaskExecutor getInstance() {
		return _instance;
	}

	@Override
	public void execute(AnalyzeTaskConfig config, AnalyzerContext context) throws LogAnalyzeException{
		List<Analyzelet> analyzelets = this.loadAnalyzelet(config);
		String logFileName = this.getLogFileName(config);
		String pointerFileName = config.getPointerName();
		this.doBegin(analyzelets, context);
		try (RandomAccessFile randAccessFile = new RandomAccessFile(
                logFileName, "r")) {
			long pointer = this.getFilePointer(pointerFileName, config.getResetPtrFileTime());
			randAccessFile.seek(pointer);
			String line = null;
			while((line = randAccessFile.readLine()) != null) {
				this.doLine(analyzelets, context, line);
				long endPointer = randAccessFile.getFilePointer();
				this.writePointerFile(pointerFileName, endPointer, config.getResetPtrFileTime());
			}
			this.doEnd(analyzelets, context);
		} catch (FileNotFoundException e) {
			throw new LogAnalyzeException("找不到目标日志文件: " + logFileName);
		} catch (IOException e) {
			throw new LogAnalyzeException("加载目标日志文件出错:" + logFileName, e);
		} catch(Throwable t) {
			throw new LogAnalyzeException("分析日志出错", t);
		}
	}
	
	/**
	 * 从配置读取log analyzelet
	 * @param config
	 * @return
	 * @throws LogAnalyzeException
	 */
	private List<Analyzelet> loadAnalyzelet(AnalyzeTaskConfig config) throws LogAnalyzeException{
		List<String> analyzeletClazzNames = config.getAnalyzelets();
		if(CollectionUtils.isEmpty(analyzeletClazzNames)) {
			throw new LogAnalyzeException("必须为任务指定至少一个Analyzelet");
		}
		
		List<Analyzelet> analyzelets = new ArrayList<>();
		
		try{
			for(String clazzName :analyzeletClazzNames) {
				Class<?> clazz =  Class.forName(clazzName);
				Analyzelet analyzelet = (Analyzelet)clazz.newInstance();
				analyzelets.add(analyzelet);
			}
		}catch(Throwable e) {
			throw new LogAnalyzeException("Ops!", e);
		}
		
		return analyzelets;
	}
	
	/**
	 * 从配置中获取日志文件名
	 * @param config
	 * @return
	 */
	private String getLogFileName(AnalyzeTaskConfig config) throws LogAnalyzeException{
		String logFileName = config.getLogFileName();
		if(StringUtils.isNotBlank(logFileName)) {
			return logFileName;
		}
		String generatorClazzName = config.getLogFileNameGenerator();
		if(StringUtils.isBlank(generatorClazzName)) {
			throw new LogAnalyzeException("必须为分析任务指定一个目标日志文件名，使用logFileName或者logFileNameGenerator");
		}
		try{
			@SuppressWarnings("unchecked")
			Class<? extends LogFileNameGenerator> nameGeneratorClazz = (Class<? extends LogFileNameGenerator>) Class.forName(config.getLogFileNameGenerator());
			LogFileNameGenerator logFileNameGenerator = nameGeneratorClazz.newInstance();
			return logFileNameGenerator.generateLogFileName();
		}catch(Throwable t) {
			throw new LogAnalyzeException(t);
		}
	}
	
	private void doBegin(List<Analyzelet> analyzelets, AnalyzerContext context) {
		for(Iterator<Analyzelet> iterator = analyzelets.iterator(); iterator.hasNext();) {
			Analyzelet analyzelet = iterator.next();
			try{
				analyzelet.begin(context);
			}catch(Throwable t) {
				iterator.remove();
				analyzelet.onError(context, t);
			}
		}
	}
	
	private void doLine(List<Analyzelet> analyzelets, AnalyzerContext context, String line) {
		line = StringUtils.strip(line);
		if(StringUtils.isNotBlank(line)) {
			for(Iterator<Analyzelet> iterator = analyzelets.iterator(); iterator.hasNext();) {
				Analyzelet analyzelet = iterator.next();
				try{
					analyzelet.doLine(line, context);
				}catch(Throwable t) {
					if(analyzelet.onErrorStop()) {
						iterator.remove();
					}
					analyzelet.onError(context, t);
				}
			}
		}
	}
	
	private void doEnd(List<Analyzelet> analyzelets, AnalyzerContext context) {
		for(Iterator<Analyzelet> iterator = analyzelets.iterator(); iterator.hasNext();) {
			Analyzelet analyzelet = iterator.next();
			try{
				analyzelet.end(context);
			}catch(Throwable t) {
				analyzelet.onError(context, t);
			}
		}
	}
	
	private long getFilePointer(String pointerFileName, long resetTime) throws Exception {
        if (StringUtils.isBlank(pointerFileName)) {
            return 0;
        }
        File pointerFile = new File(pointerFileName);
        long pointer = 0;
        if (pointerFile.exists()) {

            try (Scanner scanner = new Scanner(pointerFile)) {
                String[] lines = new String[2];
                int lineCount = resetTime > 0 ? 2 : 1;
                for (int i = 0; i < lineCount; i++) {
                    lines[i] = scanner.nextLine();
                }

                if (resetTime > 0) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date lastRecordTime = sdf.parse(lines[1]);
                    if (System.currentTimeMillis() - lastRecordTime.getTime() > TimeUnit.MINUTES.toMillis(resetTime)) {
                        writePointerFile(pointerFileName, 0, resetTime);
                        pointer = 0;
                    } else {
                        pointer = Long.parseLong(lines[0]);
                    }
                } else {
                    pointer = Long.parseLong(lines[0]);
                }
            }
        } else {
            writePointerFile(pointerFileName, 0, resetTime);
            pointer = 0;
        }

        return pointer;
    }

    private void writePointerFile(String pointerFile, long pointer, long resetTime)
            throws Exception {
        if (StringUtils.isBlank(pointerFile)) {
            return;
        }
        try (PrintWriter writer = new PrintWriter(pointerFile)) {
            writer.println(pointer);
            if (resetTime > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = sdf.format(new Date());
                writer.println(time);
            }
        }
    }

}
