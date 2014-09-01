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
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.attribute.Attribute;
import org.jasm.item.attribute.Attributes;
import org.jasm.item.attribute.SourceFileAttributeContent;
import org.jasm.item.clazz.Clazz;
import org.jasm.item.clazz.Method;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.ConstantPool;
import org.jasm.item.constantpool.FieldrefInfo;
import org.jasm.item.constantpool.IntegerInfo;
import org.jasm.item.constantpool.MethodrefInfo;
import org.jasm.item.constantpool.NameAndTypeInfo;
import org.jasm.item.constantpool.StringInfo;
import org.jasm.item.constantpool.Utf8Info;
import org.jasm.item.modifier.ClassModifier;
import org.jasm.parser.JavaAssemblerParser.ClassattributeSourceFileContext;
import org.jasm.parser.JavaAssemblerParser.ClassattributesContext;
import org.jasm.parser.JavaAssemblerParser.ClassinfoContext;
import org.jasm.parser.JavaAssemblerParser.ClassmodifierAbstractContext;
import org.jasm.parser.JavaAssemblerParser.ClassmodifierAnnotationContext;
import org.jasm.parser.JavaAssemblerParser.ClassmodifierEnumContext;
import org.jasm.parser.JavaAssemblerParser.ClassmodifierInterfaceContext;
import org.jasm.parser.JavaAssemblerParser.ClassmodifierPublicContext;
import org.jasm.parser.JavaAssemblerParser.ClassmodifierSuperContext;
import org.jasm.parser.JavaAssemblerParser.ClassmodifierSynteticContext;
import org.jasm.parser.JavaAssemblerParser.ClassnameContext;
import org.jasm.parser.JavaAssemblerParser.ClazzContext;
import org.jasm.parser.JavaAssemblerParser.ConstpoolContext;
import org.jasm.parser.JavaAssemblerParser.FieldrefinfoContext;
import org.jasm.parser.JavaAssemblerParser.IntegerinfoContext;
import org.jasm.parser.JavaAssemblerParser.MethodContext;
import org.jasm.parser.JavaAssemblerParser.MethoddescriptorContext;
import org.jasm.parser.JavaAssemblerParser.MethodmodifierAbstractContext;
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
import org.jasm.parser.JavaAssemblerParser.MethodsContext;
import org.jasm.parser.JavaAssemblerParser.NameandtypeinfoContext;
import org.jasm.parser.JavaAssemblerParser.StringinfoContext;
import org.jasm.parser.JavaAssemblerParser.SuperclassContext;
import org.jasm.parser.JavaAssemblerParser.Utf8infoContext;
import org.jasm.parser.JavaAssemblerParser.VersionContext;
import org.jasm.parser.literals.IntegerLiteral;
import org.jasm.parser.literals.Keyword;
import org.jasm.parser.literals.Label;
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
		clazz.setVersion(createVersionLiteral(ctx.VersionLiteral()));
	}
	
	
	
	@Override
	public void enterClassname(ClassnameContext ctx) {
		Clazz clazz = (Clazz)stack.peek();
		clazz.setThisClassSymbol(createSymbolReference(ctx.Identifier()));
	}
	

	@Override
	public void enterSuperclass(SuperclassContext ctx) {
		Clazz clazz = (Clazz)stack.peek();
		clazz.setSuperClassSymbol(createSymbolReference(ctx.Identifier()));
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
	public void enterConstpool(ConstpoolContext ctx) {
		Clazz clazz = (Clazz)stack.peek();
		clazz.getConstantPool().setSourceLocation(createSourceLocation(ctx.CONSTPOOL()));
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
	public void exitStringinfo(StringinfoContext ctx) {
		StringInfo entry = new StringInfo();
		if (ctx.label() != null) {
			entry.setLabel(createLabel(ctx.label().Identifier()));
		}
		entry.setSourceLocation(createSourceLocation(ctx.STRINGINFO()));
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
		entry.setSourceLocation(createSourceLocation(ctx.INTEGERINFO()));
		entry.setValueLiteral(createIntegerLiteral(ctx.IntegerLiteral()));
		addConstantPoolEntry(entry);
	}


	@Override
	public void enterClassattributes(ClassattributesContext ctx) {
		Clazz clazz = (Clazz)stack.peek();
		clazz.getAttributes().setSourceLocation(createSourceLocation(ctx.ATTRIBUTES()));
	}


	@Override
	public void enterClassattributeSourceFile(
			ClassattributeSourceFileContext ctx) {
		Clazz clazz = (Clazz)stack.peek();
		Attribute attr = new Attribute();
		attr.setSourceLocation(createSourceLocation(ctx.SOURCE()));
		clazz.getAttributes().add(attr);
		SourceFileAttributeContent content = new SourceFileAttributeContent();
		content.setValueLabel(createSymbolReference(ctx.Identifier()));
		attr.setContent(content);
	}
	
	

	@Override
	public void enterMethods(MethodsContext ctx) {
		Clazz clazz = (Clazz)stack.peek();
		clazz.getMethods().setSourceLocation(createSourceLocation(ctx.METHODS()));
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
		m.setDescriptorReference(createSymbolReference(ctx.Identifier()));
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
	public void enterMethodname(MethodnameContext ctx) {
		Method m = (Method)stack.peek();
		m.setNameReference(createSymbolReference(ctx.Identifier()));
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


	public void emitError(int line, int charPosition, String message) {
		errorMessages.add(new ErrorMessage(line, charPosition, message));
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
	
	private Keyword createKeyword(TerminalNode node) {
		return new Keyword(node.getSymbol().getLine(), node.getSymbol().getCharPositionInLine(), node.getText());
	}
	
	private VersionLiteral createVersionLiteral(TerminalNode node) {
		return new VersionLiteral(node.getSymbol().getLine(), node.getSymbol().getCharPositionInLine(), node.getText());
	}
	
	private void createErrorMessageForSymbol(TerminalNode node, String msg) {
		emitError(node.getSymbol().getLine(), node.getSymbol().getCharPositionInLine(), msg);
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
