package org.jasm.bytebuffer.print;

import java.util.List;


public interface IPrintable {
	//PrintMethods
	public boolean isStructure();
	public List<IPrintable> getStructureParts();
	public String  getPrintLabel();
	public String  getPrintName();
	public String  getPrintArgs();
	public String  getPrintComment();

}
