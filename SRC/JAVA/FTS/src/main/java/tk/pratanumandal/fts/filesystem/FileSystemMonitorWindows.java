package tk.pratanumandal.fts.filesystem;

import static com.sun.nio.file.ExtendedWatchEventModifier.FILE_TREE;
import static java.nio.file.StandardWatchEventKinds.*;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.attribute.BasicFileAttributes;

public class FileSystemMonitorWindows extends FileSystemMonitor {

    public FileSystemMonitorWindows() throws IOException {
		super();
	}
    
    @Override
    public void init(Path dir) throws IOException {
    	this.registerDirectory(dir);
    	this.walkAndLoadDirectories(dir);
    }
    
    @Override
    protected void registerDirectory(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, new WatchEvent.Kind[] {ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY}, FILE_TREE);
        keys.put(key, dir);
    }
	
	@Override
	protected void walkAndRegisterDirectories(final Path start) throws IOException {
		// do nothing
	}
	
	protected void walkAndLoadDirectories(final Path start) throws IOException {
        // load directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            	// update listeners
                for (FileSystemListener listener : listeners) {
            		listener.fileCreated(dir);
            	}
            	return super.postVisitDirectory(dir, exc);
            }
            
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            	// update listeners
                for (FileSystemListener listener : listeners) {
            		listener.fileCreated(file);
            	}
            	return super.visitFile(file, attrs);
            }
            
            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            	// skip subtree if file visit failed
            	return FileVisitResult.SKIP_SUBTREE;
            }
        });
    }
    
}
