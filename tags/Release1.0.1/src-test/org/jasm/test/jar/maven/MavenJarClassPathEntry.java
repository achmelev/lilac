package org.jasm.test.jar.maven;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jasm.resolver.AbstractJarClassPathEntry;
import org.jasm.resolver.ExternalClassInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MavenJarClassPathEntry extends AbstractJarClassPathEntry {
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	private String repositoryURL;
	private String groupId;
	private String artifactId;
	private String version;
	
	
	public MavenJarClassPathEntry(String repositoryURL,String groupId, String artifactId, String version) {
		this.repositoryURL = repositoryURL;
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
	}

	@Override
	public File getJarFile() {
		return getFile();
	}

	@Override
	protected String getName() {
		return this.repositoryURL+"/"+createPath()+"/"+createName();
	}
	
	private String createPath() {
		return groupId.replace('.', '/')+"/"+artifactId+"/"+version;
		
	}
	
	private String createName() {
		return artifactId+"-"+version+".jar";
	}
	
	private File getFile() {
		String path = createPath();
		String name = createName();
		
		File tmpDir = new File(System.getProperty("java.io.tmpdir"));
		
		File pathFile = new File(tmpDir, "mvnrepository/"+path);
		if (!pathFile.exists()) {
			if (!pathFile.mkdirs()) {
				throw new RuntimeException("Couldn't create "+pathFile.getAbsolutePath());
			}
			
		}
		File localFile = new File(pathFile, name);
		if (localFile.exists()) {
			return localFile;
		} else {
			String url = repositoryURL+"/"+path+"/"+name;
			log.info("Downloading "+url+" to "+localFile.getAbsolutePath());
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
	                        FileOutputStream out = new FileOutputStream(localFile);
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
		return localFile;
	}
	
	

}
