package hello.hellospring.Error;

import lombok.Getter;

@Getter
public class InvalidValueException extends RuntimeException {

    private final ErrorCode errorCode;

    public InvalidValueException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
