package cz.muni.fi.pb138.kartoteka.exceptions;

/**
 * Created by Peter Stanko on 5/6/15.
 *
 * @author Peter Stanko
 */
public class CategoryException extends Exception {
    public CategoryException() {
    }

    public CategoryException(String message) {
        super(message);
    }

    public CategoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public CategoryException(Throwable cause) {
        super(cause);
    }

    public CategoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
