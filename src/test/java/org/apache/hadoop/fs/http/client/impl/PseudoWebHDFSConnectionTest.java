package org.apache.hadoop.fs.http.client.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.hadoop.fs.http.client.util.Closeables;
import org.apache.hadoop.security.authentication.client.AuthenticationException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PseudoWebHDFSConnectionTest {

	static PseudoWebHDFSConnection pConn = null;
	static final String user = System.getProperty("user.name");
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		pConn = new PseudoWebHDFSConnection("http://hdfs-01:50075", user, "anything");
	}
	
	@Before
	public void setUp() throws Exception {
		System.out.println(" setUp... per Test ...");
	}

	@Test
	public void getHomeDirectory() throws MalformedURLException, IOException, AuthenticationException {
		pConn.getHomeDirectory();
	}
	
	@Test
	public void listStatus() throws MalformedURLException, IOException, AuthenticationException {
		String path = String.format("user/%s", user);
		pConn.listStatus(path);
	}
	
	@Test
	public void create() throws MalformedURLException, IOException, AuthenticationException {
		InputStream is = null;
		String filename = "shortened-1.tsv";
		String path = String.format("user/%s/%s", user, filename);

		try {
			is = getClass().getResourceAsStream(filename);
			pConn.create(path, is);
		}
		finally {
			Closeables.closeQuietly(is);
		}
	}

	//TODO Test Other PseudoWebHDFSConnectionTest's Method
}
