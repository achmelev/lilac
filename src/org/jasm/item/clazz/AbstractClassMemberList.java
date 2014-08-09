package org.jasm.item.clazz;



import java.util.ArrayList;
import java.util.List;

import org.jasm.item.AbstractBytecodeItemList;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.map.KeyToListMap;

public abstract class AbstractClassMemberList<T extends AbstractClassMember> extends AbstractBytecodeItemList<T>  {
	
	private KeyToListMap<String, T> nameToMember = new KeyToListMap<String, T>(); 
	private KeyToListMap<String, T> descriptorToMember = new KeyToListMap<String, T>(); 
	
	@Override
	protected void doResolve() {
		super.doResolve();
		
		
	}
	
	



	@Override
	protected void doUpdateMetadata() {
		nameToMember.clear();
		descriptorToMember.clear();
		for (IBytecodeItem item: getItems()) {
			if (item != null) {
				T member = (T)item;
				addToIndex(member);
			}
		}
	}

	private void addToIndex(T member) {
		nameToMember.addToList(member.getName().getValue(), member);
		descriptorToMember.addToList(member.getDescriptor().getValue(), member);
	}
	
	
	protected T getMember(String name, String descriptor) {
		List<T> values1 = (name != null)?nameToMember.get(name):null;
		List<T> values2 = (descriptor != null)?descriptorToMember.get(descriptor):null;
		
		List<List<T>> ll = new ArrayList<>();
		if (values1 != null) {
			ll.add(values1);
		}
		if (values2 != null) {
			ll.add(values2);
		}
		
		if (ll.size() == 0) {
			return null;
		}
		List<T> scan = ll.get(0);
		ll.remove(0);
		List<T> candidates = new ArrayList<>(); 
		for (T entry: scan) {
			boolean toAdd = true;
			for (List<T> l:ll) {
				toAdd = toAdd && l.contains(entry);
			}
			if (toAdd) {
				candidates.add(entry);
			}
			
		}
		
		if (candidates.size() ==  0) {
			return null;
		} else if (candidates.size() > 1) {
			throw new IllegalArgumentException("more than one entry found!");
		} else {
			T entry = candidates.get(0);
			return entry;
		}
	}

}
