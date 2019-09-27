package tk.pratanumandal.fts.util;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.security.access.AccessDeniedException;

import tk.pratanumandal.fts.filesystem.FileSystemRepository;

public class CommonUtils {
	
	public static String getFileSize(File file) throws IOException {
		if (!FtsConstants.SHOW_FOLDER_SIZE && file.isDirectory()) {
			return new String();
		}
		
		FileSystemRepository fsr = FileSystemRepository.getInstance();
		
		BigInteger integerLength = fsr.getFileSize(file);
		
		final AtomicInteger radix = new AtomicInteger(0);
        final BigDecimal BLOCK_SIZE = new BigDecimal(getFileSizeBlocks());
        
        BigDecimal length = new BigDecimal(integerLength);
        
        while (length.compareTo(BLOCK_SIZE) >= 0) {
        	length = length.divide(BLOCK_SIZE, 10, RoundingMode.HALF_UP);
        	radix.set(radix.get() + 1);
        }
        
        String[] radixes = getFileSizeRadixes();
        
        if (radix.get() > radixes.length) {
        	return new String();
        }
        else if (radix.get() == 0) {
        	return length.setScale(0, RoundingMode.HALF_UP).intValue() + " " + radixes[radix.get()];
        }
        else {
        	return length.setScale(2, RoundingMode.HALF_UP).doubleValue() + " " + radixes[radix.get()];
        }
	}
	
	public static boolean deleteFile(File file) {
		if (file.isDirectory()) {
			File[] children = file.listFiles();
			if (children == null) {
				throw new AccessDeniedException("Access denied when trying to delete folder: " + file.getName());
			}
			for (File child : children) {
				deleteFile(child);
			}
		}
		return file.delete();
	}
	
	private static int getFileSizeBlocks() {
		String OS = System.getProperty("os.name").toLowerCase();
		if (OS.contains("windows")) return 1024;
		return 1000;
	}
	
	private static String[] getFileSizeRadixes() {
		String OS = System.getProperty("os.name").toLowerCase();
		if (OS.contains("windows")) return new String[] {"bytes", "KiB", "MiB", "GiB", "TiB", "PiB"};
		return new String[] {"bytes", "KB", "MB", "GB", "TB", "PB"};
	}

}
