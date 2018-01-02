<%@page import="com.whv.jacobOper.utils.PDF2Swf"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="/tags/web-flexpaper" prefix="fp"  %>
<html>
<head>
<title>在线展示文档</title>
<script type="text/javascript" src="skins/js/flexpaper/js/jquery.min.js"></script>
<script type="text/javascript" src="skins/js/flexpaper/js/flexpaper.js"></script>
<script type="text/javascript" src="skins/js/flexpaper/js/swfobject.js"></script>
</head>
<%

String filePath=(String)request.getAttribute("filePath")==null?"":(String)request.getAttribute("filePath").toString();

int aa = PDF2Swf.environment;
String bb = aa+"";
if(bb.equals("1")){						
	filePath = filePath.replaceAll("\\\\", "/");
	filePath = filePath.substring(filePath.indexOf("/file/")+1);
}
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/"+filePath;
%>
<body>
<fp:show elementId="fp_test" swfFile="<%=basePath %>" conf="
						 Scale : 0.6, 
						 ZoomTransition : 'easeOut',
						 ZoomTime : 0.5,
						 ZoomInterval : 0.2,
						 FitPageOnLoad : false,
						 FitWidthOnLoad : true,
						 PrintEnabled : true,
						 PrintVisible : true,
						 FullScreenAsMaxWindow : false,
						 ProgressiveLoading : true,
						 MinZoomSize : 0.2,
						 MaxZoomSize : 5,
						 SearchMatchAll : true,
						 InitViewMode : 'Portrait',
						 ViewModeToolsVisible : true,
						 ZoomToolsVisible : true,
						 NavToolsVisible : true,
						 CursorToolsVisible : false,
						 SearchToolsVisible : true,
						 localeChain: 'zh_CN'
						 "></fp:show>
</body>
</html>