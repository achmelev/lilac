package org.jasm.item.attribute;

import java.util.ArrayList;
import java.util.List;

import org.jasm.JasmConsts;
import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.IContainerBytecodeItem;
import org.jasm.item.instructions.AbstractInstruction;

public class LocalVariableAnnotationTargetType extends AbstractAnnotationTargetType implements IContainerBytecodeItem<LocalVariableAnnotationTargetTypeMember>{
	
	private List<LocalVariableAnnotationTargetTypeMember> members = new ArrayList<LocalVariableAnnotationTargetTypeMember>();
	
	public LocalVariableAnnotationTargetType() {
		
	}
	
	public LocalVariableAnnotationTargetType(short type) {
		super(type);
	}
	
	@Override
	public void read(IByteBuffer source, long offset) {
		targetType = source.readUnsignedByte(offset);
		int size = source.readUnsignedShort(offset+1);
		long currentOffset = offset+3;
		for (int i=0;i<size; i++) {
			LocalVariableAnnotationTargetTypeMember member = new LocalVariableAnnotationTargetTypeMember();
			member.read(source, currentOffset);
			member.setParent(this);
			members.add(member);
			currentOffset+=member.getLength();
		}
		
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		target.writeUnsignedByte(offset, targetType);
		target.writeUnsignedShort(offset+1, members.size());
		long currentOffset = offset+3;
		for (LocalVariableAnnotationTargetTypeMember member: members) {
			member.write(target, currentOffset);
			currentOffset+=member.getLength();
		}
		
	}

	@Override
	public int getLength() {
		return 3+6*members.size();
	}

	@Override
	public String getTypeLabel() {
		return null;
	}

	@Override
	public boolean isStructure() {
		return true;
	}

	@Override
	public List<IPrintable> getStructureParts() {
		List<IPrintable> parts = new ArrayList<IPrintable>();
		for (LocalVariableAnnotationTargetTypeMember member: members) {
			parts.add(member);
		}
		return parts;
	}

	@Override
	public String getPrintLabel() {
		return null;
	}

	@Override
	public String getPrintName() {
		if (targetType == JasmConsts.ANNOTATION_TARGET_LOCAL_VAR) {
			return "targets var types";
		} else {
			return "targets resource var types";
		}
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
		for (LocalVariableAnnotationTargetTypeMember member: members) {
			member.resolve();
		}
		
	}

	@Override
	protected void doResolveAfterParse() {
		if (isInCode()) {
			for (LocalVariableAnnotationTargetTypeMember member: members) {
				member.resolve();
			}
		} else {
			emitIllegalInContextError();
		}
		
	}

	@Override
	public int getSize() {
		return members.size();
	}

	@Override
	public LocalVariableAnnotationTargetTypeMember get(int index) {
		return members.get(index);
	}

	@Override
	public int indexOf(LocalVariableAnnotationTargetTypeMember item) {
		return members.indexOf(item);
	}

	@Override
	public int getItemSizeInList(IBytecodeItem item) {
		return 1;
	}
	
	public void addMember(LocalVariableAnnotationTargetTypeMember lvm) {
		lvm.setParent(this);
		members.add(lvm);
	}

}
