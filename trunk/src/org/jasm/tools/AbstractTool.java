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
import org.jasm.resolver.DirClasspathEntry;
import org.jasm.resolver.IClassPathEntry;
import org.jasm.resolver.JarFileClassPathEntry;
import org.jasm.resolver.ZipFileClassPathEntry;
import org.jasm.tools.print.ConsolePrinter;
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
	        
	        File workDir = getWorkDir();
        	File confFile = new File(workDir,"conf/jasm.conf");
        	if (!confFile.exists()) {
        		printer.printWarning(confFile.getAbsolutePath()+" not found!");
        		initEnvironment(null);
        	} else {
        		initEnvironment(confFile);
        	}
	        
	        if (readOptions(line)) {
	        	initLog4J(line);
	        	
	        	
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
	    	if (log != null) {
	    		log.error("internal error", e);
	    	} else {
	    		e.printStackTrace();
	    	}
	    	printer.printError("internal error: "+e.getClass().getName()+":"+e.getMessage());
	    } 
		
	}
	
	private void initLog4J(CommandLine options) {
		
		Properties log4jProps = new Properties();
		
		boolean logEnabled = options.hasOption("enablelogging");
		String logLevel="warn";
		File logFile = null;
		if (logEnabled) {
			if (options.hasOption("loglevel")) {
				String value = options.getOptionValue("loglevel");
				if (value.toLowerCase().equals("error") || 
					value.toLowerCase().equals("warn") ||
					value.toLowerCase().equals("info") ||
					value.toLowerCase().equals("debug")) {
					
					logLevel = value.toLowerCase();
				} else {
					printer.printWarning("unknown loglevel "+value+", using 'warn'");
				}
			}
			
			if (options.hasOption("logfile")) {
				File logFile2 = new File(options.getOptionValue("logfile"));
				if (logFile2.exists() && logFile2.isDirectory()) {
					printer.printWarning(logFile.getAbsolutePath()+" is a directory. Using console instead.");
				} else {
					logFile = logFile2;
				}
			}
			
		}
		
		log4jProps.put("log4j.rootLogger", "fatal,out");
		log4jProps.put("log4j.logger.org.jasm", logLevel);
		if (logEnabled) {
			if (logFile == null) {
				log4jProps.put("log4j.appender.out", "org.apache.log4j.ConsoleAppender");
			} else {
				log4jProps.put("log4j.appender.out", "org.apache.log4j.FileAppender");
				log4jProps.put("log4j.appender.out.File", logFile.getAbsolutePath());
			}
			log4jProps.put("log4j.appender.out.layout","org.apache.log4j.PatternLayout");
			log4jProps.put("log4j.appender.out.layout.ConversionPattern","%d{yyyy-MM-dd HH:mm:ss} %-5p %C - %m%n");
		} else {
			log4jProps.put("log4j.appender.out", "org.apache.log4j.varia.NullAppender");
		}
		
		PropertyConfigurator.configure(log4jProps);
		
		log = LoggerFactory.getLogger(this.getClass());
		log.info("Logging configured");
	}
	
	private void initEnvironment(File f) {
		Properties props = new Properties();
		if (f != null) {
			try {
				props.load(new FileInputStream(f));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
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
			if (input.exists() && acceptInput(input)) {
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
					addZipsAndJarsFromDir(input, all);
				}
			} else {
				printer.printWarning(input.getAbsolutePath()+" doesn't exist");
			}
		} 
		
		return all;
	}
	
	private void addZipsAndJarsFromDir(File rootDir, CompositeResourceCollection all) {
		File [] children = rootDir.listFiles();
		for (File input: children) {
			if (input.isDirectory()) {
				addZipsAndJarsFromDir(input, all);
			} if (input.getName().endsWith(".jar")) {
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
			}
				
		}
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
	
	protected List<IClassPathEntry> createClassPath(String key) {
		List<IClassPathEntry> result = new ArrayList<IClassPathEntry>();
		String[] paths = line.getOptionValues(key);
		for (String path: paths) {
			File input = new File(path);
			if (!input.exists()) {
				printer.printWarning(input.getAbsolutePath()+" doesn't exist");
			} else if (input.isFile() && input.getName().endsWith(".jar")) {
				result.add(new JarFileClassPathEntry(input));
			} else if (input.isFile() && input.getName().endsWith(".zip")) {
				result.add(new ZipFileClassPathEntry(input));
			} else if (input.isDirectory()) {
				appendDirectoryToClassPath(input, true, result);
			}
		}
		
		return result;
	}
	
	protected File getRuntimeJar() {
		String resourceName = Object.class.getName().replace('.', '/')+".class";
		URL url = getClass().getClassLoader().getResource(resourceName);
		String path = url.getPath();
		path = path.substring(0,path.length()-resourceName.length());
		if (path.indexOf(".jar!")>0) {
			path = path.substring(0, path.indexOf(".jar!")+".jar".length());
		} else {
			printer.printWarning("java runtime jar not found");
			return null;
		}
		
		if (path.startsWith("file:")) {
			path = path.substring("file:".length(), path.length());
		}
		
		File result = new File(path);
		if (result.exists() && result.isFile()) {
			return result;
		} else {
			printer.printWarning("java runtime jar not found");
			return null;
		}
	}
	
	private void appendDirectoryToClassPath(File dir, boolean root, List<IClassPathEntry> result) {
		if (root) {
			result.add(new DirClasspathEntry(dir));
		}
		
		File [] children = dir.listFiles();
		for (File input: children) {
			if (input.isFile() && input.getName().endsWith(".jar")) {
				result.add(new JarFileClassPathEntry(input));
			} else if (input.isFile() && input.getName().endsWith(".zip")) {
				result.add(new ZipFileClassPathEntry(input));
			} else if (input.isFile() && input.getName().endsWith(".jar")) {
				result.add(new JarFileClassPathEntry(input));
			} else if (input.isDirectory()) {
				appendDirectoryToClassPath(input, false, result);
			}
		}
	}
	
	private File getWorkDir() {
		String resourceName = this.getClass().getName().replace('.', '/')+".class";
		URL url = getClass().getClassLoader().getResource(resourceName);
		String path = url.getPath();
		path = path.substring(0,path.length()-resourceName.length());
		if (path.endsWith("/bin/")) {
			path = path.substring(0,path.length()-"/bin/".length());
		} else if (path.indexOf(".jar!")>0) {
			path = path.substring(0, path.indexOf(".jar!"));
			path = path.substring(0,path.lastIndexOf('/'));
		} else {
			throw new IllegalStateException();
		}
		
		if (path.startsWith("file:")) {
			path = path.substring("file:".length(), path.length());
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
		
		Option enableLogging = OptionBuilder.
								isRequired(false).
								withDescription("enables logging").
								create("enablelogging");
		result.addOption(enableLogging);
		
		Option logLevel = OptionBuilder.
				isRequired(false).
				hasArg().withArgName("log lebel").
				withDescription("log level (error|warn|info|debug)").
				create("loglevel");
		result.addOption(logLevel);
		
		Option logFile = OptionBuilder.
				isRequired(false).
				hasArg().withArgName("log file name").
				withDescription("the file to log into").
				create("logfile");
		result.addOption(logFile);
		
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
	
	protected abstract boolean acceptInput(File f);
	
	protected static int runTool(AbstractTool tool) {
		try {
			tool.run();
			if (tool.printer.getErrorCounter()>0) {
				return 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
		return 0;
	}

}
