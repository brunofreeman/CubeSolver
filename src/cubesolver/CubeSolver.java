package cubesolver;

import cubesolver.cube.Cube3x3x3;
import cubesolver.cube.piece.CornerPiece;
import cubesolver.cube.piece.CubeColor;
import cubesolver.cube.piece.CubeFace;
import cubesolver.cube.piece.CubePiece;
import cubesolver.cube.piece.EdgePiece;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;

/**
 * Fridrich Algorithm
 *
 * @author Bruno Freeman
 */
public class CubeSolver {

    final private Cube3x3x3 originalCube;
    private Cube3x3x3 workingCube;
    private ArrayList<String> solveSet;
    private SolveMode solveMode;
    private String file = CubeFileFinder.getPath("storage\\averageMoves.txt");
    private static int numSteps = 8;

    public CubeSolver() //is useless because it will create an already solved cube to "solve"
    {
        originalCube = new Cube3x3x3();
        workingCube = new Cube3x3x3();
        solveSet = new ArrayList<>();
        solveMode = SolveMode.FRIDRICH;
    }

    public CubeSolver(Cube3x3x3 cube) {
        originalCube = cube;
        workingCube = new Cube3x3x3(originalCube);
        solveSet = new ArrayList<>();
        solveMode = SolveMode.FRIDRICH;
    }

    public CubeSolver(Cube3x3x3 cube, SolveMode solveMode) {
        originalCube = cube;
        workingCube = new Cube3x3x3(originalCube);
        solveSet = new ArrayList<>();
        this.solveMode = solveMode;
    }

    public static String[] removeSolveSetTitles(ArrayList<String> solveSet) {
        for (int i = solveSet.size() - 1; i >= 0; i--) {
            if (!Cube3x3x3.isValidNotation(solveSet.get(i))) {
                solveSet.remove(i);
            }
        }
        String[] returnArr = new String[solveSet.size()];
        for (int i = 0; i < solveSet.size(); i++) {
            returnArr[i] = solveSet.get(i);
        }
        return returnArr;
    }

    private void moveAndAdd(String... moves) {
        workingCube.move(moves);
        for (String move : moves) {
            solveSet.add(move);
        }
    }

    public void printState() {
        String[] states = workingCube.getCubeStateAsText();
        for (int i = 0; i < states.length; i++) {
            System.out.printf("%4s%s%n", (i + 1) + ". ", states[i]);
        }
    }

    public SolveReport solve() {
        switch (solveMode) { //TODO: add step skip conditions, add code that combines consecutives moves that could be one (e.g. U, d' --> y) - would that allow for code simplifications?
            case FRIDRICH:
                cross();
                f2l();
                oll();
                pll();
        }
        int numMoves = solveSet.size() - numSteps;
        updateAverageMoves(numMoves);
        String movesReport = ("Solve #: " + getNumRecords() + "\n" + "Number of moves: " + numMoves + "\n" + "Average number of moves: " + getAverageMoves());
        ArrayList<String> ssCopy = new ArrayList<>();
        for (String sse : solveSet) {
            ssCopy.add(sse);
        }
        solveSet.clear();
        workingCube = new Cube3x3x3(originalCube);
        return new SolveReport(ssCopy, movesReport);
    }

    private void cross() {
        daisy();
        petalsDown();
    }

    private void daisy() {
        solveSet.add("DAISY\n");
        if (workingCube.getColorsOfFace(CubeFace.R)[1][1] == CubeColor.YELLOW) {
            moveAndAdd("z'");
        } else if (workingCube.getColorsOfFace(CubeFace.L)[1][1] == CubeColor.YELLOW) {
            moveAndAdd("z");
        } else if (workingCube.getColorsOfFace(CubeFace.D)[1][1] == CubeColor.YELLOW) {
            moveAndAdd("z2");
        } else if (workingCube.getColorsOfFace(CubeFace.F)[1][1] == CubeColor.YELLOW) {
            moveAndAdd("x");
        } else if (workingCube.getColorsOfFace(CubeFace.B)[1][1] == CubeColor.YELLOW) {
            moveAndAdd("x'");
        }
        for (int i = 0; i < 4; i++) {
            CubeColor[][] faceColors = workingCube.getColorsOfFace(CubeFace.F);
            while (workingCube.faceContainsColorOnEdge(CubeFace.F, CubeColor.WHITE)) {
                if (faceColors[1][0] == CubeColor.WHITE || faceColors[1][2] == CubeColor.WHITE) {
                    if (faceColors[1][0] == CubeColor.WHITE) {
                        if (workingCube.pieceAt(-1, 1, 0).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                            if (workingCube.pieceAt(0, 1, 1).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                                if (workingCube.pieceAt(0, 1, -1).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                                    moveAndAdd("U2", "L'");
                                } else {
                                    moveAndAdd("U", "L'");
                                }
                            } else if (workingCube.pieceAt(0, 1, -1).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                                moveAndAdd("U'", "L'");
                            } else {
                                moveAndAdd("U", "L'"); //or U'
                            }
                        } else {
                            moveAndAdd("L'");
                        }
                    }
                    if (faceColors[1][2] == CubeColor.WHITE) {
                        if (workingCube.pieceAt(1, 1, 0).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                            if (workingCube.pieceAt(0, 1, 1).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                                if (workingCube.pieceAt(0, 1, -1).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                                    moveAndAdd("U2", "R");
                                } else {
                                    moveAndAdd("U'", "R");
                                }
                            } else if (workingCube.pieceAt(0, 1, -1).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                                moveAndAdd("U", "R");
                            } else {
                                moveAndAdd("U", "R"); //or U'
                            }
                        } else {
                            moveAndAdd("R");
                        }
                    }
                } else if (faceColors[0][1] == CubeColor.WHITE || faceColors[2][1] == CubeColor.WHITE) {
                    if (faceColors[0][1] == CubeColor.WHITE) {
                        if (workingCube.pieceAt(-1, 1, 0).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                            moveAndAdd("F");
                        } else {
                            moveAndAdd("F'");
                        }
                    } else if (workingCube.pieceAt(0, 1, -1).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                        if (workingCube.pieceAt(-1, 1, 0).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                            if (workingCube.pieceAt(1, 1, 0).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                                moveAndAdd("U2", "F"); //or F'
                            } else {
                                moveAndAdd("U", "F"); //or F'
                            }
                        } else if (workingCube.pieceAt(1, 1, 0).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                            moveAndAdd("U'", "F"); //or F'
                        } else {
                            moveAndAdd("U", "F"); //or U' or F'
                        }
                    } else if (workingCube.pieceAt(-1, 1, 0).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                        moveAndAdd("F'");
                    } else {
                        moveAndAdd("F");
                    }
                }
                faceColors = workingCube.getColorsOfFace(CubeFace.F);
            }
            if (workingCube.faceContainsColorOnEdge(CubeFace.R, CubeColor.WHITE)) {
                moveAndAdd("y");
                i = i == 3 ? i - 1 : i;
            } else if (workingCube.faceContainsColorOnEdge(CubeFace.L, CubeColor.WHITE)) {
                moveAndAdd("y'");
                i = i == 3 ? i - 1 : i;
            } else if (workingCube.faceContainsColorOnEdge(CubeFace.B, CubeColor.WHITE)) {
                moveAndAdd("y2");
                i = i == 3 ? i - 1 : i;
            } else {
                i = 4;
            }
        }
        CubeColor[][] faceColors = workingCube.getColorsOfFace(CubeFace.D);
        if (workingCube.faceContainsColorOnEdge(CubeFace.D, CubeColor.WHITE)) {
            if (faceColors[1][0] == CubeColor.WHITE) {
                if (workingCube.pieceAt(-1, 1, 0).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                    if (workingCube.pieceAt(0, 1, 1).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                        if (workingCube.pieceAt(0, 1, -1).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                            moveAndAdd("U2", "L2");
                        } else {
                            moveAndAdd("U", "L2");
                        }
                    } else if (workingCube.pieceAt(0, 1, -1).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                        moveAndAdd("U'", "L2");
                    } else {
                        moveAndAdd("U", "L2"); //or U'
                    }
                } else {
                    moveAndAdd("L2");
                }
            }
            if (faceColors[1][2] == CubeColor.WHITE) {
                if (workingCube.pieceAt(1, 1, 0).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                    if (workingCube.pieceAt(0, 1, 1).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                        if (workingCube.pieceAt(0, 1, -1).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                            moveAndAdd("U2", "R2");
                        } else {
                            moveAndAdd("U'", "R2");
                        }
                    } else if (workingCube.pieceAt(0, 1, -1).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                        moveAndAdd("U", "R2");
                    } else {
                        moveAndAdd("U", "R2"); //or U'
                    }
                } else {
                    moveAndAdd("R2");
                }
            }
            if (faceColors[0][1] == CubeColor.WHITE) {
                if (workingCube.pieceAt(0, 1, -1).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                    if (workingCube.pieceAt(-1, 1, 0).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                        if (workingCube.pieceAt(1, 1, 0).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                            moveAndAdd("U2", "F2");
                        } else {
                            moveAndAdd("U", "F2");
                        }
                    } else if (workingCube.pieceAt(1, 1, 0).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                        moveAndAdd("U'", "F2");
                    } else {
                        moveAndAdd("U", "F2"); //or U'
                    }
                } else {
                    moveAndAdd("F2");
                }
            }
            if (faceColors[2][1] == CubeColor.WHITE) {
                if (workingCube.pieceAt(0, 1, 1).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                    if (workingCube.pieceAt(-1, 1, 0).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                        if (workingCube.pieceAt(1, 1, 0).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                            moveAndAdd("U2", "B2");
                        } else {
                            moveAndAdd("U'", "B2");
                        }
                    } else if (workingCube.pieceAt(1, 1, 0).getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                        moveAndAdd("U", "B2");
                    } else {
                        moveAndAdd("U", "B2"); //or U'
                    }
                } else {
                    moveAndAdd("B2");
                }
            }
        }
    }

    private void petalsDown() {
        solveSet.add("\nPETALS DOWN\n");
        for (int i = 0; i < 4; i++) {
            CubeColor[][] frontFaceColors = workingCube.getColorsOfFace(CubeFace.F);
            CubeColor[][] otherFaceColors = workingCube.getColorsOfFace(CubeFace.F);
            if (frontFaceColors[0][1] == otherFaceColors[1][1]) {
                moveAndAdd("F2");
            } else {
                otherFaceColors = workingCube.getColorsOfFace(CubeFace.B);
                if (frontFaceColors[0][1] == otherFaceColors[1][1]) {
                    moveAndAdd("d2", "F2");
                } else {
                    otherFaceColors = workingCube.getColorsOfFace(CubeFace.R);
                    if (frontFaceColors[0][1] == otherFaceColors[1][1]) {
                        moveAndAdd("d'", "F2");
                    } else {
                        otherFaceColors = workingCube.getColorsOfFace(CubeFace.L);
                        if (frontFaceColors[0][1] == otherFaceColors[1][1]) {
                            moveAndAdd("d", "F2");
                        }
                    }
                }
            }
            if (i < 3) {
                moveAndAdd("y");
            }
        }
    }

    private void f2l() {
        bottomCorners();
        middleEdges();
    }

    private void bottomCorners() {
        solveSet.add("\nBOTTOM CORNERS\n");
        CubeColor[][] faceF = workingCube.getColorsOfFace(CubeFace.F);
        CubeColor[][] faceB = workingCube.getColorsOfFace(CubeFace.B);
        CubeColor[][] faceR = workingCube.getColorsOfFace(CubeFace.R);
        CubeColor[][] faceL = workingCube.getColorsOfFace(CubeFace.L);
        while (!(workingCube.faceOnlyContainsColor(CubeFace.D, CubeColor.WHITE)
                && faceF[1][1] == faceF[2][0] && faceF[1][1] == faceF[2][2]
                && faceB[1][1] == faceB[0][0] && faceB[1][1] == faceB[0][2]
                && faceR[1][1] == faceR[2][0] && faceR[1][1] == faceR[2][2]
                && faceL[1][1] == faceL[2][0] && faceL[1][1] == faceL[2][2])) {
            CubePiece piece1 = workingCube.pieceAt(-1, 1, -1);
            CubePiece piece2 = workingCube.pieceAt(1, 1, -1);
            if (piece1.containsColor(CubeColor.WHITE) || piece2.containsColor(CubeColor.WHITE)) {
                if (piece1.containsColor(CubeColor.WHITE)) {
                    if (piece1.getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                        if (piece2.getTile(CubeFace.F).getColor() == CubeColor.WHITE || piece2.getTile(CubeFace.R).getColor() == CubeColor.WHITE) {
                            frontToBottom(1, 1, -1);
                        } else {
                            topToFront(-1, 1, -1);
                        }
                    } else {
                        frontToBottom(-1, 1, -1);
                    }
                } else {
                    if (piece2.getTile(CubeFace.U).getColor() == CubeColor.WHITE) {
                        topToFront(1, 1, -1);
                    } else {
                        frontToBottom(1, 1, -1);
                    }
                }
            } else {
                CubePiece piece = workingCube.pieceAt(1, 1, 1);
                if (piece.containsColor(CubeColor.WHITE)) {
                    moveAndAdd("U");
                } else {
                    piece = workingCube.pieceAt(-1, 1, 1);
                    if (piece.containsColor(CubeColor.WHITE)) {
                        moveAndAdd("U'");
                    } else {
                        piece = workingCube.pieceAt(-1, -1, -1);
                        if (cornerUnsolved((CornerPiece) piece)) {
                            moveAndAdd("L'", "U'", "L");
                        } else {
                            piece = workingCube.pieceAt(1, -1, -1);
                            if (cornerUnsolved((CornerPiece) piece)) {
                                moveAndAdd("R", "U", "R'");
                            } else {
                                piece = workingCube.pieceAt(1, -1, 1);
                                if (cornerUnsolved((CornerPiece) piece)) {
                                    moveAndAdd("y", "R", "U", "R'");
                                } else {
                                    piece = workingCube.pieceAt(-1, -1, 1);
                                    if (cornerUnsolved((CornerPiece) piece)) {
                                        moveAndAdd("y'", "L'", "U'", "L");
                                    }
                                }
                            }
                        }
                    }
                }
            }
            faceF = workingCube.getColorsOfFace(CubeFace.F);
            faceB = workingCube.getColorsOfFace(CubeFace.B);
            faceR = workingCube.getColorsOfFace(CubeFace.R);
            faceL = workingCube.getColorsOfFace(CubeFace.L);
        }
    }

    private void topToFront(int x, int y, int z) throws IllegalArgumentException {
        if (x == 0 || z != -1 || y != 1) {
            throw new IllegalArgumentException();
        }
        if (cornerUnsolved((CornerPiece) workingCube.pieceAt(x, -1, -1))) {
            if (x == -1) {
                moveAndAdd("L'", "U2", "L");
            } else {
                moveAndAdd("R", "U2", "R'");
            }
        } else if (cornerUnsolved((CornerPiece) workingCube.pieceAt(x * -1, -1, -1))) {
            if (x == -1) {
                moveAndAdd("U'");
            } else {
                moveAndAdd("U");
            }
            topToFront(x * -1, 1, -1);
        } else if (cornerUnsolved((CornerPiece) workingCube.pieceAt(x, -1, 1))) {
            if (x == -1) {
                moveAndAdd("d");
            } else {
                moveAndAdd("d'");
            }
            topToFront(x, y, z);
        } else if (cornerUnsolved((CornerPiece) workingCube.pieceAt(x * -1, -1, 1))) {
            moveAndAdd("d2");
            topToFront(x, y, z);
        }
    }

    private void frontToBottom(int x, int y, int z) throws IllegalArgumentException {
        CornerPiece piece = (CornerPiece) workingCube.pieceAt(x, y, z);
        if (x == 0 || z != -1 || y != 1) {
            throw new IllegalArgumentException();
        }
        if (x == -1 && piece.getTile(CubeFace.L).getColor() != CubeColor.WHITE) {
            moveAndAdd("U'");
            frontToBottom(1, 1, -1);
        } else if (x == 1 && piece.getTile(CubeFace.R).getColor() != CubeColor.WHITE) {
            moveAndAdd("U");
            frontToBottom(-1, 1, -1);
        } else if (piece.getTile(CubeFace.F).getColor() == workingCube.getColorsOfFace(CubeFace.F)[1][1]) {
            if (x == -1) {
                moveAndAdd("L'", "U'", "L");
            } else {
                moveAndAdd("R", "U", "R'");
            }
        } else if (piece.getTile(CubeFace.F).getColor() == workingCube.getColorsOfFace(CubeFace.B)[1][1]) {
            moveAndAdd("d2");
            frontToBottom(x, y, z);
        } else if (piece.getTile(CubeFace.F).getColor() == workingCube.getColorsOfFace(CubeFace.R)[1][1]) {
            moveAndAdd("d'");
            frontToBottom(x, y, z);
        } else if (piece.getTile(CubeFace.F).getColor() == workingCube.getColorsOfFace(CubeFace.L)[1][1]) {
            moveAndAdd("d");
            frontToBottom(x, y, z);
        }
    }

    private boolean cornerUnsolved(CornerPiece piece) {
        return piece.getTile(0).getColor() != workingCube.getColorsOfFace(piece.getTile(0).getFace())[1][1]
                || piece.getTile(1).getColor() != workingCube.getColorsOfFace(piece.getTile(1).getFace())[1][1]
                || piece.getTile(2).getColor() != workingCube.getColorsOfFace(piece.getTile(2).getFace())[1][1];
    }

    private void middleEdges() {
        solveSet.add("\nMIDDLE EDGES\n");
        EdgePiece flPiece = (EdgePiece) workingCube.pieceAt(-1, 0, -1); //front left
        EdgePiece frPiece = (EdgePiece) workingCube.pieceAt(1, 0, -1);
        EdgePiece blPiece = (EdgePiece) workingCube.pieceAt(-1, 0, 1); //back right
        EdgePiece brPiece = (EdgePiece) workingCube.pieceAt(1, 0, 1);
        while (edgeUnsolved(flPiece) || edgeUnsolved(frPiece) || edgeUnsolved(blPiece) || edgeUnsolved(brPiece)) {
            EdgePiece fPiece = (EdgePiece) workingCube.pieceAt(0, 1, -1); //top front
            EdgePiece lPiece = (EdgePiece) workingCube.pieceAt(-1, 1, 0);
            EdgePiece rPiece = (EdgePiece) workingCube.pieceAt(1, 1, 0); //top right
            EdgePiece bPiece = (EdgePiece) workingCube.pieceAt(0, 1, 1);
            if (noYellow(fPiece) || noYellow(lPiece) || noYellow(rPiece) || noYellow(bPiece)) {
                if (!noYellow(fPiece)) {
                    if (noYellow(rPiece)) {
                        moveAndAdd("U");
                    } else if (noYellow(lPiece)) {
                        moveAndAdd("U'");
                    } else {
                        moveAndAdd("U2");
                    }
                }
                fPiece = (EdgePiece) workingCube.pieceAt(0, 1, -1);
                if (fPiece.getTile(CubeFace.F).getColor() != workingCube.getColorsOfFace(CubeFace.F)[1][1]) {
                    if (fPiece.getTile(CubeFace.F).getColor() == workingCube.getColorsOfFace(CubeFace.R)[1][1]) {
                        moveAndAdd("d'");
                    } else if (fPiece.getTile(CubeFace.F).getColor() == workingCube.getColorsOfFace(CubeFace.L)[1][1]) {
                        moveAndAdd("d");
                    } else {
                        moveAndAdd("d2");
                    }
                }
                if (fPiece.getTile(CubeFace.U).getColor() == workingCube.getColorsOfFace(CubeFace.R)[1][1]) {
                    moveAndAdd("U", "R", "U", "R'", "d'", "L'", "U'", "L");
                } else {
                    moveAndAdd("U'", "L'", "U'", "L", "d", "R", "U", "R'");
                }
            } else if (edgeUnsolved(frPiece) && noYellow(frPiece)) {
                moveAndAdd("R", "U", "R'", "d'", "L'", "U'", "L");
            } else if (edgeUnsolved(flPiece) && noYellow(flPiece)) {
                moveAndAdd("L'", "U'", "L", "d", "R", "U", "R'");
            } else if (edgeUnsolved(brPiece) && noYellow(brPiece)) {
                moveAndAdd("y", "R", "U", "R'", "d'", "L'", "U'", "L");
            } else if (edgeUnsolved(blPiece) && noYellow(blPiece)) {
                moveAndAdd("y'", "R", "U", "R'", "d'", "L'", "U'", "L");
            }
            flPiece = (EdgePiece) workingCube.pieceAt(-1, 0, -1); //front left
            frPiece = (EdgePiece) workingCube.pieceAt(1, 0, -1);
            blPiece = (EdgePiece) workingCube.pieceAt(-1, 0, 1); //back right
            brPiece = (EdgePiece) workingCube.pieceAt(1, 0, 1);
        }

    }

    private boolean edgeUnsolved(EdgePiece piece) {
        return piece.getTile(0).getColor() != workingCube.getColorsOfFace(piece.getTile(0).getFace())[1][1]
                || piece.getTile(1).getColor() != workingCube.getColorsOfFace(piece.getTile(1).getFace())[1][1];
    }

    private boolean noYellow(EdgePiece piece) {
        return piece.getTile(0).getColor() != CubeColor.YELLOW
                && piece.getTile(1).getColor() != CubeColor.YELLOW;
    }

    private void oll() {
        furUrf();
        outTwistInTwist();
    }

    private void furUrf() {
        solveSet.add("\nFUR URF\n");
        while (!yellowCross()) {
            CubeColor[][] top = workingCube.getColorsOfFace(CubeFace.U);
            if (top[0][1] == CubeColor.YELLOW && top[1][2] == CubeColor.YELLOW) {
                moveAndAdd("y'");
            } else if (top[1][2] == CubeColor.YELLOW && top[2][1] == CubeColor.YELLOW) {
                moveAndAdd("y2");
            } else if (top[2][1] == CubeColor.YELLOW && top[1][0] == CubeColor.YELLOW) {
                moveAndAdd("y");
            }
            moveAndAdd("F", "U", "R", "U'", "R'", "F'");
        }
    }

    private boolean yellowCross() {
        CubeColor[][] top = workingCube.getColorsOfFace(CubeFace.U);
        return top[1][1] == CubeColor.YELLOW && top[0][1] == CubeColor.YELLOW && top[1][2] == CubeColor.YELLOW && top[2][1] == CubeColor.YELLOW && top[1][0] == CubeColor.YELLOW;
    }

    private void outTwistInTwist() {
        solveSet.add("\nOUT TWIST IN TWIST\n");
        while (!yellowSolved()) {
            if (oneYellowCorner()) {
                CubeColor[][] top = workingCube.getColorsOfFace(CubeFace.U);
                if (top[0][0] == CubeColor.YELLOW) {
                    moveAndAdd("y'");
                } else if (top[0][2] == CubeColor.YELLOW) {
                    moveAndAdd("y2");
                } else if (top[2][2] == CubeColor.YELLOW) {
                    moveAndAdd("y");
                }
            } else if (workingCube.getColorsOfFace(CubeFace.F)[0][0] != CubeColor.YELLOW) {
                if (workingCube.getColorsOfFace(CubeFace.R)[0][0] == CubeColor.YELLOW) {
                    moveAndAdd("y");
                } else if (workingCube.getColorsOfFace(CubeFace.L)[0][0] == CubeColor.YELLOW) {
                    moveAndAdd("y'");
                } else {
                    moveAndAdd("y2");
                }
            }
            moveAndAdd("R", "U", "R'", "U", "R", "U2", "R'");
        }
    }

    private boolean yellowSolved() {
        CubeColor[][] top = workingCube.getColorsOfFace(CubeFace.U);
        for (CubeColor[] row : top) {
            for (CubeColor color : row) {
                if (color != CubeColor.YELLOW) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean oneYellowCorner() {
        CubeColor[][] top = workingCube.getColorsOfFace(CubeFace.U);
        return (top[0][0] == CubeColor.YELLOW ^ top[0][2] == CubeColor.YELLOW ^ top[2][0] == CubeColor.YELLOW ^ top[2][2] == CubeColor.YELLOW)
                && ((top[0][0] == CubeColor.YELLOW && top[0][2] == CubeColor.YELLOW) == (top[2][0] == CubeColor.YELLOW && top[2][2] == CubeColor.YELLOW));
    }

    private void pll() {
        rPrimeF();
        ffu();
    }

    private void rPrimeF() {
        solveSet.add("\nR PRIME F\n");
        while (!topCornersMatched()) {
            if (frontMatched()) {
                CubeColor c = workingCube.getColorsOfFace(CubeFace.F)[0][0];
                if (c == workingCube.getColorsOfFace(CubeFace.F)[1][1]) {
                    moveAndAdd("y2");
                } else if (c == workingCube.getColorsOfFace(CubeFace.R)[1][1]) {
                    moveAndAdd("U'", "y'");
                } else if (c == workingCube.getColorsOfFace(CubeFace.L)[1][1]) {
                    moveAndAdd("U", "y");
                } else {
                    moveAndAdd("U2");
                }
            } else if (rightMatched()) {
                CubeColor c = workingCube.getColorsOfFace(CubeFace.R)[0][0];
                if (c == workingCube.getColorsOfFace(CubeFace.F)[1][1]) {
                    moveAndAdd("U", "y2");
                } else if (c == workingCube.getColorsOfFace(CubeFace.R)[1][1]) {
                    moveAndAdd("y'");
                } else if (c == workingCube.getColorsOfFace(CubeFace.L)[1][1]) {
                    moveAndAdd("U2", "y'");
                } else {
                    moveAndAdd("U'");
                }
            } else if (leftMatched()) {
                CubeColor c = workingCube.getColorsOfFace(CubeFace.L)[0][0];
                if (c == workingCube.getColorsOfFace(CubeFace.F)[1][1]) {
                    moveAndAdd("U'", "y2");
                } else if (c == workingCube.getColorsOfFace(CubeFace.R)[1][1]) {
                    moveAndAdd("U2", "y'");
                } else if (c == workingCube.getColorsOfFace(CubeFace.L)[1][1]) {
                    moveAndAdd("y");
                } else {
                    moveAndAdd("U");
                }
            } else if (backMatched()) {
                CubeColor c = workingCube.getColorsOfFace(CubeFace.B)[2][2];
                if (c == workingCube.getColorsOfFace(CubeFace.F)[1][1]) {
                    moveAndAdd("d2");
                } else if (c == workingCube.getColorsOfFace(CubeFace.R)[1][1]) {
                    moveAndAdd("d");
                } else if (c == workingCube.getColorsOfFace(CubeFace.L)[1][1]) {
                    moveAndAdd("d'");
                }
            }
            moveAndAdd("R'", "F", "R'", "B2", "R", "F'", "R'", "B2", "R2");
        }
    }

    private boolean topCornersMatched() {
        return frontMatched() && rightMatched() && leftMatched() && backMatched();
    }

    private boolean frontMatched() {
        CubePiece fl = workingCube.pieceAt(-1, 1, -1);
        CubePiece fr = workingCube.pieceAt(1, 1, -1);
        return fl.getTile(CubeFace.F).getColor() == fr.getTile(CubeFace.F).getColor();
    }

    private boolean rightMatched() {
        CubePiece fr = workingCube.pieceAt(1, 1, -1);
        CubePiece br = workingCube.pieceAt(1, 1, 1);
        return fr.getTile(CubeFace.R).getColor() == br.getTile(CubeFace.R).getColor();
    }

    private boolean leftMatched() {
        CubePiece bl = workingCube.pieceAt(-1, 1, 1);
        CubePiece fl = workingCube.pieceAt(-1, 1, -1);
        return bl.getTile(CubeFace.L).getColor() == fl.getTile(CubeFace.L).getColor();
    }

    private boolean backMatched() {
        CubePiece br = workingCube.pieceAt(1, 1, 1);
        CubePiece bl = workingCube.pieceAt(-1, 1, 1);
        return br.getTile(CubeFace.B).getColor() == bl.getTile(CubeFace.B).getColor();
    }

    private void ffu() {
        solveSet.add("\nFFU\n");
        CubeColor c = workingCube.getColorsOfFace(CubeFace.F)[0][0];
        if (c == workingCube.getColorsOfFace(CubeFace.R)[1][1]) {
            moveAndAdd("d'");
        } else if (c == workingCube.getColorsOfFace(CubeFace.L)[1][1]) {
            moveAndAdd("d");
        } else if (c == workingCube.getColorsOfFace(CubeFace.B)[1][1]) {
            moveAndAdd("d2");
        }
        while (!workingCube.isSolved()) {
            CubeFace solid = solidTopRow();
            if (solid == CubeFace.F) {
                moveAndAdd("y2");
            } else if (solid == CubeFace.R) {
                moveAndAdd("y'");
            } else if (solid == CubeFace.L) {
                moveAndAdd("y");
            }
            String u = "U";
            if (workingCube.getColorsOfFace(CubeFace.F)[0][1] == workingCube.getColorsOfFace(CubeFace.R)[1][1]) {
                u += "'";
            }
            moveAndAdd("F2", u, "v'", "F2", "v", u, "F2");
        }
    }

    private CubeFace solidTopRow() {
        CubeColor[][] f = workingCube.getColorsOfFace(CubeFace.F);
        CubeColor[][] r = workingCube.getColorsOfFace(CubeFace.R);
        CubeColor[][] l = workingCube.getColorsOfFace(CubeFace.L);
        CubeColor[][] b = workingCube.getColorsOfFace(CubeFace.B);
        if (f[0][0] == f[0][1] && f[0][1] == f[0][2]) {
            return CubeFace.F;
        }
        if (r[0][0] == r[0][1] && r[0][1] == r[0][2]) {
            return CubeFace.R;
        }
        if (l[0][0] == l[0][1] && l[0][1] == l[0][2]) {
            return CubeFace.L;
        }
        if (b[0][0] == b[0][1] && b[0][1] == b[0][2]) {
            return CubeFace.B;
        }
        return null;
    }

    private void updateAverageMoves(int numMoves) {
        int numRecords = getNumRecords();
        int totalRecords = numRecords + 1;
        double newAverage = getAverageMoves() * numRecords / totalRecords + numMoves * 1.0 / totalRecords;
        Formatter output = null;
        try {
            output = new Formatter(file);
        } catch (FileNotFoundException e) {
        }
        output.format("%d%n", totalRecords);
        output.format("%f", newAverage);
        if (output != null) {
            output.close();
        }
    }

    private int getNumRecords() {
        Scanner fileInput = null;
        try {
            fileInput = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
        }
        return Integer.parseInt(fileInput.nextLine());
    }

    private double getAverageMoves() {
        Scanner fileInput = null;
        try {
            fileInput = new Scanner(new File(file));
        } catch (FileNotFoundException ex) {
        }
        fileInput.nextLine();
        return Double.parseDouble(fileInput.nextLine());
    }
}
