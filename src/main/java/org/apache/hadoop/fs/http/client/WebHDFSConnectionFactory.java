package org.apache.hadoop.fs.http.client;

import org.apache.hadoop.fs.http.client.impl.KerberosWebHDFSConnection;
import org.apache.hadoop.fs.http.client.impl.PseudoWebHDFSConnection;
import org.apache.hadoop.fs.http.client.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author wesley
 */
public class WebHDFSConnectionFactory {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	/** The default host to connect to */
	public static final String DEFAULT_HOST = "localhost";

	/** The default port */
	public static final int DEFAULT_PORT = 50075;

	/** The default username */
	public static final String DEFAULT_USERNAME = "";

	/** The default password */
	public static final String DEFAULT_PASSWORD = "";
	
	public static final String DEFAULT_PROTOCOL = "http://";
	
	public static final String DEFAULT_URL = DEFAULT_PROTOCOL + DEFAULT_HOST + ":" + DEFAULT_PORT;

	public static enum AuthenticationType {
		KERBEROS {
			@Override
			public WebHDFSConnection createConnection(String httpfsUrl, String username, String password) {
				return new KerberosWebHDFSConnection(httpfsUrl, username, password);
			}
		},
		PSEUDO {
			@Override
			public WebHDFSConnection createConnection(String httpfsUrl, String username, String password) {
				return new PseudoWebHDFSConnection(httpfsUrl, username, password);
			}
		};
		
		public abstract WebHDFSConnection createConnection(String httpfsUrl, String username, String password);
	}

	private int port = DEFAULT_PORT;
	private String host = DEFAULT_HOST;
	private String username = DEFAULT_USERNAME;
	private String password = DEFAULT_PASSWORD;
	private AuthenticationType authenticationType = AuthenticationType.KERBEROS;
	private WebHDFSConnection webHDFSConnection;
	
	/**
	 * Creates a new WebHDFSConnectionFactory instance.
	 *
	 */
	public WebHDFSConnectionFactory() {
	}
	
	/**
	 * Creates a new WebHDFSConnectionFactory instance.
	 *
	 * @param host
	 * 			the WebHDFS hostname
	 * @param port
	 * 			the WebHDFS port number
	 * @param username
	 * 			the user name
	 * @param password
	 * 			the user's password
	 * @param authType
	 * 			the 
	 */
	public WebHDFSConnectionFactory(String host, int port , String username, String password, AuthenticationType authType) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.authenticationType = authType;
	}
	
	public WebHDFSConnection getConnection() {
		Assert.notNull(host, "Property <host> must not be null");
		Assert.notNull(port, "Property <port> must not be null");
		Assert.notNull(username, "Property <username> must not be null");
		Assert.notNull(authenticationType, "Property <authenticationType> must not be null");
		
		String httpfsUrl = String.format("%s%s:%s", DEFAULT_PROTOCOL, host, port);

		if(webHDFSConnection == null) {
			webHDFSConnection = authenticationType.createConnection(httpfsUrl, username, password);
		}

		return webHDFSConnection;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
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

	public AuthenticationType getAuthenticationType() {
		return authenticationType;
	}

	public void setAuthenticationType(AuthenticationType authenticationType) {
		this.authenticationType = authenticationType;
	}
}
