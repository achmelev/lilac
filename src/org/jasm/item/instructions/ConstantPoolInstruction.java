package org.jasm.item.instructions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.DoubleInfo;
import org.jasm.item.constantpool.FieldrefInfo;
import org.jasm.item.constantpool.FloatInfo;
import org.jasm.item.constantpool.IConstantPoolReference;
import org.jasm.item.constantpool.IntegerInfo;
import org.jasm.item.constantpool.InterfaceMethodrefInfo;
import org.jasm.item.constantpool.InvokeDynamicInfo;
import org.jasm.item.constantpool.LongInfo;
import org.jasm.item.constantpool.MethodHandleInfo;
import org.jasm.item.constantpool.MethodTypeInfo;
import org.jasm.item.constantpool.MethodrefInfo;
import org.jasm.item.constantpool.StringInfo;
import org.jasm.parser.literals.SymbolReference;
import org.jasm.resolver.AbstractInfo;
import org.jasm.resolver.ExternalClassInfo;
import org.jasm.resolver.FieldInfo;
import org.jasm.resolver.MethodInfo;

public class ConstantPoolInstruction extends AbstractInstruction implements IConstantPoolReference {
	
	private int cpEntryIndex = -1;
	protected SymbolReference cpEntryReference = null;
	protected AbstractConstantPoolEntry cpEntry = null; 
	protected AbstractInfo info;
	
	private static Map<Short, Class[]> allowedTypes = null;
	
	static {
		allowedTypes = new HashMap<Short, Class[]>();
		allowedTypes.put(OpCodes.anewarray, new Class[]{ClassInfo.class});
		allowedTypes.put(OpCodes.checkcast, new Class[]{ClassInfo.class});
		allowedTypes.put(OpCodes.getfield, new Class[]{FieldrefInfo.class});
		allowedTypes.put(OpCodes.getstatic, new Class[]{FieldrefInfo.class});
		allowedTypes.put(OpCodes.instanceof_, new Class[]{ClassInfo.class});
		allowedTypes.put(OpCodes.invokeinterface, new Class[]{InterfaceMethodrefInfo.class});
		allowedTypes.put(OpCodes.invokedynamic, new Class[]{InvokeDynamicInfo.class});
		allowedTypes.put(OpCodes.invokespecial, new Class[]{MethodrefInfo.class,InterfaceMethodrefInfo.class});
		allowedTypes.put(OpCodes.invokestatic, new Class[]{MethodrefInfo.class,InterfaceMethodrefInfo.class});
		allowedTypes.put(OpCodes.invokevirtual, new Class[]{MethodrefInfo.class});
		allowedTypes.put(OpCodes.ldc2_w, new Class[]{DoubleInfo.class, LongInfo.class});
		allowedTypes.put(OpCodes.new_, new Class[]{ClassInfo.class});
		allowedTypes.put(OpCodes.putfield, new Class[]{FieldrefInfo.class});
		allowedTypes.put(OpCodes.putstatic, new Class[]{FieldrefInfo.class});
		allowedTypes.put(OpCodes.multianewarray, new Class[]{ClassInfo.class});
	}
	
	
	
	public ConstantPoolInstruction(short opCode, AbstractConstantPoolEntry entry) {
		super(opCode);
		this.cpEntry = entry;
		if (!(OpCodes.isConstantPoolInstruction(opCode) 
				||(opCode ==OpCodes.multianewarray)
				||(opCode == OpCodes.invokedynamic)
				||(opCode == OpCodes.invokeinterface)
			)) {
			throw new IllegalArgumentException("Unknown opcode: "+Integer.toHexString(opCode));
		}
		if (cpEntry != null) {
			Class[] classes = allowedTypes.get(getOpCode());
			if (classes == null) {
				throw new IllegalArgumentException("No allowed classes registered for "+Integer.toHexString(getOpCode()));
			}
			boolean found = false;
			for (Class cl: classes) {
				found = found || cl.equals(entry.getClass());
			}
			if (!found) {
				throw new IllegalArgumentException("class "+ entry.getClass().getName()+" isn't allowed for " + Integer.toHexString(opCode));
			}
		}	
	}

	@Override
	public int getLength() {
		return 3;
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
	public String getPrintArgs() {
		return cpEntry.getSymbolName();
	}

	@Override
	public String getPrintComment() {
		//return cpEntry.getPrintComment();
		return null;
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		cpEntryIndex = source.readUnsignedShort(offset);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedShort(offset, cpEntry.getIndexInPool());
	}

	@Override
	protected void doResolve() {
		cpEntry = getConstantPool().get(cpEntryIndex-1);
	}
	
	@Override
	protected void doResolveAfterParse() {
		Class[] classes = allowedTypes.get(getOpCode());
		if (classes == null) {
			throw new IllegalStateException("No allowed classes registered for "+Integer.toHexString(getOpCode()));
		}
		cpEntry = getConstantPool().checkAndLoadFromSymbolTable(this,classes, cpEntryReference);
	}

	@Override
	public AbstractConstantPoolEntry[] getConstantReferences() {
		return new AbstractConstantPoolEntry[]{cpEntry};
	}

	public void setCpEntryReference(SymbolReference cpEntryReference) {
		this.cpEntryReference = cpEntryReference;
	}

	@Override
	protected void doVerify() {
		//Loading classes/methods/arguments and checking access
		if (cpEntry instanceof ClassInfo) {
			info = getRoot().checkAndLoadClassInfo(this, cpEntryReference, ((ClassInfo)cpEntry).getClassName(), true);
		} else if (cpEntry instanceof FieldrefInfo) {
			info = getRoot().checkAndLoadFieldInfo(this, cpEntryReference, ((FieldrefInfo)cpEntry).getClassName(), ((FieldrefInfo)cpEntry).getName(), ((FieldrefInfo)cpEntry).getDescriptor(), true);
		} else if (cpEntry instanceof MethodrefInfo) {
			info = getRoot().checkAndLoadMethodInfo(this, cpEntryReference, ((MethodrefInfo)cpEntry).getClassName(), ((MethodrefInfo)cpEntry).getName(), ((MethodrefInfo)cpEntry).getDescriptor(), true);
		} else if (cpEntry instanceof InterfaceMethodrefInfo) {
			info = getRoot().checkAndLoadInterfaceMethodInfo(this, cpEntryReference, ((InterfaceMethodrefInfo)cpEntry).getClassName(), ((InterfaceMethodrefInfo)cpEntry).getName(), ((InterfaceMethodrefInfo)cpEntry).getDescriptor(), true);
			
		}
		
		if (info != null) {
			verifyInstructions();
		}
		
	}
	
	protected void verifyInstructions() {
		if (this.getOpCode() == OpCodes.getstatic || this.getOpCode() == OpCodes.putstatic) {
			FieldInfo fi = (FieldInfo)info;
			if (!fi.getModifier().isStatic()) {
				emitError(cpEntryReference, "the field must be static");
			}
		} else if (this.getOpCode() == OpCodes.getfield || this.getOpCode() == OpCodes.putfield) {
			FieldInfo fi = (FieldInfo)info;
			if (fi.getModifier().isStatic()) {
				emitError(cpEntryReference, "the field must be non-static");
			}
		}  else if (this.getOpCode() == OpCodes.new_) {
			ExternalClassInfo cli = (ExternalClassInfo)info;
			if (info == null) {
				throw new IllegalStateException();
			}
			if (cli.isArray()) {
				emitError(cpEntryReference, "array instantiation with new");
			}
			if (cli.getModifier().isAbstract() || cli.getModifier().isInterface()) {
				emitError(cpEntryReference, "instantiation of an abstract class or interface");
			}
		} else if (this.getOpCode() == OpCodes.invokestatic) {
			MethodInfo mi = (MethodInfo)info;
			if (!mi.getModifier().isStatic()) {
				emitError(cpEntryReference, "method must be static");
			}
			if (mi.getName().equals("<init>")) {
				emitError(cpEntryReference, "static invocation of a constructor");
			} else if (mi.getName().equals("<clinit>")) {
				emitError(cpEntryReference, "invocation of a class initialization method");
			}
		} else if (this.getOpCode() == OpCodes.invokevirtual) {
			MethodInfo mi = (MethodInfo)info;
			if (mi.getModifier().isStatic()) {
				emitError(cpEntryReference, "method must be non-static");
			} 
			if (mi.getName().equals("<init>")) {
				emitError(cpEntryReference, "virtual invocation of a constructor");
			} else if (mi.getName().equals("<clinit>")) {
				emitError(cpEntryReference, "invocation of a class initialization method");
			}
		} else if (this.getOpCode() == OpCodes.invokeinterface) {
			MethodInfo mi = (MethodInfo)info;
			if (mi.getModifier().isStatic()) {
				emitError(cpEntryReference, "method must be non-static");
			} 
			if (mi.getName().equals("<init>")) {
				emitError(cpEntryReference, "interface invocation of a constructor");
			} else if (mi.getName().equals("<clinit>")) {
				emitError(cpEntryReference, "invocation of a class initialization method");
			}
		} else if (this.getOpCode() == OpCodes.invokespecial) {
			MethodInfo mi = (MethodInfo)info;
			if (mi.getModifier().isStatic()) {
				emitError(cpEntryReference, "method must be non-static");
			} 
			if (mi.getName().equals("<clinit>")) {
				emitError(cpEntryReference, "invocation of a class initialization method");
			}
			
		}
		
	}
	

	public AbstractConstantPoolEntry getCpEntry() {
		return cpEntry;
	}

	public AbstractInfo getInfo() {
		return info;
	}
	
	
	
	

}
