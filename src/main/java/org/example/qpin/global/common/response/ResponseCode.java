package org.example.qpin.global.common.response;

import lombok.Getter;

@Getter
public enum ResponseCode {
    /*
        1000번대 : 성공 관련
    */
    SUCCESS(1000, true, "요청에 성공하였습니다."),

    /*
        2000번대 : JWT & Auth 관련
    */


    /*
        3000번대 : Response 오류 관련
    */
    // 3000~ : member 관련 오류

    // 3100~ : qr 관련 오류

    // 3200~ : parking 관련 오류

    // 3300~ : safephonenumber 관련 오류

    // 3400~ : carphoto 관련 오류

    // 3500~ : insurance 관련 오류

    // 3600~ : scrap 관련 오류

    // =====================================

    /*
        8000번대 : 원인 불명 에러
    */
    INTERNAL_SERVER_ERROR(8000, false, "서버 오류가 발생했습니다.");


    private int code;
    private boolean inSuccess;
    private String message;


    /*
        해당되는 코드 매핑
        @param code
        @param inSuccess
        @param message

     */
    ResponseCode(int code, boolean inSuccess, String message) {
        this.inSuccess = inSuccess;
        this.code = code;
        this.message = message;
    }
}
