package com.sprint.mission.discodeit.exception.BinaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class BinaryContentNotFoundException extends BinaryContentException {

  public BinaryContentNotFoundException(Object details) {
    super(ErrorCode.BINARYCONTENT_NOT_FOUND, Map.of("파일이 없습니다. : ",details));
  }
}