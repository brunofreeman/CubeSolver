package cubesolver.cube.piece;

/**
 * Names are in relation to the white center.
 *
 * @author Bruno Freeman
 */
public class PiecePosition {

    private int x;
    private int y;
    private int z;
    private static int minCoord;
    private static int maxCoord;

    public PiecePosition(int x, int y, int z) throws IllegalArgumentException
    {
        minCoord = -1;
        maxCoord = 1;
        setX(x);
        setY(y);
        setZ(z);
    }

    public void setCoordinates(int x, int y, int z) throws IllegalArgumentException {
        setX(x);
        setY(y);
        setZ(z);
    }

    public final void setX(int x) throws IllegalArgumentException {
        if (isValidCoordinate(x)) {
            this.x = x;
        } else {
            errorMessage(x, "x");
            throw new IllegalArgumentException();
        }
    }

    public final void setY(int y) throws IllegalArgumentException {
        if (isValidCoordinate(y)) {
            this.y = y;
        } else {
            errorMessage(y, "y");
            throw new IllegalArgumentException();
        }
    }

    public final void setZ(int z) throws IllegalArgumentException {
        if (isValidCoordinate(z)) {
            this.z = z;
        } else {
            errorMessage(z, "z");
            throw new IllegalArgumentException();
        }
    }

    public int[] getCoordinates() {
        int[] coordinates = {x, y, z};
        return coordinates;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    private static boolean isValidCoordinate(int coord) {
        return (coord >= minCoord && coord <= maxCoord);
    }

    @Override
    public String toString() {
        return String.format("(%2d,%2d,%2d)", getX(), getY(), getZ());
    }

    private static void errorMessage(int invalid, String axis) {
        System.out.println("Invalid coordinate, must be {-1, 0 , 1}.\n"
                + "Value given: " + invalid + "\n"
                + "Axis: " + axis);
    }
}
