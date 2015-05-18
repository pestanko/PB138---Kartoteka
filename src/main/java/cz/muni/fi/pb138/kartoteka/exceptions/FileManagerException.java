package cz.muni.fi.pb138.kartoteka.exceptions;

/**
 * Represents {@link cz.muni.fi.pb138.kartoteka.loaders.FileManager} related error
 *
 * @author Peter Stanko
 * @author Dominik Labuda
 * @author Peter Zaoral
 * @version 2015-05-18
 */
public class FileManagerException extends Exception {
    /**
     * Non-parametric constructor
     */
    public FileManagerException() {
    }
    /**
     * Constructor with message
     * @param message error message
     */
    public FileManagerException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     * @param message error message
     * @param cause error cause
     */
    public FileManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with cause
     * @param cause error cause
     */
    public FileManagerException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor with message, cause, suppression and writable stack trace
     * @param message error message
     * @param cause error cause
     * @param enableSuppression allows suppression
     * @param writableStackTrace makes stack writable
     */
    public FileManagerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
