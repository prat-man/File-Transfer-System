<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page trimDirectiveWhitespaces="true"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="tk.pratanumandal.fts.util.FtsConstants"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
	<title>File Transfer System</title>
	
	<link rel="icon" type="image/x-icon" href="favicon.ico">

	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js"></script>
	
	<link href="css/style.css" rel="stylesheet" type="text/css">
</head>
<body>

	<h1>File Transfer System</h1>
	<h3 style="margin-top: -10px;">Pratanu Mandal</h3>
	<div style="text-align: right; border: none; margin-top: -45px; padding-bottom: 10px;">
		<a href="logout"><button style="padding: 5px 15px 5px 15px;">Logout</button></a>
	</div>
	<hr>
	<br>

	<div id="pathbar" class="pathbar bordered">
		<%
			String path = request.getParameter("path");
		
			if (path != null) {
				path = path.replaceAll("\\\\", "/");
				path = path.replaceAll("/+", "/");
				
				if (path.startsWith("/")) {
					path = path.substring(1);
				}
			
				if (path.equals("/") || path.equals("\\")) {
					path = new String();
				}
			}
			
			out.write("<a href=\"?path=\" style=\"font-weight: bold;\">/</a><font size=\"5\"></font>");
			
			if (path != null && !path.isEmpty()) {
				StringTokenizer st = new StringTokenizer(path, "/");
				
				String tokenPath = new String();
				
				while (st.hasMoreTokens()) {
					String token = st.nextToken();
					tokenPath += token;
					if (st.hasMoreTokens()) {
						if (!token.equals(".")) {
							out.write("&nbsp;&nbsp;<font size=\"5\">&#8250;</font>&nbsp;&nbsp;<a href=\"?path=" + URLEncoder.encode(tokenPath, "UTF-8") + "\">" + token + "</a>");
						}
						tokenPath += "/";
					}
					else {
						if (!token.equals(".")) {
							out.write("&nbsp;&nbsp;<font size=\"5\">&#8250;</font>&nbsp;&nbsp;<a href=\"?path=" + URLEncoder.encode(tokenPath, "UTF-8") + "\">" + token + "</a>&nbsp;&nbsp;&nbsp;&nbsp;");
						}
					}
				}
			}
		%>
	</div>

	<div id="browser" class="bordered">
		<c:choose>
			<c:when test="${empty files}">
				<p style="text-align: center;">No Files Found</p>
			</c:when>
			<c:otherwise>
				<table>
					<c:forEach items="${files}" var="file">
						<tr>
							<td><img src="/icon?path=${file.getEncodedPath()}" /></td>
							<c:choose>
								<c:when test="${file.isDirectory()}">
									<td width="99%"><a href="?path=${file.getEncodedPath()}">${file.getName()}</a></td>
								</c:when>
								<c:otherwise>
									<td width="99%"><c:out value="${file.getName()}" /></td>
								</c:otherwise>
							</c:choose>
							<td style="text-align: right;"><span style="white-space: nowrap;">${file.getSize()}</span></td>
							<c:if test="${FtsConstants.DELETE}">
								<td><a onclick="deleteFile('${file.getName()}', '${file.getEncodedPath()}')">Delete</a></td>
							</c:if>
							<td><a href="/download?path=${file.getEncodedPath()}">Download</a></td>
						</tr>
					</c:forEach>
				</table>
			</c:otherwise>
		</c:choose>
	</div>

	<br>

	<div style="display:flex; flex-wrap: wrap; justify-content: space-between;">
		<div class="bordered padded" style="flex-grow: 1; flex-basis: 0;">
			<form id="fileUploadForm" method="POST" action="/uploadFile" enctype="multipart/form-data">
				<label>Select a <b>FILE</b> to upload</label><br><br>
				<input id="fileUploadField" type="file" name="file" required="required" /><br><br>
				<input type="text" name="path" value="${path}" style="display: none;" />
				<input id="fileUploadSubmit" type="submit" value="Upload" />
			</form>
			<progress id="fileUploadProgress" style="display: none;"></progress>
			<p id="fileUploadProgressValue" style="display: none; color: black; padding-left: 5px;"></p>
		</div>
		
		<div class="splitter"></div>
		
		<div class="bordered padded" style="flex-grow: 1; flex-basis: 0;">
			<form id="folderUploadForm" method="POST" action="/uploadFolder" enctype="multipart/form-data">
				<label id="folderUploadLabel">Select a <b>FOLDER</b> to upload</label><br><br>
				<input id="folderUploadField" type="file" name="files" webkitdirectory mozdirectory msdirectory odirectory directory multiple required="required" /><br><br>
				<input type="text" id="fileNames" name="fileNames" style="display: none;" />
				<input type="text" name="path" value="${path}" style="display: none;" />
				<span id="folderUploadSubmit">
					<input type="submit" value="Upload" />
					<input id="folderUploadWarning" type="button" class="alert" value="!" style="display: none;" onclick="alert('Folder upload is not supported by this browser.\nMultiple files can be uploaded at once instead.');"></input>
				</span>
			</form>
			<progress id="folderUploadProgress" style="display: none;"></progress>
			<a id="folderUploadProgressValue" style="display: none; color: black; padding-left: 5px;"></a>
		</div>
	</div>
	
	<br>
	
	<div style="display: flex; flex-wrap: wrap; justify-content: space-between; padding: 0; border: none;">
		<div class="bordered" style="flex-grow: 1; flex-basis: 0;">
			<form id="createFolderForm" class="padded">
				<label>Create a new <b>FOLDER</b></label><br><br>
				<input type="text" name="folderName" required="required" placeholder="Enter Folder Name" /><br><br>
				<input type="text" name="path" value="${path}" style="display: none;" />
				<input type="submit" value="Create Folder" />
			</form>
		</div>
		
		<div class="splitter"></div>
		
		<div class="bordered" style="flex-grow: 1; flex-basis: 0;">
			<span class="heading">Folder Information</span>
			<hr>
			<table class="slim">
				<tr>
					<td>Files</td>
					<td>${fileCount}</td>
				</tr>
				<tr>
					<td>Folders</td>
					<td>${folderCount}</td>
				</tr>
				<c:if test="${not empty size}">
				<tr>
					<td>Total size</td>
					<td>${size}</td>
				</tr>
				</c:if>
			</table>
		</div>
	</div>
	
	<br>
	
	<div style="text-align: center; border: none; font-size: 14px; padding-top: 8px; padding-bottom: 5px;">
		<span>Please consider using <b>Mozilla Firefox</b> or <b>Google Chrome</b> for the best experience</span>
	</div>

	<script src="js/script.js"></script>

</body>

</html>
