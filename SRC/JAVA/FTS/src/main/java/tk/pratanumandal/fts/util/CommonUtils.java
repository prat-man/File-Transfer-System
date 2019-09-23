package tk.pratanumandal.fts.util;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

public class CommonUtils {
	
	public static String getFileSize(File file) {
		
		String[] radixes = getFileSizeRadixes();
		
		if (file.isDirectory()) {
			if (FtsConstants.SHOW_FOLDER_SIZE) {
				Path folderPath = Paths.get(file.getAbsolutePath());
				final BigDecimal[] size = {BigDecimal.ZERO};
				final AtomicInteger radix = new AtomicInteger(0);
				final BigDecimal BLOCK_SIZE = new BigDecimal(getFileSizeBlocks());
	
		        try {
					Files.walkFileTree(folderPath, new SimpleFileVisitor<Path>() {
					    @Override
					    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
					    	BigDecimal currentRadix = BLOCK_SIZE.pow(radix.get());
					        size[0] = size[0].add(new BigDecimal(attrs.size()).divide(currentRadix, 10, RoundingMode.HALF_UP));
					        while (size[0].compareTo(BLOCK_SIZE) >= 0) {
					        	size[0] = size[0].divide(BLOCK_SIZE, 10, RoundingMode.HALF_UP);
					        	radix.set(radix.get() + 1);
					        }
					        return FileVisitResult.CONTINUE;
					    }
					    
					    @Override
		                public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException {
		                    return FileVisitResult.SKIP_SUBTREE;
		                }
					});
				} catch (IOException e) {
					return new String();
				}
		        
		        if (radix.get() > radixes.length) {
		        	return new String();
		        }
		        else if (radix.get() == 0) {
		        	return size[0].setScale(0, RoundingMode.HALF_UP).intValue() + " " + radixes[radix.get()];
		        }
		        else {
		        	return size[0].setScale(2, RoundingMode.HALF_UP).doubleValue() + " " + radixes[radix.get()];
		        }
			}
			else {
				return new String();
			}
		}
		else {
			double fileSize = file.length();
			int radix = 0;
			int BLOCK_SIZE = getFileSizeBlocks();
			
			while (fileSize >= BLOCK_SIZE) {
				fileSize /= BLOCK_SIZE;
				radix++;
			}
			
			if (radix > radixes.length) {
	        	return new String();
	        }
			else if (radix == 0) {
	        	return new BigDecimal(fileSize).setScale(0, RoundingMode.HALF_UP).intValue() + " " + radixes[radix];
	        }
	        else {
	        	return new BigDecimal(fileSize).setScale(2, RoundingMode.HALF_UP).doubleValue() + " " + radixes[radix];
	        }
		}
		
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
