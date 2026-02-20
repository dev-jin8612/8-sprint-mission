package com.sprint.mission.discodeit.exception.storage;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.Map;

public class StorageNotFoundException extends StorageException {

  public StorageNotFoundException(Object details) {
    super(ErrorCode.STORAGE_NOT_FOUND,Map.of("이미지를 찾을 수 없습니다. : ",details));
  }
}
