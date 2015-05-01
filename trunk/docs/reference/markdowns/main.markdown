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
A block statement in general consists of some keywords followed by a sequence of **child statements** enclosed in curly brackets as illustrated in the following
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

Having described above the general lexical and syntactic structure of a Java assembler progam the rest of the document will discuss the particular statements  
which can be used in Java assembler covering their syntax and semantics.

###Class statement

A class statement declares (of course) a class. Als already mentioned the must be exactly one class declaration in a Java assembler source file. 
Note however, that in Java assembler the term "class" encompasses not only class types
[as defined by the Java language specification](https://docs.oracle.com/javase/specs/jls/se7/html/jls-8.html) which includes 
[classes in the narrow sense](https://docs.oracle.com/javase/specs/jls/se7/html/jls-8.html#jls-8.1) 
and [enums](https://docs.oracle.com/javase/specs/jls/se7/html/jls-8.html#jls-8.9), but also [interface types](https://docs.oracle.com/javase/specs/jls/se7/html/jls-9.html) including 
[interfaces](https://docs.oracle.com/javase/specs/jls/se7/html/jls-9.html#jls-9.1) and [annotation types](https://docs.oracle.com/javase/specs/jls/se7/html/jls-9.html#jls-9.7).

A class statement, which is a [block statement](#statements),  consists of some [class modifier keywords](#TODO) followed by the keyword **class** 
and then by some [class member statements](#TODO) enclosed in curly brackets as defined in the following EBNF expression:   

	:::ebnf
	class statement = {class modifier}, 'class', '{',{class member},'}';
	class modifier = 'public'|'final'|'abstract'|'super'|'interface'|'syntetic'|'annotation'|'enum';
	class member = version|name|extends|implements|source file|signature|syntetic|deprecated|annotation|type annotation|bootstrap method|inner class|enclosing method|unknown attribute
	
	
      
