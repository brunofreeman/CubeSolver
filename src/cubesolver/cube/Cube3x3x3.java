package cubesolver.cube;

import cubesolver.cube.piece.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Bruno Freeman
 */
public final class Cube3x3x3 {

    //<editor-fold defaultstate="collapsed" desc="comment">
    /**
     * ***** Naming Key ********* * ******** Pieces ********** * cen = center *
     * e = edge * c = corner * * ******** Colors ********** * W = white * Y =
     * yellow * B = blue * G = green * R = red * O = orange * *
     * ******************************
     */

    /* cenW cenY cenB cenG cenR cenO eWB eWG eWR eWO eYB eYG eYR eYO eBR eRG eGO eOB cWBR cWRG cWGO cWOB cYBR cYRG cYGO cYOB */
    /**
     * The default cube orientation is yellow = up, white = down, orange = left,
     * red = right, blue = front, green = back. Pieces placed based on an XYZ
     * system where the absolute center of cube is (0, 0, 0). NOTE: (0, 0, 0)
     * isn't actually a piece.
     */
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="comment">
    final private CenterPiece cenW = new CenterPiece(new CubeTile(CubeColor.WHITE));
    final private CenterPiece cenY = new CenterPiece(new CubeTile(CubeColor.YELLOW));
    final private CenterPiece cenB = new CenterPiece(new CubeTile(CubeColor.BLUE));
    final private CenterPiece cenG = new CenterPiece(new CubeTile(CubeColor.GREEN));
    final private CenterPiece cenR = new CenterPiece(new CubeTile(CubeColor.RED));
    final private CenterPiece cenO = new CenterPiece(new CubeTile(CubeColor.ORANGE));

    final private EdgePiece eWB = new EdgePiece(new CubeTile(CubeColor.WHITE), new CubeTile(CubeColor.BLUE));
    final private EdgePiece eWG = new EdgePiece(new CubeTile(CubeColor.WHITE), new CubeTile(CubeColor.GREEN));
    final private EdgePiece eWR = new EdgePiece(new CubeTile(CubeColor.WHITE), new CubeTile(CubeColor.RED));
    final private EdgePiece eWO = new EdgePiece(new CubeTile(CubeColor.WHITE), new CubeTile(CubeColor.ORANGE));
    final private EdgePiece eYB = new EdgePiece(new CubeTile(CubeColor.YELLOW), new CubeTile(CubeColor.BLUE));
    final private EdgePiece eYG = new EdgePiece(new CubeTile(CubeColor.YELLOW), new CubeTile(CubeColor.GREEN));
    final private EdgePiece eYR = new EdgePiece(new CubeTile(CubeColor.YELLOW), new CubeTile(CubeColor.RED));
    final private EdgePiece eYO = new EdgePiece(new CubeTile(CubeColor.YELLOW), new CubeTile(CubeColor.ORANGE));
    final private EdgePiece eBR = new EdgePiece(new CubeTile(CubeColor.BLUE), new CubeTile(CubeColor.RED));
    final private EdgePiece eRG = new EdgePiece(new CubeTile(CubeColor.RED), new CubeTile(CubeColor.GREEN));
    final private EdgePiece eGO = new EdgePiece(new CubeTile(CubeColor.GREEN), new CubeTile(CubeColor.ORANGE));
    final private EdgePiece eOB = new EdgePiece(new CubeTile(CubeColor.ORANGE), new CubeTile(CubeColor.BLUE));

    final private CornerPiece cWBR = new CornerPiece(new CubeTile(CubeColor.WHITE), new CubeTile(CubeColor.BLUE), new CubeTile(CubeColor.RED));
    final private CornerPiece cWRG = new CornerPiece(new CubeTile(CubeColor.WHITE), new CubeTile(CubeColor.RED), new CubeTile(CubeColor.GREEN));
    final private CornerPiece cWGO = new CornerPiece(new CubeTile(CubeColor.WHITE), new CubeTile(CubeColor.GREEN), new CubeTile(CubeColor.ORANGE));
    final private CornerPiece cWOB = new CornerPiece(new CubeTile(CubeColor.WHITE), new CubeTile(CubeColor.ORANGE), new CubeTile(CubeColor.BLUE));
    final private CornerPiece cYBR = new CornerPiece(new CubeTile(CubeColor.YELLOW), new CubeTile(CubeColor.BLUE), new CubeTile(CubeColor.RED));
    final private CornerPiece cYRG = new CornerPiece(new CubeTile(CubeColor.YELLOW), new CubeTile(CubeColor.RED), new CubeTile(CubeColor.GREEN));
    final private CornerPiece cYGO = new CornerPiece(new CubeTile(CubeColor.YELLOW), new CubeTile(CubeColor.GREEN), new CubeTile(CubeColor.ORANGE));
    final private CornerPiece cYOB = new CornerPiece(new CubeTile(CubeColor.YELLOW), new CubeTile(CubeColor.ORANGE), new CubeTile(CubeColor.BLUE));

    final private HashMap<CubePiece, PiecePosition> cubeState;

    final private static String[] VALID_MOVES = { /* no modifier = clockwise, ' = counterclockwise or inverted, 2 = twice or a 180 */
        "R", "R'", "R2", //R = right face
        "r", "r'", "r2", //r = R + V

        "L", "L'", "L2", //L = left face
        "l", "l'", "l2", //l = L + V'

        "U", "U'", "U2", //U = up face
        "u", "u'", "u2", //u = U + H

        "D", "D'", "D2", //D = down face
        "d", "d'", "d2", //d = D + H'

        "F", "F'", "F2", //F = front face
        "f", "f'", "f2", //f = F + C

        "B", "B'", "B2", //B = back face
        "b", "b'", "b2", //b = B + C'

        "V", "V'", "V2", //V = vertical slice (between R & L), turned in direction of R
        "v", "v'", "v2", //v = R + L'

        "H", "H'", "H2", //H = horizontal slice (between U & D), turned in direction of U
        "h", "h'", "h2", //h = U + D'

        "C", "C'", "C2", //C = central slice (between F & B), turned in direction of F
        "c", "c'", "c2", //c = F + B'

        "x", "x'", "x2", //x = rotation through x-axis
        "y", "y'", "y2", //y = rotation through y-axis
        "z", "z'", "z2" //z = rotation through z-aixs
    };

    final public static int NUM_PIECES = 26;
//</editor-fold>

    public Cube3x3x3() { //default constructor places the pieces in a solved state, blue in front, yellow on top
        cubeState = new HashMap<>();
        setToDefault();
    }

    public Cube3x3x3(CubeColor front, CubeColor up) { //creates a solved cube with desired color in front and on top
        cubeState = new HashMap<>();
        setToDefault();
        orientateCube(front, up);
    }

    public Cube3x3x3(Cube3x3x3 other) {
        cubeState = new HashMap<>();
        setWithText(other.getCubeStateAsText());
    }

    public Cube3x3x3(HashMap<? extends Object, ? extends Object> cubeState) {
        this.cubeState = new HashMap<>();
        if (cubeState.keySet().toArray()[0] instanceof CubePiece && cubeState.entrySet().toArray()[0] instanceof PiecePosition) {
            if (cubeState.size() != NUM_PIECES) {
                throw new IllegalArgumentException("Invalid cube state: not enough pieces");
            }
            for (int i = 0; i < NUM_PIECES; i++) {
                this.cubeState.put((CubePiece) cubeState.keySet().toArray()[i], (PiecePosition) cubeState.entrySet().toArray()[i]);
            }
        } else if (cubeState.keySet().toArray()[0] instanceof CubeFace && cubeState.get(cubeState.keySet().toArray()[0]) instanceof CubeColor[][]) {//CubeColor[][] indicies are coords from top left (0,0) to bottom right (2,2) in relation to y rotation or x rotation from F face in most direct path
            HashMap<CubeFace, CubeColor[][]> castedCubeState = new HashMap<>();
            for (Object face : cubeState.keySet()) {
                castedCubeState.put((CubeFace) face, (CubeColor[][]) cubeState.get(face));
            }
            setWithColorArrays(castedCubeState);
        } else {
            throw new IllegalArgumentException("HashMap with unsuitable types passed to constructor");
        }
    }

    public Cube3x3x3(String[] cubeState) { //same format as getCubeStateAsText
        this.cubeState = new HashMap<>();
        setWithText(cubeState);
    }

    public void setToDefault() {
        for (CubePiece piece : cubeState.keySet()) {
            piece.resetTiles();
        }
        cubeState.clear();
        cubeState.put(cWOB, new PiecePosition(-1, -1, -1));
        cubeState.put(eWB, new PiecePosition(0, -1, -1));
        cubeState.put(cWBR, new PiecePosition(1, -1, -1));
        cubeState.put(eOB, new PiecePosition(-1, 0, -1));
        cubeState.put(cenB, new PiecePosition(0, 0, -1));
        cubeState.put(eBR, new PiecePosition(1, 0, -1));
        cubeState.put(cYOB, new PiecePosition(-1, 1, -1));
        cubeState.put(eYB, new PiecePosition(0, 1, -1));
        cubeState.put(cYBR, new PiecePosition(1, 1, -1));
        cubeState.put(eWO, new PiecePosition(-1, -1, 0));
        cubeState.put(cenW, new PiecePosition(0, -1, 0));
        cubeState.put(eWR, new PiecePosition(1, -1, 0));
        cubeState.put(cenO, new PiecePosition(-1, 0, 0));
        cubeState.put(cenR, new PiecePosition(1, 0, 0));
        cubeState.put(eYO, new PiecePosition(-1, 1, 0));
        cubeState.put(cenY, new PiecePosition(0, 1, 0));
        cubeState.put(eYR, new PiecePosition(1, 1, 0));
        cubeState.put(cWGO, new PiecePosition(-1, -1, 1));
        cubeState.put(eWG, new PiecePosition(0, -1, 1));
        cubeState.put(cWRG, new PiecePosition(1, -1, 1));
        cubeState.put(eGO, new PiecePosition(-1, 0, 1));
        cubeState.put(cenG, new PiecePosition(0, 0, 1));
        cubeState.put(eRG, new PiecePosition(1, 0, 1));
        cubeState.put(cYGO, new PiecePosition(-1, 1, 1));
        cubeState.put(eYG, new PiecePosition(0, 1, 1));
        cubeState.put(cYRG, new PiecePosition(1, 1, 1));
        //cubeState.put( NONE, new PiecePosition(  0,  0,  0 ));
    }

    public void setWithText(String[] cubeState) { //validates everything except impossible states, must be in format of getCubeStateAsText() output
        boolean fromConstructor = this.cubeState.isEmpty();
        HashMap<CubePiece, PiecePosition> tempCubeState = new HashMap<>();
        for (String pieceInfo : cubeState) { //relies on order of CubeColor arguments in CubePiece declarations NOT BE CHANGED
            CubePiece piece = null;
            try {
                switch (pieceInfo.substring(0, 6).trim()) {
                    case "center":
                        piece = getPieceWithColors(pieceInfo.substring(10, 16).trim());
                        piece.getTile(0).setFace(CubeFace.valueOf(pieceInfo.substring(17, 18)));
                        break;
                    case "edge":
                        piece = getPieceWithColors(pieceInfo.substring(10, 16).trim(), pieceInfo.substring(20, 26).trim());
                        piece.getTile(0).setFace(CubeFace.valueOf(pieceInfo.substring(17, 18)));
                        piece.getTile(1).setFace(CubeFace.valueOf(pieceInfo.substring(27, 28)));
                        break;
                    case "corner":
                        piece = getPieceWithColors(pieceInfo.substring(10, 16).trim(), pieceInfo.substring(20, 26).trim(), pieceInfo.substring(30, 36).trim());
                        piece.getTile(0).setFace(CubeFace.valueOf(pieceInfo.substring(17, 18)));
                        piece.getTile(1).setFace(CubeFace.valueOf(pieceInfo.substring(27, 28)));
                        piece.getTile(2).setFace(CubeFace.valueOf(pieceInfo.substring(37, 38)));
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Cube state text in improper format");
            }
            if (piece == null) {
                throw new IllegalArgumentException("Cube state text in improper format");
            }
            try {
                int x = Integer.parseInt(pieceInfo.substring(43, 45).trim());
                int y = Integer.parseInt(pieceInfo.substring(46, 48).trim());
                int z = Integer.parseInt(pieceInfo.substring(49, 51).trim());
                boolean validCoords = true;
                switch (pieceInfo.substring(0, 6).trim()) {
                    case "center":
                        if (!(x != 0 ^ y != 0 ^ z != 0)) {
                            validCoords = false;
                        }
                        break;
                    case "edge":
                        if (!(x == 0 ^ y == 0 ^ z == 0)) {
                            validCoords = false;
                        }
                        break;
                    case "corner":
                        if (x == 0 || y == 0 || z == 0) {
                            validCoords = false;
                        }
                }
                if (validCoords) {
                    tempCubeState.put(piece, new PiecePosition(x, y, z));
                } else {
                    throw new IllegalArgumentException("Cube state text indicates impossible coordinates");
                    //errorMessage(8, pieceInfo);
                    //break;
                }
            } catch (StringIndexOutOfBoundsException e) {
                throw new IllegalArgumentException("Cube state text is in improper format");
            }
        }
        Cube3x3x3 compareCube = new Cube3x3x3();
        HashMap<CubePiece, PiecePosition> compareState = compareCube.getCubeState();
        if (tempCubeState.size() == NUM_PIECES && tempCubeState.keySet().containsAll(compareState.keySet()) && tempCubeState.values().containsAll(compareState.values())) {
            if (possibleState(tempCubeState)) {
                if (fromConstructor) {
                    this.cubeState.putAll(tempCubeState);
                } else {
                    for (CubePiece piece : this.cubeState.keySet()) {
                        piece.resetTiles();
                    }
                    this.cubeState.clear();
                    this.cubeState.putAll(tempCubeState); //tempstate does nothing? reset tiles why?
                }
            } /*else if (fromConstructor) {
                setToDefault();
                errorMessage(6);
            }*/ else {
                //errorMessage(5);
                throw new IllegalArgumentException("Cube state text indicates impossible state");
            }
        } /*else if (fromConstructor) {
            setToDefault();
            errorMessage(3);
        }*/ else {
            //errorMessage(4);
            throw new IllegalArgumentException("Cube state text does not include all pieces once and only once");
        }
    }

    public void setWithColorArrays(HashMap<CubeFace, CubeColor[][]> cubeState) {
        //boolean fromConstructor = this.cubeState.isEmpty();
        boolean faceArraysCorrectSize = true;
        for (CubeColor[][] faceColors : cubeState.values()) {
            if (!(faceColors.length == 3 && faceColors.length == 3)) {
                faceArraysCorrectSize = false;
            }
        }
        if (cubeState.size() == 6 && cubeState.keySet().containsAll(Arrays.asList(CubeFace.F, CubeFace.B, CubeFace.R, CubeFace.L, CubeFace.U, CubeFace.D)) && faceArraysCorrectSize && !cubeState.values().contains(null)) {
            putPieceFromColorsAndFaces(new CubeColor[]{cubeState.get(CubeFace.F)[2][0], cubeState.get(CubeFace.L)[2][2], cubeState.get(CubeFace.D)[0][0]}, new CubeFace[]{CubeFace.F, CubeFace.L, CubeFace.D}, -1, -1, -1);
            putPieceFromColorsAndFaces(new CubeColor[]{cubeState.get(CubeFace.F)[2][1], cubeState.get(CubeFace.D)[0][1]}, new CubeFace[]{CubeFace.F, CubeFace.D}, 0, -1, -1);
            putPieceFromColorsAndFaces(new CubeColor[]{cubeState.get(CubeFace.F)[2][2], cubeState.get(CubeFace.R)[2][0], cubeState.get(CubeFace.D)[0][2]}, new CubeFace[]{CubeFace.F, CubeFace.R, CubeFace.D}, 1, -1, -1);
            putPieceFromColorsAndFaces(new CubeColor[]{cubeState.get(CubeFace.F)[1][0], cubeState.get(CubeFace.L)[1][2]}, new CubeFace[]{CubeFace.F, CubeFace.L}, -1, 0, -1);
            putPieceFromColorsAndFaces(new CubeColor[]{cubeState.get(CubeFace.F)[1][1]}, new CubeFace[]{CubeFace.F}, 0, 0, -1);
            putPieceFromColorsAndFaces(new CubeColor[]{cubeState.get(CubeFace.F)[1][2], cubeState.get(CubeFace.R)[1][0]}, new CubeFace[]{CubeFace.F, CubeFace.R}, 1, 0, -1);
            putPieceFromColorsAndFaces(new CubeColor[]{cubeState.get(CubeFace.F)[0][0], cubeState.get(CubeFace.L)[0][2], cubeState.get(CubeFace.U)[2][0]}, new CubeFace[]{CubeFace.F, CubeFace.L, CubeFace.U}, -1, 1, -1);
            putPieceFromColorsAndFaces(new CubeColor[]{cubeState.get(CubeFace.F)[0][1], cubeState.get(CubeFace.U)[2][1]}, new CubeFace[]{CubeFace.F, CubeFace.U}, 0, 1, -1);
            putPieceFromColorsAndFaces(new CubeColor[]{cubeState.get(CubeFace.F)[0][2], cubeState.get(CubeFace.R)[0][0], cubeState.get(CubeFace.U)[2][2]}, new CubeFace[]{CubeFace.F, CubeFace.R, CubeFace.U}, 1, 1, -1);
            putPieceFromColorsAndFaces(new CubeColor[]{cubeState.get(CubeFace.L)[2][1], cubeState.get(CubeFace.D)[1][0]}, new CubeFace[]{CubeFace.L, CubeFace.D}, -1, -1, 0);
            putPieceFromColorsAndFaces(new CubeColor[]{cubeState.get(CubeFace.D)[1][1]}, new CubeFace[]{CubeFace.D}, 0, -1, 0);
            putPieceFromColorsAndFaces(new CubeColor[]{cubeState.get(CubeFace.D)[1][2], cubeState.get(CubeFace.R)[2][1]}, new CubeFace[]{CubeFace.D, CubeFace.R}, 1, -1, 0);
            putPieceFromColorsAndFaces(new CubeColor[]{cubeState.get(CubeFace.L)[1][1]}, new CubeFace[]{CubeFace.L}, -1, 0, 0);
            putPieceFromColorsAndFaces(new CubeColor[]{cubeState.get(CubeFace.R)[1][1]}, new CubeFace[]{CubeFace.R}, 1, 0, 0);
            putPieceFromColorsAndFaces(new CubeColor[]{cubeState.get(CubeFace.L)[0][1], cubeState.get(CubeFace.U)[1][0]}, new CubeFace[]{CubeFace.L, CubeFace.U}, -1, 1, 0);
            putPieceFromColorsAndFaces(new CubeColor[]{cubeState.get(CubeFace.U)[1][1]}, new CubeFace[]{CubeFace.U}, 0, 1, 0);
            putPieceFromColorsAndFaces(new CubeColor[]{cubeState.get(CubeFace.U)[1][2], cubeState.get(CubeFace.R)[0][1]}, new CubeFace[]{CubeFace.U, CubeFace.R}, 1, 1, 0);
            putPieceFromColorsAndFaces(new CubeColor[]{cubeState.get(CubeFace.L)[2][0], cubeState.get(CubeFace.D)[2][0], cubeState.get(CubeFace.B)[0][0]}, new CubeFace[]{CubeFace.L, CubeFace.D, CubeFace.B}, -1, -1, 1);
            putPieceFromColorsAndFaces(new CubeColor[]{cubeState.get(CubeFace.B)[0][1], cubeState.get(CubeFace.D)[2][1]}, new CubeFace[]{CubeFace.B, CubeFace.D}, 0, -1, 1);
            putPieceFromColorsAndFaces(new CubeColor[]{cubeState.get(CubeFace.B)[0][2], cubeState.get(CubeFace.D)[2][2], cubeState.get(CubeFace.R)[2][2]}, new CubeFace[]{CubeFace.B, CubeFace.D, CubeFace.R}, 1, -1, 1);
            putPieceFromColorsAndFaces(new CubeColor[]{cubeState.get(CubeFace.L)[1][0], cubeState.get(CubeFace.B)[1][0]}, new CubeFace[]{CubeFace.L, CubeFace.B}, -1, 0, 1);
            putPieceFromColorsAndFaces(new CubeColor[]{cubeState.get(CubeFace.B)[1][1]}, new CubeFace[]{CubeFace.B}, 0, 0, 1);
            putPieceFromColorsAndFaces(new CubeColor[]{cubeState.get(CubeFace.B)[1][2], cubeState.get(CubeFace.R)[1][2]}, new CubeFace[]{CubeFace.B, CubeFace.R}, 1, 0, 1);
            putPieceFromColorsAndFaces(new CubeColor[]{cubeState.get(CubeFace.L)[0][0], cubeState.get(CubeFace.U)[0][0], cubeState.get(CubeFace.B)[2][0]}, new CubeFace[]{CubeFace.L, CubeFace.U, CubeFace.B}, -1, 1, 1);
            putPieceFromColorsAndFaces(new CubeColor[]{cubeState.get(CubeFace.U)[0][1], cubeState.get(CubeFace.B)[2][1]}, new CubeFace[]{CubeFace.U, CubeFace.B}, 0, 1, 1);
            putPieceFromColorsAndFaces(new CubeColor[]{cubeState.get(CubeFace.U)[0][2], cubeState.get(CubeFace.B)[2][2], cubeState.get(CubeFace.R)[0][2]}, new CubeFace[]{CubeFace.U, CubeFace.B, CubeFace.R}, 1, 1, 1);
        } /*else if (fromConstructor) {
            setToDefault();
            errorMessage(9);
        }*/ else {
            //errorMessage(10);
            throw new IllegalArgumentException("Color arrays cube state is in improper format or does not contain valid values");
        }
    }

    public void putPieceFromColorsAndFaces(CubeColor[] colors, CubeFace[] faces, int x, int y, int z) {
        sortColors(colors, faces);
        CubePiece piece = getPieceWithColors(colors);
        cubeState.put(piece, new PiecePosition(x, y, z));
        for (int i = 0; i < colors.length; i++) {
            piece.getTile(i).setTile(colors[i], faces[i]);
        }
    }

    public static void sortColors(CubeColor[] colors, CubeFace[] faces) {
        int whiteIndex = Arrays.asList(colors).indexOf(CubeColor.WHITE);
        int yellowIndex = Arrays.asList(colors).indexOf(CubeColor.YELLOW);
        int blueIndex = Arrays.asList(colors).indexOf(CubeColor.BLUE);
        int redIndex = Arrays.asList(colors).indexOf(CubeColor.RED);
        int greenIndex = Arrays.asList(colors).indexOf(CubeColor.GREEN);
        int orangeIndex = Arrays.asList(colors).indexOf(CubeColor.ORANGE);
        switch (colors.length) {
            case 1:
                break;
            case 2:
                if (whiteIndex == 1 || yellowIndex == 1) {
                    swap(colors, faces, 0, 1);
                    break;
                } else if (whiteIndex == -1 && yellowIndex == -1) {
                    if (blueIndex == 1 && orangeIndex == -1) {
                        swap(colors, faces, 0, 1);
                        break;
                    } else if (redIndex == 1 && blueIndex == -1) {
                        swap(colors, faces, 0, 1);
                        break;
                    } else if (greenIndex == 1 && redIndex == -1) {
                        swap(colors, faces, 0, 1);
                    } else if (orangeIndex == 1 && greenIndex == -1) {
                        swap(colors, faces, 0, 1);
                    }
                }
                break;
            case 3:
                if (whiteIndex == 1 || whiteIndex == 2) {
                    swap(colors, faces, 0, whiteIndex);
                } else if (yellowIndex != 0) {
                    swap(colors, faces, 0, yellowIndex);
                }
                CubeColor[] colorsPart = {colors[1], colors[2]};
                CubeFace[] facesPart = {faces[1], faces[2]};
                sortColors(colorsPart, facesPart);
                colors[1] = colorsPart[0];
                colors[2] = colorsPart[1];
                faces[1] = facesPart[0];
                faces[2] = facesPart[1];
        }
    }

    private static void swap(Object[] array, int first, int second) {
        Object temp = array[second];
        array[second] = array[first];
        array[first] = temp;
    }

    private static void swap(Object[] array1, Object[] array2, int first, int second) {
        swap(array1, first, second);
        swap(array2, first, second);
    }

    public CubePiece getPieceWithColors(String... colors) {
        CubeColor[] enumColors = new CubeColor[colors.length];
        for (int i = 0; i < colors.length; i++) {
            enumColors[i] = CubeColor.valueOf(colors[i]);
        }
        return getPieceWithColors(enumColors);
    }

    private CubePiece getPieceWithColors(CubeColor... colors) { //must be in declaration order
        CubePiece piece = null;
        switch (colors.length) {
            case 1:
                switch (colors[0]) {
                    case WHITE:
                        piece = cenW;
                        break;
                    case YELLOW:
                        piece = cenY;
                        break;
                    case BLUE:
                        piece = cenB;
                        break;
                    case GREEN:
                        piece = cenG;
                        break;
                    case RED:
                        piece = cenR;
                        break;
                    case ORANGE:
                        piece = cenO;
                }
                break;
            case 2:
                switch (colors[0]) {
                    case WHITE:
                        switch (colors[1]) {
                            case BLUE:
                                piece = eWB;
                                break;
                            case GREEN:
                                piece = eWG;
                                break;
                            case RED:
                                piece = eWR;
                                break;
                            case ORANGE:
                                piece = eWO;
                        }
                        break;
                    case YELLOW:
                        switch (colors[1]) {
                            case BLUE:
                                piece = eYB;
                                break;
                            case GREEN:
                                piece = eYG;
                                break;
                            case RED:
                                piece = eYR;
                                break;
                            case ORANGE:
                                piece = eYO;
                        }
                        break;
                    case BLUE:
                    case GREEN:
                    case RED:
                    case ORANGE:
                        switch (colors[1]) {
                            case BLUE:
                                piece = eOB;
                                break;
                            case GREEN:
                                piece = eRG;
                                break;
                            case RED:
                                piece = eBR;
                                break;
                            case ORANGE:
                                piece = eGO;
                        }
                }
                break;
            case 3:
                switch (colors[0]) {
                    case WHITE:
                        switch (colors[1]) {
                            case BLUE:
                                piece = cWBR;
                                break;
                            case GREEN:
                                piece = cWGO;
                                break;
                            case RED:
                                piece = cWRG;
                                break;
                            case ORANGE:
                                piece = cWOB;
                        }
                        break;
                    case YELLOW:
                        switch (colors[1]) {
                            case BLUE:
                                piece = cYBR;
                                break;
                            case GREEN:
                                piece = cYGO;
                                break;
                            case RED:
                                piece = cYRG;
                                break;
                            case ORANGE:
                                piece = cYOB;
                        }
                }
        }
        if (piece == null) {
            throw new IllegalArgumentException("No piece with specified colors exists");
        }
        return piece;
    }

    public CubeColor[][] getColorsOfFace(CubeFace face) {
        CubeColor[][] faceColors = new CubeColor[3][3];
        switch (face) {
            case R:
                faceColors[0][0] = pieceAt(1, 1, -1).getTile(CubeFace.R).getColor();
                faceColors[0][1] = pieceAt(1, 1, 0).getTile(CubeFace.R).getColor();
                faceColors[0][2] = pieceAt(1, 1, 1).getTile(CubeFace.R).getColor();
                faceColors[1][0] = pieceAt(1, 0, -1).getTile(CubeFace.R).getColor();
                faceColors[1][1] = pieceAt(1, 0, 0).getTile(CubeFace.R).getColor();
                faceColors[1][2] = pieceAt(1, 0, 1).getTile(CubeFace.R).getColor();
                faceColors[2][0] = pieceAt(1, -1, -1).getTile(CubeFace.R).getColor();
                faceColors[2][1] = pieceAt(1, -1, 0).getTile(CubeFace.R).getColor();
                faceColors[2][2] = pieceAt(1, -1, 1).getTile(CubeFace.R).getColor();
                break;
            case L:
                faceColors[0][2] = pieceAt(-1, 1, -1).getTile(CubeFace.L).getColor();
                faceColors[0][1] = pieceAt(-1, 1, 0).getTile(CubeFace.L).getColor();
                faceColors[0][0] = pieceAt(-1, 1, 1).getTile(CubeFace.L).getColor();
                faceColors[1][2] = pieceAt(-1, 0, -1).getTile(CubeFace.L).getColor();
                faceColors[1][1] = pieceAt(-1, 0, 0).getTile(CubeFace.L).getColor();
                faceColors[1][0] = pieceAt(-1, 0, 1).getTile(CubeFace.L).getColor();
                faceColors[2][2] = pieceAt(-1, -1, -1).getTile(CubeFace.L).getColor();
                faceColors[2][1] = pieceAt(-1, -1, 0).getTile(CubeFace.L).getColor();
                faceColors[2][0] = pieceAt(-1, -1, 1).getTile(CubeFace.L).getColor();
                break;
            case U:
                faceColors[2][0] = pieceAt(-1, 1, -1).getTile(CubeFace.U).getColor();
                faceColors[1][0] = pieceAt(-1, 1, 0).getTile(CubeFace.U).getColor();
                faceColors[0][0] = pieceAt(-1, 1, 1).getTile(CubeFace.U).getColor();
                faceColors[2][1] = pieceAt(0, 1, -1).getTile(CubeFace.U).getColor();
                faceColors[1][1] = pieceAt(0, 1, 0).getTile(CubeFace.U).getColor();
                faceColors[0][1] = pieceAt(0, 1, 1).getTile(CubeFace.U).getColor();
                faceColors[2][2] = pieceAt(1, 1, -1).getTile(CubeFace.U).getColor();
                faceColors[1][2] = pieceAt(1, 1, 0).getTile(CubeFace.U).getColor();
                faceColors[0][2] = pieceAt(1, 1, 1).getTile(CubeFace.U).getColor();
                break;
            case D:
                faceColors[0][0] = pieceAt(-1, -1, -1).getTile(CubeFace.D).getColor();
                faceColors[1][0] = pieceAt(-1, -1, 0).getTile(CubeFace.D).getColor();
                faceColors[2][0] = pieceAt(-1, -1, 1).getTile(CubeFace.D).getColor();
                faceColors[0][1] = pieceAt(0, -1, -1).getTile(CubeFace.D).getColor();
                faceColors[1][1] = pieceAt(0, -1, 0).getTile(CubeFace.D).getColor();
                faceColors[2][1] = pieceAt(0, -1, 1).getTile(CubeFace.D).getColor();
                faceColors[0][2] = pieceAt(1, -1, -1).getTile(CubeFace.D).getColor();
                faceColors[1][2] = pieceAt(1, -1, 0).getTile(CubeFace.D).getColor();
                faceColors[2][2] = pieceAt(1, -1, 1).getTile(CubeFace.D).getColor();
                break;
            case F:
                faceColors[2][0] = pieceAt(-1, -1, -1).getTile(CubeFace.F).getColor();
                faceColors[1][0] = pieceAt(-1, 0, -1).getTile(CubeFace.F).getColor();
                faceColors[0][0] = pieceAt(-1, 1, -1).getTile(CubeFace.F).getColor();
                faceColors[2][1] = pieceAt(0, -1, -1).getTile(CubeFace.F).getColor();
                faceColors[1][1] = pieceAt(0, 0, -1).getTile(CubeFace.F).getColor();
                faceColors[0][1] = pieceAt(0, 1, -1).getTile(CubeFace.F).getColor();
                faceColors[2][2] = pieceAt(1, -1, -1).getTile(CubeFace.F).getColor();
                faceColors[1][2] = pieceAt(1, 0, -1).getTile(CubeFace.F).getColor();
                faceColors[0][2] = pieceAt(1, 1, -1).getTile(CubeFace.F).getColor();
                break;
            case B:
                faceColors[0][0] = pieceAt(-1, -1, 1).getTile(CubeFace.B).getColor();
                faceColors[1][0] = pieceAt(-1, 0, 1).getTile(CubeFace.B).getColor();
                faceColors[2][0] = pieceAt(-1, 1, 1).getTile(CubeFace.B).getColor();
                faceColors[0][1] = pieceAt(0, -1, 1).getTile(CubeFace.B).getColor();
                faceColors[1][1] = pieceAt(0, 0, 1).getTile(CubeFace.B).getColor();
                faceColors[2][1] = pieceAt(0, 1, 1).getTile(CubeFace.B).getColor();
                faceColors[0][2] = pieceAt(1, -1, 1).getTile(CubeFace.B).getColor();
                faceColors[1][2] = pieceAt(1, 0, 1).getTile(CubeFace.B).getColor();
                faceColors[2][2] = pieceAt(1, 1, 1).getTile(CubeFace.B).getColor();
        }
        return faceColors;
    }

    public boolean faceOnlyContainsColor(CubeFace face, CubeColor color) {
        CubeColor[][] colors = getColorsOfFace(face);
        for (CubeColor[] row : colors) {
            for (CubeColor col : row) {
                if (col != color) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean faceContainsColor(CubeFace face, CubeColor color) {
        CubeColor[][] colors = getColorsOfFace(face);
        for (CubeColor[] row : colors) {
            for (CubeColor col : row) {
                if (col == color) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean faceContainsColorOnCenter(CubeFace face, CubeColor color) {
        CubeColor[][] colors = getColorsOfFace(face);
        return colors[1][1] == color;
    }

    public boolean faceContainsColorOnEdge(CubeFace face, CubeColor color) {
        CubeColor[][] colors = getColorsOfFace(face);
        return colors[1][0] == color || colors[1][2] == color || colors[0][1] == color || colors[2][1] == color;
    }

    boolean faceContainsColorOnCorner(CubeFace face, CubeColor color) {
        CubeColor[][] colors = getColorsOfFace(face);
        return colors[0][0] == color || colors[0][2] == color || colors[2][0] == color || colors[2][2] == color;
    }

    public void orientateCube(CubeColor front, CubeColor up) {
        if ((front == CubeColor.WHITE && up == CubeColor.YELLOW) || (up == CubeColor.WHITE && front == CubeColor.YELLOW) ||
                (front == CubeColor.BLUE && up == CubeColor.GREEN) || (up == CubeColor.GREEN && front == CubeColor.BLUE) ||
                (front == CubeColor.RED && up == CubeColor.ORANGE) || (up == CubeColor.ORANGE && front == CubeColor.RED)) {
            throw new IllegalArgumentException("Cannot orientate cube with adjacent colors");
        }
        for (int i = 0; i < 3; i++) {
            if (pieceAt(0, 0, -1).getTile(0).getColor() != front) {
                move("y");
            }
        }
        for (int i = 0; i < 3; i++) {
            if (pieceAt(0, 0, -1).getTile(0).getColor() != front) {
                move("x");
            }
        }
        for (int i = 0; i < 3; i++) {
            if (pieceAt(0, 1, 0).getTile(0).getColor() != up) {
                move("z");
            }
        }
    }

    public HashMap<CubePiece, PiecePosition> getCubeState() {
        HashMap<CubePiece, PiecePosition> cubeStateCopy = new HashMap<>(); //so the user can't edit the original?
        cubeState.putAll(cubeStateCopy);
        return cubeStateCopy;
    }

    public String[] getCubeStateAsText() {
        String[] states = new String[NUM_PIECES];
        int i = 0;
        for (CubePiece piece : cubeState.keySet()) {
            states[i++] = piece.toString() + " " + cubeState.get(piece).toString();
        }
        Arrays.sort(states); //MAKE A CUSTOM COMPARATOR
        return states;
    }

    public String[] getValidMoves() {
        return Arrays.copyOf(VALID_MOVES, VALID_MOVES.length);
    }

    public boolean possibleState(HashMap<CubePiece, PiecePosition> cubeState) {
        return true;
    }

    public boolean isSolved() { //checks if its getCubeStateAsText() is the same as a new Cube3x3x3 orientated so the up and front center colors match this Cube3x3x3
        CubeColor front = pieceAt(0, 0, -1).getTile(0).getColor();
        CubeColor up = pieceAt(0, 1, 0).getTile(0).getColor();
        Cube3x3x3 compareCube = new Cube3x3x3();
        compareCube.orientateCube(front, up);
        return Arrays.equals(getCubeStateAsText(), compareCube.getCubeStateAsText());
    }

    public String[] getRandomMoves(int numMoves) {
        Random random = new Random();
        int max = 62; //63 possible moves [0-62]
        int min = 0;
        String[] moves = new String[numMoves];
        for (int i = 0; i < numMoves; i++) {
            int randomNum = random.nextInt((max - min) + 1) + min;
            moves[i] = VALID_MOVES[randomNum];
        }
        return moves;
    }

    public void scramble() {
        executeMoves(getRandomMoves(50));
    }

    public void move(String input) {
        String[] movesAndBlanks = input.split(" ");
        int numBlanks = 0;
        for (String moveOrBlank : movesAndBlanks) {
            if (moveOrBlank.equals("")) {
                numBlanks++;
            }
        }
        String[] moves = new String[movesAndBlanks.length - numBlanks];
        int j = 0;
        for (String moveOrBlank : movesAndBlanks) {
            if (!moveOrBlank.equals("")) {
                moves[j++] = moveOrBlank;
            }
        }
        move(moves);
    }

    public void move(String... moves) {
        if (isValidNotation(moves)) {
            executeMoves(moves);
        } else {
            throw new IllegalArgumentException("Invalid moves pass to move()");
        }
    }

    public static boolean isValidNotation(String move) {
        return Arrays.asList(VALID_MOVES).contains(move.trim());
    }

    public static boolean isValidNotation(String[] moves) {
        boolean movesValid = true;
        for (String move : moves) {
            if (!Arrays.asList(VALID_MOVES).contains(move.trim())) {
                movesValid = false;
            }
        }
        return movesValid;
    }

    private void executeMoves(String... moves) {
        for (String move : moves) {
            executeMove(move.trim());
        }
    }

    //<editor-fold defaultstate="collapsed" desc="comment">
    private void executeMove(String move) { //executes if move uses 8 pieces, if not calls executeMoves() with an equivelent move set using only 8-piece moves
        CubePiece piece1;
        CubePiece piece2;
        CubePiece piece3;
        CubePiece piece4;
        CubePiece piece5;
        CubePiece piece6;
        CubePiece piece7;
        CubePiece piece8;
        switch (move) {
            case "R":
                piece1 = pieceAt(1, 1, -1); //define in clockwise rotation starting with the closest piece
                piece2 = pieceAt(1, 1, 0);
                piece3 = pieceAt(1, 1, 1);
                piece4 = pieceAt(1, 0, 1);
                piece5 = pieceAt(1, -1, 1);
                piece6 = pieceAt(1, -1, 0);
                piece7 = pieceAt(1, -1, -1);
                piece8 = pieceAt(1, 0, -1);
                changeFaces(piece1, CubeFace.F, CubeFace.U, CubeFace.U, CubeFace.B); //call pairs in order the faces are encountered in the rotation used to define the piece variables
                cubeState.get(piece1).setCoordinates(1, 1, 1);
                changeFaces(piece2, CubeFace.U, CubeFace.B);
                cubeState.get(piece2).setCoordinates(1, 0, 1);
                changeFaces(piece3, CubeFace.U, CubeFace.B, CubeFace.B, CubeFace.D);
                cubeState.get(piece3).setCoordinates(1, -1, 1);
                changeFaces(piece4, CubeFace.B, CubeFace.D);
                cubeState.get(piece4).setCoordinates(1, -1, 0);
                changeFaces(piece5, CubeFace.B, CubeFace.D, CubeFace.D, CubeFace.F);
                cubeState.get(piece5).setCoordinates(1, -1, -1);
                changeFaces(piece6, CubeFace.D, CubeFace.F);
                cubeState.get(piece6).setCoordinates(1, 0, -1);
                changeFaces(piece7, CubeFace.D, CubeFace.F, CubeFace.F, CubeFace.U);
                cubeState.get(piece7).setCoordinates(1, 1, -1);
                changeFaces(piece8, CubeFace.F, CubeFace.U);
                cubeState.get(piece8).setCoordinates(1, 1, 0);
                break;
            case "R'":
                piece1 = pieceAt(1, 1, -1);
                piece2 = pieceAt(1, 1, 0);
                piece3 = pieceAt(1, 1, 1);
                piece4 = pieceAt(1, 0, 1);
                piece5 = pieceAt(1, -1, 1);
                piece6 = pieceAt(1, -1, 0);
                piece7 = pieceAt(1, -1, -1);
                piece8 = pieceAt(1, 0, -1);
                changeFaces(piece1, CubeFace.F, CubeFace.D, CubeFace.U, CubeFace.F);
                cubeState.get(piece1).setCoordinates(1, -1, -1);
                changeFaces(piece2, CubeFace.U, CubeFace.F);
                cubeState.get(piece2).setCoordinates(1, 0, -1);
                changeFaces(piece3, CubeFace.U, CubeFace.F, CubeFace.B, CubeFace.U);
                cubeState.get(piece3).setCoordinates(1, 1, -1);
                changeFaces(piece4, CubeFace.B, CubeFace.U);
                cubeState.get(piece4).setCoordinates(1, 1, 0);
                changeFaces(piece5, CubeFace.B, CubeFace.U, CubeFace.D, CubeFace.B);
                cubeState.get(piece5).setCoordinates(1, 1, 1);
                changeFaces(piece6, CubeFace.D, CubeFace.B);
                cubeState.get(piece6).setCoordinates(1, 0, 1);
                changeFaces(piece7, CubeFace.D, CubeFace.B, CubeFace.F, CubeFace.D);
                cubeState.get(piece7).setCoordinates(1, -1, 1);
                changeFaces(piece8, CubeFace.F, CubeFace.D);
                cubeState.get(piece8).setCoordinates(1, -1, 0);
                break;
            case "R2":
                piece1 = pieceAt(1, 1, -1);
                piece2 = pieceAt(1, 1, 0);
                piece3 = pieceAt(1, 1, 1);
                piece4 = pieceAt(1, 0, 1);
                piece5 = pieceAt(1, -1, 1);
                piece6 = pieceAt(1, -1, 0);
                piece7 = pieceAt(1, -1, -1);
                piece8 = pieceAt(1, 0, -1);
                changeFaces(piece1, CubeFace.F, CubeFace.B, CubeFace.U, CubeFace.D);
                cubeState.get(piece1).setCoordinates(1, -1, 1);
                changeFaces(piece2, CubeFace.U, CubeFace.D);
                cubeState.get(piece2).setCoordinates(1, -1, 0);
                changeFaces(piece3, CubeFace.U, CubeFace.D, CubeFace.B, CubeFace.F);
                cubeState.get(piece3).setCoordinates(1, -1, -1);
                changeFaces(piece4, CubeFace.B, CubeFace.F);
                cubeState.get(piece4).setCoordinates(1, 0, -1);
                changeFaces(piece5, CubeFace.B, CubeFace.F, CubeFace.D, CubeFace.U);
                cubeState.get(piece5).setCoordinates(1, 1, -1);
                changeFaces(piece6, CubeFace.D, CubeFace.U);
                cubeState.get(piece6).setCoordinates(1, 1, 0);
                changeFaces(piece7, CubeFace.D, CubeFace.U, CubeFace.F, CubeFace.B);
                cubeState.get(piece7).setCoordinates(1, 1, 1);
                changeFaces(piece8, CubeFace.F, CubeFace.B);
                cubeState.get(piece8).setCoordinates(1, 0, 1);
                break;
            case "r":
                executeMoves("R", "V");
                break;
            case "r'":
                executeMoves("R'", "V'");
                break;
            case "r2":
                executeMoves("R2", "V2");
                break;
            case "L":
                piece1 = pieceAt(-1, 1, -1);
                piece2 = pieceAt(-1, 1, 0);
                piece3 = pieceAt(-1, 1, 1);
                piece4 = pieceAt(-1, 0, 1);
                piece5 = pieceAt(-1, -1, 1);
                piece6 = pieceAt(-1, -1, 0);
                piece7 = pieceAt(-1, -1, -1);
                piece8 = pieceAt(-1, 0, -1);
                changeFaces(piece1, CubeFace.F, CubeFace.D, CubeFace.U, CubeFace.F);
                cubeState.get(piece1).setCoordinates(-1, -1, -1);
                changeFaces(piece2, CubeFace.U, CubeFace.F);
                cubeState.get(piece2).setCoordinates(-1, 0, -1);
                changeFaces(piece3, CubeFace.U, CubeFace.F, CubeFace.B, CubeFace.U);
                cubeState.get(piece3).setCoordinates(-1, 1, -1);
                changeFaces(piece4, CubeFace.B, CubeFace.U);
                cubeState.get(piece4).setCoordinates(-1, 1, 0);
                changeFaces(piece5, CubeFace.B, CubeFace.U, CubeFace.D, CubeFace.B);
                cubeState.get(piece5).setCoordinates(-1, 1, 1);
                changeFaces(piece6, CubeFace.D, CubeFace.B);
                cubeState.get(piece6).setCoordinates(-1, 0, 1);
                changeFaces(piece7, CubeFace.D, CubeFace.B, CubeFace.F, CubeFace.D);
                cubeState.get(piece7).setCoordinates(-1, -1, 1);
                changeFaces(piece8, CubeFace.F, CubeFace.D);
                cubeState.get(piece8).setCoordinates(-1, -1, 0);
                break;
            case "L'":
                piece1 = pieceAt(-1, 1, -1);
                piece2 = pieceAt(-1, 1, 0);
                piece3 = pieceAt(-1, 1, 1);
                piece4 = pieceAt(-1, 0, 1);
                piece5 = pieceAt(-1, -1, 1);
                piece6 = pieceAt(-1, -1, 0);
                piece7 = pieceAt(-1, -1, -1);
                piece8 = pieceAt(-1, 0, -1);
                changeFaces(piece1, CubeFace.F, CubeFace.U, CubeFace.U, CubeFace.B);
                cubeState.get(piece1).setCoordinates(-1, 1, 1);
                changeFaces(piece2, CubeFace.U, CubeFace.B);
                cubeState.get(piece2).setCoordinates(-1, 0, 1);
                changeFaces(piece3, CubeFace.U, CubeFace.B, CubeFace.B, CubeFace.D);
                cubeState.get(piece3).setCoordinates(-1, -1, 1);
                changeFaces(piece4, CubeFace.B, CubeFace.D);
                cubeState.get(piece4).setCoordinates(-1, -1, 0);
                changeFaces(piece5, CubeFace.B, CubeFace.D, CubeFace.D, CubeFace.F);
                cubeState.get(piece5).setCoordinates(-1, -1, -1);
                changeFaces(piece6, CubeFace.D, CubeFace.F);
                cubeState.get(piece6).setCoordinates(-1, 0, -1);
                changeFaces(piece7, CubeFace.D, CubeFace.F, CubeFace.F, CubeFace.U);
                cubeState.get(piece7).setCoordinates(-1, 1, -1);
                changeFaces(piece8, CubeFace.F, CubeFace.U);
                cubeState.get(piece8).setCoordinates(-1, 1, 0);
                break;
            case "L2":
                piece1 = pieceAt(-1, 1, -1);
                piece2 = pieceAt(-1, 1, 0);
                piece3 = pieceAt(-1, 1, 1);
                piece4 = pieceAt(-1, 0, 1);
                piece5 = pieceAt(-1, -1, 1);
                piece6 = pieceAt(-1, -1, 0);
                piece7 = pieceAt(-1, -1, -1);
                piece8 = pieceAt(-1, 0, -1);
                changeFaces(piece1, CubeFace.F, CubeFace.B, CubeFace.U, CubeFace.D);
                cubeState.get(piece1).setCoordinates(-1, -1, 1);
                changeFaces(piece2, CubeFace.U, CubeFace.D);
                cubeState.get(piece2).setCoordinates(-1, -1, 0);
                changeFaces(piece3, CubeFace.U, CubeFace.D, CubeFace.B, CubeFace.F);
                cubeState.get(piece3).setCoordinates(-1, -1, -1);
                changeFaces(piece4, CubeFace.B, CubeFace.F);
                cubeState.get(piece4).setCoordinates(-1, 0, -1);
                changeFaces(piece5, CubeFace.B, CubeFace.F, CubeFace.D, CubeFace.U);
                cubeState.get(piece5).setCoordinates(-1, 1, -1);
                changeFaces(piece6, CubeFace.D, CubeFace.U);
                cubeState.get(piece6).setCoordinates(-1, 1, 0);
                changeFaces(piece7, CubeFace.D, CubeFace.U, CubeFace.F, CubeFace.B);
                cubeState.get(piece7).setCoordinates(-1, 1, 1);
                changeFaces(piece8, CubeFace.F, CubeFace.B);
                cubeState.get(piece8).setCoordinates(-1, 0, 1);
                break;
            case "l":
                executeMoves("L", "V'");
                break;
            case "l'":
                executeMoves("L'", "V");
                break;
            case "l2":
                executeMoves("L2", "V2");
                break;
            case "U":
                piece1 = pieceAt(-1, 1, -1);
                piece2 = pieceAt(-1, 1, 0);
                piece3 = pieceAt(-1, 1, 1);
                piece4 = pieceAt(0, 1, 1);
                piece5 = pieceAt(1, 1, 1);
                piece6 = pieceAt(1, 1, 0);
                piece7 = pieceAt(1, 1, -1);
                piece8 = pieceAt(0, 1, -1);
                changeFaces(piece1, CubeFace.F, CubeFace.L, CubeFace.L, CubeFace.B);
                cubeState.get(piece1).setCoordinates(-1, 1, 1);
                changeFaces(piece2, CubeFace.L, CubeFace.B);
                cubeState.get(piece2).setCoordinates(0, 1, 1);
                changeFaces(piece3, CubeFace.L, CubeFace.B, CubeFace.B, CubeFace.R);
                cubeState.get(piece3).setCoordinates(1, 1, 1);
                changeFaces(piece4, CubeFace.B, CubeFace.R);
                cubeState.get(piece4).setCoordinates(1, 1, 0);
                changeFaces(piece5, CubeFace.B, CubeFace.R, CubeFace.R, CubeFace.F);
                cubeState.get(piece5).setCoordinates(1, 1, -1);
                changeFaces(piece6, CubeFace.R, CubeFace.F);
                cubeState.get(piece6).setCoordinates(0, 1, -1);
                changeFaces(piece7, CubeFace.R, CubeFace.F, CubeFace.F, CubeFace.L);
                cubeState.get(piece7).setCoordinates(-1, 1, -1);
                changeFaces(piece8, CubeFace.F, CubeFace.L);
                cubeState.get(piece8).setCoordinates(-1, 1, 0);
                break;
            case "U'":
                piece1 = pieceAt(-1, 1, -1);
                piece2 = pieceAt(-1, 1, 0);
                piece3 = pieceAt(-1, 1, 1);
                piece4 = pieceAt(0, 1, 1);
                piece5 = pieceAt(1, 1, 1);
                piece6 = pieceAt(1, 1, 0);
                piece7 = pieceAt(1, 1, -1);
                piece8 = pieceAt(0, 1, -1);
                changeFaces(piece1, CubeFace.F, CubeFace.R, CubeFace.L, CubeFace.F);
                cubeState.get(piece1).setCoordinates(1, 1, -1);
                changeFaces(piece2, CubeFace.L, CubeFace.F);
                cubeState.get(piece2).setCoordinates(0, 1, -1);
                changeFaces(piece3, CubeFace.L, CubeFace.F, CubeFace.B, CubeFace.L);
                cubeState.get(piece3).setCoordinates(-1, 1, -1);
                changeFaces(piece4, CubeFace.B, CubeFace.L);
                cubeState.get(piece4).setCoordinates(-1, 1, 0);
                changeFaces(piece5, CubeFace.B, CubeFace.L, CubeFace.R, CubeFace.B);
                cubeState.get(piece5).setCoordinates(-1, 1, 1);
                changeFaces(piece6, CubeFace.R, CubeFace.B);
                cubeState.get(piece6).setCoordinates(0, 1, 1);
                changeFaces(piece7, CubeFace.R, CubeFace.B, CubeFace.F, CubeFace.R);
                cubeState.get(piece7).setCoordinates(1, 1, 1);
                changeFaces(piece8, CubeFace.F, CubeFace.R);
                cubeState.get(piece8).setCoordinates(1, 1, 0);
                break;
            case "U2":
                piece1 = pieceAt(-1, 1, -1);
                piece2 = pieceAt(-1, 1, 0);
                piece3 = pieceAt(-1, 1, 1);
                piece4 = pieceAt(0, 1, 1);
                piece5 = pieceAt(1, 1, 1);
                piece6 = pieceAt(1, 1, 0);
                piece7 = pieceAt(1, 1, -1);
                piece8 = pieceAt(0, 1, -1);
                changeFaces(piece1, CubeFace.F, CubeFace.B, CubeFace.L, CubeFace.R);
                cubeState.get(piece1).setCoordinates(1, 1, 1);
                changeFaces(piece2, CubeFace.L, CubeFace.R);
                cubeState.get(piece2).setCoordinates(1, 1, 0);
                changeFaces(piece3, CubeFace.L, CubeFace.R, CubeFace.B, CubeFace.F);
                cubeState.get(piece3).setCoordinates(1, 1, -1);
                changeFaces(piece4, CubeFace.B, CubeFace.F);
                cubeState.get(piece4).setCoordinates(0, 1, -1);
                changeFaces(piece5, CubeFace.B, CubeFace.F, CubeFace.R, CubeFace.L);
                cubeState.get(piece5).setCoordinates(-1, 1, -1);
                changeFaces(piece6, CubeFace.R, CubeFace.L);
                cubeState.get(piece6).setCoordinates(-1, 1, 0);
                changeFaces(piece7, CubeFace.R, CubeFace.L, CubeFace.F, CubeFace.B);
                cubeState.get(piece7).setCoordinates(-1, 1, 1);
                changeFaces(piece8, CubeFace.F, CubeFace.B);
                cubeState.get(piece8).setCoordinates(0, 1, 1);
                break;
            case "u":
                executeMoves("U", "H");
                break;
            case "u'":
                executeMoves("U'", "H'");
                break;
            case "u2":
                executeMoves("U2", "H2");
                break;
            case "D":
                piece1 = pieceAt(-1, -1, -1);
                piece2 = pieceAt(-1, -1, 0);
                piece3 = pieceAt(-1, -1, 1);
                piece4 = pieceAt(0, -1, 1);
                piece5 = pieceAt(1, -1, 1);
                piece6 = pieceAt(1, -1, 0);
                piece7 = pieceAt(1, -1, -1);
                piece8 = pieceAt(0, -1, -1);
                changeFaces(piece1, CubeFace.F, CubeFace.R, CubeFace.L, CubeFace.F);
                cubeState.get(piece1).setCoordinates(1, -1, -1);
                changeFaces(piece2, CubeFace.L, CubeFace.F);
                cubeState.get(piece2).setCoordinates(0, -1, -1);
                changeFaces(piece3, CubeFace.L, CubeFace.F, CubeFace.B, CubeFace.L);
                cubeState.get(piece3).setCoordinates(-1, -1, -1);
                changeFaces(piece4, CubeFace.B, CubeFace.L);
                cubeState.get(piece4).setCoordinates(-1, -1, 0);
                changeFaces(piece5, CubeFace.B, CubeFace.L, CubeFace.R, CubeFace.B);
                cubeState.get(piece5).setCoordinates(-1, -1, 1);
                changeFaces(piece6, CubeFace.R, CubeFace.B);
                cubeState.get(piece6).setCoordinates(0, -1, 1);
                changeFaces(piece7, CubeFace.R, CubeFace.B, CubeFace.F, CubeFace.R);
                cubeState.get(piece7).setCoordinates(1, -1, 1);
                changeFaces(piece8, CubeFace.F, CubeFace.R);
                cubeState.get(piece8).setCoordinates(1, -1, 0);
                break;
            case "D'":
                piece1 = pieceAt(-1, -1, -1);
                piece2 = pieceAt(-1, -1, 0);
                piece3 = pieceAt(-1, -1, 1);
                piece4 = pieceAt(0, -1, 1);
                piece5 = pieceAt(1, -1, 1);
                piece6 = pieceAt(1, -1, 0);
                piece7 = pieceAt(1, -1, -1);
                piece8 = pieceAt(0, -1, -1);
                changeFaces(piece1, CubeFace.F, CubeFace.L, CubeFace.L, CubeFace.B);
                cubeState.get(piece1).setCoordinates(-1, -1, 1);
                changeFaces(piece2, CubeFace.L, CubeFace.B);
                cubeState.get(piece2).setCoordinates(0, -1, 1);
                changeFaces(piece3, CubeFace.L, CubeFace.B, CubeFace.B, CubeFace.R);
                cubeState.get(piece3).setCoordinates(1, -1, 1);
                changeFaces(piece4, CubeFace.B, CubeFace.R);
                cubeState.get(piece4).setCoordinates(1, -1, 0);
                changeFaces(piece5, CubeFace.B, CubeFace.R, CubeFace.R, CubeFace.F);
                cubeState.get(piece5).setCoordinates(1, -1, -1);
                changeFaces(piece6, CubeFace.R, CubeFace.F);
                cubeState.get(piece6).setCoordinates(0, -1, -1);
                changeFaces(piece7, CubeFace.R, CubeFace.F, CubeFace.F, CubeFace.L);
                cubeState.get(piece7).setCoordinates(-1, -1, -1);
                changeFaces(piece8, CubeFace.F, CubeFace.L);
                cubeState.get(piece8).setCoordinates(-1, -1, 0);
                break;
            case "D2":
                piece1 = pieceAt(-1, -1, -1);
                piece2 = pieceAt(-1, -1, 0);
                piece3 = pieceAt(-1, -1, 1);
                piece4 = pieceAt(0, -1, 1);
                piece5 = pieceAt(1, -1, 1);
                piece6 = pieceAt(1, -1, 0);
                piece7 = pieceAt(1, -1, -1);
                piece8 = pieceAt(0, -1, -1);
                changeFaces(piece1, CubeFace.F, CubeFace.B, CubeFace.L, CubeFace.R);
                cubeState.get(piece1).setCoordinates(1, -1, 1);
                changeFaces(piece2, CubeFace.L, CubeFace.R);
                cubeState.get(piece2).setCoordinates(1, -1, 0);
                changeFaces(piece3, CubeFace.L, CubeFace.R, CubeFace.B, CubeFace.F);
                cubeState.get(piece3).setCoordinates(1, -1, -1);
                changeFaces(piece4, CubeFace.B, CubeFace.F);
                cubeState.get(piece4).setCoordinates(0, -1, -1);
                changeFaces(piece5, CubeFace.B, CubeFace.F, CubeFace.R, CubeFace.L);
                cubeState.get(piece5).setCoordinates(-1, -1, -1);
                changeFaces(piece6, CubeFace.R, CubeFace.L);
                cubeState.get(piece6).setCoordinates(-1, -1, 0);
                changeFaces(piece7, CubeFace.R, CubeFace.L, CubeFace.F, CubeFace.B);
                cubeState.get(piece7).setCoordinates(-1, -1, 1);
                changeFaces(piece8, CubeFace.F, CubeFace.B);
                cubeState.get(piece8).setCoordinates(0, -1, 1);
                break;
            case "d":
                executeMoves("D", "H'");
                break;
            case "d'":
                executeMoves("D'", "H");
                break;
            case "d2":
                executeMoves("D2", "H2");
                break;
            case "F":
                piece1 = pieceAt(-1, 1, -1);
                piece2 = pieceAt(0, 1, -1);
                piece3 = pieceAt(1, 1, -1);
                piece4 = pieceAt(1, 0, -1);
                piece5 = pieceAt(1, -1, -1);
                piece6 = pieceAt(0, -1, -1);
                piece7 = pieceAt(-1, -1, -1);
                piece8 = pieceAt(-1, 0, -1);
                changeFaces(piece1, CubeFace.L, CubeFace.U, CubeFace.U, CubeFace.R);
                cubeState.get(piece1).setCoordinates(1, 1, -1);
                changeFaces(piece2, CubeFace.U, CubeFace.R);
                cubeState.get(piece2).setCoordinates(1, 0, -1);
                changeFaces(piece3, CubeFace.U, CubeFace.R, CubeFace.R, CubeFace.D);
                cubeState.get(piece3).setCoordinates(1, -1, -1);
                changeFaces(piece4, CubeFace.R, CubeFace.D);
                cubeState.get(piece4).setCoordinates(0, -1, -1);
                changeFaces(piece5, CubeFace.R, CubeFace.D, CubeFace.D, CubeFace.L);
                cubeState.get(piece5).setCoordinates(-1, -1, -1);
                changeFaces(piece6, CubeFace.D, CubeFace.L);
                cubeState.get(piece6).setCoordinates(-1, 0, -1);
                changeFaces(piece7, CubeFace.D, CubeFace.L, CubeFace.L, CubeFace.U);
                cubeState.get(piece7).setCoordinates(-1, 1, -1);
                changeFaces(piece8, CubeFace.L, CubeFace.U);
                cubeState.get(piece8).setCoordinates(0, 1, -1);
                break;
            case "F'":
                piece1 = pieceAt(-1, 1, -1);
                piece2 = pieceAt(0, 1, -1);
                piece3 = pieceAt(1, 1, -1);
                piece4 = pieceAt(1, 0, -1);
                piece5 = pieceAt(1, -1, -1);
                piece6 = pieceAt(0, -1, -1);
                piece7 = pieceAt(-1, -1, -1);
                piece8 = pieceAt(-1, 0, -1);
                changeFaces(piece1, CubeFace.L, CubeFace.D, CubeFace.U, CubeFace.L);
                cubeState.get(piece1).setCoordinates(-1, -1, -1);
                changeFaces(piece2, CubeFace.U, CubeFace.L);
                cubeState.get(piece2).setCoordinates(-1, 0, -1);
                changeFaces(piece3, CubeFace.U, CubeFace.L, CubeFace.R, CubeFace.U);
                cubeState.get(piece3).setCoordinates(-1, 1, -1);
                changeFaces(piece4, CubeFace.R, CubeFace.U);
                cubeState.get(piece4).setCoordinates(0, 1, -1);
                changeFaces(piece5, CubeFace.R, CubeFace.U, CubeFace.D, CubeFace.R);
                cubeState.get(piece5).setCoordinates(1, 1, -1);
                changeFaces(piece6, CubeFace.D, CubeFace.R);
                cubeState.get(piece6).setCoordinates(1, 0, -1);
                changeFaces(piece7, CubeFace.D, CubeFace.R, CubeFace.L, CubeFace.D);
                cubeState.get(piece7).setCoordinates(1, -1, -1);
                changeFaces(piece8, CubeFace.L, CubeFace.D);
                cubeState.get(piece8).setCoordinates(0, -1, -1);
                break;
            case "F2":
                piece1 = pieceAt(-1, 1, -1);
                piece2 = pieceAt(0, 1, -1);
                piece3 = pieceAt(1, 1, -1);
                piece4 = pieceAt(1, 0, -1);
                piece5 = pieceAt(1, -1, -1);
                piece6 = pieceAt(0, -1, -1);
                piece7 = pieceAt(-1, -1, -1);
                piece8 = pieceAt(-1, 0, -1);
                changeFaces(piece1, CubeFace.L, CubeFace.R, CubeFace.U, CubeFace.D);
                cubeState.get(piece1).setCoordinates(1, -1, -1);
                changeFaces(piece2, CubeFace.U, CubeFace.D);
                cubeState.get(piece2).setCoordinates(0, -1, -1);
                changeFaces(piece3, CubeFace.U, CubeFace.D, CubeFace.R, CubeFace.L);
                cubeState.get(piece3).setCoordinates(-1, -1, -1);
                changeFaces(piece4, CubeFace.R, CubeFace.L);
                cubeState.get(piece4).setCoordinates(-1, 0, -1);
                changeFaces(piece5, CubeFace.R, CubeFace.L, CubeFace.D, CubeFace.U);
                cubeState.get(piece5).setCoordinates(-1, 1, -1);
                changeFaces(piece6, CubeFace.D, CubeFace.U);
                cubeState.get(piece6).setCoordinates(0, 1, -1);
                changeFaces(piece7, CubeFace.D, CubeFace.U, CubeFace.L, CubeFace.R);
                cubeState.get(piece7).setCoordinates(1, 1, -1);
                changeFaces(piece8, CubeFace.L, CubeFace.R);
                cubeState.get(piece8).setCoordinates(1, 0, -1);
                break;
            case "f":
                executeMoves("F", "C");
                break;
            case "f'":
                executeMoves("F'", "C'");
                break;
            case "f2":
                executeMoves("F2", "C2");
                break;
            case "B":
                piece1 = pieceAt(-1, 1, 1);
                piece2 = pieceAt(0, 1, 1);
                piece3 = pieceAt(1, 1, 1);
                piece4 = pieceAt(1, 0, 1);
                piece5 = pieceAt(1, -1, 1);
                piece6 = pieceAt(0, -1, 1);
                piece7 = pieceAt(-1, -1, 1);
                piece8 = pieceAt(-1, 0, 1);
                changeFaces(piece1, CubeFace.L, CubeFace.D, CubeFace.U, CubeFace.L);
                cubeState.get(piece1).setCoordinates(-1, -1, 1);
                changeFaces(piece2, CubeFace.U, CubeFace.L);
                cubeState.get(piece2).setCoordinates(-1, 0, 1);
                changeFaces(piece3, CubeFace.U, CubeFace.L, CubeFace.R, CubeFace.U);
                cubeState.get(piece3).setCoordinates(-1, 1, 1);
                changeFaces(piece4, CubeFace.R, CubeFace.U);
                cubeState.get(piece4).setCoordinates(0, 1, 1);
                changeFaces(piece5, CubeFace.R, CubeFace.U, CubeFace.D, CubeFace.R);
                cubeState.get(piece5).setCoordinates(1, 1, 1);
                changeFaces(piece6, CubeFace.D, CubeFace.R);
                cubeState.get(piece6).setCoordinates(1, 0, 1);
                changeFaces(piece7, CubeFace.D, CubeFace.R, CubeFace.L, CubeFace.D);
                cubeState.get(piece7).setCoordinates(1, -1, 1);
                changeFaces(piece8, CubeFace.L, CubeFace.D);
                cubeState.get(piece8).setCoordinates(0, -1, 1);
                break;
            case "B'":
                piece1 = pieceAt(-1, 1, 1);
                piece2 = pieceAt(0, 1, 1);
                piece3 = pieceAt(1, 1, 1);
                piece4 = pieceAt(1, 0, 1);
                piece5 = pieceAt(1, -1, 1);
                piece6 = pieceAt(0, -1, 1);
                piece7 = pieceAt(-1, -1, 1);
                piece8 = pieceAt(-1, 0, 1);
                changeFaces(piece1, CubeFace.L, CubeFace.U, CubeFace.U, CubeFace.R);
                cubeState.get(piece1).setCoordinates(1, 1, 1);
                changeFaces(piece2, CubeFace.U, CubeFace.R);
                cubeState.get(piece2).setCoordinates(1, 0, 1);
                changeFaces(piece3, CubeFace.U, CubeFace.R, CubeFace.R, CubeFace.D);
                cubeState.get(piece3).setCoordinates(1, -1, 1);
                changeFaces(piece4, CubeFace.R, CubeFace.D);
                cubeState.get(piece4).setCoordinates(0, -1, 1);
                changeFaces(piece5, CubeFace.R, CubeFace.D, CubeFace.D, CubeFace.L);
                cubeState.get(piece5).setCoordinates(-1, -1, 1);
                changeFaces(piece6, CubeFace.D, CubeFace.L);
                cubeState.get(piece6).setCoordinates(-1, 0, 1);
                changeFaces(piece7, CubeFace.D, CubeFace.L, CubeFace.L, CubeFace.U);
                cubeState.get(piece7).setCoordinates(-1, 1, 1);
                changeFaces(piece8, CubeFace.L, CubeFace.U);
                cubeState.get(piece8).setCoordinates(0, 1, 1);
                break;
            case "B2":
                piece1 = pieceAt(-1, 1, 1);
                piece2 = pieceAt(0, 1, 1);
                piece3 = pieceAt(1, 1, 1);
                piece4 = pieceAt(1, 0, 1);
                piece5 = pieceAt(1, -1, 1);
                piece6 = pieceAt(0, -1, 1);
                piece7 = pieceAt(-1, -1, 1);
                piece8 = pieceAt(-1, 0, 1);
                changeFaces(piece1, CubeFace.L, CubeFace.R, CubeFace.U, CubeFace.D);
                cubeState.get(piece1).setCoordinates(1, -1, 1);
                changeFaces(piece2, CubeFace.U, CubeFace.D);
                cubeState.get(piece2).setCoordinates(0, -1, 1);
                changeFaces(piece3, CubeFace.U, CubeFace.D, CubeFace.R, CubeFace.L);
                cubeState.get(piece3).setCoordinates(-1, -1, 1);
                changeFaces(piece4, CubeFace.R, CubeFace.L);
                cubeState.get(piece4).setCoordinates(-1, 0, 1);
                changeFaces(piece5, CubeFace.R, CubeFace.L, CubeFace.D, CubeFace.U);
                cubeState.get(piece5).setCoordinates(-1, 1, 1);
                changeFaces(piece6, CubeFace.D, CubeFace.U);
                cubeState.get(piece6).setCoordinates(0, 1, 1);
                changeFaces(piece7, CubeFace.D, CubeFace.U, CubeFace.L, CubeFace.R);
                cubeState.get(piece7).setCoordinates(1, 1, 1);
                changeFaces(piece8, CubeFace.L, CubeFace.R);
                cubeState.get(piece8).setCoordinates(1, 0, 1);
                break;
            case "b":
                executeMoves("B", "C'");
                break;
            case "b'":
                executeMoves("B'", "C");
                break;
            case "b2":
                executeMoves("B2", "C2");
                break;
            case "V":
                piece1 = pieceAt(0, 1, -1);
                piece2 = pieceAt(0, 1, 0);
                piece3 = pieceAt(0, 1, 1);
                piece4 = pieceAt(0, 0, 1);
                piece5 = pieceAt(0, -1, 1);
                piece6 = pieceAt(0, -1, 0);
                piece7 = pieceAt(0, -1, -1);
                piece8 = pieceAt(0, 0, -1);
                changeFaces(piece1, CubeFace.F, CubeFace.U, CubeFace.U, CubeFace.B);
                cubeState.get(piece1).setCoordinates(0, 1, 1);
                changeFaces(piece2, CubeFace.U, CubeFace.B);
                cubeState.get(piece2).setCoordinates(0, 0, 1);
                changeFaces(piece3, CubeFace.U, CubeFace.B, CubeFace.B, CubeFace.D);
                cubeState.get(piece3).setCoordinates(0, -1, 1);
                changeFaces(piece4, CubeFace.B, CubeFace.D);
                cubeState.get(piece4).setCoordinates(0, -1, 0);
                changeFaces(piece5, CubeFace.B, CubeFace.D, CubeFace.D, CubeFace.F);
                cubeState.get(piece5).setCoordinates(0, -1, -1);
                changeFaces(piece6, CubeFace.D, CubeFace.F);
                cubeState.get(piece6).setCoordinates(0, 0, -1);
                changeFaces(piece7, CubeFace.D, CubeFace.F, CubeFace.F, CubeFace.U);
                cubeState.get(piece7).setCoordinates(0, 1, -1);
                changeFaces(piece8, CubeFace.F, CubeFace.U);
                cubeState.get(piece8).setCoordinates(0, 1, 0);
                break;
            case "V'":
                piece1 = pieceAt(0, 1, -1);
                piece2 = pieceAt(0, 1, 0);
                piece3 = pieceAt(0, 1, 1);
                piece4 = pieceAt(0, 0, 1);
                piece5 = pieceAt(0, -1, 1);
                piece6 = pieceAt(0, -1, 0);
                piece7 = pieceAt(0, -1, -1);
                piece8 = pieceAt(0, 0, -1);
                changeFaces(piece1, CubeFace.F, CubeFace.D, CubeFace.U, CubeFace.F);
                cubeState.get(piece1).setCoordinates(0, -1, -1);
                changeFaces(piece2, CubeFace.U, CubeFace.F);
                cubeState.get(piece2).setCoordinates(0, 0, -1);
                changeFaces(piece3, CubeFace.U, CubeFace.F, CubeFace.B, CubeFace.U);
                cubeState.get(piece3).setCoordinates(0, 1, -1);
                changeFaces(piece4, CubeFace.B, CubeFace.U);
                cubeState.get(piece4).setCoordinates(0, 1, 0);
                changeFaces(piece5, CubeFace.B, CubeFace.U, CubeFace.D, CubeFace.B);
                cubeState.get(piece5).setCoordinates(0, 1, 1);
                changeFaces(piece6, CubeFace.D, CubeFace.B);
                cubeState.get(piece6).setCoordinates(0, 0, 1);
                changeFaces(piece7, CubeFace.D, CubeFace.B, CubeFace.F, CubeFace.D);
                cubeState.get(piece7).setCoordinates(0, -1, 1);
                changeFaces(piece8, CubeFace.F, CubeFace.D);
                cubeState.get(piece8).setCoordinates(0, -1, 0);
                break;
            case "V2":
                piece1 = pieceAt(0, 1, -1);
                piece2 = pieceAt(0, 1, 0);
                piece3 = pieceAt(0, 1, 1);
                piece4 = pieceAt(0, 0, 1);
                piece5 = pieceAt(0, -1, 1);
                piece6 = pieceAt(0, -1, 0);
                piece7 = pieceAt(0, -1, -1);
                piece8 = pieceAt(0, 0, -1);
                changeFaces(piece1, CubeFace.F, CubeFace.B, CubeFace.U, CubeFace.D);
                cubeState.get(piece1).setCoordinates(0, -1, 1);
                changeFaces(piece2, CubeFace.U, CubeFace.D);
                cubeState.get(piece2).setCoordinates(0, -1, 0);
                changeFaces(piece3, CubeFace.U, CubeFace.D, CubeFace.B, CubeFace.F);
                cubeState.get(piece3).setCoordinates(0, -1, -1);
                changeFaces(piece4, CubeFace.B, CubeFace.F);
                cubeState.get(piece4).setCoordinates(0, 0, -1);
                changeFaces(piece5, CubeFace.B, CubeFace.F, CubeFace.D, CubeFace.U);
                cubeState.get(piece5).setCoordinates(0, 1, -1);
                changeFaces(piece6, CubeFace.D, CubeFace.U);
                cubeState.get(piece6).setCoordinates(0, 1, 0);
                changeFaces(piece7, CubeFace.D, CubeFace.U, CubeFace.F, CubeFace.B);
                cubeState.get(piece7).setCoordinates(0, 1, 1);
                changeFaces(piece8, CubeFace.F, CubeFace.B);
                cubeState.get(piece8).setCoordinates(0, 0, 1);
                break;
            case "v":
                executeMoves("R", "L'");
                break;
            case "v'":
                executeMoves("R'", "L");
                break;
            case "v2":
                executeMoves("R2", "L2");
                break;
            case "H":
                piece1 = pieceAt(-1, 0, -1);
                piece2 = pieceAt(-1, 0, 0);
                piece3 = pieceAt(-1, 0, 1);
                piece4 = pieceAt(0, 0, 1);
                piece5 = pieceAt(1, 0, 1);
                piece6 = pieceAt(1, 0, 0);
                piece7 = pieceAt(1, 0, -1);
                piece8 = pieceAt(0, 0, -1);
                changeFaces(piece1, CubeFace.F, CubeFace.L, CubeFace.L, CubeFace.B);
                cubeState.get(piece1).setCoordinates(-1, 0, 1);
                changeFaces(piece2, CubeFace.L, CubeFace.B);
                cubeState.get(piece2).setCoordinates(0, 0, 1);
                changeFaces(piece3, CubeFace.L, CubeFace.B, CubeFace.B, CubeFace.R);
                cubeState.get(piece3).setCoordinates(1, 0, 1);
                changeFaces(piece4, CubeFace.B, CubeFace.R);
                cubeState.get(piece4).setCoordinates(1, 0, 0);
                changeFaces(piece5, CubeFace.B, CubeFace.R, CubeFace.R, CubeFace.F);
                cubeState.get(piece5).setCoordinates(1, 0, -1);
                changeFaces(piece6, CubeFace.R, CubeFace.F);
                cubeState.get(piece6).setCoordinates(0, 0, -1);
                changeFaces(piece7, CubeFace.R, CubeFace.F, CubeFace.F, CubeFace.L);
                cubeState.get(piece7).setCoordinates(-1, 0, -1);
                changeFaces(piece8, CubeFace.F, CubeFace.L);
                cubeState.get(piece8).setCoordinates(-1, 0, 0);
                break;
            case "H'":
                piece1 = pieceAt(-1, 0, -1);
                piece2 = pieceAt(-1, 0, 0);
                piece3 = pieceAt(-1, 0, 1);
                piece4 = pieceAt(0, 0, 1);
                piece5 = pieceAt(1, 0, 1);
                piece6 = pieceAt(1, 0, 0);
                piece7 = pieceAt(1, 0, -1);
                piece8 = pieceAt(0, 0, -1);
                changeFaces(piece1, CubeFace.F, CubeFace.R, CubeFace.L, CubeFace.F);
                cubeState.get(piece1).setCoordinates(1, 0, -1);
                changeFaces(piece2, CubeFace.L, CubeFace.F);
                cubeState.get(piece2).setCoordinates(0, 0, -1);
                changeFaces(piece3, CubeFace.L, CubeFace.F, CubeFace.B, CubeFace.L);
                cubeState.get(piece3).setCoordinates(-1, 0, -1);
                changeFaces(piece4, CubeFace.B, CubeFace.L);
                cubeState.get(piece4).setCoordinates(-1, 0, 0);
                changeFaces(piece5, CubeFace.B, CubeFace.L, CubeFace.R, CubeFace.B);
                cubeState.get(piece5).setCoordinates(-1, 0, 1);
                changeFaces(piece6, CubeFace.R, CubeFace.B);
                cubeState.get(piece6).setCoordinates(0, 0, 1);
                changeFaces(piece7, CubeFace.R, CubeFace.B, CubeFace.F, CubeFace.R);
                cubeState.get(piece7).setCoordinates(1, 0, 1);
                changeFaces(piece8, CubeFace.F, CubeFace.R);
                cubeState.get(piece8).setCoordinates(1, 0, 0);
                break;
            case "H2":
                piece1 = pieceAt(-1, 0, -1);
                piece2 = pieceAt(-1, 0, 0);
                piece3 = pieceAt(-1, 0, 1);
                piece4 = pieceAt(0, 0, 1);
                piece5 = pieceAt(1, 0, 1);
                piece6 = pieceAt(1, 0, 0);
                piece7 = pieceAt(1, 0, -1);
                piece8 = pieceAt(0, 0, -1);
                changeFaces(piece1, CubeFace.F, CubeFace.B, CubeFace.L, CubeFace.R);
                cubeState.get(piece1).setCoordinates(1, 0, 1);
                changeFaces(piece2, CubeFace.L, CubeFace.R);
                cubeState.get(piece2).setCoordinates(1, 0, 0);
                changeFaces(piece3, CubeFace.L, CubeFace.R, CubeFace.B, CubeFace.F);
                cubeState.get(piece3).setCoordinates(1, 0, -1);
                changeFaces(piece4, CubeFace.B, CubeFace.F);
                cubeState.get(piece4).setCoordinates(0, 0, -1);
                changeFaces(piece5, CubeFace.B, CubeFace.F, CubeFace.R, CubeFace.L);
                cubeState.get(piece5).setCoordinates(-1, 0, -1);
                changeFaces(piece6, CubeFace.R, CubeFace.L);
                cubeState.get(piece6).setCoordinates(-1, 0, 0);
                changeFaces(piece7, CubeFace.R, CubeFace.L, CubeFace.F, CubeFace.B);
                cubeState.get(piece7).setCoordinates(-1, 0, 1);
                changeFaces(piece8, CubeFace.F, CubeFace.B);
                cubeState.get(piece8).setCoordinates(0, 0, 1);
                break;
            case "h":
                executeMoves("U", "D'");
                break;
            case "h'":
                executeMoves("U'", "D");
                break;
            case "h2":
                executeMoves("U2", "D2");
                break;
            case "C":
                piece1 = pieceAt(-1, 1, 0);
                piece2 = pieceAt(0, 1, 0);
                piece3 = pieceAt(1, 1, 0);
                piece4 = pieceAt(1, 0, 0);
                piece5 = pieceAt(1, -1, 0);
                piece6 = pieceAt(0, -1, 0);
                piece7 = pieceAt(-1, -1, 0);
                piece8 = pieceAt(-1, 0, 0);
                changeFaces(piece1, CubeFace.L, CubeFace.U, CubeFace.U, CubeFace.R);
                cubeState.get(piece1).setCoordinates(1, 1, 0);
                changeFaces(piece2, CubeFace.U, CubeFace.R);
                cubeState.get(piece2).setCoordinates(1, 0, 0);
                changeFaces(piece3, CubeFace.U, CubeFace.R, CubeFace.R, CubeFace.D);
                cubeState.get(piece3).setCoordinates(1, -1, 0);
                changeFaces(piece4, CubeFace.R, CubeFace.D);
                cubeState.get(piece4).setCoordinates(0, -1, 0);
                changeFaces(piece5, CubeFace.R, CubeFace.D, CubeFace.D, CubeFace.L);
                cubeState.get(piece5).setCoordinates(-1, -1, 0);
                changeFaces(piece6, CubeFace.D, CubeFace.L);
                cubeState.get(piece6).setCoordinates(-1, 0, 0);
                changeFaces(piece7, CubeFace.D, CubeFace.L, CubeFace.L, CubeFace.U);
                cubeState.get(piece7).setCoordinates(-1, 1, 0);
                changeFaces(piece8, CubeFace.L, CubeFace.U);
                cubeState.get(piece8).setCoordinates(0, 1, 0);
                break;
            case "C'":
                piece1 = pieceAt(-1, 1, 0);
                piece2 = pieceAt(0, 1, 0);
                piece3 = pieceAt(1, 1, 0);
                piece4 = pieceAt(1, 0, 0);
                piece5 = pieceAt(1, -1, 0);
                piece6 = pieceAt(0, -1, 0);
                piece7 = pieceAt(-1, -1, 0);
                piece8 = pieceAt(-1, 0, 0);
                changeFaces(piece1, CubeFace.L, CubeFace.D, CubeFace.U, CubeFace.L);
                cubeState.get(piece1).setCoordinates(-1, -1, 0);
                changeFaces(piece2, CubeFace.U, CubeFace.L);
                cubeState.get(piece2).setCoordinates(-1, 0, 0);
                changeFaces(piece3, CubeFace.U, CubeFace.L, CubeFace.R, CubeFace.U);
                cubeState.get(piece3).setCoordinates(-1, 1, 0);
                changeFaces(piece4, CubeFace.R, CubeFace.U);
                cubeState.get(piece4).setCoordinates(0, 1, 0);
                changeFaces(piece5, CubeFace.R, CubeFace.U, CubeFace.D, CubeFace.R);
                cubeState.get(piece5).setCoordinates(1, 1, 0);
                changeFaces(piece6, CubeFace.D, CubeFace.R);
                cubeState.get(piece6).setCoordinates(1, 0, 0);
                changeFaces(piece7, CubeFace.D, CubeFace.R, CubeFace.L, CubeFace.D);
                cubeState.get(piece7).setCoordinates(1, -1, 0);
                changeFaces(piece8, CubeFace.L, CubeFace.D);
                cubeState.get(piece8).setCoordinates(0, -1, 0);
                break;
            case "C2":
                piece1 = pieceAt(-1, 1, 0);
                piece2 = pieceAt(0, 1, 0);
                piece3 = pieceAt(1, 1, 0);
                piece4 = pieceAt(1, 0, 0);
                piece5 = pieceAt(1, -1, 0);
                piece6 = pieceAt(0, -1, 0);
                piece7 = pieceAt(-1, -1, 0);
                piece8 = pieceAt(-1, 0, 0);
                changeFaces(piece1, CubeFace.L, CubeFace.R, CubeFace.U, CubeFace.D);
                cubeState.get(piece1).setCoordinates(1, -1, 0);
                changeFaces(piece2, CubeFace.U, CubeFace.D);
                cubeState.get(piece2).setCoordinates(0, -1, 0);
                changeFaces(piece3, CubeFace.U, CubeFace.D, CubeFace.R, CubeFace.L);
                cubeState.get(piece3).setCoordinates(-1, -1, 0);
                changeFaces(piece4, CubeFace.R, CubeFace.L);
                cubeState.get(piece4).setCoordinates(-1, 0, 0);
                changeFaces(piece5, CubeFace.R, CubeFace.L, CubeFace.D, CubeFace.U);
                cubeState.get(piece5).setCoordinates(-1, 1, 0);
                changeFaces(piece6, CubeFace.D, CubeFace.U);
                cubeState.get(piece6).setCoordinates(0, 1, 0);
                changeFaces(piece7, CubeFace.D, CubeFace.U, CubeFace.L, CubeFace.R);
                cubeState.get(piece7).setCoordinates(1, 1, 0);
                changeFaces(piece8, CubeFace.L, CubeFace.R);
                cubeState.get(piece8).setCoordinates(1, 0, 0);
                break;
            case "c":
                executeMoves("F", "B'");
                break;
            case "c'":
                executeMoves("F'", "B");
                break;
            case "c2":
                executeMoves("F2", "B2");
                break;
            case "x":
                executeMoves("R", "V", "L'");
                break;
            case "x'":
                executeMoves("R'", "V'", "L");
                break;
            case "x2":
                executeMoves("R2", "V2", "L2");
                break;
            case "y":
                executeMoves("U", "H", "D'");
                break;
            case "y'":
                executeMoves("U'", "H'", "D");
                break;
            case "y2":
                executeMoves("U2", "H2", "D2");
                break;
            case "z":
                executeMoves("F", "C", "B'");
                break;
            case "z'":
                executeMoves("F'", "C'", "B");
                break;
            case "z2":
                executeMoves("F2", "C2", "B2");
            default:
                throw new IllegalArgumentException("Invalid move passed to executeMoves()");
        }
    }
//</editor-fold>

    public CubePiece pieceAt(int x, int y, int z) {
        for (CubePiece piece : cubeState.keySet()) {
            int[] coords = cubeState.get(piece).getCoordinates();
            if (coords[0] == x && coords[1] == y && coords[2] == z) {
                return piece;
            }
        }
        throw new IllegalArgumentException("No piece at coordinates accessed");
    }

    /*public CubePiece pieceAtPublic(int x, int y, int z) throws IllegalArgumentException {
    for (CubePiece piece : cubeState.keySet()) {
    int[] coords = cubeState.get(piece).getCoordinates();
    if (coords[0] == x && coords[1] == y && coords[2] == z) {
    if (piece instanceof CenterPiece) {
    return new CenterPiece(piece);
    }
    if (piece instanceof EdgePiece) {
    return new EdgePiece(piece);
    }
    if (piece instanceof CornerPiece) {
    return new CornerPiece(piece);
    }
    }
    }
    throw new IllegalArgumentException("Error. No piece at coordinates accessed.");
    }*/
    public void changeFaces(CubePiece piece, CubeFace oldFace, CubeFace newFace) {
        piece.getTile(oldFace).setFace(newFace);
        /*if(piece.getTile(0).getFace() == oldFace)
        piece.getTile(0).setFace(newFace);
        else if(piece.getTile(1).getFace() == oldFace)
        piece.getTile(1).setFace(newFace);
        else
        piece.getTile(2).setFace(newFace);*/
    }

    public void changeFaces(CubePiece piece, CubeFace old1, CubeFace new1, CubeFace old2, CubeFace new2) {
        /*CANT DO: changeFaces(piece, CubeFace.X, CubeFace.Y, CubeFace.Y, CubeFace.Z);
          MUST DO: changeFaces(piece, CubeFace.Y, CubeFace.Z, CubeFace.X, CubeFace.Y);
          OR ONE FACE WILL BE CHANGED TWICE*/
        if (new1 == old2) {
            changeFaces(piece, old2, new2);
            changeFaces(piece, old1, new1);
        } else {
            changeFaces(piece, old1, new1);
            changeFaces(piece, old2, new2);
        }
    }

    /*private static void errorMessage(int type) {
        errorMessage(type, "no_info");
    }

    private static void errorMessage(int type, String... info) { //ADD INFO FOR ALL TYPES
        switch (type) {
            case 0:
                System.out.println("Invalid move(s) entered, cube state not altered.");
                break;
            case 1:
                System.out.println("Error. No piece at coordinates accessed."); //not depentdent on user
                break;
            case 2:
                System.out.println("Error. Invalid String[][][] passed to constructor, cube put in default state.");
                break;
            case 3:
                System.out.println("Error. Invalid String[] passed to constructor, cube put in default state.");
                break;
            case 4:
                System.out.println("Error. Invalid String[] passed to setWithText(), not all pieces entered once and only once, cube state not altered.");
                break;
            case 5:
                System.out.println("Error. Invalid String[] passed to setWithText(), impossible cube state passed, cube state not altered.");
                break;
            case 6:
                System.out.println("Error. Invalid String[] passed to constructor, impossible cube state passed, cube put in default state.");
                break;
            case 7:
                System.out.println("Error. Invalid String in String[]. Malformed String:\n" + info[0]);
                break;
            case 8:
                System.out.println("Error. Invalid String in String[]. Coordinates either illegal or for wrong piece type. Malformed String:\n" + info[0]);
                break;
            case 9:
                System.out.println("Error. Invalid HashMap<CubeFace, CubeColor[][]> passed to constructor, cube put in default state.");
                break;
            case 10:
                System.out.println("Error. Invalid HashMap<CubeFace, CubeColor[][]> passed to setWithColorArrays(), cube state not altered.");
                break;
            default:
                System.out.println("Error occured in class Cube3x3x3.");
        }
    }*/
}
