<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/bootstrap.css">
<style type="text/css">
body{
	background-color:  #00CCFF;
}
.header {
	position:absolute;
	background-color:#0099CC;
	margin-top: 0px;
	margin-left: auto;
	margin-right: auto;
	width: 100%;
	text-align: center;
}

.login {
	position: absolute;
	top: 40%;
	left: 50%;
	transform: translate(-50%, -50%);
	width: 40%;
	height: 60%;
	background-color: #66CCFF;
}
.form{
	margin-top: 20px;
	width: 50%;
	position: absolute;
	top: 40%;
	left: 50%;
	transform: translate(-50%, -50%);
	height: 80%;
	 
}
.formtalbe{
	margin-top: 30%;
}
.form input {
		width: 70%;
		height: 30px;
}
  .formheader{
position:absolute;
	margin-top: 10px;
	text-align: center;
}
</style>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/jquery-3.1.0.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/bootstrap.js"></script>
<script type="text/javascript">
	
</script>
</head>
<body>
	<div class="page-header   header">
		<h1>
			后台登陆中心 <small >----------------<span style="color:black;">yetthin</span></small>
		</h1>
	</div>
	<div class="containe row   login">
	<div class="form">
		<h1 class="formheader">Login</h1>
		<form class="formtalbe" action="${pageContext.request.contextPath }/admin/login" method="post">
			<div class="form-group">
				 <label>Admin Name</label><br/> 
   				 <input type="text" name="adminName" placeholder=" 请输入用户名"/>
				<% if(session.getAttribute("adminLogin")!=null) { 
				%>
			<label style="color: red">登陆失败!!</label>
				<%} %>
			</div>
			<div class="form-group">
				 <label>Password</label><br/>
   				 <input type="password" name="password" placeholder=" 请输入密码"/>
			</div>
			<!-- <div class="form-group">
				<label for="exampleInputFile">File input</label> <input type="file"
					id="exampleInputFile">
				<p class="help-block">Example block-level help text here.</p>
			</div> -->
			 
			<button type="submit" class="btn btn-default">Submit</button>
		</form>
		
		</div>
	</div>
</body>
</html>