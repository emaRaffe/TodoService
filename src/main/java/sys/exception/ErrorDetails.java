package sys.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ErrorDetails {

    private final Date date;
    private final String message;
    private final HttpStatus status;

}
