package loganalyzer.core.test;
import java.io.File;
import java.io.FileFilter;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
//java 监控目录文件
public class Test {
	
	public static void main(String[] args) throws Exception
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					
					
					System.out.println("start fileWatch");
					fileWatch();
					//System.out.println("start watch");
					//watch();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}

			
		}).start();
		
	}
	static class filelisteradapter extends FileAlterationListenerAdaptor{
		
		@Override
		public void onDirectoryChange(File directory) {
			 
			super.onDirectoryChange(directory);
		}
		@Override
		public void onDirectoryCreate(File directory) {
			System.out.println("onDirectoryCreate "+directory);
			super.onDirectoryCreate(directory);
		}
		@Override
		public void onDirectoryDelete(File directory) {
			 
			System.out.println("onDirectoryDelete "+directory);
			super.onDirectoryDelete(directory);
		}
		@Override
		public void onFileChange(File file) {
			 
			super.onFileChange(file);
		}
		@Override
		public void onFileCreate(File file) {
			 
			System.out.println("onFileCreate "+file);
			super.onFileCreate(file);
		}
		@Override
		public void onFileDelete(File file) {
			 
			System.out.println("onFileDelete "+file);
			super.onFileDelete(file);
		}
		@Override
		public void onStart(FileAlterationObserver observer) {
			 
			super.onStart(observer);
		}
		@Override
		public void onStop(FileAlterationObserver observer) {
			 
			super.onStop(observer);
		}
		
	}
	//commons-io-2.3.jar包提供的方法
	private static void fileWatch() throws Exception {
		//文件变更器
		FileAlterationMonitor  monitor = new FileAlterationMonitor(100);//5s扫描一次
		//目录观察者
		FileAlterationObserver observer = new FileAlterationObserver(new File("d:/html/bg"),new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				//System.out.println(pathname.getPath());
				//对感兴趣的文件监听,要返回true
				return true;
			}
		});  
		//observer.addListener(new filelisteradapter());
        //监听器接口
        observer.addListener(new FileAlterationListener() {
			
			@Override
			public void onStop(FileAlterationObserver observer) {
				 
				//System.out.println("onStop "+observer.getDirectory().getPath());
			}
			
			@Override
			public void onStart(FileAlterationObserver observer) {
				 
				//System.out.println("onStart "+observer.getDirectory().getPath());
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
        
        //monitor.stop();
		
		
	}
	
	//java7 nio包提供的方法
	private static void watch() throws Exception
	{
		WatchService watchService=FileSystems.getDefault().newWatchService();
		Paths.get("C:/").register(watchService, 
				StandardWatchEventKinds.ENTRY_CREATE,
				StandardWatchEventKinds.ENTRY_DELETE,
				StandardWatchEventKinds.ENTRY_MODIFY,StandardWatchEventKinds.OVERFLOW);
		
		while(true)
		{
			WatchKey key=watchService.take();
			//watchService.poll(10000, TimeUnit.valueOf("2014-8-26"));
			for(WatchEvent<?> event:key.pollEvents())
			{
				System.out.println(event.context()+"发生了"+event.kind()+"事件"+event.count());
			}
			if(!key.reset())
			{
				break;
			}
		}
	}
	
}
