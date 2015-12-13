package org.jasm.item.instructions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jasm.JasmConsts;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.clazz.Method;
import org.jasm.parser.literals.JavaTypeLiteral;
import org.jasm.parser.literals.SymbolReference;
import org.jasm.type.descriptor.TypeDescriptor;

public class LocalVariablesPool {
	
	private Instructions parent;
	
	private List<LocalVariable> variables = new ArrayList<>();
	private Map<String, LocalVariable> nameToVar = new HashMap<>();
	
	private boolean emittingDeactivated = false;
	
	
	
	public void addVariable(LocalVariable var) {
		variables.add(var);
	}
	
	public void resolveAfterParse() {
		List<LocalVariable> vars = new ArrayList<LocalVariable>();
		
		//Addding param vars
		if (parent != null) {
			Method m = parent.getAncestor(Method.class);
			if (m.isHighLevelSyntax() && !m.hasErrors()) {
				List<LocalVariable> implicitVars = new ArrayList<LocalVariable>();
				//Adding this
				if (!m.getModifier().isStatic()) {
					LocalVariable thisVar = new LocalVariable(LocalVariable.getTypeCode(JasmConsts.TYPENAME_OBJECT));
					thisVar.setName(new SymbolReference(m.getSourceLocation().getLine(), m.getSourceLocation().getCharPosition(), "this"));
					implicitVars.add(thisVar);
				}
				//Adding params
				if (m.getParameterTypes() != null && m.getParameterTypes().size()>0) {
					for (int i=0;i<m.getParameterTypes().size(); i++) {
						JavaTypeLiteral type = m.getParameterTypes().get(i);
						TypeDescriptor desc = type.getDescriptor();
						SymbolReference name = m.getParamNames().get(i);
						String varType = null;
						if (desc.isLong()) {
							varType = JasmConsts.TYPENAME_LONG;
						} else if (desc.isFloat()) {
							varType = JasmConsts.TYPENAME_FLOAT;
						} else if (desc.isDouble()) {
							varType = JasmConsts.TYPENAME_DOUBLE;
						} else if (desc.isObject() || desc.isArray()) {
							varType = JasmConsts.TYPENAME_OBJECT;
						} else {
							varType = JasmConsts.TYPENAME_INT;
						}
						LocalVariable parVar = new LocalVariable(LocalVariable.getTypeCode(varType));
						parVar.setName(name);
						implicitVars.add(parVar);
					}
				}
				variables.addAll(0, implicitVars);
			}
		}	
		//Building maps
		for (LocalVariable var: variables) {
			if (nameToVar.containsKey(var.getName().getSymbolName())) {
				emitError(var.getName(), "multiple var declarations with name "+var.getName().getSymbolName());
			} else {
				nameToVar.put(var.getName().getSymbolName(), var);
				vars.add(var);
				var.setPool(this);
			}
		}
		variables = vars;
		
		//Resolving parents
		vars = new ArrayList<LocalVariable>();
		for (LocalVariable var: variables) {
			if (!var.isResolved()) {
				var.resolveParent();
			}
			if (!var.isHasResolveErrors()) {
				vars.add(var);
			}
		}
		variables = vars;
		
		//Sort according to parent-child-relationship
		Comparator<LocalVariable> comp = new Comparator<LocalVariable>() {

			@Override
			public int compare(LocalVariable o1, LocalVariable o2) {
				if (o1.isAncestor(o2)) {
					return 1;
				} else if (o1.isAncestor(o2)) {
					return -1;
				} else {
					return 0;
				}
			}
		}; 
		
		Collections.sort(variables, comp);
		//Calculate indexes
		int lastOffset = 0;
		vars = new ArrayList<LocalVariable>();
		for (LocalVariable var: variables) {
			var.calculateIndex(lastOffset);
			if (var.getIndex()>=0) {
				lastOffset = Math.max(lastOffset, var.getIndex()+var.getLength());
				vars.add(var);
			}
		}
		variables = vars;
		
		//clean up nameToVar
		nameToVar.clear();
		for (LocalVariable var: variables) {
			nameToVar.put(var.getName().getSymbolName(), var);
		}
		
		
	}
	
	public LocalVariable getByName(String name) {
		return nameToVar.get(name);
	}
	
	public int calculate(LocalVariable var) {
		return variables.indexOf(var);
	}


	public void setParent(Instructions parent) {
		this.parent = parent;
	}

	
	
	public void emitError(SymbolReference ref, String message) {
		if (!emittingDeactivated) {
			parent.emitError(ref, message);
		}
	}

	public void setEmittingDeactivated(boolean emittingDeactivated) {
		this.emittingDeactivated = emittingDeactivated;
	}
	
	public int calculateSize() {
		int size = 0;
		for (LocalVariable var: variables) {
			size = Math.max(size, var.getIndex()+var.getLength());
		}
		return size;
	}
	
	public LocalVariable checkAndLoad(AbstractByteCodeItem caller,SymbolReference ref, char type, boolean returnAddressAllowed) {
		if (nameToVar.containsKey(ref.getSymbolName())) {
			LocalVariable var = nameToVar.get(ref.getSymbolName());
			if (var.getType() == type 
				|| 
				(returnAddressAllowed && var.getType() ==  JasmConsts.LOCAL_VARIABLE_TYPE_RETURNADRESS && type == JasmConsts.LOCAL_VARIABLE_TYPE_REFERENCE)
				|| type == 0) {
				return var;
			} else {
				if (caller != null) {
					caller.emitError(ref, "wrong variable type, expected "+LocalVariable.getTypeName(type));
				}
				
				return null;
			}
			
		} else {
			if (caller != null) {
				caller.emitError(ref, "unknown variable: "+ref.getSymbolName());
			}
			return null;
		}
	}
	

}
