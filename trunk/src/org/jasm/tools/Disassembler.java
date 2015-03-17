package org.jasm.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jasm.environment.Environment;
import org.jasm.tools.print.ConsolePrinter;
import org.jasm.tools.print.IPrinter;
import org.jasm.tools.resource.CompositeResourceCollection;
import org.jasm.tools.resource.DirResourceCollection;
import org.jasm.tools.resource.FileResource;
import org.jasm.tools.resource.FilterResourceCollection;
import org.jasm.tools.resource.JarResourceCollection;
import org.jasm.tools.resource.ListResourceCollection;
import org.jasm.tools.resource.Resource;
import org.jasm.tools.resource.ResourceCollection;
import org.jasm.tools.resource.ResourceFilter;
import org.jasm.tools.resource.ZipResourceCollection;
import org.jasm.tools.task.DisassemblerTask;
import org.jasm.tools.task.ITaskCallback;
import org.jasm.tools.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Disassembler extends AbstractTool implements ITaskCallback{
	
	
	private ResourceCollection inputs;
	private File output;
	
	private int successCounter = 0;
	
	public Disassembler(IPrinter printer, String[] args) {
		super(printer, args);
	}
		
	private void disassemble() {
		
		long t1 = System.currentTimeMillis();
		
		ResourceCollection col = inputs;
		Enumeration<Resource> resources = col.elements();
		
		boolean useThreadPool = Environment.getBooleanValue("jdasm.usethreadpool");
		
		if (useThreadPool) {
			ExecutorService pool = Executors.newFixedThreadPool(Environment.getIntValue("jdasm.threadpoolsize"));
			while (resources.hasMoreElements()) {
				Resource resource = resources.nextElement();
				pool.execute(new DisassemblerTask(this, resource, Environment.getContent()));
			}
			pool.shutdown();
			try {
				pool.awaitTermination(2, TimeUnit.DAYS);
			} catch (InterruptedException e) {
				//ignore
			}
		} else {
			while (resources.hasMoreElements()) {
				Resource resource = resources.nextElement();
				new DisassemblerTask(this, resource, null).run();
			}
		}
		
		printer.printInfo("Disassembled "+successCounter+" class files in "+(System.currentTimeMillis()-t1)/1000+" secs");
	}
	
	

	@Override
	public void failure(Task source) {
		
	}

	@Override
	public synchronized void success(Task source) {
		DisassemblerTask task = (DisassemblerTask)source;
		File target = new File(output,task.getClassName()+".jasm");
		File parent = target.getParentFile();
		if (!parent.exists()) {
			if (!parent.mkdirs()) {
				printer.printError("couldn't create "+parent.getAbsolutePath());
			}
		}
		if (parent.exists()) {
			try {
				PrintWriter pw = new PrintWriter(target);
				pw.print(task.getCode());
				pw.close();
				successCounter++;
			} catch (FileNotFoundException e) {
				printer.printError("couldn't write "+target.getAbsolutePath());
			}
			
		}
	}
	
	@Override
	protected List<Option> createSpecificOptions() {
		List<Option> result = new ArrayList<Option>();
		
		Option input = OptionBuilder.
						hasArgs().
						isRequired(true).
						withArgName("input").
						withDescription("inputs (jars,zips, class files or directories)").
						create("input");
		result.add(input);
		
		Option output = OptionBuilder.
				hasArgs(1).
				isRequired(true).
				withArgName("output").
				withDescription("output directory").
				create("output");
		result.add(output);
		
		return result;
	}

	@Override
	protected boolean readOptions(CommandLine line) {
		inputs = createInputCollecton("input");
		inputs = new FilterResourceCollection(inputs, new ResourceFilter() {
			
			@Override
			public boolean accept(Resource res) {
				return res.getName().endsWith(".class");
			}
		});
		output = createOutputDirectory("output");
		return output != null;
	}

	@Override
	protected String getScriptName() {
		return "jdasm";
	}

	@Override
	protected String getConfPrefix() {
		return "jdasm";
	}

	@Override
	protected boolean prepare() {
		return true;
	}

	@Override
	protected int getNumberOfWorkUnits() {
		return 1;
	}

	@Override
	protected boolean doWorkUnit(int number) {
		disassemble();
		return true;
	}


	@Override
	protected boolean acceptInput(File f) {
		return (f.isFile() && (f.getName().endsWith(".class") 
			   || f.getName().endsWith(".jar")
			   || f.getName().endsWith(".zip"))) || f.isDirectory();
	}
	
	public static void main(String [] args) {
		new Disassembler(new ConsolePrinter(), args).run();
		System.exit(0);

	}

}
