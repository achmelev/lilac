##Lilac usage

As Lilac assembler and disassembler are command line tools their operation is controlled by arguments.
A command line argument usually consists of an **option** (e.g -input) possibly followed by one or more **option arguments** (e.g. -output /temp/bin).
Where appropriate the same **option** may be used multiple times.
This page describes the existing arguments and their meaning.
There are two broad groups of arguments: general arguments common to both tools and tool-specific arguments.

###General arguments


Argument                            |Description
------------------------------------|--------------------------------------------------------------
-enablelogging                      |Enables lilac logging. NOTE: this argument is for debugging purposes only. The regular output, like assembler error messages wouldn't be affected.
-loglevel <loglevel>                |Sets the log level if the logging has been enabled has otherwise no effect. There are four possible log levels: **error**,**warn**,**info**,**debug**. Default is **info**.
-logfile  <logfile>                 |Specifies a log file to log into. If the logging is disabled this argument has no effect. If no log file has been specified and the logging is enabled log messages will be written to the standard output.
-input  <sources>                   |Input sources for the tool in which the tool searches for it's input files, that is, class files for the disassembler and assembler files (files with the .jasm-suffix) for the assembler. The allowed sources are directories, zip files and jar files. If a directory itself contains zip or jar files, the tool looks into these as well.
-output <directory>                 |The output directory in which the tool's output will be created (class files for the assembler, assembler files for the disassembler)
-D propertyname=propertyvalue       |With this argument you can set a system property, which influences the output and/or behavior of the tool. A list of available properties can be found [here](sysprops.html).   

##Assembler-specific arguments

Argument                            |Description
------------------------------------|--------------------------------------------------------------
-classpath <sources>                | Places where the assembler will search for external classes. Allowed sources are directories, zip files and jar files. **NOTE:** different from the Java compiler, the assembler classpath entries have to be separated by spaces, not by colons. 