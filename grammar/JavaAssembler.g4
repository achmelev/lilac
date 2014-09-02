grammar JavaAssembler;

//Parser

clazz:
	 CLASS LBRACE 
	 	classmember*
	 RBRACE;

classmember: version SEMI
			 | classname SEMI
			 | superclass SEMI
			 | classmodifier SEMI
			 | constpoolentry SEMI
			 | classattribute
			 | method
			 ;
			 
			 

version: VERSION VersionLiteral;

classname: NAME Identifier;

superclass: EXTENDS Identifier;

classmodifier: MODIFIER classmodifierlabel (COMMA  classmodifierlabel)*;

classmodifierlabel: PUBLIC 		# classmodifierPublic
					| SUPER  		# classmodifierSuper
					| INTERFACE  	# classmodifierInterface
					| ABSTRACT   	# classmodifierAbstract
					| SYNTETIC 		# classmodifierSyntetic
					| ANNOTATION     # classmodifierAnnotation
					| ENUM			# classmodifierEnum
					;
					

constpoolentry:  CONST UTF8INFO label StringLiteral #utf8info
				 | CONST CLASSINFO  label Identifier #classinfo
				 | CONST STRINGINFO label Identifier #stringinfo
				 | CONST FIELDREFINFO label Identifier COMMA  Identifier #fieldrefinfo
				 | CONST INTEGERINFO label IntegerLiteral  #integerinfo
				 | CONST FLOATINFO   label FloatingPointLiteral  #floatinfo
				 | CONST METHODREFINFO label Identifier COMMA  Identifier #methodrefinfo
				 | CONST NAMEANDTYPEINFO label Identifier COMMA  Identifier #nameandtypeinfo
				 ;



classattribute : SOURCE FILE Identifier SEMI #classattributeSourceFile;

method  : METHOD  LBRACE
					methodmember*
				  RBRACE;

methodmember: methodname SEMI
			  | methoddescriptor SEMI
			  | methodmodifier SEMI
			  ;
				  
methodname: NAME Identifier;
methoddescriptor: DESCRIPTOR Identifier;

methodmodifier: MODIFIER methodmodifierlabel (COMMA  methodmodifierlabel)*;
				  

methodmodifierlabel:  PUBLIC 		# methodmodifierPublic
					| PRIVATE  		# methodmodifierPrivate
					| PROTECTED  	# methodmodifierProtected
					| ABSTRACT   	# methodmodifierAbstract
					| STATIC 		# methodmodifierStatic
					| FINAL         # methodmodifierFinal
					| SYNCHRONIZED	# methodmodifierSynchronized
					| VARARGS	    # methodmodifierVarargs
					| NATIVE	    # methodmodifierNative
					| ABSTRACT	    # methodmodifierAbstract
					| STRICT 	    # methodmodifierStrict
					| SYNTETIC 	    # methodmodifierSyntetic
					;

label: Identifier;

//Lexer

// Keywords

CLASS         :  'class';
CONST         :  'const';
VERSION       :  'version';
NAME          :  'name';
EXTENDS       :  'extends';
MODIFIER      :  'modifier';
PUBLIC        :  'public';
FINAL         :  'final';
SUPER         :  'super';
INTERFACE     :  'interface';
ABSTRACT      :  'abstract';
SYNTETIC      :  'syntetic';
ANNOTATION    :  'annotation';
ENUM          :  'enum';
CLASSINFO     :  'classref';
UTF8INFO      :  'utf8';
STRINGINFO    :  'string';
FIELDREFINFO  :  'fieldref';
INTEGERINFO   :  'int';
FLOATINFO     :  'float';
METHODREFINFO :  'methodref';
NAMEANDTYPEINFO :  'nameandtype';
ATTRIBUTES    :  'attributes';
SOURCE        :  'source';
FILE          :  'file';
METHODS       :  'methods';
METHOD        :  'method';
DESCRIPTOR    :  'descriptor';
PRIVATE       :  'private';
PROTECTED     :  'protected';
STATIC        :  'static';
SYNCHRONIZED  :  'synchronized';
VARARGS       :  'varargs';
NATIVE        :  'native';
STRICT        :  'strict';


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


