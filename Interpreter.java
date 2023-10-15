import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Stack;


public class Interpreter {
    // HashMap of declared variables format <name, value>.
    static HashMap<String, Integer> variables = new HashMap<String, Integer>();
    // ArrayList to read target file into.
    static ArrayList<String> fileStringArr = new ArrayList<String>();
    // Stacks to store the line index and variable being used in the condition of the current while loop.
    static Stack<Integer> loopLineIdx = new Stack<Integer>();
    static Stack<String> loopVarName = new Stack<String>();
    static Integer currentLineIdx = 0;

    // Reads target file into memory; each line becomes a string.
    static void parseFile(String fileName) {
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                // Leading whitespace is removed.
                fileStringArr.add(nextLine.stripLeading());
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Executes a line of code.
    static void processLine(String line) {
        String[] words = line.split("[;: ]");
        String command = words[0];

        if (command.equals("clear")) {
            declareVar(words[1]);
        } else if (command.equals("incr")) {
            incrVar(words[1]);
        } else if (command.equals("decr")) {
            decrVar(words[1]);
        // When a loop is reached, the line number and variable being compared to 0 are pushed onto respective stacks.
        } else if (command.equals("while")) {
            loopLineIdx.push(currentLineIdx);
            loopVarName.push(words[1]);
        } else if (command.equals("end")) {
            // If the condition of the loop is true, end the loop by removing it from the stacks. Continue.
            if (variables.get(loopVarName.peek()) == 0) {
                loopLineIdx.pop();
                loopVarName.pop();
            // If the condition is false, go to the saved line number.
            } else {
                currentLineIdx = loopLineIdx.peek();
            }
        } else {
            System.out.println("Command not found.");
        }

        // Output the value of each variable after each line is executed.
        StringBuffer variableValues = new StringBuffer();
        for (String i : variables.keySet()) {
            variableValues.append(i + " = " + variables.get(i) + ", ");
        }
        System.out.println(variableValues);
    }


    // Function to declare a variable.
    static void declareVar(String name) {
        variables.put(name, 0);
    }


    // Function to increment a variable.
    static void incrVar(String name) {
        variables.put(name, variables.get(name) + 1);
    }


    // Function to decrement a variable.
    static void decrVar(String name) {
        variables.put(name, variables.get(name) - 1);
    }

    // Main loop; executes the line referenced by currentLineIdx until the end of the file is reached.
    public static void main(String[] args) {
        parseFile(args[0]);
        while (currentLineIdx < fileStringArr.size()) {
            processLine(fileStringArr.get(currentLineIdx));
            currentLineIdx++;
        }
    }
}

