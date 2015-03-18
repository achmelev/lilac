package org.jasm.item.constantpool;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.disassembler.ClassNameGenerator;
import org.jasm.disassembler.NameGenerator;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.AbstractTaggedBytecodeItemList;
import org.jasm.item.IBytecodeItem;
import org.jasm.map.KeyToListMap;
import org.jasm.parser.ISymbolTableEntry;
import org.jasm.parser.SymbolTable;
import org.jasm.parser.literals.SymbolReference;

public class ConstantPool extends AbstractTaggedBytecodeItemList<AbstractConstantPoolEntry> {
	
	
	private KeyToListMap<String,AbstractConstantPoolEntry> entriesByName = new KeyToListMap<>();
	private KeyToListMap<String,AbstractConstantPoolEntry> entriesByDescriptor = new KeyToListMap<String,AbstractConstantPoolEntry>();
	private KeyToListMap<String, AbstractConstantPoolEntry> entriesByText = new KeyToListMap<>();
	private KeyToListMap<Object, AbstractConstantPoolEntry> entriesByPrimitive = new KeyToListMap<>();
	private KeyToListMap<String, Utf8Info> utf8ByContent = new KeyToListMap<>();
	
	private KeyToListMap<AbstractConstantPoolEntry, IBytecodeItem> entryReferences = new KeyToListMap<>();
	
	private SymbolTable symbolTable = new SymbolTable(null);
	
	private NameGenerator constNameGenerator = new NameGenerator();
	private ClassNameGenerator classNameGenerator = new ClassNameGenerator();
	
	public ConstantPool() {
		super(AbstractConstantPoolEntry.class, "org.jasm.item.constantpool");
	}


	@Override
	protected int getSizeDiff() {
		return 1;
	}


	@Override
	public String getPrintName() {
		return null;
	}
	
	
	@Override
	public String getPrintComment() {
		return "Constants";
	}
	
	
	@Override
	public int getItemSizeInList(IBytecodeItem item) {
		if (item instanceof LongInfo || item instanceof DoubleInfo) {
			return 2;
		} else {
			return 1;
		}
	}

	private void addToIndex(AbstractConstantPoolEntry entry) {
		
		if (entry instanceof INameReferencingEntry) {
			INameReferencingEntry ref = (INameReferencingEntry)entry;
			for (String ref1: ref.getReferencedNames()) {
				entriesByName.addToList(ref1, entry);
			}
		}
		if (entry instanceof IDescriptorReferencingEntry) {
			IDescriptorReferencingEntry ref = (IDescriptorReferencingEntry)entry;
			for (String ref1: ref.getReferencedDescriptors()) {
				entriesByDescriptor.addToList(ref1, entry);
			}
		}
		

		if (entry instanceof ITextReferencingEntry) {
			ITextReferencingEntry ref = (ITextReferencingEntry)entry;
			if (!entriesByText.containsKey(ref.getContent())) {
				entriesByText.addToList(ref.getContent(),entry );
			} 
		}
		if (entry instanceof IPrimitiveValueReferencingEntry) {
			IPrimitiveValueReferencingEntry ref = (IPrimitiveValueReferencingEntry)entry;
			entriesByPrimitive.addToList(ref.getValue(),entry );
		}
		if (entry instanceof Utf8Info) {
			Utf8Info ref = (Utf8Info)entry;
			utf8ByContent.addToList(ref.getValue(),ref );
		}
		
		
		
	}
	
	
	
	
	//Access Methods
	
	public List<AbstractConstantPoolEntry> getNameReferences(String name) {
		return entriesByName.get(name);
	}
	
	public List<AbstractConstantPoolEntry> getSignatureReferences(String signature) {
		return entriesByDescriptor.get(signature);
	}
	
	
	public List<StringInfo> getStringEntries(String text) {
		List<AbstractConstantPoolEntry> entries = entriesByText.get(text);
		List<StringInfo> result = new ArrayList<>();
		for (AbstractConstantPoolEntry entry: entries) {
			result.add((StringInfo) entry);
		}
		return result;
	}
	
	public List<IntegerInfo> getIntegerEntries(int value) {
		List<AbstractConstantPoolEntry> entries = entriesByPrimitive.get(value);
		List<IntegerInfo> result = new ArrayList<>();
		for (AbstractConstantPoolEntry entry: entries) {
			result.add((IntegerInfo) entry);
		}
		return result;
	}
	
	public List<FloatInfo> getFloatEntries(float value) {
		List<AbstractConstantPoolEntry> entries = entriesByPrimitive.get(value);
		List<FloatInfo> result = new ArrayList<>();
		for (AbstractConstantPoolEntry entry: entries) {
			result.add((FloatInfo) entry);
		}
		return result;
	}
	
	public List<DoubleInfo> getDoubleEntries(double value) {
		List<AbstractConstantPoolEntry> entries = entriesByPrimitive.get(value);
		List<DoubleInfo> result = new ArrayList<>();
		for (AbstractConstantPoolEntry entry: entries) {
			result.add((DoubleInfo) entry);
		}
		return result;
	}
	
	public List<LongInfo> getLongEntries(long value) {
		List<AbstractConstantPoolEntry> entries = entriesByPrimitive.get(value);
		List<LongInfo> result = new ArrayList<>();
		for (AbstractConstantPoolEntry entry: entries) {
			result.add((LongInfo) entry);
		}
		return result;
	}
	
	
	
	public List<Utf8Info> getUtf8Infos(String text) {
		return utf8ByContent.get(text);
	}
	
	public List<ClassInfo> getClassInfos(String className) {
		return getRefs(ClassInfo.class, className, null, null);
	}
	
	public List<MethodrefInfo> getMethodRefs(String className, String name, String signature) {
		return getRefs(MethodrefInfo.class, className, name, signature);
	}
	
	public List<FieldrefInfo> getFieldRefs(String className, String name, String signature) {
		return getRefs(FieldrefInfo.class, className, name, signature);
	}
	
	public List<InterfaceMethodrefInfo> getInterfaceMethodRef(String className, String name, String signature) {
		return getRefs(InterfaceMethodrefInfo.class, className, name, signature);
	}
	
	public List<NameAndTypeInfo> getNameAndTypeInfos(String name, String signature) {
		return getRefs(NameAndTypeInfo.class, null, name, signature);
	}
	
	public List<MethodHandleInfo> getMethodHandleInfo(String className, String name, String signature) {
		return getRefs(MethodHandleInfo.class, className, name, signature);
	}
	
	public List<MethodTypeInfo> getMethodTypeInfo(String signature) {
		return getRefs(MethodTypeInfo.class, null, null, signature);
	}
	
	public List<IBytecodeItem> getReferencingItems(List<AbstractConstantPoolEntry> cps) {
		
		List<IBytecodeItem> result = new ArrayList<>();
		for (AbstractConstantPoolEntry cp: cps) {
			if (!getItems().contains(cp)) {
				throw new IllegalArgumentException("Entry insn't in pool: "+cp);
			}
			result.addAll(entryReferences.get(cp));
		}
		
		return result;
	}
	
	public <T> List<T> getReferencingItems(AbstractConstantPoolEntry entry, Class<T> clazz) {
		List<T> result = new ArrayList<T>();
		List<IBytecodeItem> all = entryReferences.get(entry);
		if (all != null) {
			for (IBytecodeItem item: all) {
				if (clazz.isAssignableFrom(item.getClass())) {
					result.add((T)item);
				}
				if (item instanceof AbstractConstantPoolEntry) {
					result.addAll(getReferencingItems((AbstractConstantPoolEntry)item, clazz));
				}
				
			}
		}
		return result;
	}
	
	
	
	private <T extends AbstractConstantPoolEntry> List<T> getRefs(Class<T> clazz, String className, String name, String signature) {
		List<AbstractConstantPoolEntry> values1 = (name != null)?entriesByName.get(name):null;
		List<AbstractConstantPoolEntry> values2 = (className != null)?entriesByName.get(className):null;
		List<AbstractConstantPoolEntry> values3 = (signature != null)?entriesByDescriptor.get(signature):null;
		
		List<List<AbstractConstantPoolEntry>> ll = new ArrayList<>();
		if (values1 != null) {
			ll.add(values1);
		}
		if (values2 != null) {
			ll.add(values2);
		}
		if (values3 != null) {
			ll.add(values3);
		}
		if (ll.size() == 0) {
			return null;
		}
		List<AbstractConstantPoolEntry> scan = ll.get(0);
		ll.remove(0);
		List<T> candidates = new ArrayList<>(); 
		for (AbstractConstantPoolEntry entry: scan) {
			boolean toAdd = true;
			if (entry.getClass().equals(clazz)) {
				for (List<AbstractConstantPoolEntry> l:ll) {
					toAdd = toAdd && l.contains(entry);
				}
				if (toAdd) {
					candidates.add((T)entry);
				}
			}
		}
		
		return candidates;
	}

	@Override
	public ConstantPool getConstantPool() {
		return this;
	}
	
	public SymbolTable getSymbolTable() {
		return symbolTable;
	}


	@Override
	public void doUpdateMetadata() {
		
		updateIndexes();
		
		entryReferences.clear();
		
		if (!isAfterParse()) {
			List<IBytecodeItem> allItems = ((AbstractByteCodeItem)getParent()).getAllItemsFromHere();
			for (IBytecodeItem item: allItems) {
				if (item instanceof IConstantPoolReference) {
					IConstantPoolReference cr = (IConstantPoolReference)item;
					AbstractConstantPoolEntry[] refs = cr.getConstantReferences();
					for (AbstractConstantPoolEntry r:refs) {
						if (!getItems().contains(r)) {
							throw new IllegalStateException(r+" isn't in pool, from "+item);
						}
						entryReferences.addToList(r, item);
					}
				}
			}
		}
	}
	
	public void updateIndexes() {
		entriesByName.clear();
		entriesByPrimitive.clear();
		entriesByText.clear();
		entriesByDescriptor.clear();
		utf8ByContent.clear();
		for (AbstractConstantPoolEntry entry: getItems()) {
			if (entry != null && !(entry instanceof InvokeDynamicInfo) && !entry.hasErrors()) {
				addToIndex(entry);
			}
		}
		
	}
	
	public void updateInvokeDynamicIndexes() {
		for (AbstractConstantPoolEntry entry: getItems()) {
			if (entry != null && (entry instanceof InvokeDynamicInfo) && !entry.hasErrors()) {
				addToIndex(entry);
			}
		}
		
	}
	
	public <T extends AbstractConstantPoolEntry> T checkAndLoadFromSymbolTable(AbstractByteCodeItem caller, Class<T> t, SymbolReference ref) {
		T result = null;
		if (getSymbolTable().contains(ref.getSymbolName())) {
			ISymbolTableEntry entry = getSymbolTable().get(ref.getSymbolName());
			if (entry.getClass().equals(t)) {
				result = (T)entry;
			} else {
				if (caller != null) {
					caller.emitError(ref, "wrong constant pool entry, expected "+getConstLabel(t));
				}
			}
		} else {
			if (caller != null) {
				caller.emitError(ref, "unknown constant "+ref.getSymbolName());
			}
		}
		return result;
	}
	
	public <T extends AbstractConstantPoolEntry> AbstractConstantPoolEntry checkAndLoadFromSymbolTable(AbstractByteCodeItem caller,Class<T> []t, SymbolReference ref) {
		AbstractConstantPoolEntry result = null;
		if (getSymbolTable().contains(ref.getSymbolName())) {
			ISymbolTableEntry entry = getSymbolTable().get(ref.getSymbolName());
			for (int i=0;i<t.length; i++) {
				if (entry.getClass().equals(t[i])) {
					result = (AbstractConstantPoolEntry)entry;
				} 
			}
			if (result == null) {
				StringBuffer buf = new StringBuffer();
				for (int i=0;i<t.length; i++) {
					if (i>0) {
						buf.append(",");
					}
					buf.append(getConstLabel(t[i]));
					
				}
				if (caller != null) {
					caller.emitError(ref, "wrong constant pool entry, expected one of the following: "+buf.toString());
				}
			}
			
		} else {
			if (caller != null) {
				caller.emitError(ref, "unknown constant "+ref.getSymbolName());
			}
		}
		return result;
	}
	
	private <T extends AbstractConstantPoolEntry> String getConstLabel(Class<T> t) {
		try {
			Constructor<T> con = t.getConstructor(new Class[]{});
			T inst = con.newInstance(new Object[]{});
			return inst.getConstTypeLabel();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	


	@Override
	protected void doResolve() {
		for (IBytecodeItem item: getItems()) {
			if (item != null && !(item instanceof InvokeDynamicInfo)) {
				item.setParent(this);
				item.resolve();
			}
		}
	}
	
	public void createDisassemblerLabels() {
		classNameGenerator.setThisClassName(this.getRoot().getThisClass().getClassName());
		for (IBytecodeItem item: getItems()) {
			if (item != null && (item instanceof ClassInfo)) {
				ClassInfo cli = (ClassInfo)item;
				String label = classNameGenerator.createDisassemblerClassName(cli.getClassName());
				if (label != null) {
					label = constNameGenerator.generateName(label);
					cli.setDisassemblerLabel(label);
				}
				
			}
		}
	}
	
	
	
	@Override
	protected void doVerify() {
		for (IBytecodeItem item: getItems()) {
			if (item != null) {
				item.verify();
			}
		}
		
	}


	public void resolveInvokeDynamics() {
		for (IBytecodeItem item: getItems()) {
			if (item != null && (item instanceof InvokeDynamicInfo)) {
				item.setParent(this);
				item.resolve();
			}
		}
	}


	@Override
	protected void doResolveAfterParse() {
		doResolve();
		for (AbstractConstantPoolEntry item: getItems()) {
			if ((item instanceof AbstractReferenceEntry) && !item.hasErrors()) {
				((AbstractReferenceEntry)item).verifyReferences();
			}
		}
	}


	public NameGenerator getConstNameGenerator() {
		return constNameGenerator;
	}


	public ClassNameGenerator getClassNameGenerator() {
		return classNameGenerator;
	}
	
	//getOrAdd methods
	
	
	public Utf8Info getOrAddUtf8nfo(String text) {
		if (getUtf8Infos(text).size() > 0) {
			return getUtf8Infos(text).get(0);
		} else {
			Utf8Info info = new Utf8Info();
			info.setValue(text);
			addGeneratedEntry(info);
			return info;
			
		}
	}
	
	public ClassInfo  getOrAddClassInfo(String name) {
		List<AbstractConstantPoolEntry> entries = entriesByName.get(name);
		for (AbstractConstantPoolEntry entry:entries) {
			if (entry instanceof ClassInfo) {
				return (ClassInfo)entry;
			}
		}
		ClassInfo result = new ClassInfo();
		Utf8Info nameUtf8 = getOrAddUtf8nfo(name);
		result.setReference(new AbstractConstantPoolEntry[]{nameUtf8}, false);
		addGeneratedEntry(result);
		
		
		return result;
		
		
	}
	
	private void addGeneratedEntry(AbstractConstantPoolEntry info) {
		info.setParent(this);
		info.setResolved(true);
		add(info);
		addToIndex(info);
	}
	
	
	

}
