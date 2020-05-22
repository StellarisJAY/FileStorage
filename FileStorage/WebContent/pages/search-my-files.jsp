<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="util.DBUtil" %>
<%@ page import="entity.FileBean" %>
<%@ page import="DAO.FileDAO" %>
<%@ page import="java.util.*" %>
<%
	String searchInput = request.getParameter("searchInput");
	String searchType = request.getParameter("searchType");
	
	FileDAO fileDAO = (FileDAO)session.getAttribute("fileDAO");
	HashMap<String, FileBean> fileList = fileDAO.getAllFiles();
	Set<String> keySet = fileList.keySet();
	ArrayList<FileBean> searchResult = new ArrayList<FileBean>();
	
	for(String key : keySet){
		if(key.indexOf(searchInput) != -1){
			searchResult.add(fileList.get(key));
		}
	}
%>
<div class="row">

	<!--  搜索表单 -->
	<form class="nav-form" action="javascript:loadSearchResult()" name="search-frm">
		<div class="col-md-5">
			<div class="input-group">
      			<input type="text" class="form-control" id="search-input" name="searchInput" placeholder="搜索..." value="<%=searchInput%>">
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
	
	<!-- 返回全部文件 -->
	<div class="col-md-2 col-md-offset-3">
		<a href="#" onclick="loadExternalPage('pages/files.jsp')">显示全部文件</a>
	</div>
</div>

<!-- 搜索结果显示区 -->
<div class="row">
	<table class="table table-striped table hover">
		<caption>搜索到<%=searchResult.size() %>条相关结果</caption>
		<thead>
			<tr><th>文件名</th><th>类型</th><th>大小</th><th>上传日期</th></tr>
		</thead>
		<tbody>
			<%
					for(int i = 0; i < searchResult.size(); i++){
						FileBean file = searchResult.get(i);
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