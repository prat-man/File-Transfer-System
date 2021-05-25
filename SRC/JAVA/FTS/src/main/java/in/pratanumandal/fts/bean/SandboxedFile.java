package in.pratanumandal.fts.bean;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import in.pratanumandal.fts.util.CommonUtils;
import in.pratanumandal.fts.util.FtsConstants;
import in.pratanumandal.fts.util.IconManager;

public class SandboxedFile {

	protected String name;
	protected String path;
	protected String size;
	protected String icon;
	protected boolean directory;
	
	public SandboxedFile(File file) throws IOException {
		this.name = file.getName();
		this.path = file.getAbsolutePath().substring(FtsConstants.SANDBOX_FOLDER.length()).replaceAll("\\\\", "/");
		this.size = CommonUtils.getFileSize(file);
        
		this.icon = IconManager.getIcon(file);
		
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
			String encodedPath = path.replaceAll("/+", "/");
			return URLEncoder.encode(encodedPath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	public String getSize() {
		return size;
	}
	
	public String getIcon() {
		return icon;
	}

	public boolean isDirectory() {
		return directory;
	}

	@Override
	public String toString() {
		return "SandboxedFile [name=" + name + ", path=" + path + ", size=" + size + ", icon=" + icon + ", directory="
				+ directory + "]";
	}
	
}
