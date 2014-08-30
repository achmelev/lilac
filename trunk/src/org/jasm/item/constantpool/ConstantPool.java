package org.jasm.item.constantpool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.AbstractTaggedBytecodeItemList;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.IContainerBytecodeItem;
import org.jasm.item.clazz.Clazz;
import org.jasm.map.KeyToListMap;
import org.jasm.parser.ISymbolTableEntry;
import org.jasm.parser.SymbolTable;
import org.jasm.parser.literals.SymbolReference;

public class ConstantPool extends AbstractTaggedBytecodeItemList<AbstractConstantPoolEntry> {
	
	//TODO - InvokeDynamic
	
	private KeyToListMap<String,AbstractConstantPoolEntry> entriesByName = new KeyToListMap<>();
	private KeyToListMap<String,AbstractConstantPoolEntry> entriesByDescriptor = new KeyToListMap<String,AbstractConstantPoolEntry>();
	private KeyToListMap<String, AbstractConstantPoolEntry> entriesByText = new KeyToListMap<>();
	private KeyToListMap<Object, AbstractConstantPoolEntry> entriesByPrimitive = new KeyToListMap<>();
	private KeyToListMap<String, Utf8Info> utf8ByContent = new KeyToListMap<>();
	
	private KeyToListMap<AbstractConstantPoolEntry, IBytecodeItem> entryReferences = new KeyToListMap<>();
	
	private SymbolTable symbolTable = new SymbolTable(null);
	
	public ConstantPool() {
		super(AbstractConstantPoolEntry.class, "org.jasm.item.constantpool");
	}


	@Override
	protected int getSizeDiff() {
		return 1;
	}


	@Override
	public String getPrintName() {
		return "constpool";
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
			} else {
				throw new IllegalArgumentException("There is already an entry containing: "+ref.getContent());
			}
		}
		if (entry instanceof IPrimitiveValueReferencingEntry) {
			IPrimitiveValueReferencingEntry ref = (IPrimitiveValueReferencingEntry)entry;
			if (!entriesByPrimitive.containsKey(ref.getValue())) {
				entriesByPrimitive.addToList(ref.getValue(),entry );
			} else {
				throw new IllegalArgumentException("There is already an entry containing: "+ref.getValue());
			}
		}
		if (entry instanceof Utf8Info) {
			Utf8Info ref = (Utf8Info)entry;
			if (!utf8ByContent.containsKey(ref.getValue())) {
				utf8ByContent.addToList(ref.getValue(),ref );
			} else {
				throw new IllegalArgumentException("There is already an entry containing: "+ref.getValue());
			}
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
	
	public void updateIndexes() {
		entriesByName.clear();
		entriesByPrimitive.clear();
		entriesByText.clear();
		entriesByDescriptor.clear();
		utf8ByContent.clear();
		for (AbstractConstantPoolEntry entry: getItems()) {
			if (entry != null && !entry.hasResolveErrors()) {
				addToIndex(entry);
			}
		}
		
	}
	
	public <T extends AbstractConstantPoolEntry> T checkAndLoadFromSymbolTable(Class<T> t, SymbolReference ref, String printLabel) {
		T result = null;
		if (getSymbolTable().contains(ref.getSymbolName())) {
			ISymbolTableEntry entry = getSymbolTable().get(ref.getSymbolName());
			if (entry.getClass().equals(t)) {
				result = (T)entry;
			} else {
				emitError(ref, "wrong constant pool entry, expected "+printLabel);
			}
		} else {
			emitError(ref, "unknown constant label "+ref.getSymbolName());
		}
		return result;
	}


	@Override
	protected void doResolveAfterParse() {
		super.doResolveAfterParse();
		for (AbstractConstantPoolEntry item: getItems()) {
			if (item instanceof AbstractReferenceEntry) {
				((AbstractReferenceEntry)item).verifyReferences();
			}
		}
	}
	
	
	
	
	

}
