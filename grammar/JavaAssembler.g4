grammar JavaAssembler;

//Parser

clazz:
	 CLASS LBRACE 
	 	classmember*
	 RBRACE;

classmember: version SEMI
			 | classname SEMI
			 | superclass SEMI
			 | interfaces SEMI
			 | classmodifier SEMI
			 | constpoolentry SEMI
			 | classattribute
			 | method
			 | field
			 ;
			 
			 

version: VERSION VersionLiteral;

classname: NAME Identifier;

superclass: EXTENDS Identifier;

interfaces: IMPLEMENTS Identifier ( COMMA Identifier)*;

classmodifier: MODIFIER classmodifierlabel (COMMA  classmodifierlabel)*;

classmodifierlabel: PUBLIC 		# classmodifierPublic
					| FINAL  		# classmodifierFinal
					| SUPER  		# classmodifierSuper
					| INTERFACE  	# classmodifierInterface
					| ABSTRACT   	# classmodifierAbstract
					| SYNTETIC 		# classmodifierSyntetic
					| ANNOTATION     # classmodifierAnnotation
					| ENUM			# classmodifierEnum
					;
					

constpoolentry:  CONST UTF8INFO label StringLiteral #utf8info
				 | CONST CLASSINFO  label Identifier #classinfo
				 | CONST STRING label Identifier #stringinfo
				 | CONST FIELDREFINFO label Identifier COMMA  Identifier #fieldrefinfo
				 | CONST INT label IntegerLiteral  #integerinfo
				 | CONST LONG label IntegerLiteral  #longinfo
				 | CONST FLOAT   label FloatingPointLiteral  #floatinfo
				 | CONST DOUBLE  label FloatingPointLiteral  #doubleinfo
				 | CONST METHODREFINFO label Identifier COMMA  Identifier #methodrefinfo
				 | CONST INTERFACEMETHODREFINFO label Identifier COMMA  Identifier #interfacemethodrefinfo
				 | CONST NAMEANDTYPEINFO label Identifier COMMA  Identifier #nameandtypeinfo
				 ;



classattribute : SOURCE FILE Identifier SEMI #classattributeSourceFile
			   | signatureattribute #classattributeSignature
			   | synteticattribute #classAttributeSyntetic
			   | deprecatedattribute #classAttributeDeprecated
			   | annotation #classAnnotation 
			   | innerclass #classInnerClass
			   | enclosingmethod #classEnclosingMethod
			   ;
			   
innerclass: INNER CLASS LBRACE 
	 			innerclassmember*
	 		RBRACE;

innerclassmember: innerclass_inner SEMI
				  | innerclass_outer SEMI
				  | innerclass_name SEMI
				  | innerclass_modifier SEMI
				  ;

innerclass_inner: INNER Identifier;
innerclass_outer: OUTER Identifier;
innerclass_name: NAME Identifier;
innerclass_modifier: MODIFIER innerclassmodifierlabel (COMMA  innerclassmodifierlabel)*;
innerclassmodifierlabel: PUBLIC #innerclassmodifierPublic
						|PRIVATE #innerclassmodifierPrivate
						|PROTECTED #innerclassmodifierProtected
						|STATIC #innerclassmodifierStatic
						|FINAL #innerclassmodifierFinal
						|INTERFACE #innerclassmodifierInterface
						|ABSTRACT #innerclassmodifierAbstract
						|SYNTETIC #innerclassmodifierSyntetic
						|ANNOTATION #innerclassmodifierAnnotation
						|ENUM #innerclassmodifierEnum
						; 

enclosingmethod: ENCLOSING METHOD Identifier COMMA Identifier SEMI;

method  : METHOD  LBRACE
					methodmember*
				  RBRACE;

methodmember: methodname SEMI
			  | methoddescriptor SEMI
			  | methodmodifier SEMI
			  | methodattribute
			  | methodvar
			  | methodinstruction
			  ;
				  
methodname: NAME Identifier;
methoddescriptor: DESCRIPTOR Identifier;

methodmodifier: MODIFIER methodmodifierlabel (COMMA  methodmodifierlabel)*;
				  

methodmodifierlabel:  PUBLIC 		# methodmodifierPublic
					| PRIVATE  		# methodmodifierPrivate
					| PROTECTED  	# methodmodifierProtected
					| STATIC 		# methodmodifierStatic
					| FINAL         # methodmodifierFinal
					| SYNCHRONIZED	# methodmodifierSynchronized
					| BRIDGE	    # methodmodifierBridge
					| VARARGS	    # methodmodifierVarargs
					| NATIVE	    # methodmodifierNative
					| ABSTRACT   	# methodmodifierAbstract
					| STRICT 	    # methodmodifierStrict
					| SYNTETIC 	    # methodmodifierSyntetic
					;

methodattribute: THROWS Identifier (COMMA Identifier)* SEMI #methodAttributeExceptions
				 | signatureattribute #methodAttributeSignature
				 | synteticattribute #methodAttributeSyntetic
				 | deprecatedattribute #methodAttributeDeprecated
				 | annotation #methodAnnotation 
				 ;

methodvar: methodvarimplicit #methodvar_implicit
		   | methodvarrelative #methodvar_relative
		   | methodvarabsolute #methodvar_absolute
		   ;
		   
methodvartype: DOUBLE|FLOAT|INT|LONG|OBJECT|RETURNADRESS;



methodvarimplicit: VAR methodvartype Identifier SEMI;
methodvarrelative: VAR methodvartype Identifier AT Identifier ( Plus? IntegerLiteral)? SEMI;
methodvarabsolute: VAR methodvartype Identifier AT IntegerLiteral SEMI;

methodinstruction: (label COLON)? instruction SEMI;

instruction: Argumentlessop #argumentlessop
			 | Constantpoolop Identifier #constantpoolop
			 ;





field  : FIELD  LBRACE
					fieldmember*
				  RBRACE;

fieldmember: fieldname SEMI
			  | fielddescriptor SEMI
			  | fieldmodifier SEMI
			  | fieldattribute
			  ;
				  
fieldname: NAME Identifier;
fielddescriptor: DESCRIPTOR Identifier;

fieldmodifier: MODIFIER fieldmodifierlabel (COMMA  fieldmodifierlabel)*;
				  

fieldmodifierlabel:  PUBLIC 		# fieldmodifierPublic
					| PRIVATE  		# fieldmodifierPrivate
					| PROTECTED  	# fieldmodifierProtected
					| STATIC 		# fieldmodifierStatic
					| FINAL         # fieldmodifierFinal
					| VOLATILE  	# fieldmodifierVolatile
					| TRANSIENT	    # fieldmodifierTransient
					| SYNTETIC 	    # fieldmodifierSyntetic
					| ENUM	 	    # fieldmodifierEnum
					;

fieldattribute : CONSTANT VALUE Identifier SEMI #fieldattributeConstantValue
				  | signatureattribute #fieldAttributeSignature
				  | synteticattribute #fieldAttributeSyntetic
				  | deprecatedattribute #fieldAttributeDeprecated
				  | annotation #fieldAnnotation 
				 ;


signatureattribute: SIGNATURE Identifier SEMI;
synteticattribute: SYNTETIC SEMI;
deprecatedattribute: DEPRECATED SEMI;

annotation: INVISIBLE? PARAMETER? annotationdeclaration;

annotationdeclaration: ANNOTATION  LBRACE
						 annotationtype
						 annotationindex?
						 annotationelement*
					   RBRACE;
					   
annotationtype: TYPE Identifier SEMI;
annotationindex: INDEX IntegerLiteral SEMI;

annotationelement: ELEMENT LBRACE
					 annotationelementname SEMI
					 annotationelementvalue
				   RBRACE;
				   
annotationelementname: NAME Identifier;

annotationelementvalue: simpleannotationelementvalue|enumannotationelementvalue|arrayannotationelementvalue|annotationdeclaration;

simpleannotationelementvalue: (BYTE|CHAR|FLOAT|DOUBLE|INT|LONG|SHORT|STRING|BOOLEAN|CLASS) VALUE Identifier SEMI;
enumannotationelementvalue: ENUM VALUE Identifier COMMA Identifier SEMI;
arrayannotationelementvalue: ARRAY VALUE LBRACE
								annotationelementvalue*
							 RBRACE;


label: Identifier;

//Lexer

//Instructions

Argumentlessop: 'aaload'|'aastore'|'aconst_null'|'areturn'|'arraylength'|'athrow'|'baload'|'bastore'|'caload'|'castore'|'d2f'|'d2i'|'d2l'|'dadd'|'daload'|'dastore'|'dcmpg'|'dcmpl'|'dconst_0'|'dconst_1'|'ddiv'|'dmul'|'dneg'|'drem'|'dreturn'|'dsub'|'dup'|'dup_x1'|'dup_x2'|'dup2'|'dup2_x1'|'dup2_x2'|'f2d'|'f2i'|'f2l'|'fadd'|'faload'|'fastore'|'fcmpg'|'fcmpl'|'fconst_0'|'fconst_1'|'fconst_2'|'fdiv'|'fmul'|'fneg'|'frem'|'freturn'|'fsub'|'i2b'|'i2c'|'i2d'|'i2f'|'i2l'|'i2s'|'iadd'|'iaload'|'iand'|'iastore'|'iconst_m1'|'iconst_0'|'iconst_1'|'iconst_2'|'iconst_3'|'iconst_4'|'iconst_5'|'idiv'|'imul'|'ineg'|'ior'|'irem'|'ireturn'|'ishl'|'ishr'|'isub'|'iushr'|'ixor'|'l2d'|'l2f'|'l2i'|'ladd'|'laload'|'land'|'lastore'|'lcmp'|'lconst_0'|'lconst_1'|'ldiv'|'lmul'|'lneg'|'lor'|'lrem'|'lreturn'|'lshl'|'lshr'|'lsub'|'lushr'|'lxor'|'monitorenter'|'monitorexit'|'nop'|'pop'|'pop2'|'ret'|'return'|'saload'|'sastore'|'swap';
Constantpoolop: 'ldc'|'anewarray'|'checkcast'|'getfield'|'getstatic'|'instanceof'|'invokespecial'|'invokestatic'|'invokevirtual'|'ldc_w'|'ldc2_w'|'new'|'putfield'|'putstatic';

// Keywords

CLASS         :  'class';
CONST         :  'const';
CONSTANT      :  'constant';
VERSION       :  'version';
NAME          :  'name';
EXTENDS       :  'extends';
IMPLEMENTS    :  'implements';
MODIFIER      :  'modifier';
PUBLIC        :  'public';
FINAL         :  'final';
SUPER         :  'super';
INTERFACE     :  'interface';
ABSTRACT      :  'abstract';
SYNTETIC      :  'synthetic';
ANNOTATION    :  'annotation';
ENUM          :  'enum';
CLASSINFO     :  'classref';
UTF8INFO      :  'utf8';
FIELDREFINFO  :  'fieldref';
METHODREFINFO :  'methodref';
INTERFACEMETHODREFINFO : 'intfmethodref';
NAMEANDTYPEINFO :  'nameandtype';
ATTRIBUTES    :  'attributes';
SOURCE        :  'source';
FILE          :  'file';
METHOD        :  'method';
FIELD         :  'field';
DESCRIPTOR    :  'descriptor';
PRIVATE       :  'private';
PROTECTED     :  'protected';
STATIC        :  'static';
SYNCHRONIZED  :  'synchronized';
VARARGS       :  'varargs';
NATIVE        :  'native';
STRICT        :  'strict';
VOLATILE      :  'volatile';
TRANSIENT     :  'transient';
BRIDGE        :  'bridge';
VALUE         :  'value';
THROWS        :  'throws';
SIGNATURE     :  'signature';
DEPRECATED    :  'deprecated';
INVISIBLE     :  'invisible';
TYPE          :  'type';
ELEMENT       :  'element';
BYTE          :  'byte';
CHAR          :  'char';
DOUBLE        :  'double';
FLOAT         :  'float';
INT           :  'int';
LONG          :  'long';
SHORT         :  'short';
STRING        :  'string';
BOOLEAN       :  'boolean';
ARRAY         :  'array';
INDEX         :  'index';
PARAMETER     :  'parameter';
INNER         :  'inner';
OUTER         :  'outer';
ENCLOSING     :  'enclosing';
OBJECT        :  'object';
RETURNADRESS  :  'returnadress';
AT            :  'at';
VAR            :  'var';

Plus            :  '+';


//Version

VersionLiteral: NonZeroDigit Digit* '_' Digit+;

// Integer Literals

IntegerLiteral
    :   Sign? DecimalIntegerLiteral
    |   Sign? HexIntegerLiteral
    |   Sign? OctalIntegerLiteral
    |   Sign? BinaryIntegerLiteral
    ;

fragment
DecimalIntegerLiteral
    :   DecimalNumeral
    ;

fragment
HexIntegerLiteral
    :   HexNumeral
    ;

fragment
OctalIntegerLiteral
    :   OctalNumeral
    ;

fragment
BinaryIntegerLiteral
    :   BinaryNumeral
    ;


fragment
DecimalNumeral
    :   '0'
    |   NonZeroDigit Digit*
    ;


fragment
Digit
    :   '0'
    |   NonZeroDigit
    ;

fragment
NonZeroDigit
    :   [1-9]
    ;

fragment
HexNumeral
    :   '0' [xX] HexDigits
    ;

fragment
HexDigits
    :   HexDigit+
    ;

fragment
HexDigit
    :   [0-9a-fA-F]
    ;

fragment
OctalNumeral
    :   '0' OctalDigits
    ;

fragment
OctalDigits
    :   OctalDigit+
    ;

fragment
OctalDigit
    :   [0-7]
    ;



fragment
BinaryNumeral
    :   '0' [bB] BinaryDigits
    ;

fragment
BinaryDigits
    :   BinaryDigit+
    ;

fragment
BinaryDigit
    :   [01]
    ;


// Floating-Point Literal

FloatingPointLiteral
	: Sign? HexFloatingPointLiteral  
	| Sign? DecimalFloatingPointLiteral
    |   'NaN'
    |   Sign? 'Infinity'
    ;

fragment
DecimalFloatingPointLiteral
    :   DecimalNumeral '.' Digit+ ExponentPart?;

fragment
HexFloatingPointLiteral
    :   BinaryDigit '.' HexDigit+ HexExponentPart;



fragment
ExponentPart
    :   ExponentIndicator Sign? DecimalNumeral
    ;
    
fragment
HexExponentPart
    :   BinaryExponentIndicator Sign? HexDigits
    ;

fragment
ExponentIndicator
    :   [eE]
    ;

fragment
BinaryExponentIndicator
    :   [pP]
    ;


fragment
Sign
    :   [+-]
    ;


 

// String Literals

StringLiteral
    :   '"' StringCharacters? '"'
    ;

fragment
StringCharacters
    :   StringCharacter+
    ;

fragment
StringCharacter
    :   ~["\\]
    |   EscapeSequence
    ;

// Escape Sequences for Character and String Literals

fragment
EscapeSequence
    :   '\\' [btnfr"'\\]
    |   OctalEscape
    |   UnicodeEscape
    ;

fragment
OctalEscape
    :   '\\' OctalDigit
    |   '\\' OctalDigit OctalDigit
    |   '\\' ZeroToThree OctalDigit OctalDigit
    ;

fragment
UnicodeEscape
    :   '\\' 'u' HexDigit HexDigit HexDigit HexDigit
    ;

fragment
ZeroToThree
    :   [0-3]
    ;


DefaultLiteral
    :   'default'
    ;


NullLiteral
    :   'nil'
    ;


// Separators
LBRACE          : '{';
RBRACE          : '}';
SEMI            : ';';
COMMA           : ',';
COLON           : ':';


// Identifiers

Identifier
    :   JavaLetter JavaLetterOrDigit*
    ;

fragment
JavaLetter
    :   [a-zA-Z$_] // these are the "java letters" below 0xFF
    |   // covers all characters above 0xFF which are not a surrogate
        ~[\u0000-\u00FF\uD800-\uDBFF]
        {Character.isJavaIdentifierStart(_input.LA(-1))}?
    |   // covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
        [\uD800-\uDBFF] [\uDC00-\uDFFF]
        {Character.isJavaIdentifierStart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
    ;

fragment
JavaLetterOrDigit
    :   [a-zA-Z0-9$_] // these are the "java letters or digits" below 0xFF
    |   // covers all characters above 0xFF which are not a surrogate
        ~[\u0000-\u00FF\uD800-\uDBFF]
        {Character.isJavaIdentifierPart(_input.LA(-1))}?
    |   // covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
        [\uD800-\uDBFF] [\uDC00-\uDFFF]
        {Character.isJavaIdentifierPart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
    ;



//
// Whitespace and comments
//

WS  :  [ \t\r\n\u000C]+ -> skip
    ;

COMMENT
    :   '/*' .*? '*/' -> skip
    ;

LINE_COMMENT
    :   '//' ~[\r\n]* -> skip
    ;


