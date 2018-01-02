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
 * doc docx格式转换
 */
public class PDF2Swf {
	public static int environment = 1;// 环境 1：windows 2:linux
	private String fileString;// (只涉及pdf2swf路径问题)
	private String outputPath = "";// 输入路径 ，如果不设置就输出在默认的位置
	private String fileName;
	private File pdfFile;
	private File swfFile;
	private static String contentPath=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession().getServletContext().getRealPath("/");
	private static String swfToolHome=((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession().getServletContext().getRealPath(PropOptUtil.getProperties("global.properties", "jacob.swfToolHome"));
	public PDF2Swf(String fileString) {
		ini(fileString);
	}

	/**
	 * 重新设置file
	 * 
	 * @param fileString
	 */
	public void setFile(String fileString) {
		ini(fileString);
	}

	/**
	 * 初始化
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
	 * 转换成 swf
	 */
	@SuppressWarnings("unused")
	public void pdf2swf() {
		Runtime r = Runtime.getRuntime();
		if (!swfFile.exists()) {
			if (pdfFile.exists()) {
				if (environment == 1) {// windows环境处理
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
						System.err.println("****swf转换成功，文件输出："
								+ swfFile.getPath() + "****");
						if (pdfFile.exists()) {
							pdfFile.delete();
						}

					} catch (IOException e) {
						e.printStackTrace();
						
					}
				} else if (environment == 2) {// linux环境处理
					try {
						Process p = r.exec("pdf2swf " + pdfFile.getPath()
								+ " -o " + swfFile.getPath() + " -T 9");
						System.out.print(loadStream(p.getInputStream()));
						System.err.print(loadStream(p.getErrorStream()));
						System.err.println("****swf转换成功，文件输出："
								+ swfFile.getPath() + "****");
						if (pdfFile.exists()) {
							pdfFile.delete();
						}
					} catch (Exception e) {
						e.printStackTrace();
						
					}
				}
			} else {
				System.out.println("****pdf不存在,无法转换****");
			}
		} else {
			System.out.println("****swf已经存在不需要转换****");
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
	 * 返回文件路径
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
	 * 设置输出路径
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
		// 设置汉字拼音输出的格式
		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
		t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		t3.setVCharType(HanyuPinyinVCharType.WITH_V);
		String t4 = "";
		int t0 = t1.length;
		try {
			for (int i = 0; i < t0; i++) {
				// 判断能否为汉字字符
				// System.out.println(t1[i]);
				if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
					t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);// 将汉字的几种全拼都存到t2数组中
					t4 += t2[0];// 取出该汉字全拼的第一种读音并连接到字符串t4后
				} else {
					// 如果不是汉字字符，间接取出字符并连接到字符串t4后
					t4 += Character.toString(t1[i]);
				}
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return t4;
	}
	
}
