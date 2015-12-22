package net.nzbget.client;

/**
 * Created by Matthias on 05.12.2015.
 */
public class NzbgetException extends RuntimeException {

    public NzbgetException() {
    }

    public NzbgetException(String detailMessage) {
        super(detailMessage);
    }

    public NzbgetException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NzbgetException(Throwable throwable) {
        super(throwable);
    }

}
