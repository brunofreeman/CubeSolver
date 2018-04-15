package cubesolver.cube.piece;

/**
 *
 * @author Bruno Freeman
 */
public class EdgePiece extends CubePiece {

    public EdgePiece(CubeTile tile1, CubeTile tile2) {
        super(tile1, tile2);
    }

    public EdgePiece(CubePiece piece) {
        super(piece);
    }

    @Override
    public String toString() {
        String color1 = getTile(0).getColor().name();
        String face1 = getTile(0).getFace().name();
        String color2 = getTile(1).getColor().name();
        String face2 = getTile(1).getFace().name();
        String tile1 = String.format("%10s", color1 + ";" + face1);
        String tile2 = String.format("%10s", color2 + ";" + face2);
        String status = String.format("%6s {%-30s  }", "edge", tile1 + tile2);
        return status;
    }
}
