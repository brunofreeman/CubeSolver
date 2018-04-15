package cubesolver;

import cubesolver.cube.Cube3x3x3;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.Scanner;

public class CubeTester {

    private static final Scanner input = new Scanner(System.in);
    private String file = CubeFileFinder.getPath("storage\\averageMoves.txt");
    private Cube3x3x3 cube;
    private CubeSolver solver;
    private static int numMovesToRow = 25;
    private boolean terminated = false;
    private boolean multiple = false;

    public CubeTester() {
        cube = new Cube3x3x3();
        solver = new CubeSolver(cube);
    }

    public CubeTester(Cube3x3x3 cube) {
        this.cube = cube;
        solver = new CubeSolver(this.cube);
    }

    public void run() {
        System.out.println("Cube Tester\n");
        while (!terminated) {
            if (!multiple) {
                System.out.print("Random moves, specified moves, check state, reset cube, scramble cube, save to file, load from file, run solver, explain notation, or terminate?\n(rm/sm/cs/rc/sc/stf/lff/rs/en/t): ");
            }
            String choiceInput = input.nextLine();
            String[] choices = choiceInput.trim().split((" "));
            int numTimes = 1;
            try {
                numTimes = Integer.parseInt(choices[choices.length - 1]);
                choices = Arrays.copyOfRange(choices, 0, choices.length - 1);
            } catch (Exception e) {
            }
            for (int i = 0; i < numTimes; i++) {
                if (choices.length > 1) {
                    multiple = true;
                }
                for (int j = 0; j < choices.length; j++) {
                    if (j == choices.length - 1) {
                        multiple = false;
                    }
                    execOption(choices[j]);
                }
            }
        }
    }

    private void execOption(String choice) {
        switch (choice) {
            case "rm":
                System.out.println("----------------------");
                randomMoves();
                break;
            case "sm":
                System.out.println("----------------------");
                specifiedMoves();
                break;
            case "cs":
                String solved = cube.isSolved() ? "\nCube solved." : "\nCube not solved.";
                System.out.println(solved);
                printState();
                System.out.println("----------------------");
                break;
            case "rc":
                cube.setToDefault();
                System.out.println("\nCube reset.");
                printState();
                System.out.println("----------------------");
                break;
            case "sc":
                cube.scramble();
                System.out.println("\nCube scrambled.");
                printState();
                System.out.println("----------------------");
                break;
            case "stf":
                System.out.println("\nCube state saved to " + file + ".");
                System.out.println("----------------------");
                writeToFile();
                break;
            case "lff":
                System.out.println("\nCube state loaded from " + file + ".");
                System.out.println("----------------------");
                loadFromFile();
                break;
            case "rs":
                System.out.println("\nSolver output:");
                solver = new CubeSolver(cube);
                solver.solve();
                System.out.println("----------------------");
                break;
            case "en":
                System.out.println("\nNotation:");
                printNotation();
                System.out.println("----------------------");
                break;
            case "t":
                System.out.println("----------------------");
                terminated = true;
                break;
            default:
                System.out.println("\nInvalid selection.");
                System.out.println("----------------------");
        }
    }

    private void randomMoves() {
        int numMoves = 0;
        while (numMoves != -1) {
            try {
                System.out.print("Enter the amount of random move to make (-1 to exit): ");
                numMoves = input.nextInt();
                if (numMoves > 0) {
                    System.out.println();
                    String[] moves = cube.getRandomMoves(numMoves);
                    int numRows = moves.length / numMovesToRow;
                    int moveNum = 0;
                    for (int i = 0; i < numRows; i++) {
                        for (int j = 0; j < numMovesToRow; j++) {
                            System.out.printf("%-4s", moves[moveNum++]);
                        }
                        System.out.println();
                    }
                    int numLeft = moves.length % numMovesToRow;
                    for (int i = 0; i < numLeft; i++) {
                        System.out.printf("%-4s", moves[moveNum++]);
                    }
                    if (numLeft > 0) {
                        System.out.println();
                    }
                    System.out.println();
                    cube.move(moves);
                    printState();
                } else if (numMoves != -1) {
                    System.out.println("\nInvalid selection.");
                }

                System.out.println("----------------------");
            } catch (Exception e) {
                System.out.println("\nInvalid selection.");
                System.out.println("----------------------");
                input.nextLine();
            }
        }
        input.nextLine();
    }

    private void specifiedMoves() {
        boolean terminated = false;
        while (!terminated) {
            System.out.print("Enter move(s) (\"exit\" to exit): ");
            terminated = executeSpecifiedMoves(input.nextLine());
            System.out.println("----------------------");
        }
    }

    private boolean executeSpecifiedMoves(String input) {
        boolean terminated = false;
        String[] movesAndBlanks = input.split(" ");
        int numBlanks = 0;
        for (int i = 0; i < movesAndBlanks.length; i++) {
            if (movesAndBlanks[i].equals("")) {
                numBlanks++;
            }
        }
        String[] moves = new String[movesAndBlanks.length - numBlanks];
        int j = 0;
        for (int i = 0; i < movesAndBlanks.length; i++) {
            if (!movesAndBlanks[i].equals("")) {
                moves[j++] = movesAndBlanks[i];
            }
        }
        if (moves[moves.length - 1].trim().equals("exit")) {
            terminated = true;
            moves = Arrays.copyOfRange(moves, 0, moves.length - 1);
        }
        if (!cube.isValidNotation(moves) || moves.length == 0) {
            if (!terminated || (terminated && moves.length > 0)) {
                System.out.println("\nInvalid selection.");
            }
        } else {
            cube.move(moves);
            System.out.println();
            printState();
        }
        return terminated;
    }

    private void printState() {
        String[] states = cube.getCubeStateAsText();
        for (int i = 0; i < states.length; i++) {
            System.out.printf("%4s%s%n", (i + 1) + ". ", states[i]);
        }
    }

    private void writeToFile() {
        Formatter output = null;
        try {
            output = new Formatter(file);
        } catch (FileNotFoundException e) {
        }
        String[] cubeState = cube.getCubeStateAsText();
        for (int i = 1; i <= Cube3x3x3.NUM_PIECES; i++) {
            String n = "%n";
            if (i == Cube3x3x3.NUM_PIECES) {
                n = "";
            }
            output.format("%-4s%s" + n, i + ".", cubeState[i - 1]);
        }
        if (output != null) {
            output.close();
        }
    }

    private void loadFromFile() {
        Scanner fileInput = null;
        try {
            fileInput = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
        }
        String[] cubeState = new String[Cube3x3x3.NUM_PIECES];
        for (int i = 0; i < Cube3x3x3.NUM_PIECES; i++) {
            cubeState[i] = fileInput.nextLine().substring(4);
        }
        cube = new Cube3x3x3(cubeState);
    }

    private void printNotation() {
        System.out.println("no modifier = clockwise, ' = counterclockwise or inverted, 2 = twice or a 180");
        System.out.println("e.g.: R (clockwise), R' (counterclockwise), R2 (180)");
        System.out.println("R = right face");
        System.out.println("r = R + V");
        System.out.println("L = left face");
        System.out.println("l = L + V'");
        System.out.println("U = up face");
        System.out.println("u = U + H");
        System.out.println("D = down face");
        System.out.println("d = D + H'");
        System.out.println("F = front face");
        System.out.println("f = F + C");
        System.out.println("B = back face");
        System.out.println("b = B + C'");
        System.out.println("V = vertical slice (between R & L), turned in direction of R");
        System.out.println("v = R + L'");
        System.out.println("H = horizontal slice (between U & D), turned in direction of U");
        System.out.println("h = U + D'");
        System.out.println("C = central slice (between F & B), turned in direction of F");
        System.out.println("c = F + B'");
        System.out.println("x = rotation through x-axis");
        System.out.println("y = rotation through y-axis");
        System.out.println("z = rotation through z-aixs");
    }
}
