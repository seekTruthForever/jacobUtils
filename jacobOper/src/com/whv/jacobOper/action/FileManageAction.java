package com.whv.jacobOper.action;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.URIDereferencer;

import com.whv.jacobOper.constant.Constant;
import com.whv.jacobOper.utils.FileDeleter;
import com.whv.jacobOper.utils.JacobUtils;
import com.whv.jacobOper.utils.PDF2Swf;

/**
 * 文件管理servlet
 * @author huawei
 *
 */
public class FileManageAction extends HttpServlet {
	/**
	 * get
	 */
	protected void doGet(HttpServletRequest req,HttpServletResponse rep) throws ServletException, IOException{
		String uri = req.getRequestURI();
		uri=uri==null?"":uri;
		String method = req.getParameter("method");
		if(method==null || method.isEmpty()) {
			method="show";
		}
		if("show".equals(method)){
			String file = req.getParameter("file");
			if(file == null) {
				RequestDispatcher dispatcher = req.getRequestDispatcher(Constant.getURL("__default"));
				dispatcher.forward(req, rep);
				return;
			}
			file=URLDecoder.decode(file, "UTF-8");
			 JacobUtils jacobUtils = new JacobUtils(file);
			   String pdfPath = jacobUtils.convert2PDF();
			   PDF2Swf pdf2Swf = new PDF2Swf(pdfPath);
			   pdf2Swf.setOutputPath("/file/");
			   pdf2Swf.pdf2swf();
			   String filePath = pdf2Swf.getswfPath();
			   FileDeleter.getInstance().startTask(pdf2Swf.getOutputPath());
			   req.setAttribute("filePath", filePath);
			RequestDispatcher dispatcher = req.getRequestDispatcher(Constant.getURL("fileManage.show"));
			dispatcher.forward(req, rep);
		}else {
			RequestDispatcher dispatcher = req.getRequestDispatcher(Constant.getURL("__default"));
			dispatcher.forward(req, rep);
		}
	}
	/**
	 * post
	 */
	protected void doPost(HttpServletRequest req,HttpServletResponse rep) throws ServletException, IOException{
		req.setCharacterEncoding("UTF-8");
		String uri = req.getRequestURI();
		uri=uri==null?"":uri;
		String method = req.getParameter("method");
		if("show".equals(method)){
			String file = req.getParameter("file");
			if(file == null) {
				RequestDispatcher dispatcher = req.getRequestDispatcher(Constant.getURL("__default"));
				dispatcher.forward(req, rep);
				return;
			}
			 JacobUtils jacobUtils = new JacobUtils(file);
			   String pdfPath = jacobUtils.convert2PDF();
			   PDF2Swf pdf2Swf = new PDF2Swf(pdfPath);
			   pdf2Swf.setOutputPath("/file/");
			   pdf2Swf.pdf2swf();
			   String filePath = pdf2Swf.getswfPath();
			   FileDeleter.getInstance().startTask(pdf2Swf.getOutputPath());
			   req.setAttribute("filePath", filePath);
			RequestDispatcher dispatcher = req.getRequestDispatcher(Constant.getURL("fileManage.show"));
			dispatcher.forward(req, rep);
		}else {
			RequestDispatcher dispatcher = req.getRequestDispatcher(Constant.getURL("__default"));
			dispatcher.forward(req, rep);
		}
	}
	
}
