package com.whv.jacobOper.utils;

import java.io.File;
import java.util.Date;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/***
 *jacob工具类：打印、转换为pdf
 * @author huawei
 *
 * @date 2017/8/15
 *
 */
public class JacobUtils {
    private static final int wdFormatPDF = 17;
    private static final int xlTypePDF = 0;
    private static final int ppSaveAsPDF = 32;
    private String fileString;// (只涉及pdf2swf路径问题)
	private String fileName;
	private String pdfFileString;
	public JacobUtils(String fileString) {
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
		File pdfFile = FileUtil.getRenameFile(fileName.substring(fileName.lastIndexOf(File.separator)+1)+".pdf", fileName + ".pdf");
		pdfFileString = pdfFile.getAbsolutePath();
	}
	/**
	 * 转化为pdf文件
	 * @return pdf路径
	 */
	public String convert2PDF() {
        
        int time = convert2PDF(fileString,pdfFileString);
                
        if (time == -4) {
            System.out.println("转化失败，未知错误...");
        } else if(time == -3) {
            System.out.println("原文件就是PDF文件,无需转化...");
        } else if (time == -2) {
            System.out.println("转化失败，文件不存在...");
        }else if(time == -1){
            System.out.println("转化失败，请重新尝试...");
        }else if (time < -4) {
            System.out.println("转化失败，请重新尝试...");
        }else {
            System.out.println("转化成功，用时：  " + time + "s...");
        }
        return pdfFileString;
    }

    /***
     * 判断需要转化文件的类型（Excel、Word、ppt）
     * 
     * @param inputFile
     * @param pdfFile
     */
    public static int convert2PDF(String inputFile, String pdfFile) {
        String kind = getFileSufix(inputFile);
        File file = new File(inputFile);
        if (!file.exists()) {
            return -2;//文件不存在
        }
        if (kind.equals("pdf")) {
        	FileUtil.copy(inputFile, pdfFile);
            return -3;//原文件就是PDF文件
        }
        if (kind.equals("doc")||kind.equals("docx")||kind.equals("txt")) {
            return JacobUtils.word2PDF(inputFile, pdfFile);
        }else if (kind.equals("ppt")||kind.equals("pptx")) {
            return JacobUtils.ppt2PDF(inputFile, pdfFile);
        }else if(kind.equals("xls")||kind.equals("xlsx")){
            return JacobUtils.Ex2PDF(inputFile, pdfFile);
        }else {
            return -4;
        }
    }

    /***
     * 判断文件类型
     * 
     * @param fileName
     * @return
     */
    public static String getFileSufix(String fileName) {
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(splitIndex + 1);
    }

    /***
     * 
     * Word转PDF
     * 
     * @param inputFile
     * @param pdfFile
     * @return
     */

    public static int word2PDF(String inputFile, String pdfFile) {
        // TODO Auto-generated method stub
    	ComThread.InitSTA();
        try {
            // 打开Word应用程序
        	ActiveXComponent app = new ActiveXComponent("Word.Application");
            System.out.println("开始转化Word为PDF...");
            long date = new Date().getTime();
            // 设置Word不可见
            app.setProperty("Visible", new Variant(false));
            // 禁用宏
            app.setProperty("AutomationSecurity", new Variant(3));
            // 获得Word中所有打开的文档，返回documents对象
            Dispatch docs = app.getProperty("Documents").toDispatch();
            // 调用Documents对象中Open方法打开文档，并返回打开的文档对象Document
            Dispatch doc = Dispatch.call(docs, "Open", inputFile, false, true).toDispatch();
            /***
             * 
             * 调用Document对象的SaveAs方法，将文档保存为pdf格式
             * 
             * Dispatch.call(doc, "SaveAs", pdfFile, wdFormatPDF
             * word保存为pdf格式宏，值为17 )
             * 
             */
            Dispatch.call(doc, "ExportAsFixedFormat", pdfFile, wdFormatPDF);// word保存为pdf格式宏，值为17
            System.out.println(doc);
            // 关闭文档
            long date2 = new Date().getTime();
            int time = (int) ((date2 - date) / 1000);
            Dispatch.call(doc, "Close", false);
            // 关闭Word应用程序
            app.invoke("Quit", 0);
            return time;
        } catch (Exception e) {
            // TODO: handle exception
            return -1;
        }finally{
        	//释放资源
			ComThread.Release();
        }

    }

    /***
     * 
     * Excel转化成PDF
     * 
     * @param inputFile
     * @param pdfFile
     * @return
     */
    public static int Ex2PDF(String inputFile, String pdfFile) {
        try {

            ComThread.InitSTA(true);
            ActiveXComponent ax = new ActiveXComponent("Excel.Application");
            System.out.println("开始转化Excel为PDF...");
            long date = new Date().getTime();
            ax.setProperty("Visible", false);
            ax.setProperty("AutomationSecurity", new Variant(3)); // 禁用宏
            Dispatch excels = ax.getProperty("Workbooks").toDispatch();

            Dispatch excel = Dispatch
                    .invoke(excels, "Open", Dispatch.Method,
                            new Object[] { inputFile, new Variant(false), new Variant(false) }, new int[9])
                    .toDispatch();
            // 转换格式
            Dispatch.invoke(excel, "ExportAsFixedFormat", Dispatch.Method, new Object[] { new Variant(0), // PDF格式=0
                    pdfFile, new Variant(xlTypePDF) // 0=标准 (生成的PDF图片不会变模糊) 1=最小文件
                                            // (生成的PDF图片糊的一塌糊涂)
            }, new int[1]);

            // 这里放弃使用SaveAs
            /*
             * Dispatch.invoke(excel,"SaveAs",Dispatch.Method,new Object[]{
             * outFile, new Variant(57), new Variant(false), new Variant(57),
             * new Variant(57), new Variant(false), new Variant(true), new
             * Variant(57), new Variant(true), new Variant(true), new
             * Variant(true) },new int[1]);
             */
            long date2 = new Date().getTime();
            int time = (int) ((date2 - date) / 1000);
            Dispatch.call(excel, "Close", new Variant(false));

            if (ax != null) {
                ax.invoke("Quit", new Variant[] {});
                ax = null;
            }
            return time;
        } catch (Exception e) {
            // TODO: handle exception
            return -1;
        }finally{
        	//释放资源
			ComThread.Release();
        }
    }

    /***
     * ppt转化成PDF
     * 
     * @param inputFile
     * @param pdfFile
     * @return
     */
    public static int ppt2PDF(String inputFile, String pdfFile) {
        try {
            ComThread.InitSTA(true);
            ActiveXComponent app = new ActiveXComponent("PowerPoint.Application");
//            app.setProperty("Visible", false);
            System.out.println("开始转化PPT为PDF...");
            long date = new Date().getTime();
            Dispatch ppts = app.getProperty("Presentations").toDispatch();
            Dispatch ppt = Dispatch.call(ppts, "Open", inputFile, true, // ReadOnly
                    true, // Untitled指定文件是否有标题
                    false// WithWindow指定文件是否可见
            ).toDispatch();
            Dispatch.invoke(ppt, "SaveAs", Dispatch.Method, new Object[]{
                    pdfFile,new Variant(ppSaveAsPDF)},new int[1]);
            System.out.println("PPT");
            Dispatch.call(ppt, "Close");
            long date2 = new Date().getTime();
            int time = (int) ((date2 - date) / 1000);
            app.invoke("Quit");
            return time;
        } catch (Exception e) {
            // TODO: handle exception
            return -1;
        }finally{
        	//释放资源
			ComThread.Release();
        }
    }
    /**
     * 打印word文档
     * @param inputFile
     */
    public static void printWord(String inputFile){
    	System.out.println("开始打印");
		ComThread.InitSTA();
		ActiveXComponent word=new ActiveXComponent("Word.Application");
		Dispatch doc=null;
		Dispatch.put(word, "Visible", new Variant(false));
		Dispatch docs=word.getProperty("Documents").toDispatch();
		doc=Dispatch.call(docs, "Open", inputFile).toDispatch();
		try {
			Dispatch.call(doc, "PrintOut");//打印
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("打印失败");
		}finally{
			try {
				if(doc!=null){
					Dispatch.call(doc, "Close",new Variant(0));
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			//释放资源
			ComThread.Release();
		}
    }
}