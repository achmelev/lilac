<h2>Table of Contents</h2>

[TOC]

##Introduction

A [Java](http://en.wikipedia.org/wiki/Java_%28programming_language%29) assembler (or assembly) language is a low level programming language for the [Java Virtual Machine (JVM)](http://en.wikipedia.org/wiki/Java_virtual_machine) in which there is a strong correspondence 
between the language constructs and the [JVM bytecode](http://en.wikipedia.org/wiki/Java_bytecode). There is no official standard for the syntax of a Java assembler language. 
The [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) does contain something like a disassembler (but no assembler) - the [javap](http://docs.oracle.com/javase/7/docs/technotes/tools/windows/javap.html) utility - 
unfortunately the syntax of the javap's output has not been designed as a possible input to an assembler but rather with a focus on human readability. Accordingly no one
ever tried to develop an assembler for this syntax. From 1996 onwards [Jasmin](http://jasmin.sourceforge.net/) has gradually established itself as a de-facto alternative standard for a Java assembler language, its
syntax is however poorly documented and seems at times to lag considerably behind the development of the JVM itself.   

This reference describes the syntax of the Java assembler language as defined by [Lilac](http://lilac.sourceforge.net) - an open source assembler/disassembler tool suite for the JVM.

**Please note: ** this is a reference of the the Java assembler language, not of the Java language itself or of the Java Virtual Machine. 
So throughout this document a thorough knowledge of the Java language concepts as well as some basic understanding of the JVM's inner workings are assumed.

###Notation

Throughout this specification the syntax definitions will be given in the [Extended Backus-Naur Form](http://en.wikipedia.org/wiki/Extended_Backus%E2%80%93Naur_Form) - notation.

##Language Basics

###Lexical Structure 

On the lexical level a Java assembler program is a sequence of words and separators. There are following separators in the language: **,**&nbsp;&nbsp;**;**&nbsp;&nbsp;**:**&nbsp;&nbsp;**{**&nbsp;&nbsp;**}**&nbsp;&nbsp;**->** .
The words of a Java Assembler program fall, like in other programming languages, into three broad categories: keywords, literals and identifiers. 

####Literals

There are four different different literal types: integer literals, floating point literals, string literals and base64 literals. The syntax of the first three is the same as [in the Java language](https://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html#jls-3.10).
A base64 literal is just a [base64 encoded byte sequence](https://de.wikipedia.org/wiki/Base64) enclosed in square brackets.

Here are some examples of a Java assembler literal:

	:::lilac
	"Hello Word"
	1234
	1235.56
    [UG9seWZvbiB6d2l0c2NoZXJuZCBhw59lbiBNw6R4Y2hlbnMgVsO2Z2VsIFLDvGJlbiwgSm9naHVydCB1bmQgUXVhcms=]

####Identifiers

The syntax of a Java assembler identifier is defined as follows:

	:::ebnf
	identifier = javaidentifier, {'.' javaidentifier} ;

A **javaidentifier** from the definition above is a Java language identifier [as defined in the Java Language specification](https://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html#jls-3.8).

Here are some examples of a valid Java assembler identifier:

	:::lilac
	this
	out
	in_12
	this$0
	System.out
	
####Comments

In addition to the words and separators a Java assembler program might also contain comments, whose syntax is, again, the same as [in the Java Language](https://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html#jls-3.7).

Here are some examples of a comment:

	:::lilac
	//This is a sigle line comment
	/**
	And this is
	A multiple-line one
	**/

###Syntactic Structure

####Names and labels

On the syntactic level Java assembler has two different kinds of [identifiers](#identifiers): **names** and **labels**. Names identify an entity declared in a program, 
in the context of Java assembler those entities can be [variables](#variable-statement) and [constants](#constant-statements).

The following example shows two [constants](#constant-statements): a [string constant](#string-statement) referencing a [utf8 constant](#utf8-constant-statement) by it's name:

	:::lilac
	const utf8 helloword_content "Hello World";
	const string helloword helloword_content;

Labels on the other hand identify a location inside the program to which, for example, the control flow of the program might be transferred. 
This is illustrated in the following example where the [if_acmpne instruction](#branch-instructions) transfers the control to the [return instruction](#branch-instructions)

	:::lilac
	if_acmpne end;
    iconst_1;
    putfield MyClass.int_field;
    end: return;

####Statements

On the syntactic level a Java assembler program is a sequence of **statements**, which come in two flavors: **simple statements** and **block statements**. 
A block statement generally consists of some keywords followed by a sequence of **member statements** enclosed in curly brackets as illustrated in the following
example:

	:::lilac
	private final field {
		name value_name;
		descriptor type_desc; 
	}

In the example we see a [field statement](#field-statement) which in turn contains two further statements: a [name statement](#name-statement) and a [descriptor statement](#descriptor-statement).

A simple statement is just a sequence of [keywords, identifiers and literals](#lexical-structure) possibly separated by some [separators](#lexical-structure)
and, this is important, *always* terminated by **;**

**Note:** the definition given above is a most general definition of a simple statements, in case of particular statements there are always additional syntactic rules
governing the occurrence and order of [keywords, identifiers. literals and separators](#lexical-structure). 

Here are some examples of a simple statement:

	:::lilac
	const nameandtype hash_nat hash_name, hash_desc;
	line ir0, 532;
	lookupswitch 1->ir36,3->ir41,100->ir46,default->ir53;
	append else, {int};

Because block statements themselves contain another statements the whole syntactic structure of a Java assembler program can be in effect 
seen as a [forest](http://en.wikipedia.org/wiki/Tree_%28graph_theory%29) of statements with **block statements** being parent nodes of their **member statements**. 
Because on the semantic level there is an additional requirement, that a Java assembler source file contains exactly one [class statement](#class-statement), 
this [forest](http://en.wikipedia.org/wiki/Tree_%28graph_theory%29) is in fact just a tree with the [class statement](#class-statement) at the root.

##Language Statements

Having described above the general lexical and syntactic structure of a Java assembler program, the rest of this reference will cover the syntax and semantics of the particular statements used in Java assembler.

###Class statement

A class statement specifies a class type. As already mentioned above, the must be exactly one class statement in a Java assembler source file. 
Note however, that in Java assembler the term "class" encompasses not only class types
[as defined by the Java language specification](https://docs.oracle.com/javase/specs/jls/se7/html/jls-8.html) which includes 
[classes in the narrow sense](https://docs.oracle.com/javase/specs/jls/se7/html/jls-8.html#jls-8.1) 
and [enums](https://docs.oracle.com/javase/specs/jls/se7/html/jls-8.html#jls-8.9), but also [interface types](https://docs.oracle.com/javase/specs/jls/se7/html/jls-9.html) including 
[interfaces](https://docs.oracle.com/javase/specs/jls/se7/html/jls-9.html#jls-9.1) and [annotation types](https://docs.oracle.com/javase/specs/jls/se7/html/jls-9.html#jls-9.7).

A class statement, which is a [block statement](#statements),  consists of some [class modifier keywords](#class-modifiers) followed by the keyword **class** 
and then by some [class member statements](#class-members) enclosed in curly brackets as defined in the following EBNF expression:   

	:::ebnf
	class statement = {class modifier}, 'class', '{',{class member},'}';
	class modifier = 'public'|'final'|'abstract'|'super'|'interface'|'synthetic'|'annotation'|'enum';
	class member = version|name|superclass|interfaces|source file|signature|synthetic|deprecated|constant|method|field|annotation|type annotation|bootstrap method|inner class|enclosing method|unknown attribute
	
####Class modifiers

Class modifiers in the Java assembler correspond one to one to the [access flags defined in the JVM specification](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.1-200-E.1)
as shown in the following table:

Assembler modifier keyword|access flag from the Jvm spec
--------------------------|----------------------------
public                    |ACC_PUBLIC
final                     |ACC_FINAL
abstract                  |ACC_ABSTRACT
super                     |ACC_SUPER
interface                 |ACC_INTERFACE
synthetic                 |ACC_SYNTETHIC
annotation                |ACC_ANNOTATION
enum                      |ACC_ENUM

Read more about the meaning of modifiers as well as as about the rules governing the allowed combinations of modifiers 
[in the JVM specification](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.1-200-E.1).

####Class members

As mentioned above a class statement contains member statements.
The following table lists all statements which can serve as members of a class statement. 
The second column of the table defines for each statement how many instances of it are allowed or required 
to exist within a class statement.

class member              |how many
--------------------------|----------------------------
[version](#version-statement)          |exactly one
[name](#name-statement)          	  |exactly one
[superclass](#superclass-statement)           |exactly one (except for the java.lang.Object class)
[interfaces](#interfaces-statement)       |zero or one
[synthetic](#synthetic-statement)        |zero or one
[deprecated](#deprecated-statement)       |zero or one
[source file](#source-file-statement)      |zero or one
[signature](#signature-statement)        |zero or one
[constant](#constant-statements)		  |zero or more
[method](#method-statement)			  |zero or more
[field](#field-statement)			  |zero or more
[annotation](#annotation-statement)       |zero or more
[bootstrap method](#bootstrap-method-statement) |zero or more
[inner class](#inner-class-statement) |zero or more
[enclosing method](#enclosing-method-statement) |zero or more
[unknown attribute](#TODO)|zero or more

####Class statement example

Here is an example of a class declaration including some members:

	:::lilac
	public interface abstract class {
		version 52.0;
		name ThisClass; 
		extends Object;
		const utf8 run_desc "()V";
		const utf8 RuntimeVisibleAnnotations_utf8 "RuntimeVisibleAnnotations";
		const utf8 SourceFile_utf8 "SourceFile";
		const utf8 Object_name "java/lang/Object";
		const utf8 ThisClass_name "java/lang/Runnable";
		const utf8 run_name "run";
		const classref Object Object_name; 
		const classref ThisClass ThisClass_name; 
		const utf8 type_desc "Ljava/lang/FunctionalInterface;";
		const utf8 source_file_name "Runnable.java";
		source file source_file_name; 
		annotation {
		   type type_desc; 
		}
	   
		public abstract method {
			name run_name; 
			descriptor run_desc; 
		}
	}

###Version statement
 
A version statement specifies the [version of the class file format](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.1) to use in the class file generated by the assembler. 
It is a [simple statement](#statements) with has as a single argument a [floating point literal](#literals) specifying the actual version number.
 
	:::ebnf
	version statement = 'version', floating point literal, ';' ;

Example:

	:::lilac
	version 52.0;

###Name statement

A name statement specifies the name of a [class](#class-statement), a [method](#method-statement), [field](#field-statement),[inner class](#inner-class-statement) or [annotation element](#annotation-element-statement). 
It is a [simple statement](#statements) which has as a single argument the [name](#names-and-labels) of an [utf8 constant](#utf8-constant-statement) which in turn
specifies the actual name as defined in the following EBNF expression:

	:::ebnf
	name statement = 'name', utf8 constant, ';' ;

Example:

	:::lilac
	name ThisClass; 

###Superclass statement

A superclass statement specifies the direct superclass of the current class. It is a [simple statement](#statements) which has as a single argument the
name of a [class reference constant](#class-reference-statement) which in turn specifies the actual super class as defined in the folowing EBNF expression:

	:::ebnf
	superclass statement = 'extends', class reference constant, ';' ;
	
Example:
	
	:::lilac
	extends Object;

There must always exist a superclass statement within a [class statement](#class-statement) except for one special case: a class with the name **java.lang.Object**
which is defined as the root of the Java class hierarchy.

###Interfaces statement

An interfaces statement specifies the superintefaces of the current class. It is a [simple statement](#statements) with multiple arguments, every one of which
is a name of a [class reference constant](#class-reference-statement). The [class reference constants](#class-reference-statement) in turn specify the actual super interfaces of the current class.
Every constant name may be preceded by a [label](#names-and-labels). The following EBNF expression defines the syntax of an interfaces statement:

	:::ebnf
	interfaces statement = 'implements', [label, ':'], class reference constant, {',',[label, ':'], class reference constant} , ';' ;

Example:

	:::lilac
	implements runnable: Runnable,Serializable;

###Signature statement

A signature statement specifies a [signature](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.9.1) for a [class](#class-statement), 
[method](#method-statement) or [field](#field-statement). It is a [simple statement](#statements) whose single argument ist the [name](#names-and-labels) of 
an [utf8 constant](#utf8-constant-statement) which in turn specifies the actual signature as defined in the following EBNF expression:

	::ebnf
	signature statement = 'signature', utf8 constant, ';' ;
	
Example:

	:::lilac
	signature ThisClassSignature;

Note: like the JVM itself the java assembler doesn't enforce the correct syntax of a signature. 
Read more about this syntax [here](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.9.1).

###Deprecated statement

This statement marks a [class](#class-statement), [method](#method-statement) or [field](#field-statement) as deprecated. 
It consists of a single keyword as defined in the following EBNF expression:

	:::ebnf
	deprecated statement = 'deprecated', ';' ;
	
###Synthetic statement

This statement marks a [class](#class-statement), [method](#method-statement) or [field](#field-statement) as synthetic, that is, as have been generated by the compiler itself without having a 
corresponding declaration in the underlying [source file](#source-file-statement). It consists of a single keyword as defined in the following EBNF expression:

	:::ebnf
	synthetic statement = 'synthetic', ';' ;

###Source file statement

The source file statement specifies the source file from which the current class has been generated by a compiler. The main use of this information is for debugging purposes.
It is a [simple statement](#statements) whose single argument the [name](#names-and-labels) of 
an [utf8 constant](#utf8-constant-statement) which in turn specifies the actual source file name as defined in the following EBNF expression:
	
	:::ebnf
	source file statement = 'source file', utf8 constant, ';' ;
	
Example:

	:::lilac
	source file SourceFileName;

###Constant statements

A constant statement declares a constant which may be used by other statements in the program. Java assembler uses constants not only as symbolic names
for literals like in high level program languages but also to hold dynamic linking infos like class, method or field references.


####Utf8 constant statement

An utf8 constant statement declares an utf8 string constant. In has as a single argument a [string literal](#literals), which specifies the value of the constant. The syntax of the statement is as follows:

	:::ebnf
	utf8 constant statement = 'const utf8',name, string literal, ';' ;

Example

	::lilac
	const utf8 helloworld "Hello World";

####Class reference statement

A class reference statement declares a class reference constant. It has as a single argument the name of
an [utf8 constant](#utf8-constant-statement) which in turn specifies the actual class name. The utf8 constant must contain either a [valid binary class name](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.2.1)
or a [valid array type descriptor](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.3.2). The syntax of the statement is as follows:


	:::ebnf
	class reference constant statement = 'const classref',name, utf8 constant, ';' ;

Example
	
	:::lilac
	const classref Object object_utf8;

####Name and type statement

A name an type statement declares a name and type constant, that is, a constant combining a name and a type descriptor. The statement has the names of two [utf8 constants](#utf8-constant-statement) 
as arguments. The first utf8 constant contains a valid field or method name, the second a valid field or method descriptor as defined in the [JVM specification](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.6).
The syntax of the statement is as follows:
	
	:::ebnf
	name and type constant statement = 'const nameandtype',name, utf8 constant, utf8 constant, ';' ;

Example:

	:::lilac
	const nameandtype hash_nat hash_name, hash_desc;

####Field reference statement

A field reference statement declares a field rerefence constant. It has two arguments. The first argument specifies the name of a [class reference constant](#class-reference-statement) which in turn specifies the actual class or interface owning the field. The second argument specifies the name of a  [name and type constant](#name-and-type-statement) which in turn specifies the name an the type of the field.
The syntax of the statement is as follows:

	:::ebnf
	field reference statement = 'const fieldref',name, class reference constant, name and type constant, ';' ;
	
Example:

	:::lilac
	const fieldref hash ThisClass, hash_nat;

####Method reference statement

A method reference statement declares a class method reference constant. Note that this statement can be used only to define references for **class** methods while interface method references are defined 
using an [interface method reference statement](#interface-method-reference-statement).
The statement has two arguments: the first argument specifies the name or a [class reference constant](#class-reference-statement) which in turn specifies the actual class owning the method, the second argument specifies the name of a  [name an type constant](#name-and-type-statement) which in turn specifies the name an the type of the method.
The syntax of the statement is as follows:

	:::ebnf
	method reference statement = 'const methodref',name, class reference constant, name and type constant, ';' ;
	
Example:

	:::lilac
	const methodref Character.isBmpCodePoint Character, Character.isBmpCodePoint_nat;

####Interface method reference statement

An interface method reference statement declares an interface method reference constant. Note that this statement can be used only to define references for **interface** methods, while class method references are defined 
using an [method reference statement](#method-reference-statement).
The statement has two arguments: the first argument specifies the name or a [class reference constant](#class-reference-statement) which in turn specifies the actual interface in which the method resides, the second argument specifies the name of a  [name an type constant](#name-and-type-statement)  which in turn specifies the name an the type of the method.
The syntax of the statement is as follows:

	:::ebnf
	interface method reference statement = 'const intfmethodref',name, class reference constant, name and type constant, ';' ;
	
Example:

	:::lilac
	const intfmethodref CharSequence.length CharSequence, AbstractStringBuilder.length_nat;
    
####String statement

A string statement declares a string constant, which can be used by [instructions](#instruction-statements). The statement has as a single argument the name of an [utf8 constant](#utf8-constant-statement),
which specifies the actual string content.
The syntax of the statement is as follows:

    :::ebnf
	string statement = 'const string', name, utf8 constant ';' ;
    
Example:

	:::lilac
	const string charsetName utf8_319;

####Integer value statement

An integer value statement declares an integer constant. It has as a single argument an [integer literal](#literals), which specifies the value of the constant.
The syntax of the statement is as follows:

    :::ebnf
	integer statement = 'const int', name, integer literal, ';' ;

Example:

	:::lilac
	const int int_15 124567;

####Long integer value statement

A long integer value statement declares a long integer constant. It has as a single argument an [integer literal](#literals), which specifies the value of the constant.
The syntax of the statement is as follows:

    :::ebnf
	long integer statement = 'const long', name, integer literal, ';' ;

Example:

	:::lilac
	const long long_15 124567;

####Floating point value statement

A floating point value statement declares a floating point constant. It has as a single argument a [floating point literal](#literals), which specifies the value of the constant.
The syntax of the statement is as follows:

    :::ebnf
	floating point value statement = 'const float', name, floating point literal, ';' ;

Example:

	:::lilac
	const float float_15 123.5;

####Double-precision floating point value statement

A double-precision floating point value statement declares a double-precision floaing point constant. It has as a single argument a [floating point literal](#literals), which specifies the value of the constant.
The syntax of the statement is as follows:

    :::ebnf
	double-precision floating point value statement = 'const double', name, floating point literal, ';' ;

Example:

	:::lilac
	const double double_15 123.5;

####Method type statement

A method type statement declares a method type constant. It has as a single argument the name of an utf8 constant, which in turn specifies a valid method descriptor as defined in the [JVM specification](#http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.6).
The syntax of the statement is as follows:

    :::ebnf
	method type statement = 'const methodtype', name, utf8 constant, ';' ;
    
Example:

	:::lilac
	const methodtype mType mType_utf8;
    
####Method handle statement

A method handle statement declares a method handle constant. The value of such constant is
[a typed, directly executable reference to an underlying method, constructor, field, or similar low-level operation, with optional transformations of arguments or return values](http://docs.oracle.com/javase/8/docs/api/java/lang/invoke/MethodHandle.html).
The syntax of the statement is as follows:

    :::ebnf
    method handle statement = 'const ', ('getfield'|'getstatic'|'putfield'|'putstatic'|'invokespecial'|'invokevirtual'|'invokeinterface'|'invokestatic'|'newinvokespecial'), 'methodhandle', name,constant, ';' ;


As can be seen from the definition above a method handle defined by a method handle statement can belong to one of 9 different "kinds" (see more [here](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.8) ),
specified by one of the 9 corresponding modifier keywords.
The statement has as a single argument the name of a constant, which, dependent on the "kind", can be a [field reference](#field-reference-statement), a [method reference](#method-reference-statement)
or an [interface method reference statement](#interface-method-reference-statement).

Example:

    :::lilac
    const getstatic methodhandle System.out;

####Dynamic method reference statement

A dynamic method reference statement declares a dynamic method reference constant which can be used as an argument of the [invokedynamic instruction](#nstructions-with-constant-arguments).
The statement has two arguments - the first argument specifies the name of a [bootstrap method](#bootstrap-method-statement), the second argument specifies the name of a [name and type constant](#name-and-type-statement).
Read more about the exact meaning of these two arguments in the [JVM specification](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.4.10).
The syntax of the statement is as follows:

    :::ebnf
    dynamic method reference statement = 'const dynref',name,bootstrapmethod,',',name and type constant,';' ;

Example:

    :::lilac
    const dynref inv1 bootstrapmethod1, name_and_type1;
    
###Field statement

A field statement declares a field within a class type. It is a [block statement](#statements),which  consists of some [field modifier keywords](#field-modifiers) followed by the keyword **field** 
and then by some [field member statements](#field-members) enclosed in curly brackets as defined in the following EBNF expression:

    :::ebnf
	field statement = {field modifier}, 'class', '{',{class member},'}' ;
	field modifier = 'public'|'final'|'abstract'|'super'|'interface'|'synthetic'|'annotation'|'enum' ;
	field member = name|descriptor|constant value|signature|synthetic|deprecated|annotation|type annotation|unknown attribute ;

####Field modifiers

Field modifiers in the Java assembler correspond one to one to the [access flags defined in the JVM specification](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.5)
as shown in the following table:

Assembler modifier keyword|access flag from the Jvm spec
--------------------------|----------------------------
public                    |ACC_PUBLIC
private                   |ACC_PRIVATE
protected                 |ACC_PROTECTED
static                    |ACC_STATIC
final                     |ACC_FINAL
volatile                  |ACC_VOLATILE
transient                 |ACC_TRANSIENT
synthetic                 |ACC_SYNTHETIC
enum                      |ACC_ENUM

Read more about the meaning of modifiers as well as as about the rules governing the allowed combinations of modifiers 
[in the JVM specification](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.5)

####Field members

The following table lists all statements which can serve as members of a field statement. 
The second column of the table defines for each statement how many instances of it are allowed or required 
to exist within a field statement.

field member              |how many
--------------------------|----------------------------
[name](#name-statement)          	  |exactly one
[descriptor](#descriptor-statement)          	  |exactly one
[constant value](#constant-value-statement)           |zero or one
[synthetic](#synthetic-statement)        |zero or one
[deprecated](#deprecated-statement)       |zero or one
[signature](#signature-statement)        |zero or one
[annotation](#annotation-statement)       |zero or more
[unknown attribute](#TODO)|zero or more


####Field statement example

    :::lilac
    private static final field {
      name serialVersionUID_name; // serialVersionUID
      descriptor serialVersionUID_desc; // J
      constant value long_158; // -6849794470754667710
    }


###Descriptor statement

A name statement specifies the descriptor of a [method](#method-statement) or  a [field](#field-statement). 
It is a [simple statement](#statements) which has as a single argument the [name](#names-and-labels) of an [utf8 constant](#utf8-constant-statement) which in turn
specifies the actual descriptor string as shown in the following EBNF expression:

	:::ebnf
	descriptor statement = 'descriptor', utf8 constant, ';' ;

Dependent on the statement's context the descriptor string must contain either a [valid field descriptor](#http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.3.2)
or a [valid method descriptor](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.3.3).

Example:
    
	:::lilac
	descriptor method_descriptor;
    
###Constant value statement

A constant value statement specifies an  initial value for a static [field](#field statement) as described in the [JVM specification](#https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.2).
It is a [simple statement](#statements) which has as a single argument the [name](#names-and-labels) of a [long, float, double, or string constant](#constant-statements)
which in turn contains the actual value to assign as defined in the following EBNF expression:

    :::ebnf
	constant value statement = 'constant','value', constant, ';' ;

Example:
    
    ::lilac
    constant value string123;
    
###Method statement

A method statement declares a method within a class type. It is a [block statement](#statements),which  consists of some [method modifier keywords](#method-modifiers) followed by the keyword **method** 
and then by some [method member statements](#method-members) enclosed in curly brackets as defined in the following EBNF expression:

    :::ebnf
	method statement = {method modifier}, 'method', '{',{method member},'}' ;
	method modifier = 'public'|'private'|'protected'|'static'|'final'|'synchronized'|'bridge'|'varargs'|'native'|'abstract'|'strict'|'synthetic' ;
	method member = name|descriptor|exception|signature|synthetic|deprecated|annotation|parameter annotation|type annotation|annotation default|stack map|unknown attribute|variable|instruction|exception handler|line number table|variable table|variable type table|max stack|max locals;

####Method modifiers

Method modifiers in the Java assembler correspond one to one to the [access flags defined in the JVM specification](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.6)
as shown in the following table:

Assembler modifier keyword|access flag from the Jvm spec
--------------------------|----------------------------
public                    |ACC_PUBLIC
private                   |ACC_PRIVATE
protected                 |ACC_PROTECTED
static                    |ACC_STATIC
final                     |ACC_FINAL
synchronized              |ACC_SYNCHRONIZED
bridge                    |ACC_BRIDGE
varargs                   |ACC_VARARGS
native                    |ACC_NATIVE
abstract                  |ACC_ABSTRACT
strict                    |ACC_STRICT
synthetic                 |ACC_SYNTHETIC


Read more about the meaning of modifiers as well as as about the rules governing the allowed combinations of modifiers 
[in the JVM specification](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.6)
  
####Method members

The following table lists all statements which can serve as members of a method statement. 
The second column of the table defines for each statement how many instances of it are allowed or required 
to exist within a method statement.

method member              |how many
--------------------------|----------------------------
[name](#name-statement)          	  |exactly one
[descriptor](#descriptor-statement)          	  |exactly one
[exceptions](#exceptions-statement)           |zero or more
[synthetic](#synthetic-statement)        |zero or one
[deprecated](#deprecated-statement)       |zero or one
[signature](#signature-statement)        |zero or one
[annotation](#annotation-statement)       |zero or more
[annotation default](#annotation-default-statement)  |zero or one
[stack map](#stackmap-statement)  |zero or one
[unknown attribute](#TODO)|zero or more
[variable](#variable-statement)|zero or more
[instruction](#instruction-statements)|zero or more
[exception handler](#exception-handler-statement)|zero or more
[line numbers](#line-numbers-statement)|zero or more
[debug variables](#debug-variables-statement)|zero or more
[max stack](#max-stack-statement)|zero or one
[max locals](#max-locals-statement)|zero or one
 
**Note:** method members can appear in any order within a method statement. The order doesn't have any semantic meaning, however, different orders may result in binary different though
semantically identical class files.

####Method statement example

    :::lilac
    static method {
        name clinit0_name; // <clinit>
        descriptor method_desc$1; // ()V
        line numbers {
          line ir0, 129;
          line ir7, 1171;
        }
        maxstack 3;
        //Instructions
        ir0: iconst_0;
        anewarray ObjectStreamField;
        putstatic serialPersistentFields;
        ir7: new String$CaseInsensitiveComparator;
        dup;
        aconst_null;
        invokespecial String$CaseInsensitiveComparator.init0;
        putstatic CASE_INSENSITIVE_ORDER;
        return;
    }

###Exceptions statement

An exceptions statement specifies exceptions which may be thrown by the surrounding method. It is a [simple statement](#statement) with multiple arguments. All arguments specify a name of
a [class reference constant](#class-reference-statement). The [class reference constants](#class-reference-statement) in turn specify the actual exception classes thrown by the method.
Every class reference constant may be preceded by a [label](names-and-labels). The following EBNF expression defines the syntax of the exceptions statement:

	:::ebnf
	exceptions statement = 'throws', [label, ':'], class reference constant, {',',[label, ':'], class reference constant} , ';' ;

Example:

	:::lilac
	throws ioexception: IOException,IllegalArgumentException;
 

###Variable statement

A variable statement declares a [local variable](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-2.html#jvms-2.6.1) within a [method statement](#method-statement).
The statement specifies the type and the name of the variable, the allowed types being: **double**, **float**, **int**, **long**, **object**
and **returnadress**. Additionally the index of the variable within the [local variable array](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-2.html#jvms-2.6.1)
may be specified, either as an absolute index value or  as an offset relative to the index an another variable. Note, that, dependent on the type, a variable
can occupy one or two slots in the [local variable array](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-2.html#jvms-2.6.1).
For the variables whose declarations do not contain an index specification the assembler assigns an implicit index which is the slot following on the last slot occupied by the previously declared variables.

**Note:** it is allowed by the JVM specification that multiple variables occupy the same slot in the [local variable array](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-2.html#jvms-2.6.1).

The exact syntax of the variable statement is as specified in the following EBNF expression:

    ::ebnf
    variable statement = 'var', variable type, identifier, [index], ';' ;
    variable type = 'double'|'float'|'int'|'long'|'object'|'returnadress' ;
    index  = 'at', [relative offset|non-negative integer] ;
    relative offset = identifier, '+'|'-', non-negative integer ;
    
Example:

    ::lilac
    var int a;
    var float b;
    var double d1 at 0;
    var double d2 at b - 1;
    var double d2 at b;
    var double d3;
    var double d4 at b + 1;

The example above illustrates various possibilities to specify the index of a variable. The integer variable **a** has an implicit index **0** and
occupies one slot. The floating point variable b also has an implicitl index, this time it is **1** and again occupies one slot. The double precision variable d1
has an explicitly specified index **0** (remember: it's allowed for multiple variables to occupy the same slots) and occupies two slots. The variable d2 has, again,
an explicitly specified index 0, this time however, the index has been specified as an offset relative to the slot occupied by **b**.
And so on...

###Instruction statements

An instruction statement specifies an instruction from the [JVM instruction set](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5) within a [method statement](#method-statement). An instruction statement generally starts
with a **mnemonic keyword** which might be followed by one or more arguments. An argument of an instruction can be a name of a [constant](#constant-statements)
or a [local variable](#variable-statement), a [label](#names-and-labels) of an instruction or a [literal](#literals). The instruction statement might be preceded by a
[label](#names-and-labels).

####Correspondence betweeen the mnemonic keywords in Java assembler and the [JVM instruction set](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5).

In general the mnemonic keywords of the Java assembler correspond one to one with the instructions from the [JVM instruction set](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5).
There are however the following exceptions:

- The argument-less **load** and **store** instructions from the the [JVM instruction set](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5) such
as [aload_0](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.aload_n) or [fstore_1](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.fstore_n) don't have
a corresponding **mnemonic keyword**. Instead they will always be created by the assembler if a **aload**, **astore**, **iload**, **istore**,**dload**, **dstore**,
 **fload** or **fstore** instruction statement has as argument a [local variable](#variable-statement) whose index lies between **0** and **3**. If in such case
you want the assembler to generate a "normal" load or store instruction, you have to precede the mnemonic keyword of the instruction with the modifier keyword
**normal**.

- To generate the instruction [wide](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.wide) from the [JVM instruction set](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5) you have to precede
the **mnemonic keyword** of an instruction statement with the modifier keyword **wide**.

Examples:

    ::lilac
    normal load this;
    wide store this;

####Instruction categories

Most JVM instructions belong to one of three following categories: [argumentless instructions](#argumentless-instructions), [constant instructions](#instructions-with-constant-arguments), [variable instructions](#instructions-with-local-variable-arguments) and [branch instructions](#branch-instructions). Additionally there are some instructions which don't
fit an any of thost broad categories but stand on their own.

#####Argumentless instructions

These are the instructions which don't have any arguments. The majority of the of the JVM instructions belongs to this category. The following list contains all argumentless instructions with every entry linked to
the corresponding section in the JVM specification:

[aaload](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.aaload), [aastore](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.aastore), [aconst_null](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.aconst_null), [areturn](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.areturn), [arraylength](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.arraylength), [athrow](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.athrow), [baload](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.baload), [bastore](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.bastore), [caload](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.caload), [castore](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.castore), [d2f](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.d2f), [d2i](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.d2i), [d2l](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.d2l), [dadd](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.dadd), [daload](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.daload), [dastore](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.dastore), [dcmpg](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.dcmpg), [dcmpl](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.dcmpl), [dconst_0](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.dconst_0), [dconst_1](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.dconst_1), [ddiv](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ddiv), [dmul](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.dmul), [dneg](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.dneg), [drem](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.drem), [dreturn](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.dreturn), [dsub](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.dsub), [dup](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.dup), [dup_x1](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.dup_x1), [dup_x2](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.dup_x2), [dup2](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.dup2), [dup2_x1](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.dup2_x1), [dup2_x2](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.dup2_x2), [f2d](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.f2d), [f2i](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.f2i), [f2l](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.f2l), [fadd](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.fadd), [faload](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.faload), [fastore](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.fastore), [fcmpg](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.fcmpg), [fcmpl](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.fcmpl), [fconst_0](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.fconst_0), [fconst_1](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.fconst_1), [fconst_2](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.fconst_2), [fdiv](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.fdiv), [fmul](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.fmul), [fneg](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.fneg), [frem](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.frem), [freturn](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.freturn), [fsub](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.fsub), [i2b](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.i2b), [i2c](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.i2c), [i2d](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.i2d), [i2f](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.i2f), [i2l](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.i2l), [i2s](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.i2s), [iadd](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.iadd), [iaload](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.iaload), [iand](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.iand), [iastore](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.iastore), [iconst_m1](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.iconst_m1), [iconst_0](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.iconst_0), [iconst_1](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.iconst_1), [iconst_2](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.iconst_2), [iconst_3](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.iconst_3), [iconst_4](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.iconst_4), [iconst_5](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.iconst_5), [idiv](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.idiv), [imul](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.imul), [ineg](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ineg), [ior](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ior), [irem](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.irem), [ireturn](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ireturn), [ishl](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ishl), [ishr](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ishr), [isub](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.isub), [iushr](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.iushr), [ixor](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ixor), [l2d](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.l2d), [l2f](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.l2f), [l2i](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.l2i), [ladd](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ladd), [laload](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.laload), [land](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.land), [lastore](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lastore), [lcmp](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lcmp), [lconst_0](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lconst_0), [lconst_1](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lconst_1), [ldiv](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ldiv), [lmul](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lmul), [lneg](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lneg), [lor](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lor), [lrem](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lrem), [lreturn](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lreturn), [lshl](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lshl), [lshr](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lshr), [lsub](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lsub), [lushr](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lushr), [lxor](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lxor), [monitorenter](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.monitorenter), [monitorexit](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.monitorexit), [nop](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.nop), [pop](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.pop), [pop2](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.pop2), [return](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.return), [saload](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.saload), [sastore](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.sastore), [swap](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.swap)

Example:

    ::lilac
    aaload;
    
**Note:** The adjective "argumentless" doesn't mean in the context of JVM instructions, that the instructions in questions don't manipulate any arguments as they are executed.
They just take all their arguments from the JVM stack and so in the assembler source and in the bytecode generated from it they don't need to have any arguments specified.

#####Instructions with constant arguments

These are the instructions which have as a single argument the name of a [constant](#constant-statements). Dependent on the instruction there may be restrictions on the types of the constants allowed as argument.
The following list contains all instructions from this category with every entry linkedto the corresponding section in the JVM specification:

[invokeinterface](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.invokeinterface), [invokedynamic](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.invokedynamic), [anewarray](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.anewarray), [checkcast](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.checkcast), [getfield](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.getfield), [getstatic](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.getstatic), [instanceof](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.instanceof), [invokespecial](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.invokespecial), [invokestatic](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.invokestatic), [invokevirtual](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.invokevirtual), [ldc2_w](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ldc2_w), [ldc2](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ldc2), [new](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.new), [putfield](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.putfield), [putstatic](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.putstatic)

Example:

    ::lilac
    new java.lang.String;
    
#####Instructions with local variable arguments

These are the instructions which have a single argument the name of a [local valiable](#variable-statement). Dependent on the instruction there may be restrictions on the types of the variables allowed as argument.
The following list contains all instructions from this category with every entry linkedto the corresponding section in the JVM specification:

[aload](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.aload), [astore](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.astore), [dload](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.dload), [dstore](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.dstore), [fload](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.fload), [fstore](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.fstore), [iload](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.iload), [istore](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.istore), [lload](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lload), [lstore](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lstore),[ret](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ret)

Example:

    ::lilac
    aload this;

#####Branch instructions

These are the instructions causing the virtual machine to begin, conditionaly or unconditionally, to transfer the execution to an instruction different than the next instruction. Branch instructions
have as a single argument the [label](#names-and-labels) of the instruction to which the execution will be transferred.
The following list contains all instructions from this category with every entry linkedto the corresponding section in the JVM specification:

[goto](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.goto), [goto_w](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.goto_w), [if_acmpeq](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.if_acmpeq), [if_acmpne](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.if_acmpne), [if_icmpeq](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.if_icmpeq), [if_icmpge](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.if_icmpge), [if_icmpgt](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.if_icmpgt), [if_icmple](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.if_icmple), [if_icmplt](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.if_icmplt), [if_icmpne](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.if_icmpne), [ifeq](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ifeq), [ifge](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ifge), [ifgt](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ifgt), [ifle](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ifle), [iflt](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.iflt), [ifne](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ifne), [ifnonnull](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ifnonnull), [ifnull](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.ifnull), [jsr](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.jsr), [jsr_w](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.jsr_w)

Example:

    ::lilac
    goto end;

#####Integer literal instructions

These are the instructions which take an integer literal as argument. There are two instructions of this type:
[bipush](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.bipush), taking argument values from -128 to 127,
and  [sipush](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.sipush), taking argument values from -32768 to 32767.

Examples:
    
    ::lilac
    bipush 120;
    sipush 1000;

#####iinc instruction

The [iinc](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.iinc) instruction takes two arguments:
the name of a local variable and an integer literal in the value range from -128 to 127.

Example:

    ::lilac
    iinc counter,2;

#####newarray instruction

The [newarray](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.newarray) instruction, which creates a primitive array on the stack,
has a special syntax consisting of two keywords: the **newarray** keyword followed by the keyword designating a primitive type which can be one
of the following: **boolean**, **byte**, **char**, **double**, **float**, **int**, **long, **short**.

#####multianewarray instruction

The [multianewarray](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.multinewarray) instruction, which creates a multidimensional
array, takes two arguments: the name of a [class reference constant](#class-reference-statement) and a integer literal in the range from 1 to 255.

Example:

    ::lilac
    multianewarray Object,3;

#####Switch instructions

Switch instructions, which include [tableswitch](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.tableswitch) and [lookupswitch](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.lookupswitch),
are special branch instructions which calculate their target dependent on the integer value from the stack. A switch instruction has multiple arguments in a special syntax: an integer literal or the keyword
**default** followed by **->** and then by the label of the target instruction. 

Example:

    ::lilac
    tableswitch 0->target0,1->target1,default->defaulttarget;
    

####Bytecode ranges

Some statements require as arguments so called **bytecode ranges**. A **bytecode range** is just a sequence of instructions specified by two instruction labels separated by **->** : the label of the first instruction in the range and,
dependent on the context, either the label of the last instruction in the sequence or the label of the first instruction after the sequence. In the first case the specified **bytecode range** is an **including bytecode range**
otherwise an **excluding byte code range**. When specifying an **excluding byte code range** the second label may be omitted indicating, that the **bytecode range** includes all instructions beginning with the first instruction and ending
with the last instruction of the method.

Example:

    ::lilac    
    begin->end

###Exception Handler statement

An exception handler statement specifies an exception handler within a method. This statement is neither a [simple statement](#statement) nor a [block statement](#statement) but has instead it's own syntax which
is specified in the following EBNF expression:

    ::ebnf
    exception handler statement = [label, ':'], 'try', including bytecode range, catch, class reference constant|'all', 'go', 'to', label, ';' ;

**In words**: the statement starts with the **try** keyword, possibly preceded by a label. It follows an [including bytecode range](#bytecode-range), to which the handler applies. As next appears the keyword **catch** followed either by the name of a [class reference constant](#class-reference-statement), which
specifies the exception class handled by the handler, or by the keyword **all**, which indicates that the handler handles all exception. As last we see keywords **go** and **to** followed by the label of the instruction,
to which the handler transfers control when an exception occures.

Example:

    ::lilac
    firstHandler: try begin -> end catch RuntimeException go to handlerBegin;

###Line numbers statement

A line numbers statement specifies line numbers of the original [source file](#source-file-statement). It is a [block statement](#statements) with [line number statements](#line number statement) as members as defined
in the following EBNF expression:

    ::ebnf
    line numbers statement = 'line', 'numbers', '{', {line number}, '}' ;

Example:
    
    ::lilac
    line numbers {
        line label, 5;
    }

####Line number statement

A line number statement specifies the correspondence between an [instruction](#instruction-statements) and a line in the original [source file](#source-file-statement). This is a [simple statement](#statements) with two arguments,
the first argument is the label of the instruction, the second is an integer literal specifing the number of the source file line. The exact syntax is as follows:

    ::ebnf
    line number statement = 'line', label, line number, ';' ;

Example:

    ::lilac
    line label, 5;
    

###Debug variables statement

A debug variables statement specifies a mapping between the [local variables](variable-statement) in a method and variables in the original [source file](source-file-statement). It is a [block statement](#statements) with with [debug variable statements](#debug variable statement) as members as defined
in the following EBNF expression:

    ::ebnf
    debug variables statement = 'debug', 'variables',{'types'} '{', {debug variable}, '}' ;

Example:
    
    ::lilac
    debug variables {
        var this, ir0, this_name, this_desc;
    }

**Note**: the optional keyword **types** indicates that the mapping includes [signatures](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.9.1) of the source variables. The absence of the keyword
indicates that the [descriptors](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.3.3) are included.

####Debug variable statement

A debug variable statement specifies a mapping between a [local variable](variable-statement) in a method and a variable in the original [source file](source-file-statement). It is a [simple statement](#statements) with four arguments:
the name of the local variable, the [excluding bytecode range](#bytecode ranges), a name of an [utf8 constant](#utf8-constant-statement) specifying the name of the variable in the original source file, and at last a name
of an [utf8 constant](#utf8-constant-statement) specifying, dependent on the type of the surrounding [block statement](#debug-variables-statement) either the [descriptor](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.3.3)
of the variable in the original source file or its [signature](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.9.1).

The exact syntax is defined in the following EBNF expression:

    ::ebnf
    debug variable statement = 'var', name, ',', excluding bytecode range, ',',  utf8 constant, ',', utf8 constant, ';' ;
    
Example

    ::lilac
    var this, ir0, this_name, this_desc;

###Max stack statement

A max stack statement specifies the maximum runtime size of the stack of a method. If this member is absent in a [method statement](#method-statement) the Java
assembler will calculate this size itself. It is a [simple statement](#statements) with one argument - an integer literal specifying the actual stack size as
defined in the following EBNF expression:

    ::ebnf
    max stack statement = 'maxstack', integer literal, ';' ;

Example:

    ::lilac
    maxstack 5;

###Max locals statement

A max locals statement specifies the size of the [local variable array](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-2.html#jvms-2.6.1) of a method. If this member is absent in a [method statement](#method-statement) the Java
assembler will infer this size from the [variable statements](#variable-statement). It is a [simple statement](#statements) with one argument - an integer literal specifying the actual size as
defined in the following EBNF expression:

    ::ebnf
    max locals statement = 'maxlocals', integer literal, ';' ;

Example:

    ::lilac
    maxlocals 5;

###Stackmap statement

A stackmap statement specifies the [stackmap](#http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.10.1). There are two variants of this statement:
The first simple variant, to be used by developers, consists just of a single keyword **stackmap**. This variant tells the assembler to generate the stackmap
of the method itself. The second variant actually specifies the entire complicated stackmap structure and is primarily intended for the use by the disassembler
in order to ensure the perfect assembler/disassembler roundtrip.
In this specification no detailed explanation will be given for the second variant, as it shouldn't be used by developers. You might however get additional
information from the [JVM specification](#http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.4).
The syntax of the second variant is as defined in the folowing EBNF expression:

    ::ebnf
    stackmap statement = 'stackmap', '{', {stackmap frame}, '}';
    stackmap frame = append|chop|same frame|same frame extended|same locals|same locals extended|full ;
    append = 'append', label, typeslist, ';' ;
    chop = 'chop', label, integer literal, ';' ;
    same frame = 'same', label, ';' ;
    same frame extended = 'same', 'extended', label, ';' ;
    same locals = 'same', 'locals', label, types list, ';' ;
    same locals extended = 'same', 'locals',  'extended', label, typeslist, ';' ;
    full = 'full', label, typeslist, typeslist, ';' ;
    typeslist = '{', {'double'|'float'|'int'|'long'|'null'|'uninitialized'|'unititializedthis'|'top'},  '}' ;

Example (first variant):

    ::lilac
    stackmap;

Example (second variant):

    ::lilac
    stackmap {
      full ir17, {object ThisClass,object classref_214,int,int}, {};
      same ir30;
      same ir49;
    }
        
    
    

###Inner class statement

An inner class statement within a [class statement](#class-statement) declares an ["inner class"-relationship](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.6) between two classes.One of the classes participating in the relationship is usually the current class, though it is not a requirement.
The  inner class statement is a [block statement](#statements),which  consists of some [modifier keywords](#inner-class-modifiers) followed by the keywords **inner** and **class** 
and then by some [inner class member statements](#inner-class-members) enclosed in curly brackets as defined in the following EBNF expression:

    :::ebnf
	inner class statement = {inner class modifier}, 'inner', 'class', '{',{inner class member},'}' ;
	inner class modifier = 'public'|'private'|'protected'|'static'|'final'|'interface'|'abstract'|'synthetic'|'annotation'|'enum' ;
	inner class member = name|inner|outer;

Example:

    ::lilac
    public inner class {
        inner Foo$;
        outer Foo;
        name Foo_name;
    }

####Inner class modifiers

Inner class modifiers in the Java assembler correspond one to one to the [access flags defined in the JVM specification](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.6)
as shown in the following table:

Assembler modifier keyword|access flag from the Jvm spec
--------------------------|----------------------------
public                    |ACC_PUBLIC
private                   |ACC_PRIVATE
protected                 |ACC_PROTECTED
static                    |ACC_STATIC
final                     |ACC_FINAL
interface                 |ACC_INTERFACE
abstract                  |ACC_ABSTRACT
synthetic                 |ACC_SYNTHETIC
annotation                |ACC_ANNOTATION
enum                      |ACC_ANNOTATION

####Inner class members

An inner class statement contains member statements, referred to in the definition above as **inner class members**.
The following table lists all statements which can serve as members of an inner class statement. 
The second column of the table defines for each statement how many instances of it are allowed or required 
to exist within a method statement.

method member              |how many
--------------------------|----------------------------
[inner](#inner-and-outer-statement)          	  |exactly one
[outer](#outer-statement)          	  |zero or one
[name](#name-statement)           |zero or one

####Inner statement

This statement within an [inner-class-statement](#inner-class-statement) specifies the inner end of the "inner class"-relationship.
It has as a single argument the name of a [class reference constant](#class-reference-statement) which in turn specifies the actual class as shown in the folowing EBNF expression:

    ::ebnf
    inner statement = 'inner', class reference, ';' ;

####Outer statement

This statement within an [inner-class-statement](#inner-class-statement) specifies the outer end of the "inner class"-relationship.
It has as a single argument the name of a [class reference constant](#class-reference-statement) which in turn specifies the actual class as shown in the folowing EBNF expression:

    ::ebnf
    outer statement = 'outer', class reference, ';' ;

###Enclosing method statement

An enclosing statement specifies [the enclosing method](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.7) for the inner end of an "inner class"-relationship between two classes
(see also [Inner class statement](#inner-class-statement).
This is a simple statement, which has two arguments of which the second is optional. The first argument is the name of a [class reference constant](#class-reference-statement), which specifies
the class of the enclosing method. The second argument is the name of a [name and type constant](#name-and-type-statement), which specifies the name and the type of the enclosing method.
The absence of the second argument means, that the inner class isn't enclosed in a method. The exact syntax of the statement is as in the following EBNF expression:

    ::ebnf
    enclosing method statement = 'enclosing', 'method', class reference, [',', name and type], ';' ;
    
Example:

    ::lilac
    enclosing method InnerClassTests, nameandtype_26;

###Bootstrap method statement

A bootstrap method statement specifies a bootstrap method which can be referenced by a [dynamic method reference](#dynamic-method-reference-statement). It is a [simple statement](#statements) with has at least one
argument. The statement consists of the keywords **bootstrap** and **method** followed by the name of the method and then by comma separated arguments.
The first argument specifies a name of a [method handle constant](#method-handle-statement). Further arguments if present specify names of further constants
to be used as arguments of the bootstrap method as defined in the [JVM specification](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.23).
The exact syntax of the statement is as in the following EBNF expression:

    ::ebnf
    bootstrap method statement = 'bootstrap', 'method', method name, method handle constant, {',', argument constant} ;

Example:
    
    ::lilac
    bootstrap method firstBootstrapMethod method_handle, string1, string2;

###Annotation statement

An annotation statement declares either an [annotation](https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.6.1) for a class, method,method parameter,field or [a type declaration](https://docs.oracle.com/javase/tutorial/java/annotations/type_annotations.html)
or a [nested annotation within another annotation](#annotation-element-members).
An annotation statement, which is a [block statement](#statements),  consists of the keyword **annotation**  folowed by [annotation member statements](#annotation-members) enclosed in curly brackets
and possibly preceded by some of the following keywords: **invisible**, **parameter**, **type** as defined in the following EBNF expression:

    ::ebnf
	annotation statement = {'invisible'|'parameter'|type}, 'annotation', '{',{annotation member},'}' ;
	annotation member = annotation type|annotation element
    
The meaning of the keywords preceding **annotaion** is as follows:

 **invisible** - marks the annotation as invisible as runtime
 
 **parameter** - states that that the current annotation is a [method parameter annotation](https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.7.4).
 In this case the presence of the a [annotation parameter index](#annotation-parameter-index-statement)  as member is also requred.
 **Note**: parameter annotations are only allowed within of a [method statement](#method-statement)
 
 **type** - states that the current annotation is a a [type annotation](https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.7.4). In this case the presence of the [annotation target](#annotation-target-statement)
 as member is also required.

Example (Deprecated annotation):

    ::lilac
    annotation {
      type type_desc; // Ljava/lang/Deprecated;
    }

    
####Annotation members

An annotation statement contains member statements, referred to in the definition above as **annotation members**.
The following table lists all statements which can serve as members of an annotation statement. 
The second column of the table defines for each statement how many instances of it are allowed or required 
to exist within an annotation statement.

method member              |how many
--------------------------|----------------------------
[annotation type](#annotation-type-statement)          	  |exactly one
[annotation element](#annotation-element-statement)       |zero or more
[annotation parameter index](#annotation-parameter-index-statement)       |zero or one
[annotation target](#annotation-target-statement)       |zero or one
[annotation target path](#annotation-target-path-statement)       |zero or one

####Annotation type statement

An annotation type statement declares the type (annotation class) of the surrounding annotation. It is a [simple statement](#statements) which has as a single argument a name of [class reference constant](#class-reference-statement),
which in turn specifies the annotation class as shown in the following EBNF expression:

    ::ebnf
    annotation type statement = 'type', class reference, ';' ;

Example:

    ::lilac
    type type_desc;

####Annotation parameter index statement

An annotation parameter index statement specifies the index of a method parameter for which the surrounding annotation is being declared. It is a [simple statement](#statements) which has as a single argument an integer literal
specifying the actual index as shown in following EBNF expression:

    ::ebnf
    annotation parameter index statement = 'index',integer literal,';'

The index specified has to be a valid index of a parameter of the method for which the annotation has been declared. 

Example:

    ::lilac
    index 0;

####Annotation element statement

An annotation element statement specifies the value of an annotation element for an [annotation](#annotation-statement). An annotation element statement, which is [block statement](#statements), consists auf the keyword **element**
followed by some [annotation member statements](#annotation-members) enclosed in curly brackets as defined in the following EBNF expression:

    ::ebnf
    annotation element statement = 'element','{',{annotation element member},'}' ;
    annotation element member = name|annotation element value ;
    
Example:

    ::lilac
    element {
        name element1_name;
        string value empty_string; 
    }

#####Annotation element members

An annotation element statement contains member statements, referred to in the definition above as **annotation element members**.
The following table lists all statements which can serve as members of an annotation element statement. 
The second column of the table defines for each statement how many instances of it are allowed or required 
to exist within a annotation element statement.

method member              |how many
--------------------------|----------------------------
[name](#name-statement)          	  |exactly one
[annotation element value](#annotation-element-value-statement)       |zero or one
[annotation](#annotation statement)       |zero or one

An annotation element always has exactly two members: a name statement, specifying the name of the element and a value statement, which is either a  [annotation element value statement](#annotation-element-value-statement) or or
a [nested annotation](#annotation statement)

#####Annotation element value statement

An annotation element value statement either specifies a value for an annotation element or serves as a member for [another array annotation element value](#array-value) . This value can be a **simple value**, an **enumeration value** or an **array value**. Every one of these three possibility has a different syntax which
will be explained below:

######Simple value

The syntax for a simple value is that of a [simple statement](#statements) which consists of **type keyword** followed by the keyword **value** an the by a single argument.
The single argument is the name of a constant, the exact type of which dependends on the type of the value specified by the type keyword. The EBNF expression fo the syntax is as follows:

    ::ebnf
    simple value = type keyword, 'value', constant name, ';'
    type keyword = 'byte'|'boolean'|'char'|'class'|'float'|'double'|'int'|'long'|'short'|'string'

Example:

    ::lilac
    boolean value int_0;

######Enumeration value

The syntax for an enumeration is that of a [simple statement](#statements) which has two arguments as shown in the following EBNF experssion:

    ::ebnf
    enumeration value = 'enum', 'value', utf8 constant name, utf8 constant name, ';' ;

The first argument is the name of a [utf8 constant](#utf8-constant-statement) specifing the [type descriptor](#http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.3.2) an enumeration class, the second is the name of an [utf8 constant](#utf8-constant-statement) which
specifies the name of the enumeration member.

Example:

    ::lilac
    enum value type_desc, utf8_DAYS;

######Array value

The syntax for an array value ist that of a [block statement](#statements). This block statement can contain any number of nested [annotation element values](#annotation-element-value-statement) as shown in the foolowing EBNF expression:

    ::ebnf
    array value = 'array','value','{',{annotation element value},'}' ;

Example:

    ::lilac
    array value {
        boolean value int_0;
        boolean value int_1;
    }
    

####Annotation target statement

An annotation target statement denotes the kind of target on which a [type annotation](https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.7.4) appears.
The various kinds of target correspond to the type contexts of the Java programming language where types are used in declarations and expressions.
Dependent on the actual target the statement can have many different syntaxtic forms, which wiil be listed below.

#####Return target

States that the annotation appears on the return type of а method.

Syntax:
    
    ::ebnf
    return target statement = 'targets', 'return', 'type', ';' ;

Example:

    ::lilac
    targets return type;

#####Receiver type target

States that the annotation appears on the type of the [receiver parameter](http://docs.oracle.com/javase/specs/jls/se8/html/jls-8.html#jls-8.4.1) of а method.

Syntax:
    
    ::ebnf
    receiver type target statement = 'targets', 'receiver', 'type', ';' ;

Example:

    ::lilac
    targets receiver type;

#####Field type target

States that the annotation appears on the type of а field.

Syntax:
    
    ::ebnf
    field type target statement = 'targets', 'field', 'type', ';' ;

Example:

    ::lilac
    targets field type;
    
#####Parameter type target

States that the annotation appears on the type of а method parameter. This statement has an integer literal argument which denotes the index of the parameter

Syntax:
    
    ::ebnf
    parameter type target statement = 'targets', 'parameter', 'type', integer literal, ';' ;

Example:

    ::lilac
    targets parameter 0;

#####Parameter type bound target

States that the annotation appears on the type in the bound of а method parameter. This statement has two  integer literal arguments where the first denotes the index of the parameter
and the second the index of the annotated type within the bound.

Syntax:
    
    ::ebnf
    parameter type bound target statement = 'targets', 'type', 'parameter', 'bound', integer literal, ',', integer literal, ';' ;

Example:

    ::lilac
    targets type parameter bound 0, 1;


#####Type parameter target

States that the annotation appears on the type parameter of a generic class, interface, method or constructor. This statement has an integer literal as argument
which denotes the index of the type parameter.

Syntax:
    
    ::ebnf
    type parameter target statement = 'targets', 'type', 'parameter', integer literal, ';' ;

Example:

    ::lilac
    targets type parameter 0;

#####Supertype target

States that the annotation appears on the type in the **extends** or **implements** clause of a class. This statement has an optional argument - a label which, if present,
denotes annotated type from the [**implements** clause](#interfaces-statement). The absence of this argument indicates that the annotation appears in the **extends** clause.

Syntax:

    ::ebnf
    supertype target statement = 'targets', 'supertype', [interface label], ';' ;

Example:

    ::lilac
    targets supertype runnable;

#####Exception type target

States that the annotation appears on the exception type in the **throws** clause of a method. This statement has as single argument a label denoting the actual exception type
from the [**throws clause**](#exceptions-statement).

Syntax:

    ::ebnf
    exception type target statement = 'targets', 'throws', exception label, ';' ;

Example:

    ::lilac
    targets throws illegalargumentexception;

#####New type target

States that the annotation appears on the type in a **new** expression. This statement has has as single argument a label
which points to the corresponding [new instruction](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.new).

Syntax:

    ::ebnf
    new type target statement = 'targets', 'new', label, ';' ;

Example:

    ::lilac
    targets new newLabel;

#####Instanceof type target

States that the annotation appears on the type in a **instanceof** expression. This statement has as single argument a label
which points to the corresponding [instanceof instruction](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-6.html#jvms-6.5.instanceof).

Syntax:

    ::ebnf
    instanceof type target statement = 'targets', 'instanceof', label, ';' ;

Example:

    ::lilac
    targets instanceof instanceofLabel;

#####Method reference type target

States that the annotation appears on the type in a [method reference expression](https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html#jls-15.13). This statement has as single argument a label
which points to the corresponding bytecode instruction.

Syntax:

    ::ebnf
    method reference type target statement = 'targets', 'method', 'reference', label, ';' ;

Example:

    ::lilac
    targets method reference methRefLabel;

#####Constructor reference type target

States that the annotation appears on the type in a [constructor reference expression](https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html#jls-15.13). This statement has as  single argument a label
which points to the corresponding bytecode instruction.

Syntax:

    ::ebnf
    constructor reference type target statement = 'targets', 'constructor', 'reference', label, ';' ;

Example:

    ::lilac
    targets constructor reference methRefLabel;

#####Cast type target

States that the annotation appears on the type in a **cast** expression. This statement has two arguments. The first argument is the label of the corresponding
bytecode instruction, the second is an integer literal, which specifies the index of the actual type in the cast expression.
A value of 0 in the second argument specifies the first (or only) type in the cast operator. The possibility of more than one type in a cast expression arises from a cast to an intersection type.  

Syntax:

    ::ebnf
    cast type target statement = 'targets', 'cast', 'type', label, integer literal, ';' ;

Example:

    ::lilac
    targets cast type castLabel, 0;

#####Constructor type argument target

States that the annotation appears on the type argument in a generic constructor invocation. This statement has two arguments. The first arguments´ is the label of the corresponding
bytecode instruction, the second is an integer literal, which specifies the index of the actual type argument. 

Syntax:

    ::ebnf
    constructor type argument target statement = 'targets', 'constructor', 'type', 'argument', label, integer literal, ';' ;

Example:

    ::lilac
    targets constructor type argument newLabel, 0;

#####Method type argument target

States that the annotation appears on the type argument in a generic method invocation. This statement has two arguments. The first argument is the label of the corresponding
bytecode instruction, the second is an integer literal, which specifies the index of the actual type argument. 

Syntax:

    ::ebnf
    method type target statement = 'targets', 'method', 'type', 'argument', label, integer literal, ';' ;

Example:

    ::lilac
    targets method type argument methodIvokeLabel, 0;

#####Constructor reference type argument target

States that the annotation appears on the type argument in a generic [constructor reference](https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html#jls-15.13) invocation. This statement has two arguments. The first argument is the label of the corresponding
bytecode instruction, the second is an integer literal, which specifies the index of the actual type argument. 

Syntax:

    ::ebnf
    constructor reference type argument target statement = 'targets', 'constructor', 'reference', 'type', 'argument', label, integer literal, ';' ;

Example:

    ::lilac
    targets constructor reference type argument label, 0;

#####Method reference type argument target

States that the annotation appears on the type argument in a generic [method reference](https://docs.oracle.com/javase/specs/jls/se8/html/jls-15.html#jls-15.13) invocation. This statement has two arguments. The first argument is the label of the corresponding
bytecode instruction, the second is an integer literal, which specifies the index of the actual type argument. 

Syntax:

    ::ebnf
    method reference type target statement = 'targets', 'method', 'reference', 'type', 'argument', label, integer literal,';' ;

Example:

    ::lilac
    targets method reference type argument label, 0;

#####Catch type target

States that the annotation appears on the type argument in a *catch* expression. This statement has as single argument the label of the corresponding
[exception handler](#exception-handler-statement). 

Syntax:

    ::ebnf
    catch type target statement = 'targets', 'catch', 'type', label,';' ;

Example:

    ::lilac
    targets catch type handlerLabel;

#####Variable type targetVariable

States  that the annotation appears on the type of a local variable including resource variables. This is a [block statement](#statements) with the following syntax:

    ::ebnf
    variable type target statement = 'targets', ['resource'], 'var', 'types', '{', {excluding bytecode range},'}' ;

The member statements of the block statement are [excluding bytecode ranges](#bytecode-ranges) specifying the areas of the method's bytecode where the variable is valid.


Example:
    
    ::lilac
    targets var types {
        begin -> end
    }
    
####Annotation target path statement

An annotation target path statements specifies the exact location of the annotation within a array, nested or parametrized type.
(see also the [explanations](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.20.2) in the JVM specification).
It is a [simple statement](#statements) with a variable number of arguments each of which specifies
an iterative, left-to-right step towards the precise location of the annotation in the type. The exact syntax of the statement is defined
in the following EBNF expression:

    ::ebnf
    annotation target path statement = 'target', 'path', path part, {path part} ;
    path part = 'array'|'nested'|'type', 'argument', 'bound'|'type', 'argument', '(', integer literal, ')' ;

Examples:
    
    ::lilac
    target path array;
    target path type argument bound;
    target path type argument(1);

Read more about the the meaning of the different argument types in the [JVM specification](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.20.2-220-B-A.1).

###Annotation default statement

An annotation default statement specifies the default value of an [annotation type element](https://docs.oracle.com/javase/specs/jls/se8/html/jls-9.html#jls-9.6.1). Because annotation type elements are technically methods of
[annotation classes](#class-statement) annotation default statements are always members of the corresponding [method statements](#method-statement).
An annotation default statement is a block statement with exact one member - an [annotation element value](annotation-element-value-statement) which specifies the actual value. The syntax is defined in the following EBNF expression:

    ::ebnf
    annotation default statement = 'annotation', 'default', '{', {annotation element value}, '}' ;

Example:

    ::lilac
    annotation default {
        boolean value int_0;
    }

###Unknown attribute

An unknown attribute statement specifies an [attribute](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7) of the class file whose
syntax is unknown to the assembler. This statement is primarily intended for the use by the disassembler in order to represent attributes either specific
to a JVM implementation or just, though standard, not implemented by the assembler - the second case ensuring a compatibility to
the future versions of the JVM. It is a [simple statement](#statements) with a [base64 literal](#literals) as a single argument. The base64 literal specifies
the binary content of the attribute. The exact syntax of the statement is defined in the following EBNF expression:

    ::ebnf
    unknown attribute statement = 'unknown', 'attribute', ['code'], base64 literal ;

Example:

    ::lilac
    unknown attribute [UG9seWZvbiB6d2l0c2NoZXJuZCBhw59lbiBNw6R4Y2hlbnMgVsO2Z2VsIFLDvGJlbiwgSm9naHVydCB1bmQgUXVhcms=];





    








