package org.jasm.tools;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.jasm.tools.print.ConsolePrinter;
import org.jasm.tools.print.IPrinter;
import org.jasm.tools.task.Task;

public class DummyTool extends AbstractTool {

	public DummyTool(IPrinter printer, String[] args) {
		super(printer, args);
	}

	@Override
	protected List<Option> createSpecificOptions() {
		List<Option> result = new ArrayList<Option>();
		
		Option dummy = OptionBuilder.
						isRequired(true).
						withArgName("dummy").
						withDescription("dummy args").
						create("dummy");
		result.add(dummy);
		
		
		return result;
	}

	@Override
	protected boolean readOptions(CommandLine line) {
		return true;
	}

	@Override
	protected String getScriptName() {
		return "dummy";
	}
	
	public static void main(String[] args) {
		new DummyTool(new ConsolePrinter(), args).run();
	}

	@Override
	protected String getConfPrefix() {
		return "dummy";
	}

	@Override
	protected boolean prepare() {
		return false;
	}

	@Override
	protected int getNumberOfWorkUnits() {
		return 0;
	}

	@Override
	protected boolean doWorkUnit(int number) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void failure(Task source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void success(Task source) {
		// TODO Auto-generated method stub
		
	}

}
