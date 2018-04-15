package cubesolver.cube;

import cubesolver.cube.piece.CubeFace;
import cubesolver.cube.piece.CubePiece;
import cubesolver.cube.piece.PiecePosition;
import java.util.HashMap;

/**
 *
 * @author Bruno Freeman
 */
public abstract class Cube {

    private HashMap<CubePiece, PiecePosition> cubeState;

    private static String[] VALID_MOVES;

    public abstract HashMap<CubePiece, PiecePosition> getCubeState();

    public abstract String[] getCubeStateAsText();

    public abstract String[] getValidMoves();

    public abstract boolean isSolved();

    public abstract String[] getRandomMoves(int numMoves);

    public abstract void move(String move);

    public abstract void move(String[] moves);

    protected static boolean validNotation(String[] moves) {
        return false;
    }

    protected abstract void executeMoves(String[] moves);

    protected abstract void executeMove(String move);

    protected abstract CubePiece pieceAt(int x, int y, int z);

    protected abstract void changeFaces(CubePiece piece, CubeFace oldFace, CubeFace newFace);

    protected abstract void changeFaces(CubePiece piece, CubeFace old1, CubeFace new1, CubeFace old2, CubeFace new2);

    protected static void errorMessage(int arg) {
    }
}
