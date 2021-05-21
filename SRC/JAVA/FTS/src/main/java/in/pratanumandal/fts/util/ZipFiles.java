package in.pratanumandal.fts.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFiles {
	
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
	
	/**
	 * Private constructor to disallow creation of instance
	 */
	private ZipFiles() {
		super();
	}

	/**
	 * This method calculates the length of writing a directory to an output stream as an uncompressed zip file
	 * 
	 * @param dir
	 * @param out
	 */
	public static long getZipLength(File dir) {
		CounterOutputStream cos = new CounterOutputStream();
	
		try {
			doZipDirectory(dir, cos);
			return cos.getCounter();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				cos.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return -1;
	}
	
	/**
	 * This method writes a directory to an output stream as an uncompressed zip file
	 * 
	 * @param dir
	 * @param out
	 */
	public static void zipDirectory(File dir, OutputStream out) {
		try {
			doZipDirectory(dir, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method writes a directory to an output stream as an uncompressed zip file
	 * 
	 * @param dir
	 * @param out
	 * @throws IOException
	 */
	private static void doZipDirectory(File dir, OutputStream out) throws IOException {
		// recursively get all file paths in directory
		List<String> filesListInDir = new ArrayList<>();
		populateFilesList(filesListInDir, dir);
		
		// open zip output stream
		ZipOutputStream zos = new ZipOutputStream(out);
		
		// do not compress
		zos.setLevel(ZipOutputStream.STORED);
		
		try {
			for (String filePath : filesListInDir) {
				// for ZipEntry we need to keep only relative file path, so we used substring on absolute path
				ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length() + 1, filePath.length()));
				
				// open file input stream
				FileInputStream fis = new FileInputStream(filePath);
				
				try {
					// put zip entry in zip output stream
					zos.putNextEntry(ze);
					
					// read the file and write to ZipOutputStream
					byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
					int len;
					while ((len = fis.read(buffer)) > 0) {
						zos.write(buffer, 0, len);
					}
					
					// close the current zip entry
					zos.closeEntry();
				}
				finally {
					// close the file input stream
					fis.close();
				}
			}
		}
		finally {
			// close the zip output stream
			zos.close();
		}
	}

	/**
	 * This method populates all the files in a directory to a List
	 * 
	 * @param dir
	 * @throws IOException
	 */
	private static void populateFilesList(List<String> filesListInDir, File dir) throws IOException {
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile())
				filesListInDir.add(file.getAbsolutePath());
			else
				populateFilesList(filesListInDir, file);
		}
	}

	/**
	 * This method compresses the single file to zip format
	 * 
	 * @param file
	 * @param zipFileName
	 */
	public static void zipSingleFile(File file, String zipFileName) {
		try {
			// create ZipOutputStream to write to the zip file
			FileOutputStream fos = new FileOutputStream(zipFileName);
			ZipOutputStream zos = new ZipOutputStream(fos);
			
			// do not compress
			zos.setLevel(ZipOutputStream.STORED);
			
			// add a new Zip Entry to the ZipOutputStream
			ZipEntry ze = new ZipEntry(file.getName());
			zos.putNextEntry(ze);
			
			// read the file and write to ZipOutputStream
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int len;
			while ((len = fis.read(buffer)) > 0) {
				zos.write(buffer, 0, len);
			}

			// Close the zip entry to write to zip file
			zos.closeEntry();
			// Close resources
			zos.close();
			fis.close();
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
