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
import org.jasm.resolver.ClazzClassPathEntry;
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
	
	private List<AssemblerTask> survivors = new ArrayList<AssemblerTask>();


	public Assembler(IPrinter printer, String[] args) {
		super(printer, args);
	}

	@Override
	public void failure(Task source) {
		
		
	}

	@Override
	public synchronized void success(Task source) {
		if (currentStage == 0) {
			survivors.add((AssemblerTask)source);
		} else {
			write(source);
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
		return 2;
	}

	@Override
	protected boolean doWorkUnit(int number) {
		currentStage = number;
		if (number == 0) {
			assemble();
			return true;
		} else if (number == 1) {
			verifyAndWrite();
			return true;
		} else {
			throw new IllegalArgumentException(number+"");
		}
		
	}
	
	private void assemble() {
		ResourceCollection col = inputs;
		Enumeration<Resource> resources = col.elements();
		ExecutorService pool = Executors.newFixedThreadPool(Environment.getIntValue("jasm.threadpoolsize"));
		while (resources.hasMoreElements()) {
			Resource resource = resources.nextElement();
			AssemblerTask task = new AssemblerTask(this, resource, Environment.getContent());
			task.setStage(0);
			pool.execute(task);
		}
		pool.shutdown();
		try {
			pool.awaitTermination(2, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			//ignore
		}
	}
	
	private void verifyAndWrite() {
		
		ClassInfoResolver resolver = new ClassInfoResolver();
		for (AssemblerTask task: survivors) {
			resolver.add(new ClazzClassPathEntry(task.getClazz()));
		}
		
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
