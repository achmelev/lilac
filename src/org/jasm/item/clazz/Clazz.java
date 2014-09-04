package org.jasm.item.clazz;

import java.util.ArrayList;
import java.util.List;

import org.jasm.bytebuffer.IByteBuffer;
import org.jasm.bytebuffer.print.IPrintable;
import org.jasm.bytebuffer.print.SimplePrintable;
import org.jasm.item.AbstractByteCodeItem;
import org.jasm.item.IBytecodeItem;
import org.jasm.item.IContainerBytecodeItem;
import org.jasm.item.attribute.Attributes;
import org.jasm.item.constantpool.AbstractConstantPoolEntry;
import org.jasm.item.constantpool.ClassInfo;
import org.jasm.item.constantpool.ConstantPool;
import org.jasm.item.constantpool.IConstantPoolReference;
import org.jasm.item.modifier.ClassModifier;
import org.jasm.parser.AssemblerParser;
import org.jasm.parser.literals.Keyword;
import org.jasm.parser.literals.SymbolReference;
import org.jasm.parser.literals.VersionLiteral;
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
	private Fields fields = null;
	private Methods methods = null;
	private Attributes attributes = null;
	
	private List<IBytecodeItem> children = new ArrayList<>();
	
	
	public Clazz() {
		initChildren();
	}
	
	public Clazz(int minorVersion, int majorVersion, ClassModifier modifier) {
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.modifier = modifier;
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
				args[i] = interfaces.get(i).getSymbolName();
				comment[i] = interfaces.get(i).getClassName();
		
			}
			result.add(new SimplePrintable(null, "implements", args, comment));
			
		}
		if (!modifier.hasNoFlags()) {
			result.add(new SimplePrintable(null, "modifier", new String[]{modifier.toString()}, (String)null));
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
		return "class";
	}
	
	@Override
	public String getTypeLabel() {
		return  getPrintName();
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
	}
	
	@Override
	protected void doResolveAfterParse() {
		//Version
		if (version != null) {
			try {
				String versionStr = version.getContent();
				majorVersion = Integer.parseInt(versionStr.substring(0, versionStr.indexOf('_')));
				minorVersion = Integer.parseInt(versionStr.substring(versionStr.indexOf('_')+1, versionStr.length()));
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
		//this, super
		if (thisClassSymbol != null) {
			thisClass = pool.checkAndLoadFromSymbolTable(ClassInfo.class, thisClassSymbol);
		} else {
			emitError(null, "missing name statement");
		}
		
		if (superClassSymbol != null) {
			superClass = pool.checkAndLoadFromSymbolTable(ClassInfo.class, superClassSymbol);
		}
		
		//interfaces
		if (interfaceSymbols != null) {
			for (SymbolReference ref: interfaceSymbols) {
				ClassInfo cl = pool.checkAndLoadFromSymbolTable(ClassInfo.class, ref);
				if (cl != null) {
					interfaces.add(cl);
				}
			}
		}
		
		//Modifier
		modifier = new ClassModifier(0);
		for (Keyword k: modifierLiterals) {
			modifier.setFlag(k.getKeyword());
		}
		//Attributes
		attributes.resolve();
		//Fields
		fields.resolve();
		//Methods
		methods.resolve();
		
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

	
	
	
	

}
