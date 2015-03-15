package org.jasm.tools;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Disassembler {
	
	public static void main(String [] args) {
		CommandLineParser parser = new GnuParser();
	    try {
	        // parse the command line arguments
	        CommandLine line = parser.parse( createOptions(), args );
	        
	        String[] result = line.getOptionValues("input");
	    	
	    }
	    catch(ParseException exp ) {
	        System.out.println( "command line parsing failed.  Reason: " + exp.getMessage() );
	        usage();
	    }

	}
	
	private static Options createOptions() {
		Options result = new Options();
		
		Option input = OptionBuilder.
						hasArgs(2).
						isRequired(true).
						withArgName("input").
						withDescription("inputs (jars,zips, class files or directories)").
						create("input");
		result.addOption(input);
		
		return result;
	}
	
	private static void usage() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "ant", createOptions() );
	}
	

}
