package org.jasm.item.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.ConstantPool;
import org.jasm.item.constantpool.IConstantPoolReference;
import org.jasm.parser.literals.SymbolReference;

public class ExceptionsAttributeContent extends AbstractSimpleAttributeContent implements IConstantPoolReference {
	
	private int [] indexes = null; 
	private List<SymbolReference> exceptionReferences;
	private Set<Integer> referencedThrowsDeclarations = new HashSet<Integer>();
	private ClassInfo[] classInfos = null;
	
	private Map<String, Integer> exceptionIndexesLabelTable =  new HashMap<String, Integer>();
	
	
	public ExceptionsAttributeContent() {
		
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		int numberOfInfos = source.readUnsignedShort(offset);
		this.indexes = new int[numberOfInfos];
		for (int i=0;i<numberOfInfos; i++) {
			indexes[i] = source.readUnsignedShort(offset+2*(i+1));
		}
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedShort(offset, classInfos.length);
		for (int i=0;i<classInfos.length; i++) {
			target.writeUnsignedShort(offset+2*(i+1), classInfos[i].getIndexInPool());
		}
	}

	@Override
	public int getLength() {
		return (classInfos.length+1)*2;
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
		return null;
	}
	
	

	
	@Override
	public String getPrintName() {
		return "throws";
	}
	
	@Override
	public String getPrintArgs() {
		StringBuffer buf = new StringBuffer();
		int index = 0;
		for (int i=0;i<classInfos.length; i++) {
			ClassInfo cli = classInfos[i];
			if (index >0) {
				buf.append(", ");
			}
			boolean hasLabel = referencedThrowsDeclarations.contains(i);
			buf.append((hasLabel?("throwsref_"+i+":"):"")+cli.getSymbolName());
			index++;
		}
		return buf.toString();
	}

	@Override
	public String getPrintComment() {
		StringBuffer buf = new StringBuffer();
		int index = 0;
		for (ClassInfo cli: classInfos) {
			if (index > 0) {
				buf.append(", ");
			}
			buf.append(cli.getClassName());
			index++;
		}
		return buf.toString();
	}

	@Override
	protected void doResolve() {
		classInfos = new ClassInfo[indexes.length];
		for (int i=0;i<indexes.length; i++) {
			classInfos[i] = (ClassInfo)getConstantPool().get(indexes[i]-1);
		}
	}
	
	@Override
	protected void doResolveAfterParse() {
		ConstantPool pool = getConstantPool();
		List<ClassInfo> exceptions = new ArrayList<>();
		for (SymbolReference ref: exceptionReferences) {
			ClassInfo cl = pool.checkAndLoadFromSymbolTable(this,ClassInfo.class, ref);
			if (cl != null) {
				exceptions.add(cl);
			}
		}
		classInfos = new ClassInfo[exceptions.size()];
		classInfos = exceptions.toArray(classInfos);
		
		for (int i=0;i<exceptionReferences.size(); i++) {
			addExceptionIndexLabel(exceptionReferences.get(i), i);
		}
	}

	
	
	public ClassInfo[] getClassInfos() {
		return classInfos;
	}

	public List<String> getExceptionClassNames() {
		List<String> result = new ArrayList<>();
		for (ClassInfo cli: classInfos) {
			result.add(cli.getClassName());
		}
		return result;
	}

	@Override
	public AbstractConstantPoolEntry[] getConstantReferences() {
		return classInfos;
	}

	public void setExceptionReferences(List<SymbolReference> exceptionReferences) {
		this.exceptionReferences = exceptionReferences;
	}

	@Override
	protected void doUpdateMetadata() {
		referencedThrowsDeclarations.clear();
		List<IBytecodeItem> items = ((AbstractByteCodeItem)getParent().getParent()).getAllItemsFromHere();
		for (IBytecodeItem item: items) {
			if (item instanceof IThrowsDeclarationsReference) {
				for (int index: ((IThrowsDeclarationsReference)item).getIndexes()) {
					referencedThrowsDeclarations.add(index);
				}
			}
		}
	}
	
	public void addExceptionIndexLabel(SymbolReference ref, int index) {
		if (ref.getReferenceLabel() != null) {
			if (exceptionIndexesLabelTable.containsKey(ref.getReferenceLabel())) {
				emitError(ref, "dublicate throws label");
			} else {
				exceptionIndexesLabelTable.put(ref.getReferenceLabel(), index);
			}
		}
	}
	
	public Integer checkAndLoadInterfaceIndex(AbstractByteCodeItem caller, SymbolReference ref) {
		if (!exceptionIndexesLabelTable.containsKey(ref.getSymbolName())) {
			caller.emitError(ref, "unknown throws label");
			return null;
		} else {
			return exceptionIndexesLabelTable.get(ref.getSymbolName());
		}
	}
	
	

}
