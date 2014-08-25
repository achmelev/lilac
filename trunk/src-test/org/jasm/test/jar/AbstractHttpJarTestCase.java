package org.jasm.test.jar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractHttpJarTestCase {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	protected void doJarTest() {
		log.info("Testing "+getURL());
		File f = getFile(getURL());
		try {
			JarFile jar = new JarFile(f);
			Enumeration<JarEntry> entries = jar.entries();
			int errorCounter = 0;
			int counter = 0;
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
					InputStream data = jar.getInputStream(entry);
					try {
						testClass(IOUtils.toByteArray(data));
					} catch (Throwable e) {
						log.error("Error testing: "+entry.getName(),e);
						errorCounter++;
					}
					data.close();
					counter++;
				}
			}
			Assert.assertEquals(0, errorCounter);
			log.info("Successfully tested "+counter+" classes!");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private File getFile(String url) {
		String shortName = url.substring(url.lastIndexOf('/'), url.length());
		final File f = new File(System.getProperty("java.io.tmpdir"), shortName);
		if (f.exists()) {
			return f;
		} else {
			log.info("Downloading "+url+" to "+f.getAbsolutePath());
			try {
				CloseableHttpClient httpclient = HttpClients.createDefault();
				HttpGet httpget = new HttpGet(url);
				ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
	                public String handleResponse(
	                        final HttpResponse response) throws ClientProtocolException, IOException {
	                    int status = response.getStatusLine().getStatusCode();
	                    if (status == 200) {
	                        HttpEntity entity = response.getEntity();
	                        InputStream content = entity.getContent();
	                        FileOutputStream out = new FileOutputStream(f);
	                        IOUtils.copy(content, out);
	                        out.close();
	                    } else {
	                        throw new ClientProtocolException("Unexpected response status: " + status);
	                    }
	                    return null;
	                }

	            };
	            
	            httpclient.execute(httpget, responseHandler);
			} catch (Exception e) {
				throw new RuntimeException("Error downloading "+url,e);
			}
		}
		return f;
	}
	
	
	protected abstract void testClass(byte[] data);
	
	protected abstract String getURL();

}
