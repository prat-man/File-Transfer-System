package tk.pratanumandal.fts.bean;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import tk.pratanumandal.fts.util.CommonUtils;
import tk.pratanumandal.fts.util.FtsConstants;

public class SandboxedFile {

	private String name;
	private String path;
	private String size;
	private boolean directory;
	
	public SandboxedFile(File file) throws IOException {
		this.name = file.getName();
		this.path = file.getAbsolutePath().substring(FtsConstants.SANDBOX_FOLDER.length());
		this.size = CommonUtils.getFileSize(file);
		this.directory = file.isDirectory();
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}
	
	public String getEncodedPath() {
		try {
			String encodedPath = path.replaceAll("\\\\", "/");
			encodedPath = encodedPath.replaceAll("/+", "/");
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

	@Override
	public String toString() {
		return "SandboxedFile [name=" + name + ", path=" + path + ", directory=" + directory + "]";
	}
	
}
