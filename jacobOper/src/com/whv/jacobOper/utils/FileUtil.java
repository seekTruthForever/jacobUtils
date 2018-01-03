package com.whv.jacobOper.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * 文件操作工具类
 * @author huawei
 *
 */
public class FileUtil {
	/**
	 * 复制文件
	 * @param src 源文件地址
	 * @param target 目标文件地址
	 */
	public static void copy(String src,String target) {
		File file = new File(src);
		File targetFile = new File(target);
		src = file.getAbsolutePath();
		target = targetFile.getAbsolutePath();
		if(!file.exists()) return;
		if(file.isDirectory()) {
			copyDirs(src, target);
		}else {
			copyFile(src, target);
		}
	}
	/**
	 * 复制目录
	 * @param dirSrc 源目录地址
	 * @param dirTarget 目标目录地址
	 */
	private static void copyDirs(String dirSrc,String dirTarget) {
		List<File> files = getFileList(dirSrc, new ArrayList<File>());
		for(File file : files) {
			String src = file.getAbsolutePath();
			String target = dirTarget+File.separator+src.substring(src.lastIndexOf(dirSrc)+1);
			if(!new File(target).getParentFile().exists()) {
				new File(target).getParentFile().mkdirs();
			}
			copyFile(src,target);
		}
	}
	/**
	 * 复制文件
	 * @param src 源文件地址
	 * @param target 目标文件地址
	 */
	private static void copyFile(String src,String target) {
		 InputStream input = null;
		 OutputStream out = null;
		 File srcFile = new File(src);
		 File targetFile = new File(target);
		 if(!srcFile.exists()) return;
		try {
			if(!targetFile.getParentFile().exists()) {
				targetFile.getParentFile().mkdirs();
			}
			input = new FileInputStream(srcFile);
			out = new FileOutputStream(targetFile);
	         byte[] b = new byte[1024 * 10];  
	         int len = -1;  
	         while ((len = input.read(b)) != -1) {  
	             out.write(b, 0, len);  
	         }  
	         out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {
			try {
				if(input != null) input.close();
				if(out != null) out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}  
		}
	}
	/**
	 * 删除文件
	 * @param path 路径
	 */
	public static boolean delete(String path) {
		File file = new File(path);
		if (!file.exists())  
            return false;  
        if (file.isFile()) {  
            return file.delete();  
        }  
        File[] childFiles = file.listFiles();  
        for (File f : childFiles) {  
        	delete(f.getAbsolutePath());  
        }  
        return file.delete();  
	}
	/**
	 * 获取目录下的所有文件列表
	 * @param strPath 目录路径
	 * @param filelist 当前文件列表
	 * @return 文件列表
	 */
		public static List<File> getFileList(String strPath,List<File> filelist) {
	        File dir = new File(strPath);
	        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
	        if (files != null) {
	            for (int i = 0; i < files.length; i++) {
	                if (files[i].isDirectory()) { // 判断是文件还是文件夹
	                    getFileList(files[i].getAbsolutePath(),filelist); // 获取文件绝对路径
	                } else { 
	                    filelist.add(files[i]);
	                }
	            }

	        }
	        return filelist;
	    }
/**
 * 获取目录下的所有文件列表
 * @param strPath 目录路径
 * @param filelist 当前文件列表
 * @param type 文件后缀
 * @param nameRegex 名称正则表达式
 * @return 文件列表
 */
	public static List<File> getFileList(String strPath,List<File> filelist,String type,String nameRegex) {
        File dir = new File(strPath);
        FilesFilter fileFilter = new  FilesFilter(type, nameRegex);
        File[] files = dir.listFiles(fileFilter); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    getFileList(files[i].getAbsolutePath(),filelist,type, nameRegex); // 获取文件绝对路径
                } else { 
                    filelist.add(files[i]);
                }
            }

        }
        return filelist;
    }
	/**
	 * 获取重命名后的文件
	 * @param fileName 文件名
	 * @param path 文件保存目录
	 * @return File
	 */
	public static File getRenameFile(String fileName,String path) {
		if(path==null) path="";
		if(path.endsWith(fileName)) {
			path = path.substring(0, path.lastIndexOf(fileName));
		}
		File dir = new File(path);
		if(!dir.exists()) {
			dir.mkdirs();
		}else {
			if(!dir.isDirectory()) {
				dir = new File(path+"_DIR");
				if(!dir.exists()) {
					dir.mkdirs();
				}
			}
		}
		String type = fileName.substring(fileName.lastIndexOf("."));
		String fileNamePre = fileName.substring(0, fileName.lastIndexOf("."));
		String nameRegex = "\\Q"+fileNamePre+"\\E(\\(\\d+\\))?\\Q"+type+"\\E";
		NamesFilter namesFilter = new  NamesFilter(type, nameRegex);
		File[] files = dir.listFiles(namesFilter);
		String fileNameStr = fileName;
		int i=0;
		for(File file :files) {
			String subName = file.getName();
			if(subName.equals(fileName)) {
				if(i==0) {
					i=1;
				}
			}else {
				int fileNum = Integer.valueOf(subName.substring(subName.lastIndexOf("(")+1, subName.lastIndexOf(")")));
				if(i<=fileNum) {
					i=fileNum+1;
				}
			}
		}
		if(i>0) {
			fileNameStr = fileNamePre+"("+i+")"+type;
		}
		return new File(dir.getPath()+File.separator+fileNameStr);
	}
	/**
	 * 文件名称列表过滤器
	 * @author huawei
	 *
	 */
	protected static class NamesFilter implements FilenameFilter{  
        private String type;  
        private String nameRegex;
        public NamesFilter(){  
            this.type = "";  
            this.nameRegex = ".*";
        }  
        /**
         * 文件名称列表过滤构造方法
         * @param type 文件扩展名
         * @param nameRegex 文件名称匹配正则
         */
        public NamesFilter(String type,String nameRegex){  
            this.type = type==null?"":type;  
            this.nameRegex = nameRegex==null?".*":nameRegex;
        }  
        public boolean accept(File dir,String name){  
            return name.endsWith(type)&&name.matches(nameRegex);  
        }  
    }  
	/**
	 * 文件列表过滤器
	 * @author huawei
	 *
	 */
	protected static class FilesFilter implements FileFilter{  
        private String type;  
        private String nameRegex;
        public FilesFilter(){  
            this.type = "";  
            this.nameRegex = ".*";
        }  
        /**
         * 文件列表过滤构造方法
         * @param type 文件扩展名
         * @param nameRegex 文件名称匹配正则
         */
        public FilesFilter(String type,String nameRegex){  
            this.type = type==null?"":type;  
            this.nameRegex = nameRegex==null?".*":nameRegex;
        }  
		public boolean accept(File pathname) {
			return pathname.isDirectory()||
					(pathname.getName().endsWith(type)&&pathname.getName().matches(nameRegex));
		}  
    }  
}
