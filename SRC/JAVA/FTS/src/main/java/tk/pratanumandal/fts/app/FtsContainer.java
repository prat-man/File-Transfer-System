package tk.pratanumandal.fts.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidParameterException;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.stereotype.Component;

import tk.pratanumandal.fts.bean.FtsConfig;
import tk.pratanumandal.fts.filesystem.FileSystemRepository;
import tk.pratanumandal.fts.util.FtsConfigLoader;
import tk.pratanumandal.fts.util.FtsConstants;

@Component
public class FtsContainer implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

	@Autowired
	private Logger logger;
	
	@Override
	public void customize(ConfigurableServletWebServerFactory factory) {
		
		File file = new File(FtsConstants.CONFIG_FILE);
		
		if (file.exists()) {
			FtsConfig config;
			try {
				config = FtsConfigLoader.load(file);
				
				if (config.getPort() > 0) {
					FtsConstants.PORT = config.getPort();
				}
				factory.setPort(FtsConstants.PORT);
				
				if (config.getSandbox() != null) {
					FtsConstants.SANDBOX_FOLDER = new File(config.getSandbox()).getAbsolutePath();
				}
				
				if (config.getCredentials() != null) {
					FtsConstants.CREDENTIALS = config.getCredentials().getCredentialList();
				}
				
				if (config.getShowFolderSize() != null) {
					if (config.getShowFolderSize().equalsIgnoreCase("true")) {
						FtsConstants.SHOW_FOLDER_SIZE = true;
					}
					else if (config.getShowFolderSize().equalsIgnoreCase("false")) {
						FtsConstants.SHOW_FOLDER_SIZE = false;
					}
					else {
						throw new InvalidParameterException("Element 'show-folder-size' can only be 'true' or 'false'. Unrecognized value: " + config.getShowFolderSize());
					}
				}
				
				if (config.getDelete() != null) {
					if (config.getDelete().equalsIgnoreCase("allow")) {
						FtsConstants.DELETE = true;
					}
					else if (config.getDelete().equalsIgnoreCase("disallow")) {
						FtsConstants.DELETE = false;
					}
					else {
						throw new InvalidParameterException("Element 'delete' can only be 'allow' or 'disallow'. Unrecognized value: " + config.getDelete());
					}
				}
				
				if (config.getVerbose() != null) {
					if (config.getVerbose().equalsIgnoreCase("true")) {
						FtsConstants.VERBOSE = true;
					}
					else if (config.getVerbose().equalsIgnoreCase("false")) {
						FtsConstants.VERBOSE = false;
					}
					else {
						throw new InvalidParameterException("Element 'verbose' can only be 'true' or 'false'. Unrecognized value: " + config.getVerbose());
					}
				}
				
				logger.info("Configuration file loaded");
			} catch (JAXBException | InvalidParameterException e) {
				logger.error("An error occurred when trying to load configuration file");
				e.printStackTrace();
			}
		}
		else {
			logger.warn("Configuration file not found");
		}
		
		try {
			Files.createDirectories(Paths.get(FtsConstants.SANDBOX_FOLDER));
		} catch (IOException e) {
			logger.error("An error occurred when trying to create sandbox folder");
			e.printStackTrace();
		}
		
		try {
			FileSystemRepository.getInstance();
			logger.info("File system monitor started");
		} catch (IOException e) {
			logger.error("An error occurred when trying to initialize file system repository");
			e.printStackTrace();
		}
		
	}

}
