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
		 <label>/jtdoa/shenzhen  沪深股市接口 </label>
		 <input name="begin" type="text" value="" placeholder="起始位置  start with 0 默认为0 可不选"/>
		 <input name="end" type="text" value="" placeholder="结束位置   默认为9 可不选"/>
		 <label>股市选择及属性 可不选 默认为沪深 的 涨幅,跌幅,换手率</label>
		 <select name="marketCode" >
		 	<option value="">默认(0:0,1,2)</option>
		 	<option value="0:0">沪深:涨幅(0:0)</option>
		 	<option value="0:0,1">沪深:涨幅,跌幅(0:0,1)</option>
		 	<option value="0:0,1,2">沪深:涨幅,跌幅,换手率(0:0,1,2)</option>
		 </select>
		<input type="submit" value="get"/>
	</form><br/>
	<form action="${pageContext.request.contextPath }/jtdoa/getLevel2" method="post">
		<label>摆单接口 </label>
		<input type="text" name="symbol" placeholder="格式:000001.SH"/>
		<input type="submit" value="submit"/>
	</form>
	<%-- <form method="post" action="${pageContext.request.contextPath }/jtdoa/getLevel2">
		<label>获取level2 摆单  不要被吓住了</label>
		<input type="text" name="symbol" placeholder="股票代码+':'+市场"></input>
		<input type="submit" value="get"/>
	</form> --%>
	</div>
</body>
</html>