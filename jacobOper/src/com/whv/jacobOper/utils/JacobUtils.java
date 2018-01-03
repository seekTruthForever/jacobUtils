package com.whv.jacobOper.utils;

import java.io.File;
import java.util.Date;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/***
 *jacob�����ࣺ��ӡ��ת��Ϊpdf
 * @author huawei
 *
 * @date 2017/8/15
 *
 */
public class JacobUtils {
    private static final int wdFormatPDF = 17;
    private static final int xlTypePDF = 0;
    private static final int ppSaveAsPDF = 32;
    private String fileString;// (ֻ�漰pdf2swf·������)
	private String fileName;
	private String pdfFileString;
	public JacobUtils(String fileString) {
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
		File pdfFile = FileUtil.getRenameFile(fileName.substring(fileName.lastIndexOf(File.separator)+1)+".pdf", fileName + ".pdf");
		pdfFileString = pdfFile.getAbsolutePath();
	}
	/**
	 * ת��Ϊpdf�ļ�
	 * @return pdf·��
	 */
	public String convert2PDF() {
        
        int time = convert2PDF(fileString,pdfFileString);
                
        if (time == -4) {
            System.out.println("ת��ʧ�ܣ�δ֪����...");
        } else if(time == -3) {
            System.out.println("ԭ�ļ�����PDF�ļ�,����ת��...");
        } else if (time == -2) {
            System.out.println("ת��ʧ�ܣ��ļ�������...");
        }else if(time == -1){
            System.out.println("ת��ʧ�ܣ������³���...");
        }else if (time < -4) {
            System.out.println("ת��ʧ�ܣ������³���...");
        }else {
            System.out.println("ת���ɹ�����ʱ��  " + time + "s...");
        }
        return pdfFileString;
    }

    /***
     * �ж���Ҫת���ļ������ͣ�Excel��Word��ppt��
     * 
     * @param inputFile
     * @param pdfFile
     */
    public static int convert2PDF(String inputFile, String pdfFile) {
        String kind = getFileSufix(inputFile);
        File file = new File(inputFile);
        if (!file.exists()) {
            return -2;//�ļ�������
        }
        if (kind.equals("pdf")) {
        	FileUtil.copy(inputFile, pdfFile);
            return -3;//ԭ�ļ�����PDF�ļ�
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
     * �ж��ļ�����
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
     * WordתPDF
     * 
     * @param inputFile
     * @param pdfFile
     * @return
     */

    public static int word2PDF(String inputFile, String pdfFile) {
        // TODO Auto-generated method stub
    	ComThread.InitSTA();
        try {
            // ��WordӦ�ó���
        	ActiveXComponent app = new ActiveXComponent("Word.Application");
            System.out.println("��ʼת��WordΪPDF...");
            long date = new Date().getTime();
            // ����Word���ɼ�
            app.setProperty("Visible", new Variant(false));
            // ���ú�
            app.setProperty("AutomationSecurity", new Variant(3));
            // ���Word�����д򿪵��ĵ�������documents����
            Dispatch docs = app.getProperty("Documents").toDispatch();
            // ����Documents������Open�������ĵ��������ش򿪵��ĵ�����Document
            Dispatch doc = Dispatch.call(docs, "Open", inputFile, false, true).toDispatch();
            /***
             * 
             * ����Document�����SaveAs���������ĵ�����Ϊpdf��ʽ
             * 
             * Dispatch.call(doc, "SaveAs", pdfFile, wdFormatPDF
             * word����Ϊpdf��ʽ�ֵ꣬Ϊ17 )
             * 
             */
            Dispatch.call(doc, "ExportAsFixedFormat", pdfFile, wdFormatPDF);// word����Ϊpdf��ʽ�ֵ꣬Ϊ17
            System.out.println(doc);
            // �ر��ĵ�
            long date2 = new Date().getTime();
            int time = (int) ((date2 - date) / 1000);
            Dispatch.call(doc, "Close", false);
            // �ر�WordӦ�ó���
            app.invoke("Quit", 0);
            return time;
        } catch (Exception e) {
            // TODO: handle exception
            return -1;
        }finally{
        	//�ͷ���Դ
			ComThread.Release();
        }

    }

    /***
     * 
     * Excelת����PDF
     * 
     * @param inputFile
     * @param pdfFile
     * @return
     */
    public static int Ex2PDF(String inputFile, String pdfFile) {
        try {

            ComThread.InitSTA(true);
            ActiveXComponent ax = new ActiveXComponent("Excel.Application");
            System.out.println("��ʼת��ExcelΪPDF...");
            long date = new Date().getTime();
            ax.setProperty("Visible", false);
            ax.setProperty("AutomationSecurity", new Variant(3)); // ���ú�
            Dispatch excels = ax.getProperty("Workbooks").toDispatch();

            Dispatch excel = Dispatch
                    .invoke(excels, "Open", Dispatch.Method,
                            new Object[] { inputFile, new Variant(false), new Variant(false) }, new int[9])
                    .toDispatch();
            // ת����ʽ
            Dispatch.invoke(excel, "ExportAsFixedFormat", Dispatch.Method, new Object[] { new Variant(0), // PDF��ʽ=0
                    pdfFile, new Variant(xlTypePDF) // 0=��׼ (���ɵ�PDFͼƬ�����ģ��) 1=��С�ļ�
                                            // (���ɵ�PDFͼƬ����һ����Ϳ)
            }, new int[1]);

            // �������ʹ��SaveAs
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
        	//�ͷ���Դ
			ComThread.Release();
        }
    }

    /***
     * pptת����PDF
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
            System.out.println("��ʼת��PPTΪPDF...");
            long date = new Date().getTime();
            Dispatch ppts = app.getProperty("Presentations").toDispatch();
            Dispatch ppt = Dispatch.call(ppts, "Open", inputFile, true, // ReadOnly
                    true, // Untitledָ���ļ��Ƿ��б���
                    false// WithWindowָ���ļ��Ƿ�ɼ�
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
        	//�ͷ���Դ
			ComThread.Release();
        }
    }
    /**
     * ��ӡword�ĵ�
     * @param inputFile
     */
    public static void printWord(String inputFile){
    	System.out.println("��ʼ��ӡ");
		ComThread.InitSTA();
		ActiveXComponent word=new ActiveXComponent("Word.Application");
		Dispatch doc=null;
		Dispatch.put(word, "Visible", new Variant(false));
		Dispatch docs=word.getProperty("Documents").toDispatch();
		doc=Dispatch.call(docs, "Open", inputFile).toDispatch();
		try {
			Dispatch.call(doc, "PrintOut");//��ӡ
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("��ӡʧ��");
		}finally{
			try {
				if(doc!=null){
					Dispatch.call(doc, "Close",new Variant(0));
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			//�ͷ���Դ
			ComThread.Release();
		}
    }
}