<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page trimDirectiveWhitespaces="true"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.StringTokenizer"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
	<title>File Transfer System</title>
	
	<link rel="icon" type="image/x-icon" href="favicon.ico">
	
	<meta name="viewport" content="width=device-width, initial-scale=1.0">

	<script src="js/jquery.min.js"></script>
	<script src="js/jquery.form.min.js"></script>
	<script src="js/alertify.min.js"></script>
	
	<link rel="stylesheet" href="css/alertify.min.css" />
	<!-- <link rel="stylesheet" href="css/default.min.css" /> -->
	
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
	
		<h3 class="username">Welcome, ${name}!</h3>
		
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
				
				out.write(
						"<a href=\"/\" class=\"home\">" +
							"<img src=\"img/home-pathbar.svg\" alt=\"/\" width=\"16px\" class=\"normal\" />" +
							"<img src=\"img/home-pathbar-hover.svg\" alt=\"/\" width=\"16px\" class=\"hover\" />" +
						"</a>" +
						"<font size=\"5\"></font>"
						);
				
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
					<p class="center">No Files Found</p>
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
								<td class="pad-right right">
									<span style="white-space: nowrap;">${file.getSize()}</span>
								</td>
								<td class="pad-left pad-right sticky">
									<a onclick="viewFile('${file.getPath()}')">
										<img src="img/view.svg" alt="View" width="20px" class="img-button" />
									</a>
								</td>
								<c:if test="${admin}">
								<td class="pad-left pad-right sticky">
									<a onclick="deleteFile('${file.getName()}', '${file.getEncodedPath()}')">
										<img src="img/trash.svg" alt="Delete" width="20px" class="img-button" />
									</a>
								</td>
								</c:if>
								<td class="pad-left sticky">
									<a href="/download?path=${file.getEncodedPath()}" target="_BLANK">
										<img src="img/download.svg" alt="Download" width="20px" class="img-button" />
									</a>
								</td>
							</tr>
						</c:forEach>
					</table>
				</c:otherwise>
			</c:choose>
		</div>
	
		<br>
	
		<c:if test="${admin || writer}">
		<div class="flex-wrapper">
			<div class="bordered" style="flex-grow: 1; flex-basis: 0;">
				<span class="heading">Select one or more files to upload</span>
				<div class="padded">
					<form id="fileUploadForm" method="POST" action="/uploadFiles" enctype="multipart/form-data">
						<label class="file pad-bottom">
							<input type="file" id="fileUploadField" name="files" multiple required="required" />
							<span class="file-custom" id="fileUploadLabel">Choose file(s)</span>
						</label>
						<input type="text" id="fileNames" name="fileNames" style="display: none;" />
						<input type="text" name="path" value="${path}" style="display: none;" />
						<input id="fileUploadSubmit" type="submit" value="Upload File(s)" />
					</form>
					<div class="progress-bar" id="fileUploadProgressBar">
						<div class="progress-track">
					    	<div class="progress" id="fileUploadProgress"></div>
					    </div>
					</div>
					<p id="fileUploadProgressValue" class="progress-label"></p>
				</div>
			</div>
			
			<div class="splitter" id="folderUploadSplitter"></div>
			
			<div class="bordered" style="flex-grow: 1; flex-basis: 0;" id="folderUpload">
				<span class="heading">Select a folder to upload</span>
				<div class="padded">
					<form id="folderUploadForm" method="POST" action="/uploadFiles" enctype="multipart/form-data">
						<label class="file pad-bottom">
							<input type="file" id="folderUploadField" name="files" webkitdirectory mozdirectory msdirectory odirectory directory required="required" />
							<span class="file-custom" id="folderUploadLabel">Choose folder</span>
						</label>
						<input type="text" id="fileNames" name="fileNames" style="display: none;" />
						<input type="text" name="path" value="${path}" style="display: none;" />
						<span id="folderUploadSubmit">
							<input type="submit" value="Upload Folder" />
						</span>
					</form>
					<div class="progress-bar" id="folderUploadProgressBar">
						<div class="progress-track">
					    	<div class="progress" id="folderUploadProgress"></div>
					    </div>
					</div>
					<p id="folderUploadProgressValue" class="progress-label"></p>
				</div>
			</div>
		</div>
		
		<br>
		</c:if>
		
		<div style="display: flex; flex-wrap: wrap; justify-content: space-between; padding: 0; border: none;">
			<c:if test="${admin || writer}">
			<div class="bordered" style="flex-grow: 1; flex-basis: 0;">
				<span class="heading">Create a new folder</span>
				<form id="createFolderForm" class="padded">
					<div class="pad-bottom">
						<input type="text" name="folderName" required="required" placeholder="Enter folder name" />
					</div>
					<input type="text" name="path" value="${path}" style="display: none;" />
					<input type="submit" value="Create Folder" />
				</form>
			</div>
			
			<div class="splitter"></div>
			</c:if>
			
			<div class="bordered" style="flex-grow: 1; flex-basis: 0;">
				<span class="heading">Folder Information</span>
				<table class="slim">
					<tr>
						<td>Folders</td>
						<td>${folderCount}</td>
					</tr>
					<tr>
						<td>Files</td>
						<td>${fileCount}</td>
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
	
	</div>
	
	<div class="push"></div>
	
	<div class="footer" id="footer">
		<span>Please consider using <b>Mozilla Firefox</b> or <b>Google Chrome</b> for the best experience</span>
	</div>

	<script src="js/common.js"></script>
	<script src="js/script.js"></script>

</body>

</html>
