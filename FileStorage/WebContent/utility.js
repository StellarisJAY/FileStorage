var xmlhttp;

function loadExternalPage(url){
	xmlhttp = null;
	if(window.XMLHttpRequest){
		xmlhttp = new XMLHttpRequest();
	}
	else if(window.ActiveXObject){
		xmlhttp = new ActiveXObject();
	}
	
	if(xmlhttp != null){
		xmlhttp.onreadystatechange = state_change;
		xmlhttp.open('GET', url, true);
		xmlhttp.send(null);
	}
}

function state_change(){
	if(xmlhttp.readyState == 4){
		if(xmlhttp.status == 200){
			document.getElementById('main').innerHTML = xmlhttp.responseText;
		}
		else{
			alert("加载错误: " + xmlhttp.statusText);
		}
	}
}
function checkFile(){
	if(frm.file.value==""){
		alert('请选择要上传的文件');
		return false;
	}
	return true;
}

function checkSearch(){
	if(document.getElementById('search-input').value==""){
		alert('请输入搜索内容');
		document.getElementById('search-input').focus();
		return false;
	}
	return true;
}

function loadSearchResult(){
	var input = document.getElementById('search-input').value;
	var type = document.getElementById('search-type').value;
	loadExternalPage('pages/search-my-files.jsp?searchInput='+input+'&searchType='+type);
}
