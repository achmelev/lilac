package org.jasm.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.attribute.AbstractAnnotationsAttributeContent;
import org.jasm.item.attribute.AbstractParameterAnnotationsAttributeContent;
import org.jasm.item.attribute.Annotation;
import org.jasm.item.attribute.AnnotationElementNameValue;
import org.jasm.item.attribute.AnnotationElementValue;
import org.jasm.item.attribute.Attribute;
import org.jasm.item.attribute.CodeAttributeContent;
import org.jasm.item.attribute.ConstantValueAttributeContent;
import org.jasm.item.attribute.DeprecatedAttributeContent;
import org.jasm.item.attribute.EnclosingMethodAttributeContent;
import org.jasm.item.attribute.ExceptionsAttributeContent;
import org.jasm.item.attribute.IAttributeContent;
import org.jasm.item.attribute.InnerClass;
import org.jasm.item.attribute.InnerClassesAttributeContent;
import org.jasm.item.attribute.RuntimeInvisibleAnnotationsAttributeContent;
import org.jasm.item.attribute.RuntimeInvisibleParameterAnnotationsAttributeContent;
import org.jasm.item.attribute.RuntimeVisibleAnnotationsAttributeContent;
import org.jasm.item.attribute.RuntimeVisibleParameterAnnotationsAttributeContent;
import org.jasm.item.attribute.SignatureAttributeContent;
import org.jasm.item.attribute.SourceFileAttributeContent;
import org.jasm.item.attribute.SynteticAttributeContent;
import org.jasm.item.clazz.Clazz;
import org.jasm.item.clazz.Field;
import org.jasm.item.clazz.IAttributesContainer;
import org.jasm.item.clazz.Method;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.ConstantPool;
import org.jasm.item.constantpool.DoubleInfo;
import org.jasm.item.constantpool.FieldrefInfo;
import org.jasm.item.constantpool.FloatInfo;
import org.jasm.item.constantpool.IntegerInfo;
import org.jasm.item.constantpool.InterfaceMethodrefInfo;
import org.jasm.item.constantpool.LongInfo;
import org.jasm.item.constantpool.MethodrefInfo;
import org.jasm.item.constantpool.NameAndTypeInfo;
import org.jasm.item.constantpool.StringInfo;
import org.jasm.item.constantpool.Utf8Info;
import org.jasm.item.instructions.AbstractInstruction;
import org.jasm.item.instructions.AbstractPushInstruction;
import org.jasm.item.instructions.AbstractSwitchInstruction;
import org.jasm.item.instructions.ArgumentLessInstruction;
import org.jasm.item.instructions.BipushInstruction;
import org.jasm.item.instructions.BranchInstruction;
import org.jasm.item.instructions.ConstantPoolInstruction;
import org.jasm.item.instructions.IincInstruction;
import org.jasm.item.instructions.Instructions;
import org.jasm.item.instructions.InvokeInterfaceInstruction;
import org.jasm.item.instructions.LdcInstruction;
import org.jasm.item.instructions.LocalVariable;
import org.jasm.item.instructions.LocalVariableInstruction;
import org.jasm.item.instructions.LookupSwitchInstruction;
import org.jasm.item.instructions.MultianewarrayInstruction;
import org.jasm.item.instructions.NewarrayInstruction;
import org.jasm.item.instructions.OpCodes;
import org.jasm.item.instructions.SipushInstruction;
import org.jasm.item.instructions.TableSwitchInstruction;
import org.jasm.item.modifier.ClassModifier;
import org.jasm.parser.JavaAssemblerParser.AnnotationContext;
import org.jasm.parser.JavaAssemblerParser.AnnotationdeclarationContext;
import org.jasm.parser.JavaAssemblerParser.AnnotationelementContext;
import org.jasm.parser.JavaAssemblerParser.AnnotationelementnameContext;
import org.jasm.parser.JavaAssemblerParser.AnnotationelementvalueContext;
import org.jasm.parser.JavaAssemblerParser.AnnotationindexContext;
import org.jasm.parser.JavaAssemblerParser.AnnotationtypeContext;
import org.jasm.parser.JavaAssemblerParser.ArgumentlessopContext;
import org.jasm.parser.JavaAssemblerParser.ArrayannotationelementvalueContext;
import org.jasm.parser.JavaAssemblerParser.BranchopContext;
import org.jasm.parser.JavaAssemblerParser.ClassInnerClassContext;
import org.jasm.parser.JavaAssemblerParser.ClassattributeSourceFileContext;
import org.jasm.parser.JavaAssemblerParser.ClassinfoContext;
import org.jasm.parser.JavaAssemblerParser.ClassmodifierAbstractContext;
import org.jasm.parser.JavaAssemblerParser.ClassmodifierAnnotationContext;
import org.jasm.parser.JavaAssemblerParser.ClassmodifierContext;
import org.jasm.parser.JavaAssemblerParser.ClassmodifierEnumContext;
import org.jasm.parser.JavaAssemblerParser.ClassmodifierFinalContext;
import org.jasm.parser.JavaAssemblerParser.ClassmodifierInterfaceContext;
import org.jasm.parser.JavaAssemblerParser.ClassmodifierPublicContext;
import org.jasm.parser.JavaAssemblerParser.ClassmodifierSuperContext;
import org.jasm.parser.JavaAssemblerParser.ClassmodifierSynteticContext;
import org.jasm.parser.JavaAssemblerParser.ClassnameContext;
import org.jasm.parser.JavaAssemblerParser.ClazzContext;
import org.jasm.parser.JavaAssemblerParser.ConstantpoolopContext;
import org.jasm.parser.JavaAssemblerParser.DeprecatedattributeContext;
import org.jasm.parser.JavaAssemblerParser.DoubleinfoContext;
import org.jasm.parser.JavaAssemblerParser.EnclosingmethodContext;
import org.jasm.parser.JavaAssemblerParser.EnumannotationelementvalueContext;
import org.jasm.parser.JavaAssemblerParser.FieldContext;
import org.jasm.parser.JavaAssemblerParser.FieldattributeConstantValueContext;
import org.jasm.parser.JavaAssemblerParser.FielddescriptorContext;
import org.jasm.parser.JavaAssemblerParser.FieldmodifierEnumContext;
import org.jasm.parser.JavaAssemblerParser.FieldmodifierFinalContext;
import org.jasm.parser.JavaAssemblerParser.FieldmodifierPrivateContext;
import org.jasm.parser.JavaAssemblerParser.FieldmodifierProtectedContext;
import org.jasm.parser.JavaAssemblerParser.FieldmodifierPublicContext;
import org.jasm.parser.JavaAssemblerParser.FieldmodifierStaticContext;
import org.jasm.parser.JavaAssemblerParser.FieldmodifierSynteticContext;
import org.jasm.parser.JavaAssemblerParser.FieldmodifierTransientContext;
import org.jasm.parser.JavaAssemblerParser.FieldmodifierVolatileContext;
import org.jasm.parser.JavaAssemblerParser.FieldnameContext;
import org.jasm.parser.JavaAssemblerParser.FieldrefinfoContext;
import org.jasm.parser.JavaAssemblerParser.FloatinfoContext;
import org.jasm.parser.JavaAssemblerParser.IincopContext;
import org.jasm.parser.JavaAssemblerParser.InnerclassContext;
import org.jasm.parser.JavaAssemblerParser.Innerclass_innerContext;
import org.jasm.parser.JavaAssemblerParser.Innerclass_nameContext;
import org.jasm.parser.JavaAssemblerParser.Innerclass_outerContext;
import org.jasm.parser.JavaAssemblerParser.InnerclassmodifierAbstractContext;
import org.jasm.parser.JavaAssemblerParser.InnerclassmodifierAnnotationContext;
import org.jasm.parser.JavaAssemblerParser.InnerclassmodifierEnumContext;
import org.jasm.parser.JavaAssemblerParser.InnerclassmodifierFinalContext;
import org.jasm.parser.JavaAssemblerParser.InnerclassmodifierInterfaceContext;
import org.jasm.parser.JavaAssemblerParser.InnerclassmodifierPrivateContext;
import org.jasm.parser.JavaAssemblerParser.InnerclassmodifierProtectedContext;
import org.jasm.parser.JavaAssemblerParser.InnerclassmodifierPublicContext;
import org.jasm.parser.JavaAssemblerParser.InnerclassmodifierStaticContext;
import org.jasm.parser.JavaAssemblerParser.InnerclassmodifierSynteticContext;
import org.jasm.parser.JavaAssemblerParser.IntegerinfoContext;
import org.jasm.parser.JavaAssemblerParser.InterfacemethodrefinfoContext;
import org.jasm.parser.JavaAssemblerParser.InterfacesContext;
import org.jasm.parser.JavaAssemblerParser.LocalvaropContext;
import org.jasm.parser.JavaAssemblerParser.LonginfoContext;
import org.jasm.parser.JavaAssemblerParser.MethodAttributeExceptionsContext;
import org.jasm.parser.JavaAssemblerParser.MethodContext;
import org.jasm.parser.JavaAssemblerParser.MethoddescriptorContext;
import org.jasm.parser.JavaAssemblerParser.MethodinstructionContext;
import org.jasm.parser.JavaAssemblerParser.MethodmodifierAbstractContext;
import org.jasm.parser.JavaAssemblerParser.MethodmodifierBridgeContext;
import org.jasm.parser.JavaAssemblerParser.MethodmodifierContext;
import org.jasm.parser.JavaAssemblerParser.MethodmodifierFinalContext;
import org.jasm.parser.JavaAssemblerParser.MethodmodifierNativeContext;
import org.jasm.parser.JavaAssemblerParser.MethodmodifierPrivateContext;
import org.jasm.parser.JavaAssemblerParser.MethodmodifierProtectedContext;
import org.jasm.parser.JavaAssemblerParser.MethodmodifierPublicContext;
import org.jasm.parser.JavaAssemblerParser.MethodmodifierStaticContext;
import org.jasm.parser.JavaAssemblerParser.MethodmodifierStrictContext;
import org.jasm.parser.JavaAssemblerParser.MethodmodifierSynchronizedContext;
import org.jasm.parser.JavaAssemblerParser.MethodmodifierSynteticContext;
import org.jasm.parser.JavaAssemblerParser.MethodmodifierVarargsContext;
import org.jasm.parser.JavaAssemblerParser.MethodnameContext;
import org.jasm.parser.JavaAssemblerParser.MethodrefinfoContext;
import org.jasm.parser.JavaAssemblerParser.MethodvarabsoluteContext;
import org.jasm.parser.JavaAssemblerParser.MethodvarimplicitContext;
import org.jasm.parser.JavaAssemblerParser.MethodvarrelativeContext;
import org.jasm.parser.JavaAssemblerParser.MultinewarrayopContext;
import org.jasm.parser.JavaAssemblerParser.NameandtypeinfoContext;
import org.jasm.parser.JavaAssemblerParser.NewarrayopContext;
import org.jasm.parser.JavaAssemblerParser.PushopContext;
import org.jasm.parser.JavaAssemblerParser.SignatureattributeContext;
import org.jasm.parser.JavaAssemblerParser.SimpleannotationelementvalueContext;
import org.jasm.parser.JavaAssemblerParser.StringinfoContext;
import org.jasm.parser.JavaAssemblerParser.SuperclassContext;
import org.jasm.parser.JavaAssemblerParser.SwitchMemberContext;
import org.jasm.parser.JavaAssemblerParser.SwitchopContext;
import org.jasm.parser.JavaAssemblerParser.SynteticattributeContext;
import org.jasm.parser.JavaAssemblerParser.Utf8infoContext;
import org.jasm.parser.JavaAssemblerParser.VersionContext;
import org.jasm.parser.literals.DoubleLiteral;
import org.jasm.parser.literals.FloatLiteral;
import org.jasm.parser.literals.IntegerLiteral;
import org.jasm.parser.literals.Keyword;
import org.jasm.parser.literals.Label;
import org.jasm.parser.literals.LongLiteral;
import org.jasm.parser.literals.StringLiteral;
import org.jasm.parser.literals.SymbolReference;
import org.jasm.parser.literals.VersionLiteral;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class AssemblerParser  extends JavaAssemblerBaseListener {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private List<ErrorMessage> errorMessages = new ArrayList<>();
	
	private Clazz result = null;
	
	Stack<IBytecodeItem> stack = new Stack<>();

	public List<ErrorMessage> getErrorMessages() {
		return errorMessages;
	}
	
	
	public Clazz parse(InputStream inp) {
		
		result = null;
		errorMessages.clear();
		stack.clear();
		
		ANTLRInputStream input = null;
		try {
			input = new ANTLRInputStream(inp);
		} catch (IOException e) {
			throw new RuntimeException("Error creating antlr stream",e);
		}
		
		SyntaxErrorListener errorListener = new SyntaxErrorListener(this);
		JavaAssemblerLexer lexer = new JavaAssemblerLexer(input);
		lexer.removeErrorListeners();
		lexer.addErrorListener(errorListener);
		/*for (Token tok: lexer.getAllTokens()) {
			log.debug(tok.getType() +" "+tok.getText());
		}*/
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		JavaAssemblerParser parser = new JavaAssemblerParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(errorListener);
		if (log.isDebugEnabled()) {
			//parser.setTrace(true);
		}
		
		//Parse
		ParseTree tree = parser.clazz();
		
		if (log.isDebugEnabled()) {
			log.debug("tree: "+tree.toStringTree());
		}
		
		if (errorMessages.size() == 0) {
			//Walk tree an create class
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(this, tree);
			if (errorMessages.size() == 0) {
				Clazz clazz = result;
				clazz.resolve();
				if (clazz.getParser().getErrorMessages().size() == 0) {
					clazz.updateMetadata();
				}
				
			} else {
				result = null;
			}
		}
		
		
		
		return result;
	}
	
	public void debugErrors() {
		if (log.isDebugEnabled()) {
			if (getErrorMessages().size() > 0) {
				StringBuffer buf = new StringBuffer();
				for (ErrorMessage erm: getErrorMessages()) {
					buf.append(erm.toString());
					buf.append("\n");
				}
				log.debug("\nsyntax error messages:\n"+buf.toString());
			}
		}
	}


	@Override
	public void enterClazz(ClazzContext ctx) {
		Clazz clazz = new Clazz();
		clazz.setSourceLocation(createSourceLocation(ctx.CLASS()));
		clazz.setModifier(new ClassModifier(0));
		clazz.setParser(this);
		stack.push(clazz);
	}


	@Override
	public void exitClazz(ClazzContext ctx) {
		result = (Clazz)stack.pop();
	}


	@Override
	public void enterVersion(VersionContext ctx) {
		Clazz clazz = (Clazz)stack.peek();
		if (clazz.getVersion() == null) {
			clazz.setVersion(createVersionLiteral(ctx.VersionLiteral()));
		} else {
			emitError(ctx.VERSION(), "multiple class version statements");
		}
	}
	
	
	
	@Override
	public void enterClassname(ClassnameContext ctx) {
		Clazz clazz = (Clazz)stack.peek();
		if (clazz.getThisClassSymbol() == null) {
			clazz.setThisClassSymbol(createSymbolReference(ctx.Identifier()));
		} else {
			emitError(ctx.NAME(), "multiple class name statements");
		}
		
	}
	

	@Override
	public void enterSuperclass(SuperclassContext ctx) {
		Clazz clazz = (Clazz)stack.peek();
		if (clazz.getSuperClass() == null) {
			clazz.setSuperClassSymbol(createSymbolReference(ctx.Identifier()));
		} else {
			emitError(ctx.EXTENDS(), "multiple super class statements");
		}
		
	}
	
	


	@Override
	public void enterInterfaces(InterfacesContext ctx) {
		Clazz clazz = (Clazz)stack.peek();
		if (clazz.getInterfaceSymbols() == null) {
			List<TerminalNode> nodes = ctx.Identifier();
			List<SymbolReference> symbols = new ArrayList<>();
			for (TerminalNode node: nodes) {
				symbols.add(createSymbolReference(node));
			}
			clazz.setInterfaceSymbols(symbols);
		} else {
			emitError(ctx.IMPLEMENTS(), "multiple interfaces statements");
		}
	}


	@Override
	public void enterClassmodifier(ClassmodifierContext ctx) {
		Clazz clazz = (Clazz)stack.peek();
		if (clazz.getModifierLiterals().size() > 0) {
			emitError(ctx.MODIFIER(), "multiple modifier statements");
		}
	}


	@Override
	public void enterClassmodifierEnum(ClassmodifierEnumContext ctx) {
		Clazz clazz = (Clazz)stack.peek();
		clazz.getModifierLiterals().add(createKeyword(ctx.ENUM()));
	}


	@Override
	public void enterClassmodifierInterface(ClassmodifierInterfaceContext ctx) {
		Clazz clazz = (Clazz)stack.peek();
		clazz.getModifierLiterals().add(createKeyword(ctx.INTERFACE()));
	}


	@Override
	public void enterClassmodifierPublic(ClassmodifierPublicContext ctx) {
		Clazz clazz = (Clazz)stack.peek();
		clazz.getModifierLiterals().add(createKeyword(ctx.PUBLIC()));
	}


	@Override
	public void enterClassmodifierAnnotation(ClassmodifierAnnotationContext ctx) {
		Clazz clazz = (Clazz)stack.peek();
		clazz.getModifierLiterals().add(createKeyword(ctx.ANNOTATION()));
	}
	


	@Override
	public void enterClassmodifierAbstract(ClassmodifierAbstractContext ctx) {
		Clazz clazz = (Clazz)stack.peek();
		clazz.getModifierLiterals().add(createKeyword(ctx.ABSTRACT()));
	}
	
	

	@Override
	public void enterClassmodifierFinal(ClassmodifierFinalContext ctx) {
		Clazz clazz = (Clazz)stack.peek();
		clazz.getModifierLiterals().add(createKeyword(ctx.FINAL()));
	}


	@Override
	public void enterClassmodifierSyntetic(ClassmodifierSynteticContext ctx) {
		Clazz clazz = (Clazz)stack.peek();
		clazz.getModifierLiterals().add(createKeyword(ctx.SYNTETIC()));
	}


	@Override
	public void enterClassmodifierSuper(ClassmodifierSuperContext ctx) {
		Clazz clazz = (Clazz)stack.peek();
		clazz.getModifierLiterals().add(createKeyword(ctx.SUPER()));
	}
	
	
	
	@Override
	public void enterClassinfo(ClassinfoContext ctx) {
		ClassInfo entry = new ClassInfo();
		if (ctx.label() != null) {
			entry.setLabel(createLabel(ctx.label().Identifier()));
		}
		entry.setSourceLocation(createSourceLocation(ctx.CLASSINFO()));
		entry.setReferenceLabels(new SymbolReference[]{createSymbolReference(ctx.Identifier())});
		addConstantPoolEntry(entry);
	}
	
	

	@Override
	public void enterStringinfo(StringinfoContext ctx) {
		StringInfo entry = new StringInfo();
		if (ctx.label() != null) {
			entry.setLabel(createLabel(ctx.label().Identifier()));
		}
		entry.setSourceLocation(createSourceLocation(ctx.STRING()));
		entry.setReferenceLabels(new SymbolReference[]{createSymbolReference(ctx.Identifier())});
		addConstantPoolEntry(entry);
	}


	@Override
	public void enterUtf8info(Utf8infoContext ctx) {
		Utf8Info entry = new Utf8Info();
		if (ctx.label() != null) {
			entry.setLabel(createLabel(ctx.label().Identifier()));
		}
		entry.setSourceLocation(createSourceLocation(ctx.UTF8INFO()));
		entry.setValueLiteral(createStringLiteral(ctx.StringLiteral()));
		addConstantPoolEntry(entry);
	}
	


	@Override
	public void enterMethodrefinfo(MethodrefinfoContext ctx) {
		MethodrefInfo entry = new MethodrefInfo();
		if (ctx.label() != null) {
			entry.setLabel(createLabel(ctx.label().Identifier()));
		}
		entry.setSourceLocation(createSourceLocation(ctx.METHODREFINFO()));
		entry.setReferenceLabels(new SymbolReference[]{createSymbolReference(ctx.Identifier(0)),createSymbolReference(ctx.Identifier(1))});
		addConstantPoolEntry(entry);
	}
	
	


	@Override
	public void enterInterfacemethodrefinfo(InterfacemethodrefinfoContext ctx) {
		InterfaceMethodrefInfo entry = new InterfaceMethodrefInfo();
		if (ctx.label() != null) {
			entry.setLabel(createLabel(ctx.label().Identifier()));
		}
		entry.setSourceLocation(createSourceLocation(ctx.INTERFACEMETHODREFINFO()));
		entry.setReferenceLabels(new SymbolReference[]{createSymbolReference(ctx.Identifier(0)),createSymbolReference(ctx.Identifier(1))});
		addConstantPoolEntry(entry);
	}


	@Override
	public void enterNameandtypeinfo(NameandtypeinfoContext ctx) {
		NameAndTypeInfo entry = new NameAndTypeInfo();
		if (ctx.label() != null) {
			entry.setLabel(createLabel(ctx.label().Identifier()));
		}
		entry.setSourceLocation(createSourceLocation(ctx.NAMEANDTYPEINFO()));
		entry.setReferenceLabels(new SymbolReference[]{createSymbolReference(ctx.Identifier(0)),createSymbolReference(ctx.Identifier(1))});
		addConstantPoolEntry(entry);
	}
	
	

	@Override
	public void enterFieldrefinfo(FieldrefinfoContext ctx) {
		FieldrefInfo entry = new FieldrefInfo();
		if (ctx.label() != null) {
			entry.setLabel(createLabel(ctx.label().Identifier()));
		}
		entry.setSourceLocation(createSourceLocation(ctx.FIELDREFINFO()));
		entry.setReferenceLabels(new SymbolReference[]{createSymbolReference(ctx.Identifier(0)),createSymbolReference(ctx.Identifier(1))});
		addConstantPoolEntry(entry);
		
	}
	
	


	@Override
	public void enterIntegerinfo(IntegerinfoContext ctx) {
		IntegerInfo entry = new IntegerInfo();
		if (ctx.label() != null) {
			entry.setLabel(createLabel(ctx.label().Identifier()));
		}
		entry.setSourceLocation(createSourceLocation(ctx.INT()));
		entry.setValueLiteral(createIntegerLiteral(ctx.IntegerLiteral()));
		addConstantPoolEntry(entry);
	}
	
	
	
	

	@Override
	public void enterLonginfo(LonginfoContext ctx) {
		LongInfo entry = new LongInfo();
		if (ctx.label() != null) {
			entry.setLabel(createLabel(ctx.label().Identifier()));
		}
		entry.setSourceLocation(createSourceLocation(ctx.LONG()));
		entry.setValueLiteral(createLongLiteral(ctx.IntegerLiteral()));
		addConstantPoolEntry(entry);
	}


	@Override
	public void enterFloatinfo(FloatinfoContext ctx) {
		FloatInfo entry = new FloatInfo();
		if (ctx.label() != null) {
			entry.setLabel(createLabel(ctx.label().Identifier()));
		}
		entry.setSourceLocation(createSourceLocation(ctx.FLOAT()));
		entry.setValueLiteral(createFloatLiteral(ctx.FloatingPointLiteral()));
		addConstantPoolEntry(entry);
	}
	
	


	@Override
	public void enterDoubleinfo(DoubleinfoContext ctx) {
		DoubleInfo entry = new DoubleInfo();
		if (ctx.label() != null) {
			entry.setLabel(createLabel(ctx.label().Identifier()));
		}
		entry.setSourceLocation(createSourceLocation(ctx.DOUBLE()));
		entry.setValueLiteral(createDoubleLiteral(ctx.FloatingPointLiteral()));
		addConstantPoolEntry(entry);
	}


	@Override
	public void enterClassattributeSourceFile(
			ClassattributeSourceFileContext ctx) {
		SourceFileAttributeContent content = new SourceFileAttributeContent();
		content.setValueLabel(createSymbolReference(ctx.Identifier()));
		addAttribute(content, ctx.SOURCE());
	}
	


	@Override
	public void enterMethodmodifierStatic(MethodmodifierStaticContext ctx) {
		Method m = (Method)stack.peek();
		m.getModifierLiterals().add(createKeyword(ctx.STATIC()));
	}


	@Override
	public void enterMethodmodifierSynchronized(
			MethodmodifierSynchronizedContext ctx) {
		Method m = (Method)stack.peek();
		m.getModifierLiterals().add(createKeyword(ctx.SYNCHRONIZED()));
	}


	@Override
	public void enterMethodmodifierPrivate(MethodmodifierPrivateContext ctx) {
		Method m = (Method)stack.peek();
		m.getModifierLiterals().add(createKeyword(ctx.PRIVATE()));
	}


	@Override
	public void enterMethodmodifierAbstract(MethodmodifierAbstractContext ctx) {
		Method m = (Method)stack.peek();
		m.getModifierLiterals().add(createKeyword(ctx.ABSTRACT()));
	}


	@Override
	public void enterMethodmodifierNative(MethodmodifierNativeContext ctx) {
		Method m = (Method)stack.peek();
		m.getModifierLiterals().add(createKeyword(ctx.NATIVE()));
	}


	@Override
	public void enterMethodmodifierVarargs(MethodmodifierVarargsContext ctx) {
		Method m = (Method)stack.peek();
		m.getModifierLiterals().add(createKeyword(ctx.VARARGS()));
	}


	@Override
	public void enterMethodmodifierProtected(MethodmodifierProtectedContext ctx) {
		Method m = (Method)stack.peek();
		m.getModifierLiterals().add(createKeyword(ctx.PROTECTED()));
	}


	@Override
	public void enterMethodmodifierStrict(MethodmodifierStrictContext ctx) {
		Method m = (Method)stack.peek();
		m.getModifierLiterals().add(createKeyword(ctx.STRICT()));
	}
	
	
	

	
	

	

	@Override
	public void enterMethodmodifier(MethodmodifierContext ctx) {
		Method m = (Method)stack.peek();
		if (m.getModifierLiterals().size() > 0) {
			emitError(ctx.MODIFIER(), "multiple modifier statements within the same method statement");
		}
	}


	@Override
	public void enterMethoddescriptor(MethoddescriptorContext ctx) {
		Method m = (Method)stack.peek();
		if (m.getDescriptorReference() == null) {
			m.setDescriptorReference(createSymbolReference(ctx.Identifier()));
		} else {
			emitError(ctx.DESCRIPTOR(), "multiple descriptor statements within the same method statement");
		}
		
	}
	
	
	
	@Override
	public void enterMethodmodifierPublic(MethodmodifierPublicContext ctx) {
		Method m = (Method)stack.peek();
		m.getModifierLiterals().add(createKeyword(ctx.PUBLIC()));
	}


	@Override
	public void enterMethodmodifierSyntetic(MethodmodifierSynteticContext ctx) {
		Method m = (Method)stack.peek();
		m.getModifierLiterals().add(createKeyword(ctx.SYNTETIC()));
	}


	@Override
	public void enterMethodmodifierFinal(MethodmodifierFinalContext ctx) {
		Method m = (Method)stack.peek();
		m.getModifierLiterals().add(createKeyword(ctx.FINAL()));
	}
	
	@Override
	public void enterMethodmodifierBridge(MethodmodifierBridgeContext ctx) {
		Method m = (Method)stack.peek();
		m.getModifierLiterals().add(createKeyword(ctx.BRIDGE()));
	}



	@Override
	public void enterMethodname(MethodnameContext ctx) {
		Method m = (Method)stack.peek();
		if (m.getNameReference() == null) {
			m.setNameReference(createSymbolReference(ctx.Identifier()));
		} else {
			emitError(ctx.NAME(), "multiple method name statements within the same method statement");
		}
	}

	@Override
	public void enterMethodAttributeExceptions(
			MethodAttributeExceptionsContext ctx) {
		ExceptionsAttributeContent content = new ExceptionsAttributeContent();
		
		List<TerminalNode> nodes = ctx.Identifier();
		List<SymbolReference> symbols = new ArrayList<>();
		for (TerminalNode node: nodes) {
			symbols.add(createSymbolReference(node));
		}
		
		content.setExceptionReferences(symbols);
		addAttribute(content, ctx.THROWS());
	}

	@Override
	public void enterMethodvarimplicit(MethodvarimplicitContext ctx) {
		LocalVariable var = new LocalVariable(LocalVariable.getTypeCode(ctx.methodvartype().getText()));
		var.setName(createSymbolReference(ctx.Identifier()));
		addVar(var);
	}
	
	
	
	@Override
	public void enterMethodvarabsolute(MethodvarabsoluteContext ctx) {
		LocalVariable var = new LocalVariable(LocalVariable.getTypeCode(ctx.methodvartype().getText()));
		var.setName(createSymbolReference(ctx.Identifier()));
		IntegerLiteral lit = createIntegerLiteral(ctx.IntegerLiteral());
		if (lit.getValue()<0) {
			emitError(ctx.IntegerLiteral(), "an offset must be positive");
		} else {
			var.setOffset(lit.getValue());
			addVar(var);
		}
		
	}
	
	
	@Override
	public void enterMethodvarrelative(MethodvarrelativeContext ctx) {
		LocalVariable var = new LocalVariable(LocalVariable.getTypeCode(ctx.methodvartype().getText()));
		var.setName(createSymbolReference(ctx.Identifier(0)));
		IntegerLiteral lit = createIntegerLiteral(ctx.IntegerLiteral());
		if (lit.getValue()<0) {
			emitError(ctx.IntegerLiteral(), "an offset must be positive");
		} else {
			var.setOffset(lit.getValue());
			var.setParentName(createSymbolReference(ctx.Identifier(1)));
			addVar(var);
		}
	}
	
	


	@Override
	public void enterArgumentlessop(ArgumentlessopContext ctx) {
		String name = ctx.Argumentlessop().getText();
		Short code = OpCodes.getOpcodeForName(name);
		ArgumentLessInstruction instr = new ArgumentLessInstruction(code);
		instr.setSourceLocation(createSourceLocation(ctx.Argumentlessop()));
		setInstructionLabel(ctx, instr);
		addInstruction(instr);
	}
	
	@Override
	public void enterConstantpoolop(ConstantpoolopContext ctx) {
		String name = ctx.Constantpoolop().getText();
		if (name.equals("ldc")) {
			LdcInstruction instr = new LdcInstruction(null);
			instr.setCpEntryReference(createSymbolReference(ctx.Identifier()));
			instr.setSourceLocation(createSourceLocation(ctx.Constantpoolop()));
			setInstructionLabel(ctx, instr);
			addInstruction(instr);
		} else if (name.equals("invokeinterface")) {
			InvokeInterfaceInstruction instr = new InvokeInterfaceInstruction(OpCodes.invokeinterface, null);
			instr.setCpEntryReference(createSymbolReference(ctx.Identifier()));
			instr.setSourceLocation(createSourceLocation(ctx.Constantpoolop()));
			setInstructionLabel(ctx, instr);
			addInstruction(instr);
		} else {
			Short code = OpCodes.getOpcodeForName(name);
			ConstantPoolInstruction instr = new ConstantPoolInstruction(code, null);
			instr.setCpEntryReference(createSymbolReference(ctx.Identifier()));
			instr.setSourceLocation(createSourceLocation(ctx.Constantpoolop()));
			setInstructionLabel(ctx, instr);
			addInstruction(instr);
		}
	}
	
	


	@Override
	public void enterLocalvarop(LocalvaropContext ctx) {
		String name = ctx.Localvarop().getText();
		
		boolean wide = (ctx.wideOrNormal() != null) && ctx.wideOrNormal().getText().equals("wide");
		boolean normal = (ctx.wideOrNormal() != null) && ctx.wideOrNormal().getText().equals("normal");
		Short code = OpCodes.getOpcodeForName(name);
		
		LocalVariableInstruction instr = new LocalVariableInstruction(code, wide, -1);
		if (normal) {
			instr.setForceNormal(normal);
		}
		instr.setLocalVariableReference(createSymbolReference(ctx.Identifier()));
		instr.setSourceLocation(createSourceLocation(ctx.Localvarop()));
		setInstructionLabel(ctx, instr);
		addInstruction(instr);
	}
	
	


	@Override
	public void enterPushop(PushopContext ctx) {
		AbstractPushInstruction instr = null;
		String name = ctx.Pushop().getText();
		if (name.equals(OpCodes.getNameForOpcode(OpCodes.bipush))) {
			instr = new BipushInstruction((byte)-1);
		} else if (name.equals(OpCodes.getNameForOpcode(OpCodes.sipush))) {
			instr = new SipushInstruction((short)-1);
		} else {
			throw new IllegalStateException("unexpected insruction name: "+name);
		}
		instr.setValueLiteral(createIntegerLiteral(ctx.IntegerLiteral()));
		instr.setSourceLocation(createSourceLocation(ctx.Pushop()));
		setInstructionLabel(ctx, instr);
		addInstruction(instr);
	}
	
	@Override
	public void enterIincop(IincopContext ctx) {
		boolean isWide = ctx.WIDE() != null;
		IincInstruction instr = new IincInstruction(-1, (short)-1, isWide);
		instr.setValueLiteral(createIntegerLiteral(ctx.IntegerLiteral()));
		instr.setLocalVariableReference(createSymbolReference(ctx.Identifier()));
		instr.setSourceLocation(createSourceLocation(ctx.Iincop()));
		setInstructionLabel(ctx, instr);
		addInstruction(instr);
		
	}
	
	

	@Override
	public void enterNewarrayop(NewarrayopContext ctx) {
		NewarrayInstruction instr = new NewarrayInstruction(ctx.arrayType().getText());
		instr.setSourceLocation(createSourceLocation(ctx.Newarrayop()));
		setInstructionLabel(ctx, instr);
		addInstruction(instr);
	}
	
	@Override
	public void enterMultinewarrayop(MultinewarrayopContext ctx) {
		MultianewarrayInstruction instr = new MultianewarrayInstruction();
		instr.setCpEntryReference(createSymbolReference(ctx.Identifier()));
		instr.setDimensionsLiteral(createIntegerLiteral(ctx.IntegerLiteral()));
		instr.setSourceLocation(createSourceLocation(ctx.Multinewarrayop()));
		setInstructionLabel(ctx, instr);
		addInstruction(instr);
	}
	
	


	@Override
	public void enterSwitchop(SwitchopContext ctx) {
		String name = ctx.Switchop().getText();
		short opCode = OpCodes.getOpcodeForName(name);
		AbstractSwitchInstruction instr = null;
		if (opCode == OpCodes.lookupswitch) {
			instr = new LookupSwitchInstruction();
		} else if (opCode == OpCodes.tableswitch) {
			instr = new TableSwitchInstruction();
		}
		instr.setSourceLocation(createSourceLocation(ctx.Switchop()));
		setInstructionLabel(ctx, instr);
		stack.push(instr);
	}
	
	


	@Override
	public void enterSwitchMember(SwitchMemberContext ctx) {
		TerminalNode defLit = ctx.switchSource().DefaultLiteral();
		TerminalNode intLit = ctx.switchSource().IntegerLiteral();
		SymbolReference ref = createSymbolReference(ctx.Identifier());
		AbstractSwitchInstruction instr = (AbstractSwitchInstruction)stack.peek();
		if (defLit != null) {
			instr.setDefaultTargetReference(ref);
		} else if (intLit != null) {
			instr.addTargetReference(createIntegerLiteral(intLit), ref);
		} else {
			throw new IllegalStateException();
		}
		
	}
	
	
	@Override
	public void exitSwitchop(SwitchopContext ctx) {
		addInstruction((AbstractSwitchInstruction)stack.pop());
	}
	
	


	@Override
	public void enterBranchop(BranchopContext ctx) {
		String name = ctx.Branchop().getText();
		short opCode = OpCodes.getOpcodeForName(name);
		BranchInstruction instr = new BranchInstruction(opCode, null);
		instr.setTargetReference(createSymbolReference(ctx.Identifier()));
		instr.setSourceLocation(createSourceLocation(ctx.Branchop()));
		setInstructionLabel(ctx, instr);
		addInstruction(instr);
	}


	private void setInstructionLabel(ParserRuleContext context, AbstractInstruction instr) {
		MethodinstructionContext ctx = (MethodinstructionContext)context.getParent();
		if (ctx.label() != null) {
			instr.setLabel(createLabel(ctx.label().Identifier()));
		}
		
	}


	private void addVar(LocalVariable var) {
		CodeAttributeContent content = getAttributeContentCreatingIfNecessary(CodeAttributeContent.class);
		content.getInstructions().getVariablesPool().addVariable(var);
	}
	
	private void addInstruction(AbstractInstruction instr) {
		CodeAttributeContent content = getAttributeContentCreatingIfNecessary(CodeAttributeContent.class);
		Instructions instrs = content.getInstructions();
		if (instr.getLabel() != null) {
			if (!instrs.getSymbolTable().contains(instr.getSymbolName())) {
				instrs.getSymbolTable().add(instr);
			} else {
				emitError(instr.getSourceLocation().getLine(), instr.getSourceLocation().getCharPosition(), "dublicate instruction label "+instr.getSymbolName());
			}
		}
		
		content.getInstructions().addWithoutSetOffsets(instr);
	}


	@Override
	public void enterMethod(MethodContext ctx) {
		Clazz clazz = (Clazz)stack.peek();
		Method m = new Method();
		m.setSourceLocation(createSourceLocation(ctx.METHOD()));
		clazz.getMethods().add(m);
		stack.push(m);
		
	}
	
	

	@Override
	public void exitMethod(MethodContext ctx) {
		stack.pop();
	}
	
	
	


	@Override
	public void enterField(FieldContext ctx) {
		Clazz clazz = (Clazz)stack.peek();
		Field f = new Field();
		f.setSourceLocation(createSourceLocation(ctx.FIELD()));
		clazz.getFields().add(f);
		stack.push(f);
	}
	
	
	
	@Override
	public void enterFieldattributeConstantValue(
			FieldattributeConstantValueContext ctx) {
		ConstantValueAttributeContent content = new ConstantValueAttributeContent();
		content.setValueReference(createSymbolReference(ctx.Identifier()));
		addAttribute(content, ctx.CONSTANT());
	}


	@Override
	public void enterFieldname(FieldnameContext ctx) {
		Field f = (Field)stack.peek();
		if (f.getNameReference() == null) {
			f.setNameReference(createSymbolReference(ctx.Identifier()));
		} else {
			emitError(ctx.NAME(), "multiple field name statements within the same field statement");
		}
	}
	
	@Override
	public void enterFielddescriptor(FielddescriptorContext ctx) {
		Field f = (Field)stack.peek();
		if (f.getDescriptorReference() == null) {
			f.setDescriptorReference(createSymbolReference(ctx.Identifier()));
		} else {
			emitError(ctx.DESCRIPTOR(), "multiple descriptor statements within the same field statement");
		}
		
	}
	
	@Override
	public void enterFieldmodifierFinal(FieldmodifierFinalContext ctx) {
		Field f = (Field)stack.peek();
		f.getModifierLiterals().add(createKeyword(ctx.FINAL()));
	}
	
	
	
	@Override
	public void enterFieldmodifierProtected(FieldmodifierProtectedContext ctx) {
		Field f = (Field)stack.peek();
		f.getModifierLiterals().add(createKeyword(ctx.PROTECTED()));
	}


	@Override
	public void enterFieldmodifierPublic(FieldmodifierPublicContext ctx) {
		Field f = (Field)stack.peek();
		f.getModifierLiterals().add(createKeyword(ctx.PUBLIC()));
	}


	@Override
	public void enterFieldmodifierEnum(FieldmodifierEnumContext ctx) {
		Field f = (Field)stack.peek();
		f.getModifierLiterals().add(createKeyword(ctx.ENUM()));
	}


	@Override
	public void enterFieldmodifierVolatile(FieldmodifierVolatileContext ctx) {
		Field f = (Field)stack.peek();
		f.getModifierLiterals().add(createKeyword(ctx.VOLATILE()));
	}


	@Override
	public void enterFieldmodifierTransient(FieldmodifierTransientContext ctx) {
		Field f = (Field)stack.peek();
		f.getModifierLiterals().add(createKeyword(ctx.TRANSIENT()));
	}


	@Override
	public void enterFieldmodifierStatic(FieldmodifierStaticContext ctx) {
		Field f = (Field)stack.peek();
		f.getModifierLiterals().add(createKeyword(ctx.STATIC()));
	}


	@Override
	public void enterFieldmodifierPrivate(FieldmodifierPrivateContext ctx) {
		Field f = (Field)stack.peek();
		f.getModifierLiterals().add(createKeyword(ctx.PRIVATE()));
	}


	@Override
	public void enterFieldmodifierSyntetic(FieldmodifierSynteticContext ctx) {
		Field f = (Field)stack.peek();
		f.getModifierLiterals().add(createKeyword(ctx.SYNTETIC()));
	}


	@Override
	public void exitField(FieldContext ctx) {
		stack.pop();
	}
	
	@Override
	public void enterSignatureattribute(SignatureattributeContext ctx) {
		SignatureAttributeContent content = new SignatureAttributeContent();
		content.setValueLabel(createSymbolReference(ctx.Identifier()));
		addAttribute(content, ctx.SIGNATURE());
	}

	@Override
	public void enterDeprecatedattribute(DeprecatedattributeContext ctx) {
		DeprecatedAttributeContent content = new DeprecatedAttributeContent();
		addAttribute(content, ctx.DEPRECATED());
	}


	@Override
	public void enterSynteticattribute(SynteticattributeContext ctx) {
		SynteticAttributeContent content = new SynteticAttributeContent();
		addAttribute(content, ctx.SYNTETIC());
	}
	
	@Override
	public void enterEnclosingmethod(EnclosingmethodContext ctx) {
		EnclosingMethodAttributeContent content = new EnclosingMethodAttributeContent();
		content.setClazzReference(createSymbolReference(ctx.Identifier(0)));
		content.setMethodReference(createSymbolReference(ctx.Identifier(1)));
		addAttribute(content, ctx.ENCLOSING());
	}
	
	
	//Annotations
	
	
	
	


	@Override
	public void enterAnnotationdeclaration(AnnotationdeclarationContext ctx) {
		boolean rootAnnotation = (ctx.getParent() instanceof AnnotationContext);
		boolean nestedAnnotation = (ctx.getParent() instanceof AnnotationelementvalueContext);
		Annotation annot = new Annotation();
		if (rootAnnotation) {
			boolean invisible = ((AnnotationContext)ctx.getParent()).INVISIBLE() != null;
			boolean parameter = ((AnnotationContext)ctx.getParent()).PARAMETER() != null;
			IAttributeContent content = null;
			if (parameter) {
				if (invisible) {
					content = getAttributeContentCreatingIfNecessary(RuntimeInvisibleParameterAnnotationsAttributeContent.class);
				} else {
					content = getAttributeContentCreatingIfNecessary(RuntimeVisibleParameterAnnotationsAttributeContent.class);
				}
				((AbstractParameterAnnotationsAttributeContent)content).addAnnotation(annot);
				if (invisible) {
					annot.setSourceLocation(createSourceLocation(((AnnotationContext)ctx.getParent()).INVISIBLE()));
				} else {
					annot.setSourceLocation(createSourceLocation(ctx.ANNOTATION()));
				}
				if (((AbstractParameterAnnotationsAttributeContent)content).getSourceLocation() == null) {
					((AbstractParameterAnnotationsAttributeContent)content).setSourceLocation(annot.getSourceLocation());
				}
				
			} else {
				if (invisible) {
					content = getAttributeContentCreatingIfNecessary(RuntimeInvisibleAnnotationsAttributeContent.class);
				} else {
					content = getAttributeContentCreatingIfNecessary(RuntimeVisibleAnnotationsAttributeContent.class);
				}
				((AbstractAnnotationsAttributeContent)content).add(annot);
				if (invisible) {
					annot.setSourceLocation(createSourceLocation(((AnnotationContext)ctx.getParent()).INVISIBLE()));
				} else {
					annot.setSourceLocation(createSourceLocation(ctx.ANNOTATION()));
				}
				if (((AbstractAnnotationsAttributeContent)content).getSourceLocation() == null) {
					((AbstractAnnotationsAttributeContent)content).setSourceLocation(annot.getSourceLocation());
				}
			}
			
			
		} else if (nestedAnnotation){
			AnnotationElementValue value = new AnnotationElementValue();
			value.setTag('@');
			addAnnotationValue(value);
			annot.setSourceLocation(createSourceLocation(ctx.ANNOTATION()));
			value.setNestedAnnotation(annot);
			
		} else {
			throw new IllegalStateException("");
		}
		stack.push(annot);
	}
	

	@Override
	public void enterAnnotationtype(AnnotationtypeContext ctx) {
		Annotation annot = (Annotation)stack.peek();
		annot.setTypeValueReference(createSymbolReference(ctx.Identifier()));
	}
	
	
	
	

	@Override
	public void enterAnnotationindex(AnnotationindexContext ctx) {
		Annotation annot = (Annotation)stack.peek();
		IntegerLiteral lit = createIntegerLiteral(ctx.IntegerLiteral());
		if (lit.isValid()) {
			annot.setParameterIndex(lit.getValue());
		} else {
			emitError(ctx.IntegerLiteral(), "malformed integer or integer out of bounds");
		}
		
	}


	@Override
	public void enterAnnotationelement(AnnotationelementContext ctx) {
		Annotation annot = (Annotation)stack.peek();
		AnnotationElementNameValue element = new AnnotationElementNameValue();
		annot.addElement(element);
		stack.push(element);
	}
	
	
	

	@Override
	public void enterAnnotationelementname(AnnotationelementnameContext ctx) {
		AnnotationElementNameValue element = (AnnotationElementNameValue)stack.peek();
		element.setNameReference(createSymbolReference(ctx.Identifier()));
	}
	
	

	
	
	

	@Override
	public void enterArrayannotationelementvalue(
			ArrayannotationelementvalueContext ctx) {
		AnnotationElementValue value = new AnnotationElementValue();
		value.setTag('[');
		addAnnotationValue(value);
		stack.push(value);
	}
	
	@Override
	public void enterEnumannotationelementvalue(
			EnumannotationelementvalueContext ctx) {
		AnnotationElementValue value = new AnnotationElementValue();
		value.setTag('e');
		value.setEnumTypeNameReference(createSymbolReference(ctx.Identifier(0)));
		value.setEnumConstNameReference(createSymbolReference(ctx.Identifier(1)));
		addAnnotationValue(value);
	}


	@Override
	public void enterSimpleannotationelementvalue(
			SimpleannotationelementvalueContext ctx) {
		char tag = ' ';
		if (ctx.BYTE() != null) {
			tag = 'B';
		} else if (ctx.BOOLEAN() != null) {
			tag = 'Z';
		} else if (ctx.CHAR() != null) {
			tag = 'C';
		} else if (ctx.CLASS() != null) {
			tag = 'c';
		} else if (ctx.DOUBLE() != null) {
			tag = 'D';
		} else if (ctx.FLOAT() != null) {
			tag = 'F';
		} else if (ctx.INT() != null) {
			tag = 'I';
		} else if (ctx.LONG() != null) {
			tag = 'J';
		} else if (ctx.SHORT() != null) {
			tag = 'S';
		} else if (ctx.STRING() != null) {
			tag = 's';
		} else {
			throw new IllegalStateException("unexpected modifier");
		}
		
		AnnotationElementValue value = new AnnotationElementValue();
		value.setTag(tag);
		if (value.isPrimitiveValue()) {
			value.setPrimitiveValueReference(createSymbolReference(ctx.Identifier()));
		} else {
			value.setClassInfoReference(createSymbolReference(ctx.Identifier()));
		}
		addAnnotationValue(value);
	}
	
	private void addAnnotationValue(AnnotationElementValue value) {
		if (stack.peek() instanceof AnnotationElementNameValue) {
			AnnotationElementNameValue element = (AnnotationElementNameValue)stack.peek();
			element.setValue(value);
		} else if (stack.peek() instanceof AnnotationElementValue) {
			AnnotationElementValue arrayValue = (AnnotationElementValue)stack.peek();
			if (!arrayValue.isArray()) {
				throw new IllegalStateException("Unexpected value on the stack: "+arrayValue.getTag());
			}
			arrayValue.addArrayMember(value);
		} else {
			throw new IllegalStateException("Unexpected element in the stack");
		}
	}


	@Override
	public void exitAnnotationelement(AnnotationelementContext ctx) {
		stack.pop();
	}


	@Override
	public void exitAnnotationdeclaration(AnnotationdeclarationContext ctx) {
		stack.pop();
	}
	

	@Override
	public void exitArrayannotationelementvalue(
			ArrayannotationelementvalueContext ctx) {
		stack.pop();
	}


	public void emitError(int line, int charPosition, String message) {
		errorMessages.add(new ErrorMessage(line, charPosition, message));
	}
	
	
	
	

	

	

	@Override
	public void enterInnerclass(InnerclassContext ctx) {
		InnerClass innerClass = new InnerClass();
		InnerClassesAttributeContent content = getAttributeContentCreatingIfNecessary(InnerClassesAttributeContent.class);
		innerClass.setSourceLocation(createSourceLocation(ctx.INNER()));
		if (content.getSourceLocation() == null) {
			content.setSourceLocation(innerClass.getSourceLocation());
		}
		content.add(innerClass);
		stack.push(innerClass);
	}


	@Override
	public void enterInnerclass_inner(Innerclass_innerContext ctx) {
		InnerClass incl = (InnerClass)stack.peek();
		if (incl.getInnerClassReference() == null) {
			incl.setInnerClassReference(createSymbolReference(ctx.Identifier()));
		} else {
			emitError(ctx.INNER(), "multiple inner statements");
		}
		
	}
	
	@Override
	public void enterInnerclass_outer(Innerclass_outerContext ctx) {
		InnerClass incl = (InnerClass)stack.peek();
		if (incl.getOuterClassReference() == null) {
			incl.setOuterClassReference(createSymbolReference(ctx.Identifier()));
		} else {
			emitError(ctx.OUTER(), "multiple outer statements");
		}
		
	}
	
	@Override
	public void enterInnerclass_name(Innerclass_nameContext ctx) {
		InnerClass incl = (InnerClass)stack.peek();
		if (incl.getInnerNameReference() == null) {
			incl.setInnerNameReference(createSymbolReference(ctx.Identifier()));
		} else {
			emitError(ctx.NAME(), "multiple name statements");
		}
	}
	
	


	@Override
	public void enterInnerclassmodifierStatic(
			InnerclassmodifierStaticContext ctx) {
		InnerClass incl = (InnerClass)stack.peek();
		incl.getModifierLiterals().add(createKeyword(ctx.STATIC()));
	}


	@Override
	public void enterInnerclassmodifierSyntetic(
			InnerclassmodifierSynteticContext ctx) {
		InnerClass incl = (InnerClass)stack.peek();
		incl.getModifierLiterals().add(createKeyword(ctx.SYNTETIC()));
	}


	@Override
	public void enterInnerclassmodifierFinal(InnerclassmodifierFinalContext ctx) {
		InnerClass incl = (InnerClass)stack.peek();
		incl.getModifierLiterals().add(createKeyword(ctx.FINAL()));
	}


	@Override
	public void enterInnerclassmodifierProtected(
			InnerclassmodifierProtectedContext ctx) {
		InnerClass incl = (InnerClass)stack.peek();
		incl.getModifierLiterals().add(createKeyword(ctx.PROTECTED()));
	}


	@Override
	public void enterInnerclassmodifierEnum(InnerclassmodifierEnumContext ctx) {
		InnerClass incl = (InnerClass)stack.peek();
		incl.getModifierLiterals().add(createKeyword(ctx.ENUM()));
	}


	@Override
	public void enterInnerclassmodifierPublic(
			InnerclassmodifierPublicContext ctx) {
		InnerClass incl = (InnerClass)stack.peek();
		incl.getModifierLiterals().add(createKeyword(ctx.PUBLIC()));
	}


	@Override
	public void enterInnerclassmodifierInterface(
			InnerclassmodifierInterfaceContext ctx) {
		InnerClass incl = (InnerClass)stack.peek();
		incl.getModifierLiterals().add(createKeyword(ctx.INTERFACE()));
	}


	@Override
	public void enterInnerclassmodifierAnnotation(
			InnerclassmodifierAnnotationContext ctx) {
		InnerClass incl = (InnerClass)stack.peek();
		incl.getModifierLiterals().add(createKeyword(ctx.ANNOTATION()));
	}


	@Override
	public void enterInnerclassmodifierAbstract(
			InnerclassmodifierAbstractContext ctx) {
		InnerClass incl = (InnerClass)stack.peek();
		incl.getModifierLiterals().add(createKeyword(ctx.ABSTRACT()));
	}


	@Override
	public void enterInnerclassmodifierPrivate(
			InnerclassmodifierPrivateContext ctx) {
		InnerClass incl = (InnerClass)stack.peek();
		incl.getModifierLiterals().add(createKeyword(ctx.PRIVATE()));
	}
	
	

	@Override
	public void exitClassInnerClass(ClassInnerClassContext ctx) {
		stack.pop();
	}


	public void emitError(TerminalNode node, String message) {
		errorMessages.add(new ErrorMessage(node.getSymbol().getLine(), node.getSymbol().getCharPositionInLine(), message));
	}
	
	
	private void addConstantPoolEntry(AbstractConstantPoolEntry entry) {
		ConstantPool pool = ((Clazz)stack.peek()).getConstantPool();
		pool.add(entry);
		if (!pool.getSymbolTable().contains(entry.getSymbolName())) {
			pool.getSymbolTable().add(entry);
		} else {
			emitError(entry.getSourceLocation().getLine(), entry.getSourceLocation().getCharPosition(), "dublicate constant pool entry label "+entry.getSymbolName());
		}
	}
	

	private SourceLocation createSourceLocation(TerminalNode node) {
		return new SourceLocation(node.getSymbol().getLine(), node.getSymbol().getCharPositionInLine());
	}
	
	private SymbolReference createSymbolReference(TerminalNode node) {
		return new SymbolReference(node.getSymbol().getLine(), node.getSymbol().getCharPositionInLine(), node.getText());
	}
	
	private Label createLabel(TerminalNode node) {
		return new Label(node.getSymbol().getLine(), node.getSymbol().getCharPositionInLine(), node.getText());
	}
	
	private StringLiteral createStringLiteral(TerminalNode node) {
		return new StringLiteral(node.getSymbol().getLine(), node.getSymbol().getCharPositionInLine(), node.getText());
	}
	
	private IntegerLiteral createIntegerLiteral(TerminalNode node) {
		return new IntegerLiteral(node.getSymbol().getLine(), node.getSymbol().getCharPositionInLine(), node.getText());
	}
	
	private LongLiteral createLongLiteral(TerminalNode node) {
		return new LongLiteral(node.getSymbol().getLine(), node.getSymbol().getCharPositionInLine(), node.getText());
	}
	
	private FloatLiteral createFloatLiteral(TerminalNode node) {
		return new FloatLiteral(node.getSymbol().getLine(), node.getSymbol().getCharPositionInLine(), node.getText());
	}
	
	private DoubleLiteral createDoubleLiteral(TerminalNode node) {
		return new DoubleLiteral(node.getSymbol().getLine(), node.getSymbol().getCharPositionInLine(), node.getText());
	}
	
	private Keyword createKeyword(TerminalNode node) {
		return new Keyword(node.getSymbol().getLine(), node.getSymbol().getCharPositionInLine(), node.getText());
	}
	
	private VersionLiteral createVersionLiteral(TerminalNode node) {
		return new VersionLiteral(node.getSymbol().getLine(), node.getSymbol().getCharPositionInLine(), node.getText());
	}
	
	
	
	private void addAttribute(IAttributeContent content, TerminalNode node) {
		IAttributesContainer container = (IAttributesContainer)stack.peek();
		Attribute attr = new Attribute();
		if (node != null) {
			attr.setSourceLocation(createSourceLocation(node));
		}
		container.getAttributes().add(attr);
		attr.setContent(content);
	}
	
	private <T extends IAttributeContent> T getAttributeContentCreatingIfNecessary(Class<T> clazz) {
		IAttributesContainer container = (IAttributesContainer)stack.peek();
		List<Attribute> attributes = container.getAttributes().getAttributesByContentType(clazz);
		if (attributes.size() == 0) {
			Attribute attr = new Attribute();
			container.getAttributes().add(attr);
			IAttributeContent content;
			try {
				content = clazz.newInstance();
				attr.setContent(content);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return (T)content;
		} else if (attributes.size() == 1) {
			return (T)attributes.get(0).getContent();
		} else {
			throw new IllegalStateException("multiple attributes with content type: "+clazz.getName());
		}
		
	}
	

}

class SyntaxErrorListener extends BaseErrorListener {
	
	private AssemblerParser parent;
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	SyntaxErrorListener(AssemblerParser parent) {
		this.parent = parent;
	}

	@Override
	public void syntaxError(Recognizer<?, ?> recognizer,
			Object offendingSymbol, int line, int charPositionInLine,
			String msg, RecognitionException e) {
		parent.getErrorMessages().add(new ErrorMessage(line, charPositionInLine, msg));
		if (log.isDebugEnabled()) {
			log.debug("offending symbol: "+offendingSymbol);
		}
		
	}

	@Override
	public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex,
			int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
		if (log.isDebugEnabled()) {
			log.debug("Ambiguity: "+startIndex+":"+stopIndex);
		}
		
	}

	@Override
	public void reportAttemptingFullContext(Parser recognizer, DFA dfa,
			int startIndex, int stopIndex, BitSet conflictingAlts,
			ATNConfigSet configs) {
		// TODO Auto-generated method stub
		super.reportAttemptingFullContext(recognizer, dfa, startIndex, stopIndex,
				conflictingAlts, configs);
	}

	@Override
	public void reportContextSensitivity(Parser recognizer, DFA dfa,
			int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
		// TODO Auto-generated method stub
		super.reportContextSensitivity(recognizer, dfa, startIndex, stopIndex,
				prediction, configs);
	}
	
	
	
	
	
}
