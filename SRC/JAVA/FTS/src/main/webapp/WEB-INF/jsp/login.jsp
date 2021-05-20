<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page trimDirectiveWhitespaces="true"%>
<%@page import="in.pratanumandal.fts.util.FtsConstants"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>
	<title>File Transfer System | Login</title>
	
	<link rel="icon" type="image/x-icon" href="favicon.ico">
	
	<meta name="viewport" content="width=device-width, initial-scale=1.0">

	<script src="js/jquery.min.js"></script>
	<script src="js/jquery.form.min.js"></script>
	
	<link href="css/style.css" rel="stylesheet" type="text/css">
</head>
<body>

	<div class="nav-bar-wrapper" id="nav-bar-wrapper">
		<div class="nav-bar">
			<div style="flex-grow: 1;">
				<h1>
					<a href="/">
						<span class="brand">File Transfer System</span>
					</a>
				</h1>
				<h3>
					<a href="https://pratanumandal.in/" target="_BLANK">
						<span class="author">Pratanu Mandal</span>
					</a>
				</h3>
			</div>
		</div>
	</div>

	<div class="view-wrapper center" id="view-wrapper">
	
		<div class="login-wrapper">
			<div class="flex-wrapper">
				<div style="flex-grow: 1;">
					<div class="center responsive">
						<div class="login-container">
							<img src="img/FTS.svg" alt="File Transfer System" width="100px" class="fts-logo" />
							<br><br><br>
							<h2 class="name">${FtsConstants.NAME}</h2>
							<br>
							<h3 class="description">${FtsConstants.DESCRIPTION}</h3>
						</div>
					</div>
				</div>
				
				<div class="splitter login"></div>
				
				<div style="flex-grow: 1;">
					<div class="center responsive">
						<div class="login-container">
							<c:url value="/login" var="loginUrl" />
							<form action="${loginUrl}" method="post">
								<div class="floating-input">
								  <input type="text" id="username" name="username" required />
								  <span class="floating-label">Username</span>
								</div>
								<br><br>
								<div class="floating-input">
								  <input type="password" id="password" name="password" required />
								  <span class="floating-label">Password</span>
								</div>
								<br><br><br><br>
								<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
								<div>
									<input type="submit" value="Log In" class="login-button" />
									<br><br>
									<c:if test="${param.error != null}">
										<br>
										<div class="login-error">Invalid username or password</div>
									</c:if>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
			
		</div>
		
	</div>
	
	<div class="push"></div>
	
	<div class="footer" id="footer">
		<span>Please consider using <b>Mozilla Firefox</b> or <b>Google Chrome</b> for the best experience</span>
	</div>
	
	<script src="js/common.js"></script>

</body>
</html>
