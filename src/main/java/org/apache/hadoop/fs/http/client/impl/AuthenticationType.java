package org.apache.hadoop.fs.http.client.impl;

import org.apache.hadoop.fs.http.client.WebHDFSConnection;

public enum AuthenticationType {
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