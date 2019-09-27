package tk.pratanumandal.fts.controller;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;
import javax.websocket.server.PathParam;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import tk.pratanumandal.fts.bean.SandboxedFile;
import tk.pratanumandal.fts.exception.ForbiddenException;
import tk.pratanumandal.fts.exception.InvalidFileNameException;
import tk.pratanumandal.fts.exception.ResourceNotFoundException;
import tk.pratanumandal.fts.util.CommonUtils;
import tk.pratanumandal.fts.util.FtsConstants;
import tk.pratanumandal.fts.util.ZipFiles;

@Controller
public class FtsController {
	
	@Autowired
	private Logger logger;
	
	@RequestMapping("/**/{[path:[^\\.]*}")
	public String any() {
		throw new ResourceNotFoundException("The requested path was not found on the server");
	}
	
	@GetMapping("/ping")
	public void ping(HttpServletRequest request, HttpServletResponse response) {
		response.setStatus(200);
	}
	
	@GetMapping("/login")
	public String login(@PathParam("logout") String logout, HttpServletResponse response) {
		
		if (logout != null || 
			(SecurityContextHolder.getContext().getAuthentication() != null &&
			 SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
			 !(SecurityContextHolder.getContext().getAuthentication()  instanceof AnonymousAuthenticationToken))) {
			try {
				response.sendRedirect("/");
			} catch (IOException e) {
				logger.error("An error occurred when trying to log in");
				e.printStackTrace();
			}
		}
		
		return "login";
	}
	
	@GetMapping("/")
	public String index(@PathParam("path") String path, Map<String, Object> model) throws IOException {
		
		path = validatePath(path);

		List<SandboxedFile> files = new ArrayList<>();

		File folder = new File(FtsConstants.SANDBOX_FOLDER + "/" + path);
		
		if (!folder.exists()) {
			logger.error("An error occurred when trying to access path: " + path);
			throw new ResourceNotFoundException("The requested folder was not found on the server");
		}
		
		File[] listOfFiles = folder.listFiles();
		
		if (listOfFiles == null) {
			logger.error("An error occurred when trying to access path: " + path);
			throw new ForbiddenException("Access Denied");
		}

		Arrays.sort(listOfFiles, (obj1, obj2) -> {
			File file1 = (File) obj1;
			File file2 = (File) obj2;

			if (file1.isDirectory() && !file2.isDirectory()) {
				return -1;
			} else if (!file1.isDirectory() && file2.isDirectory()) {
				return +1;
			} else {
				return file1.compareTo(file2);
			}
		});

		for (File file : listOfFiles) {
			boolean hasAccess = true;
			
			if (file.isDirectory()) {
				if (file.listFiles() == null) {
					hasAccess = false;
				}
			}
			else {
				try (
					FileInputStream fin = new FileInputStream(file);
				){
				} catch (IOException e) {
					hasAccess = false;
				}
			}
			
			if (hasAccess) {
				try {
					SandboxedFile sandboxedFile = new SandboxedFile(file);
					files.add(sandboxedFile);
				} catch (IOException e) {
					logger.error("An error occurred when trying to access path: " + path);
					e.printStackTrace();
				}
			}
		}
		
		int fileCount = 0, folderCount = 0;
		
		for (File file : listOfFiles) {
			if (file.isDirectory()) folderCount++;
			else fileCount++;
		}
		
		String size = CommonUtils.getFileSize(folder);

		model.put("files", files);
		
		model.put("path", path);
		
		model.put("fileCount", fileCount);
		
		model.put("folderCount", folderCount);
		
		model.put("size", size);

		return "index";
	}

	@GetMapping("/download")
	public void getFile(@PathParam("path") String path, HttpServletResponse response) throws IOException {
		
		path = validatePath(path);

		File file = new File(FtsConstants.SANDBOX_FOLDER + "/" + path);
		
		if (!file.exists()) {
			logger.error("An error occurred when trying to access path: " + path);
			throw new ResourceNotFoundException("The requested file or folder was not found on the server");
		}

		if (file.isDirectory()) {
			ZipFiles zipFiles = new ZipFiles();

			File zipFile = File.createTempFile("fts-", ".zip");

			zipFiles.zipDirectory(file, zipFile.getAbsolutePath());

			zipFile.deleteOnExit();

			response.setContentType("application/force-download");
			response.setContentLength((int) zipFile.length());
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + zipFile.getName());

			try {
				FileInputStream is = new FileInputStream(zipFile);
				IOUtils.copy(is, response.getOutputStream());
				response.flushBuffer();
				is.close();
			} catch (IOException ex) {
				logger.error("An error occurred when trying to download file: " + path);
				throw new RuntimeException("IOError writing file to output stream");
			}

			zipFile.delete();
		} else {
			response.setContentType("application/force-download");
			response.setContentLength((int) file.length());
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName());

			try {
				FileInputStream is = new FileInputStream(file);
				IOUtils.copy(is, response.getOutputStream());
				response.flushBuffer();
				is.close();
			} catch (IOException ex) {
				logger.error("An error occurred when trying to download file: " + path);
				throw new RuntimeException("IOError writing file to output stream");
			}
		}
	}
	
	@GetMapping("/icon")
	public void getIcon(@PathParam("path") String path, HttpServletResponse response) throws IOException {
		
		path = validatePath(path);

		File file = new File(FtsConstants.SANDBOX_FOLDER + "/" + path);
		
		if (!file.exists()) {
			logger.error("An error occurred when trying to access path: " + path);
			throw new ResourceNotFoundException("The requested icon was not found on the server");
		}

		Icon icon = FileSystemView.getFileSystemView().getSystemIcon(file);

		if (icon != null) {
			BufferedImage bi = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(),
					BufferedImage.TYPE_INT_ARGB);

			Graphics2D g2d = bi.createGraphics();
			icon.paintIcon(null, g2d, 0, 0);
			g2d.dispose();

			response.setContentType("image/png");
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Content-Disposition", "attachment; filename=\"icon.png\"");

			try {
				ImageIO.write(bi, "png", response.getOutputStream());
				response.flushBuffer();
			} catch (IOException ex) {
				logger.error("An error occurred when trying to access icon for path: " + path);
				throw new RuntimeException("IOError writing file to output stream");
			}
		}
	}
	
	@DeleteMapping("/delete")
	public void deleteFile(@PathParam("path") String path, HttpServletResponse response) {
		
		if (!FtsConstants.DELETE) {
			String ipAddress = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRemoteAddr();
			logger.warn("An attempt was made to delete files / folders from IP: " + ipAddress);
			throw new ForbiddenException("Deletion of files and folders is forbidden");
		}
		
		path = validatePath(path);
		
		if (path.equals(new String())) {
			String ipAddress = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRemoteAddr();
			logger.warn("An attempt was made to delete root directory from IP: " + ipAddress);
			throw new ForbiddenException("Deletion of root directory is forbidden");
		}
		
		File file = new File(FtsConstants.SANDBOX_FOLDER + "/" + path);
		
		if (!file.exists()) {
			logger.error("An error occurred when trying to access path: " + path);
			throw new ResourceNotFoundException("The requested file or folder was not found on the server");
		}
		
		try {
			// delete file or directory
			boolean deleted = CommonUtils.deleteFile(file);
			
			if (!deleted) {
				logger.error("An error occurred when trying to delete file/folder: " + path);
				throw new RuntimeException("Failed to delete file/folder: \"" + path + "\"");
			}
		}
		catch (AccessDeniedException e) {
			logger.error("An error occurred when trying to delete file/folder: " + path);
			e.printStackTrace();
			throw new RuntimeException("Failed to delete file/folder: \"" + path + "\"");
		}
		
		response.setStatus(200);
	}

	@PostMapping("/uploadFile")
	public void fileUpload(@RequestParam("file") MultipartFile file, @RequestParam("path") String path, HttpServletResponse response) {
		
		path = validatePath(path);
		
		if (!file.isEmpty()) {
			try {
				String fileName;
				File fileObj = new File(file.getOriginalFilename());
				if (fileObj.isAbsolute()) {
					fileName = fileObj.getName();
				} else {
					fileName = file.getOriginalFilename();
				}
				fileName = validatePath(fileName);
				Path filePath;
				if (path.isEmpty()) {
					filePath = Paths.get(FtsConstants.SANDBOX_FOLDER + "/" + fileName);
				} else {
					filePath = Paths.get(FtsConstants.SANDBOX_FOLDER + "/" + path + "/" +  fileName);
				}
				Files.createDirectories(filePath.getParent());
				Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				logger.error("An error occurred when trying to upload file to server");
				e.printStackTrace();
				response.setStatus(500);
				return;
			}
		}
		
		response.setStatus(200);
	}
	
	@PostMapping("/uploadFolder")
	public void folderUpload(@RequestParam("files") List<MultipartFile> files, @RequestParam("fileNames") String fileNames, @RequestParam("path") String path, HttpServletResponse response) {
		
		path = validatePath(path);
		
		String[] fileNameArr = fileNames.split(";", -1);
		
		for (int index = 0; index < files.size(); index++) {
			
			MultipartFile file = files.get(index);
			
			if (!file.isEmpty()) {
				try {
					String fileName;
					File fileObj = new File(file.getOriginalFilename());
					if (fileObj.isAbsolute()) {
						if (index < fileNameArr.length && !fileNameArr[index].isEmpty()) {
							fileName = fileNameArr[index];
						} else {
							fileName = fileObj.getName();
						}
					} else {
						fileName = file.getOriginalFilename();
					}
					fileName = validatePath(fileName);
					Path filePath;
					if (path.isEmpty()) {
						filePath = Paths.get(FtsConstants.SANDBOX_FOLDER + "/" + fileName);
					} else {
						filePath = Paths.get(FtsConstants.SANDBOX_FOLDER + "/" + path + "/" +  fileName);
					}
					Files.createDirectories(filePath.getParent());
					Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					logger.error("An error occurred when trying to upload folder to server");
					e.printStackTrace();
					response.setStatus(500);
					return;
				}
			}
		}
		
		response.setStatus(200);
	}
	
	@PostMapping("/createFolder")
	public void createFolder(@RequestParam("folderName") String folderName, @RequestParam("path") String path, HttpServletResponse response) {
		
		folderName = validatePath(folderName);
		path = validatePath(path);
		
		Path filePath;
		
		try {
			if (path.isEmpty()) {
				filePath = Paths.get(FtsConstants.SANDBOX_FOLDER + "/" + folderName);
			} else {
				filePath = Paths.get(FtsConstants.SANDBOX_FOLDER + "/" + path + "/" +  folderName);
			}
		} catch (InvalidPathException e) {
			logger.error("An error occurred when trying to create folder: " + folderName);
			throw new InvalidFileNameException("Invalid Folder Name: \"" + folderName + "\"");
		}
		
		try {
			Files.createDirectories(filePath);
		} catch (IOException e) {
			logger.error("An error occurred when trying to create folder structure");
			e.printStackTrace();
		}
		
		response.setStatus(200);
	}
	
	private String validatePath(String path) {
		
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
		else {
			path = new String();
		}
		
		Pattern pattern = Pattern.compile("((/|\\\\)(\\s)*\\.(\\s)*\\.(\\s)*(/|\\\\))|"			// in between
										 + "(^(\\s)*\\.(\\s)*\\.(\\s)*(/|\\\\))|"				// at beginning
										 + "((/|\\\\)\\.(\\s)*\\.(\\s)*$)|"						// at end
										 + "(^(/|\\\\)*(\\s)*\\.(\\s)*\\.(\\s)*(/|\\\\)*$)");	// only ..
		
		Matcher matcher = pattern.matcher(path);

		if (matcher.find()) {
			String ipAddress = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getRemoteAddr();
			logger.warn("An attempt was made to access parent directory from IP: " + ipAddress);
			throw new ForbiddenException("Access to parent directory in path is forbidden");
		}
		
		return path;
	}

}
