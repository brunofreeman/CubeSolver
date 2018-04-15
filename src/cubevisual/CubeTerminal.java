package cubevisual;

import cubesolver.CubeFileFinder;
import cubesolver.CubeSolver;
import cubesolver.SolveReport;
import cubesolver.cube.Cube3x3x3;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class CubeTerminal {

    private String file = CubeFileFinder.getPath("storage\\cubeState.txt");
    private Cube3x3x3 cube;
    private CubeSolver solver;
    private static int numMovesToRow = 25;
    private TextArea tp; //terminal prompt, response, and output
    private TextField tr;
    private TextFlow to;
    private Mode mode = Mode.NORM;
    private Cube3D cube3d;
    private CubeApp app;
    private HashMap<String, Integer> highlightCounter = new HashMap<>();

    public CubeTerminal(TextArea tp, TextField tr, TextFlow to, CubeApp app) {
        cube = new Cube3x3x3();
        solver = new CubeSolver(cube);
        this.tp = tp;
        this.tr = tr;
        this.to = to;
        this.app = app;
        cube3d = new Cube3D(cube, 100, this.app, this);
        prompt();
    }

    public CubeTerminal(Cube3x3x3 cube, TextArea tp, TextField tr, TextFlow to, Cube3D cube3d, CubeApp app) {
        this.cube = cube;
        solver = new CubeSolver(this.cube);
        this.tp = tp;
        this.tr = tr;
        this.to = to;
        this.cube3d = cube3d;
        this.app = app;
        prompt();
    }

    private void prompt() {
        prompt("Random moves, specified moves, check state, reset cube, scramble cube, input cube, save to file, load from file, run solver, explain notation, or clear terminal?\n(rm/sm/cs/rc/sc/ic/stf/lff/rs/en/ct):");
    }

    private void prompt(String prompt) {
        tp.clear();
        tp.setText(prompt);
    }

    public void exec(String cmd) {
        switch (mode) {
            case NORM:
                String[] cmds = cmd.trim().split((" "));
                int numTimes = 1;
                try {
                    numTimes = Integer.parseInt(cmds[cmds.length - 1]);
                    cmds = Arrays.copyOfRange(cmds, 0, cmds.length - 1);
                } catch (Exception e) {
                }
                for (int i = 0; i < numTimes; i++) {
                    for (int j = 0; j < cmds.length; j++) {
                        execOption(cmds[j]);
                    }
                }
                break;
            case RM:
                try {
                    randomMoves(Integer.parseInt(cmd.trim()));
                } catch (NumberFormatException e) {
                    println("\nInvalid input.");
                    line();
                }
                break;
            case SM:
                specifiedMoves(cmd);
        }
    }

    private void execOption(String cmd) {
        switch (cmd) {
            case "rm":
                line();
                mode = Mode.RM;
                prompt("Enter the amount of random move to make (-1 to exit):");
                break;
            case "sm":
                line();
                mode = Mode.SM;
                prompt("Enter move(s) (\"exit\" to exit):");
                break;
            case "cs":
                String solved = cube.isSolved() ? "\nCube solved.\n" : "\nCube not solved.\n";
                println(solved);
                printState();
                line();
                break;
            case "rc":
                cube.setToDefault();
                println("\nCube reset.");
                printState();
                updateCube3d();
                line();
                break;
            case "sc":
                cube.scramble();
                println("\nCube scrambled.");
                printState();
                updateCube3d();
                line();
                break;
            case "ic":
                app.launchColorPicker();
                break;
            case "stf":
                println("\nCube state saved to " + file + ".");
                line();
                writeToFile();
                break;
            case "lff":
                println("\nCube state loaded from " + file + ".");
                line();
                loadFromFile();
                updateCube3d();
                break;
            case "rs":
                println("\nSolver output:");
                solver = new CubeSolver(cube);
                SolveReport sr = solver.solve();
                print(formatSolveReport(sr.solveSet));
                println();
                line();
                println(sr.movesReport);
                line();
                moveAndAnimate(CubeSolver.removeSolveSetTitles(sr.solveSet));
                break;
            case "en":
                println("\nNotation:");
                printNotation();
                line();
                break;
            case "ct":
                to.getChildren().clear();
                highlightCounter = new HashMap<>();
                break;
            default:
                println("\nInvalid selection.");
                line();
        }
    }

    private String formatSolveReport(ArrayList<String> report) {
        /*ArrayList<String> formattedReport = new ArrayList<>();
        boolean first = true;
        for (String move : report) {
            if (!Cube3x3x3.isValidNotation(move)) {
                if (!first) {
                    formattedReport.set(formattedReport.size() - 1, formattedReport.get(formattedReport.size() - 1).substring(0, formattedReport.get(formattedReport.size() - 1).length() - 2));
                }
                move = capitalizeSolveSetTitle(move.replace("(\\[|\\]", "").replace("\n", "")) + ":" + "\n";
                if (!first) {
                    formattedReport.add("\n");
                }
                first = false;
                formattedReport.add(move);
            } else {
                formattedReport.add(move + ", ");
            }
        }
        formattedReport.set(formattedReport.size() - 1, formattedReport.get(formattedReport.size() - 1).substring(0, formattedReport.get(formattedReport.size() - 1).length() - 2));
        return formattedReport;*/
        String formattedReport = "";
        boolean first = true;
        for (String move : report) {
            if (!Cube3x3x3.isValidNotation(move)) {
                if (!first) {
                    formattedReport = formattedReport.substring(0, formattedReport.length() - 2);
                }
                move = capitalizeSolveSetTitle(move.replace("(\\[|\\]", "").replace("\n", "")) + ":" + "\n";
                if (!first) {
                    formattedReport += "\n";
                }
                first = false;
                formattedReport += move;
            } else {
                formattedReport += move + ", ";
            }
        }
        formattedReport = formattedReport.substring(0, formattedReport.length() - 2);
        return formattedReport;
    }

    private String capitalizeSolveSetTitle(String title) {
        switch (title) {
            case "DAISY":
                return "Daisy";
            case "PETALS DOWN":
                return "Petals Down";
            case "BOTTOM CORNERS":
                return "Bottom Corners";
            case "MIDDLE EDGES":
                return "Middle Edges";
            case "OUT TWIST IN TWIST":
                return "Out Twist, In Twist";
            case "R PRIME F":
                return "R Prime F";
            case "FUR URF":
            case "FFU":
                return title;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void highlightMove(String move) {
        if (!Cube3x3x3.isValidNotation(move)) {
            throw new IllegalArgumentException();
        }
        if (highlightCounter.containsKey(move)) {
            highlightCounter.put(move, highlightCounter.get(move).intValue() + 1);
        } else {
            highlightCounter.put(move, 1);
        }
        //System.out.println("move: " + move +"\n" + highlightCounter.toString() + "\nMatching: " + highlightCounter.get(move));
        int foundCount = 0;
        boolean found = false;
        for (int i = 0; i < to.getChildren().size(); i++) {
            Text text = (Text) to.getChildren().get(i);
            //System.out.println("1\"" + text.getText() + "2\"");
            //System.out.print(text.getText());
            //int index = text.getText().indexOf(move);
            Pattern p = Pattern.compile(", " + move + "$|, " + move + "\\n|" + move + ",|^" + move + "$");
            Matcher m = p.matcher(text.getText());
            //if (index != -1) {
                while (!found && m.find()) { //index != -1) {
                    /*if (m.group().charAt(m.group().length() - 1) == ',') {
                        index--;
                    }*/

                    //System.out.println(m.group());
                    /*boolean notTitle = true;
                    try {
                        notTitle = text.getText().substring(index + move.length(), index + move.length() + 1).equals(",") || text.getText().substring(index - 2, index - 1).equals(",");
                    } catch (StringIndexOutOfBoundsException e1) {
                        try {
                            notTitle = text.getText().substring(index + move.length(), index + move.length() + 1).equals(",");
                        } catch (StringIndexOutOfBoundsException e2) {
                            try {
                                notTitle = text.getText().substring(index - 2, index - 1).equals(",");
                            } catch (StringIndexOutOfBoundsException e3) {
                                notTitle = false;
                            }
                        }
                    }*/
                    /*try {
                        System.out.println(text.getText().substring(index, index + 3) + " - " +  " - " + index);
                    } catch (Exception e) {
                        System.out.println(text.getText().substring(index, index) + " - " +  " - " + index + " - " + text.getText());
                    }*/
                    //System.out.println(foundCount);
                    if (++foundCount == highlightCounter.get(move)) {
                        int index = m.start();
                        if (m.group().charAt(0) == ',') {
                            index += 2;
                        }
                        //System.out.println("text: " + text.getText());
                        String before = text.getText().substring(0, index);
                        String after = text.getText().substring(index + move.length());
                        //System.out.println("before: " + before + "\nmove: " + move + "\nafter: " + after);
                        to.getChildren().add(i + 1, new Text(after));
                        to.getChildren().add(i + 1, new Text(move));
                        to.getChildren().add(i + 1, new Text(before));
                        to.getChildren().remove(i);
                        //System.out.println("removed: " + ((Text) to.getChildren().remove(i)).getText());
                        //System.out.println("highlighting: " + ((Text) to.getChildren().get(i +1)).getText() + "\n-------------------");
                        to.getChildren().get(i + 1).getStyleClass().add("highlight-text");
                        to.getChildren().get(i + 1).applyCss();
                        ((Text) to.getChildren().get(i)).setFont(Font.font("monospace", app.getFontSize()));
                        ((Text) to.getChildren().get(i + 1)).setFont(Font.font("monospace", app.getFontSize()));
                        ((Text) to.getChildren().get(i + 2)).setFont(Font.font("monospace", app.getFontSize()));
                        //i++;
                        found = true;
                        //index = -1;
                    } /*else {
                        index = text.getText().indexOf(move, index + 1);
                    }*/
                //}
            }
        }
    }
    
    public void condenseChildren() {
        String str = "";
        for (Node node : to.getChildren()) {
            str += ((Text) node).getText();
        }
        to.getChildren().clear();
        Text text = new Text(str);
        text.setFont(Font.font("monospace", app.getFontSize()));
        to.getChildren().add(text);
    }

    public void removeHighlight() {
        for (Node text : to.getChildren()) {
            text.getStyleClass().clear();
            ((Text) text).setFont(Font.font("monospace", app.getFontSize()));
        }
    }

    private void updateCube3d() {
        cube3d.setCube(cube);
        cube3d.cubeToCube3d();
        app.changeMeshViews();
    }

    private void randomMoves(int numMoves) {
        if (numMoves > 0) {
            try {
                println();
                String[] moves = cube.getRandomMoves(numMoves);
                int numRows = moves.length / numMovesToRow;
                int moveNum = 0;
                for (int i = 0; i < numRows; i++) {
                    for (int j = 0; j < numMovesToRow; j++) {
                        print(String.format("%-4s", moves[moveNum++]));
                    }
                    println();
                }
                int numLeft = moves.length % numMovesToRow;
                for (int i = 0; i < numLeft; i++) {
                    print(String.format("%-4s", moves[moveNum++]));
                }
                if (numLeft > 0) {
                    println();
                }
                println();
                moveAndAnimate(moves);
                printState();
                line();
            } catch (Exception e) {
                println("\nInvalid input.");
                line();
            }
        } else if (numMoves == -1) {
            resetMode();
        } else {
            println("\nInvalid input.");
            line();
        }
    }

    private void specifiedMoves(String cmd) { //can hit enter with blank string, causes exception
        boolean terminated = false;
        String[] movesAndBlanks = cmd.split(" ");
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
                println("\nInvalid selection.");
            }
        } else {
            moveAndAnimate(moves);
            println();
            printState();
        }
        if (terminated) {
            resetMode();
        }
    }

    private void moveAndAnimate(String... moves) {
        cube.move(moves);
        cube3d.animate(moves);
    }

    private void resetMode() {
        mode = Mode.NORM;
        prompt();
    }

    private void printState() {
        String[] states = cube.getCubeStateAsText();
        for (int i = 0; i < states.length; i++) {
            println(String.format("%4s%s", (i + 1) + ". ", states[i]));
        }
    }

    private void writeToFile() {
        Formatter output = null;
        try {
            output = new Formatter(file);
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
        } catch (FileNotFoundException e) {
        }
    }

    private void loadFromFile() {
        Scanner fileInput = null;
        try {
            fileInput = new Scanner(new File(file));
            String[] cubeState = new String[Cube3x3x3.NUM_PIECES];
            for (int i = 0; i < Cube3x3x3.NUM_PIECES; i++) {
                cubeState[i] = fileInput.nextLine().substring(4);
            }
            cube = new Cube3x3x3(cubeState);
        } catch (FileNotFoundException e) {
        }
    }

    private void printNotation() {
        println("no modifier = clockwise, ' = counterclockwise or inverted, 2 = twice or a 180");
        println("e.g.: R (clockwise), R' (counterclockwise), R2 (180)");
        println("R = right face");
        println("r = R + V");
        println("L = left face");
        println("l = L + V'");
        println("U = up face");
        println("u = U + H");
        println("D = down face");
        println("d = D + H'");
        println("F = front face");
        println("f = F + C");
        println("B = back face");
        println("b = B + C'");
        println("V = vertical slice (between R & L), turned in direction of R");
        println("v = R + L'");
        println("H = horizontal slice (between U & D), turned in direction of U");
        println("h = U + D'");
        println("C = central slice (between F & B), turned in direction of F");
        println("c = F + B'");
        println("x = rotation through x-axis");
        println("y = rotation through y-axis");
        println("z = rotation through z-aixs");
    }

    private void line() {
        println("...");
    }

    private void print(ArrayList<String> out) {
        for (String single : out) {
            print(single);
        }
    }

    private void println(String out) {
        to.getChildren().add(new Text(out + "\n"));
        updateTOSize();
    }

    private void println() {
        to.getChildren().add(new Text("\n"));
        updateTOSize();
    }

    private void print(String out) {
       to.getChildren().add(new Text(out));
       updateTOSize();
    }

    private void updateTOSize() {
        for (Node text : to.getChildren()) {
            ((Text) text).setFont(Font.font("monospace", app.getFontSize()));
        }
        app.scrollToBottom(); //TODO: not going all the way (missing the last input or something)
    }

    private enum Mode {
        NORM, RM, SM;
    }
}