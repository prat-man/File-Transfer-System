package in.pratanumandal.fts.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="fts-config")
@XmlAccessorType(XmlAccessType.FIELD)
public class FtsConfig {
	
	private String name;
	
	private String description;
	
	private int port;
	
	private String sandbox;
	
	private Credentials credentials;
	
	@XmlElement(name="show-folder-size")
	private String showFolderSize;
	
	private String verbose;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getSandbox() {
		return sandbox;
	}

	public void setSandbox(String sandbox) {
		this.sandbox = sandbox;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
	
	public String getShowFolderSize() {
		return showFolderSize;
	}

	public void setShowFolderSize(String showFolderSize) {
		this.showFolderSize = showFolderSize;
	}

	public String getVerbose() {
		return verbose;
	}

	public void setVerbose(String verbose) {
		this.verbose = verbose;
	}

	@XmlRootElement(name="credentials")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Credentials {
		
		@XmlElement(name="credential")
		private List<Credential> credentialList;

		public List<Credential> getCredentialList() {
			return credentialList;
		}

		public void setCredentialList(List<Credential> credentialList) {
			this.credentialList = credentialList;
		}
		
		@XmlRootElement(name="credential")
		@XmlAccessorType(XmlAccessType.FIELD)
		public static class Credential {
			
			private String name;
			
			private String username;
			
			private String password;
			
			private String role;

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getUsername() {
				return username;
			}

			public void setUsername(String username) {
				this.username = username;
			}

			public String getPassword() {
				return password;
			}

			public void setPassword(String password) {
				this.password = password;
			}

			public String getRole() {
				return role;
			}

			public void setRole(String role) {
				this.role = role;
			}
			
		}
		
	}

}
