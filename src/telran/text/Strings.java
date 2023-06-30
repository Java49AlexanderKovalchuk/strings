package telran.text;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.function.BinaryOperator;

public class Strings {
	
	static private String regexDouble = "(\\d+(\\.\\d+)?)";
	
	static HashMap<String, BinaryOperator<Double>> mapOperations;
	static {
		mapOperations = new HashMap<>();
		mapOperations.put("-", (a, b) -> a - b);
		mapOperations.put("+", (a, b) -> a + b);
		mapOperations.put("*", (a, b) -> a * b);
		mapOperations.put("/", (a, b) -> a / b);
	}	
	static HashMap<String, Double> mapVariables;

	public static String javaVariableName() {
		return "([a-zA-Z$][\\w$]*|_[\\w$]+)";
	}
	
	public static String zero_300() {
		return "[1-9]\\d?|[1-2]\\d\\d|300|0";
	}

	public static String ipV4Octet() {
		return "([01]?\\d\\d?|2([0-4]\\d|5[0-5]))";
	}
	
	public  static String ipV4() {
       	String octetRegex = ipV4Octet(); 
		return String.format("(%1$s\\.){3}%1$s", octetRegex);
	}
	
	public  static String arithmeticExpression() {
		String operandRE = operand();
		String operatorRE = operator();
		return String.format("%1$s(%2$s%1$s)*", operandRE, operatorRE);
		
	}
	public static String operator() {
		return "\\s*([-+*/])\\s*";
	}
	
	public static String operand() {
		
		String regexVariable = javaVariableName();
		return String.format("(%1$s|%2$s)", regexDouble, regexVariable);
	}
	
	public static boolean isArithmeticExpression(String expression) {
		expression = expression.trim();
		return expression.matches(arithmeticExpression());
	}
	public static double computeExpression (String expression) {
		
		if(!isArithmeticExpression(expression)) {
			throw new IllegalArgumentException("Wrong arithmetic expression");
		}
		expression = expression.replaceAll("\\s+", "");
		String[] operands = expression.split(operator());
		String[] operators = expression.split(operand());
		double res = Double.parseDouble(operands[0]); 
		for(int i = 1; i < operands.length; i++) {
			double operand = Double.parseDouble(operands[i]);
			res = mapOperations.get(operators[i]).apply(res, operand);
		}
		
		return res;
	}
	
	//update whole code for any numbers (double)  
	//update code taking into consideration possible variables name
	public static double computeExpression(String expression, 
			HashMap<String, Double> mapVariables) {
		if(!isArithmeticExpression(expression)) {
			throw new IllegalArgumentException("Wrong arithmetic expression");
		}
		
		expression = expression.replaceAll("\\s+", "");
		
		String[] operands = expression.split(operator());
		String[] operators = expression.split(operand());
		double res = operands[0].matches(regexDouble) ? Double.parseDouble(operands[0]) :
			mapVariables.get(operands[0]);
		for(int i = 1; i < operands.length; i++) {
			double operand;
			if(operands[i].matches(regexDouble)) {
				operand = Double.parseDouble(operands[i]);
			} else {
				if(mapVariables.get(operands[i]) == null) {
					throw new NoSuchElementException();
				} else {
				operand = mapVariables.get(operands[i]);
				}
			}
			res = mapOperations.get(operators[i]).apply(res, operand);
		}
		return res;
	}
} 
