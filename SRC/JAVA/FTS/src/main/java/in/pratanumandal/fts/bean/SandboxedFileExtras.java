package in.pratanumandal.fts.bean;

import java.io.File;
import java.io.IOException;

import in.pratanumandal.fts.util.ThumbnailManager;

public class SandboxedFileExtras extends SandboxedFile {
	
	protected String thumbnail;

	public SandboxedFileExtras(File file) throws IOException {
		super(file);
		this.thumbnail = ThumbnailManager.getThumbnail(file);
	}

	public String getThumbnail() {
		return thumbnail;
	}

	@Override
	public String toString() {
		return "SandboxedFileExtras [thumbnail=" + thumbnail + ", name=" + name + ", path=" + path + ", size=" + size
				+ ", icon=" + icon + ", directory=" + directory + ", fileCount=" + fileCount + ", folderCount="
				+ folderCount + ", creationTime=" + creationTime + ", lastModifiedTime=" + lastModifiedTime
				+ ", lastAccessTime=" + lastAccessTime + "]";
	}

}
