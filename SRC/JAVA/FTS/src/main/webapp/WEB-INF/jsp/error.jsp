<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isErrorPage="true"%>
<%@page trimDirectiveWhitespaces="true"%>
<html lang="en">
<head>
	<title>File Transfer System | Error</title>
	
	<link rel="icon" type="image/x-icon" href="favicon.ico">
	
	<meta charset="utf-8">
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
			<div style="align-self: center;">
				<a href="logout">
					<button>Log Out</button>
				</a>
			</div>
		</div>
	</div>
	
	<div class="view-wrapper" id="view-wrapper">
	
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
		
		<br><br>
		
		<a href="/">
			<button>
				<img src="img/home.svg" alt="/" width="14px" class="img-button" />
				<span style="margin-left: 5px;">Home</span>
			</button>
		</a>
		
	</div>
	
	<div class="push"></div>
	
	<div class="footer" id="footer">
		<span>Please consider using <b>Mozilla Firefox</b> or <b>Google Chrome</b> for the best experience</span>
	</div>
	
	<script src="js/common.js"></script>
	
</body>
</html>
