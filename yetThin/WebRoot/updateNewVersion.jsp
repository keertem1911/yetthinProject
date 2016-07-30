<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath }/css/bootstrap.css">
<style type="text/css">
	 .word{
	 	 
	 	color:black;
	 }
	 .main{
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

	<div >

		<!-- Nav tabs -->
		<ul class="nav nav-tabs" role="tablist">
			<li role="presentation" class="active warning"><a href="#home"
				aria-controls="home" role="tab" data-toggle="tab"><span class="word">手动输入上传</span></a></li>
			<li role="presentation"><a href="#profile"
				aria-controls="profile" role="tab" data-toggle="tab"><span class="word">txt文件上传</span></a></li>
		</ul>

		<!-- Tab panes -->
		<div class="tab-content main">
			 
			<div role="tabpanel" class="tab-pane active col-lg-4" id="home">
				<form>
					<div class="form-group">
						<label>版本号</label> <input type="text" class="form-control"
							name="versionCode" placeholder="versionCode" />
					</div>
					<div class="form-group">
						<label>版本名称</label> <input type="text" class="form-control"
							name="versionName" placeholder="versionName" />
					</div>
					<div class="form-group">
						<label>apk地址</label> <input type="text" class="form-control"
							name="apkUrl" placeholder="versionName" />
					</div>
					<div class="form-group">
						<label>apk说明</label> <input type="text" class="form-control"
							name="apkUrl" placeholder="explain" />
					</div>
					<br />

					<button type="submit" class="btn btn-default">Submit</button>
				</form>
				 
			</div>
			<div role="tabpanel" class="tab-pane main" id="profile">
				<form>
					<div class="form-group">
						<label for="exampleInputFile">上传apk更新文件</label> <input type="file"
							id="InputFile" name="InputFile">
						<p class="help-block">Example block-level help text here.</p>
					</div>
					<div class="checkbox">
						<label> <input type="checkbox"> Check me out
						</label>
					</div>
					<button type="submit" class="btn btn-default">Submit</button>
				</form>
			</div>
		</div>

	</div>

	 
</body>
</html>