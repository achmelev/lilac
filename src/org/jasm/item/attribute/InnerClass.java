package org.jasm.item.attribute;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.bytebuffer.print.SimplePrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.IConstantPoolReference;
import org.jasm.item.constantpool.Utf8Info;
import org.jasm.item.modifier.InnerClassModifier;
import org.jasm.item.utils.IdentifierUtils;
import org.jasm.parser.literals.Keyword;
import org.jasm.parser.literals.SymbolReference;

public class InnerClass extends AbstractByteCodeItem implements IConstantPoolReference {
	
	private SymbolReference innerClassReference;
	private int innerClassIndex = -1;
	private ClassInfo innerClass = null;
	private SymbolReference outerClassReference;
	private int outerClassIndex = -1;
	private ClassInfo outerClass = null;
	private SymbolReference innerNameReference;
	private int innerNameIndex = -1;
	private Utf8Info innerName = null;
	private List<Keyword> modifierLiterals;
	private InnerClassModifier modifier;
	
	public InnerClass() {
		modifierLiterals = new ArrayList<>();
	}
	
	public InnerClass(ClassInfo innerClass, ClassInfo outerClass, Utf8Info innerName, InnerClassModifier modifier) {
		this.innerClass = innerClass;
		this.outerClass = outerClass;
		this.innerName = innerName;
		this.modifier = modifier;
	}
	

	@Override
	public void read(IByteBuffer source, long offset) {
		innerClassIndex = source.readUnsignedShort(offset);
		outerClassIndex = source.readUnsignedShort(offset+2);
		innerNameIndex = source.readUnsignedShort(offset+4);
		modifier = new InnerClassModifier(source.readUnsignedShort(offset+6));
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedShort(offset, innerClass.getIndexInPool());
		if (outerClass == null) {
			target.writeUnsignedShort(offset+2, 0);
		} else {
			target.writeUnsignedShort(offset+2, outerClass.getIndexInPool());
		}
		if (innerName == null) {
			target.writeUnsignedShort(offset+4, 0);
		} else {
			target.writeUnsignedShort(offset+4, innerName.getIndexInPool());
		}
		target.writeUnsignedShort(offset+6, modifier.getValue());
	}

	@Override
	public int getLength() {
		return 8;
	}

	@Override
	public boolean isStructure() {
		return true;
	}

	@Override
	public List<IPrintable> getStructureParts() {
		List<IPrintable> result = new ArrayList<>();
		result.add(new SimplePrintable(null, "inner", innerClass.getSymbolName(), innerClass.getPrintComment()));
		if (outerClass != null) {
			result.add(new SimplePrintable(null, "outer", outerClass.getSymbolName(), outerClass.getPrintComment()));
		} 
		if (innerName != null) {
			result.add(new SimplePrintable(null, "name", innerName.getSymbolName(), innerName.getValue()));
		} 
		if (!modifier.hasNoFlags()) {
			result.add(new SimplePrintable(null, "modifier", modifier.toString(), null));
		}
		return result;
	}

	@Override
	public String getPrintLabel() {
		return null;
	}

	@Override
	public String getPrintName() {
		return "inner class";
	}
	
	@Override
	public String getTypeLabel() {
		return  getPrintName();
	}

	@Override
	public String getPrintArgs() {
		return null;
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	protected void doResolve() {
		this.innerClass = (ClassInfo)this.getConstantPool().get(innerClassIndex-1);
		if (outerClassIndex != 0) {
			this.outerClass = (ClassInfo)this.getConstantPool().get(outerClassIndex-1);
		}
		if (innerNameIndex != 0) {
			this.innerName = (Utf8Info)this.getConstantPool().get(innerNameIndex-1);
		}
	}
	
	@Override
	protected void doResolveAfterParse() {
		if (this.innerClassReference != null) {
			this.innerClass = getConstantPool().checkAndLoadFromSymbolTable(this,ClassInfo.class, innerClassReference);
		} else {
			emitError(null, "missing inner statement");
		}
		if (this.outerClassReference != null) {
			this.outerClass = getConstantPool().checkAndLoadFromSymbolTable(this,ClassInfo.class, outerClassReference);
		} 
		if (this.innerNameReference != null) {
			this.innerName = getConstantPool().checkAndLoadFromSymbolTable(this,Utf8Info.class, innerNameReference);
			if (this.innerName != null) {
				verifyName(innerNameReference, innerName);
			}
		}
		
		
		if (!this.hasResolveErrors()) {
			modifier = new InnerClassModifier(0);
			for (Keyword kw: modifierLiterals) {
				modifier.setFlag(kw.getKeyword());
			}
		}
	}

	public ClassInfo getInnerClass() {
		return innerClass;
	}
	
	public String getInnerClassName() {
		return innerClass.getClassName();
	}

	public ClassInfo getOuterClass() {
		return outerClass;
	}
	
	public String getOuterClassName() {
		if (outerClass == null) {
			return null;
		}
		return outerClass.getClassName();
	}

	public Utf8Info getInnerName() {
		
		return innerName;
	}
	
	public String getInnerNameValue() {
		if (innerName == null) {
			return null;
		}
		return innerName.getValue();
	}

	public InnerClassModifier getModifier() {
		return modifier;
	}

	@Override
	public AbstractConstantPoolEntry[] getConstantReferences() {
		ArrayList<AbstractConstantPoolEntry> result = new ArrayList<>();
		result.add(innerClass);
		if (innerName != null) {
			result.add(innerName);
		}
		if (outerClass != null) {
			result.add(outerClass);
		}
		AbstractConstantPoolEntry[] result1 = new AbstractConstantPoolEntry[result.size()];
		for (int i=0;i<result.size(); i++) {
			result1[i] = result.get(i);
		}
		
		return result1;
	}
	

	public List<Keyword> getModifierLiterals() {
		return modifierLiterals;
	}

	public void setInnerClassReference(SymbolReference innerClassReference) {
		this.innerClassReference = innerClassReference;
	}

	public void setOuterClassReference(SymbolReference outerClassReference) {
		this.outerClassReference = outerClassReference;
	}

	public void setInnerNameReference(SymbolReference innerNameReference) {
		this.innerNameReference = innerNameReference;
	}

	public SymbolReference getInnerClassReference() {
		return innerClassReference;
	}

	public SymbolReference getOuterClassReference() {
		return outerClassReference;
	}

	public SymbolReference getInnerNameReference() {
		return innerNameReference;
	}
	
	private void verifyName(SymbolReference ref,Utf8Info name) {
		IdentifierUtils.checkIdentifier(this, ref, name);
	}
	
	
	
	

}
