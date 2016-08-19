<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/bootstrap.css">
<style type="text/css">
	form {
		vertical-align: center;
		text-align: left;
		margin-left: 20px;
		
	}
	.main{
	margin-top: 20px;
	}
	table{
		
	}
	 
	.big1{
		text-align:left;
		width:150px;
	}
	.small1{
		width:50px;
	}
	.small2{
		width:30px;	
	}
</style>
<title>行情接口展示</title>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath }/js/jQuery.timers.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath }/js/bootstrap.js"></script>
	<script type="text/javascript">
			$(function(){
				function shenzhenFun(){
					var url =$("#shenzhen").attr("action");
					var values={};
					var inputs=$(".shenzhen1");
					for(var i=0;i<inputs.length;++i){
							values[$(inputs[i]).attr("name")]=$(inputs[i]).val();
					}
					console.log(values);
					
					$.post(url,values,function(msg){
						$(".level1").empty();
						var html="<table border=1 ><tr class='big1'>";
						console.log(msg);
						for(var i=0;i<msg.index.length;++i){
							
							html+="<td >";
							html+="<span color='red'>"+msg.index[i].name+"</span><br/>";
							if(msg.index[i].increase=="false"){
							html+="<span style='color:green' >"+msg.index[i].growth+"</span>&nbsp;&nbsp;";
							html+="<span style='color:green'>-"+msg.index[i].price+"</span><br/>";
							html+="<span style='color:green'>-"+msg.index[i].growthRate+"%</span>";
							}
							else{
							html+="<span style='color:red' >"+msg.index[i].growth+"</span>&nbsp;&nbsp;";
							html+="<span style='color:red' >+"+msg.index[i].price+"</span><br/>";
							html+="<span style='color:red' >+"+msg.index[i].growthRate+"%</span>";
							}
							html+="</td>";
						}
						 html+="</tr>";
						 for(var i=0;i<msg.item.length;++i){
							 
							 html+="<tr style='widhth:50px'>";
							 html+="<td colspan='3' >"+msg.item[i]["group"]+"</td>";
							 html+="</tr>";
							 var color="red";
							 var plus ="+";
							 for(var j=0;j<msg.item[i].index.length;++j){
								 
								if(msg.item[i].index[j].increase=="false"){
									color="green";
									plus="-";
								}
							 html+="<tr style='widhth:30px'>";
							 html+="<td><span  >"+msg.item[i].index[j].name+":"+msg.item[i].index[j].stockID+"</span></td>";
							 html+="<td><span style='color:"+color+"' >"+msg.item[i].index[j].price+"</span></td>";
							 html+="<td><span style='color:"+color+"' >"+plus+msg.item[i].index[j].rate+"%</span></td>";
							 
							 html+="</tr>";
							 }
						 }
						 html+="</table>";
						 $(".level1").append(html);
					});
				}
				$("#shenzhen-sub").click(function(){
					shenzhenFun();
				});
				//$('body').everyTime('6s',function(){
					//shenzhenFun();
				//});
			})
	</script>
</head>
<body>
	<div>
	<br/>
	<form action="${pageContext.request.contextPath }/jtdoa/shenzhen" method="post" id="shenzhen">
		 <label>/jtdoa/shenzhen  沪深股市接口 </label>
		 <input class="shenzhen1" name="begin" type="text" value="" placeholder="起始位置  start with 0 默认为0 可不选"/>
		 <input class="shenzhen1" name="end" type="text" value="" placeholder="结束位置   默认为9 可不选"/>
		 <label>股市选择及属性 可不选 默认为沪深 的 涨幅,跌幅,换手率</label>
		 <select name="marketCode"  class="shenzhen1">
		 	<option value="">默认(0:0,1,2)</option>
		 	<option value="0:0">沪深:涨幅(0:0)</option>
		 	<option value="0:0,1">沪深:涨幅,跌幅(0:0,1)</option>
		 	<option value="0:0,1,2">沪深:涨幅,跌幅,换手率(0:0,1,2)</option>
		 </select>
		<input type="button" value="get" id="shenzhen-sub"/>
	</form><br/>
	<form action="${pageContext.request.contextPath }/jtdoa/getLevel2" method="post">
		<label>摆单接口 </label>
		<input type="text" name="symbol" placeholder="格式:000001.SH"/>
		<input type="submit" value="submit"/>
	</form>
	  <form method="post" action="${pageContext.request.contextPath }/jtdoa/getLevel2Detail">
	 <label>获取交易详单</label>
		<input type="text" name="symbol" placeholder="股票代码+'.'+市场"></input>
		<input type="submit" value="get"/>
	</form>  
	</div>
	<div class="display">
		<div class="level1"></div>
		<div class="level2"></div>
		<div class="level2detail"></div>
	</div>
	
	
	
	
</body>
</html>