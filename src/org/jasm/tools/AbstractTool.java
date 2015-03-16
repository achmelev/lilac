package org.jasm.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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
import org.apache.log4j.PropertyConfigurator;
import org.jasm.environment.Environment;
import org.jasm.tools.print.IPrinter;
import org.jasm.tools.resource.CompositeResourceCollection;
import org.jasm.tools.resource.DirResourceCollection;
import org.jasm.tools.resource.FileResource;
import org.jasm.tools.resource.JarResourceCollection;
import org.jasm.tools.resource.ListResourceCollection;
import org.jasm.tools.resource.ResourceCollection;
import org.jasm.tools.resource.ZipResourceCollection;
import org.jasm.tools.task.ITaskCallback;
import org.jasm.tools.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractTool implements Runnable, ITaskCallback{
	
	protected Logger log;
	
	private String[] args;
	protected IPrinter printer;
	
	private Options options;
	
	CommandLine line;
		
	public AbstractTool(IPrinter printer, String[] args) {
		this.printer = printer;
		this.args = args;
	}

	@Override
	public void run() {
		CommandLineParser parser = new GnuParser();
	    try {
	    	options = createOptions();
	        line = parser.parse( options, args );
	        
	        printer.printInfo("starting "+getScriptName()+"...");
	        
	        if (readOptions(line)) {
	        	File workDir = getWorkDir();
	        	File log4jFile = new File(workDir,"conf/log4j.properties");
	        	if (!log4jFile.exists()) {
	        		printer.printError(log4jFile.getAbsolutePath()+" not found!");
	        		return;
	        	} else {
	        		initLog4J(log4jFile);
	        	}
	        	File confFile = new File(workDir,"conf/jasm.conf");
	        	if (!confFile.exists()) {
	        		printer.printError(log4jFile.getAbsolutePath()+" not found!");
	        		return;
	        	} else {
	        		initEnvironment(confFile);
	        	}
	        	
	        	if (prepare()) {
	        		int numberOfWorkUnits = getNumberOfWorkUnits();
	        		for (int i=0;i<numberOfWorkUnits; i++) {
	        			if (!doWorkUnit(i)) {
	        				break;
	        			}
	        		}
	        	}
	        }
	    	
	    }
	    catch(ParseException exp ) {
	        printer.printError( "command line parsing failed.  Reason: " + exp.getMessage() );
	        usage();
	    } catch (Throwable e) {
	    	log.error("internal error", e);
	    	printer.printError("internal error: "+e.getClass().getName()+":"+e.getMessage());
	    } 
		
	}
	
	private void initLog4J(File f) {
		PropertyConfigurator.configure(f.getAbsolutePath());
		log = LoggerFactory.getLogger(this.getClass());
		log.info("Logging configured");
	}
	
	private void initEnvironment(File f) {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(f));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		List<String> knownKeys = Environment.getVariableNames();
		
		Properties comlProps = line.getOptionProperties("D");
		for (Object okey: comlProps.keySet()) {
			String key = okey.toString().trim();
			String compositeKeys = getConfPrefix()+"."+key;
			props.put(compositeKeys, comlProps.getProperty(key));
		}
		
		for (Object okey: props.keySet()) {
			String key = okey.toString().trim();
			if (!knownKeys.contains(key)) {
				printer.printWarning("unknown konfiguration key "+key);
			}
		}
		
		Environment.initFrom(props);
	}
	
	protected ResourceCollection createInputCollecton(String key) {
		String[] paths = line.getOptionValues(key);
		ListResourceCollection simpleFiles = new ListResourceCollection();
		CompositeResourceCollection all = new CompositeResourceCollection();
		all.add(simpleFiles);
		for (String inputName: paths) {
			File input = new File(inputName);
			if (input.exists()) {
				if (input.isFile()) {
					if (input.getName().endsWith(".jar")) {
						try {
							all.add(new JarResourceCollection(new JarFile(input)));
						} catch (IOException e) {
							printer.printWarning("couldn't read jar file "+input.getAbsolutePath());
						}
					} else if (input.getName().endsWith(".zip")) {
						try {
							all.add(new ZipResourceCollection(new ZipFile(input)));
						} catch (IOException e) {
							printer.printWarning("couldn't read zip file "+input.getAbsolutePath());
						}
					} else {
						simpleFiles.add(new FileResource(input));
					}
				} else {
					all.add(new DirResourceCollection(input));
				}
			} else {
				printer.printWarning(input.getAbsolutePath()+" doesn't exist");
			}
		} 
		
		return all;
	}
	
	protected File createOutputDirectory(String key) {
		String path = line.getOptionValue(key);
		File output = new File(path);
		if (output.exists() && output.isFile()) {
			printer.printError(output.getAbsolutePath()+" doesn't exist or isn't a directory");
			return null;
		}
		
		if (!output.exists()) {
			if (!output.mkdirs()) {
				printer.printError("couldn't create "+output.getAbsolutePath());
			}
		}
		
		return output;
	}
	
	
	private File getWorkDir() {
		String resourceName = this.getClass().getName().replace('.', '/')+".class";
		URL url = getClass().getClassLoader().getResource(resourceName);
		String path = url.getPath();
		System.err.println(path);
		path = path.substring(0,path.length()-resourceName.length());
		if (path.endsWith("/bin/")) {
			path = path.substring(0,path.length()-"/bin/".length());
		} else if (path.indexOf(".jar!")>0) {
			path = path.substring(0, path.indexOf(".jar!"));
			path = path.substring(0,path.lastIndexOf('/'));
		} else {
			throw new IllegalStateException();
		}
		
		return new File(path);
	}
	

	private Options createOptions() {
		
		Options result = new Options();
		
		Option props = OptionBuilder.
				isRequired(false).
				hasArgs(2).withValueSeparator('=').
				withArgName("property=value").
				withDescription("configuration values").
				create("D");
		
		result.addOption(props);
		
		List<Option> specific = createSpecificOptions();
		
		for (Option opt: specific) {
			result.addOption(opt);
		}
		
		
		
		return result;
	}
	

	private void usage() {
		HelpFormatter formatter = new HelpFormatter();
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		formatter.printHelp(pw, HelpFormatter.DEFAULT_WIDTH, getScriptName(), null, options, HelpFormatter.DEFAULT_LEFT_PAD, HelpFormatter.DEFAULT_DESC_PAD, null, false);
        pw.flush();	
		printer.printInfo(sw.toString());
	}
	
	@Override
	public void printError(Task source, String message) {
		printer.printError(source.getResource().getName()+" : "+message);
	}
	
	@Override
	public void printInfo(Task source, String message) {
		printer.printInfo(source.getResource().getName()+" : "+message);
	}
	
	@Override
	public void printWarning(Task source, String message) {
		printer.printWarning(source.getResource().getName()+" : "+message);
	}
	
	protected abstract List<Option> createSpecificOptions();
	protected abstract boolean readOptions(CommandLine line);
	protected abstract String getScriptName();
	protected abstract String getConfPrefix();
	
	protected abstract boolean prepare();
	protected abstract int getNumberOfWorkUnits();
	protected abstract boolean doWorkUnit(int number);

}
