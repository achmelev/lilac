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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Disassembler implements ITaskCallback, Runnable {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private List<File> inputs;
	private File output;
	
	public Disassembler(List<File> inputs, File output) {
		this.inputs = inputs;
		this.output = output;
	}
	
	@Override
	public void run() {
		
		if (output.exists() && output.isFile()) {
			printMessage("Error: "+output.getAbsolutePath()+" doesn't exist or isn't a directory");
			return;
		}
		
		if (!output.exists()) {
			if (!output.mkdirs()) {
				printMessage("Error: couldn't create "+output.getAbsolutePath());
			}
		}
		
		ListResourceCollection simpleFiles = new ListResourceCollection();
		CompositeResourceCollection all = new CompositeResourceCollection();
		all.add(simpleFiles);
		for (File input: inputs) {
			if (input.exists()) {
				if (input.isFile()) {
					if (input.getName().endsWith(".jar")) {
						try {
							all.add(new JarResourceCollection(new JarFile(input)));
						} catch (IOException e) {
							printMessage("Warning: couldn't read jar file "+input.getAbsolutePath());
						}
					} else if (input.getName().endsWith(".zip")) {
						try {
							all.add(new ZipResourceCollection(new ZipFile(input)));
						} catch (IOException e) {
							printMessage("Warning: couldn't read jar file "+input.getAbsolutePath());
						}
					} else if (input.getName().endsWith(".class")) {
						simpleFiles.add(new FileResource(input));
					}
				} else {
					all.add(new DirResourceCollection(input));
				}
			} else {
				printMessage("Warning: "+input.getAbsolutePath()+" doesn't exist");
			}
		} 
		
		FilterResourceCollection allClasses = new FilterResourceCollection(all, new ResourceFilter() {
			
			@Override
			public boolean accept(Resource res) {
				return res.getName().endsWith(".class");
			}
		});
		
		disassemble(allClasses);
		
	}
	
	private void disassemble(ResourceCollection col) {
		Enumeration<Resource> resources = col.elements();
		ExecutorService pool = Executors.newFixedThreadPool(20);
		while (resources.hasMoreElements()) {
			Resource resource = resources.nextElement();
			pool.execute(new DisassemblerTask(this, resource, null));
		}
		pool.shutdown();
		try {
			pool.awaitTermination(2, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			//ignore
		}
	}
	
	@Override
	public void printError(Runnable source, String message) {
		System.out.println(message);
	}

	@Override
	public void failure(Runnable source) {
		
	}

	@Override
	public synchronized void success(Runnable source) {
		DisassemblerTask task = (DisassemblerTask)source;
		File target = new File(output,task.getClassName()+".jasm");
		File parent = target.getParentFile();
		if (!parent.exists()) {
			if (!parent.mkdirs()) {
				printMessage("Error: couldn't create "+parent.getAbsolutePath());
			}
		}
		if (parent.exists()) {
			try {
				PrintWriter pw = new PrintWriter(target);
				pw.print(task.getCode());
				pw.close();
			} catch (FileNotFoundException e) {
				printMessage("Error: couldn't write "+target.getAbsolutePath());
			}
			
		}
	}
	
	private void printMessage(String message) {
		System.out.println(message);
	}
	
	
	
	public static void main(String [] args) {
		CommandLineParser parser = new GnuParser();
	    try {
	        // parse the command line arguments
	        CommandLine line = parser.parse( createOptions(), args );
	        
	        String[] inputStrs = line.getOptionValues("input");
	        List<File> inputFiles = new ArrayList<File>();
	        for (String name: inputStrs) {
	        	inputFiles.add(new File(name));
	        }
	        File output = new File(line.getOptionValue("output"));
	        
	        Disassembler da = new Disassembler(inputFiles, output);
	        da.run();
	    	
	    }
	    catch(ParseException exp ) {
	        System.out.println( "command line parsing failed.  Reason: " + exp.getMessage() );
	        usage();
	    } catch (Throwable e) {
	    	e.printStackTrace();
	    	System.exit(1);
	    } finally {
	    	System.exit(0);
	    }

	}
	
	private static Options createOptions() {
		Options result = new Options();
		
		Option input = OptionBuilder.
						hasArgs().
						isRequired(true).
						withArgName("input").
						withDescription("inputs (jars,zips, class files or directories)").
						create("input");
		result.addOption(input);
		
		Option output = OptionBuilder.
				hasArgs(1).
				isRequired(true).
				withArgName("output").
				withDescription("output directory").
				create("output");
		result.addOption(output);
		
		return result;
	}
	
	private static void usage() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "disassembler", createOptions() );
	}

	@Override
	public void printWarning(Runnable source, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printInfo(Runnable source, String message) {
		// TODO Auto-generated method stub
		
	}

	

	




	
	

}
