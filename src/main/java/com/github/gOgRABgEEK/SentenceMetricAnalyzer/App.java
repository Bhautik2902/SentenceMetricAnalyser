package com.github.gOgRABgEEK.SentenceMetricAnalyzer;

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
		// TODO Auto-generated method stub
		
	}

	private static boolean processArgs(CmdArguments cmdArgs) {
		// TODO Auto-generated method stub
		return false;
	}

	private static CmdArguments processCommand(String cmd) {
    	return null;
    }
    
}

class CmdArguments {
	byte word_len;
	String file_path;
	List<String> deli_list;
	
	public CmdArguments(byte len, String path, List<String> list) {
		this.word_len = len;
		this.file_path = path;
		this.deli_list = list;
	}
	
	public CmdArguments() {
		
	}	
}