package hello.hellospring.Error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred.");
    }
    @ExceptionHandler(InvalidValueException.class)
    protected ResponseEntity<ErrorResponse> InvalidhandleExceptio(InvalidValueException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus())
                .body(new ErrorResponse(e.getErrorCode().getStatus(),e.getErrorCode().getCode(),e.getErrorCode().getMessage()));
        // 이 예제에서는 간단하게 메시지만 반환하고 있지만, 실제 애플리케이션에서는
        // errorCode, message 및 기타 관련 정보를 포함하는 객체를 반환할 수도 있습니다.
    }

}
