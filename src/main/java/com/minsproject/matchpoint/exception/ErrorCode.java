package com.minsproject.matchpoint.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // NOT_FOUND
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "팀을 찾을 수 없어요."),

    SPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 종목을 찾을 수 없어요."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원을 찾을 수 없어요."),

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "팀 멤버를 찾을 수 없어요."),

    DELETING_NOT_ALLOWED(HttpStatus.NOT_FOUND, "삭제할 수 없습니다."),

    TOKEN_NOU_FOUND(HttpStatus.NOT_FOUND, "토큰이 없습니다."),

    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "매칭 장소를 찾을 수 없습니다."),

    MATCH_NOT_FOUND(HttpStatus.NOT_FOUND, "매칭을 찾을 수 없습니다."),

    // UNAUTHORIZED
    MODIFICATION_NOT_ALLOWED(HttpStatus.UNAUTHORIZED, "수정할 수 없습니다."),

    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "회원이나 비밀번호를 확인해주세요."),

    MATCH_RESPOND_NOT_ALLOWED(HttpStatus.UNAUTHORIZED, "요청하신 매칭 응답에 권한이 없습니다."),

    // BAD_REQUEST
    DUPLICATED_USER_EMAIL(HttpStatus.BAD_REQUEST, "이미 가입한 이메일입니다."),

    MATCH_INVITER_CANNOT_MATCH(HttpStatus.BAD_REQUEST, "매칭 신청자가 매칭을 신청할 수 없는 상태입니다."),

    MATCH_INVITEE_CANNOT_MATCH(HttpStatus.BAD_REQUEST, "매칭을 신청할 수 없는 상대입니다."),

    ALREADY_IN_TEAM(HttpStatus.BAD_REQUEST, "이미 가입한 팀입니다."),

    MATCH_INVITE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "매칭 신청 권한이 없습니다."),

    TEAM_NOT_ACCEPTING_MATCHES(HttpStatus.BAD_REQUEST, "해당 팀은 현재 매칭을 받고 있지 않습니다."),

    ADDRESS_NOT_SUITABLE_FOR_MATCH(HttpStatus.BAD_REQUEST, "매칭을 진행할 수 없는 주소입니다."),

    INVALID_MATCH_DAY(HttpStatus.BAD_REQUEST, "매칭 날짜는 오늘 이후여야 합니다."),

    INVALID_MATCH_PLACE(HttpStatus.BAD_REQUEST, "매칭 장소가 없습니다."),

    WRONG_PROVIDER(HttpStatus.BAD_REQUEST, "소셜로그인에 문제가 발생했습니다."),

    MEMBER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "해당 종목에 이미 운동 프로필을 생성했습니다."),

    MEMBER_DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),

    INVALID_LEVEL(HttpStatus.BAD_REQUEST, "올바르지 않은 레벨입니다."),

    MATCH_INVALID_STATUS(HttpStatus.BAD_REQUEST, "올바른 매칭 응답 요청이 아닙니다."),

    ;

    private HttpStatus status;
    private String message;
}
