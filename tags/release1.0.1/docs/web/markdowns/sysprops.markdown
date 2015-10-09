##Lilac system properties

Like the JRE itself the operation of the Lilac tools can be controlled not only by command line [arguments](usage.html) but also by system properties. Lilac system properties are tool-specific and can be specified in one of two ways: on the command line with **-D** argument and in the configuration file jasm.conf in the **conf** subdirectory of the Lilac installation directory. Property values specified in **jasm.conf** have a prefix associated with the tool they apply to: the assemmbler prefix is **jasm** while the disassembler prefix is **jdasm**. On the command line this prefixes are omitted.

###Assembler system properties (prefix jasm)

Property Name                        | Description
-------------------------------------| ----------------------------------------
usethreadpool                        | **true**, if the assembler should use multiple threads while assembling, **false** otherwise. **false** is the default setting
threadpoolsize                       | The size of the thread pool when multiple threads are enabled. Default setting is **5**
verification.enabled                 | **true** enables the class verification, which includes among other things the checking of external class, method or field references as well as the bytecode verification, **false** disables it. Default setting is **true**
verification.annotations.enabled     | **true** enables the annotation-specific part of the verification, false disables it. Default setting is **true**. This property only has an effect if *verification.enabled=true*
verification.bytecode.enabled        | **true** enables the bytecode verification as specified in the [JVM spec](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.10), **false** disables it. Default setting is **true**. This property only has an effect if *verification.enabled=true*. NOTE: you have to enable the bytecode verification if you want the assembler to generate [stackmaps](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.4) for you.
forcestackmaps                       | **true** if the assembler should always generate stackmaps and ignore the possibly existing **stackmap**-statements the the assembler code. If **false** the assembler will only generate a stackmap when it encounters a **stackmap**-statement without arguments. Default setting is **false**.
classpath.useruntime                 |If this property is set to **true**, the assembler will use the classes of the current JRE while searching for external classes. Default setting is **true**.
dotwostages                          |If this property is set to **false** the assembler will process the .jasm-files one after another and independently from each other. That means while assembling a .jasm-file the assembler classpath wouldn't contain entries for another jasm files. Default setting is **true**

###Disassembler system properties (prefix jdasm)


Property Name                        | Description
-------------------------------------| ----------------------------------------
usethreadpool                        | **true**, if the disassembler should use multiple threads while disassembling, **false** otherwise. **false** is the default setting
threadpoolsize                       | The size of the thread pool when multiple threads are enabled. Default setting is **5**
omitdebuginfos                       | If set to **true** the disassemler wouldn't disassemble [debug info attributes](http://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.7.12). Default setting is **false**. 