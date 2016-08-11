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
<title>Insert title here jsp</title>
<script type="text/javascript" src="js/jquery-3.1.0.min.js"></script>
<script type="text/javascript" src="js/jquery-form.js"></script>

<script type="text/javascript">
 	 
</script>
</head>
<body>
	<div class="main">
 	<form action="${pageContext.request.contextPath }/user/register" method="post">
 	 	<label>register</label>
 	 	<input type="text" name="phoneNum" placeholder="电话号码"/>
 		<input type="password" name="password" placeholder="密码"/>
 		<!-- <input type="text" value="" name="verifyCode" placeholder="验证码"/>
 	 -->	
 		<input type="submit" value="submit"  />
 	</form>
 	<br/>
 <%-- 	<form action="${pageContext.request.contextPath }/user/getRegisterVerify" method="post">
 	 	<label>getRegisterVerify</label>
 		<input name="phoneNum" type="text" placeholder="电话号码"/>
 		<input type="submit" value="submit"/>
 	</form> --%>
 	<br/>
 	<form action="${pageContext.request.contextPath }/user/login" method="post">
 	 	<label>login</label>
 	 	<input type="text" name="phoneNum" placeholder="电话号码"/>
 		<input type="password" name="password" placeholder="密码"/>
 		<input type="submit" value="submit"  />
 	</form>
 	<br/>
 	<form action="${pageContext.request.contextPath }/user/forgetPwd" method="post">
 	 	<label>forgetPwd</label>
 		<input name="phoneNum" type="text" placeholder="电话号码"/>
 		 
 		<input name="password" type="password" placeholder="密码"/>
 		<input type="submit" value="submit"/>
 	</form><!-- 
 	<br/>
 	<form action="${pageContext.request.contextPath }/user/getforgetPwdVerify" method="post">
 	 	<label>getforgetPwdVerify</label>
 		<input name="phoneNum" type="text" placeholder="电话号码"/>
 		<input name="_method" type="hidden" value="put" />
 		<input type="submit" value="submit"/>
 	</form>  -->
 	<br/>
 	<form action="${pageContext.request.contextPath }/user/updateJPushID"  id="updateJpushIDForm" method="post">
 	
 	 	<label>updateJPushID</label>
 		<input name="userID" type="text" placeholder="用户Id" />
 		<input name="JpushID" type="text" placeholder="更新极光ID" />
 		<input name="_method" type="hidden" value="put" />
 		 
 		<select name="phoneType" >
 			<option value="0">推送极光的客户端</option>
 			<option value="1">Android</option>
 			<option value="-1">IOS</option>
 		</select>
 		
 		<input type="submit" value="submit"/>
 	</form>
 	<br/>
 	<form action="${pageContext.request.contextPath }/user/updateJPushStatus" method="post">
 	 	<label>updateJPushStatus</label>
 		<input name="userID" type="text" placeholder="用户ID"/>
 		<input name="JpushStatus" type="text" placeholder="推送极光的开关 1关闭 0打开" />
 		<input type="text" name="JpushType" placeholder="推送类型"/>
 		<input type="submit" value="submit"/>
 	</form>
 	<br/>
 	<form action="${pageContext.request.contextPath }/user/bindingEmail" method="post">
 	 	<label>bindingEmail</label>
 		<input name="userID" type="text" placeholder="用户ID"/>
 		<input name="email" type="text" placeholder="用户邮箱" />
 		<input name="_method" type="hidden" value="put" />
 		<input type="submit" value="submit"/>
 	</form>
 	<br/>
 	<form action="${pageContext.request.contextPath }/user/changePwd" method="post">
 	 	<label>changePwd</label>
 		<input name="userID" type="text" placeholder="用户ID"/>
 		<input name="oldPwd" type="text" placeholder="旧密码"/>
 		<input name="newPwd" type="text" placeholder="新密码"/>
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
 		<input name="userID" type="text" placeholder="用户ID"/>
 		<input name="ideaText" type="text"  placeholder="意见反馈"/>
 		<input name="_method" type="hidden" value="put" />
 		<input type="submit" value="submit"/>
 	</form>
  
 		<form action="${pageContext.request.contextPath }/user/uploadPicture" method="POST" enctype="multipart/form-data">
		File: <input type="file" name="file"/>
		Desc: <input type="text" name="userID"/>
		<input type="submit" value="Submit"/>
	</form> <br/>
	
		<form action="${pageContext.request.contextPath }/user/changePhone" method="POST" >
		<label>changePhone</label>
		 <input type="text" name="newPhoneNum" placeholder="新手机号"/>
		<input type="text" name="userID" placeholder="用户ID"/>
			<input type="password" name="password" placeholder="密码"/>
		<input type="submit" value="Submit"/>
	</form>
	
 	<br/>
  	<a href="Jdtoa.jsp">查看行情接口</a>
  </div>
</body>
</html>