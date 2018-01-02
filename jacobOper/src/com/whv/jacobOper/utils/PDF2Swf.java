package com.whv.jacobOper.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * doc docx��ʽת��
 */
public class PDF2Swf {
	public static int environment = 1;// ���� 1��windows 2:linux
	private String fileString;// (ֻ�漰pdf2swf·������)
	private String outputPath = "";// ����·�� ����������þ������Ĭ�ϵ�λ��
	private String fileName;
	private File pdfFile;
	private File swfFile;
	private static String contentPath=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession().getServletContext().getRealPath("/");
	private static String swfToolHome=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession().getServletContext().getRealPath(PropOptUtil.getProperties("global.properties", "jacob.swfToolHome"));
	public PDF2Swf(String fileString) {
		ini(fileString);
	}

	/**
	 * ��������file
	 * 
	 * @param fileString
	 */
	public void setFile(String fileString) {
		ini(fileString);
	}

	/**
	 * ��ʼ��
	 * 
	 * @param fileString
	 */
	private void ini(String fileString) {
		this.fileString = fileString;
		fileName = fileString.substring(0, fileString.lastIndexOf("."));
		pdfFile = new File(fileName + ".pdf");
		swfFile = new File(getPinYin(fileName) + ".swf");
	}

	

	/**
	 * ת���� swf
	 */
	@SuppressWarnings("unused")
	public void pdf2swf() {
		Runtime r = Runtime.getRuntime();
		if (!swfFile.exists()) {
			if (pdfFile.exists()) {
				if (environment == 1) {// windows��������
					try {

						Process p = r
								.exec(swfToolHome+File.separator+"pdf2swf.exe"
										+ " -t "
										+ pdfFile.getPath()
										+ " -s flashversion=9 -o "
										+ swfFile.getPath()+ " -f");
						System.out.print(loadStream(p.getInputStream()));
						System.err.print(loadStream(p.getErrorStream()));
						System.out.print(loadStream(p.getInputStream()));
						System.err.println("****swfת���ɹ����ļ������"
								+ swfFile.getPath() + "****");
						if (pdfFile.exists()) {
							pdfFile.delete();
						}

					} catch (IOException e) {
						e.printStackTrace();
						
					}
				} else if (environment == 2) {// linux��������
					try {
						Process p = r.exec("pdf2swf " + pdfFile.getPath()
								+ " -o " + swfFile.getPath() + " -T 9");
						System.out.print(loadStream(p.getInputStream()));
						System.err.print(loadStream(p.getErrorStream()));
						System.err.println("****swfת���ɹ����ļ������"
								+ swfFile.getPath() + "****");
						if (pdfFile.exists()) {
							pdfFile.delete();
						}
					} catch (Exception e) {
						e.printStackTrace();
						
					}
				}
			} else {
				System.out.println("****pdf������,�޷�ת��****");
			}
		} else {
			System.out.println("****swf�Ѿ����ڲ���Ҫת��****");
		}
	}

	static String loadStream(InputStream in) throws IOException {

		int ptr = 0;
		in = new BufferedInputStream(in);
		StringBuffer buffer = new StringBuffer();

		while ((ptr = in.read()) != -1) {
			buffer.append((char) ptr);
		}

		return buffer.toString();
	}

	

	/**
	 * �����ļ�·��
	 * 
	 * @param s
	 */
	public String getswfPath() {
		if (swfFile != null) {
			String tempString = swfFile.getPath();
			// tempString = getPinYin(tempString);
			tempString = tempString.replaceAll("\\\\", "/");
			return tempString;
		} else {
			return "";
		}

	}

	/**
	 * �������·��
	 */
	public void setOutputPath(String outputPath) {
		//String allPath=Thread.currentThread().getContextClassLoader().getResource("").getPath(); 
		 // String contentPath=allPath.substring(1,allPath.indexOf("WEB-INF"));
		  if(outputPath.startsWith(File.separator)){
			  outputPath = contentPath + File.separator+outputPath.substring(1);
		  }else{
			  outputPath = contentPath + File.separator+outputPath;
		  }
		  
		  this.outputPath = outputPath;
		  if(!new File(outputPath).exists()) {
			  new File(outputPath).mkdirs();
		  }
		if (!outputPath.equals("")) {
			String realName = fileName.substring(fileName.lastIndexOf(File.separator)+1);
			// realName = getPinYin(realName);
			if (outputPath.endsWith(File.separator)) {
				swfFile = new File(outputPath + realName + ".swf");
			} else {
				swfFile = new File(outputPath +File.separator+ realName + ".swf");
			}
		}
	}
	public String getFileString() {
		return fileString;
	}

	public void setFileString(String fileString) {
		this.fileString = fileString;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public File getPdfFile() {
		return pdfFile;
	}

	public void setPdfFile(File pdfFile) {
		this.pdfFile = pdfFile;
	}

	public static String getSwfToolHome() {
		return swfToolHome;
	}

	public static void setSwfToolHome(String swfToolHome) {
		PDF2Swf.swfToolHome = swfToolHome;
	}

	public File getSwfFile() {
		return swfFile;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public static String getPinYin(String src) {
		char[] t1 = null;
		t1 = src.toCharArray();
		// System.out.println(t1.length);
		String[] t2 = new String[t1.length];
		// System.out.println(t2.length);
		// ���ú���ƴ������ĸ�ʽ
		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
		t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		t3.setVCharType(HanyuPinyinVCharType.WITH_V);
		String t4 = "";
		int t0 = t1.length;
		try {
			for (int i = 0; i < t0; i++) {
				// �ж��ܷ�Ϊ�����ַ�
				// System.out.println(t1[i]);
				if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
					t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);// �����ֵļ���ȫƴ���浽t2������
					t4 += t2[0];// ȡ���ú���ȫƴ�ĵ�һ�ֶ��������ӵ��ַ���t4��
				} else {
					// ������Ǻ����ַ������ȡ���ַ������ӵ��ַ���t4��
					t4 += Character.toString(t1[i]);
				}
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return t4;
	}
	
}
