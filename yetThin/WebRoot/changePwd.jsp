<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/bootstrap.css">
<style type="text/css">
	 form{
	 	position:absolute;
	 	left:40%;
	 	top:50%;
	 	margin-top: 20px;
	 	font-size: x-large;
	 }
	 
</style>
<title>Insert title here</title>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/jquery-3.1.0.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/bootstrap.js"></script>
</head>
<body>
	<div class="col-lg-8">
	 <form>
  <div class="form-group">
    <label for="exampleInputPassword1">old Password</label>
    <input type="password" class="form-control" name="oldPassword"id="exampleInputPassword1" placeholder="Password">
  </div>
  <div class="form-group">
    <label for="exampleInputPassword1">new Password</label>
    <input type="password" class="form-control"  name="newPassword" id="exampleInputPassword1" placeholder="Password">
  </div>
  
  <button type="submit" class="btn btn-default">Submit</button>
</form>
</div>
</body>
</html>