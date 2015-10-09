package org.jasm.bytebuffer.print;

import java.util.List;

public class SimplePrintable implements IPrintable {
	
	private String label;
	private String name; 
	private String[] args;
	private String [] comment = null;
	
	public SimplePrintable(String label, String name, String[] args, String [] comment) {
		this.label = label;
		this.name = name;
		this.args = args;
		this.comment = comment;
	}
	
	public SimplePrintable(String label, String name, String[] args, String comment) {
		this(label, name, args, comment==null?null:new String[]{comment});
	}
	
	public SimplePrintable(String label, String name, String arg, String comment) {
		this(label, name, arg==null?null:new String[]{arg}, comment==null?null:new String[]{comment});
	}
	

	@Override
	public boolean isStructure() {
		return false;
	}

	@Override
	public List<IPrintable> getStructureParts() {
		return null;
	}

	@Override
	public String getPrintLabel() {
		return this.label;
	}

	@Override
	public String getPrintName() {
		return this.name;
	}

	@Override
	public String getPrintArgs() {
		if (args == null) {
			return null;
		}
		StringBuffer buf = new StringBuffer();
		for (int i=0;i<args.length; i++) {
			if (i>0) {
				buf.append(", ");
			}
			buf.append(args[i]);
			
		}
		return buf.toString();
	}

	@Override
	public String getPrintComment() {
		if (comment == null) {
			return null;
		}
		StringBuffer buf = new StringBuffer();
		for (int i=0;i<comment.length; i++) {
			if (i>0) {
				buf.append(", ");
			}
			buf.append(comment[i]);
			
		}
		return buf.toString();
	}

	@Override
	public boolean toOmit() {
		return false;
	}

}
