package org.jasm.disassembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jasm.item.utils.IdentifierUtils;

public class NameGenerator {
	
	private Set<String> names = new HashSet<String>();
	private static Set<String> keywords = new HashSet<String>(); 
	
	static {
		keywords.add("aaload");
		keywords.add("aastore");
		keywords.add("abstract");
		keywords.add("aconst_null");
		keywords.add("all");
		keywords.add("aload");
		keywords.add("anewarray");
		keywords.add("annotation");
		keywords.add("areturn");
		keywords.add("argument");
		keywords.add("array");
		keywords.add("arraylength");
		keywords.add("astore");
		keywords.add("at");
		keywords.add("athrow");
		keywords.add("attribute");
		keywords.add("baload");
		keywords.add("bastore");
		keywords.add("bipush");
		keywords.add("boolean");
		keywords.add("bootstrap");
		keywords.add("bound");
		keywords.add("bridge");
		keywords.add("byte");
		keywords.add("caload");
		keywords.add("cast");
		keywords.add("castore");
		keywords.add("catch");
		keywords.add("char");
		keywords.add("checkcast");
		keywords.add("class");
		keywords.add("classref");
		keywords.add("code");
		keywords.add("const");
		keywords.add("constant");
		keywords.add("constructor");
		keywords.add("d2f");
		keywords.add("d2i");
		keywords.add("d2l");
		keywords.add("dadd");
		keywords.add("daload");
		keywords.add("dastore");
		keywords.add("dcmpg");
		keywords.add("dcmpl");
		keywords.add("dconst_0");
		keywords.add("dconst_1");
		keywords.add("ddiv");
		keywords.add("debug");
		keywords.add("default");
		keywords.add("deprecated");
		keywords.add("descriptor");
		keywords.add("dload");
		keywords.add("dmul");
		keywords.add("dneg");
		keywords.add("double");
		keywords.add("drem");
		keywords.add("dreturn");
		keywords.add("dstore");
		keywords.add("dsub");
		keywords.add("dup");
		keywords.add("dup2");
		keywords.add("dup2_x1");
		keywords.add("dup2_x2");
		keywords.add("dup_x1");
		keywords.add("dup_x2");
		keywords.add("dynref");
		keywords.add("element");
		keywords.add("enclosing");
		keywords.add("enum");
		keywords.add("extends");
		keywords.add("f2d");
		keywords.add("f2i");
		keywords.add("f2l");
		keywords.add("fadd");
		keywords.add("faload");
		keywords.add("fastore");
		keywords.add("fcmpg");
		keywords.add("fcmpl");
		keywords.add("fconst_0");
		keywords.add("fconst_1");
		keywords.add("fconst_2");
		keywords.add("fdiv");
		keywords.add("field");
		keywords.add("fieldref");
		keywords.add("file");
		keywords.add("final");
		keywords.add("fload");
		keywords.add("float");
		keywords.add("fmul");
		keywords.add("fneg");
		keywords.add("formal");
		keywords.add("frem");
		keywords.add("freturn");
		keywords.add("fstore");
		keywords.add("fsub");
		keywords.add("getfield");
		keywords.add("getstatic");
		keywords.add("go");
		keywords.add("goto");
		keywords.add("goto_w");
		keywords.add("i2b");
		keywords.add("i2c");
		keywords.add("i2d");
		keywords.add("i2f");
		keywords.add("i2l");
		keywords.add("i2s");
		keywords.add("iadd");
		keywords.add("iaload");
		keywords.add("iand");
		keywords.add("iastore");
		keywords.add("iconst_0");
		keywords.add("iconst_1");
		keywords.add("iconst_2");
		keywords.add("iconst_3");
		keywords.add("iconst_4");
		keywords.add("iconst_5");
		keywords.add("iconst_m1");
		keywords.add("idiv");
		keywords.add("if_acmpeq");
		keywords.add("if_acmpne");
		keywords.add("if_icmpeq");
		keywords.add("if_icmpge");
		keywords.add("if_icmpgt");
		keywords.add("if_icmple");
		keywords.add("if_icmplt");
		keywords.add("if_icmpne");
		keywords.add("ifeq");
		keywords.add("ifge");
		keywords.add("ifgt");
		keywords.add("ifle");
		keywords.add("iflt");
		keywords.add("ifne");
		keywords.add("ifnonnull");
		keywords.add("ifnull");
		keywords.add("iinc");
		keywords.add("iload");
		keywords.add("implements");
		keywords.add("imul");
		keywords.add("index");
		keywords.add("ineg");
		keywords.add("inner");
		keywords.add("instanceof");
		keywords.add("int");
		keywords.add("interface");
		keywords.add("intfmethodref");
		keywords.add("invisible");
		keywords.add("invokedynamic");
		keywords.add("invokeinterface");
		keywords.add("invokespecial");
		keywords.add("invokestatic");
		keywords.add("invokevirtual");
		keywords.add("ior");
		keywords.add("irem");
		keywords.add("ireturn");
		keywords.add("ishl");
		keywords.add("ishr");
		keywords.add("istore");
		keywords.add("isub");
		keywords.add("iushr");
		keywords.add("ixor");
		keywords.add("jsr");
		keywords.add("jsr_w");
		keywords.add("l2d");
		keywords.add("l2f");
		keywords.add("l2i");
		keywords.add("ladd");
		keywords.add("laload");
		keywords.add("land");
		keywords.add("lastore");
		keywords.add("lcmp");
		keywords.add("lconst_0");
		keywords.add("lconst_1");
		keywords.add("ldc");
		keywords.add("ldc2_w");
		keywords.add("ldc_w");
		keywords.add("ldiv");
		keywords.add("line");
		keywords.add("lload");
		keywords.add("lmul");
		keywords.add("lneg");
		keywords.add("long");
		keywords.add("lookupswitch");
		keywords.add("lor");
		keywords.add("lrem");
		keywords.add("lreturn");
		keywords.add("lshl");
		keywords.add("lshr");
		keywords.add("lstore");
		keywords.add("lsub");
		keywords.add("lushr");
		keywords.add("lxor");
		keywords.add("maxlocals");
		keywords.add("maxstack");
		keywords.add("method");
		keywords.add("methodhandle");
		keywords.add("methodref");
		keywords.add("methodtype");
		keywords.add("monitorenter");
		keywords.add("monitorexit");
		keywords.add("multianewarray");
		keywords.add("name");
		keywords.add("nameandtype");
		keywords.add("native");
		keywords.add("nested");
		keywords.add("new");
		keywords.add("newarray");
		keywords.add("newinvokespecial");
		keywords.add("nop");
		keywords.add("normal");
		keywords.add("numbers");
		keywords.add("object");
		keywords.add("outer");
		keywords.add("parameter");
		keywords.add("path");
		keywords.add("pop");
		keywords.add("pop2");
		keywords.add("private");
		keywords.add("protected");
		keywords.add("public");
		keywords.add("putfield");
		keywords.add("putstatic");
		keywords.add("receiver");
		keywords.add("reference");
		keywords.add("resource");
		keywords.add("ret");
		keywords.add("return");
		keywords.add("returnadress");
		keywords.add("saload");
		keywords.add("sastore");
		keywords.add("short");
		keywords.add("signature");
		keywords.add("sipush");
		keywords.add("source");
		keywords.add("stackmap");
		keywords.add("static");
		keywords.add("strict");
		keywords.add("string");
		keywords.add("super");
		keywords.add("supertype");
		keywords.add("swap");
		keywords.add("synchronized");
		keywords.add("synthetic");
		keywords.add("tableswitch");
		keywords.add("target");
		keywords.add("targeted");
		keywords.add("targets");
		keywords.add("throws");
		keywords.add("to");
		keywords.add("transient");
		keywords.add("try");
		keywords.add("type");
		keywords.add("types");
		keywords.add("unknown");
		keywords.add("utf8");
		keywords.add("value");
		keywords.add("var");
		keywords.add("varargs");
		keywords.add("vars");
		keywords.add("version");
		keywords.add("volatile");
		keywords.add("wide");
	}
	
	public String generateName(String candidate) {
		candidate = candidate.trim();
		String result = candidate;
		if (!IdentifierUtils.isValidIdentifier(candidate)) {
			return null;
		}
		if (names.contains(candidate) || keywords.contains(candidate) || candidate.equals("NaN")) {
			int counter = 0;
			result = candidate+"$"+counter;
			while (names.contains(result)) {
				counter++;
				result = candidate+"$"+counter;
			}
		} else {
			result = candidate;
		}
		names.add(result);
		return result;
	}
	
	public static void main(String[] args) {
		File f = new File("grammar/JavaAssembler.g4");
		
		List<String> list = new ArrayList<String>();
		try {
			LineNumberReader reader = new LineNumberReader(new FileReader(f));
			String line = reader.readLine();
			boolean readKeywords = false;
			
			while (line != null) {
				line = line.trim();
				
				if (line.length()> 0) {
					if (readKeywords && !line.startsWith("//stopKeywords")) {
						String value;
						try {
							value = line.substring(line.indexOf('\'')+1, line.lastIndexOf('\''));
						} catch (Throwable e) {
							throw new RuntimeException(line,e);
						}
						list.add(value);
					} else if (line.startsWith("//startKeywords")) {
						readKeywords = true;
					} else if (line.startsWith("//stopKeywords")) {
						readKeywords = false;
					} 
				} 
				
				line = reader.readLine();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Collections.sort(list);
		for (String v: list) {
			System.out.println("keywords.add(\""+v+"\");");
		}
	}

}
