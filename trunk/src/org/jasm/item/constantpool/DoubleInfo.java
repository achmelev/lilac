package org.jasm.item.constantpool;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.parser.literals.DoubleLiteral;

public class DoubleInfo extends AbstractConstantPoolEntry implements IPrimitiveValueReferencingEntry {
	
	private Double value = null;
	private DoubleLiteral valueLiteral = null;
	
	public DoubleInfo() {
		
	}
	
	public DoubleInfo(double value) {
		this.value = value;
	}

	@Override
	public short getTag() {
		return 6;
	}

	@Override
	public int getLength() {
		return 9;
	}

	@Override
	protected void doResolve() {
		

	}
	
	@Override
	protected void doResolveAfterParse() {
		if (valueLiteral.isValid()) {
			value = new Double(valueLiteral.getValue());
		} else {
			emitError(valueLiteral, "malformed double or double out of bounds");
		}
	}

	@Override
	public void readBody(IByteBuffer source, long offset) {
		value = source.readDouble(offset);

	}

	@Override
	public void writeBody(IByteBuffer target, long offset) {
		target.writeDouble(offset, value);
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	
	@Override
	public String getTypeLabel() {
		return  "const double";
	}

	@Override
	public String getPrintArgs() {
		if (Double.isNaN(value)) {
			return "NaN";
		} else if (value == Double.NEGATIVE_INFINITY) {
			return "-Infinity";
		} else if (value == Double.POSITIVE_INFINITY) {
			return "Infinity";
		} else {
			return DoubleLiteral.createExactHexLiteral(value);
		}
	}

	@Override
	public String getPrintComment() {
		if (Double.isNaN(value)) {
			return null;
		} else if (value == Double.NEGATIVE_INFINITY) {
			return null;
		} else if (value == Double.POSITIVE_INFINITY) {
			return null;
		} else {
			return value.toString();
		}
	}

	public DoubleLiteral getValueLiteral() {
		return valueLiteral;
	}

	public void setValueLiteral(DoubleLiteral valueLiteral) {
		this.valueLiteral = valueLiteral;
	}
	
	
	

}
