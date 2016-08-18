<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
	form {
		vertical-align: center;
		text-align: left;
		margin-left: 20px;
		
	}
	.main{
	margin-top: 20px;
	}
</style>
<title>行情接口展示</title>
</head>
<body>
	<div>
	<br/>
	<form action="${pageContext.request.contextPath }/jtdoa/shenzhen" method="post">
		 
		<input type="submit" value="get"/>
	</form><br/>
	<%-- <form method="post" action="${pageContext.request.contextPath }/jtdoa/getLevel2">
		<label>获取level2 摆单  不要被吓住了</label>
		<input type="text" name="symbol" placeholder="股票代码+':'+市场"></input>
		<input type="submit" value="get"/>
	</form> --%>
	</div>
</body>
</html>