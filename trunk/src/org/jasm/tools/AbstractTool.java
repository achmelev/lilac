package org.jasm.tools;

import org.jasm.tools.print.IPrinter;

public class AbstractTool implements Runnable {
	
	private String[] args;
	private IPrinter printer;
	
	public AbstractTool(IPrinter printer, String[] args) {
		this.printer = printer;
		this.args = args;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
