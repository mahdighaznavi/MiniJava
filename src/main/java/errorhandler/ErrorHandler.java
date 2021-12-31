package errorhandler;

/**
 * Created by Alireza on 6/28/2015.
 */
public final class ErrorHandler {
    private static boolean hasError = false;

    private ErrorHandler() {
        throw new UnsupportedOperationException();
    }

    public static void printError(String msg) {
        setHasError(true);
        System.out.println(msg);
    }

    public static boolean isHasError() {
        return hasError;
    }

    public static void setHasError(boolean hasError) {
        ErrorHandler.hasError = hasError;
    }
}
