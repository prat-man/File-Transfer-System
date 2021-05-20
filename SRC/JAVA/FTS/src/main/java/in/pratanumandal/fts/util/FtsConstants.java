package in.pratanumandal.fts.util;

import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;

import in.pratanumandal.fts.bean.FtsConfig.Credentials.Credential;

public class FtsConstants {
	
	public static String CONFIG_FILE = getDefaultConfigLocation();
	
	public static String SANDBOX_FOLDER = getDefaultSandboxLocation();
	
	public static List<Credential> CREDENTIALS = null;
	
	public static int PORT = 80;
	
	public static boolean SHOW_FOLDER_SIZE = true;
	
	public static boolean VERBOSE = false;
	
	public static String NAME = "File Transfer System";
	
	public static String DESCRIPTION = "A centralized file sharing repository";
	
	
	private static String getDefaultConfigLocation() {
		return new File("config.xml").getAbsolutePath();
	}
	
	private static String getDefaultSandboxLocation() {
		return new JFileChooser().getFileSystemView().getDefaultDirectory().getAbsolutePath() + "/Sandbox";
	}
	
	public static String getName(String username) {
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
	
}
