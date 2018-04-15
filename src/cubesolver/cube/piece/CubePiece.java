package cubesolver.cube.piece;

import java.util.Arrays;

/**
 *
 * @author Bruno Freeman
 */
public abstract class CubePiece {

    private final CubeTile[] tiles;
    private final CubeTile[] originalTiles;

    public CubePiece(CubeTile... tiles) {
        this.tiles = tiles;
        originalTiles = new CubeTile[tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            originalTiles[i] = new CubeTile(tiles[i]);
        }
    }

    public CubePiece(CubePiece other) {
        tiles = Arrays.copyOf(other.getTiles(), other.getTiles().length);
        originalTiles = Arrays.copyOf(other.getOriginalTiles(), other.getOriginalTiles().length);
    }

    public void setTile(int num, CubeTile tile) {
        tiles[num] = tile;
    }

    public CubeTile getTile(int num) {
        return tiles[num];
    }

    public CubeTile getTile(CubeFace face) throws IllegalArgumentException {
        for (CubeTile tile : tiles) {
            if (tile.getFace() == face) {
                return tile;
            }
        }
        throw new IllegalArgumentException();
    }

    public final CubeTile[] getTiles() {
        return tiles;
    }

    public final CubeTile[] getOriginalTiles() {
        return originalTiles;
    }

    public void resetTiles() {
        for (int i = 0; i < originalTiles.length; i++) {
            tiles[i] = new CubeTile(originalTiles[i].getColor(), originalTiles[i].getFace());
        }
    }

    public boolean containsColor(CubeColor color) {
        for (CubeTile tile : tiles) {
            if (tile.getColor() == color) {
                return true;
            }
        }
        return false;
    }

    @Override
    public abstract String toString();
}
