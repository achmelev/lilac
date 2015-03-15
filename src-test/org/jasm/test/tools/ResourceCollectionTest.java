package org.jasm.test.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.jasm.tools.resource.CompositeResourceCollection;
import org.jasm.tools.resource.DirResourceCollection;
import org.jasm.tools.resource.FilterResourceCollection;
import org.jasm.tools.resource.JarResourceCollection;
import org.jasm.tools.resource.Resource;
import org.jasm.tools.resource.ResourceCollection;
import org.jasm.tools.resource.ResourceFilter;
import org.jasm.tools.resource.ZipResourceCollection;
import org.junit.Test;

public class ResourceCollectionTest {
	
	Random r = new Random(System.currentTimeMillis());
	
	@Test
	public void testDir() {
		
		File root = createTestDirectory();
		List<Resource> result = new ArrayList<Resource>();
		DirResourceCollection col = new DirResourceCollection(root);
		Enumeration<Resource> elems = col.elements();
		while (elems.hasMoreElements()) {
			Resource elem = elems.nextElement();
			result.add(elem);
		}
		
		Assert.assertEquals(12, result.size());
		
		FileUtils.deleteQuietly(root);
		
		
	}
	
	@Test
	public void testJar() {
		
		File jarFile = createJarFile();
		JarFile root;
		try {
			root = new JarFile(jarFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		List<Resource> result = new ArrayList<Resource>();
		JarResourceCollection col = new JarResourceCollection(root);
		Enumeration<Resource> elems = col.elements();
		while (elems.hasMoreElements()) {
			Resource elem = elems.nextElement();
			result.add(elem);
		}
		
		Assert.assertEquals(12, result.size());
		
		FileUtils.deleteQuietly(jarFile);
		
		
	}
	
	@Test
	public void testCompositeAndFilter() {
		
		File jarFile = createJarFile();
		JarFile root;
		try {
			root = new JarFile(jarFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		JarResourceCollection jarCol = new JarResourceCollection(root);
		
		File zipFile = createZipFile();
		ZipFile zipRoot;
		try {
			zipRoot = new ZipFile(zipFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		ZipResourceCollection zipCol = new ZipResourceCollection(zipRoot);
		
		File dirRoot = createTestDirectory();
		
		DirResourceCollection dirCol = new DirResourceCollection(dirRoot);
		
		
		
		CompositeResourceCollection ccol = new CompositeResourceCollection();
		ccol.add(jarCol);
		ccol.add(dirCol);
		ccol.add(zipCol);
		
		ResourceCollection  col = ccol;
		
		List<Resource> result = new ArrayList<Resource>();
		Enumeration<Resource> elems = col.elements();
		while (elems.hasMoreElements()) {
			Resource elem = elems.nextElement();
			result.add(elem);
		}
		
		Assert.assertEquals(36, result.size());
		
		col = new FilterResourceCollection(col, new ResourceFilter() {
			
			@Override
			public boolean accept(Resource res) {
				return res.getName().endsWith(".txt");
			}
		});
		
		result = new ArrayList<Resource>();
		elems = col.elements();
		while (elems.hasMoreElements()) {
			Resource elem = elems.nextElement();
			result.add(elem);
		}
		
		Assert.assertEquals(6, result.size());
		
		
		FileUtils.deleteQuietly(jarFile);
		FileUtils.deleteQuietly(zipFile);
		FileUtils.deleteQuietly(dirRoot);
		
		
	}
	
	@Test
	public void testZip() {
		
		File jarFile = createZipFile();
		ZipFile root;
		try {
			root = new ZipFile(jarFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		List<Resource> result = new ArrayList<Resource>();
		ZipResourceCollection col = new ZipResourceCollection(root);
		Enumeration<Resource> elems = col.elements();
		while (elems.hasMoreElements()) {
			Resource elem = elems.nextElement();
			result.add(elem);
		}
		
		Assert.assertEquals(12, result.size());
		
		FileUtils.deleteQuietly(jarFile);
		
		
	}
	
	private File createTestDirectory() {
		File testDir = new File(FileUtils.getTempDirectory(),"testdirresource"+Math.abs(r.nextInt())+System.currentTimeMillis());
		if (!testDir.mkdir()) {
			throw new RuntimeException("couldn't create dir: "+testDir.getAbsolutePath());
		}
		
		createDir(testDir,"dir1");
		createDir(testDir,"dir2");
		createFile(testDir, "dir2/file1.txt");
		createFile(testDir, "dir2/file2.txt");
		createDir(testDir,"dir3");
		createFile(testDir, "dir3/file3.bin");
		createDir(testDir,"dir3/dir31");
		createFile(testDir, "dir3/dir31/file4.bin");
		createFile(testDir, "dir3/file5.bin");
		createDir(testDir,"dir3/dir33");
		createDir(testDir,"dir4");
		createDir(testDir,"dir4/dir41");
		createFile(testDir, "dir4//dir41/file6.bin");
		createFile(testDir, "dir4//dir41/file7.bin");
		createFile(testDir, "dir4//dir41/file8.bin");
		createFile(testDir, "dir4//dir41/file9.bin");
		createFile(testDir, "dir4/file10.bin");
		createDir(testDir,"dir4/dir42/dir411");
		createDir(testDir,"dir5");
		createFile(testDir, "file11.bin");
		createFile(testDir, "file12.bin");
		
		return testDir;
	}
	
	private File createJarFile() {
		File testJar = new File(FileUtils.getTempDirectory(),"test"+Math.abs(r.nextInt())+System.currentTimeMillis()+".jar");
		
		
		try {
			FileOutputStream fOut = new FileOutputStream(testJar);
			
	        Manifest manifest = new Manifest();
	        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
	        JarOutputStream testDir = new JarOutputStream(fOut,manifest);;
	        
			createJarEntryDir(testDir,"dir1");
			createJarEntryDir(testDir,"dir2");
			createJarEntryFile(testDir, "dir2/file1.txt");
			createJarEntryFile(testDir, "dir2/file2.txt");
			createJarEntryDir(testDir,"dir3");
			createJarEntryFile(testDir, "dir3/file3.bin");
			createJarEntryDir(testDir,"dir3/dir31");
			createJarEntryFile(testDir, "dir3/dir31/file4.bin");
			createJarEntryFile(testDir, "dir3/file5.bin");
			createJarEntryDir(testDir,"dir3/dir33");
			createJarEntryDir(testDir,"dir4");
			createJarEntryDir(testDir,"dir4/dir41");
			createJarEntryFile(testDir, "dir4//dir41/file6.bin");
			createJarEntryFile(testDir, "dir4//dir41/file7.bin");
			createJarEntryFile(testDir, "dir4//dir41/file8.bin");
			createJarEntryFile(testDir, "dir4//dir41/file9.bin");
			createJarEntryFile(testDir, "dir4/file10.bin");
			createJarEntryDir(testDir,"dir4/dir42/dir411");
			createJarEntryDir(testDir,"dir5");
			createJarEntryFile(testDir, "file11.bin");
			createJarEntryFile(testDir, "file12.bin");
			
			testDir.close();
			
			return testJar;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		
	}
	
	private File createZipFile() {
		File testJar = new File(FileUtils.getTempDirectory(),"test"+Math.abs(r.nextInt())+System.currentTimeMillis()+".jar");
		
		
		try {
			FileOutputStream fOut = new FileOutputStream(testJar);

	        ZipOutputStream testDir = new ZipOutputStream(fOut);;
	        
			createZipEntryDir(testDir,"dir1");
			createZipEntryDir(testDir,"dir2");
			createZipEntryFile(testDir, "dir2/file1.txt");
			createZipEntryFile(testDir, "dir2/file2.txt");
			createZipEntryDir(testDir,"dir3");
			createZipEntryFile(testDir, "dir3/file3.bin");
			createZipEntryDir(testDir,"dir3/dir31");
			createZipEntryFile(testDir, "dir3/dir31/file4.bin");
			createZipEntryFile(testDir, "dir3/file5.bin");
			createZipEntryDir(testDir,"dir3/dir33");
			createZipEntryDir(testDir,"dir4");
			createZipEntryDir(testDir,"dir4/dir41");
			createZipEntryFile(testDir, "dir4//dir41/file6.bin");
			createZipEntryFile(testDir, "dir4//dir41/file7.bin");
			createZipEntryFile(testDir, "dir4//dir41/file8.bin");
			createZipEntryFile(testDir, "dir4//dir41/file9.bin");
			createZipEntryFile(testDir, "dir4/file10.bin");
			createZipEntryDir(testDir,"dir4/dir42/dir411");
			createZipEntryDir(testDir,"dir5");
			createZipEntryFile(testDir, "file11.bin");
			createZipEntryFile(testDir, "file12.bin");
			
			testDir.close();
			
			return testJar;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		
	}
	
	private void createDir(File rootDir, String path) {
		File testDir = new File(rootDir, path);
		if (!testDir.mkdirs()) {
			throw new RuntimeException("couldn't create dir: "+testDir.getAbsolutePath());
		}
	}
	
	private void createFile(File rootDir, String path) {
		File testFile = new File(rootDir, path);
		try {
			FileUtils.writeByteArrayToFile(testFile, new byte[]{(byte)0});
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void createJarEntryFile(JarOutputStream jarOut, String path) {
		try {
			jarOut.putNextEntry(new ZipEntry(path));
			jarOut.write(new byte[]{(byte)0});
			jarOut.closeEntry();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void createJarEntryDir(JarOutputStream jarOut, String path) {
		try {
			jarOut.putNextEntry(new ZipEntry(path+"/"));
			jarOut.closeEntry();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void createZipEntryFile(ZipOutputStream jarOut, String path) {
		try {
			jarOut.putNextEntry(new ZipEntry(path));
			jarOut.write(new byte[]{(byte)0});
			jarOut.closeEntry();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void createZipEntryDir(ZipOutputStream jarOut, String path) {
		try {
			jarOut.putNextEntry(new ZipEntry(path+"/"));
			jarOut.closeEntry();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
