<h1>Java Assembler reference - Lilac version</h1>

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

There are three different different literal types: integer literals, float point literals and string literals whose syntax is the same as [in the Java language](https://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html#jls-3.10). 

Here are some examples of a Java assembler literal:

	:::lilac
	"Hello Word"
	1234
	1235.56

####Identifiers

The syntax of a Java assembler identifier is defined as follows:

	:::ebnf
	identifier = javaidentifier, {'.' javaidentifier};

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
in the context of Java assembler those entities can be [variables](#TODO) and [constants](#TODO).

The following example shows two [constants](#TODO), the [string constant](#TODO) referencing the [utf8 costant](#TODO) by it's name:

	:::lilac
	const utf8 helloword_content "Hello World";
	const string helloword helloword_content;

Labels on the other hand identify a location inside the program to which, for example, the control flow of the program might be transferred. 
This is illustrated in the following example where the [if_acmpne instruction](#TODO) transfers the control to the [return instruction](#TODO)

	:::lilac
	if_acmpne end;
    iconst_1;
    putfield MyClass.int_field;
    end: return;

####Statements

On the syntactic level a Java assembler program is a sequence of **statements**, which come in two flavors: **simple statements** and **block statements**. 
A block statement in general consists of some keywords followed by a sequence of **member statements** enclosed in curly brackets as illustrated in the following
example:

	:::lilac
	private final field {
		name value_name;
		descriptor type_desc; 
	}

In the example we see [field statement](#TODO) which in turn contains two further statements: a [name statement](#TODO) and a [descriptor statement](#TODO).

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

From the fact that block statements themselves contain another statements follows, that the whole syntactic structure of a Java assembler program can be in effect 
mathematically seen as a [forest](http://en.wikipedia.org/wiki/Tree_%28graph_theory%29) of statements as a with **block statements** being parent nodes of their **child statements**. 
Indeed because on the semantic level there is an additional requirement, that a Java assembler source file contains exactly one [class statement](#TODO), 
this [forest](http://en.wikipedia.org/wiki/Tree_%28graph_theory%29) is in fact just one tree with the [class statement](#TODO) at the root.

##Language Statements

Having described above the general lexical and syntactic structure of a Java assembler program the rest of the document will discuss the particular statements which can be used in Java assembler covering their syntax and semantics.

###Class statement

A class statement specifies a class type. As already mentioned the must be exactly one class statement in a Java assembler source file. 
Note however, that in Java assembler the term "class" encompasses not only class types
[as defined by the Java language specification](https://docs.oracle.com/javase/specs/jls/se7/html/jls-8.html) which includes 
[classes in the narrow sense](https://docs.oracle.com/javase/specs/jls/se7/html/jls-8.html#jls-8.1) 
and [enums](https://docs.oracle.com/javase/specs/jls/se7/html/jls-8.html#jls-8.9), but also [interface types](https://docs.oracle.com/javase/specs/jls/se7/html/jls-9.html) including 
[interfaces](https://docs.oracle.com/javase/specs/jls/se7/html/jls-9.html#jls-9.1) and [annotation types](https://docs.oracle.com/javase/specs/jls/se7/html/jls-9.html#jls-9.7).

A class statement, which is a [block statement](#statements),  consists of some [class modifier keywords](#TODO) followed by the keyword **class** 
and then by some [class member statements](#TODO) enclosed in curly brackets as defined in the following EBNF expression:   

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
[in the JVM specification](https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.1-200-E.1)

####Class members

The class statement can contain some member statements, referred to in the definition above as **class members**.
The following table lists all statements which can serve as members if a class statement. 
The second column of the table defines for each statement how many instances of it are allowed or required 
to exist within a class statement.

class member              |how many
--------------------------|----------------------------
[version](#version-statement)          |exactly one
[name](#name-statement)          	  |exactly one
[superclass](#TODO)           |exactly one (except for the java.lang.Object class)
[interfaces](#TODO)       |zero or one
[synthetic](#TODO)        |zero or one
[deprecated](#TODO)       |zero or one
[constant](#TODO)		  |zero or more
[method](#TODO)			  |zero or more
[field](#TODO)			  |zero or more
[annotation](#TODO)       |zero or more
[type annotation](#TODO)  |zero or more
[bootstrap method](#TODO) |zero or more
[inner class](#TODO)      |zero or more
[enclosing method](#TODO) |zero or more
[unknown attribute](#TODO)|zero or more

####Class example

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
The syntax of this statement is as follows:
 
	:::ebnf
	version statement = 'version', float point literal

Example:

	:::lilac
	version 52.0;

###Name statement

A name statement specifies the name of a [class](class-statement), a [method](#TODO), [field](#TODO),[inner class](#TODO) or [annotation element](#TODO). 
It is a [simple statement](#statements) which has as a single argument the [name](#names-and-labels) of an [utf8 constant](#TODO) which in turn
contains the actual name as shown in the following EBNF expression:

	:::ebnf
	name statement = 'name', utf8 constant

Example:

	:::lilac
	name ThisClass; 

###Superclass statement

A superclass statement specifies the direct superclass of the current class. It is [simple statement](#statements) which has as a single argument the
name of [class reference constant](#TODO) which in turn specifies the actual super class as shown in the folowing EBNF expression:

	:::ebnf
	superclass statement = 'extends', class reference constant
	
Example:
	
	:::lilac
	extends Object;

The must be always a superclass statement within a class statement](#class-statement) except for one special case: a class with the name **java.lang.Object**
which is defined as the root of the Java class hierarchy.

###Interfaces statement

An interfaces statement specifies the superintefaces of the current class. It is a [simple statement](#statements) with multiple arguments, every one of which
is a name of [class reference constant](#TODO). The [class reference constants](#TODO) in turn specify the actual super interfaces of the current class.
The following EBNF expression defines the syntax of an interfaces statement:

	:::ebnf
	interfaces statement = 'implements', class reference constant, {',',class reference constant}

Example:

	:::lilac
	implements Runnable,Serializable;

      
