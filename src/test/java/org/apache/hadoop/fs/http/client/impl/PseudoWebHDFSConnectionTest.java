package org.apache.hadoop.fs.http.client.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Map;

import org.apache.hadoop.fs.http.client.WebHDFSConnectionFactory;
import org.apache.hadoop.fs.http.client.util.Closeables;
import org.apache.hadoop.fs.http.client.util.ResponseUtil;
import org.apache.hadoop.security.authentication.client.AuthenticationException;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class PseudoWebHDFSConnectionTest {

	static final String FILENAME = "shortened-1.tsv";
	static final String USER = System.getProperty("user.name");
	static final String BASE = String.format("user/%s", USER);
	static final String PATH = String.format("%s/%s", BASE, FILENAME);
	
	static PseudoWebHDFSConnection pConn = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		pConn = new PseudoWebHDFSConnection(String.format("http://hdfs-01:%s", WebHDFSConnectionFactory.DEFAULT_PORT), USER, "n/a");
	}

	@Test
	public void getHomeDirectory() throws MalformedURLException, IOException, AuthenticationException {
		String response = pConn.getHomeDirectory();
		unmarshalResponse(response);
	}
	
	@Test
	public void listStatus() throws MalformedURLException, IOException, AuthenticationException {
		String response = pConn.listStatus(BASE);
		unmarshalResponse(response);
	}

	@Test
	public void create() throws MalformedURLException, IOException, AuthenticationException {
		InputStream is = null;

		try {
			is = getClass().getResourceAsStream(String.format("/%s", FILENAME));
			String response = pConn.create(PATH, is);
			unmarshalResponse(response, 201);
		}
		finally {
			Closeables.closeQuietly(is);
		}
	}

	@Test
	public void getContentSummary() throws MalformedURLException, IOException, AuthenticationException {
		String response = pConn.getContentSummary(BASE);
		unmarshalResponse(response);
	}

	@Ignore("Doesn't appear to be supported in CDH4: No enum constant org.apache.hadoop.fs.http.client.HttpFSFileSystem.Operation.CREATESYMLINK")
	public void createSymLink() throws MalformedURLException, IOException, AuthenticationException {
		String response = pConn.createSymLink("/tmp", String.format("%s/temporary", BASE));
		unmarshalResponse(response);
	}
	
	@Test
	public void mkdirs() throws MalformedURLException, IOException, AuthenticationException {
		String response = pConn.mkdirs(String.format("%s/temporary", BASE));
		unmarshalResponse(response);
	}

	//TODO Test Other PseudoWebHDFSConnectionTest's Method
	private String unmarshalResponse(String response) {
		return unmarshalResponse(response, 200);
	}
	
	private String unmarshalResponse(String response, int expectedCode) {
		assertThat(response, is(notNullValue()));

		Map<String, Object> responseMap = ResponseUtil.toMap(response);
		int actualCode = ((Double)responseMap.get(ResponseUtil.CODE)).intValue();

		assertThat(String.format("WebHDFS request failed: %s", responseMap.get(ResponseUtil.MESSAGE)), actualCode, is(equalTo(expectedCode)));

		return (String)responseMap.get(ResponseUtil.CONTENT);
	}
}
