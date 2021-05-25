package in.pratanumandal.fts.bean;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

import in.pratanumandal.fts.exception.ForbiddenException;
import in.pratanumandal.fts.util.FtsConstants;
import in.pratanumandal.fts.util.ThumbnailManager;

public class SandboxedFileExtras extends SandboxedFile {
	
	protected String thumbnail;
	
	protected int fileCount;
	protected int folderCount;
	
	protected String creationTime;
	protected String lastModifiedTime;
	protected String lastAccessTime;

	public SandboxedFileExtras(File file) throws IOException {
		super(file);
        
		this.thumbnail = ThumbnailManager.getThumbnail(file);
		
		if (this.directory) {
			File[] listOfFiles = file.listFiles();
			
			if (listOfFiles == null) {
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

	public String getThumbnail() {
		return thumbnail;
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
		return "SandboxedFileExtras [thumbnail=" + thumbnail + ", fileCount=" + fileCount + ", folderCount="
				+ folderCount + ", creationTime=" + creationTime + ", lastModifiedTime=" + lastModifiedTime
				+ ", lastAccessTime=" + lastAccessTime + ", name=" + name + ", path=" + path + ", size=" + size
				+ ", icon=" + icon + ", directory=" + directory + "]";
	}

}
