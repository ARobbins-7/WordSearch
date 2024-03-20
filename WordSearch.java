// Andrew Robbins
// 2/3/24
// CS 145
// Assingment 1, Word Search Generator

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;

// This Program will create word searches based off of user input, and can display
// both a word search, and a solved version.
public class WordSearch {
    private static int height;
    private static int length;
    private static int wordC;
    private static ArrayList<String> words;
    private static char[][] key;
    private static char[][] savedKey;
    private static char[][] savedSolution;
    private static String input;
    private static boolean wordSearchGenerated = false;
    private static boolean overlap = true;

    // Main method, calls the starting methods.
    public static void main(String[] args) {
        printIntro();
        scanner();
    }

    // Prints a menu.
    public static void printIntro() {
        System.out.println("");
        System.out.println("Welcome");
        System.out.println("This program is designed to create custom word searches");
        System.out.println("Please choose one of the following options:");
        System.out.println("Generate a new word search (g)");
        System.out.println("Print your current word search (p)");
        System.out.println("Show the solution to your current word search (s)");
        System.out.println("Save your current puzzle to a txt file (t)");
        System.out.println("Quit the program (q)");
        System.out.println("");
    }

    // Gives the menu function using a scanner.
    public static void scanner() {
        Scanner in = new Scanner(System.in);
        String input = in.next();
        input = input.substring(0, 1);

        if (input.equals("g") || (input.equals("G"))) {
            wordSearchGenerated = true;
            userInput();
        } else if (input.equals("p") || (input.equals("P"))) {
            System.out.println("");
            if (!wordSearchGenerated) {
                System.out.println("Please generate a word search first");
                System.out.println("");
                scanner();
                return;
            } else {
                printSavedPuzzle();
                System.out.println("");
                System.out.println("Enter M for Menu");
                System.out.println("");
                scanner();
            }
        } else if (input.equals("s") || (input.equals("S"))) {
            System.out.println("");
            if (!wordSearchGenerated) {
                System.out.println("Please generate a word search first");
                System.out.println("");
                scanner();
            } else {
                printSavedSolution();
                System.out.println("");
                System.out.println("Enter M for Menu");
                System.out.println("");
                scanner();
            }
        } else if (input.equals("q") || (input.equals("Q"))) {
            System.exit(0);
        } else if (input.equals("m") || (input.equals("M"))) {
            System.out.println("");
            printIntro();
            scanner();
        } else if (input.equals("t") || (input.equals("T"))) {
                System.out.println("");
            if (!wordSearchGenerated) {
                System.out.println("Please generate a word search first");
                System.out.println("");
                scanner();
            } else {
                System.out.println("Puzzle saved");
                txtOut("puzzle.txt");
            }
        } else {
            System.out.println("");
            System.out.println("Please enter valid input");
            System.out.println("");
            scanner();
        }
    }

    // Creates a puzzle using the user input, places the words at random on the grid
    // then fills the rest of the grid with - and saves, then replaces the - with random 
    // letters and saves.
    public static void rndm() {
        System.out.println("Generating word search...");
        key = new char[height][length];
        for (String word : words) {
            overlap = true;
            while (overlap) {
                int b = word.length();
                int a = height - b;
                int x = randomRange(0, a);
                int y = randomRange(0, length - b);

                // 1 for vertical, 2 for horizontal, 3 for diagonal.
                int direction = randomRange(1, 3); 
                overlap = false;
                // Vertical placement.
                if (direction == 1) {                   
                    for (int ind = 0; ind < b; ind++) {
                        if (key[x + ind][y] != 0) {
                            overlap = true;
                            break;
                        }
                    }
                    if (!overlap) {
                        for (int ind = 0; ind < b; ind++) {
                            key[x + ind][y] = word.charAt(ind);
                        }
                    }
                // Horizontal placement.
                } else if (direction == 2) {
                    for (int ind = 0; ind < b; ind++) {
                        if (key[x][y + ind] != 0) {
                            overlap = true;
                            break;
                        }
                    }
                    if (!overlap) {
                        for (int ind = 0; ind < b; ind++) {
                            key[x][y + ind] = word.charAt(ind);
                        }
                    }
                //Diagonal placement.
                } else {
                    for (int ind = 0; ind < b; ind++) {
                        if (key[x + ind][y + ind] != 0) {
                            overlap = true;
                            break;
                        }
                    }
                    if (!overlap) {
                        for (int ind = 0; ind < b; ind++) {
                            key[x + ind][y + ind] = word.charAt(ind);
                        }
                    }
                }
            }
        }

        // Replaces empty spaces with '-'
        for (int i = 0; i < length; i++) {
            for (int ind = 0; ind < height; ind++) {
                if (key[ind][i] == 0) {
                    key[ind][i] = '-';
                }
            }
        }
        saveSolution();

        // Fill spaces filled with '-' with random characters
        for (int i = 0; i < length; i++) {
            for (int ind = 0; ind < height; ind++) {
                if (key[ind][i] == '-') {
                    char t = (char) randomRange(97, 122);
                    key[ind][i] = t;
                }
            }
        }
        savePuzzle();
    }
    
    // Saves the word puzzle to a temporary array.
    public static void savePuzzle() {
        savedKey = new char[height][length];
        for (int i = 0; i < height; i++) {
            System.arraycopy(key[i], 0, savedKey[i], 0, length);
        }
    }

    // Prints the saved puzzle.
    public static void printSavedPuzzle() {
        for (int i = 0; i < height; i++) {
            for (int ind = 0; ind < length; ind++) {
                System.out.print(savedKey[i][ind] + " ");
            }
            System.out.println(" ");
        }
        // Prints the words you are searching for under the puzzle.
        System.out.println(" ");
        System.out.println("Find these words:");
        for(int i =0; i<wordC; i++) {
            System.out.println(words.get(i));
        }
    }

    // Saves a version of the puzzle with '-' instead of random letters.
    public static void saveSolution() {
        savedSolution = new char[height][length];
        for (int i = 0; i < height; i++) {
            System.arraycopy(key[i], 0, savedSolution[i], 0, length);
        }
    }

    // Prints the saved solution.
    public static void printSavedSolution() {
        for (int i = 0; i < height; i++) {
            for (int ind = 0; ind < length; ind++) {
                System.out.print(savedSolution[i][ind] + " ");
            }
            System.out.println(" ");
        }
        // Prints the words you are searching for under the solution.
        System.out.println(" ");
        System.out.println("Find these words:");
        for(int i =0; i<wordC; i++) {
            System.out.println(words.get(i));
        }
    }

    // Takes user input and calls other methods to create a word puzzle.
    public static void userInput() {
        Scanner scan = new Scanner(System.in);
        wordC = 0;
        words = new ArrayList<>();
        System.out.println("");
        System.out.println("Please enter the words for your puzzle with only one word per line.");
        System.out.println("When you have entered your words type: run");
        System.out.println("");
        while (scan.hasNextLine()) {
            input = scan.next();
            if (input.equals("run")) {
                System.out.println("");
                break;
            }
            wordC++;
            words.add(input);
        }
        size();
        rndm();  
        printIntro();
        scanner();
    }

    // Defines the size for a specific puzzle.
    public static void size() {
        int maxWordLength = 0;
        for (String word : words) {
            if (word.length() > maxWordLength) {
                maxWordLength = word.length();
            }
        }
        height = maxWordLength + 8;
        length = height + (height / 3);
    }

    // Creates a random range which is used elsewhere in the program, probably shouldnt
    // have this as a method, but couldnt figure out how to fix that in a simple way.
    public static int randomRange(int min, int max) {
        Random generator = new Random();
        return generator.nextInt(max - min + 1) + min;
    }

    // Saves both the current puzzle and its solution to a txt file.
    public static void txtOut(String fileName) {
        try {
            // Gets the current working directory 
            String programDirectory = System.getProperty("user.dir");
            String filePath = programDirectory + File.separator + fileName;

            PrintStream printStream = new PrintStream(new FileOutputStream(filePath));

            // Writes the puzzle grid to the file
            for (int i = 0; i < height; i++) {
                for (int ind = 0; ind < length; ind++) {
                    printStream.print(savedKey[i][ind] + " ");
                }
                printStream.println();
            }

            // Writr the words to find under the puzzle
            printStream.println("Find these words:");
            for (int i = 0; i < wordC; i++) {
                printStream.println(words.get(i));
            }
            printStream.println();
            printStream.println();
            
            // Writes the solution to the file
            for (int i = 0; i < height; i++) {
                for (int ind = 0; ind < length; ind++) {
                    printStream.print(savedSolution[i][ind] + " ");
                }
                printStream.println();
            }
            printStream.println("Find these words:");
            for (int i = 0; i < wordC; i++) {
                printStream.println(words.get(i));
            }
            printStream.close();
            System.out.println("Puzzle saved to " + filePath);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
            e.printStackTrace();
        }
    }
}