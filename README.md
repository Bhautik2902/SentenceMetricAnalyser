# Sentence Length Analyzer

The Sentence Length Analyzer is a command-line application designed to analyze the average sentence length within a given text document. This tool calculates the average number of words per sentence, providing valuable insights into the syntactic structure and readability of textual content.


## Table of Contents
- [Features](#features)
- [Download](#download)
- [Usage](#usage)

## Features

- **Sentence Length Calculation:** The application calculates the average sentence length of the sentences in the text file to avoid run-on sentences in the assignment. So that user can better their writing style.
  
- **Input Flexibility:** The application provides a way to define the word to the user by asking for word length. Also, the delimiters that end the sentence are also flexible and the user can input them while writing the command.

- **Customizable Output:** The output is a real number which represents the average sentence length in the provided document rounded to two decimal digits.

## Download

The .exe file of the file can be downloaded from <a href="https://github.com/gOgRABgEEK/SentenceMetricAnalyser/releases/tag/v1.0.0"> here</a>. Download the .exe file and the application is ready to be used. You can ignore the warnings from windows.

## Usage

This command-line application is fairly easy and similar to any other CLI. There are four flags to interact with. A command can include multiple flags in any order with a proper argument following the flag. The file flag with its argument is mandatory to provide to use the feature. The default value has been picked if any flag is not included in the command. The flags with their functions and expected arguments are listed below.
        
- **-f:**  The argument for this flag is the file path. The static path of the locally stored text (.txt) file is expected. Also, the file path input undergoes the format check to verify whether it is valid or not.
  
- **-d:**  This flag allows the user to provide delimiters which define the end of the sentence. The sequence of delimiter characters is expected as an argument [ e.g. -d .?!; ]. In this scenario '.', '?', '!', and ';' will mark the end of the sentence. If this flag is not provided, by default '.', '?' and '!' will be considered.

- - **-h:**  Shows the description and usage of the command.

- **-w:** This flag provides a way to set the word length meaning how many characters the user found suitable which make a word. The Integer is expected as an argument. In case this flag is not included in the command, by default word length is 3.

- **-a:** This flag provides a way to consider the actual word. The Integer is expected as an argument.
  
- **Exit:** To exit the application.
