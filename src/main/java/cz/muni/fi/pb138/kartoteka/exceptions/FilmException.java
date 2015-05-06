package cz.muni.fi.pb138.kartoteka.exceptions;

/**
 * Created by Peter Stanko on 5/6/15.
 *
 * @author Peter Stanko
 */
public class FilmException extends Throwable {
    public FilmException(String s) {

    }

    public FilmException(String message, Throwable cause) {
        super(message, cause);
    }

    public FilmException(Throwable cause) {
        super(cause);
    }

    public FilmException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public FilmException() {
    }
}
