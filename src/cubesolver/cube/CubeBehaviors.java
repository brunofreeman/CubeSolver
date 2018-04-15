package cubesolver.cube;

import cubesolver.cube.piece.CubePiece;
import cubesolver.cube.piece.PiecePosition;
import java.util.HashMap;

/**
 *
 * @author Bruno Freeman
 */
public interface CubeBehaviors {

    HashMap<CubePiece, PiecePosition> getCubeState();

    String[] getCubeStateAsText();

    String[] getValidMoves();

    boolean isSolved();

    String[] getRandomMoves(int numMoves);

    void move(String move);

    void move(String[] moves);
}
