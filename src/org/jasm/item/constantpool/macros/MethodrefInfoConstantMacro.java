package org.jasm.item.constantpool.macros;

import java.util.ArrayList;
import java.util.List;

import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.parser.literals.AbstractLiteral;
import org.jasm.parser.literals.ClassReference;
import org.jasm.parser.literals.JavaTypeLiteral;
import org.jasm.parser.literals.SymbolReference;
import org.jasm.type.descriptor.MethodDescriptor;
import org.jasm.type.descriptor.TypeDescriptor;

public class MethodrefInfoConstantMacro extends AbstractConstantMacro {
	
	private AbstractLiteral clazz;
	private SymbolReference name;
	private JavaTypeLiteral returnType;
	private List<JavaTypeLiteral> parameterTypes;
	private boolean isInterfaceMethod;

	@Override
	public void resolve() {
		ClassInfo classInfo = null;
		String classSymbolName = null;
		if (clazz instanceof SymbolReference) {
			SymbolReference ref = (SymbolReference)clazz;
			classInfo = parent.checkAndLoadFromSymbolTable(parent, ClassInfo.class, ref);
			classSymbolName = ref.getSymbolName();
		} else if (clazz instanceof ClassReference)  {
			ClassReference ref = (ClassReference)clazz;
			classInfo = parent.getOrAddClassInfo(ref.getClassName());
			classSymbolName = registerClassConstant(ref.getClassName(), null);
		} else {
			throw new IllegalStateException("");
		}
		
		if (classInfo != null) {
			ClassInfo ci = (ClassInfo)parent.getSymbolTable().get(classSymbolName);
			MethodDescriptor desc = createDescriptor();
			AbstractConstantPoolEntry result = null;
			if (desc != null) {
				if (this.isInterfaceMethod) {
					result = parent.getOrAddInterfaceMethofdrefInfo(ci.getClassName(), name.getSymbolName(), desc.getValue());
				} else {
					result = parent.getOrAddMethofdrefInfo(ci.getClassName(), name.getSymbolName(), desc.getValue());
				}
				registerConstant(classSymbolName+"."+name.getSymbolName(), result, label);
			}	
		}
		
	}
	
	public void setReturnType(JavaTypeLiteral returnTypeLiteral) {
		this.returnType = returnTypeLiteral;
		this.returnType.setParent(parent);
	}
	
	public void addParameterType(JavaTypeLiteral literal) {
		if (parameterTypes == null) {
			parameterTypes = new ArrayList<JavaTypeLiteral>();
		}
		parameterTypes.add(literal);
		literal.setParent(parent);
	}

	public void setName(SymbolReference name) {
		this.name = name;
	}
	
	protected MethodDescriptor createDescriptor() {
		TypeDescriptor returnTypeDesc = (returnType == null)?null:returnType.getDescriptor();
		List<TypeDescriptor> paramTypes = new ArrayList<TypeDescriptor>();
		if (parameterTypes != null) {
			for (JavaTypeLiteral jtl: parameterTypes) {
				TypeDescriptor desc = jtl.getDescriptor();
				if (desc != null) {
					paramTypes.add(desc);
				}	
			}
		}
		if (parent.hasErrors()) {
			return null;
		}
		return  new MethodDescriptor(paramTypes, returnTypeDesc);
	}

	public void setInterfaceMethod(boolean isInterfaceMethod) {
		this.isInterfaceMethod = isInterfaceMethod;
	}

	public void setClazz(AbstractLiteral clazz) {
		this.clazz = clazz;
	}
	
	

}
