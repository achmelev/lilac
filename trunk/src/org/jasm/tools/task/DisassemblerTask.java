package org.jasm.tools.task;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.jasm.bytebuffer.print.PrettyPrinter;
import org.jasm.item.clazz.Clazz;
import org.jasm.item.environment.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisassemblerTask implements Runnable {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private InputStream source;
	private String sourceName;
	private PrintWriter target;
	private Properties env;
	private ITaskCallback callback;
	
	private String className = null;
	private String code;
	
	public DisassemblerTask(ITaskCallback callback, String sourceName, InputStream source, Properties env) {
		this.source = source;
		this.env = env;
		this.callback = callback;
		this.sourceName = sourceName;
		
		
	}

	@Override
	public void run() {
		try {
			
			if (env != null) {
				Environment.initFrom(env);
			}
			
			byte [] data = IOUtils.toByteArray(this.source);
			
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
			source.close();
			
			
		} catch (Throwable e) {
			callback.printError(this, "error disassembling "+sourceName+" : "+e.getClass().getName()+"-->"+e.getMessage());
			log.error("Error disassembling "+sourceName,e);
			callback.failure(this);
		} finally {
			try {
				source.close();
			} catch (IOException e) {
				//ignore
			}
		}
		
	}

}
