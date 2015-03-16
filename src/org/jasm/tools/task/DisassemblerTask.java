package org.jasm.tools.task;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.jasm.bytebuffer.print.PrettyPrinter;
import org.jasm.environment.Environment;
import org.jasm.item.clazz.Clazz;
import org.jasm.tools.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisassemblerTask implements Runnable {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private Resource resource;
	private Properties env;
	private ITaskCallback callback;
	
	private String className = null;
	private String code;
	
	public DisassemblerTask(ITaskCallback callback, Resource resource, Properties env) {
		this.env = env;
		this.callback = callback;
		this.resource = resource;
	}

	@Override
	public void run() {
		InputStream source = null;
		try {
			
			if (env != null) {
				Environment.initFrom(env);
			}
			
			source = this.resource.createInputStream();
			
			byte [] data = IOUtils.toByteArray(source);
			
			ByteArrayByteBuffer bbuf = new ByteArrayByteBuffer(data);
			
			Clazz clazz = new Clazz();
			clazz.read(bbuf, 0L);
			clazz.resolve();
			clazz.updateMetadata();
			className = clazz.getThisClass().getClassName();
			
			StringWriter sw = new StringWriter();
			PrintWriter writer = new PrintWriter(sw);
			PrettyPrinter printer = new PrettyPrinter(writer);
			printer.printItem(clazz);
			writer.close();
			code = sw.toString();
			callback.success(this);
			
		} catch (Throwable e) {
			callback.printError(this, "error disassembling "+resource.getName()+" : "+e.getClass().getName()+"-->"+e.getMessage());
			log.error("Error disassembling "+resource.getName(),e);
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

	public String getClassName() {
		return className;
	}

	public String getCode() {
		return code;
	}
	
	

}
