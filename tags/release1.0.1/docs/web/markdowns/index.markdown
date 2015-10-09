Lilac is an assembler and disassembler for the Java Virtual Machine (JVM). The assembler parses text files written in an assembler-like syntax and creates Java class files,
which can be loaded into JVM. The disassembler does the same process in reverse, that is, it reads Java class files and creates text files which can be feeded to the assembler again.

Lilac has been created after the author has for some years searched in vain for a Java-assembler/disassembler solution suitable not only for educational but also professional purposes.

Two minimum goals Lilac sets out to achieve are:

* A perfect roundtrip - the assembling of a disassembled class file should yield the original file
* Symbolic syntax - the assembler sytax should use symblic names instead of magic numbers while accessing variables and constants
