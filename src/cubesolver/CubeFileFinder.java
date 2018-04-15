package cubesolver;

/**
 *
 * @author Bruno Freeman
 */
public abstract class CubeFileFinder
{
    public static String getPath(String file) {
        return System.getProperty("user.dir") + "\\src\\resources\\" + file;
    }
}
