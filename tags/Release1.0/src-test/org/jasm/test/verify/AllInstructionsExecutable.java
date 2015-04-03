package org.jasm.test.verify;

import org.jasm.test.playground.IExecutable;
import org.jasm.test.playground.bean.IntB;

public class AllInstructionsExecutable implements IExecutable {
	
	
	private boolean boolField;
	private byte byteField;
	private short shortField;
	private int intField;
	private long longField;
	private float floatField;
	private double doubleField;
	private char charField;
	private IntB objectField;
	
	private boolean[] boolArrayField;
	private byte[] byteArrayField;
	private short[] shortArrayField;
	private int[] intArrayField;
	private long[] longArrayField;
	private float[] floatArrayField;
	private double[] doubleArrayField;
	private char[] charArrayField;
	private IntB[] objectArrayField;
	
	private int [] [] intMultiArrayField;
	
	public AllInstructionsExecutable() {
		boolField = true;
		byteField = 1;
		shortField = 1;
		intField = 1;
		longField = 1;
		floatField = 1;
		doubleField = 1.0;
		charField = 'c';
		objectField = new IntB();
		
		boolArrayField = new boolean[10];
		byteArrayField = new byte[10];
		shortArrayField = new short[10];
		intArrayField = new int[10];
		longArrayField = new long[10];
		floatArrayField = new float[10];
		doubleArrayField = new double[10];
		charArrayField = new char[10];
		objectArrayField = new IntB[10];
		
		intMultiArrayField = new int[10] [10];
	}
	
	

	@Override
	public void execute() {
		
		
	}

}
