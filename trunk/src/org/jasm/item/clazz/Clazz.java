package org.jasm.item.clazz;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.bytebuffer.print.SimplePrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.IContainerBytecodeItem;
import org.jasm.item.IErrorEmitter;
import org.jasm.item.attribute.Attributes;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.ConstantPool;
import org.jasm.item.constantpool.IConstantPoolReference;
import org.jasm.item.instructions.Instructions;
import org.jasm.item.modifier.ClassModifier;
import org.jasm.parser.AssemblerParser;
import org.jasm.parser.SourceLocation;
import org.jasm.parser.literals.AbstractLiteral;
import org.jasm.parser.literals.Keyword;
import org.jasm.parser.literals.SymbolReference;
import org.jasm.parser.literals.VersionLiteral;
import org.jasm.resolver.ClassInfoResolver;
import org.jasm.resolver.ClazzClassPathEntry;
import org.jasm.resolver.ExternalClassInfo;
import org.jasm.resolver.ResolveClassNotFoundException;
import org.jasm.resolver.ResolveIllegalAccessException;
import org.jasm.resolver.ResolveIsInterfaceException;
import org.jasm.resolver.ResolveIsntInterfaceException;
import org.jasm.type.descriptor.MethodDescriptor;
import org.jasm.type.descriptor.TypeDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Clazz extends AbstractByteCodeItem implements IContainerBytecodeItem<IBytecodeItem>, IConstantPoolReference, IAttributesContainer {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	private AssemblerParser parser = null;
	
	private VersionLiteral version;
	private int majorVersion = -1;
	private int minorVersion = -1;
	private ConstantPool pool = null;
	private List<Keyword> modifierLiterals;
	private ClassModifier modifier = null;
	private ClassInfo thisClass; 
	private int thisClassIndex = -1;
	private SymbolReference thisClassSymbol;
	private ClassInfo superClass;
	private int superClassIndex = -1;
	private SymbolReference superClassSymbol;
	private List<SymbolReference> interfaceSymbols;
	private List<ClassInfo> interfaces;
	private List<Integer> interfacesIndexes = null;
	private Set<Integer> referencedImplementsDeclarations = null;
	private Fields fields = null;
	private Methods methods = null;
	private Attributes attributes = null;
	
	private List<IBytecodeItem> children = new ArrayList<>();
	
	private Map<String, Integer> interfaceIndexesLabelTable =  new HashMap<String, Integer>();
	
	private ClassInfoResolver resolver;
	private ClassInfoResolver meResolver;
	private org.jasm.resolver.ExternalClassInfo me;
	
	public Clazz() {
		initChildren();
	}
	
	private void initChildren() {
		pool = new ConstantPool();
		pool.setParent(this);
		children.add(pool);
		fields = new Fields();
		fields.setParent(this);
		children.add(fields);
		methods = new Methods();
		methods.setParent(this);
		children.add(methods);
		attributes = new Attributes();
		attributes.setParent(this);
		children.add(attributes);
		modifierLiterals= new ArrayList<>();
		interfaces = new ArrayList<>();
		referencedImplementsDeclarations = new HashSet<Integer>();
		
		
	}

	@Override
	public void read(IByteBuffer source, long offset) {
		long currentOffset = offset;
		long magic = source.readUnsignedInt(currentOffset);
		if (magic != 0xCAFEBABEL) {
			throw new IllegalArgumentException("Unexpected starting bytes: "+Long.toHexString(magic));
		}
		currentOffset+=4;
		this.minorVersion = source.readUnsignedShort(currentOffset);
		this.majorVersion = source.readUnsignedShort(currentOffset+2);
		currentOffset+=4;
		pool.read(source, currentOffset);
		currentOffset+=pool.getLength();
		if (log.isDebugEnabled()) {
			log.debug("Reading modifier, class, supetClass, currentOffset="+currentOffset);
		}
		modifier = new ClassModifier(source.readUnsignedShort(currentOffset));
		currentOffset+=2;
		thisClassIndex = source.readUnsignedShort(currentOffset);
		currentOffset+=2;
		superClassIndex = source.readUnsignedShort(currentOffset);
		currentOffset+=2;
		if (log.isDebugEnabled()) {
			log.debug("Reading interfaces, currentOffset="+currentOffset);
		}
		int interfacesLength = source.readUnsignedShort(currentOffset);
		interfacesIndexes = new ArrayList<>();
		for (int i=0;i<interfacesLength; i++) {
			currentOffset+=2;
			interfacesIndexes.add(source.readUnsignedShort(currentOffset));
		}
		currentOffset+=2;
		fields.read(source, currentOffset);
		currentOffset+=fields.getLength();
		methods.read(source, currentOffset);
		currentOffset+=methods.getLength();
		attributes.read(source, currentOffset);
	}

	@Override
	public void write(IByteBuffer target, long offset) {
		long currentOffset = offset;
		target.writeUnsignedInt(currentOffset, 0xCAFEBABEL);
		target.writeUnsignedShort(currentOffset+4, minorVersion);
		target.writeUnsignedShort(currentOffset+6, majorVersion);
		currentOffset+=8;
		pool.write(target, currentOffset);
		currentOffset+=pool.getLength();
		if (log.isDebugEnabled()) {
			log.debug("Writing modifier, class, supetClass, currentOffset="+currentOffset);
		}
		target.writeUnsignedShort(currentOffset, modifier.getValue());
		currentOffset+=2;
		target.writeUnsignedShort(currentOffset, pool.indexOf(thisClass)+1);
		currentOffset+=2;
		if (this.superClass != null) {
			target.writeUnsignedShort(currentOffset, pool.indexOf(superClass)+1);
		} else {
			target.writeUnsignedShort(currentOffset, 0);
		}
		currentOffset+=2;
		if (log.isDebugEnabled()) {
			log.debug("Writing interfaces, currentOffset="+currentOffset);
		}
		target.writeUnsignedShort(currentOffset, interfaces.size());
		
		for (ClassInfo cl: interfaces) {
			currentOffset+=2;
			target.writeUnsignedShort(currentOffset, pool.indexOf(cl)+1);
		}
		currentOffset+=2;
		fields.write(target, currentOffset);
		currentOffset+=fields.getLength();
		methods.write(target, currentOffset);
		currentOffset+=methods.getLength();
		attributes.write(target, currentOffset);
	}

	@Override
	public int getLength() {
		int result = 8;
		result+=pool.getLength();
		result+=6;
		result+=2;
		result+=interfaces.size()*2;
		result+=fields.getLength();
		result+=methods.getLength();
		result+=attributes.getLength();
		return result;
	}

	@Override
	public boolean isStructure() {
		return true;
	}

	@Override
	public List<IPrintable> getStructureParts() {
		List<IPrintable> result = new ArrayList<IPrintable>();
		
		result.add(new SimplePrintable(null, "version", new String[]{majorVersion+"_"+minorVersion}, (String)null));
		result.add(new SimplePrintable(null, "name", new String[]{thisClass.getSymbolName()}, thisClass.getClassName()));
		if (this.superClass != null) {
			result.add(new SimplePrintable(null, "extends", new String[]{superClass.getSymbolName()}, superClass.getClassName()));
		}
		if (interfaces != null && interfaces.size()>0) {
			String [] comment = new String[interfaces.size()];
			String[] args = new String[interfaces.size()];
			for (int i=0;i<interfaces.size(); i++) {
				boolean hasLabel = referencedImplementsDeclarations.contains(i);
				args[i] = (hasLabel?"implref_"+i+":":"")+interfaces.get(i).getSymbolName();
				comment[i] = interfaces.get(i).getClassName();
		
			}
			result.add(new SimplePrintable(null, "implements", args, comment));
			
		}
		
		result.add(pool);
		result.add(attributes);
		result.add(fields);
		result.add(methods);
		
		return result;
	}

	@Override
	public String getPrintLabel() {
		return null;
	}

	@Override
	public String getPrintName() {
		if (!modifier.hasNoFlags()) {
			return modifier.toString()+" class";
		} else {
			return "class";
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
		pool.resolve();
		this.thisClass = (ClassInfo)pool.get(this.thisClassIndex-1);
		if (this.superClassIndex != 0) {
			this.superClass = (ClassInfo)pool.get(this.superClassIndex-1);
		}
		this.interfaces = new ArrayList<>();
		for (int i=0;i<interfacesIndexes.size(); i++) {
			interfaces.add((ClassInfo)pool.get(interfacesIndexes.get(i)-1));
		}
		fields.resolve();
		methods.resolve();
		attributes.resolve();
		pool.resolveInvokeDynamics();
		pool.createDisassemblerLabels();
	}
	
	private Boolean resolvedMyself; 
	
	public boolean resolveMyselfAndSuperclasses() {
		if (isAfterParse()) {
			if (resolvedMyself == null) {
				verifySuperclass();
				for (int i=0;i<interfaces.size(); i++) {
					verifyInterface(interfaceSymbols.get(i), interfaces.get(i));
				}
				me = ExternalClassInfo.createFromClass(this);
				ClazzClassPathEntry entry = new ClazzClassPathEntry();
				meResolver = new ClassInfoResolver();
				meResolver.add(entry);
				resolvedMyself = getParser().getErrorCounter() == 0;
			}
			return resolvedMyself;
		} else {
			return true;
		}
	}
	
	@Override
	protected void doVerify() {
		
		if (resolveMyselfAndSuperclasses()) {
			pool.verify();
			methods.verify();
			attributes.verify();
			if (getParser().getErrorCounter() == 0) {
				List<Instructions> instrs = this.getDescendants(Instructions.class);
				for (Instructions instr: instrs) {
					instr.verifyByteCode();
				}
				
			}
			
		}
		
	}
	
	private void verifyModifiers() {
		boolean valid = true;
		if (getModifier().isInterface()) {
			if (!getModifier().isAbstract() || getModifier().isFinal() || getModifier().isEnum()) {
				valid = false;
			}
		} else {
			if (getModifier().isAnnotation()) {
				valid = false;
			}
		}
		if (getModifier().isAnnotation() && !getModifier().isInterface()) {
			valid = false;
		}
		if (!valid) {
			emitError(null, "illegal class modifiers");
		}
	}
	
	private void verifySuperclass() {
		if (this.superClass != null) {
			ExternalClassInfo superClass = checkAndLoadClassInfo(this, superClassSymbol, getSuperClass().getClassName(), true);
			if (superClass != null) {
				if (getModifier().isInterface()) {
					if (!superClass.getName().equals("java/lang/Object")) {
						emitError(superClassSymbol, "interfaces must have java/lang/Object as superclass");
					}
				} else {
					if (superClass.getModifier().isInterface()) {
						emitError(superClassSymbol, "interfaces aren't allowed as superclasses");
					}
					if (superClass.getModifier().isFinal()) {
						emitError(superClassSymbol, "final classes aren't allowed as superclasses");
					}
				}
			}
		}
	}
	
	private void verifyInterface(SymbolReference intfSymbol, ClassInfo clinfo) {
		boolean valid = true;
		if (clinfo.isArray()) {
			valid = false;
		} else {
			ExternalClassInfo intfClass = checkAndLoadClassInfo(this, intfSymbol, clinfo.getClassName(), true);
			if (intfClass != null) {
				
				if (!intfClass.getModifier().isInterface()) {
					valid = false;
				} 
			}
		}
		if (!valid) {
			emitError(intfSymbol, clinfo.getClassName()+" isn't an interface");
		}
		
	}
	
	

	@Override
	protected void doResolveAfterParse() {
		//Version
		if (version != null) {
			try {
				String versionStr = version.getContent();
				majorVersion = Integer.parseInt(versionStr.substring(0, versionStr.indexOf('_')));
				minorVersion = Integer.parseInt(versionStr.substring(versionStr.indexOf('_')+1, versionStr.length()));
				if (getClassVersion().compareTo(new BigDecimal("45.0"))<0
						|| getClassVersion().compareTo(new BigDecimal("52.0"))>0) {
							emitError(version, "illegal version number");
				}
				
			} catch (Exception e) {
				emitError(version, "malformed or illegal version");
				majorVersion = 0;
				minorVersion = 0;
			}
		} else {
			emitError(null, "missing version statement");
		}
		//Pool
		pool.resolve();
		pool.updateIndexes();
		//this, super, me
		if (thisClassSymbol != null) {
			thisClass = pool.checkAndLoadFromSymbolTable(this,ClassInfo.class, thisClassSymbol);
		} else {
			emitError(null, "missing name statement");
		}
		
		if (superClassSymbol != null) {
			superClass = pool.checkAndLoadFromSymbolTable(this,ClassInfo.class, superClassSymbol);
		}
		
		//interfaces
		if (interfaceSymbols != null) {
			for (SymbolReference ref: interfaceSymbols) {
				ClassInfo cl = pool.checkAndLoadFromSymbolTable(this,ClassInfo.class, ref);
				if (cl != null) {
					interfaces.add(cl);
					addIntefaceIndexLabel(ref, interfaces.size()-1);
				}
			}
		}
		
		if (thisClass.isArray()) {
			emitError(thisClassSymbol, "array isn't allowed as class name");
		}
		if (this.getThisClass().getClassName().equals("java/lang/Object")) {
			if (this.getSuperClass() !=null) {
				emitError(superClassSymbol, "java/lang/Object must not have a superclass");
			}
		}
		if (this.getSuperClass() != null && this.getSuperClass().isArray()) {
			emitError(superClassSymbol, "arrays aren't allowed as superclasses");
			return;
		}
		
		//Modifier
		modifier = new ClassModifier(0);
		for (Keyword k: modifierLiterals) {
			modifier.setFlag(k.getKeyword());
		}
		verifyModifiers();
		//Attributes
		attributes.resolve();
		//Fields
		fields.resolve();
		//Methods
		methods.resolve();
		//invokeDynamics
		pool.resolveInvokeDynamics();
		pool.updateInvokeDynamicIndexes();
		
		
	}
	
	

	@Override
	public void updateMetadata() {
		pool.updateMetadata();
		for (ClassInfo intf: interfaces) {
			intf.updateMetadata();
		}
		fields.updateMetadata();
		methods.updateMetadata();
		attributes.updateMetadata();
		doUpdateMetadata();
	}

	@Override
	public ConstantPool getConstantPool() {
		return pool;
	}

	@Override
	public boolean isRoot() {
		return true;
	}
	
	//container
	@Override
	public int getSize() {
		return children.size();
	}

	@Override
	public IBytecodeItem get(int index) {
		return children.get(index);
	}

	@Override
	public int indexOf(IBytecodeItem item) {
		return children.indexOf(item);
	}

	public int getMajorVersion() {
		return majorVersion;
	}

	public int getMinorVersion() {
		return minorVersion;
	}

	public ClassInfo getThisClass() {
		return thisClass;
	}
	
	public String getPackage() {
		String name = thisClass.getClassName();
		if (name.indexOf('/')>=0) {
			return name.substring(0, name.lastIndexOf('/'));
		} else {
			return "";
		}
	}
	
	public void setThisClass(ClassInfo thisClass) {
		this.thisClass = thisClass;
	}

	public ClassInfo getSuperClass() {
		return superClass;
	}
	
	public void setSuperClass(ClassInfo superClass) {
		this.superClass = superClass;
	}

	public Attributes getAttributes() {
		return attributes;
	}

	public List<ClassInfo> getInterfaces() {
		return interfaces;
	}

	public ClassModifier getModifier() {
		return modifier;
	}

	public void setModifier(ClassModifier modifier) {
		this.modifier = modifier;
	}

	public Fields getFields() {
		return fields;
	}

	public Methods getMethods() {
		return methods;
	}

	@Override
	public int getItemSizeInList(IBytecodeItem item) {
		return 1;
	}

	@Override
	public AbstractConstantPoolEntry[] getConstantReferences() {
		int n = (superClass !=null)?2:1;
		AbstractConstantPoolEntry[] result = new AbstractConstantPoolEntry[interfaces.size()+n];
		result[0] = thisClass;
		if (this.superClass != null) {
			result[1] = superClass;
		}
		for (int i=0;i<interfaces.size(); i++) {
			result[i+n] = interfaces.get(i);
		}
		return result;
	}
	
	
	
	public VersionLiteral getVersion() {
		return version;
	}
	
	public BigDecimal getDecimalVersion() {
		return new BigDecimal(majorVersion+"."+minorVersion);
	}

	public void setVersion(VersionLiteral version) {
		this.version = version;
	}

	public SymbolReference getThisClassSymbol() {
		return thisClassSymbol;
	}

	public void setThisClassSymbol(SymbolReference thisClassSymbol) {
		this.thisClassSymbol = thisClassSymbol;
	}

	public SymbolReference getSuperClassSymbol() {
		return superClassSymbol;
	}

	public void setSuperClassSymbol(SymbolReference superClassSymbol) {
		this.superClassSymbol = superClassSymbol;
	}

	public List<Keyword> getModifierLiterals() {
		return modifierLiterals;
	}

	public void setParser(AssemblerParser parser) {
		this.parser = parser;
	}

	public AssemblerParser getParser() {
		return parser;
	}

	public List<SymbolReference> getInterfaceSymbols() {
		return interfaceSymbols;
	}

	public void setInterfaceSymbols(List<SymbolReference> interfaceSymbols) {
		this.interfaceSymbols = interfaceSymbols;
	}

	@Override
	protected void doUpdateMetadata() {
		List<IBytecodeItem> items = getAllItemsFromHere();
		for (IBytecodeItem item: items) {
			if (item instanceof IImplementsDeclarationsReference) {
				for (int index: ((IImplementsDeclarationsReference)item).getIndexes()) {
					referencedImplementsDeclarations.add(index);
				}
			}
		}
	}
	
	public void addIntefaceIndexLabel(SymbolReference ref, int index) {
		if (ref.getReferenceLabel() != null) {
			if (interfaceIndexesLabelTable.containsKey(ref.getReferenceLabel())) {
				emitError(ref, "dublicate implements label");
			} else {
				interfaceIndexesLabelTable.put(ref.getReferenceLabel(), index);
			}
		}
	}
	
	public Integer checkAndLoadInterfaceIndex(AbstractByteCodeItem caller, SymbolReference ref) {
		if (!interfaceIndexesLabelTable.containsKey(ref.getSymbolName())) {
			caller.emitError(ref, "unknown implements label");
			return null;
		} else {
			return interfaceIndexesLabelTable.get(ref.getSymbolName());
		}
	}

	public void setResolver(ClassInfoResolver resolver) {
		if (resolver == null) {
			throw new IllegalArgumentException("Resolver: "+resolver);
		}
		this.resolver = resolver;
		if (this.resolver == null) {
			throw new IllegalArgumentException("Resolver: "+this.resolver);
		}
	}
	
	public ClassInfoResolver getResolver() {
		if (resolver == null) {
			throw new IllegalStateException("Resolver not set");
		}
		return resolver;
	}
	
	
	
	
	public ExternalClassInfo checkAndLoadClassInfo(IErrorEmitter caller, SymbolReference symbol, String className, boolean checkAccess) {
		
		ExternalClassInfo result = null;
		try {
			if (meResolver != null) {
				result = meResolver.resolve(this,  className, checkAccess);
			}
			
			if (result == null) {
				result =  getResolver().resolve(this, className, checkAccess);
			}
			if (result == null) {
				emitResolveError(caller, symbol, className, "class "+className+" not found");
			}
			return result;
		} catch (ResolveIllegalAccessException e) {
			emitResolveError(caller, symbol, className, "class "+className+" is not accessible");
		}
		return null;
	}
	
	public void checkAndLoadTypeDescriptor(AbstractByteCodeItem caller, SymbolReference symbol, TypeDescriptor desc) {
		if (desc.isArray() || desc.isObject()) {
			ExternalClassInfo info = checkAndLoadClassInfo(caller, symbol, desc.getClassName(), false);
			desc.setExternalInfo(info);
		}
		
	}
	
	public void checkAndLoadMethodDescriptor(AbstractByteCodeItem caller, SymbolReference symbol, MethodDescriptor desc) {
		if (desc.getReturnType() != null) {
			checkAndLoadTypeDescriptor(caller, symbol, desc.getReturnType());
		}
		for (TypeDescriptor tdesc: desc.getParameters()) {
			checkAndLoadTypeDescriptor(caller, symbol, tdesc);
		}
	}
	
	
	public org.jasm.resolver.MethodInfo checkAndLoadMethodInfo(AbstractByteCodeItem caller, SymbolReference symbol, String className, String methodName, String desc, boolean checkAccess) {
		org.jasm.resolver.MethodInfo result = null;
		String methodKey = className+"."+methodName+"@"+desc; 
		try {
			if (meResolver != null) {
				try {
					result = meResolver.resolveMethod(this,  className, methodName, desc, checkAccess);
				} catch (ResolveClassNotFoundException e) {
					//ignore
				}
			}
			
			if (result == null) {
				result =  getResolver().resolveMethod(this, className, methodName, desc, checkAccess);
			}
			if (result == null) {
				emitResolveError(caller, symbol, methodKey, "method "+methodKey+" not found");
			}
			return result;
		} catch (ResolveIllegalAccessException e) {
			emitResolveError(caller, symbol, methodKey, "method "+methodKey+" is not accessible");
		} catch (ResolveIsInterfaceException e) {
			emitResolveError(caller, symbol, methodKey, "class "+className+" is an interface");
		} catch (ResolveClassNotFoundException e) {
			emitResolveError(caller, symbol, className, "class "+className+" not found");
		}
		return null;
	}
	
	public org.jasm.resolver.MethodInfo checkAndLoadInterfaceMethodInfo(AbstractByteCodeItem caller, SymbolReference symbol, String className, String methodName, String desc, boolean checkAccess) {
		org.jasm.resolver.MethodInfo result = null;
		String methodKey = className+"."+methodName+"@"+desc; 
		try {
			if (meResolver != null) {
				try {
					result = meResolver.resolveInterfaceMethod(this,  className, methodName, desc, checkAccess);
				} catch (ResolveClassNotFoundException e) {
					//ignore
				}
			}
			
			if (result == null) {
				result =  getResolver().resolveInterfaceMethod(this, className, methodName, desc, checkAccess);
			}
			if (result == null) {
				emitResolveError(caller, symbol, methodKey, "method "+methodKey+" not found");
			}
			return result;
		} catch (ResolveIllegalAccessException e) {
			emitResolveError(caller, symbol, methodKey, "method "+methodKey+" is not accessible");
		} catch (ResolveIsntInterfaceException e) {
			emitResolveError(caller, symbol, methodKey, "class "+className+" is not an interface");
		} catch (ResolveClassNotFoundException e) {
			emitResolveError(caller, symbol, className, "class "+className+" not found");
		}
		return null;
	}
	
	public org.jasm.resolver.FieldInfo checkAndLoadFieldInfo(AbstractByteCodeItem caller, SymbolReference symbol, String className, String fieldName, String desc, boolean checkAccess) {
		org.jasm.resolver.FieldInfo result = null;
		String fieldKey = className+"."+fieldName+"@"+desc; 
		try {
			if (meResolver != null) {
				try {
					result = meResolver.resolveField(this,  className, fieldName, desc, checkAccess);
				} catch (ResolveClassNotFoundException e) {
					//ignore
				}
			}
			
			if (result == null) {
				result =  getResolver().resolveField(this, className, fieldName, desc, checkAccess);
			}
			if (result == null) {
				emitResolveError(caller, symbol, fieldKey, "field "+fieldKey+" not found");
			}
			return result;
		} catch (ResolveIllegalAccessException e) {
			emitResolveError(caller, symbol, fieldKey, "field "+fieldKey+" is not accessible");
		} catch (ResolveClassNotFoundException e) {
			emitResolveError(caller, symbol, className, "class "+className+" not found");
		} 
		return null;
	}
	
	
	private List<String> notResolved = new ArrayList<String>();
	
	private void emitResolveError(IErrorEmitter caller, SymbolReference symbol, String name, String message) {
		if (!notResolved.contains(name)) {
			caller.emitError(symbol, message);
			notResolved.add(name);
		}
	}

	public org.jasm.resolver.ExternalClassInfo getMe() {
		return me;
	}

}
