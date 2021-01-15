package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 *
    	 **/
    	expr = expr.replaceAll(" ","");
    	expr = expr + " ";
    	String c = "";
    	for(int i = 0; i < expr.length(); i ++) {
    		 c = expr.substring(i,i+1);
    		if(c.matches("^[a-zA-Z]*$")) {
    			//System.out.println("got here"); //debugging statement
    			String name = c;
    			for(i=i + 1; i < expr.length()-1;i++) {
    				c = expr.substring(i,i+1);
    				if(!c.matches("^[a-zA-Z]*$") || c.equals(null) == true) {
    					break;
    				}
    				name = name + c;
    			}
    			if(c.equals("[")) {
    				if(!isArrayin(name,arrays)) {
    					arrays.add(new Array(name));
    				}
    			}
    			else {
    				if(!isVariablein(name,vars)) {
    					vars.add(new Variable(name));
    				}
    			}
    		}
    	}
	   /* System.out.println("These are variables");
	        for(int i = 0; i < vars.size(); i++) {
				System.out.println(vars.get(i));
				
			} 
	        System.out.println("These are arrays");
			for(int i = 0; i < arrays.size(); i++) {
				System.out.println(arrays.get(i));
				
			} */   //debugging code to check if arrays and vars is good. Comment out later
		  }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float 
    evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	// following line just a placeholder for compilation
    	expr = expr.replaceAll(" ", ""); // space removal
    	expr = expr + ""; // Possibly unnecessary way to avoid null pointer error
    	String c = ""; //current position holder -  cannot use in helper methods, has to stay in this function
    	Stack<String> ops = new Stack<String>(); // stack for operators
    	Stack<Float> nums = new Stack<Float>(); // stack for numbers
    	//System.out.println(expr); //debugging code
    	// begin traveling by substring
    	for(int i = 0; i < expr.length();i++) {
    		c = expr.substring(i,i+1);
    		
    		if(c.matches("^[0-9]*$")) {
    			String num = "";
    			for(i = i; i < expr.length(); i++) {
    				c = expr.substring(i,i+1);
    				if(!c.matches("^[0-9]*$") || c.equals(null) == true) {
    					break;
    				}
    				num = num + c;
    			}
    			i = i - 1; //return to next char in the chain
    			nums.push(Float.parseFloat(num));
    			//System.out.println(num); // debugging code
    		}
    		else if(c.equals("(")) { //start subexpresison
    			String subexpr = "";
    			c = expr.substring(i+1, i + 2); //move forward one to build subexpr
    			int opcount = 1;
    			int clcount = 0;
    			for(i = i + 1; i < expr.length(); i++) { //miniature version of the entire program until the right parenthesis is reached
    				c = expr.substring(i,i+1);
    				if(c.equals(")")) {
    					clcount = clcount + 1;
    				}
    				else if(c.equals("(")) {
    					opcount = opcount + 1;
    				}
    				if(opcount != clcount) {
    					subexpr = subexpr + c;
    					//System.out.println(subexpr);
    				}
    				else {
    					break; //leave when the final closing parenthesis is found
    				}
    			}
    			//System.out.println("keeps crashing here"); //debugging code
    			//System.out.println("evaluate 2 is starting"); //debugging code
    			float res = evaluate(subexpr, vars, arrays); // recursion of smaller problem
    			//System.out.println(res); //debugging code
    			nums.push(res);
    		}
    		else if(c.matches("^[-+*/]$")) {   //when symbol matches then carry operations out in order to that point
    			while(ops.isEmpty() == false) {
    				if(priority(c, ops.peek()) == true) {
    					//System.out.println("priority true was read"); //debugging code
    					break;
    				}
    				else {
    					//System.out.println("Priority false was read"); //debugging code
    					float n1 = nums.pop(); //num should be already present
    					float n2 = nums.pop(); //num should be already present
    					String tempop = ops.pop();
    					float res = solve(tempop, n1, n2);
    					nums.push(res);
    				}
    			}
    			//System.out.println("operator going in: " + c); //debugging code
    			ops.push(c); //operator should be pushed no matter what.
    		}
    		else if(c.matches("^[a-zA-Z]*$")) {  //almost same procedure for parenthesis for vars and arrays
    			String name = "";
    			for(i = i; i < expr.length(); i++) {
    				c = expr.substring(i,i+1);
    				if(!c.matches("^[a-zA-Z]*$") || c.equals(null) == true) {
    					break;
    				}
    				name = name + c;
    				//System.out.println(name); //debugging code
    			} 
    			if(c.equals("[")) { //this part works like parenthesis with recurison
    				String subexpr = "";
        			c = expr.substring(i+1, i + 2); //move forward one to build subexpr
        			int opcount = 1;
        			int clcount = 0;
        			for(i = i + 1; i < expr.length(); i++) { //miniature version of the entire program until the right bracket is reached
        				c = expr.substring(i,i+1);
        				if(c.equals("]")) {
        					clcount = clcount + 1;
        				}
        				else if(c.equals("[")) {
        					opcount = opcount + 1;
        				}
        				if(opcount != clcount) {
        					subexpr = subexpr + c;
        					//System.out.println(subexpr); //debugging code
        				}
        				else {
        					break; //leave when the final closing bracket is found
        				}
        			}
        			for(int x = 0; x < arrays.size(); x++) {
        				if(arrays.get(x).name.equals(name)) {
        					float arrval = (float)arrays.get(x).values[(int)evaluate(subexpr,vars,arrays)];
        					//System.out.println(arrval); //debugging code
        					nums.push(arrval);
        				}
        			}
    			}
    			else { //if not array has to be variable
    				//System.out.println(vars.toString()); //debugging code
    			for(int x = 0; x < vars.size(); x++) {
    				if(vars.get(x).name.equals(name)) {
    					float varval = vars.get(x).value;
    					//System.out.println(varval); //debugging code
    					nums.push(varval);
    					break;
    				}
    			}
    			i = i - 1;
    			}
    		}
    	} //by the end rest of operations should be carried out for remaining operators. nums should have 1 element left, and that is answer
    	
    	while(ops.isEmpty() == false) {
    		float n1 = nums.pop();
    		//System.out.println("n1 is: " + n1); //debugging code
    		float n2 = nums.pop();
    		//System.out.println("n2 is: " + n2); //debugging code
    		String tempop = ops.pop();
    		float res = solve(tempop,n1,n2); //can make helper method out of this to simplify
    		nums.push(res);
    	}
    	return nums.pop(); //this should be the answer
    }
    
    
    
    
    
    
    
    //helper methods
    
    //checks if array is in the arraylist already
    private static boolean isArrayin(String expr, ArrayList<Array> arrays) {
    	for(Array array: arrays) {
    		if(array.name.equals(expr)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    
    
    
    
    
    
    
    //checks if variable is in the arraylist alreaady
    private static boolean isVariablein(String expr, ArrayList<Variable> vars) {
    	for(Variable var: vars) {
    		if (var.name.equals(expr)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    
    
    
    
    //priority checking method for the operator match case op1 is the current op and op2 is the top op in the stack
    private static boolean priority(String op1, String op2) {
    	if(op1.equals("+") || op1.equals("-")) {
    		if(op2.equals("*") || op2.equals("/") || op2.equals("+") || op2.equals("-")) {
    			return false;
    		}
    	}
    	else if (op1.equals("/") || op1.equals("*")) {
    		if(op2.equals("/") || op2.equals("*")) {
    			return false;
    		}
    	}
    	return true;
    }
    
    
    
    
    
    
    
    
    // helper method to solve the given simple expression. Remember to do reverse order of input
    private static float solve(String operator, float n1, float n2) {
    	if(operator.equals("+")) {
    		return (n2 + n1);
    	}
    	if(operator.equals("-")) {
    		return (n2 - n1);
    	}
    	if(operator.equals("*")) {
    		return (n2 * n1);
    	}
    	if(operator.equals("/")) {
    		return (n2 / n1);
    	}
    	return 0;
    }
}
