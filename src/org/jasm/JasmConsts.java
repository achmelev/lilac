package org.jasm;

public class JasmConsts {
	public static String DEFAULT = "default";
	public static String NIL= "nil";
	public static String ALL= "all";
	
	public static char LOCAL_VARIABLE_TYPE_REFERENCE= 'a';
	public static char LOCAL_VARIABLE_TYPE_INT = 'i';
	public static char LOCAL_VARIABLE_TYPE_DOUBLE = 'd';
	public static char LOCAL_VARIABLE_TYPE_FLOAT = 'f';
	public static char LOCAL_VARIABLE_TYPE_LONG = 'l';
	public static char LOCAL_VARIABLE_TYPE_RETURNADRESS = 'r';
	
	public static short ARRAY_TYPE_BOOLEAN = 4;
	public static short ARRAY_TYPE_CHAR = 5;
	public static short ARRAY_TYPE_FLOAT = 6;
	public static short ARRAY_TYPE_DOUBLE = 7;
	public static short ARRAY_TYPE_BYTE = 8;
	public static short ARRAY_TYPE_SHORT = 9;
	public static short ARRAY_TYPE_INT = 10;
	public static short ARRAY_TYPE_LONG = 11;
	
	public static String TYPENAME_BOOLEAN="boolean"; 
	public static String TYPENAME_BYTE="byte"; 
	public static String TYPENAME_CHAR="char";
	public static String TYPENAME_DOUBLE="double";
	public static String TYPENAME_FLOAT="float";
	public static String TYPENAME_INT="int";
	public static String TYPENAME_SHORT="short";
	public static String TYPENAME_LONG="long";
	public static String TYPENAME_OBJECT="object";
	public static String TYPENAME_RETURNADRESS="returnadress";
	
	public static short ANNOTATION_TARGET_GENERIC_CLASS_TYPE_PARAMETER=0x00;
	public static short ANNOTATION_TARGET_GENERIC_METHOD_TYPE_PARAMETER=0x01;
	public static short ANNOTATION_TARGET_SUPERTYPE=0x10;
	public static short ANNOTATION_TARGET_GENERIC_CLASS_TYPE_PARAMETER_BOUND=0x11;
	public static short ANNOTATION_TARGET_GENERIC_METHOD_TYPE_PARAMETER_BOUND=0x12;
	public static short ANNOTATION_TARGET_FIELD=0x13;
	public static short ANNOTATION_TARGET_RETURN_TYPE=0x14;
	public static short ANNOTATION_TARGET_RECEIVER_TYPE=0x15;
	public static short ANNOTATION_TARGET_FORMAL_PARAMETER=0x16;
	public static short ANNOTATION_TARGET_THROWS=0x17;
	public static short ANNOTATION_TARGET_LOCAL_VAR=0x40;
	public static short ANNOTATION_TARGET_RESOURCE_VAR=0x41;
	public static short ANNOTATION_TARGET_EXCEPTION_PARAMETER=0x42;
	public static short ANNOTATION_TARGET_INSTANCEOF=0x43;
	public static short ANNOTATION_TARGET_NEW=0x44;
	public static short ANNOTATION_TARGET_METHOD_REF_NEW=0x45;
	public static short ANNOTATION_TARGET_METHOD_REF_ID=0x46;
	public static short ANNOTATION_TARGET_CAST=0x47;
	public static short ANNOTATION_TARGET_GENERIC_CONSTRUCTOR_TYPE_ARGUMENT=0x48;
	public static short ANNOTATION_TARGET_GENERIC_METHOD_TYPE_ARGUMENT=0x49;
	public static short ANNOTATION_TARGET_GENERIC_CONSTRUCTOR_TYPE_ARGUMENT_IN_METHOD_REF=0x4A;
	public static short ANNOTATION_TARGET_GENERIC_METHOD_TYPE_ARGUMENT_IN_METHOD_REF=0x4B;
	
	public static int ANNOTATION_TARGET_SUPERTYPE_CLASSINDEX=65535;
	
}
