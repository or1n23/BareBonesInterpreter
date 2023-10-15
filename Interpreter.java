import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Stack;


public class Interpreter {
    // HashMap of variables in target file in format <name, value>.
    static HashMap<String, Integer> variables = new HashMap<String, Integer>();
    // ArrayList to store every line in target file.
    static ArrayList<String> fileStringArr = new ArrayList<String>();
    // Stacks to store the line index and variable being used in the condition of the current while loop.
    static Stack<Integer> loopLineIdx = new Stack<Integer>();
    static Stack<String> loopVarName = new Stack<String>();
    static Integer currentLineIdx = 0;

    // Reads target file into memory; each line becomes a string.
    static void ParseFile(String fileName) {
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                // Leading whitespace is removed.
                nextLine.stripLeading();
                fileStringArr.add(nextLine.stripLeading());
            }
    
            scanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Executes each line.
    static void ProcessLine(String line) {
        String[] words = line.split("[;: ]");
        String command = words[0];

        if (command.equals("clear")) {
            DeclareVar(words[1]);
        } else if (command.equals("incr")) {
            IncrVar(words[1]);
        } else if (command.equals("decr")) {
            DecrVar(words[1]);
        } else if (command.equals("while")) {
            loopLineIdx.push(currentLineIdx);
            loopVarName.push(words[1]);
        } else if (command.equals("end")) {
            if (variables.get(loopVarName.peek()) == 0) {
                loopLineIdx.pop();
                loopVarName.pop();
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
    static void DeclareVar(String name) {
        variables.put(name, 0);
    }


    // Function to increment a variable.
    static void IncrVar(String name) {
        variables.put(name, variables.get(name) + 1);
    }


    // Function to decrement a variable.
    static void DecrVar(String name) {
        variables.put(name, variables.get(name) - 1);
    }

    // Main loop; executes the line referenced by currentLineIdx until the end of the file is reached.
    public static void main(String[] args) {
        ParseFile(args[0]);
        while (currentLineIdx < fileStringArr.size()) {
            ProcessLine(fileStringArr.get(currentLineIdx));
            currentLineIdx++;
        }
    }
}

