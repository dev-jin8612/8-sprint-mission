package com.sprint.mission.discodeit.exception.storage;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class StorageAlreadyExistsException extends StorageException {

  public StorageAlreadyExistsException(Object details) {
    super(ErrorCode.DUPLICATE_STORAGE, Map.of("이미 존재하는 이미지 입니다. : ",details));
  }
}
