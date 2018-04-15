package cubesolver.cube.piece;

/**
 * Models a single tile on a cube. A single piece may have more than one tile.
 * For now, a class isn't necessary, a variable of type CubeColor would suffice,
 * but this makes it easier to expand in the future.
 *
 * @author Bruno Freeman
 */
public class CubeTile implements Cloneable {

    private CubeColor color; //uses Enum type CubeColor to define the tile's color
    private CubeFace face;

    public CubeTile(CubeColor color, CubeFace face) {
        this.color = color;
        this.face = face;
    }
    
    public CubeTile(CubeTile tile) {
        this.color = tile.getColor();
        this.face = tile.getFace();
    }

    public CubeTile(CubeColor color) //for creating CubeTiles for a solved cube in default orientation
    {
        this.color = color;
        switch (color) {
            case WHITE:
                face = CubeFace.D;
                break;
            case YELLOW:
                face = CubeFace.U;
                break;
            case BLUE:
                face = CubeFace.F;
                break;
            case GREEN:
                face = CubeFace.B;
                break;
            case RED:
                face = CubeFace.R;
                break;
            case ORANGE:
                face = CubeFace.L;
                break;
        }
    }

    public void setTile(CubeColor color, CubeFace face) {
        this.color = color;
        this.face = face;
    }

    public void setColor(CubeColor color) {
        this.color = color;
    }

    public void setFace(CubeFace face) {
        this.face = face;
    }

    public CubeColor getColor() {
        return color;
    }

    public CubeFace getFace() {
        return face;
    }

    public String toString() {
        return color + ";" + face;
    }

    @Override
    public CubeTile clone() {
        try {
            return (CubeTile) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
