<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>
	<title>File Transfer System | Login</title>
	
	<link rel="icon" type="image/x-icon" href="favicon.ico">
	
	<link href="css/login.css" rel="stylesheet" type="text/css">
</head>
<body>

	<div style="text-align: center; border: none;">
		<h1>File Transfer System</h1>
		<h3 style="margin-top: -10px;">Pratanu Mandal</h3>
	</div>
	<hr>
	<br>

	<div style="text-align: center;">
		<div style="display: inline-block; text-align: left;">
			<c:url value="/login" var="loginUrl" />
			<form action="${loginUrl}" method="post">
				<label for="username">Username</label>
				<input type="text" id="username" name="username" />
				<br><br><br>
				<label for="password">Password</label>
				<input type="password" id="password" name="password" />
				<br><br><br><br>
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				<div style="text-align: center;">
					<input type="submit" value="Log In" />
					<br><br><br>
					<c:if test="${param.error != null}">
					<span style="color: red;">Invalid username and password</span>
				</c:if>
				</div>
			</form>
		</div>
	</div>

</body>
</html>
