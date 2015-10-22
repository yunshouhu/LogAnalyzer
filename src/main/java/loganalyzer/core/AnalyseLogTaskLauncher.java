package loganalyzer.core;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * 命令行入口
 * @author hongze.chi@gmail.com
 * http://my.oschina.net/chihz/blog/55658?p=1
 *
 */
public class AnalyseLogTaskLauncher {

	private static AnalyzerLogTaskExecutor logTaskExecutor = BasicAnalyzerLogTaskExecutor.getInstance();
	private static AnalyzerContext analyzerContext =null;
	public static AnalyzeTaskConfig taskConfig = null;
	
	public static String path = null;
	
    private static void fileWatch() throws Exception {  
        //文件变更器  
        FileAlterationMonitor  monitor = new FileAlterationMonitor(500);//0.5s扫描一次  
        //目录观察者  
        File dir=new File(taskConfig.getLogFileName()).getParentFile();
        
        System.out.println("dir="+dir.getAbsolutePath());
        FileAlterationObserver observer = new FileAlterationObserver(dir);
        //监听器接口  
        observer.addListener(new FileAlterationListener() {  
              
            @Override  
            public void onStop(FileAlterationObserver observer) {   
            }  
              
            @Override  
            public void onStart(FileAlterationObserver observer) {  
                   
                 
            }  
              
            @Override  
            public void onFileDelete(File file) {  
                 System.out.println("onFileDelete "+file.getPath());  
                  
            }  
              
            @Override  
            public void onFileCreate(File file) {  
                 System.out.println("onFileCreate "+file.getPath());  
                  
            }  
              
            @Override  
            public void onFileChange(File file) {  
                   
                 System.out.println("onFileChange "+file.getPath());  
                 try {
                	// path=file.getPath();
					logTaskExecutor.execute(taskConfig, analyzerContext);
					
				} catch (LogAnalyzeException e) {
					e.printStackTrace();
				}
            }  
              
            @Override  
            public void onDirectoryDelete(File file) {  
                   
                System.out.println("onDirectoryDelete "+file.getPath());  
            }  
              
            @Override  
            public void onDirectoryCreate(File file) {  
                System.out.println("onDirectoryCreate "+file.getPath());  
                  
            }  
              
            @Override  
            public void onDirectoryChange(File file) {  
                System.out.println("onDirectoryChange "+file.getPath());  
                  
            }  
        });   
        monitor.addObserver(observer);    
        monitor.start();  
    }
    
    
	//java -cp ./lib/ loganalyzer.core.AnalyseLogTaskLauncher -log ./log4j.xml -task /data/tasks/test_task.yaml
	public static void main(String[] args) {
		
		if(args.length<1)
		{
			System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
			URL taskurl=Thread.currentThread().getContextClassLoader().getResource("task.yaml");
			System.out.println(taskurl);
			System.out.println(taskurl.getPath());
			URL log4jxml=Thread.currentThread().getContextClassLoader().getResource("log4j.xml");
			System.out.println(log4jxml.getPath());
			String path="-log "+log4jxml.getPath()+" -task "+taskurl.getPath();
			
			args=path.split(" ");
		}
		
		//读取命令行参数
		CommandLineParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("log", true, "log4j配置路径");
        options.addOption("task", true, "分析任务配置文件名");
        
        CommandLine cmdline = null;

        try {
            cmdline = parser.parse(options, args, false);
        } catch (org.apache.commons.cli.ParseException e) {
        	System.out.println("");
            e.printStackTrace();
            System.exit(0);
        }
        
        //初始化log4j
        String log4jConfigPath = cmdline.getOptionValue("log");
        if(StringUtils.isNotBlank(log4jConfigPath)){
        	DOMConfigurator.configure(log4jConfigPath);
        }
        
        Logger logger = LogManager.getLogger(AnalyseLogTaskLauncher.class);
        
        //载入Task配置
        String taskConfigPath = cmdline.getOptionValue("task");
       
        if(StringUtils.isNotBlank(taskConfigPath)) {
        	try {
				taskConfig = AnalyzeTaskConfig.loadFromConfigFile(taskConfigPath);
			} catch (Exception e) {
				logger.error("Ops!", e);
				System.exit(0);
			}
        }
        
        try{
        	
        	path=taskConfig.getLogFileName();
        	
        	analyzerContext = buildContext(cmdline, taskConfig);
        			
        	logTaskExecutor.execute(taskConfig, analyzerContext);
        	
        	fileWatch();
        	
        	
        }catch(Exception e) {
        	//logger.error("Ops!", e);
        	e.printStackTrace();
        	
        }finally{
        	logger.info("log analyze task finished!");
        }
	}
	
	private static AnalyzerContext buildContext(CommandLine cmdLine, AnalyzeTaskConfig config) {
		AnalyzerContext analyzerContext = new AnalyzerContext();
		List<String> cmdOptions = config.getCmdOptions();
		//先把命令行参数加进去
		if(CollectionUtils.isNotEmpty(cmdOptions)) {
			for(String cmdOpt : cmdOptions) {
				analyzerContext.addParam(cmdOpt, cmdLine.getOptionValue(cmdOpt));
			}
		}
		//再把配置参数加进去
		Map<String, String> initParams = config.getInitParams();
		analyzerContext.addParam(initParams);
		return analyzerContext;
	}
}
