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
 * �ļ�����������
 * @author huawei
 *
 */
public class FileUtil {
	/**
	 * �����ļ�
	 * @param src Դ�ļ���ַ
	 * @param target Ŀ���ļ���ַ
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
	 * ����Ŀ¼
	 * @param dirSrc ԴĿ¼��ַ
	 * @param dirTarget Ŀ��Ŀ¼��ַ
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
	 * �����ļ�
	 * @param src Դ�ļ���ַ
	 * @param target Ŀ���ļ���ַ
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
	 * ɾ���ļ�
	 * @param path ·��
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
	 * ��ȡĿ¼�µ������ļ��б�
	 * @param strPath Ŀ¼·��
	 * @param filelist ��ǰ�ļ��б�
	 * @return �ļ��б�
	 */
		public static List<File> getFileList(String strPath,List<File> filelist) {
	        File dir = new File(strPath);
	        File[] files = dir.listFiles(); // ���ļ�Ŀ¼���ļ�ȫ����������
	        if (files != null) {
	            for (int i = 0; i < files.length; i++) {
	                if (files[i].isDirectory()) { // �ж����ļ������ļ���
	                    getFileList(files[i].getAbsolutePath(),filelist); // ��ȡ�ļ�����·��
	                } else { 
	                    filelist.add(files[i]);
	                }
	            }

	        }
	        return filelist;
	    }
/**
 * ��ȡĿ¼�µ������ļ��б�
 * @param strPath Ŀ¼·��
 * @param filelist ��ǰ�ļ��б�
 * @param type �ļ���׺
 * @param nameRegex ����������ʽ
 * @return �ļ��б�
 */
	public static List<File> getFileList(String strPath,List<File> filelist,String type,String nameRegex) {
        File dir = new File(strPath);
        FilesFilter fileFilter = new  FilesFilter(type, nameRegex);
        File[] files = dir.listFiles(fileFilter); // ���ļ�Ŀ¼���ļ�ȫ����������
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) { // �ж����ļ������ļ���
                    getFileList(files[i].getAbsolutePath(),filelist,type, nameRegex); // ��ȡ�ļ�����·��
                } else { 
                    filelist.add(files[i]);
                }
            }

        }
        return filelist;
    }
	/**
	 * ��ȡ����������ļ�
	 * @param fileName �ļ���
	 * @param path �ļ�����Ŀ¼
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
	 * �ļ������б������
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
         * �ļ������б���˹��췽��
         * @param type �ļ���չ��
         * @param nameRegex �ļ�����ƥ������
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
	 * �ļ��б������
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
         * �ļ��б���˹��췽��
         * @param type �ļ���չ��
         * @param nameRegex �ļ�����ƥ������
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
