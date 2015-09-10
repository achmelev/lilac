##Questions and answers


###Q: Why does anyone need a java assembler and disassembler?

A: There are two major groups of people, who might need a java assembler and disassembler.
To the first group belong people interested in internal workings of the Java Virtual Machine. An assembler and a disassembler can be a great help if your want to develop an understanding for the JVM,
bytecode and other related topics. The second group are professional developers, like myself,
who sometimes have the need to peek into an tweak some java-based library or tool, for which, very unfortunately,
he sources aren't available. Of course this kind of challenge doesn't arise with open source libraries/tools and for the proprietary counterparts
one should contact the vendor, but sometimes the vendor doesn't exist anymore, or doesn't support your particular version, or just doesn't have time,
or for some other reason just cannot or doesn't want to help. And even with open source libraries it is often easier to just disassemble and change this one class making trouble
than to locate in the depths of the internet the exact version of the sources you need.   

###Q: But there are all those java [decompilers](https://developer.jboss.org/people/ozizka/blog/2014/05/06/java-decompilers-a-sad-situation-of) out there. Don't they serve the same purpose?

A: Yes, decompilers are really great and I'm using them myself all the time. Unfortunately, while they are great if you need to understand the logic of a library without sources,
it is not always easy or indeed possible to compile the decompiled code again. And even when it works you always have this nagging doubt about having possibly lost something
through the decompile/compile-again round trip. Besides, java decompilers are much less of use if the class file did't originate from a Java program but from some other JVM-based programming language (e.g. Scala).


###Q: OK, but there already some assemblers and disassemblers for the JVM, for example [Jasmin](http://jasmin.sourceforge.net/) or [Krakatau](https://github.com/Storyyeller/Krakatau). Why do we need another one?

A: Good question. Before beginning with Lilac development I've tried these and some other tools and found them unsatisfactory for a number of reasons as for example not offering a disassembler (Jasmin),
not delivering a perfect round-trip (if you disassemble a class file and then assemble it again, you don't get the original class), and last but not least an assembler syntax which forces you to use numeric
variable offsets instead of symbolic names. And, I really like to have my tools to be written in java, which rules Krakatau out. But this is, of course, only my personal opinion.


###Q: Why did you have to invent your own syntax instead if using the Jasmin-syntax, which has for now acquired a quasi-standard status?

A: Jasmin-syntax doesn't let you to declare constants, which in turn means, that a perfect round-trip is impossible with an assembler based on it. And the perfect round-trip is really important to me (see the last question).

###Q: How do I learn your (Lilac) assembler syntax?

A: The best way to do it is to "reverse-engineer", that is, every time you ask yourself how to code some Java feature in Lilac just write a Java class which uses this feature and disassemble
it with Lilac. This and extensive studying of the [JVM spec](http://docs.oracle.com/javase/specs/jvms/se8/html/) should do the trick.
Additionaly I intend to write a more or less comprehensive guide for the Lilac syntax, this however will take time.