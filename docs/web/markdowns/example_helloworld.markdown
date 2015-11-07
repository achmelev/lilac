[Raw code](codeexamples/helloworld.jasm)
	
	:::lilac
	public super class {
	  version 52.0;
	  name ThisClass; 
	  extends Object; 
  
	   //Constants
   
	  const classref ThisClass ThisClass_name;
	  const utf8 ThisClass_name "org/jasm/examples/HelloWorld";
  
	  const classref Object Object_name;
	  const utf8 Object_name "java/lang/Object";
  
	  const classref System System_name;
	  const utf8 System_name "java/lang/System";
  
	  const utf8 out_name "out";
	  const utf8 out_desc "Ljava/io/PrintStream;";
	  const nameandtype System.out_nat out_name,out_desc;
	  const fieldref System.out System,System.out_nat;
  
	  const classref PrintStream PrintStream_name;
	  const utf8 PrintStream_name "java/io/PrintStream";
  
	  const utf8 println_name "println";
	  const utf8 println_desc "(Ljava/lang/String;)V";
	  const nameandtype PrintStream.println_nat println_name,println_desc;
	  const methodref PrintStream.println PrintStream,PrintStream.println_nat;
  
	  const utf8 init0_name "<init>";
	  const utf8 init0_desc "()V";
	  const methodref Object.init0 Object, Object.init0_nat; 
	  const nameandtype Object.init0_nat init0_name, init0_desc;
  
	  //This has to be declared if you have code
	  const utf8 Code_utf8 "Code";
	
	  const utf8 main_name "main";
	  const utf8 main_desc "([Ljava/lang/String;)V";
  
	  const utf8 helloworld_content "Hello World!";
	  const string helloworld helloworld_content;
  

	  //Methods
	
	  //Constructor
	  public method {
		name init0_name; 
		descriptor init0_desc; 
	
		//Variables
	
		var object this;
	
		//Instructions
	
		aload this;
		invokespecial Object.init0;
		return;
	  }
 
	  //Main Method
	  public static method {
		name main_name;
		descriptor main_desc; 
	
		//Variables
	
		var object args; //This is the method parameter
	
		//Instructions
	
		getstatic System.out;
		ldc helloworld;
		invokevirtual PrintStream.println;
	
		return;
	  }
	}