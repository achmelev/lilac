package org.jasm.test.assembler.loader;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.antlr.v4.parse.ATNBuilder.subrule_return;
import org.jasm.resolver.ClassInfoResolver;

public class ByteArrayClassLoader extends ClassLoader {
	
	public String name = null;
	public byte[] data = null;
	private ClassInfoResolver resolver = null;
	
	public ByteArrayClassLoader(ClassLoader parent, String name, byte[] data) {
		super(parent);
		this.name = name;
		this.data = data;
	}
	

	@Override
	protected Class<?> loadClass(String name, boolean resolve)
			throws ClassNotFoundException {
		if (this.name.equals(name)) {
			Class result = defineClass(name,data,0,data.length);
			if (resolve) {
				super.resolveClass(result);
			}
			return result;
		} else {
			String resourceName = name.replace('.', '/')+".class";
			if (resourceName.startsWith("java/")) {
				return super.loadClass(name,resolve);
			} else {
				
				byte [] data = null;
				if (resolver != null) {
					data = resolver.findBytes(resourceName);
				}
				if (data == null) {
					InputStream inp = getParent().getResourceAsStream(resourceName);
					int length = 1024*1024;
					byte[] buf = new byte[length];
					try {
						ByteArrayOutputStream bao = new ByteArrayOutputStream();
						int read = inp.read(buf);
						while (read>0) {
							bao.write(buf, 0, read);
							read = inp.read(buf);
						}
						data = bao.toByteArray();
						
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
				
				if (data !=null) {
					Class result = defineClass(name,data,0,data.length);
					if (resolve) {
						super.resolveClass(result);
					}
					return result;
				} else {
					return super.loadClass(name, resolve);
				}
			}
		}
		
	}


	public void setResolver(ClassInfoResolver resolver) {
		this.resolver = resolver;
	}
	
	
	

}
