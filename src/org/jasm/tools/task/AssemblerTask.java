package org.jasm.tools.task;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.jasm.environment.Environment;
import org.jasm.item.clazz.Clazz;
import org.jasm.loader.AssemblerClassLoaderException;
import org.jasm.parser.AssemblerParser;
import org.jasm.parser.IParserErrorListener;
import org.jasm.parser.SimpleParserErrorListener;
import org.jasm.resolver.ClassInfoResolver;
import org.jasm.resolver.ClassLoaderClasspathEntry;
import org.jasm.resolver.ClazzClassPathEntry;
import org.jasm.tools.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssemblerTask implements Task, IParserErrorListener {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private Resource resource;
	private Properties env;
	private ITaskCallback callback;
	
	private Clazz clazz= null;
	
	private int stage;

	
	private AssemblerParser parser;
	
	public AssemblerTask(ITaskCallback callback, Resource resource, Properties env) {
		this.env = env;
		this.callback = callback;
		this.resource = resource;
	}

	@Override
	public void run() {
		if (stage == 0) {
			doAssemble();
		} else {
			doVerify();
		}
		
	}
	
	private void doAssemble() {
		InputStream source = null;
		try {
			
			if (env != null) {
				Environment.initFrom(env);
			}
			
			source = this.resource.createInputStream();
			
			parser = new AssemblerParser();
			parser.addErrorListener(this);
			Clazz clazz =  parser.parse(this.resource.createInputStream());
			if (parser.getErrorCounter() > 0) {
				parser.flushErrors();
				callback.failure(this);
			} else {
				this.clazz = clazz;
				callback.success(this);
			}
			
			
		} catch (Throwable e) {
			if (e instanceof OutOfMemoryError) {
				throw e;
			}
			callback.printError(this, "internal error while assembling "+resource.getName()+" : "+e.getClass().getName()+"-->"+e.getMessage());
			log.error("Error assembling "+resource.getName(),e);
			callback.failure(this);
		} finally {
			try {
				if (source != null) {
					source.close();
				}
			} catch (IOException e) {
				//ignore
			}
			
		}
	}
	
	private void doVerify() {
		try {		
			if (clazz.getResolver() == null) {
				throw new IllegalStateException("RESOLVER NOT SET");
			}
			
			clazz.verify();
			if (parser.getErrorCounter()>0) {
				parser.flushErrors();
				callback.failure(this);
			} else {
				callback.success(this);
			}
		} catch (Throwable e) {
			callback.printError(this, "internal error while verifying "+resource.getName()+" : "+e.getClass().getName()+"-->"+e.getMessage());
			log.error("Error assembling "+resource.getName(),e);
			callback.failure(this);
		}
	}



	public Resource getResource() {
		return resource;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	@Override
	public void clear() {
		
	}

	@Override
	public void error(int line, int charPos, String msg) {
		callback.printError(this, "line "+line+" " + msg);
	}

	@Override
	public void flush() {
		
		
	}

	public Clazz getClazz() {
		return clazz;
	}
	
	

}
