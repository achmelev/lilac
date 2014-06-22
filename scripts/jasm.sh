#!/bin/bash

if [ -z "$JASM_JAVA_HOME" ]; then
	echo "Please set the variable JASM_JAVA_HOME to the java runtime's directory. You need at least JRE 1.7."
else 
	JASM_JAVA_CMD=$JASM_JAVA_HOME/bin/java
	if [ -f $JASM_JAVA_CMD ]; then 
		JASM_INSTALL_DIR=`dirname "$0"`
		$JASM_JAVA_CMD -jar $JASM_INSTALL_DIR/jasm.jar $*
	else 
		echo "Couldn't find $JASM_JAVA_CMD! Please adjust JASM_JAVA_HOME to the correct value."
	fi
fi


