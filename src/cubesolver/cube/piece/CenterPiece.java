package cubesolver.cube.piece;

/**
 *
 * @author Bruno Freeman
 */
public class CenterPiece extends CubePiece {

    public CenterPiece(CubeTile tile) {
        super(tile);
    }

    public CenterPiece(CubePiece piece) {
        super(piece);
    }

    @Override
    public String toString() {
        String color = getTile(0).getColor().name();
        String face = getTile(0).getFace().name();
        String tile = String.format("%10s", color + ";" + face);
        String status = String.format("%6s {%-30s  }", "center", tile);
        return status;
    }
}
