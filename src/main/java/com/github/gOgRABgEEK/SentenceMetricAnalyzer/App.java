package com.github.gOgRABgEEK.SentenceMetricAnalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class App 
{
    public static void main( String[] args )
    {
        Scanner scn = new Scanner(System.in);      
        
        while (true) {
        	System.out.println();
        	System.out.println("Sentence Metric Analyzer\n$sma ");
        	
            String cmd = scn.nextLine();
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
            String line;
            int charInSen = 0;
            int total_sentence = 0;
            double avgLengthSum = 0;
            
            boolean wordTrueCondition = (cmdArgs.word_len != Integer.MIN_VALUE && !(cmdArgs.word_len < 3));
            
           
            int ch;           
            while ((ch = reader.read()) != -1) {
                // Process each line as needed 	
                boolean isAlphaNum = (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9' || ch == '\'');
            	HashSet<Character> hs = new HashSet<>();
            	if (wordTrueCondition && cmdArgs.deli_list != null) {  // both true         		
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
            	else if (wordTrueCondition && cmdArgs.deli_list == null)  {  // true & false
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
            	else if (!wordTrueCondition && cmdArgs.deli_list != null) {   // false & true
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
            	else if (!wordTrueCondition && cmdArgs.deli_list == null) {   // false & false
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
	}

	private static boolean processArgs(CmdArguments cmdArgs) {
		// TODO Auto-generated method stub
		return false;
	}

	private static CmdArguments processCommand(String cmd) {
    	String[] args = cmd.split("\\s+");
    	CmdArguments cmdAttrb = new CmdArguments();
    	
    	String pre_flag = "";
    	for (int i=0; i<args.length; i++) {
    		if (i == 0 && !args[i].equals("sma")) {
    			System.out.println(args[i] + ": Command not found.");
    			return null;
    		}
    		
    		// flag verifier 
    		if (i == 1 && (args[1].equals("-f") || args[1].equals("-w") || args[1].equals("-d"))) {
    			pre_flag = args[1];
    		}
    		else if (i == 1 && !(args[1].equals("-f") || args[1].equals("-w") || args[1].equals("-d"))) {
    			
    		}
    		
//    		switch (args[i]) {
//	            case "-f":
//	                flagF = true;
//	                if (i + 1 < args.length) {
//	                    fileArg = args[i + 1];
//	                    i++; // Skip the next argument since it's the file path
//	                } else {
//	                    System.out.println("-f flag requires a file argument.");
//	                    return null;
//	                }
//	                break;
//	            case "-w":
//	                flagW = true;
//	                if (i + 1 < args.length) {
//	                    wordArg = args[i + 1];
//	                    i++; // Skip the next argument since it's the word
//	                } else {
//	                    displayError("-w flag requires a word argument.");
//	                    return;
//	                }
//	                break;
//	            case "-d":
//	                flagD = true;
//	                if (i + 1 < args.length) {
//	                    directoryArg = args[i + 1];
//	                    i++; // Skip the next argument since it's the directory path
//	                } else {
//	                    displayError("-d flag requires a directory argument.");
//	                    return;
//	                }
//	                break;
//	            default:
//	                // Handle other arguments or display an error message
//	                displayError("Unknown flag or argument: " + args[i]);
//	                return;
//    		}
    		
    	}

    }
    
}

class CmdArguments {
	byte word_len;
	String file_path;
	List<Character> deli_list;
	
	public CmdArguments(byte len, String path, List<Character> list) {
		this.word_len = len;
		this.file_path = path;
		this.deli_list = list;
	}
	
	public CmdArguments() {
		
	}	
}