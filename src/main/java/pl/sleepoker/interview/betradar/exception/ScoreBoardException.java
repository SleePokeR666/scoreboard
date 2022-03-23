package pl.sleepoker.interview.betradar.exception;

import java.io.Serial;

public class ScoreBoardException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1444886086884843388L;

    public ScoreBoardException(String message) {
        super(message);
    }

    public ScoreBoardException(String message, Throwable cause) {
        super(message, cause);
    }
}
