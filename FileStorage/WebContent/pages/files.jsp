<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="entity.FileBean" %>
<%@ page import="DAO.FileDAO" %>
<%@ page import="java.util.*" %>
<%@ page import="entity.User" %>
<div class="row">

	<!--  搜索表单 -->
	<form class="nav-form" action="javascript:loadSearchResult()" name="search-frm">
		<div class="col-md-5">
			<div class="input-group">
      			<input type="text" class="form-control" id="search-input" name="searchInput" placeholder="搜索...">
      			<span class="input-group-btn">
       				 <button class="btn btn-default" type="submit" onclick="return checkSearch()"><span class="glyphicon glyphicon-search"></span></button>
      			</span>
    		</div>
		</div>
		<div class="col-md-2">
			<select class="form-control" id="search-type" name="search-type">
				<option value="1">全局搜索</option>
				<option value="2">按名称搜索</option>
				<!--  <option value="3">按类型搜索</option>-->
			</select>
		</div>		
	</form>
	
	<!-- 上传按钮 -->
	<div class="col-md-2 col-md-offset-3">
		<button class="btn btn-info" data-toggle="modal" data-target="#upload-modal"><span class="glyphicon glyphicon-open">上传</span></button>
	</div>
</div>

<!-- 文件及搜索结果显示区 -->
<div class="row">
	<table class="table table-striped table hover">
		<thead>
			<tr><th>文件名</th><th>类型</th><th>大小</th><th>上传日期</th></tr>
		</thead>
		<tbody>
			<%
				User user = (User)session.getAttribute("user");
				FileDAO fileDAO = (FileDAO)session.getAttribute("fileDAO");
				HashMap<String, FileBean> fileList = fileDAO.getAllFiles();
				Set<String> keySet = fileList.keySet();
				for(String key : keySet){
					FileBean file = fileList.get(key);
			%>
					<tr><td><%=file.getFilename() %></td><td><%=file.getType() %></td><td><%= file.getStandardFileSize()%></td>
					<td><%=file.getUploadDate() %></td>
					<td><a href="<%=file.getUrl() %>" download><span class="glyphicon glyphicon-download-alt"> 下载</span></a></td>
					<td><a href="share.jsp?fileId=<%= file.getId()%>"><span class="glyphicon glyphicon-send"> 分享</span></a></td>
					<td><a href="/FileStorage/deleteFile?id=<%= file.getId()%>" onclick="return confirmDelete(<%= file.getFilename()%>)"><span class="glyphicon glyphicon-trash"> 删除</span></a></td></tr>
			<% 	}
			%>
		</tbody>
	</table>
</div>

<!-- 弹出框，bootstrap的模态框 -->
<div class="modal fade" id="upload-modal" tabindex="-1" role="dialog" >
  	<div class="modal-dialog modal-md" role="document">
    	<div class="modal-content">
      		<div class="modal-header">
      			<h4 class="modal-title">上传文件</h4>
      		</div>
      		<div class="modal-body">
      			<form action="UploadFile" method="post" enctype="multipart/form-data" name="frm">
      				<input type="file" name="file" id="fileUpload">
      				<button class="btn btn-info" type="submit" onclick="return checkUploadFile()">上传</button>
      			</form>
      		</div>
      			
      		<div class="modal-footer" align="center">
      			<a href="#" data-dismiss="modal">取消</a>
      		</div>
    	</div>
  	</div>
</div>



