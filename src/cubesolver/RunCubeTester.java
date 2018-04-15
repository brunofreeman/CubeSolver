package cubesolver;

import cubesolver.cube.Cube3x3x3;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author Bruno Freeman
 */
public class RunCubeTester {

    public static void main(String[] args) {
        String csp = CubeFileFinder.getPath("cubeState.txt");
        Scanner fileInput = null;
        try {
            fileInput = new Scanner(new File(csp));
        } catch (FileNotFoundException e) {
        }
        String[] cubeState = new String[Cube3x3x3.NUM_PIECES];
        for (int i = 0; i < Cube3x3x3.NUM_PIECES; i++) {
            cubeState[i] = fileInput.nextLine().substring(4);
        }
        CubeTester myCube = new CubeTester(new Cube3x3x3(cubeState));
        myCube.run();
    }
}
