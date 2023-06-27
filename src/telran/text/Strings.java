package telran.text;

import java.lang.module.ModuleDescriptor.Builder;

public class Strings {

	public static String javaVariableName() {
		return "[a-zA-Z$][\\w$]*|_[\\w$]+";
	}
	
	public static String zero_300() {
		return "[1-9]\\d?|[1-2]\\d\\d|300|0";
	}

	public static String ipV4Octet() {
		return "\\d\\d?|[0-1]\\d\\d|[0-2][0-4]\\d|[0-2][0-5][0-5]";
	}
	
	public  static String ipV4() {
        String str = "jhj";
		String regOctet = ipV4Octet(); 
		return "((" + regOctet + ").){3}(" + regOctet + ")";
	}
}
  