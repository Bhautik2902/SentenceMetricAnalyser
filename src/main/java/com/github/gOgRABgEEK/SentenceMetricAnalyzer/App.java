package com.github.gOgRABgEEK.SentenceMetricAnalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class App 
{
    public static void main( String[] args )
    {
        Scanner scn = new Scanner(System.in);      
        
        while (true) {
        	System.out.println();
        	System.out.print("Sentence Metric Analyzer\n$ ");
        	
            String cmd = scn.nextLine();
            
            // Quit on exit command
            if (cmd.contains("Exit") || cmd.contains("exit")) {            	
            	System.out.println("Quitting");
            	break;
            }
            
            // fetch arguments from the command
            CmdArguments cmdArgs = processCommand(cmd);
            
            if (cmdArgs != null) {
            	boolean isOkay = processArgs(cmdArgs);
            	if (isOkay) {
            		findAvgLength(cmdArgs);
            	}	
            }
        }       
    }
    
    private static void findAvgLength(CmdArguments cmdArgs) {
    	// read files from file path.
    	String filePath = cmdArgs.file_path;
    	filePath = filePath.replace('/', File.pathSeparatorChar);
    	
        int customBufferSize = 16384; // 16 KB
        List<Character> delimiters = cmdArgs.deli_list;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath), customBufferSize)) {
            int charInSen = 0;
            int total_sentence = 0;
            double avgLengthSum = 0;
            
            boolean wordTrueCondition = (cmdArgs.word_len != Integer.MIN_VALUE && !(cmdArgs.word_len < 3));
                   
            char ch;           
            while ((ch = (char) reader.read()) != (char)-1) {
                // Process each line as needed 	
                boolean isAlphaNum = (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9' || ch == '\'');
            	HashSet<Character> hs = new HashSet<>();
            	if (wordTrueCondition && cmdArgs.deli_list.size() != 0) {  // both true         		
            		hs.addAll(cmdArgs.deli_list);
            			                    
            		if (hs.contains(ch)) {  // char is delimiter.
            			total_sentence++;
            			avgLengthSum += (charInSen/cmdArgs.word_len);
            			charInSen = 0;
            			continue;
            		}
            		
            		if (isAlphaNum) {   // char is part of the word
            			charInSen++;
            			continue;
            		}
	            }
            	else if (wordTrueCondition && cmdArgs.deli_list.size() == 0)  {  // true & false
            		hs.add('.'); hs.add('?'); hs.add('!');   // default delimiters
            		
            		if (hs.contains(ch)) {  // char is delimiter.
            			total_sentence++;
            			avgLengthSum += (charInSen/cmdArgs.word_len);
            			charInSen = 0;
            			continue;
            		}
            		
            		if (isAlphaNum) {   // char is part of the word
            			charInSen++;
            			continue;
            		}
            		
            	}
            	else if (!wordTrueCondition && cmdArgs.deli_list.size() != 0) {   // false & true
            		hs.addAll(cmdArgs.deli_list);
            		
            		if (hs.contains(ch)) {  // char is delimiter.
            			total_sentence++;
            			avgLengthSum += (charInSen/3);  // default word length
            			charInSen = 0;
            			continue;
            		}
            		
            		if (isAlphaNum) {   // char is part of the word
            			charInSen++;
            			continue;
            		}
            	}
            	else if (!wordTrueCondition && cmdArgs.deli_list.size() == 0) {   // false & false
            		hs.add('.'); hs.add('?'); hs.add('!');   // default delimiters
            		if (hs.contains(ch)) {  // char is delimiter.
            			total_sentence++;
            			avgLengthSum += (charInSen/3);  // default word length
            			charInSen = 0;
            			continue;
            		}
            		
            		if (isAlphaNum) {   // char is part of the word
            			charInSen++;
            			continue;
            		}

            	}      
            }
            if (total_sentence == 0) {
            	System.out.println("The file doesn't have any provided delimiters");
            	return;
            }
            String roundedVal = String.format("%.2f", avgLengthSum/total_sentence);
            System.out.println("Average length of sentence is: " + roundedVal);
            
        } 
        catch (FileNotFoundException e) {
            System.out.println("The specified file could not be found.");
        }
        catch (IOException ex) {
        	System.out.println("Error: " + ex.getMessage());
        }
    	
	}

	private static boolean processArgs(CmdArguments cmdArgs) {
		
		// file name
        String fname_rx = "([a-zA-Z]:)?(\\\\[a-zA-Z0-9._-]+)+\\\\?";

        if (cmdArgs.file_path != null && !Pattern.matches(fname_rx, cmdArgs.file_path)) {
        	System.out.println("File path is not valid.");
        	return false;
        }
		// word length
		
		// delimiters; verify if those are valid delimiters and not alphanumeric character
        for (char ch : cmdArgs.deli_list) {    	
        	boolean isValidDelimiter = !(ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9' || ch == '\'');
        	if (!isValidDelimiter) {
        		System.out.println("Invalid delimiter found");
        		return false;
        	}
        }
		return true;
	}

	private static CmdArguments processCommand(String cmd) {
    	String[] args = cmd.split("\\s+");
    	CmdArguments cmdAttrb = new CmdArguments();
    	
    	for (int i=0; i<args.length; i++) {
    		
    		if (i == 0 && !args[i].equals("sma")) {
    			System.out.println(args[i] + ": Command not found.");
    			return null;
    		}
    		else if (i == 0 && args[i].equals("sma")) {
    			continue;
    		}
    		
    		switch (args[i]) {
	            case "-f":
	                if (i + 1 < args.length) {
	                	cmdAttrb.file_path = args[i + 1];
	                    i++; // Skip the next argument since it's the file path
	                } else {
	                    System.out.println("-f flag requires a file argument.");
	                    return null;
	                }
	                break;
	            case "-w":
	                if (i + 1 < args.length) {
	                	try {
	                		cmdAttrb.word_len = Byte.parseByte(args[i+1]);
		                    i++; // Skip the next argument since it's the word
	                	}
	                	catch (NumberFormatException ex) {
	                		System.out.println("Real number is required for word length");
	                		return null;
	                	}
	                	
	                } else {
	                	System.out.println("-w flag requires a word length argument.");
	                    return null;
	                }
	                break;
	            case "-d":
	                if (i + 1 < args.length) {
	                    for (char ch :  args[i+1].toCharArray()) {
	                    	cmdAttrb.deli_list.add(ch);
	                    }
	                    i++; // Skip the next argument since it's the directory path
	                } else {
	                	System.out.println("-d flag requires a directory argument.");
	                    return null;
	                }
	                break;
	            default:
	                // Handle other arguments or display an error message
	            	System.out.println("Unknown flag or argument: " + args[i]);
	                return null;
    		}   		
    	}
    	return cmdAttrb;
    }
    
}

class CmdArguments {
	byte word_len;
	String file_path;
	List<Character> deli_list = new ArrayList<Character>();
	
	public CmdArguments(byte len, String path, List<Character> list) {
		this.word_len = len;
		this.file_path = path;
		this.deli_list = list;
	}
	
	public CmdArguments() {
		
	}	
}