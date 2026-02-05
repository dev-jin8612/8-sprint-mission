package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  USER_NOT_FOUND("유저 없음",HttpStatus.NOT_FOUND),
  DUPLICATE_USER("이미 존재하는 유저", HttpStatus.CONFLICT),
  DUPLICATE_USERSTATUS ("이미 존재하는 유저", HttpStatus.CONFLICT),
  USERSTATUS_NOT_FOUND("채널 없음", HttpStatus.NOT_FOUND),
  LOGIN_FAILED("비밀번호 불일치", HttpStatus.CONFLICT),
  READSTATUS_NOT_FOUND("읽음 상태 없음", HttpStatus.NOT_FOUND),
  MESSAGE_NOT_FOUND("메세지 없음", HttpStatus.NOT_FOUND),
  BINARYCONTENT_NOT_FOUND("파일이 없음", HttpStatus.NOT_FOUND),
  CHANNEL_NOT_FOUND("채널 없음", HttpStatus.NOT_FOUND),
  PRIVATE_CHANNEL_UPDATE("비공개 채널은 수정 불가", HttpStatus.UNPROCESSABLE_ENTITY);

  HttpStatus status;
  String message;

  ErrorCode(String message, HttpStatus notFound) {
    this.message = message;
  }
}
