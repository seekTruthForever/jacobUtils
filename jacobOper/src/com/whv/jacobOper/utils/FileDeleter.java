package com.whv.jacobOper.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
/**
 * 定时删除某文件夹下的文件
 * @author huawei
 * @date 2017/8/16
 */

public class FileDeleter extends TimerTask {
	private Timer timer = new Timer();
	private static boolean isRunning = false;
	private static List<String> dirs = null;

	public FileDeleter() {

	}

	private static FileDeleter fileDeleter = new FileDeleter();

	//静态工厂方法   
	public static FileDeleter getInstance() {
		return fileDeleter;
	}

	private Calendar calendar = Calendar.getInstance();

	/**
	 * 关闭，停止获取数据
	 */
	public void stopTask() {
		timer.cancel();
		isRunning = false;

	}

	private static void delFiles(List<String> dirList) {
		for(int i=0;i<dirList.size();i++){
			File f = new File(dirList.get(i));
			File[] files = null;
			if (f.exists()&&f.isDirectory()) {
				System.out.println("........删除 "+f.getName()+" 目录下的文件...........");
				files = f.listFiles();
				for (int j = 0; j < files.length; j++) {
					if(new Date().getTime()-files[j].lastModified()>5*60*1000){//删除5分钟之前的文件
						files[j].delete();
					}
				}
			}
		}
	}

	public void startTask(String dirPath) {
		setDirs(dirPath);
		if (!isRunning) {//若定时器不在运行时，新建一个定时器
			System.out.println("........新建一个定时任务：定时删除某文件夹下的文件...........");
			timer = new Timer();
			timer.scheduleAtFixedRate(this, new Date(), 1000 * 60 * 60);//60分钟转化为毫秒    
			isRunning = true;
		}
	}

	public void run() {
		delFiles(getDirs());
		//在每天的23:30后不再执行
		if (calendar.get(Calendar.HOUR_OF_DAY) == 23
				&& calendar.get(Calendar.MINUTE) == 30) {
			this.stopTask();
		}
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public static boolean isRunning() {
		return isRunning;
	}

	public static void setRunning(boolean isRunning) {
		FileDeleter.isRunning = isRunning;
	}

	public static List<String> getDirs() {
		return dirs==null?new ArrayList<String>():dirs;
	}

	public static void setDirs(String dir) {
		dirs = dirs==null?new ArrayList<String>():dirs;
		if(!dirs.contains(dir)){
			dirs.add(dir);
		}
	}
}