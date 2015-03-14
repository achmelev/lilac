package org.jasm.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jasm.JasmConsts;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.attribute.AbstractAnnotationsAttributeContent;
import org.jasm.item.attribute.AbstractParameterAnnotationsAttributeContent;
import org.jasm.item.attribute.AbstractStackmapVariableinfo;
import org.jasm.item.attribute.Annotation;
import org.jasm.item.attribute.AnnotationDefaultAttributeContent;
import org.jasm.item.attribute.AnnotationElementNameValue;
import org.jasm.item.attribute.AnnotationElementValue;
import org.jasm.item.attribute.AnnotationTargetTypePath;
import org.jasm.item.attribute.AppendStackmapFrame;
import org.jasm.item.attribute.Attribute;
import org.jasm.item.attribute.BootstrapMethod;
import org.jasm.item.attribute.BootstrapMethodsAttributeContent;
import org.jasm.item.attribute.CatchAnnotationTargetType;
import org.jasm.item.attribute.ChopStackmapFrame;
import org.jasm.item.attribute.CodeAttributeContent;
import org.jasm.item.attribute.ConstantValueAttributeContent;
import org.jasm.item.attribute.DebugLocalVariable;
import org.jasm.item.attribute.DebugLocalVariableType;
import org.jasm.item.attribute.DeprecatedAttributeContent;
import org.jasm.item.attribute.DoubleStackmapVariableinfo;
import org.jasm.item.attribute.EmptyAnnotationTargetType;
import org.jasm.item.attribute.EnclosingMethodAttributeContent;
import org.jasm.item.attribute.ExceptionHandler;
import org.jasm.item.attribute.ExceptionsAttributeContent;
import org.jasm.item.attribute.FloatStackmapVariableinfo;
import org.jasm.item.attribute.FormalParameterAnnotationTargetType;
import org.jasm.item.attribute.FullStackmapFrame;
import org.jasm.item.attribute.IAttributeContent;
import org.jasm.item.attribute.IStackmapVariableinfoContainer;
import org.jasm.item.attribute.InnerClass;
import org.jasm.item.attribute.InnerClassesAttributeContent;
import org.jasm.item.attribute.IntegerStackmapVariableinfo;
import org.jasm.item.attribute.LineNumber;
import org.jasm.item.attribute.LineNumberTableAttributeContent;
import org.jasm.item.attribute.LocalVariableAnnotationTargetType;
import org.jasm.item.attribute.LocalVariableAnnotationTargetTypeMember;
import org.jasm.item.attribute.LocalVariableTableAttributeContent;
import org.jasm.item.attribute.LocalVariableTypeTableAttributeContent;
import org.jasm.item.attribute.LongStackmapVariableinfo;
import org.jasm.item.attribute.NullStackmapVariableinfo;
import org.jasm.item.attribute.ObjectStackmapVariableinfo;
import org.jasm.item.attribute.OffsetAnnotationTargetType;
import org.jasm.item.attribute.RuntimeInvisibleAnnotationsAttributeContent;
import org.jasm.item.attribute.RuntimeInvisibleParameterAnnotationsAttributeContent;
import org.jasm.item.attribute.RuntimeInvisibleTypeAnnotationsAttributeContent;
import org.jasm.item.attribute.RuntimeVisibleAnnotationsAttributeContent;
import org.jasm.item.attribute.RuntimeVisibleParameterAnnotationsAttributeContent;
import org.jasm.item.attribute.RuntimeVisibleTypeAnnotationsAttributeContent;
import org.jasm.item.attribute.SameExtendedStackmapFrame;
import org.jasm.item.attribute.SameLocalsOneStackitemExtendedStackmapFrame;
import org.jasm.item.attribute.SameLocalsOneStackitemStackmapFrame;
import org.jasm.item.attribute.SameStackmapFrame;
import org.jasm.item.attribute.SignatureAttributeContent;
import org.jasm.item.attribute.SourceFileAttributeContent;
import org.jasm.item.attribute.StackMapAttributeContent;
import org.jasm.item.attribute.SupertypeAnnotationTargetType;
import org.jasm.item.attribute.SynteticAttributeContent;
import org.jasm.item.attribute.ThrowsAnnotationTargetType;
import org.jasm.item.attribute.TopStackmapVariableinfo;
import org.jasm.item.attribute.TypeArgumentAnnotationTargetType;
import org.jasm.item.attribute.TypeParameterAnnotationTargetType;
import org.jasm.item.attribute.TypeParameterBoundAnnotationTargetType;
import org.jasm.item.attribute.UninitializedStackmapVariableinfo;
import org.jasm.item.attribute.UninitializedThisStackmapVariableinfo;
import org.jasm.item.attribute.UnknownAttributeContent;
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
import org.jasm.item.constantpool.InvokeDynamicInfo;
import org.jasm.item.constantpool.LongInfo;
import org.jasm.item.constantpool.MethodHandleInfo;
import org.jasm.item.constantpool.MethodHandleInfo.MethodHandleReferenceKind;
import org.jasm.item.constantpool.MethodTypeInfo;
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
import org.jasm.item.instructions.InvokeDynamicInstruction;
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
import org.jasm.parser.JavaAssemblerParser.AnnotationdefaultContext;
import org.jasm.parser.JavaAssemblerParser.AnnotationelementContext;
import org.jasm.parser.JavaAssemblerParser.AnnotationelementnameContext;
import org.jasm.parser.JavaAssemblerParser.AnnotationelementvalueContext;
import org.jasm.parser.JavaAssemblerParser.AnnotationparameterindexContext;
import org.jasm.parser.JavaAssemblerParser.AnnotationtargetpathContext;
import org.jasm.parser.JavaAssemblerParser.AnnotationtypeContext;
import org.jasm.parser.JavaAssemblerParser.AppendStackmapFrameContext;
import org.jasm.parser.JavaAssemblerParser.ArgumentlessopContext;
import org.jasm.parser.JavaAssemblerParser.ArrayannotationelementvalueContext;
import org.jasm.parser.JavaAssemblerParser.BootstrapmethodContext;
import org.jasm.parser.JavaAssemblerParser.BranchopContext;
import org.jasm.parser.JavaAssemblerParser.CasttypeTargetTypeContext;
import org.jasm.parser.JavaAssemblerParser.CatchtypeTargetTypeContext;
import org.jasm.parser.JavaAssemblerParser.ChopStackmapFrameContext;
import org.jasm.parser.JavaAssemblerParser.ClassAttributeSynteticContext;
import org.jasm.parser.JavaAssemblerParser.ClassInnerClassContext;
import org.jasm.parser.JavaAssemblerParser.ClassattributeSourceFileContext;
import org.jasm.parser.JavaAssemblerParser.ClassinfoContext;
import org.jasm.parser.JavaAssemblerParser.ClassmodifierAbstractContext;
import org.jasm.parser.JavaAssemblerParser.ClassmodifierAnnotationContext;
import org.jasm.parser.JavaAssemblerParser.ClassmodifierEnumContext;
import org.jasm.parser.JavaAssemblerParser.ClassmodifierFinalContext;
import org.jasm.parser.JavaAssemblerParser.ClassmodifierInterfaceContext;
import org.jasm.parser.JavaAssemblerParser.ClassmodifierPublicContext;
import org.jasm.parser.JavaAssemblerParser.ClassmodifierSuperContext;
import org.jasm.parser.JavaAssemblerParser.ClassmodifierSynteticContext;
import org.jasm.parser.JavaAssemblerParser.ClassnameContext;
import org.jasm.parser.JavaAssemblerParser.ClazzContext;
import org.jasm.parser.JavaAssemblerParser.ConstantpoolopContext;
import org.jasm.parser.JavaAssemblerParser.ConstructorreferencetypeTargetTypeContext;
import org.jasm.parser.JavaAssemblerParser.ConstructorreferencetypeargumentTargetTypeContext;
import org.jasm.parser.JavaAssemblerParser.ConstructortypeargumentTargetTypeContext;
import org.jasm.parser.JavaAssemblerParser.DebugvarContext;
import org.jasm.parser.JavaAssemblerParser.DebugvartypeContext;
import org.jasm.parser.JavaAssemblerParser.DeprecatedattributeContext;
import org.jasm.parser.JavaAssemblerParser.DoubleStackmapvarinfoContext;
import org.jasm.parser.JavaAssemblerParser.DoubleinfoContext;
import org.jasm.parser.JavaAssemblerParser.DynrefinfoContext;
import org.jasm.parser.JavaAssemblerParser.EmptyTargetFieldTypeContext;
import org.jasm.parser.JavaAssemblerParser.EmptyTargetReceiverTypeContext;
import org.jasm.parser.JavaAssemblerParser.EmptyTargetReturnTypeContext;
import org.jasm.parser.JavaAssemblerParser.EnclosingmethodContext;
import org.jasm.parser.JavaAssemblerParser.EnumannotationelementvalueContext;
import org.jasm.parser.JavaAssemblerParser.ExceptionsattributeContext;
import org.jasm.parser.JavaAssemblerParser.ExtendedStackmapAttributeContext;
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
import org.jasm.parser.JavaAssemblerParser.FloatStackmapvarinfoContext;
import org.jasm.parser.JavaAssemblerParser.FloatinfoContext;
import org.jasm.parser.JavaAssemblerParser.FormalparametertypeTargetTypeContext;
import org.jasm.parser.JavaAssemblerParser.FullStackmapFrameContext;
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
import org.jasm.parser.JavaAssemblerParser.InstanceoftypeTargetTypeContext;
import org.jasm.parser.JavaAssemblerParser.IntStackmapvarinfoContext;
import org.jasm.parser.JavaAssemblerParser.IntegerinfoContext;
import org.jasm.parser.JavaAssemblerParser.InterfacemethodrefinfoContext;
import org.jasm.parser.JavaAssemblerParser.InterfacesContext;
import org.jasm.parser.JavaAssemblerParser.LabeledIdentifierContext;
import org.jasm.parser.JavaAssemblerParser.LdcopContext;
import org.jasm.parser.JavaAssemblerParser.LinenumberContext;
import org.jasm.parser.JavaAssemblerParser.LocalvaropContext;
import org.jasm.parser.JavaAssemblerParser.LocalvartypeTargetTypeContext;
import org.jasm.parser.JavaAssemblerParser.LocalvartypememberContext;
import org.jasm.parser.JavaAssemblerParser.LongStackmapvarinfoContext;
import org.jasm.parser.JavaAssemblerParser.LonginfoContext;
import org.jasm.parser.JavaAssemblerParser.MethodContext;
import org.jasm.parser.JavaAssemblerParser.MethoddescriptorContext;
import org.jasm.parser.JavaAssemblerParser.MethodexceptionhandlerContext;
import org.jasm.parser.JavaAssemblerParser.MethodhandleinfoContext;
import org.jasm.parser.JavaAssemblerParser.MethodinstructionContext;
import org.jasm.parser.JavaAssemblerParser.MethodlinenumbertableContext;
import org.jasm.parser.JavaAssemblerParser.MethodmaxlocalsContext;
import org.jasm.parser.JavaAssemblerParser.MethodmaxstackContext;
import org.jasm.parser.JavaAssemblerParser.MethodmodifierAbstractContext;
import org.jasm.parser.JavaAssemblerParser.MethodmodifierBridgeContext;
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
import org.jasm.parser.JavaAssemblerParser.MethodreferencetypeTargetTypeContext;
import org.jasm.parser.JavaAssemblerParser.MethodreferencetypeargumentTargetTypeContext;
import org.jasm.parser.JavaAssemblerParser.MethodrefinfoContext;
import org.jasm.parser.JavaAssemblerParser.MethodtypeargumentTargetTypeContext;
import org.jasm.parser.JavaAssemblerParser.MethodtypeinfoContext;
import org.jasm.parser.JavaAssemblerParser.MethodvarabsoluteContext;
import org.jasm.parser.JavaAssemblerParser.MethodvariabletableContext;
import org.jasm.parser.JavaAssemblerParser.MethodvariabletypetableContext;
import org.jasm.parser.JavaAssemblerParser.MethodvarimplicitContext;
import org.jasm.parser.JavaAssemblerParser.MethodvarrelativeContext;
import org.jasm.parser.JavaAssemblerParser.MultinewarrayopContext;
import org.jasm.parser.JavaAssemblerParser.NameandtypeinfoContext;
import org.jasm.parser.JavaAssemblerParser.NewarrayopContext;
import org.jasm.parser.JavaAssemblerParser.NewtypeTargetTypeContext;
import org.jasm.parser.JavaAssemblerParser.NullStackmapvarinfoContext;
import org.jasm.parser.JavaAssemblerParser.ObjectStackmapvarinfoContext;
import org.jasm.parser.JavaAssemblerParser.ParameterTypeBoundTargetTypeContext;
import org.jasm.parser.JavaAssemblerParser.ParameterTypeTargetTypeContext;
import org.jasm.parser.JavaAssemblerParser.ParameterannotationContext;
import org.jasm.parser.JavaAssemblerParser.PushopContext;
import org.jasm.parser.JavaAssemblerParser.RetopContext;
import org.jasm.parser.JavaAssemblerParser.SameExtendedStackmapFrameContext;
import org.jasm.parser.JavaAssemblerParser.SameLocalsExtendedStackmapFrameContext;
import org.jasm.parser.JavaAssemblerParser.SameLocalsStackmapFrameContext;
import org.jasm.parser.JavaAssemblerParser.SameStackmapFrameContext;
import org.jasm.parser.JavaAssemblerParser.SignatureattributeContext;
import org.jasm.parser.JavaAssemblerParser.SimpleStackmapAttributeContext;
import org.jasm.parser.JavaAssemblerParser.SimpleannotationelementvalueContext;
import org.jasm.parser.JavaAssemblerParser.StackitemstackmapvarinfosContext;
import org.jasm.parser.JavaAssemblerParser.StackmapattributeContext;
import org.jasm.parser.JavaAssemblerParser.StringinfoContext;
import org.jasm.parser.JavaAssemblerParser.SuperclassContext;
import org.jasm.parser.JavaAssemblerParser.SupertypeTargetTypeContext;
import org.jasm.parser.JavaAssemblerParser.SwitchMemberContext;
import org.jasm.parser.JavaAssemblerParser.SwitchopContext;
import org.jasm.parser.JavaAssemblerParser.SynteticattributeContext;
import org.jasm.parser.JavaAssemblerParser.TargetPathArrayContext;
import org.jasm.parser.JavaAssemblerParser.TargetPathNestedContext;
import org.jasm.parser.JavaAssemblerParser.TargetPathTypeArgumentBoundContext;
import org.jasm.parser.JavaAssemblerParser.TargetPathTypeArgumentContext;
import org.jasm.parser.JavaAssemblerParser.ThrowstypeTargetTypeContext;
import org.jasm.parser.JavaAssemblerParser.TopStackmapvarinfoContext;
import org.jasm.parser.JavaAssemblerParser.TypeannotationContext;
import org.jasm.parser.JavaAssemblerParser.UninitializedStackmapvarinfoContext;
import org.jasm.parser.JavaAssemblerParser.UninitializedThisStackmapvarinfoContext;
import org.jasm.parser.JavaAssemblerParser.UnknownattributeContext;
import org.jasm.parser.JavaAssemblerParser.Utf8infoContext;
import org.jasm.parser.JavaAssemblerParser.VersionContext;
import org.jasm.parser.literals.Base64Literal;
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

import com.sun.xml.internal.bind.v2.runtime.Location;






public class AssemblerParser  extends JavaAssemblerBaseListener {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private List<IParserErrorListener> errorListeners = new ArrayList<IParserErrorListener>();
	
	private Clazz result = null;
	
	Stack<IBytecodeItem> stack = new Stack<>();
	
	private int errorCounter = 0;
	
	public void addErrorListener(IParserErrorListener listener) {
		errorListeners.add(listener);
	}
	
	public void emitError(int line, int charPos, String msg) {
		errorCounter++;
		for (IParserErrorListener listener: errorListeners) {
			listener.error(line, charPos, msg);
		}
	}
	
	public void emitInternalError(AbstractByteCodeItem source, Throwable e) {
		errorCounter++;
		SourceLocation loc = source.getNextSourceLocation();
		log.error("Got internal error at "+loc.getLine()+"/"+loc.getCharPosition()+" in "+source.getClass().getName(),e);
	}
	
	public void flushErrors() {
		for (IParserErrorListener listener: errorListeners) {
			listener.flush();
		}
	}
	
	
	
	public int getErrorCounter() {
		return errorCounter;
	}

	public Clazz parse(InputStream inp) {
		ANTLRInputStream input = null;
		try {
			input = new ANTLRInputStream(inp);
		} catch (IOException e) {
			throw new RuntimeException("Error creating antlr stream",e);
		}
		return doParse(input);
		
	}
	
	public Clazz parse(Reader inp) {
		ANTLRInputStream input = null;
		try {
			input = new ANTLRInputStream(inp);
		} catch (IOException e) {
			throw new RuntimeException("Error creating antlr stream",e);
		}
		return doParse(input);
		
	}
	
	
	private  Clazz doParse(ANTLRInputStream input) {
		
		result = null;
		errorCounter = 0;
		for (IParserErrorListener listener: errorListeners) {
			listener.clear();
		}
		stack.clear();
		
		SyntaxErrorListener errorListener = new SyntaxErrorListener(this);
		JavaAssemblerLexer lexer = new JavaAssemblerLexer(input);
		lexer.removeErrorListeners();
		lexer.addErrorListener(errorListener);
		/*for (Token tok: lexer.getAllTokens()) {
			log.info(tok.getType() +" "+tok.getText());
		}*/
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		JavaAssemblerParser parser = new JavaAssemblerParser(tokens);
		parser.removeErrorListeners();
		parser.addErrorListener(errorListener);
		//parser.getInterpreter().setPredictionMode(PredictionMode.LL_EXACT_AMBIG_DETECTION);
		if (log.isDebugEnabled()) {
			//parser.setTrace(true);
			
		}
		
		//Parse
		ParseTree tree = parser.clazz();
		
		/*if (log.isDebugEnabled()) {
			log.debug("tree: "+tree.toStringTree());
		}*/
		
		if (errorCounter == 0) {
			//Walk tree an create class
			ParseTreeWalker walker = new ParseTreeWalker();
			walker.walk(this, tree);
			if (errorCounter == 0) {
				Clazz clazz = result;
				clazz.resolve();
				if (errorCounter == 0) {
					clazz.updateMetadata();
				}
				
			} else {
				result = null;
			}
		}
		
		
		
		return result;
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
			List<LabeledIdentifierContext> nodes = ctx.labeledIdentifier();
			List<SymbolReference> symbols = new ArrayList<>();
			for (LabeledIdentifierContext node: nodes) {
				symbols.add(createSymbolReference(node));
			}
			clazz.setInterfaceSymbols(symbols);
		} else {
			emitError(ctx.IMPLEMENTS(), "multiple interfaces statements");
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
	public void enterMethodtypeinfo(MethodtypeinfoContext ctx) {
		MethodTypeInfo entry = new MethodTypeInfo();
		if (ctx.label() != null) {
			entry.setLabel(createLabel(ctx.label().Identifier()));
		}
		entry.setSourceLocation(createSourceLocation(ctx.METHODTYPE()));
		entry.setReferenceLabels(new SymbolReference[]{createSymbolReference(ctx.Identifier())});
		addConstantPoolEntry(entry);
	}

	@Override
	public void enterDynrefinfo(DynrefinfoContext ctx) {
		InvokeDynamicInfo entry = new InvokeDynamicInfo();
		if (ctx.label() != null) {
			entry.setLabel(createLabel(ctx.label().Identifier()));
		}
		entry.setSourceLocation(createSourceLocation(ctx.DYNREFINFO()));
		entry.setMethodReference(createSymbolReference(ctx.Identifier(0)));
		entry.setNameAndTypeReference(createSymbolReference(ctx.Identifier(1)));
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
	public void enterMethodhandleinfo(MethodhandleinfoContext ctx) {
		MethodHandleInfo entry = new MethodHandleInfo();
		if (ctx.label() != null) {
			entry.setLabel(createLabel(ctx.label().Identifier()));
		}
		entry.setSourceLocation(createSourceLocation(ctx.METHODHANDLE()));
		entry.setRefSymbolReference(createSymbolReference(ctx.Identifier()));
		if (ctx.GETFIELD() != null) {
			entry.setKind(MethodHandleReferenceKind.GET_FIELD);
		} else if (ctx.PUTFIELD() != null) {
			entry.setKind(MethodHandleReferenceKind.PUT_FIELD);
		} else if (ctx.GETSTATIC() != null) {
			entry.setKind(MethodHandleReferenceKind.GET_STATIC);
		} else if (ctx.PUTSTATIC() != null) {
			entry.setKind(MethodHandleReferenceKind.PUT_STATIC);
		} else if (ctx.INVOKEINTERFACE() != null) {
			entry.setKind(MethodHandleReferenceKind.INVOKE_INTERFACE);
		} else if (ctx.INVOKESPECIAL() != null) {
			entry.setKind(MethodHandleReferenceKind.INVOKE_SPECIAL);
		} else if (ctx.INVOKEVIRTUAL() != null) {
			entry.setKind(MethodHandleReferenceKind.INVOKE_VIRTUAL);
		} else if (ctx.INVOKESTATIC() != null) {
			entry.setKind(MethodHandleReferenceKind.INVOKE_STATIC);
		} else if (ctx.NEWINVOKESPECIAL() != null) {
			entry.setKind(MethodHandleReferenceKind.NEW_INVOKE_SPECIAL);
		} else {
			throw new IllegalStateException();
		}
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
	public void enterExceptionsattribute(ExceptionsattributeContext ctx) {
		ExceptionsAttributeContent content = new ExceptionsAttributeContent();
		
		
		List<LabeledIdentifierContext> nodes = ctx.labeledIdentifier();
		List<SymbolReference> symbols = new ArrayList<>();
		for (LabeledIdentifierContext node: nodes) {
			SymbolReference ref = createSymbolReference(node);
			if (node.label() != null) {
				ref.setReferenceLabel(node.label().getText());
			}
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
		if (lit.isValid()) {
			if (lit.getValue()<0) {
				emitError(ctx.IntegerLiteral(), "an offset must be positive");
			} else {
				var.setOffset(lit.getValue());
				addVar(var);
			}
		} else {
			emitError(ctx.IntegerLiteral(), "malformed integer or integer out of bounds");
		}
		
	}
	
	
	@Override
	public void enterMethodvarrelative(MethodvarrelativeContext ctx) {
		LocalVariable var = new LocalVariable(LocalVariable.getTypeCode(ctx.methodvartype().getText()));
		var.setName(createSymbolReference(ctx.Identifier(0)));
		
		int offset = -1;
		if (ctx.IntegerLiteral() != null) {
			IntegerLiteral lit = createIntegerLiteral(ctx.IntegerLiteral());
			if (lit.isValid()) {
				if (lit.getValue()<0) {
					emitError(ctx.IntegerLiteral(), "an offset must be positive");
				} else {
					offset = lit.getValue();
				}
			} else {
				emitError(ctx.IntegerLiteral(), "malformed integer or integer out of bounds");
			}
		} else {
			offset = 0;
		}
		
		var.setOffset(offset);
		var.setParentName(createSymbolReference(ctx.Identifier(1)));
		addVar(var);
	}
	
	
	
	

	

	@Override
	public void enterMethodmaxlocals(MethodmaxlocalsContext ctx) {
		CodeAttributeContent code = getAttributeContentCreatingIfNecessary(CodeAttributeContent.class);
		code.setMaxLocalsLiteral(createIntegerLiteral(ctx.IntegerLiteral()));
	}


	@Override
	public void enterMethodmaxstack(MethodmaxstackContext ctx) {
		CodeAttributeContent code = getAttributeContentCreatingIfNecessary(CodeAttributeContent.class);
		code.setMaxStackLiteral(createIntegerLiteral(ctx.IntegerLiteral()));
	}


	@Override
	public void enterMethodlinenumbertable(MethodlinenumbertableContext ctx) {
		LineNumberTableAttributeContent content = new LineNumberTableAttributeContent();
		CodeAttributeContent code = getAttributeContentCreatingIfNecessary(CodeAttributeContent.class);
		stack.push(code);
		addAttribute(content, ctx.LINE());
		stack.pop();
		stack.push(content);
		
	}
	
	@Override
	public void enterLinenumber(LinenumberContext ctx) {
		LineNumberTableAttributeContent content = (LineNumberTableAttributeContent)stack.peek();
		LineNumber line = new LineNumber();
		line.setLineNumberLiteral(createIntegerLiteral(ctx.IntegerLiteral()));
		line.setStartInstructionLabel(createSymbolReference(ctx.Identifier()));
		content.add(line);
	}

	@Override
	public void exitMethodlinenumbertable(MethodlinenumbertableContext ctx) {
		stack.pop();
	}
	
	


	@Override
	public void enterMethodvariabletable(MethodvariabletableContext ctx) {
		LocalVariableTableAttributeContent content = new LocalVariableTableAttributeContent();
		CodeAttributeContent code = getAttributeContentCreatingIfNecessary(CodeAttributeContent.class);
		stack.push(code);
		addAttribute(content, ctx.DEBUG());
		stack.pop();
		stack.push(content);
	}


	@Override
	public void exitMethodvariabletable(MethodvariabletableContext ctx) {
		stack.pop();
	}


	@Override
	public void enterDebugvar(DebugvarContext ctx) {
		LocalVariableTableAttributeContent content = (LocalVariableTableAttributeContent)stack.peek();
		DebugLocalVariable var = new DebugLocalVariable();
		var.setVariableReference(createSymbolReference(ctx.Identifier(0)));
		var.setStartInstructionReference(createSymbolReference(ctx.Identifier(1)));
		int index = 2;
		if (ctx.Identifier().size() == 5) {
			var.setEndInstructionReference(createSymbolReference(ctx.Identifier(2)));
			index = 3;
		}
		var.setNameReference(createSymbolReference(ctx.Identifier(index)));
		var.setDescriptorReference(createSymbolReference(ctx.Identifier(index+1)));
		content.add(var);
	}
	
	


	@Override
	public void enterMethodvariabletypetable(MethodvariabletypetableContext ctx) {
		LocalVariableTypeTableAttributeContent content = new LocalVariableTypeTableAttributeContent();
		CodeAttributeContent code = getAttributeContentCreatingIfNecessary(CodeAttributeContent.class);
		stack.push(code);
		addAttribute(content, ctx.DEBUG());
		stack.pop();
		stack.push(content);
	}


	@Override
	public void exitMethodvariabletypetable(MethodvariabletypetableContext ctx) {
		stack.pop();
	}


	@Override
	public void enterDebugvartype(DebugvartypeContext ctx) {
		LocalVariableTypeTableAttributeContent content = (LocalVariableTypeTableAttributeContent)stack.peek();
		DebugLocalVariableType var = new DebugLocalVariableType();
		var.setVariableReference(createSymbolReference(ctx.Identifier(0)));
		var.setStartInstructionReference(createSymbolReference(ctx.Identifier(1)));
		int index = 2;
		if (ctx.Identifier().size() == 5) {
			var.setEndInstructionReference(createSymbolReference(ctx.Identifier(2)));
			index = 3;
		}
		var.setNameReference(createSymbolReference(ctx.Identifier(index)));
		var.setDescriptorReference(createSymbolReference(ctx.Identifier(index+1)));
		content.add(var);
	}


	@Override
	public void enterArgumentlessop(ArgumentlessopContext ctx) {
		String name = null;
		
		name = ctx.argumentlessop_keyword().getText();
		
		Short code = OpCodes.getOpcodeForName(name);
		ArgumentLessInstruction instr = new ArgumentLessInstruction(code);
		instr.setSourceLocation(createSourceLocation((TerminalNode)ctx.argumentlessop_keyword().getChild(0)));
		
		
		setInstructionLabel(ctx, instr);
		addInstruction(instr);
	}
	
	@Override
	public void enterConstantpoolop(ConstantpoolopContext ctx) {
		TerminalNode nameNode = null;
		nameNode = (TerminalNode)ctx.constantpoolop_keyword().getChild(0);
		
		String name = nameNode.getText();
		if (name.equals("invokeinterface")) {
			InvokeInterfaceInstruction instr = new InvokeInterfaceInstruction(OpCodes.invokeinterface, null);
			instr.setCpEntryReference(createSymbolReference(ctx.Identifier()));
			instr.setSourceLocation(createSourceLocation(nameNode));
			setInstructionLabel(ctx, instr);
			addInstruction(instr);
		} else if (name.equals("invokedynamic")) {
			InvokeDynamicInstruction instr = new InvokeDynamicInstruction(OpCodes.invokedynamic, null);
			instr.setCpEntryReference(createSymbolReference(ctx.Identifier()));
			instr.setSourceLocation(createSourceLocation(nameNode));
			setInstructionLabel(ctx, instr);
			addInstruction(instr);
		} else if (name.equals("ldc2")) {
			Short code = OpCodes.ldc2_w;
			ConstantPoolInstruction instr = new ConstantPoolInstruction(code, null);
			instr.setCpEntryReference(createSymbolReference(ctx.Identifier()));
			instr.setSourceLocation(createSourceLocation(nameNode));
			setInstructionLabel(ctx, instr);
			addInstruction(instr);
		} else {
			Short code = OpCodes.getOpcodeForName(name);
			ConstantPoolInstruction instr = new ConstantPoolInstruction(code, null);
			instr.setCpEntryReference(createSymbolReference(ctx.Identifier()));
			instr.setSourceLocation(createSourceLocation(nameNode));
			setInstructionLabel(ctx, instr);
			addInstruction(instr);
		}
	}
	
	


	@Override
	public void enterLdcop(LdcopContext ctx) {
		boolean isWide = ctx.WIDE() != null;
		LdcInstruction instr = new LdcInstruction(isWide);
		instr.setCpEntryReference(createSymbolReference(ctx.Identifier()));
		instr.setSourceLocation(createSourceLocation(ctx.LDC()));
		setInstructionLabel(ctx, instr);
		addInstruction(instr);
	}

	@Override
	public void enterLocalvarop(LocalvaropContext ctx) {
		String name = ctx.localvarop_keyword().getText();
		
		boolean wide = (ctx.wideOrNormal() != null) && ctx.wideOrNormal().getText().equals("wide");
		boolean normal = (ctx.wideOrNormal() != null) && ctx.wideOrNormal().getText().equals("normal");
		Short code = OpCodes.getOpcodeForName(name);
		
		LocalVariableInstruction instr = new LocalVariableInstruction(code, wide, -1);
		if (normal) {
			instr.setForceNormal(normal);
		}
		instr.setLocalVariableReference(createSymbolReference(ctx.Identifier()));
		instr.setSourceLocation(createSourceLocation((TerminalNode)ctx.localvarop_keyword().getChild(0)));
		setInstructionLabel(ctx, instr);
		addInstruction(instr);
	}
	
	
	
	


	@Override
	public void enterRetop(RetopContext ctx) {
		String name = ctx.retop_keyword().getText();
		
		Short code = OpCodes.getOpcodeForName(name);
		
		LocalVariableInstruction instr = new LocalVariableInstruction(code, false, -1);
		
		instr.setLocalVariableReference(createSymbolReference(ctx.Identifier()));
		instr.setSourceLocation(createSourceLocation((TerminalNode)ctx.retop_keyword().getChild(0)));
		setInstructionLabel(ctx, instr);
		addInstruction(instr);
	}

	@Override
	public void enterPushop(PushopContext ctx) {
		AbstractPushInstruction instr = null;
		String name = ctx.pushop_keyword().getText();
		if (name.equals(OpCodes.getNameForOpcode(OpCodes.bipush))) {
			instr = new BipushInstruction((byte)-1);
		} else if (name.equals(OpCodes.getNameForOpcode(OpCodes.sipush))) {
			instr = new SipushInstruction((short)-1);
		} else {
			throw new IllegalStateException("unexpected insruction name: "+name);
		}
		instr.setValueLiteral(createIntegerLiteral(ctx.IntegerLiteral()));
		instr.setSourceLocation(createSourceLocation((TerminalNode)ctx.pushop_keyword().getChild(0)));
		setInstructionLabel(ctx, instr);
		addInstruction(instr);
	}
	
	@Override
	public void enterIincop(IincopContext ctx) {
		boolean isWide = ctx.WIDE() != null;
		IincInstruction instr = new IincInstruction(-1, (short)-1, isWide);
		instr.setValueLiteral(createIntegerLiteral(ctx.IntegerLiteral()));
		instr.setLocalVariableReference(createSymbolReference(ctx.Identifier()));
		instr.setSourceLocation(createSourceLocation((TerminalNode)ctx.iincop_keyword().getChild(0)));
		setInstructionLabel(ctx, instr);
		addInstruction(instr);
		
	}
	
	

	@Override
	public void enterNewarrayop(NewarrayopContext ctx) {
		NewarrayInstruction instr = new NewarrayInstruction(ctx.arrayType().getText());
		instr.setSourceLocation(createSourceLocation((TerminalNode)ctx.newarrayop_keyword().getChild(0)));
		setInstructionLabel(ctx, instr);
		addInstruction(instr);
	}
	
	@Override
	public void enterMultinewarrayop(MultinewarrayopContext ctx) {
		MultianewarrayInstruction instr = new MultianewarrayInstruction();
		instr.setCpEntryReference(createSymbolReference(ctx.Identifier()));
		instr.setDimensionsLiteral(createIntegerLiteral(ctx.IntegerLiteral()));
		instr.setSourceLocation(createSourceLocation((TerminalNode)ctx.multinewarrayop_keyword().getChild(0)));
		setInstructionLabel(ctx, instr);
		addInstruction(instr);
	}
	
	


	@Override
	public void enterSwitchop(SwitchopContext ctx) {
		String name = ctx.switchop_keyword().getText();
		short opCode = OpCodes.getOpcodeForName(name);
		AbstractSwitchInstruction instr = null;
		if (opCode == OpCodes.lookupswitch) {
			instr = new LookupSwitchInstruction();
		} else if (opCode == OpCodes.tableswitch) {
			instr = new TableSwitchInstruction();
		}
		instr.setSourceLocation(createSourceLocation((TerminalNode)ctx.switchop_keyword().getChild(0)));
		setInstructionLabel(ctx, instr);
		stack.push(instr);
	}
	
	


	@Override
	public void enterSwitchMember(SwitchMemberContext ctx) {
		TerminalNode defLit = ctx.switchSource().DEFAULT();
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
		String name = ctx.branchop_keyword().getText();
		short opCode = OpCodes.getOpcodeForName(name);
		BranchInstruction instr = new BranchInstruction(opCode, null, name.endsWith("_w"));
		instr.setTargetReference(createSymbolReference(ctx.Identifier()));
		instr.setSourceLocation(createSourceLocation((TerminalNode)ctx.branchop_keyword().getChild(0)));
		setInstructionLabel(ctx, instr);
		addInstruction(instr);
	}
	
	private void setInstructionLabel(ParserRuleContext context, AbstractInstruction instr) {
		MethodinstructionContext ctx = (MethodinstructionContext)context.getParent();
		if (ctx.label() != null) {
			instr.setLabel(createLabel(ctx.label().Identifier()));
		}
		
	}
	
	
	

	@Override
	public void enterMethodexceptionhandler(MethodexceptionhandlerContext ctx) {
		ExceptionHandler handler = new ExceptionHandler();
		handler.setStartSymbolReference(createSymbolReference(ctx.Identifier(0)));
		handler.setEndSymbolReference(createSymbolReference(ctx.Identifier(1)));
		handler.setHandlerSymbolReference(createSymbolReference(ctx.Identifier(2)));
		
		if (ctx.identifierOrAll().Identifier() != null) {
			SymbolReference ref = createSymbolReference(ctx.identifierOrAll().Identifier());
			handler.setCatchTypeReference(ref);
		}
		
		if (ctx.label() != null) {
			handler.setLabel(ctx.label().getText());
		}
		
		handler.setSourceLocation(createSourceLocation(ctx.TRY()));
		addExceptionHandler(handler);
	}


	private void addVar(LocalVariable var) {
		CodeAttributeContent content = getAttributeContentCreatingIfNecessary(CodeAttributeContent.class);
		content.getInstructions().getVariablesPool().addVariable(var);
	}
	
	
	
	private void addExceptionHandler(ExceptionHandler handler) {
		CodeAttributeContent content = getAttributeContentCreatingIfNecessary(CodeAttributeContent.class);
		content.getExceptionTable().add(handler);
		if (handler.getSymbolName() != null) {
			if (!content.getExceptionTable().getSymbolTable().contains(handler.getSymbolName())) {
				content.getExceptionTable().getSymbolTable().add(handler);
			} else {
				emitError(handler.getSourceLocation().getLine(), handler.getSourceLocation().getCharPosition(), "dublicate exception handler label "+handler.getSymbolName());
			}
		}
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
	public void enterClassAttributeSyntetic(ClassAttributeSynteticContext ctx) {
		// TODO Auto-generated method stub
		super.enterClassAttributeSyntetic(ctx);
	}
	
	

	@Override
	public void enterEnclosingmethod(EnclosingmethodContext ctx) {
		EnclosingMethodAttributeContent content = new EnclosingMethodAttributeContent();
		content.setClazzReference(createSymbolReference(ctx.Identifier(0)));
		if (ctx.Identifier().size()==2) {
			content.setMethodReference(createSymbolReference(ctx.Identifier(1)));
		}
		addAttribute(content, ctx.ENCLOSING());
	}
	
	@Override
	public void enterBootstrapmethod(BootstrapmethodContext ctx) {
		BootstrapMethod method = new BootstrapMethod();
		method.setSymbolName(ctx.Identifier(0).getText());
		method.setMethodHandleReference(createSymbolReference(ctx.Identifier(1)));
		if (ctx.Identifier().size()>2) {
			for (int i=2; i<ctx.Identifier().size(); i++) {
				method.addParamReference(createSymbolReference(ctx.Identifier(i)));
			}
		}
		addBootstrapMethod(method);
	}
	
	//Annotations

	@Override
	public void enterAnnotationdeclaration(AnnotationdeclarationContext ctx) {
		boolean rootAnnotation = (ctx.getParent() instanceof AnnotationContext);
		boolean nestedAnnotation = (ctx.getParent() instanceof AnnotationelementvalueContext);
		Annotation annot = new Annotation();
		if (rootAnnotation) {
			
			
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
	public void exitAnnotation(AnnotationContext ctx) {
		boolean invisible = ctx.INVISIBLE() != null;
		Annotation annot = (Annotation)stack.peek();
		stack.pop();
		
		IAttributeContent content = null;
		if (invisible) {
			content = getAttributeContentCreatingIfNecessary(RuntimeInvisibleAnnotationsAttributeContent.class);
		} else {
			content = getAttributeContentCreatingIfNecessary(RuntimeVisibleAnnotationsAttributeContent.class);
		}
		
		((AbstractAnnotationsAttributeContent)content).add(annot);
		if (invisible) {
			annot.setSourceLocation(createSourceLocation(ctx.INVISIBLE()));
		} else {
			annot.setSourceLocation(createSourceLocation(ctx.annotationdeclaration().ANNOTATION()));
		}
		if (((AbstractAnnotationsAttributeContent)content).getSourceLocation() == null) {
			((AbstractAnnotationsAttributeContent)content).setSourceLocation(annot.getSourceLocation());
		}
	}
	
	@Override
	public void enterParameterannotation(ParameterannotationContext ctx) {
		Annotation annot = new Annotation();
		boolean invisible = ctx.INVISIBLE() != null;
		IAttributeContent content = null;
		if (invisible) {
			content = getAttributeContentCreatingIfNecessary(RuntimeInvisibleParameterAnnotationsAttributeContent.class);
		} else {
			content = getAttributeContentCreatingIfNecessary(RuntimeVisibleParameterAnnotationsAttributeContent.class);
		}
		((AbstractParameterAnnotationsAttributeContent)content).addAnnotation(annot);
		if (invisible) {
			annot.setSourceLocation(createSourceLocation(ctx.INVISIBLE()));
		} else {
			annot.setSourceLocation(createSourceLocation(ctx.parameterannotationdeclaration().ANNOTATION()));
		}
		if (((AbstractParameterAnnotationsAttributeContent)content).getSourceLocation() == null) {
			((AbstractParameterAnnotationsAttributeContent)content).setSourceLocation(annot.getSourceLocation());
		}
		stack.push(annot);
	}
	

	@Override
	public void exitParameterannotation(ParameterannotationContext ctx) {
		stack.pop();
	}
	
	
	


	@Override
	public void enterTypeannotation(TypeannotationContext ctx) {
		Annotation annot = new Annotation();
		annot.setTypeAnnotation(true);
		stack.push(annot);
	}
	
	


	@Override
	public void exitTypeannotation(TypeannotationContext ctx) {
		Annotation annot = (Annotation)stack.peek();
		stack.pop();
		boolean invisible = ctx.INVISIBLE() != null;
		IAttributeContent content = null;
		
		boolean isCode = isCodeTypeAnnotation(annot) && (stack.peek() instanceof Method);
		if (isCode) {
			CodeAttributeContent code = getAttributeContentCreatingIfNecessary(CodeAttributeContent.class);
			stack.push(code);
		}
		
		if (invisible) {
			content = getAttributeContentCreatingIfNecessary(RuntimeInvisibleTypeAnnotationsAttributeContent.class);
		} else {
			content = getAttributeContentCreatingIfNecessary(RuntimeVisibleTypeAnnotationsAttributeContent.class);
		}
		
		if (isCode) {
			stack.pop();
		}
		
		
		((AbstractAnnotationsAttributeContent)content).add(annot);
		if (invisible) {
			annot.setSourceLocation(createSourceLocation(ctx.INVISIBLE()));
		} else {
			annot.setSourceLocation(createSourceLocation(ctx.typeannotationdeclaration().ANNOTATION()));
		}
		if (((AbstractAnnotationsAttributeContent)content).getSourceLocation() == null) {
			((AbstractAnnotationsAttributeContent)content).setSourceLocation(annot.getSourceLocation());
		}
	}
	
	private boolean isCodeTypeAnnotation(Annotation annot) {
		return  (annot.getTarget().getTargetType()>=JasmConsts.ANNOTATION_TARGET_LOCAL_VAR)
				&& (annot.getTarget().getTargetType()<=JasmConsts.ANNOTATION_TARGET_GENERIC_METHOD_TYPE_ARGUMENT_IN_METHOD_REF);
	}
	

	@Override
	public void enterEmptyTargetReceiverType(EmptyTargetReceiverTypeContext ctx) {
		Annotation annot = (Annotation)stack.peek();
		EmptyAnnotationTargetType target = new EmptyAnnotationTargetType(JasmConsts.ANNOTATION_TARGET_RECEIVER_TYPE);
		target.setSourceLocation(createSourceLocation(ctx.TARGETS()));
		annot.setTarget(target);
	}


	@Override
	public void enterEmptyTargetReturnType(EmptyTargetReturnTypeContext ctx) {
		Annotation annot = (Annotation)stack.peek();
		EmptyAnnotationTargetType target = new EmptyAnnotationTargetType(JasmConsts.ANNOTATION_TARGET_RETURN_TYPE);
		target.setSourceLocation(createSourceLocation(ctx.TARGETS()));
		annot.setTarget(target);
	}


	@Override
	public void enterEmptyTargetFieldType(EmptyTargetFieldTypeContext ctx) {
		Annotation annot = (Annotation)stack.peek();
		EmptyAnnotationTargetType target = new EmptyAnnotationTargetType(JasmConsts.ANNOTATION_TARGET_FIELD);
		target.setSourceLocation(createSourceLocation(ctx.TARGETS()));
		annot.setTarget(target);
	}
	
	


	@Override
	public void enterParameterTypeTargetType(ParameterTypeTargetTypeContext ctx) {
		Annotation annot = (Annotation)stack.peek();
		TypeParameterAnnotationTargetType target = new TypeParameterAnnotationTargetType();
		target.setIndexLiteral(createIntegerLiteral(ctx.IntegerLiteral()));
		target.setSourceLocation(createSourceLocation(ctx.TARGETS()));
		annot.setTarget(target);
	}
	
	


	@Override
	public void enterParameterTypeBoundTargetType(
			ParameterTypeBoundTargetTypeContext ctx) {
		Annotation annot = (Annotation)stack.peek();
		TypeParameterBoundAnnotationTargetType target = new TypeParameterBoundAnnotationTargetType();
		target.setParameterIndexLiteral(createIntegerLiteral(ctx.IntegerLiteral(0)));
		target.setBoundIndexLiteral(createIntegerLiteral(ctx.IntegerLiteral(1)));
		target.setSourceLocation(createSourceLocation(ctx.TARGETS()));
		annot.setTarget(target);
	}
	
	


	@Override
	public void enterSupertypeTargetType(SupertypeTargetTypeContext ctx) {
		Annotation annot = (Annotation)stack.peek();
		SupertypeAnnotationTargetType target = new SupertypeAnnotationTargetType();
		target.setTargetType(JasmConsts.ANNOTATION_TARGET_SUPERTYPE);
		if (ctx.Identifier() != null) {
			target.setIndexSymbolReference(createSymbolReference(ctx.Identifier()));
		}
		target.setSourceLocation(createSourceLocation(ctx.TARGETS()));
		annot.setTarget(target);
	}
	
	


	@Override
	public void enterThrowstypeTargetType(ThrowstypeTargetTypeContext ctx) {
		Annotation annot = (Annotation)stack.peek();
		ThrowsAnnotationTargetType target = new ThrowsAnnotationTargetType();
		target.setTargetType(JasmConsts.ANNOTATION_TARGET_THROWS);
		target.setIndexSymbolReference(createSymbolReference(ctx.Identifier()));
		target.setSourceLocation(createSourceLocation(ctx.TARGETS()));
		annot.setTarget(target);
	}
	
	@Override
	public void enterMethodreferencetypeTargetType(
			MethodreferencetypeTargetTypeContext ctx) {
		createOffsetTargetType(ctx.TARGETS(), ctx.Identifier(), JasmConsts.ANNOTATION_TARGET_METHOD_REF_ID);
	}


	@Override
	public void enterConstructorreferencetypeTargetType(
			ConstructorreferencetypeTargetTypeContext ctx) {
		createOffsetTargetType(ctx.TARGETS(), ctx.Identifier(), JasmConsts.ANNOTATION_TARGET_METHOD_REF_NEW);
	}


	@Override
	public void enterInstanceoftypeTargetType(
			InstanceoftypeTargetTypeContext ctx) {
		createOffsetTargetType(ctx.TARGETS(), ctx.Identifier(), JasmConsts.ANNOTATION_TARGET_INSTANCEOF);
	}

	@Override
	public void enterNewtypeTargetType(NewtypeTargetTypeContext ctx) {
		createOffsetTargetType(ctx.TARGETS(), ctx.Identifier(), JasmConsts.ANNOTATION_TARGET_NEW);
	}
	
	private void createOffsetTargetType(TerminalNode beginNode, TerminalNode idNode, short targetType) {
		Annotation annot = (Annotation)stack.peek();
		OffsetAnnotationTargetType target = new OffsetAnnotationTargetType();
		target.setTargetType(targetType);
		target.setInstructionReference(createSymbolReference(idNode));
		target.setSourceLocation(createSourceLocation(beginNode));
		annot.setTarget(target);
	}
	
	@Override
	public void enterMethodtypeargumentTargetType(
			MethodtypeargumentTargetTypeContext ctx) {
		createTypeArgumentTargetType(ctx.TARGETS(), ctx.Identifier(), ctx.IntegerLiteral(), JasmConsts.ANNOTATION_TARGET_GENERIC_METHOD_TYPE_ARGUMENT);
	}

	@Override
	public void enterConstructorreferencetypeargumentTargetType(
			ConstructorreferencetypeargumentTargetTypeContext ctx) {
		createTypeArgumentTargetType(ctx.TARGETS(), ctx.Identifier(), ctx.IntegerLiteral(), JasmConsts.ANNOTATION_TARGET_GENERIC_CONSTRUCTOR_TYPE_ARGUMENT_IN_METHOD_REF);
	}

	@Override
	public void enterConstructortypeargumentTargetType(
			ConstructortypeargumentTargetTypeContext ctx) {
		createTypeArgumentTargetType(ctx.TARGETS(), ctx.Identifier(), ctx.IntegerLiteral(), JasmConsts.ANNOTATION_TARGET_GENERIC_CONSTRUCTOR_TYPE_ARGUMENT);
	}

	@Override
	public void enterCasttypeTargetType(CasttypeTargetTypeContext ctx) {
		createTypeArgumentTargetType(ctx.TARGETS(), ctx.Identifier(), ctx.IntegerLiteral(), JasmConsts.ANNOTATION_TARGET_CAST);
	}


	@Override
	public void enterMethodreferencetypeargumentTargetType(
			MethodreferencetypeargumentTargetTypeContext ctx) {
		createTypeArgumentTargetType(ctx.TARGETS(), ctx.Identifier(), ctx.IntegerLiteral(), JasmConsts.ANNOTATION_TARGET_GENERIC_METHOD_TYPE_ARGUMENT_IN_METHOD_REF);
	}

	private void createTypeArgumentTargetType(TerminalNode beginNode, TerminalNode idNode,TerminalNode indexNode, short targetType) {
		Annotation annot = (Annotation)stack.peek();
		TypeArgumentAnnotationTargetType target = new TypeArgumentAnnotationTargetType();
		target.setTargetType(targetType);
		target.setInstructionReference(createSymbolReference(idNode));
		target.setParameterIndexLiteral(createIntegerLiteral(indexNode));
		target.setSourceLocation(createSourceLocation(beginNode));
		annot.setTarget(target);
	}
	
	@Override
	public void enterFormalparametertypeTargetType(FormalparametertypeTargetTypeContext ctx) {
		Annotation annot = (Annotation)stack.peek();
		FormalParameterAnnotationTargetType target = new FormalParameterAnnotationTargetType();
		target.setTargetType(JasmConsts.ANNOTATION_TARGET_FORMAL_PARAMETER);
		target.setIndexLiteral(createIntegerLiteral(ctx.IntegerLiteral()));
		target.setSourceLocation(createSourceLocation(ctx.TARGETS()));
		annot.setTarget(target);
	}
	
	

	@Override
	public void enterCatchtypeTargetType(CatchtypeTargetTypeContext ctx) {
		Annotation annot = (Annotation)stack.peek();
		CatchAnnotationTargetType target = new CatchAnnotationTargetType();
		target.setTargetType(JasmConsts.ANNOTATION_TARGET_CATCH);
		target.setHandlerReference(createSymbolReference(ctx.Identifier()));
		target.setSourceLocation(createSourceLocation(ctx.TARGETS()));
		annot.setTarget(target);
		
	}


	@Override
	public void enterLocalvartypeTargetType(LocalvartypeTargetTypeContext ctx) {
		Annotation annot = (Annotation)stack.peek();
		LocalVariableAnnotationTargetType target = new LocalVariableAnnotationTargetType();
		if (ctx.RESOURCE() !=null) {
			target.setTargetType(JasmConsts.ANNOTATION_TARGET_RESOURCE_VAR);
		} else {
			target.setTargetType(JasmConsts.ANNOTATION_TARGET_LOCAL_VAR);
		}
		target.setSourceLocation(createSourceLocation(ctx.TARGETS()));
		annot.setTarget(target);
	}
	
	@Override
	public void enterLocalvartypemember(LocalvartypememberContext ctx) {
		Annotation annot = (Annotation)stack.peek();
		LocalVariableAnnotationTargetType target = (LocalVariableAnnotationTargetType)annot.getTarget();
		LocalVariableAnnotationTargetTypeMember member = new LocalVariableAnnotationTargetTypeMember();
		member.setStartInstructionReference(createSymbolReference(ctx.Identifier(0)));
		if (ctx.Identifier().size() == 2) {
			member.setEndInstructionReference(createSymbolReference(ctx.Identifier(1)));
		}
		if (ctx.localvartypemember_vararg().IntegerLiteral() != null) {
			member.setVariableLiteral(createIntegerLiteral(ctx.localvartypemember_vararg().IntegerLiteral()));
		} else if (ctx.localvartypemember_vararg().Identifier() != null) {
			member.setVariableReference(createSymbolReference(ctx.localvartypemember_vararg().Identifier()));
		} else {
			throw new IllegalStateException();
		}
		target.addMember(member);
	}
	
	


	@Override
	public void enterAnnotationtargetpath(AnnotationtargetpathContext ctx) {
		Annotation annot = (Annotation)stack.peek();
		AnnotationTargetTypePath path = new AnnotationTargetTypePath(new short[ctx.annotationtargetpath_arg().size()], new short[ctx.annotationtargetpath_arg().size()]);
		path.setSourceLocation(createSourceLocation(ctx.TARGET()));
		annot.setTargetPath(path);
	}
	

	@Override
	public void enterTargetPathTypeArgumentBound(
			TargetPathTypeArgumentBoundContext ctx) {
		Annotation annot = (Annotation)stack.peek();
		AnnotationTargetTypePath path = (AnnotationTargetTypePath)annot.getTargetPath();
		path.setArgument(JasmConsts.ANNOTATION_TARGET_TYPE_PATHKIND_TYPE_ARGUMENT, null);
	}


	@Override
	public void enterTargetPathTypeArgument(TargetPathTypeArgumentContext ctx) {
		Annotation annot = (Annotation)stack.peek();
		AnnotationTargetTypePath path = (AnnotationTargetTypePath)annot.getTargetPath();
		path.setArgument(JasmConsts.ANNOTATION_TARGET_TYPE_PATHKIND_TYPE_ARGUMENT, createIntegerLiteral(ctx.IntegerLiteral()));
	}


	@Override
	public void enterTargetPathNested(TargetPathNestedContext ctx) {
		Annotation annot = (Annotation)stack.peek();
		AnnotationTargetTypePath path = (AnnotationTargetTypePath)annot.getTargetPath();
		path.setArgument(JasmConsts.ANNOTATION_TARGET_TYPE_PATHKIND_NESTED, null);
	}


	@Override
	public void enterTargetPathArray(TargetPathArrayContext ctx) {
		Annotation annot = (Annotation)stack.peek();
		AnnotationTargetTypePath path = (AnnotationTargetTypePath)annot.getTargetPath();
		path.setArgument(JasmConsts.ANNOTATION_TARGET_TYPE_PATHKIND_ARRAY, null);
	}


	@Override
	public void enterAnnotationdefault(AnnotationdefaultContext ctx) {
		AnnotationDefaultAttributeContent content = new AnnotationDefaultAttributeContent();
		addAttribute(content, ctx.ANNOTATION());
		stack.push(content);
	}
	
	

	@Override
	public void exitAnnotationdefault(AnnotationdefaultContext ctx) {
		stack.pop();
	}


	@Override
	public void enterAnnotationtype(AnnotationtypeContext ctx) {
		Annotation annot = (Annotation)stack.peek();
		annot.setTypeValueReference(createSymbolReference(ctx.Identifier()));
	}
	
	
	
	

	@Override
	public void enterAnnotationparameterindex(
			AnnotationparameterindexContext ctx) {
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
		} else if (stack.peek() instanceof AnnotationDefaultAttributeContent) {
			AnnotationDefaultAttributeContent content = (AnnotationDefaultAttributeContent)stack.peek();
			content.setValue(value);
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
		Annotation annot = (Annotation)stack.peek();
		if (annot.isNested()) {
			stack.pop();
		}
	}
	

	@Override
	public void exitArrayannotationelementvalue(
			ArrayannotationelementvalueContext ctx) {
		stack.pop();
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
	
	

	@Override
	public void enterUnknownattribute(UnknownattributeContext ctx) {
		UnknownAttributeContent content = new UnknownAttributeContent();
		content.setDataLiteral(createBase64Literal(ctx.Base64Literal()));
		
		boolean onCode = (ctx.CODE() != null);
		Attribute attr = null;
		if (onCode) {
			CodeAttributeContent content2 = getAttributeContentCreatingIfNecessary(CodeAttributeContent.class);
			stack.push(content2);
			attr = addAttribute(content, ctx.UNKNOWN());
			stack.pop();
		} else {
			attr = addAttribute(content, ctx.UNKNOWN());
		}
		
		attr.setNameReference(createSymbolReference(ctx.Identifier()));
	}
	
	
	@Override
	public void enterExtendedStackmapAttribute(ExtendedStackmapAttributeContext ctx) {
		StackMapAttributeContent content = new StackMapAttributeContent();
		CodeAttributeContent code = getAttributeContentCreatingIfNecessary(CodeAttributeContent.class);
		if (code.getAttributes().getAttributesByContentType(StackMapAttributeContent.class).isEmpty()) {
			stack.push(code);
			addAttribute(content, ctx.STACKMAP());
			stack.pop();
			stack.push(content);
		} else {
			emitError(ctx.STACKMAP(), "dublicate stackmap declaration");
		}
	}
	
	
	
	
	@Override
	public void enterSimpleStackmapAttribute(SimpleStackmapAttributeContext ctx) {
		Method m = (Method)stack.peek();
		m.setGenerateStackMap(true);
	}

	@Override
	public void enterSameStackmapFrame(SameStackmapFrameContext ctx) {
		StackMapAttributeContent content = (StackMapAttributeContent)stack.peek();
		SameStackmapFrame stackframe = new SameStackmapFrame();
		stackframe.setInstructionReference(createSymbolReference(ctx.Identifier()));
		stackframe.setSourceLocation(createSourceLocation(ctx.SAME()));
		content.add(stackframe);
	}

	@Override
	public void enterSameExtendedStackmapFrame(
			SameExtendedStackmapFrameContext ctx) {
		StackMapAttributeContent content = (StackMapAttributeContent)stack.peek();
		SameExtendedStackmapFrame stackframe = new SameExtendedStackmapFrame();
		stackframe.setInstructionReference(createSymbolReference(ctx.Identifier()));
		stackframe.setSourceLocation(createSourceLocation(ctx.SAME()));
		content.add(stackframe);
	}
	
	

	@Override
	public void enterChopStackmapFrame(ChopStackmapFrameContext ctx) {
		StackMapAttributeContent content = (StackMapAttributeContent)stack.peek();
		ChopStackmapFrame stackframe = new ChopStackmapFrame();
		stackframe.setInstructionReference(createSymbolReference(ctx.Identifier()));
		stackframe.setkLiteral(createIntegerLiteral(ctx.IntegerLiteral()));
		stackframe.setSourceLocation(createSourceLocation(ctx.CHOP()));
		content.add(stackframe);
	}

	@Override
	public void enterFullStackmapFrame(FullStackmapFrameContext ctx) {
		StackMapAttributeContent content = (StackMapAttributeContent)stack.peek();
		FullStackmapFrame stackframe = new FullStackmapFrame ();
		stackframe.setInstructionReference(createSymbolReference(ctx.Identifier()));
		stackframe.setSourceLocation(createSourceLocation(ctx.FULL()));
		content.add(stackframe);
		stack.push(stackframe);
	}
	
	
	
	@Override
	public void enterStackitemstackmapvarinfos(
			StackitemstackmapvarinfosContext ctx) {
		FullStackmapFrame info = (FullStackmapFrame)stack.peek();
		info.switchAddingToStackItems();
	}

	@Override
	public void enterSameLocalsExtendedStackmapFrame(
			SameLocalsExtendedStackmapFrameContext ctx) {
		StackMapAttributeContent content = (StackMapAttributeContent)stack.peek();
		SameLocalsOneStackitemExtendedStackmapFrame stackframe = new SameLocalsOneStackitemExtendedStackmapFrame ();
		stackframe.setInstructionReference(createSymbolReference(ctx.Identifier()));
		stackframe.setSourceLocation(createSourceLocation(ctx.SAME()));
		content.add(stackframe);
		stack.push(stackframe);
	}
		

	@Override
	public void enterAppendStackmapFrame(AppendStackmapFrameContext ctx) {
		StackMapAttributeContent content = (StackMapAttributeContent)stack.peek();
		AppendStackmapFrame stackframe = new AppendStackmapFrame();
		stackframe.setInstructionReference(createSymbolReference(ctx.Identifier()));
		stackframe.setSourceLocation(createSourceLocation(ctx.APPEND()));
		content.add(stackframe);
		stack.push(stackframe);
	}

	@Override
	public void enterSameLocalsStackmapFrame(SameLocalsStackmapFrameContext ctx) {
		StackMapAttributeContent content = (StackMapAttributeContent)stack.peek();
		SameLocalsOneStackitemStackmapFrame stackframe = new SameLocalsOneStackitemStackmapFrame ();
		stackframe.setInstructionReference(createSymbolReference(ctx.Identifier()));
		stackframe.setSourceLocation(createSourceLocation(ctx.SAME()));
		content.add(stackframe);
		stack.push(stackframe);
	}
	
	@Override
	public void enterIntStackmapvarinfo(IntStackmapvarinfoContext ctx) {
		IntegerStackmapVariableinfo info = new IntegerStackmapVariableinfo();
		info.setSourceLocation(createSourceLocation(ctx.INT()));
		addStackmapVarinfo(info);
	}

	@Override
	public void enterUninitializedStackmapvarinfo(
			UninitializedStackmapvarinfoContext ctx) {
		UninitializedStackmapVariableinfo info = new UninitializedStackmapVariableinfo();
		info.setSourceLocation(createSourceLocation(ctx.UNINITIALIZED()));
		info.setInstructionReference(createSymbolReference(ctx.Identifier()));
		addStackmapVarinfo(info);
	}

	@Override
	public void enterDoubleStackmapvarinfo(DoubleStackmapvarinfoContext ctx) {
		DoubleStackmapVariableinfo info = new DoubleStackmapVariableinfo();
		info.setSourceLocation(createSourceLocation(ctx.DOUBLE()));
		addStackmapVarinfo(info);
	}

	@Override
	public void enterLongStackmapvarinfo(LongStackmapvarinfoContext ctx) {
		LongStackmapVariableinfo info = new LongStackmapVariableinfo();
		info.setSourceLocation(createSourceLocation(ctx.LONG()));
		addStackmapVarinfo(info);
	}
	
	@Override
	public void enterNullStackmapvarinfo(NullStackmapvarinfoContext ctx) {
		NullStackmapVariableinfo info = new NullStackmapVariableinfo();
		info.setSourceLocation(createSourceLocation(ctx.NULL()));
		addStackmapVarinfo(info);
	}

	@Override
	public void enterTopStackmapvarinfo(TopStackmapvarinfoContext ctx) {
		TopStackmapVariableinfo info = new TopStackmapVariableinfo();
		info.setSourceLocation(createSourceLocation(ctx.TOP()));
		addStackmapVarinfo(info);
	}

	@Override
	public void enterObjectStackmapvarinfo(ObjectStackmapvarinfoContext ctx) {
		ObjectStackmapVariableinfo info = new ObjectStackmapVariableinfo();
		info.setSourceLocation(createSourceLocation(ctx.OBJECT()));
		info.setClassInfoReference(createSymbolReference(ctx.Identifier()));
		addStackmapVarinfo(info);
	}

	@Override
	public void enterFloatStackmapvarinfo(FloatStackmapvarinfoContext ctx) {
		FloatStackmapVariableinfo info = new FloatStackmapVariableinfo();
		info.setSourceLocation(createSourceLocation(ctx.FLOAT()));
		addStackmapVarinfo(info);
	}

	@Override
	public void enterUninitializedThisStackmapvarinfo(
			UninitializedThisStackmapvarinfoContext ctx) {
		UninitializedThisStackmapVariableinfo info = new UninitializedThisStackmapVariableinfo();
		info.setSourceLocation(createSourceLocation(ctx.UNINITIALIZEDTHIS()));
		addStackmapVarinfo(info);
	}
	
	private void addStackmapVarinfo(AbstractStackmapVariableinfo info) {
		IStackmapVariableinfoContainer cont = (IStackmapVariableinfoContainer)stack.peek();
		cont.addVariableInfo(info);
	}

	@Override
	public void exitExtendedStackmapAttribute(ExtendedStackmapAttributeContext ctx) {
		stack.pop();
	}
	
	

	@Override
	public void exitFullStackmapFrame(FullStackmapFrameContext ctx) {
		stack.pop();
	}

	@Override
	public void exitSameLocalsExtendedStackmapFrame(
			SameLocalsExtendedStackmapFrameContext ctx) {
		stack.pop();
	}

	@Override
	public void exitAppendStackmapFrame(AppendStackmapFrameContext ctx) {
		stack.pop();
	}

	@Override
	public void exitSameLocalsStackmapFrame(SameLocalsStackmapFrameContext ctx) {
		stack.pop();
	}

	public void emitError(TerminalNode node, String message) {
		emitError(node.getSymbol().getLine(), node.getSymbol().getCharPositionInLine(), message);
	}
	
	private <T extends ParserRuleContext> T getAncestor(ParserRuleContext context, Class<T> clazz) {
		T result = (T)context.getParent();
		while (result != null && !result.getClass().equals(clazz)) {
			result = (T)result.getParent();
		}
		return result;
	}
	
	
	private void addConstantPoolEntry(AbstractConstantPoolEntry entry) {
		ConstantPool pool = ((Clazz)stack.peek()).getConstantPool();
		pool.add(entry);
		if (!pool.getSymbolTable().contains(entry.getSymbolName())) {
			pool.getSymbolTable().add(entry);
		} else {
			emitError(entry.getSourceLocation().getLine(), entry.getSourceLocation().getCharPosition(), "dublicate constant declaration "+entry.getSymbolName());
		}
	}
	
	private void addBootstrapMethod(BootstrapMethod entry) {
		BootstrapMethodsAttributeContent pool = getAttributeContentCreatingIfNecessary(BootstrapMethodsAttributeContent.class);
		pool.add(entry);
		if (!pool.getSymbolTable().contains(entry.getSymbolName())) {
			pool.getSymbolTable().add(entry);
		} else {
			emitError(entry.getSourceLocation().getLine(), entry.getSourceLocation().getCharPosition(), "dublicate bootstrap method declaration "+entry.getSymbolName());
		}
	}
	

	private SourceLocation createSourceLocation(TerminalNode node) {
		return new SourceLocation(node.getSymbol().getLine(), node.getSymbol().getCharPositionInLine());
	}
	
	private SymbolReference createSymbolReference(TerminalNode node) {
		return new SymbolReference(node.getSymbol().getLine(), node.getSymbol().getCharPositionInLine(), node.getText());
	}
	
	private SymbolReference createSymbolReference(LabeledIdentifierContext lic) {
		TerminalNode node = lic.Identifier();
		SymbolReference result =  new SymbolReference(node.getSymbol().getLine(), node.getSymbol().getCharPositionInLine(), node.getText());
		if (lic.label() != null) {
			result.setReferenceLabel(lic.label().getText());
		}
		return result;
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
	
	private Base64Literal createBase64Literal(TerminalNode node) {
		return new Base64Literal(node.getSymbol().getLine(), node.getSymbol().getCharPositionInLine(), node.getText());
	}
	
	
	
	private Attribute addAttribute(IAttributeContent content, TerminalNode node) {
		IAttributesContainer container = (IAttributesContainer)stack.peek();
		Attribute attr = new Attribute();
		if (node != null) {
			attr.setSourceLocation(createSourceLocation(node));
		}
		container.getAttributes().add(attr);
		attr.setContent(content);
		return attr;
	}
	
	private <T extends IAttributeContent> T getAttributeContentCreatingIfNecessary(Class<T> clazz) {
		IAttributesContainer container = (IAttributesContainer)stack.peek();
		return IAttributesContainer.getUniqueAttributeContentCreatingIfNecessary(clazz, container);
		
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
		parent.emitError(line, charPositionInLine, msg);
		if (log.isDebugEnabled()) {
			log.debug("offending symbol: "+offendingSymbol);
			if (e instanceof NoViableAltException) {
				NoViableAltException e1 = (NoViableAltException)e;
				log.debug(e1.getOffendingState()+"");
				log.debug(e1.getDeadEndConfigs().toString());
			}
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
		if (log.isDebugEnabled()) {
			log.debug("AttemptingFullContext");
		}
	}

	@Override
	public void reportContextSensitivity(Parser recognizer, DFA dfa,
			int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
		if (log.isDebugEnabled()) {
			log.debug("ContextSensitivity");
		}
	}
	

	
	
	
}
