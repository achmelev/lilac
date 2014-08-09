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

public class ConstantPool extends AbstractTaggedBytecodeItemList<AbstractConstantPoolEntry> {
	
	//TODO - InvokeDynamic
	
	private KeyToListMap<String,AbstractConstantPoolEntry> entriesByName = new KeyToListMap<>();
	private KeyToListMap<String,AbstractConstantPoolEntry> entriesByDescriptor = new KeyToListMap<String,AbstractConstantPoolEntry>();
	private Map<String, AbstractConstantPoolEntry> entriesByText = new HashMap<>();
	private Map<Object, AbstractConstantPoolEntry> entriesByPrimitive = new HashMap<>();
	private Map<String, Utf8Info> utf8ByContent = new HashMap<>();
	
	private KeyToListMap<AbstractConstantPoolEntry, IBytecodeItem> entryReferences = new KeyToListMap<>();
	
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
				entriesByText.put(ref.getContent(),entry );
			} else {
				throw new IllegalArgumentException("There is already an entry containing: "+ref.getContent());
			}
		}
		if (entry instanceof IPrimitiveValueReferencingEntry) {
			IPrimitiveValueReferencingEntry ref = (IPrimitiveValueReferencingEntry)entry;
			if (!entriesByPrimitive.containsKey(ref.getValue())) {
				entriesByPrimitive.put(ref.getValue(),entry );
			} else {
				throw new IllegalArgumentException("There is already an entry containing: "+ref.getValue());
			}
		}
		if (entry instanceof Utf8Info) {
			Utf8Info ref = (Utf8Info)entry;
			if (!utf8ByContent.containsKey(ref.getValue())) {
				utf8ByContent.put(ref.getValue(),ref );
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
	
	
	public StringInfo getStringEntry(String text) {
		return (StringInfo)entriesByText.get(text);
	}
	
	public IntegerInfo getIntegerEntry(int value) {
		return (IntegerInfo)entriesByText.get(value);
	}
	
	public FloatInfo getFloatEntry(float value) {
		return (FloatInfo)entriesByPrimitive.get(value);
	}
	
	public LongInfo getLongEntry(long value) {
		return (LongInfo)entriesByPrimitive.get(value);
	}
	
	public DoubleInfo getDoubleEntry(double value) {
		return (DoubleInfo)entriesByPrimitive.get(value);
	}
	
	public Utf8Info getUtf8Info(String text) {
		return utf8ByContent.get(text);
	}
	
	public ClassInfo getClassInfo(String className) {
		return getRef(ClassInfo.class, className, null, null);
	}
	
	public MethodrefInfo getMethodRef(String className, String name, String signature) {
		return getRef(MethodrefInfo.class, className, name, signature);
	}
	
	public FieldrefInfo getFieldRef(String className, String name, String signature) {
		return getRef(FieldrefInfo.class, className, name, signature);
	}
	
	public InterfaceMethodrefInfo getInterfaceMethodRef(String className, String name, String signature) {
		return getRef(InterfaceMethodrefInfo.class, className, name, signature);
	}
	
	public NameAndTypeInfo getNameAndTypeInfo(String name, String signature) {
		return getRef(NameAndTypeInfo.class, null, name, signature);
	}
	
	public MethodHandleInfo getMethodHandleInfo(String className, String name, String signature) {
		return getRef(MethodHandleInfo.class, className, name, signature);
	}
	
	public MethodTypeInfo getMethodTypeInfo(String signature) {
		return getRef(MethodTypeInfo.class, null, null, signature);
	}
	
	public List<IBytecodeItem> getReferencingItems(AbstractConstantPoolEntry cp) {
		if (!getItems().contains(cp)) {
			throw new IllegalArgumentException("Entry insn't in pool: "+cp);
		}
		return entryReferences.get(cp);
	}
	
	private <T extends AbstractConstantPoolEntry> T getRef(Class<T> clazz, String className, String name, String signature) {
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
		List<AbstractConstantPoolEntry> candidates = new ArrayList<>(); 
		for (AbstractConstantPoolEntry entry: scan) {
			boolean toAdd = true;
			if (entry.getClass().equals(clazz)) {
				for (List<AbstractConstantPoolEntry> l:ll) {
					toAdd = toAdd && l.contains(entry);
				}
				if (toAdd) {
					candidates.add(entry);
				}
			}
		}
		
		if (candidates.size() ==  0) {
			return null;
		} else if (candidates.size() > 1) {
			throw new IllegalArgumentException("more than one entry found!");
		} else {
			AbstractConstantPoolEntry entry = candidates.get(0);
			return (T)entry;
		}
	}

	@Override
	public ConstantPool getConstantPool() {
		return this;
	}
	


	@Override
	public void doUpdateMetadata() {
		entriesByName.clear();
		entriesByPrimitive.clear();
		entriesByText.clear();
		entriesByDescriptor.clear();
		utf8ByContent.clear();
		for (AbstractConstantPoolEntry entry: getItems()) {
			addToIndex(entry);
		}
		
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
	
	
	
	
	

}
