package com.github.gOgRABgEEK.SentenceMetricAnalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class App 
{
	static Scanner scn = new Scanner(System.in);
	static List<Character> defaultDelis = new ArrayList<>();

    public static void main( String[] args )
    {
		// adding default delimiters to list.
		defaultDelis.add('.');
		defaultDelis.add('?');
		defaultDelis.add('!');
                    
        while (true) {
        	System.out.print("\nSentence Metric Analyzer\n$ ");
        	
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
		scn.close();     
    }
    
    private static void findAvgLength(CmdArguments cmdArgs) {
    	// read files from file path.
    	
    	Path filePath = Paths.get(cmdArgs.file_path);
    	
        int customBufferSize = 16384; // 16 KB

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toString()), customBufferSize)) {
            int charInSen = 0;
            int total_sentence = 0;
            double avgLengthSum = 0;
            
            boolean wordTrueCondition = (cmdArgs.word_len != Byte.MIN_VALUE && !(cmdArgs.word_len < 3));
            HashSet<Character> hs = new HashSet<>();

			// for -a flag
			if (cmdArgs.actualWord) {
				if (wordTrueCondition) {
					printOnConsole("Usage", "Two options found for word size choice. Choose any one.");
					return;
				}
				List<Character> currList;
				if (cmdArgs.deli_list.size() != 0) { 
					currList = cmdArgs.deli_list;
				}
				else {
					currList = defaultDelis;
				}

				int spaceCount = 0; 
				char ch;
				while ((ch = (char) reader.read()) != (char)-1) { 
					hs.addAll(currList);
										
					if (hs.contains(ch)) {  // char is delimiter.
						total_sentence++;
						avgLengthSum += (spaceCount+1);  // no. of words will be total spaces in sentence + 1.
						spaceCount = 0;
						continue;
					}
					
					if (ch == ' ') {   // char is part of the word
						spaceCount++;
						continue;
					}
				}
			}

            // for -w and -d flag
			else if (wordTrueCondition && cmdArgs.deli_list.size() != 0) {  // both true         		
				char ch;           
				while ((ch = (char) reader.read()) != (char)-1) { 
					hs.addAll(cmdArgs.deli_list);
										
					if (hs.contains(ch)) {  // char is delimiter.
						total_sentence++;
						avgLengthSum += (charInSen/cmdArgs.word_len);
						charInSen = 0;
						continue;
					}
					
					if (isCharAlphaNum(ch)) {   // char is part of the word
						charInSen++;
						continue;
					}
				}
			}
			else if (wordTrueCondition && cmdArgs.deli_list.size() == 0)  {  // true & false
				char ch;           
				while ((ch = (char) reader.read()) != (char)-1) { 
					hs.addAll(defaultDelis);   // default delimiters
				
					if (hs.contains(ch)) {  // char is delimiter.
						total_sentence++;
						avgLengthSum += (charInSen/cmdArgs.word_len);
						charInSen = 0;
						continue;
					}
					
					if (isCharAlphaNum(ch)) {   // char is part of the word
						charInSen++;
						continue;
					}
				}
			}
			else if (!wordTrueCondition && cmdArgs.deli_list.size() != 0) {   // false & true
				char ch;           
				while ((ch = (char) reader.read()) != (char)-1) { 
					hs.addAll(cmdArgs.deli_list);
				
					if (hs.contains(ch)) {  // char is delimiter.
						total_sentence++;
						avgLengthSum += (charInSen/3);  // default word length
						charInSen = 0;
						continue;
					}
					
					if (isCharAlphaNum(ch)) {   // char is part of the word
						charInSen++;
						continue;
					}
				}
			}
			else if (!wordTrueCondition && cmdArgs.deli_list.size() == 0) {   // false & false
				char ch;           
				while ((ch = (char) reader.read()) != (char)-1) { 
					hs.addAll(defaultDelis);   // default delimiters
					if (hs.contains(ch)) {  // char is delimiter.
						total_sentence++;
						avgLengthSum += (charInSen/3);  // default word length
						charInSen = 0;
						continue;
					}
				
					if ((isCharAlphaNum(ch))) {   // char is part of the word
						charInSen++;
						continue;
					}
				}
			}

            if (total_sentence == 0) {
				printOnConsole("Usage", "The file doesn't have any provided delimiters");
            	return;
            }
            String roundedVal = String.format("%.2f", avgLengthSum/total_sentence);
            System.out.println("Average length of sentence is: " + roundedVal);
			printOnConsole("Output", "Average length of sentence is: " + roundedVal);      
        } 
        catch (FileNotFoundException e) {
			printOnConsole("Error", "The specified file could not be found.");      
        }
        catch (IOException ex) {
        	printOnConsole("Error", ex.getMessage());
        }
    	
	}

	private static boolean processArgs(CmdArguments cmdArgs) {
		
		// file name
        //String fname_rx = "([a-zA-Z]:)?(\\\\[a-zA-Z0-9._-]+)+\\\\?";
        
		if (cmdArgs.file_path == null) {
			printOnConsole("Error", "File path is required");
			return false;
		}

        Path normalizedPath = Paths.get(cmdArgs.file_path).normalize();
        
        if (!Files.exists(normalizedPath)) {
			printOnConsole("Error", "File path is not valid");
        	return false;
        }
		if (!cmdArgs.file_path.contains(".txt")) {
			printOnConsole("Error", "Only text(.txt) files are accepted");
			return false;
		}

		// word length
		if (cmdArgs.word_len >= 15) {
			printOnConsole("Usage", "The maximum word limit allowed is 15");			
			return false;
		}
		
		// delimiters; verify if those are valid delimiters and not alphanumeric character
        for (char ch : cmdArgs.deli_list) {    	
        	boolean isValidDelimiter = !(ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9' || ch == '\'');
        	if (!isValidDelimiter) {
				printOnConsole("Error", "Invalid delimiter found");		
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
				printOnConsole("Error", "\'args[i]\' " + "Command not found.");		
    			return null;
    		}
    		else if (i == 0 && args[i].equals("sma")) {
				if (args.length == 1) {
					printCmdDescription();
					return null;
				}
    			continue;
    		}
    		
    		switch (args[i]) {
	            case "-f":
	                if (i + 1 < args.length) {
	                	Path filePath = Paths.get(args[i + 1]);	                	
	                	cmdAttrb.file_path = filePath.toString();
	                    i++; // Skip the next argument since it's the file path
	                } else {
						printOnConsole("Error", "-f flag requires a file argument.");		
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
							printOnConsole("Error", "Non fractional value is required for word length");		
	                		return null;
	                	}
	                	
	                } else {
						printOnConsole("Error", "-w flag requires a word length argument.");		
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
						printOnConsole("Error", "-d flag requires a directory argument.");		
	                    return null;
	                }
	                break;
				case "-a":
					cmdAttrb.actualWord = true;
					break;
				case "-h":
					printCmdDescription();
					return null;
	            default:
	                // Handle other arguments or display an error message
	            	System.out.println("Unknown option or argument: " + args[i]);
	                return null;
    		}   		
    	}
    	return cmdAttrb;
    }
    private static boolean isCharAlphaNum(char ch) {
		return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9' || ch == '\'');
	}

	private static void printCmdDescription() {
		StringBuilder sb = new StringBuilder();

		sb.append("Usage: ");
		sb.append("sma [options] [args...]");
		sb.append("\n");
		sb.append("    or  Exit");
		sb.append("\n\n");
		sb.append("The options include:");
		sb.append("\n\n\t");

		sb.append("-f <To provide the file path>");
		sb.append("\n\t\t");
		sb.append("[The static file path with file name and extention goes as the argument]");

		sb.append("\n\n\t");

		sb.append("-w <To specify the word length>");
		sb.append("\n\t\t");
		sb.append("[Integer number to define the word length]");

		sb.append("\n\n\t");

		sb.append("-a <To consider the actual word in the text file.>");
		sb.append("\n\t\t");
		sb.append("[No argument required for this option.]");

		sb.append("\n\n\t");

		sb.append("-d <To specify delimeters which defines the end of the sentence>");
		sb.append("\n\t\t");
		sb.append("[String of consicutive delimiter characters]");
		
		sb.append("\n\n\t");
		sb.append("-h <To show the command description and usage>");

		sb.append("\n\n");
		sb.append("Note: -w and -a both do not work togather");
		
		System.out.println(sb.toString());
	}

	public static void printOnConsole(String label, String msg) {
		System.out.println(label + ": " + msg);
	}
}

class CmdArguments {
	byte word_len;
	String file_path;
	boolean actualWord;
	List<Character> deli_list = new ArrayList<Character>();
		
		
	public CmdArguments() {
		word_len = Byte.MIN_VALUE;
		actualWord = false;
	}	
}