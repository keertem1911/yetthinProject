<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/bootstrap.css">
<style type="text/css">
	 table{
	 	margin-top: 20px;
	 }
	 
</style>
<title>Insert title here</title>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/jquery-3.1.0.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/bootstrap.js"></script>
</head>
<body>
	<div class="input-group input-group-lg col-xs-4">
  <!-- <span class="input-group-addon" id="sizing-addon1"></span> -->
  <input type="text" class="form-control" placeholder="PhoneNum" aria-describedby="sizing-addon1"/>
	 <span class="input-group-btn">
        <button class="btn btn-primary" type="button">Go!</button>
      </span>
      <span class="input-group-btn">
      <button type="button" class="btn btn-danger clearbtn">Clear</button>
      </span>	
	</div>
	<table class=" table table-hover" border="1" bordercolor="black" cellpadding="1" cellspacing="1">
				<thead>
				<tr>
					<td><span>userID</span></td>
					<td><span>userNum</span></td>
					<td><span>userpassWd</span></td>
					<td><span>查看详情</span></td>
					<td><span>删除</span></td>
				</tr>
				</thead>
				<tbody >
				<tr>
					<td><span>1</span></td>
					<td><span>188292902111</span></td>
					<td><span>1231231</span></td>
					<td><a href="#"><span class="label label-info">more info</span></a></td>
					<td><a href="#"><span class="label label-warning">delete</span></a></td>
				</tr>
				<tr>
					<td><span>2</span></td>
					<td><span>1828929292</span></td>
					<td><span>12312313</span></td>
					<td><a href="#"><span class="label label-info">more info</span></a></td>
					<td><a href="#"><span class="label label-warning">delete</span></a></td>
				</tr>
				
				</tbody>
			</table>
</body>
</html>