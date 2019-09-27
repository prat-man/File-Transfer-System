package tk.pratanumandal.fts.util;

import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;

import tk.pratanumandal.fts.bean.FtsConfig.Credentials.Credential;

public class FtsConstants {
	
	public static String CONFIG_FILE = getDefaultConfigLocation();
	
	public static String SANDBOX_FOLDER = getDefaultSandboxLocation();
	
	public static List<Credential> CREDENTIALS = null;
	
	public static int PORT = 80;
	
	public static boolean SHOW_FOLDER_SIZE = true;
	
	public static boolean DELETE = true;
	
	public static boolean VERBOSE = false;
	
	
	private static String getDefaultConfigLocation() {
		return new File("config.xml").getAbsolutePath();
	}
	
	private static String getDefaultSandboxLocation() {
		return new JFileChooser().getFileSystemView().getDefaultDirectory().getAbsolutePath() + "/Sandbox";
	}
	
}
