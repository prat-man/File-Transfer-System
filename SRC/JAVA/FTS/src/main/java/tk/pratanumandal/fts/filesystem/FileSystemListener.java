package tk.pratanumandal.fts.filesystem;

import java.nio.file.Path;

public interface FileSystemListener {
	
	public void fileCreated(Path path);
	
	public void fileModified(Path path);
	
	public void fileDeleted(Path path);

}
