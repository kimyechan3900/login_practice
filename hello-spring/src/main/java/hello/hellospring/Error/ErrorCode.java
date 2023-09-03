package hello.hellospring.Error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(500, "C_001", "서버에 문제가 발생하였습니다."),
    METHOD_NOT_ALLOWED(405, "C_002", "API는 열려있으나 메소드는 사용 불가합니다."),
    INVALID_INPUT_VALUE(400, "C_003", "적절하지 않은 요청 값입니다."),
    INVALID_TYPE_VALUE(400, "C_004", "요청 값의 타입이 잘못되었습니다."),
    ENTITY_NOT_FOUND(400, "C_005", "지정한 Entity를 찾을 수 없습니다."),

    MEMBER_NOT_FOUND(400, "ME_001", "사용자를 찾을 수 없습니다."),
    WRONG_PASSWORD(400, "ME_002","비밀번호가 일치하지 않습니다."),
    NO_REPORT_DATA(400, "ME_003", "지난 일주일 간 데이터를 분석 중입니다."),

    DUPLICATED_USERNAME(400, "AU_001", "이미 존재하는 닉네임입니다."),
    DUPLICATED_EMAIL(400, "AU_002", "이미 존재하는 E-mail입니다."),
    UNAUTHORIZED_REDIRECT_URI(400, "AU_003", "인증되지 않은 REDIRECT_URI입니다."),
    BAD_LOGIN(400, "AU_004", "잘못된 아이디 또는 패스워드입니다."),
    INVALID_PASSWORD(400, "AU_005", "잘못된 패스워드입니다."),
    ;

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

}