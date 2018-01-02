<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<title>在线展示测试</title>
<script type="text/javascript">
function onlineShow(){
	var path = document.getElementById("file_input").value;
	if(!path){
		alert("请选择需要展示的文件！")
		return;
	}
	path = encodeURI(encodeURI(path));
	document.getElementById("show_frame").src="fileManage.so?method=show&file="+path;
}
</script>
</head>
<body>
<input type="file" name="file_input" id="file_input"/> 
<input type="button" name="show_btn" value="在线展示" onclick="onlineShow()"/>
<iframe src="" id="show_frame"  width="100%" height="1200" scrolling="yes" frameborder="0" style="border: 0px;"></iframe>
</body>
</html>