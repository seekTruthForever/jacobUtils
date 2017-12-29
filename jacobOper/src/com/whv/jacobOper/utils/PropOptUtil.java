package com.whv.jacobOper.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

/**
 * 属性文件操作工具类
 * @author huawei
 *
 */
public class PropOptUtil {
	/**
	 * 从属性文件中获取值
	 * @param key 属性名称
	 * @return
	 */
	public static String getProperties(String filePath,String key) {
		Properties properties = new Properties();
		String value = null;
		InputStream in = PropOptUtil.class.getClassLoader()
				.getResourceAsStream(filePath);
		try {
			properties.load(new InputStreamReader(in,"UTF-8"));
			value = properties.getProperty(key).trim();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return value;
	}
	/**
	 * 写入属性值
	 * @param filePath 属性文件路径
	 * @param key 属性名称
	 * @param value 属性值
	 */
	public static void createProperties(String filePath,String key,String value){
		Properties properties = new Properties();
		String url = PropOptUtil.class.getClassLoader().getResource(filePath).getPath();
		InputStream in = null;
		try {
			File file = new File(url);
			if(!file.exists()){
				file.createNewFile();
			}
			in = new FileInputStream(file);
			properties.load(new InputStreamReader(in,"UTF-8"));
			properties.setProperty(key, value);
			FileOutputStream out = new FileOutputStream(file);
			properties.store(new OutputStreamWriter(out,"UTF-8"), "修改属性文件"+url);
		} catch (IOException e){
			e.printStackTrace();
		}finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
