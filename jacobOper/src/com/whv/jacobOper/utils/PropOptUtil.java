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
 * �����ļ�����������
 * @author huawei
 *
 */
public class PropOptUtil {
	/**
	 * �������ļ��л�ȡֵ
	 * @param key ��������
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
	 * д������ֵ
	 * @param filePath �����ļ�·��
	 * @param key ��������
	 * @param value ����ֵ
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
			properties.store(new OutputStreamWriter(out,"UTF-8"), "�޸������ļ�"+url);
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
