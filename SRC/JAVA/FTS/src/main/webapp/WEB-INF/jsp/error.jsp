<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isErrorPage="true"%>
<%@page trimDirectiveWhitespaces="true"%>
<html lang="en">
<head>
	<title>File Transfer System | Error</title>
	
	<link rel="icon" type="image/x-icon" href="favicon.ico">
	
	<link href="css/error.css" rel="stylesheet" type="text/css">
</head>
<body>

	<h1>File Transfer System | Error</h1>
	<h3 style="margin-top: -10px;">Pratanu Mandal</h3>
	<div style="text-align: right; border: none; margin-top: -45px; padding-bottom: 10px;">
		<a href="logout"><button style="padding: 5px 15px 5px 15px;">Logout</button></a>
	</div>
	<hr>
	<br>
	
	<table>
		<tr>
			<td>Date</td>
			<td>${timestamp}</td>
		</tr>
		<tr>
			<td>Error</td>
			<td>${error}</td>
		</tr>
		<tr>
			<td>Status</td>
			<td>${status}</td>
		</tr>
		<tr>
			<td>Message</td>
			<td>${message}</td>
		</tr>
	</table>
	
	<br>
</body>
</html>
