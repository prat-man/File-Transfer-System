package tk.pratanumandal.fts.filesystem;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.SystemUtils;

import tk.pratanumandal.fts.util.FtsConstants;

public class FileSystemRepository implements FileSystemListener {
	
	// singleton model
	private static FileSystemRepository instance;

	private final Map<String, BigInteger> fileLengthMap;
	private final FileSystemMonitor fsm;
	
	// singleton model
	private FileSystemRepository() throws IOException {
		this.fileLengthMap = new HashMap<>();
		
		if (SystemUtils.IS_OS_WINDOWS) {
			this.fsm = new FileSystemMonitorWindows();
		} else {
			this.fsm = new FileSystemMonitor();
		}
		
		this.fsm.addListener(this);
		
		this.fsm.init(Paths.get(FtsConstants.SANDBOX_FOLDER));
		
		this.fsm.start();
	}

	@Override
	public void fileCreated(Path path) {
		if (FtsConstants.VERBOSE) {
			System.out.println("File Created: " + path);
		}
		this.fileLengthMap.put(path.toString(), getFileSizeInit(path.toFile()));
	}

	@Override
	public void fileModified(Path path) {
		if (FtsConstants.VERBOSE) {
			System.out.println("File Modified: " + path);
		}
		// update upto root (exclusive)
		while (path.compareTo(Paths.get(FtsConstants.SANDBOX_FOLDER)) != 0) {
			this.fileLengthMap.put(path.toString(), getFileSizeInit(path.toFile()));
			path = path.getParent();
		}
		// update root
		this.fileLengthMap.put(path.toString(), getFileSizeInit(path.toFile()));
	}

	@Override
	public void fileDeleted(Path path) {
		if (FtsConstants.VERBOSE) {
			System.out.println("File Deleted: " + path);
		}
		this.fileLengthMap.remove(path.toString());
	}
	
	private BigInteger getFileSizeInit(File file) {
		if (file.isDirectory()) {
			if (FtsConstants.SHOW_FOLDER_SIZE) {
				BigInteger length = BigInteger.ZERO;
				File[] children = file.listFiles();
	            if (children != null) {
	                for (File child : children) {
	                	BigInteger childLength = fileLengthMap.get(child.getAbsolutePath());
	                	if (childLength != null) {
	                		length = length.add(childLength);
	                	}
	                }
	            }
	            return length;
			}
			else {
				return BigInteger.ZERO;
			}
		}
		else {
			return BigInteger.valueOf(file.length());
		}
	}
	
	public BigInteger getFileSize(File file) {
		// return the file size
		return this.fileLengthMap.get(file.getAbsolutePath());
	}
	
	public void destroy() {
		this.fsm.kill();
	}
	
	public static FileSystemRepository getInstance() throws IOException {
		if (instance == null) {
			instance = new FileSystemRepository();
		}
		return instance;
	}
	
}
