package in.pratanumandal.fts.bean;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

import in.pratanumandal.fts.exception.ForbiddenException;
import in.pratanumandal.fts.util.CommonUtils;
import in.pratanumandal.fts.util.FtsConstants;

public class SandboxedFile {

	protected String name;
	protected String path;
	protected String size;
	
	protected boolean directory;
	protected int fileCount;
	protected int folderCount;
	
	protected String creationTime;
	protected String lastModifiedTime;
	protected String lastAccessTime;
	
	public SandboxedFile(File file) throws IOException {
		this.name = file.getName();
		this.path = file.getAbsolutePath().substring(FtsConstants.SANDBOX_FOLDER.length()).replaceAll("\\\\", "/");
		this.size = CommonUtils.getFileSize(file);
		this.directory = file.isDirectory();
		
		if (this.directory) {
			File[] listOfFiles = file.listFiles();
			
			if (listOfFiles == null) {
				//logger.error("An error occurred when trying to access path: " + path);
				throw new ForbiddenException("Access Denied");
			}
			
			this.fileCount = 0;
			this.folderCount = 0;
			
			for (File child : listOfFiles) {
				if (child.isDirectory()) folderCount++;
				else fileCount++;
			}
		}
		else {
			this.fileCount = -1;
			this.folderCount = -1;
		}
		
		BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
		
		this.creationTime = FtsConstants.DATE_FORMAT.format(attr.creationTime().toMillis());
		this.lastModifiedTime = FtsConstants.DATE_FORMAT.format(attr.lastModifiedTime().toMillis());
		this.lastAccessTime = FtsConstants.DATE_FORMAT.format(attr.lastAccessTime().toMillis());
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}
	
	public String getEncodedPath() {
		try {
			String encodedPath = path.replaceAll("/+", "/");
			return URLEncoder.encode(encodedPath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	public String getSize() {
		return size;
	}

	public boolean isDirectory() {
		return directory;
	}

	public int getFileCount() {
		return fileCount;
	}

	public int getFolderCount() {
		return folderCount;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public String getLastModifiedTime() {
		return lastModifiedTime;
	}

	public String getLastAccessTime() {
		return lastAccessTime;
	}

	@Override
	public String toString() {
		return "SandboxedFile [name=" + name + ", path=" + path + ", size=" + size + ", directory=" + directory
				+ ", fileCount=" + fileCount + ", folderCount=" + folderCount + ", creationTime=" + creationTime
				+ ", lastModifiedTime=" + lastModifiedTime + ", lastAccessTime=" + lastAccessTime + "]";
	}
	
}
