<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="entity.User" %>
<%@ page import="DAO.UserDAO" %>
<%@ page import="DAO.FileDAO" %>
<%
	User user = (User)session.getAttribute("user");
	FileDAO fileDAO = (FileDAO)session.getAttribute("fileDAO");
%>
<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<title>个人网络存储</title>
	<script src="utility.js"></script>
	<!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
	 <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
 	<!-- 可选的 Bootstrap 主题文件 -->
 	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
 	<script src="https://cdn.staticfile.org/jquery/1.10.2/jquery.min.js"></script>
 	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
</head>

<%
	if(user == null){%>
		<script>window.alert("你还没有登录");
				window.location="login.html";
		</script>
<% 	}else{
%>
<body onload="loadExternalPage('pages/files.jsp')">
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-8 col-md-offset-2">
				
				<!-- 头部导航栏 -->
				<nav class="navbar">
					<div class="container">
						<div class="navbar-header">
							<a href="#" style="color:white;" class="navbar-brand">网络存储</a>
						</div>
					</div>
				</nav>
				
				<!-- 用户头像和信息 -->
				<div class="row card" id="user-block">
					<div class="col-md-3 col-md-offset-1" align="center">
						<img class="image-circle" src="image/user/default.png" alt="用户头像" width="100px" height="100px"><br>
						<a href="#">修改头像</a>
					</div>
					<div class="col-md-8">
						<ul>
							<li>用户名：<%=user.getUsername() %></li>
							<li>角色：普通用户</li>
							<li>总容量：500MB</li>
							<li><a href="login.html">注销</a></li>
						</ul>
					</div>
				</div>
				
				<!-- 存储用量和基本分析 -->
				<div class="row card" id="analysis-block">
					<div class="col-md-8" align="center">
						<div class="progress">
  							<div class="progress-bar" style="width:<%=user.getUsagePersent()%>"></div>
						</div>
						<p>存储空间：<%=User.getStandardSize(user.getUsed_space()) %>/500M(<%=user.getUsagePersent() %>)</p>
					</div>
					<div class="col-md-4" align="right">
						<button class="btn btn-info">扩容</button>
						<button class="btn btn-warning">清空</button>
					</div>
				</div>
				
				<!-- 各种外部加载内容 -->
				<div class="row card" id="main">
					
				</div>
			</div>
			
			<!-- 侧边栏 -->
			<div class="col-md-1 sidebar">
				<ul class="nav sidebar-nav">
					<li class="nav-item"><a href="#" class="side-txt" onclick="loadExternalPage('pages/my-share.jsp')">我的分享</a></li><hr>
					<li class="nav-item"><a href="#" class="side-txt">用户组</a></li><hr>
					<li class="nav-item"><a href="#" class="side-txt" onclick="loadExternalPage('pages/all-share.jsp')">全网分享</a></li><hr>
				</ul>
			</div>
		</div>
	</div>
</body>
<script>
function checkUploadFile(){
	if(frm.file.value == ""){
		alert("请选择要上传的文件");
		return false;
	}
	<% int space = user.getUsed_space(); int max_space = user.getMax_space();%>
	var f = document.getElementById('fileUpload').files[0];
	if(f.size + <%= space%> > <%= max_space%>){
		alert("文件大小过大，超出了容量限制");
		return false;
	}
	return true;
}

function confirmDelete(filename){
	return confirm("确认删除文件名为：" + filename + "的文件？");
}


</script>
<style>
body{background-color:#efecec;}
.sidebar{background-color:#4e73df; height:600px; padding-top:100px;}
.navbar{background-color:#4e73df;}
.card{background-color:white; margin:30px; padding-left:50px; padding-right:50px; padding-top:25px;}
.side-txt{color:white;}
</style>
</html>

<%}%>