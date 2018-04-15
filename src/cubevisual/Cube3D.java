package cubevisual;

import cubesolver.CubeFileFinder;
import cubesolver.cube.Cube3x3x3;
import static cubesolver.cube.Cube3x3x3.isValidNotation;
import cubesolver.cube.piece.CenterPiece;
import cubesolver.cube.piece.CubePiece;
import cubesolver.cube.piece.CubeTile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

/**
 *
 * @author Bruno Freeman
 */
public class Cube3D {

    private Cube3x3x3 cube;
    private float cs; //cube size
    private CubeApp app;
    private CubeTerminal ct;
    private MeshView[][][] cube3d;
    private MeshView[][][] cube3dOld;
    final private static double at = 500; //animate time
    private ArrayList<Tlam> tlams; //TimelineAndMove
    final private Counter counter = new Counter();
    private boolean animating = false;
    private ArrayList<String> stack = new ArrayList<>();

    public Cube3D(Cube3x3x3 c, float cs, CubeApp app, CubeTerminal ct) {
        this.cs = cs;
        this.app = app;
        this.ct = ct;
        cube = new Cube3x3x3(c);
        cubeToCube3d();
        cube3dOld = cube3d;
    }

    public void setCube(Cube3x3x3 c) {
        cube = new Cube3x3x3(c);
    }

    public void setCS(float cs) {
        this.cs = cs;
    }

    public void setCT(CubeTerminal ct) {
        this.ct = ct;
    }

    public void cubeToCube3d() {
        cube3d = new MeshView[3][3][3];
        for (int x = 0; x <= 2; x++) {
            for (int y = 0; y <= 2; y++) {
                for (int z = 0; z <= 2; z++) {
                    if (x-1 != 0 || y-1 !=0 || z-1 != 0) {
                        MeshView mv = generateMesh();
                        cube3d[x][y][z] = mv;
                        mv.setMaterial(generateMaterial(x-1,y-1,z-1));
                        generateTransforms(x-1, y-1, z-1, mv);

                    }
                }
            }
        }
        cube3d[1][1][1] = generateMesh();
    }

    private void updateCube(String move) {
        cube3dOld = cube3d;
        cube.move(move);
        cubeToCube3d();
    }

    private void generateTransforms(int x, int y, int z, MeshView mv) {
        mv.getTransforms().add(new Translate(x * cs, y * -cs, z * cs));
        generateRotation(x, y, z, mv);
    }

    private MeshView generateMesh() {
        float hw = cs / 2;
        float hh = cs / 2;
        float hd = cs / 2;

        float[] points = {
            hw, hh, hd,
            hw, hh, -hd,
            hw, -hh, hd,
            hw, -hh, -hd,
            -hw, hh, hd,
            -hw, hh, -hd,
            -hw, -hh, hd,
            -hw, -hh, -hd
        };

        float h = 406;
        float w = 541;
        float[] tex = {
            0, 136/h, //0
            0, 271/h, //1
            136/w, 0, //2
            136/w, 136/h, //3
            136/w, 271/h,//4
            136/w, 1,//5
            271/w, 0,//6
            271/w, 136/h, //7
            271/w, 271/h, //8
            271/w, 1, //9
            406/w, 136/h,//10
            406/w, 271/h,//11
            1, 136/h,//12
            1, 271/h//13
        };

        int[] faces = {
            3,7, 2,6, 7,3, //top
            7,3, 2,6, 6,2,
            4,1, 7,3, 6,0, //left
            5,4, 7,3, 4,1,
            5,4, 3,7, 7,3, //front
            1,8, 3,7, 5,4,
            1,8, 2,10, 3,7, //right
            1,8, 0,11, 2,10,
            0,11, 6,12, 2,10, //back
            0,11, 4,13, 6,12,
            0,9, 1,8, 4,5, //bottom
            1,8, 5,4, 4,5
        };

        TriangleMesh mesh = new TriangleMesh();
        mesh.getTexCoords().addAll(tex);
        mesh.getPoints().addAll(points);
        mesh.getFaces().addAll(faces);
        return new MeshView(mesh);
    }

    private Material generateMaterial(int x, int y, int z) {
        CubePiece piece = cube.pieceAt(x,y,z);
        CubeTile[] tiles = piece.getTiles();
        String file = "";
        for (int i = 0; i < tiles.length; i++) {
            file += tiles[i].getColor().toString().substring(0, 1);
        }
        switch (file.length()) {
            case 1:
                file = "cen" + file;
                break;
            case 2:
                file = "e" + file;
                break;
            case 3:
                file = "c" + file;
        }
        file += ".png";
        file = "maps\\" + file;
        PhongMaterial m = new PhongMaterial();
        try {
            m.setDiffuseMap(new Image(new FileInputStream(new File(CubeFileFinder.getPath(file)))));
        } catch (FileNotFoundException e) {
        }
        return m;
    }

    private void generateRotation(int x, int y, int z, MeshView mv) {
        CubePiece piece = cube.pieceAt(x,y,z);
        CubeTile[] tiles = piece.getTiles();
        ObservableList<Transform> transforms = mv.getTransforms();
        if (piece instanceof CenterPiece) {
            switch (tiles[0].getFace()) {
                case R:
                    transforms.add(new Rotate(-90, Rotate.Y_AXIS));
                    break;
                case L:
                    transforms.add(new Rotate(90, Rotate.Y_AXIS));
                    break;
                case U:
                    transforms.add(new Rotate(-90, Rotate.X_AXIS));
                    break;
                case D:
                    transforms.add(new Rotate(90, Rotate.X_AXIS));
                    break;
                case B:
                    transforms.add(new Rotate(180, Rotate.X_AXIS));
            }
        } else {
            switch (tiles[0].getFace()) {
                case R:
                    transforms.add(new Rotate(90, Rotate.Z_AXIS));
                    switch (tiles[1].getFace()) {
                        case U:
                            transforms.add(new Rotate(90, Rotate.Y_AXIS));
                            break;
                        case D:
                            transforms.add(new Rotate(-90, Rotate.Y_AXIS));
                            break;
                        case B:
                            transforms.add(new Rotate(180, Rotate.Y_AXIS));
                    }
                    break;
                case L:
                    transforms.add(new Rotate(-90, Rotate.Z_AXIS));
                    switch (tiles[1].getFace()) {
                        case U:
                            transforms.add(new Rotate(-90, Rotate.Y_AXIS));
                            break;
                        case D:
                            transforms.add(new Rotate(90, Rotate.Y_AXIS));
                            break;
                        case B:
                            transforms.add(new Rotate(180, Rotate.Y_AXIS));
                    }
                    break;
                case U:
                    switch (tiles[1].getFace()) {
                        case R:
                            transforms.add(new Rotate(-90, Rotate.Y_AXIS));
                            break;
                        case L:
                            transforms.add(new Rotate(90, Rotate.Y_AXIS));
                            break;
                        case B:
                            transforms.add(new Rotate(180, Rotate.Y_AXIS));
                    }
                    break;
                case D:
                    transforms.add(new Rotate(180, Rotate.X_AXIS));
                    switch (tiles[1].getFace()) {
                        case R:
                            transforms.add(new Rotate(-90, Rotate.Y_AXIS));
                            break;
                        case L:
                            transforms.add(new Rotate(90, Rotate.Y_AXIS));
                            break;
                        case F:
                            transforms.add(new Rotate(180, Rotate.Y_AXIS));
                    }
                    break;
                case F:
                    transforms.add(new Rotate(90, Rotate.X_AXIS));
                    switch (tiles[1].getFace()) {
                        case R:
                            transforms.add(new Rotate(-90, Rotate.Y_AXIS));
                            break;
                        case L:
                            transforms.add(new Rotate(90, Rotate.Y_AXIS));
                            break;
                        case U:
                            transforms.add(new Rotate(180, Rotate.Y_AXIS));
                    }
                    break;
                case B:
                    transforms.add(new Rotate(-90, Rotate.X_AXIS));
                    switch (tiles[1].getFace()) {
                        case R:
                            transforms.add(new Rotate(-90, Rotate.Y_AXIS));
                            break;
                        case L:
                            transforms.add(new Rotate(90, Rotate.Y_AXIS));
                            break;
                        case D:
                            transforms.add(new Rotate(180, Rotate.Y_AXIS));
                    }
            }
        }
    }

    public ArrayList<MeshView> getMeshViews() {
        ArrayList<MeshView> meshes = new ArrayList<>();
        for (int x = 0; x <= 2; x++) {
            for (int y = 0; y <= 2; y++) {
                for (int z = 0; z <= 2; z++) {
                    if (x-1 != 0 || y-1 !=0 || z-1 != 0) {
                        meshes.add(cube3d[x][y][z]);
                    }
                }
            }
        }
        return meshes;
    }

    public void animate(String input) {
        String[] animatesAndBlanks = input.split(" ");
        int numBlanks = 0;
        for (String animateOrBlank : animatesAndBlanks) {
            if (animateOrBlank.equals("")) {
                numBlanks++;
            }
        }
        String[] animations = new String[animatesAndBlanks.length - numBlanks];
        int j = 0;
        for (String animateOrBlank : animatesAndBlanks) {
            if (!animateOrBlank.equals("")) {
                animations[j++] = animateOrBlank;
            }
        }
        animate(animations);
    }

    public void animate(String... animations) {
        if (!animating) {
            stack.clear();
            if (isValidNotation(animations)) {
                animating = true;
                updateTlams(animations);
                execTlams();
            }
        } else {
            stack.addAll(Arrays.asList(animations));
        }
    }

    private void updateTlams(String... animations) {
        tlams = new ArrayList<>();
        for (String animation : animations) {
            tlams.add(new Tlam(getTl(animation.trim()), animation));
        }
    }

    private void execTlams() {
        Timeline tl = tlams.get(counter.getCount()).tl;
        final String move = tlams.get(counter.getCount()).move;
        ct.highlightMove(move);
        tl.play();
        tl.setOnFinished((ActionEvent e) -> {
            updateCube(move);
            app.changeMeshViews();
            ct.removeHighlight();
            counter.increment();
            if (counter.getCount() < tlams.size()) {
                execTlams();
            } else {
                counter.reset();
                animating = false;
                ct.condenseChildren();
                if (stack.size() > 0) {
                    animate(stack.toArray(new String[stack.size()]));
                }
            }
        });
    }

    public boolean isAnimating() {
        return animating;
    }

    private Timeline getTl(String animation) {
        switch (animation) {
            case "R":
                return tlFromData(Axis.X, Direction.NORM, 1);
            case "R'":
                return tlFromData(Axis.X, Direction.PRIME, 1);
            case "R2":
                return tlFromData(Axis.X, Direction.SQUARED, 1);
            case "r":
                return tlFromData(Axis.X, Direction.NORM, 1, 0);
            case "r'":
                return tlFromData(Axis.X, Direction.PRIME, 1, 0);
            case "r2":
                return tlFromData(Axis.X, Direction.SQUARED, 1, 0);
            case "L":
                return tlFromData(Axis.X, Direction.NORM, -1);
            case "L'":
                return tlFromData(Axis.X, Direction.PRIME, -1);
            case "L2":
                return tlFromData(Axis.X, Direction.SQUARED, -1);
            case "l":
                return tlFromData(Axis.X, Direction.NORM, -1, 0);
            case "l'":
                return tlFromData(Axis.X, Direction.PRIME, -1, 0);
            case "l2":
                return tlFromData(Axis.X, Direction.SQUARED, -1, 0);
            case "U":
                return tlFromData(Axis.Y, Direction.NORM, 1);
            case "U'":
                return tlFromData(Axis.Y, Direction.PRIME, 1);
            case "U2":
                return tlFromData(Axis.Y, Direction.SQUARED, 1);
            case "u":
                return tlFromData(Axis.Y, Direction.NORM, 1, 0);
            case "u'":
                return tlFromData(Axis.Y, Direction.PRIME, 1, 0);
            case "u2":
                return tlFromData(Axis.Y, Direction.SQUARED, 1, 0);
            case "D":
                return tlFromData(Axis.Y, Direction.NORM, -1);
            case "D'":
                return tlFromData(Axis.Y, Direction.PRIME, -1);
            case "D2":
                return tlFromData(Axis.Y, Direction.SQUARED, -1);
            case "d":
                return tlFromData(Axis.Y, Direction.NORM, -1, 0);
            case "d'":
                return tlFromData(Axis.Y, Direction.PRIME, -1, 0);
            case "d2":
                return tlFromData(Axis.Y, Direction.SQUARED, -1, 0);
            case "F":
                return tlFromData(Axis.Z, Direction.NORM, -1);
            case "F'":
                return tlFromData(Axis.Z, Direction.PRIME, -1);
            case "F2":
                return tlFromData(Axis.Z, Direction.SQUARED, -1);
            case "f":
                return tlFromData(Axis.Z, Direction.NORM, -1, 0);
            case "f'":
                return tlFromData(Axis.Z, Direction.PRIME, -1, 0);
            case "f2":
                return tlFromData(Axis.Z, Direction.SQUARED, -1, 0);
            case "B":
                return tlFromData(Axis.Z, Direction.NORM, 1);
            case "B'":
                return tlFromData(Axis.Z, Direction.PRIME, 1);
            case "B2":
                return tlFromData(Axis.Z, Direction.SQUARED, 1);
            case "b":
                return tlFromData(Axis.Z, Direction.NORM, 1, 0);
            case "b'":
                return tlFromData(Axis.Z, Direction.PRIME, 1, 0);
            case "b2":
                return tlFromData(Axis.Z, Direction.SQUARED, 1, 0);
            case "V":
                return tlFromData(Axis.X, Direction.NORM, 0);
            case "V'":
                return tlFromData(Axis.X, Direction.PRIME, 0);
            case "V2":
                return tlFromData(Axis.X, Direction.SQUARED, 0);
            case "v":
                return tlFromData(Axis.X, Direction.NORM, 1, -1);
            case "v'":
                return tlFromData(Axis.X, Direction.PRIME, 1, -1);
            case "v2":
               return tlFromData(Axis.X, Direction.SQUARED, 1, -1);
            case "H":
                return tlFromData(Axis.Y, Direction.NORM, 0);
            case "H'":
                return tlFromData(Axis.Y, Direction.PRIME, 0);
            case "H2":
                return tlFromData(Axis.Y, Direction.SQUARED, 0);
            case "h":
                return tlFromData(Axis.Y, Direction.NORM, 1, -1);
            case "h'":
                return tlFromData(Axis.Y, Direction.PRIME, 1, -1);
            case "h2":
                return tlFromData(Axis.Y, Direction.SQUARED, 1, -1);
            case "C":
                return tlFromData(Axis.Z, Direction.NORM, 0);
            case "C'":
                return tlFromData(Axis.Z, Direction.PRIME, 0);
            case "C2":
                return tlFromData(Axis.Z, Direction.SQUARED, 0);
            case "c":
                return tlFromData(Axis.Z, Direction.NORM, 1, -1);
            case "c'":
                return tlFromData(Axis.Z, Direction.PRIME, 1, -1);
            case "c2":
                return tlFromData(Axis.Z, Direction.SQUARED, 1, -1);
            case "x":
                return tlFromData(Axis.X, Direction.NORM, 1, -1, 0);
            case "x'":
                return tlFromData(Axis.X, Direction.PRIME, 1, -1, 0);
            case "x2":
                return tlFromData(Axis.X, Direction.SQUARED, 1, -1, 0);
            case "y":
                return tlFromData(Axis.Y, Direction.NORM, 1, -1, 0);
            case "y'":
                return tlFromData(Axis.Y, Direction.PRIME, 1, -1, 0);
            case "y2":
                return tlFromData(Axis.Y, Direction.SQUARED, 1, -1, 0);
            case "z":
                return tlFromData(Axis.Z, Direction.NORM, 1, -1, 0);
            case "z'":
                return tlFromData(Axis.Z, Direction.PRIME, 1, -1, 0);
            case "z2":
                return tlFromData(Axis.Z, Direction.SQUARED, 1, -1, 0);
        }
        return null;
    }

    private Timeline tlFromData(Axis axis, Direction dir, int... pos) {
        int turn = 0;
        Random rand = new Random();
        switch (axis) {
            case X:
                switch (dir) {
                    case NORM:
                        turn = -1;
                        break;
                    case PRIME:
                        turn = 1;
                        break;
                    case SQUARED:
                        turn = rand.nextInt() % 2 == 0 ? 2 : -2;
                }
                break;
            case Y:
            case Z:
                switch (dir) {
                    case NORM:
                        turn = 1;
                        break;
                    case PRIME:
                        turn = -1;
                        break;
                    case SQUARED:
                        turn = rand.nextInt() % 2 == 0 ? 2 : -2;
                }
        }
        if (((axis == Axis.X || axis == Axis.Y) && (pos.length < 3 && !contains(pos, 1)) && contains(pos, -1)) || (axis == Axis.Z && (pos.length < 3 && !contains(pos, -1)) && contains(pos, 1))) {
            turn *= -1;
        }
        final int turnFinal = turn;
        Timeline tl = null;
        switch (axis) {
            case X:
                tl = new Timeline(new KeyFrame(new Duration(at/90), (ActionEvent event) -> {
                    for (int coord : pos) {
                        for (int y = 0; y <= 2; y++) {
                            for (int z = 0; z <= 2; z++) {
                                MeshView mv = cube3d[coord + 1][y][z];
                                mv.setRotationAxis(Rotate.X_AXIS);
                                mv.setRotate(mv.getRotate() + turnFinal);
                            }
                        }
                    }
                }));
                break;
            case Y:
                tl = new Timeline(new KeyFrame(new Duration(at/90), (ActionEvent event) -> {
                    for (int coord : pos) {
                        for (int x = 0; x <= 2; x++) {
                            for (int z = 0; z <= 2; z++) {
                                MeshView mv = cube3d[x][coord + 1][z];
                                mv.setRotationAxis(Rotate.Y_AXIS);
                                mv.setRotate(mv.getRotate() + turnFinal);
                            }
                        }
                    }
                }));
                break;
            case Z:
                tl = new Timeline(new KeyFrame(new Duration(at/90), (ActionEvent event) -> {
                    for (int coord : pos) {
                        for (int x = 0; x <= 2; x++) {
                            for (int y = 0; y <= 2; y++) {
                                MeshView mv = cube3d[x][y][coord + 1];
                                mv.setRotationAxis(Rotate.Z_AXIS);
                                mv.setRotate(mv.getRotate() + turnFinal);
                            }
                        }
                    }
                }));
        }
        tl.setCycleCount(90);
        return tl;
    }

    private boolean contains(int[] pos, int coord) {
        for (int p : pos) {
            if (p == coord) {
                return true;
            }
        }
        return false;
    }

    /*private void update(Axis axis, int pos, Direction dir) {
        ArrayList<MeshView> mvs = new ArrayList<>();
        switch (axis) { //clock wise is opp for pos = -1 ******************************************
            case X:
                mvs.add(cube3d[pos+1][2][0]); //defined in clockwise order, indexes need to be 1 higher than coords
                mvs.add(cube3d[pos+1][2][1]);
                mvs.add(cube3d[pos+1][2][2]);
                mvs.add(cube3d[pos+1][1][2]);
                mvs.add(cube3d[pos+1][0][2]);
                mvs.add(cube3d[pos+1][0][1]);
                mvs.add(cube3d[pos+1][0][0]);
                mvs.add(cube3d[pos+1][1][0]);
                break;
            case Y:
                mvs.add(cube3d[0][pos+1][0]);
                mvs.add(cube3d[0][pos+1][1]);
                mvs.add(cube3d[0][pos+1][2]);
                mvs.add(cube3d[1][pos+1][2]);
                mvs.add(cube3d[2][pos+1][2]);
                mvs.add(cube3d[2][pos+1][1]);
                mvs.add(cube3d[2][pos+1][0]);
                mvs.add(cube3d[1][pos+1][0]);
                break;
            case Z:
                mvs.add(cube3d[0][2][pos+1]);
                mvs.add(cube3d[1][2][pos+1]);
                mvs.add(cube3d[2][2][pos+1]);
                mvs.add(cube3d[2][1][pos+1]);
                mvs.add(cube3d[2][0][pos+1]);
                mvs.add(cube3d[1][0][pos+1]);
                mvs.add(cube3d[0][0][pos+1]);
                mvs.add(cube3d[0][1][pos+1]);
            }
        shift(mvs, dir);
        switch (axis) {
            case X:
                cube3d[pos+1][2][0] = mvs.get(0);
                cube3d[pos+1][2][1] = mvs.get(1);
                cube3d[pos+1][2][2] = mvs.get(2);
                cube3d[pos+1][1][2] = mvs.get(3);
                cube3d[pos+1][0][2] = mvs.get(4);
                cube3d[pos+1][0][1] = mvs.get(5);
                cube3d[pos+1][0][0] = mvs.get(6);
                cube3d[pos+1][1][0] = mvs.get(7);
                break;
            case Y:
                cube3d[0][pos+1][0] = mvs.get(0);
                cube3d[0][pos+1][1] = mvs.get(1);
                cube3d[0][pos+1][2] = mvs.get(2);
                cube3d[1][pos+1][2] = mvs.get(3);
                cube3d[2][pos+1][2] = mvs.get(4);
                cube3d[2][pos+1][1] = mvs.get(5);
                cube3d[2][pos+1][0] = mvs.get(6);
                cube3d[1][pos+1][0] = mvs.get(7);
                break;
            case Z:
                cube3d[0][2][pos+1] = mvs.get(0);
                cube3d[1][2][pos+1] = mvs.get(1);
                cube3d[2][2][pos+1] = mvs.get(2);
                cube3d[2][1][pos+1] = mvs.get(3);
                cube3d[2][0][pos+1] = mvs.get(4);
                cube3d[1][0][pos+1] = mvs.get(5);
                cube3d[0][0][pos+1] = mvs.get(6);
                cube3d[0][1][pos+1] = mvs.get(7);
            }
    }

    private void shift(ArrayList<MeshView> mvs, Direction dir) {
        int times = 0;
        switch (dir) {
            case NORM:
                times = 2;
                break;
            case PRIME:
                times = 6;
                break;
            case SQUARED:
                times = 4;
        }
        for (int i = 0; i < times; i++) {
            MeshView last = mvs.get(mvs.size() - 1);
            for (int k = mvs.size() - 1; k > 0; k--) {
                mvs.set(k, mvs.get(k-1));
            }
            mvs.set(0, last);
        }
    }*/

    private static class Counter {

        private int count;

        public Counter() {
            count = 0;
        }

        public int getCount() {
            return count;
        }

        public void increment() {
            count++;
        }

        public void reset() {
            count = 0;
        }
    }

    private static class Tlam {

        public Timeline tl;
        //public Axis axis;
        //public int pos;
        //public Direction dir;
        public String move;

        public Tlam(Timeline tl, String move) {
            this.tl = tl;
            //this.axis = axis;
            //this.pos = pos;
            //this.dir = dir;
            this.move = move;
        }
    }

    private enum Axis {
        X, Y, Z;
    }

    private enum Direction {
        NORM, PRIME, SQUARED;
    }
}