package org.jasm.bytebuffer.print;

import java.io.PrintWriter;

import org.apache.commons.lang3.StringEscapeUtils;

public class PrettyPrinter {
	
	private PrintWriter writer = null;
	
	private int indentation = 0;
	
	public PrettyPrinter(PrintWriter writer) {
		this.writer = writer;
	}
	
	
	public void printLine(String line) {
		StringBuffer buf = new StringBuffer();
		for (int i=0;i<indentation*2; i++) {
			buf.append(' ');
		}
		buf.append(line);
		writer.println(buf.toString());
	}
	
	public void addIndent() {
		indentation++;
	}
	
	public void removeIndent() {
		if (indentation >0) {
			indentation--;
		}
	}
	
	public void printItem(IPrintable item) {
		
		/*if (item.isStructure() && item.getStructureParts().size() == 0) {
			return;
		}*/
		
		StringBuffer buf = new StringBuffer();
		if (item == null) {
			System.out.println("here!");
		}
		if (item.getPrintLabel() != null) {
			buf.append(item.getPrintLabel()+": ");
		}
		if (item.isStructure()) {
			if (item.getPrintName() != null) {
				buf.append(item.getPrintName());
				if (item.getPrintArgs() != null) {
					buf.append(" "+item.getPrintArgs());
				}
				buf.append(" {");
				if (item.getPrintComment() != null) {
					buf.append(" //"+item.getPrintComment());
				}
				printLine(buf.toString());
				addIndent();
			} else {
				if (item.getPrintComment() != null) {
					printLine(" //"+item.getPrintComment());
				}
			}
			for (IPrintable item1: item.getStructureParts()) {
				if (item1 !=null) {
					printItem(item1);
				}
			}
			if (item.getPrintName() != null) {
				removeIndent();
				printLine("}");
			}
		} else {
			if (item.getPrintName() != null) {
				buf.append(item.getPrintName());
				if (item.getPrintArgs() != null) {
					buf.append(" "+item.getPrintArgs());
				}
				buf.append(";");
				if (item.getPrintComment() != null) {
					buf.append(" // "+item.getPrintComment());
				}
			} else {
				if (item.getPrintComment() != null) {
					buf.append("//"+item.getPrintComment());
				}
			}
			
			
			printLine(buf.toString());
		}
	}
	
	public static String getJavaStyleString(String source) {
		return "\""+StringEscapeUtils.escapeJava(source)+"\"";
	}
}
