package cubesolver.cube.piece;

/**
 *
 * @author Bruno Freeman
 */
public class CornerPiece extends CubePiece {

    public CornerPiece(CubeTile tile1, CubeTile tile2, CubeTile tile3) {
        super(tile1, tile2, tile3);
    }

    public CornerPiece(CubePiece piece) {
        super(piece);
    }

    @Override
    public String toString() {
        String color1 = getTile(0).getColor().name();
        String face1 = getTile(0).getFace().name();
        String color2 = getTile(1).getColor().name();
        String face2 = getTile(1).getFace().name();
        String color3 = getTile(2).getColor().name();
        String face3 = getTile(2).getFace().name();
        String tile1 = String.format("%10s", color1 + ";" + face1);
        String tile2 = String.format("%10s", color2 + ";" + face2);
        String tile3 = String.format("%10s", color3 + ";" + face3);
        String status = String.format("%6s {%-30s  }", "corner", tile1 + tile2 + tile3);
        return status;
    }
}
