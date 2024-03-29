package in.pratanumandal.fts.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;

import org.apache.tika.Tika;
import org.springframework.security.access.AccessDeniedException;

import in.pratanumandal.fts.bean.FtsConfig.Credentials.Credential;
import in.pratanumandal.fts.filesystem.FileSystemRepository;

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
	
	public static String getNameByUsername(String username) {
		for (Credential credential : FtsConstants.CREDENTIALS) {
			if (credential.getUsername().equals(username)) {
				if (credential.getName() != null) {
					return credential.getName();//.split("\\s|,")[0];
				}
				return username;
			}
		}
		
		return username;
	}
	
	public static String imageToBase64JPG(BufferedImage image) throws IOException {
		BufferedImage opaqueImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		Graphics2D g2d = opaqueImage.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, opaqueImage.getWidth(), opaqueImage.getHeight());
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(opaqueImage, "JPG", out);
		byte[] bytes = out.toByteArray();
		
		String base64String = Base64.getEncoder().encodeToString(bytes);
		return "data:image/jpeg;base64," + base64String;
	}
	
	public static String imageToBase64PNG(BufferedImage image) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(image, "PNG", out);
		byte[] bytes = out.toByteArray();
		
		String base64String = Base64.getEncoder().encodeToString(bytes);
		return "data:image/png;base64," + base64String;
	}
	
	public static String getMimeType(File file) throws IOException {
		Tika tika = new Tika();
		return tika.detect(file).split(";")[0];
	}

}
