package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.channel.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.data.ReadStatusDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "ReadStatus API", description = "ReadStatus 관련 API")
public interface ReadStatusApi {

  @Operation(summary = "방문기록 생성", description = "유저의 채널 접속 정보를 기록합니다.")
  ResponseEntity<ReadStatusDTO> createReadStatus(
      @Parameter(description = "유저 ID와 채널 ID 정보")
      @RequestBody ReadStatusCreateRequest readStatusCreateRequest
  );

  @Operation(summary = "방문기록 수정", description = "유저의 채널 접속 정보를 수정합니다.")
  ResponseEntity<ReadStatusDTO> updateReadStatus(
      @Parameter(description = "수정할 방문기록 ID")
      @PathVariable UUID readStatusId
  );

  @Operation(summary = "방문기록 조회", description = "유저의 모든 채널 방문기록을 조회합니다.")
  ResponseEntity<List<ReadStatusDTO>> findAllByUserId(
      @Parameter(description = "조회할 유저 ID")
      @RequestParam UUID userId
  );
}
