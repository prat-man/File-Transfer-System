package tk.pratanumandal.fts.filesystem;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileSystemMonitor extends Thread {
	
	protected final WatchService watcher;
    protected final Map<WatchKey, Path> keys;
    protected final ArrayList<FileSystemListener> listeners;
    
    protected boolean kill;
    
	public FileSystemMonitor() throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<>();
        this.listeners = new ArrayList<>();
        this.kill = false;
        this.setDaemon(true);
    }

    public void init(Path dir) throws IOException {
    	this.walkAndRegisterDirectories(dir);
    }
	
    public void addListener(FileSystemListener listener) {
    	this.listeners.add(listener);
    }
	
	protected void registerDirectory(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, new WatchEvent.Kind[] {ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY});
        keys.put(key, dir);
    }
	
	protected void walkAndRegisterDirectories(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                registerDirectory(dir);
                return FileVisitResult.CONTINUE;
            }
            
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
	
	public void kill() {
    	this.kill = true;
    }
	
	@Override
	public void run() {
        while (!kill) {
            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }
 
            Path dir = keys.get(key);
            if (dir == null) {
                continue;
            }
 
            for (WatchEvent<?> event : key.pollEvents()) {
                @SuppressWarnings("rawtypes")
                WatchEvent.Kind kind = event.kind();
 
                // Context for directory entry event is the file name of entry
                @SuppressWarnings("unchecked")
                Path name = ((WatchEvent<Path>)event).context();
                if (name == null) {
                    continue;
                }
                
                Path child = dir.resolve(name);
 
                if (kind == ENTRY_CREATE) {
                	// if directory is created, and watching recursively, then register it and its sub-directories
                    try {
                        if (Files.isDirectory(child)) {
                            this.walkAndRegisterDirectories(child);
                        }
                    } catch (IOException e) {
                    	e.printStackTrace();
                    }
                    // update listeners
                    for (FileSystemListener listener : listeners) {
                		listener.fileCreated(child);
                	}
                }
                else if (kind == ENTRY_MODIFY) {
                	// update listeners
                	for (FileSystemListener listener : listeners) {
                		listener.fileModified(child);
                	}
                }
                else if (kind == ENTRY_DELETE) {
                	// update listeners
                	for (FileSystemListener listener : listeners) {
                		listener.fileDeleted(child);
                	}
                }
            }
 
            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);
 
                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }
	
}
