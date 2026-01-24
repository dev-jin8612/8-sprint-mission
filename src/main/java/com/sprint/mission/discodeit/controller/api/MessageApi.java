package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.data.MessageDTO;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Message API", description = "Message 관련 API")
public interface MessageApi {

  @Operation(summary = "Message 생성", description = "메세지를 생성합니다.")
  ResponseEntity<MessageDTO> createMessage(
      @Parameter(description = "메세지 내용과 보낸 채널, 보낸 사람의 정보")
      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,

      @Parameter(description = "메세지로 보낸 파일")
      @RequestPart(name = "attachments", required = false)
      List<org.springframework.web.multipart.MultipartFile> imgList
  ) throws IOException;

  @Operation(summary = "Message 수정", description = "메세지를 수정합니다.")
  ResponseEntity<MessageDTO> updateMessage(
      @Parameter(description = "수정할 메세지 ID")
      @PathVariable UUID messageId,

      @Parameter(description = "수정할 메세지 내용")
      @RequestParam MessageUpdateRequest newContent
  );

  @Operation(summary = "Message 탐색", description = "채널 안의 메세지를 전부 조회합니다.")
  ResponseEntity<List<MessageDTO>> searchMessage(
      @Parameter(description = "찾을 채널 ID")
      @RequestParam UUID channelId
  );

  @Operation(summary = "Message 삭제", description = "메세지를 삭제합니다.")
  void deleteMessage(
      @Parameter(description = "삭제할 메세지 ID")
      @PathVariable UUID messageId
  );
}
