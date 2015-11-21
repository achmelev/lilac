package org.jasm.item.instructions.macros;

import java.util.ArrayList;
import java.util.List;

import org.jasm.parser.ISymbolTableEntry;
import org.jasm.parser.SourceLocation;
import org.jasm.parser.literals.Label;
import org.jasm.parser.literals.SymbolReference;

public class MacroCall implements ISymbolTableEntry{
	
	private SymbolReference nameReference;
	private List<IMacroArgument> arguments = new ArrayList<IMacroArgument>();
	private SourceLocation sourceLocation = null;
	private int index=0;
	private Label label;
	
	public void setNameReference(SymbolReference nameReference) {
		this.nameReference = nameReference;
	}
	
	public void addArgument(IMacroArgument argument) {
		arguments.add(argument);
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
	
	
	
	
	

}
