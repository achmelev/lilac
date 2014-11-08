package org.jasm.item.instructions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jasm.parser.literals.IntegerLiteral;
import org.jasm.parser.literals.SymbolReference;



public abstract class AbstractSwitchInstruction extends AbstractInstruction {
	
	
	
	
	public AbstractSwitchInstruction(short opCode) {
		super(opCode);
		// TODO Auto-generated constructor stub
	}

	private SymbolReference defaultTargetReference = null;
	private List<IntegerLiteral> intLiterals = new ArrayList<>();
	private Map<IntegerLiteral, SymbolReference> targetReferences= new HashMap<>();
	
	
	public void addTargetReference(IntegerLiteral lit, SymbolReference ref) {
		targetReferences.put(lit, ref);
		intLiterals.add(lit);
	}
	
	public void setDefaultTargetReference(SymbolReference defaultTargetReference) {
		if (this.defaultTargetReference != null) {
			emitError(defaultTargetReference, "multiple default target declarations");
		}
		this.defaultTargetReference = defaultTargetReference;
	}
	
	@Override
	protected void doResolveAfterParse() {
		Instructions instrs = (Instructions)getParent();
		AbstractInstruction defaultInstr = null;
		if (defaultTargetReference != null) {
			defaultInstr = instrs.checkAndLoadFromSymbolTable(this, defaultTargetReference);
		} else {
			emitError(null, "missing default target declaration");
		}
		
		
		List<Integer> values = new ArrayList<>();
		Map<Integer, AbstractInstruction> targets = new HashMap<>();
		for (IntegerLiteral lit: intLiterals) {
			Integer value = lit.checkAndLoadValue(this);
			if (values.contains(value)) {
				emitError(lit, "multiple target declarations for "+value);
			} else {
				SymbolReference ref = targetReferences.get(lit);
				AbstractInstruction instr = instrs.checkAndLoadFromSymbolTable(this, ref);
				if (instr != null) {
					values.add(value);
					targets.put(value, instr);
				}
			}
		}
		
		int [] iValues = new int[values.size()];
		AbstractInstruction[] targetsArray = new AbstractInstruction[values.size()];
		for (int i=0;i<values.size(); i++) {
			iValues[i] = values.get(i);
			targetsArray[i] = targets.get(values.get(i));
		}
		
		if (defaultInstr != null) {
			setTargets(defaultInstr,iValues,targetsArray);
		}
		
		
	}
	
	protected abstract void setTargets(AbstractInstruction defaultTarget, int[] values, AbstractInstruction[] targets);
	
	
	
}
