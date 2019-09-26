package tk.pratanumandal.fts.filesystem;

import static com.sun.nio.file.ExtendedWatchEventModifier.FILE_TREE;
import static java.nio.file.StandardWatchEventKinds.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;

public class FileSystemMonitorWindows extends FileSystemMonitor {

    public FileSystemMonitorWindows() throws IOException {
		super();
	}
    
    @Override
    public void init(Path dir) throws IOException {
    	this.registerDirectory(dir);
    }
    
    @Override
    protected void registerDirectory(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, new WatchEvent.Kind[] {ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY}, FILE_TREE);
        keys.put(key, dir);
    }
	
	@Override
	protected void walkAndRegisterDirectories(Path start) throws IOException {
		// do nothing
	}
    
}
