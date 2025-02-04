package com.garrett;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import javax.naming.OperationNotSupportedException;

public class SimpleVM
{   
	public List<String> list;
	public List<Operation> operations;
	public SymbolTable symbols;
	public Stack<Integer> stack;
	private int programCount;
	
    /**
     * Creates a SimpleVM with the program contained in 
     * the supplied Scanner.
     * 
     * @param scanner the Scanner containing the program
     * @throws OperationNotSupportedException 
     */
    public SimpleVM(Scanner scanner) throws IOException, OperationNotSupportedException
    {
    	// Initialize variables
    	list = new ArrayList<String>();
    	operations = new ArrayList<Operation>();
    	symbols = new SymbolTable();
    	stack = new Stack<Integer>();
    	programCount = 0;
    	
        // Convert the text program into a list of Operation objects
    	convertTextToList(scanner);
    }

    /**
     * Runs the loaded program.
     * @throws OperationNotSupportedException 
     */
    public void run() throws OperationNotSupportedException
    {
        //  convert list items to operation objects and execute each operation
    	convertListToOperations(list);
    	
    	/* for every operation object, invoke it's execute method
    	for (Operation o : operations) {
    		programCount += o.execute(0, stack, symbols);
    	}*/
    }
    
    /**
     * Returns the value of the given variable.
     * 
     * @param name the variable name
     * @return the value
     */
    public int getValue(String name)
    {
        //  Looks things up in the symbol table
    	return this.symbols.getValue(name);
    }
    
    /**
     * Adds each line command of the program passed in to a list.
     * @param scanner the program containing lines of different commands.
     * @return the size of the list containing all the commands.
     * @author Joshua S. Garrett
     */
    public void convertTextToList(Scanner scanner) {
    	while (scanner.hasNextLine()) {
    		list.add(scanner.nextLine());
    	}
    }
    
    /**
     * Converts each list element to its corresponding operation type.
     * @author Joshua S. Garrett
     * @throws OperationNotSupportedException 
     */
    public void convertListToOperations(List<String> list) throws OperationNotSupportedException {
    	// for each element in list
    		// get list item
    		// get command
    		// get the value (string)
    		// 		new PushOperation(value);
    	String cmd = "";
    	String value = "";
    	for (int i = 0; i < list.size(); i++) {
    		// get current listItem
    		String currentItem = list.get(i);
    		// get command
    		cmd = getItemCommand(currentItem);
    		if (cmd.equalsIgnoreCase("push") && hasValue(currentItem)) {
    			// get value
    			value = getItemValue(currentItem);
    			char c = value.charAt(0);
    			if (Character.isDigit(c)) {
    				System.out.println("The value " + value + " is numeric.");
    				// push constant on top of stack
    				Operation operation = new PushOperation(Integer.parseInt(value));
    				operation.execute(this.programCount, stack, symbols);
        			operations.add(operation);
    			} else {
    				// value is not numeric, and is a variable name.
    				System.out.println("The value " + value + " is not numeric.");
    				// check if variable is in symbols table
    				if (symbols.hasSymbol(value) != -1) {
    					Operation operation = new PushOperation(symbols.getValue(value));
    					operation.execute(this.programCount, stack, symbols);
        				operations.add(operation);
    				} else {
    					// symbol doesn't exist
    					throw new RuntimeException("The variable is not defined in the table.");    				
    				}
    			}
    		} else if (cmd.equalsIgnoreCase("pop") && hasValue(currentItem)) {
    			// get the full string following the 'pop' command
    			value = getItemValue(currentItem);
    			// create new pop operation and execute
    			Operation operation = new PopOperation(value);
    			operation.execute(this.programCount, stack, symbols);
    			operations.add(operation);
    		} else if (cmd.equalsIgnoreCase("add")) {
    			System.out.println(cmd);
    		} else {
    			throw new OperationNotSupportedException("That operation is not supported.");
    		}
    	}
    }
    
    /**
     * Prints each element of the list to console.
     * @author Joshua S. Garrett
     */
    @Override
    public String toString() {
    	StringBuilder str = new StringBuilder();
    	str.append("[");
    	for (int i = 0; i < list.size(); i++) {
    		if (i == list.size() - 1) {
    			str.append(list.get(i));
    			break;
    		}
    		str.append(list.get(i));
    		str.append(", ");
    	}
    	str.append("]");
		return str.toString();
    }
    
    /**
     * Utility method: Returns the command of a line item.
     * @author Joshua S. Garrett
     */
    public String getItemCommand(String lineItem) {
    	// split the lineItem into two parts, the first part is the command
    	String[] str = lineItem.split("\s");
    	return str[0];
    }
    
    /**
     * Utility method: Returns the value of a line item, if it has one.
     * @author Joshua S. Garrett
     */
    public String getItemValue(String lineItem) {
    	// if line item has value
    	// return value
    	// else return ""
    	if (hasValue(lineItem)) {
    		String[] str = lineItem.split("\s");
    		return str[1];
    	}
    	return "";
    }
    
    /**
     * Utility method: Determines if a line item has a value or not.
     * @author Joshua S. Garrett
     */
    public boolean hasValue(String lineItem) {
    	return lineItem.contains(" ") ? true : false;
    }
    
    /**
     * Returns the current program count.
     * @return the current count
     */
    public int getProgramCount() {
    	return this.programCount;
    }
}
