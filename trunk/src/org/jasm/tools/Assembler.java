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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.jasm.bytebuffer.ByteArrayByteBuffer;
import org.jasm.environment.Environment;
import org.jasm.resolver.ClassInfoResolver;
import org.jasm.resolver.ClassLoaderClasspathEntry;
import org.jasm.resolver.ClazzClassPathEntry;
import org.jasm.resolver.IClassPathEntry;
import org.jasm.resolver.JarFileClassPathEntry;
import org.jasm.tools.print.ConsolePrinter;
import org.jasm.tools.print.IPrinter;
import org.jasm.tools.resource.FilterResourceCollection;
import org.jasm.tools.resource.Resource;
import org.jasm.tools.resource.ResourceCollection;
import org.jasm.tools.resource.ResourceFilter;
import org.jasm.tools.task.AssemblerTask;
import org.jasm.tools.task.Task;

public class Assembler extends AbstractTool {
	
	private int currentStage = -1;
	private ResourceCollection inputs;
	private File output;
	private List<IClassPathEntry> classpath;
	
	private List<AssemblerTask> survivors = new ArrayList<AssemblerTask>();
	
	private int successCounter = 0;
	
	private boolean verificationEnabled = true;
	


	public Assembler(IPrinter printer, String[] args) {
		super(printer, args);
	}

	@Override
	public void failure(Task source) {
		
		
	}

	@Override
	public synchronized void success(Task source) {
		if (currentStage == 0) {
			boolean twoStages = Environment.getBooleanValue("jasm.dotwostages");
			if (verificationEnabled && twoStages) {
				survivors.add((AssemblerTask)source);
			} else {
				write(source);
				successCounter++;
			}
			
		} else {
			write(source);
			successCounter++;
		}
	}
	
	private void write(Task source) {
		AssemblerTask task = (AssemblerTask)source;
		File target = new File(output,task.getClazz().getThisClass().getClassName()+".class");
		File parent = target.getParentFile();
		if (!parent.exists()) {
			if (!parent.mkdirs()) {
				printer.printError("couldn't create "+parent.getAbsolutePath());
			}
		}
		if (parent.exists()) {
			try {
				FileOutputStream out = new FileOutputStream(target);
				byte [] data = new byte[task.getClazz().getLength()];
				ByteArrayByteBuffer bbuf = new ByteArrayByteBuffer(data);
				task.getClazz().write(bbuf, 0);
				out.write(data);
				out.close();
			} catch (FileNotFoundException e) {
				printer.printError("couldn't write "+target.getAbsolutePath());
			} catch (IOException e) {
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
		
		Option classpath = OptionBuilder.
				hasArgs().
				isRequired(false).
				withArgName("classpath entries").
				withDescription("class path entries (jars zips or dirs)").
				create("classpath");
		result.add(classpath);
		
		return result;
	}

	@Override
	protected boolean readOptions(CommandLine line) {
		inputs = createInputCollecton("input");
		inputs = new FilterResourceCollection(inputs, new ResourceFilter() {
			
			@Override
			public boolean accept(Resource res) {
				return res.getName().endsWith(".jasm");
			}
		});
		if (line.hasOption("classpath")) {
			classpath = createClassPath("classpath");
		} else {
			classpath = new ArrayList<IClassPathEntry>();
		}
		output = createOutputDirectory("output");
		
		
		return output != null;
	}

	@Override
	protected String getScriptName() {
		return "jasm";
	}

	@Override
	protected String getConfPrefix() {
		return "jasm";
	}

	@Override
	protected boolean prepare() {
		return true;
	}

	@Override
	protected int getNumberOfWorkUnits() {
		verificationEnabled = Environment.getBooleanValue("jasm.verification.enabled");
		boolean twoStages = Environment.getBooleanValue("jasm.dotwostages");
		return (verificationEnabled && twoStages)?2:1;
	}
	
	long t = -1;

	@Override
	protected boolean doWorkUnit(int number) {
		currentStage = number;
		boolean twoStages = Environment.getBooleanValue("jasm.dotwostages");
		if (number == 0) {
			t = System.currentTimeMillis();
			assemble();
			if (!verificationEnabled || !twoStages) {
				printer.printInfo("Created "+successCounter+" class files in "+(System.currentTimeMillis()-t)/1000+" secs");
			}
			return true;
		} else if (number == 1) {
			verifyAndWrite();
			printer.printInfo("Created "+successCounter+" class files in "+(System.currentTimeMillis()-t)/1000+" secs");
			return true;
		} else {
			throw new IllegalArgumentException(number+"");
		}
		
	}
	
	private void assemble() {
		
		boolean useThreadPool = Environment.getBooleanValue("jasm.usethreadpool");
		//Creating class resolver
		ClassInfoResolver resolver = null;
		boolean twoStages = Environment.getBooleanValue("jasm.dotwostages");
		if (!twoStages) {
			resolver = createClassInfoResolver();
		}
				
		
		ResourceCollection col = inputs;
		Enumeration<Resource> resources = col.elements();
		if (useThreadPool) {
			ExecutorService pool = Executors.newFixedThreadPool(Environment.getIntValue("jasm.threadpoolsize"));
			while (resources.hasMoreElements()) {
				Resource resource = resources.nextElement();
				AssemblerTask task = new AssemblerTask(this, resource, Environment.getContent());
				task.setStage(0);
				task.setResolver(resolver);
				pool.execute(task);
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
				AssemblerTask task = new AssemblerTask(this, resource, null);
				task.setStage(0);
				task.setResolver(resolver);
				task.run();
			}
		}
		
	}
	
	private ClassInfoResolver createClassInfoResolver() {
		boolean twoStages = Environment.getBooleanValue("jasm.dotwostages");
		ClassInfoResolver resolver = new ClassInfoResolver();
		if (twoStages) {
			for (AssemblerTask task: survivors) {
				task.getClazz().setResolver(resolver);
				resolver.add(new ClazzClassPathEntry(task.getClazz()));
			}
		}
		for (IClassPathEntry entry: classpath) {
			resolver.add(entry);
		}
		if (Environment.getBooleanValue("jasm.classpath.useruntime")) {
			File rtJar = getRuntimeJar();
			if (rtJar != null) {
				resolver.add(new JarFileClassPathEntry(getRuntimeJar()));
			}
			//resolver.add(new ClassLoaderClasspathEntry(Thread.currentThread().getContextClassLoader()));
			
		}
		
		return resolver;
	}
	
	private void verifyAndWrite() {
		
		//Creating class resolver
		ClassInfoResolver resolver = createClassInfoResolver();
		
		boolean useThreadPool = Environment.getBooleanValue("jasm.usethreadpool");
		
		if (useThreadPool) {
			ExecutorService pool = Executors.newFixedThreadPool(Environment.getIntValue("jasm.threadpoolsize"));
			for (AssemblerTask task: survivors) {
				task.setStage(1);
				task.setResolver(resolver);
				pool.execute(task);
			}
			pool.shutdown();
			try {
				pool.awaitTermination(2, TimeUnit.DAYS);
			} catch (InterruptedException e) {
				//ignore
			}
		} else {
			for (AssemblerTask task: survivors) {
				task.setStage(1);
				task.setResolver(resolver);
				task.run();
			}
		}
	}
	
	
	@Override
	protected boolean acceptInput(File f) {
		return (f.isFile() && f.getName().endsWith(".jasm")) || f.isDirectory();
	}
	
	public static void main(String [] args) {
		new Assembler(new ConsolePrinter(), args).run();
		System.exit(0);

	}

}
