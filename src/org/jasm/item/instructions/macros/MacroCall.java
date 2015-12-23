package org.jasm.item.instructions.macros;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jasm.item.instructions.Instructions;
import org.jasm.parser.ISymbolTableEntry;
import org.jasm.parser.SourceLocation;
import org.jasm.parser.literals.JavaTypeLiteral;
import org.jasm.parser.literals.Label;
import org.jasm.parser.literals.SymbolReference;

public class MacroCall implements ISymbolTableEntry, IMacroArgument {
	
	private SymbolReference nameReference;
	private List<IMacroArgument> arguments = new ArrayList<IMacroArgument>();
	private Map<IMacroArgument, JavaTypeLiteral> casttypes = new HashMap<IMacroArgument, JavaTypeLiteral>();
	private SourceLocation sourceLocation = null;
	private int index=0;
	private Label label;
	
	private IMacro macro; 
	
	private Instructions parent;
	
	public void setNameReference(SymbolReference nameReference) {
		this.nameReference = nameReference;
	}
	
	public void addArgument(IMacroArgument argument) {
		arguments.add(argument);
	}
	
	public void cast(IMacroArgument argument, JavaTypeLiteral castType) {
		casttypes.put(argument, castType);
	}

	public SymbolReference getNameReference() {
		return nameReference;
	}

	public List<IMacroArgument> getArguments() {
		return arguments;
	}

	public SourceLocation getSourceLocation() {
		return sourceLocation;
	}

	public void setSourceLocation(SourceLocation sourceLocation) {
		this.sourceLocation = sourceLocation;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}

	@Override
	public String getSymbolName() {
		if (label == null) {
			return null;
		}
		return label.getLabel();
	}

	@Override
	public boolean hasErrors() {
		return false;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public String getInvalidErrorMessage() {
		return null;
	}

	public IMacro getMacro() {
		return macro;
	}

	public void setMacro(IMacro macro) {
		this.macro = macro;
	}

	public Instructions getParent() {
		return parent;
	}

	public void setParent(Instructions parent) {
		this.parent = parent;
	}
	
	
}
