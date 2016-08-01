<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here jsp</title>
<script type="text/javascript" src="js/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="js/jquery-form.js"></script>

<script type="text/javascript">
 	$(function(){
 		function init(){
 			var url=$("#updateJpushIDForm").attr("action");
 		$.ajax({
 			url:url,
 			type:"post",
 			data:{
 				userID:"7",
 				JpushID:"123123123",
 				_method:"put"
 			},
 			success:function(msg){
				alert(msg);
 				$("#showtable").apply(msg);
			}
 		});
 		}
 		 $(".mybtn").click(function(){
 			 init();
 		 })
 		 $("#btnupdate").click(function(){
 			 
 			var url =window.location.href;
			 url=url.substr(0, url.lastIndexOf("/")+1);
 		$("#updateForm").ajaxSubmit({
			url: url+'admin/updateNewVersion2',
			success: function(data) {
				 			console.log(data); 
			},
			});
 			 
 		 })
 	})
</script>
</head>
<body>
	
 	<form action="${pageContext.request.contextPath }/user/register" method="post">
 	 	<label>register</label>
 	 	<input type="text" name="phoneNum"/>
 		<input type="password" name="password"/>
 		<input type="hidden" value="@#$qowafdhjqo" name="verifyCode"/>
 		
 		<input type="submit" value="submit"  />
 	</form>
 	<br/>
 	<form action="${pageContext.request.contextPath }/user/getRegisterVerify" method="get">
 	 	<label>getRegisterVerify</label>
 		<input name="phoneNum" type="text" />
 		<input type="submit" value="submit"/>
 	</form>
 	<br/>
 	<form action="${pageContext.request.contextPath }/user/login" method="post">
 	 	<label>login</label>
 	 	<input type="text" name="phoneNum"/>
 		<input type="password" name="password"/>
 		<input type="submit" value="submit"  />
 	</form>
 	<br/>
 	<form action="${pageContext.request.contextPath }/user/forgetPwd" method="post">
 	 	<label>forgetPwd</label>
 		<input name="phoneNum" type="text" />
 		<input name="verifyCode" type="hidden" value="!@#$%&*%" />
 		<input name="password" type="text"/>
 		<input type="submit" value="submit"/>
 	</form>
 	<br/>
 	<form action="${pageContext.request.contextPath }/user/getforgetPwdVerify" method="post">
 	 	<label>getforgetPwdVerify</label>
 		<input name="phoneNum" type="text" />
 		<input name="_method" type="hidden" value="put" />
 		<input type="submit" value="submit"/>
 	</form>
 	<br/>
 	<form action="${pageContext.request.contextPath }/user/updateJpushID"  id="updateJpushIDForm" method="post">
 	
 	 	<label>updateJpushID</label>
 		<input name="userID" type="text" />
 		<input name="JpushID" type="text" />
 		<input name="_method" type="hidden" value="put" />
 		<input type="submit" value="submit"/>
 	</form>
 	<br/>
 	<form action="${pageContext.request.contextPath }/user/updateJpushStatus" method="post">
 	 	<label>updateJpushStatus</label>
 		<input name="userID" type="text" />
 		<input name="status" type="text" />
 		<input type="submit" value="submit"/>
 	</form>
 	<br/>
 	<form action="${pageContext.request.contextPath }/user/bindingEmail" method="post">
 	 	<label>bindingEmail</label>
 		<input name="userID" type="text" />
 		<input name="email" type="text" />
 		<input name="_method" type="hidden" value="put" />
 		<input type="submit" value="submit"/>
 	</form>
 	<br/>
 	<form action="${pageContext.request.contextPath }/user/changePwd" method="post">
 	 	<label>changePwd</label>
 		<input name="userID" type="text" />
 		<input name="oldPwd" type="text" />
 		<input name="newPwd" type="text" />
 		<input name="_method" type="hidden" value="put" />
 		<input type="submit" value="submit"/>
 	</form>
 	<br/>
 	<form action="${pageContext.request.contextPath }/help/checkNewVersion" method="get">
 	 	<label>checkNewVersion</label>
 		<input type="submit" value="submit"/>
 	</form>
 	<br/>
 	<form action="${pageContext.request.contextPath }/user/feedback" method="post">
 	 	<label>feedback</label>
 		<input name="userID" type="text" />
 		<input name="ideaText" type="text" />
 		<input name="_method" type="hidden" value="put" />
 		<input type="submit" value="submit"/>
 	</form>
 	<form action="admin/updateNewVersion2" id="updateForm"
 	 method="post" enctype="multipart/form-data">
 	 	<label>upload</label>
 		 
 		<input name="file" type="file" />
 		<input type="button" id="btnupdate" value="submit"/>
 	</form>
 	<br/>
 	<input type="button" class="mybtn" value="click"/>
 	<label id="showtable"></label>
</body>
</html>