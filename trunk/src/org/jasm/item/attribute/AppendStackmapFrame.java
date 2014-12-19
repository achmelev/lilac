package org.jasm.item.attribute;

import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.IContainerBytecodeItem;

public class AppendStackmapFrame extends AbstractStackmapFrame implements IContainerBytecodeItem<AbstractStackmapVariableinfo> {
	
	
	private AbstractStackmapVariableinfo[] infos;
	
	public AppendStackmapFrame() {
		super((short)252);
	}

	@Override
	public int getLength() {
		return 1+2+calculateVariableInfosLength(infos);
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
		return "append";
	}

	@Override
	public String getPrintArgs() {
		return getInstruction().getPrintLabel()+", "+createItemsListArg(infos);
	}

	@Override
	public String getPrintComment() {
		return null;
	}

	@Override
	protected void doReadBody(IByteBuffer source, long offset) {
		deltaOffset = source.readUnsignedShort(offset+1);
		infos = readVariableInfos(this, source, offset+3, tagValue-251);
		
	}

	@Override
	protected void doWriteBody(IByteBuffer target, long offset) {
		target.writeUnsignedShort(offset+1, calculateDeltaOffset());
		writeVariableInfos(target, offset+3, infos);
	}

	@Override
	protected void doResolveBody() {
		for (AbstractStackmapVariableinfo info: infos) {
			info.resolve();
		}
		
	}

	@Override
	protected void doResolveBodyAfterParse() {
		for (AbstractStackmapVariableinfo info: infos) {
			info.resolve();
		}
		
	}

	@Override
	protected boolean offsetCodedInTag() {
		return false;
	}

	@Override
	protected short calculateTag(short tagRangeBegin) {
		return (short)(251+infos.length);
	}

	@Override
	public int getSize() {
		return infos.length;
	}

	@Override
	public AbstractStackmapVariableinfo get(int index) {
		return infos[index];
	}

	@Override
	public int indexOf(AbstractStackmapVariableinfo item) {
		for (int i=0;i<infos.length; i++) {
			if (infos[i] == item) {
				return i;
			}
		}
		
		return -1;
	}

	@Override
	public int getItemSizeInList(IBytecodeItem item) {
		return 1;
	}

}
