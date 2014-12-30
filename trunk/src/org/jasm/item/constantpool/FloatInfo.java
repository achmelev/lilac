package org.jasm.item.constantpool;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.parser.literals.FloatLiteral;
import org.jasm.type.verifier.VerifierParams;

public class FloatInfo extends AbstractConstantPoolEntry implements IPrimitiveValueReferencingEntry {
	
	private Float value = null;
	private FloatLiteral valueLiteral;
	
	public FloatInfo() {
		
	}

	@Override
	public short getTag() {
		return 4;
	}

	@Override
	public int getLength() {
		return 5;
	}

	@Override
	protected void doResolve() {
		

	}
	
	
	
	@Override
	protected void doVerify(VerifierParams params) {
		
		
	}

	@Override
	protected void doResolveAfterParse() {
		if (valueLiteral.isValid()) {
			value = new Float(valueLiteral.getValue());
		} else {
			emitError(valueLiteral, "malformed float or float out of bounds");
		}
	}

	@Override
	public void readBody(IByteBuffer source, long offset) {
		value = source.readFloat(offset);

	}

	@Override
	public void writeBody(IByteBuffer target, long offset) {
		target.writeFloat(offset, value);
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	
	@Override
	public String getConstTypeLabel() {
		return  "float";
	}

	@Override
	public String getPrintArgs() {
		if (Float.isNaN(value)) {
			return "NaN";
		} else if (value == Float.NEGATIVE_INFINITY) {
			return "-Infinity";
		} else if (value == Float.POSITIVE_INFINITY) {
			return "Infinity";
		} else {
			return FloatLiteral.createExactHexLiteral(value);
		}
	}

	@Override
	public String getPrintComment() {
		if (Float.isNaN(value)) {
			return null;
		} else if (value == Float.NEGATIVE_INFINITY) {
			return null;
		} else if (value == Float.POSITIVE_INFINITY) {
			return null;
		} else {
			return value.toString();
		}
	}

	public FloatLiteral getValueLiteral() {
		return valueLiteral;
	}

	public void setValueLiteral(FloatLiteral valueLiteral) {
		this.valueLiteral = valueLiteral;
	}

	@Override
	protected String doGetDisassemblerLabel() {
		return null;
	}
	
	

}
