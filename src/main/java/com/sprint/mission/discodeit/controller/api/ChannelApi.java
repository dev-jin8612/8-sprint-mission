package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.data.ChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Channel API", description = "Channel 관련 API")
public interface ChannelApi {

  @Operation(summary = "Public Channel 생성", description = "공개 채널을 생성합니다.")
  ResponseEntity<Channel> createPublicChannel(
      @Parameter(description = "채널의 이름과 설명이 들어가 있습니다.")
      @RequestBody PublicChannelCreateRequest channelCreateRequest
  );

  @Operation(summary = "Private Channel 생성", description = "비공개 채널을 생성합니다.")
  ResponseEntity<Channel> createPrivateChannel(
      @Parameter(description = "초대할 사람들의 UUID가 들어가 있습니다.")
      @RequestBody PrivateChannelCreateRequest channelCreateRequest
  );

  @Operation(summary = "Public Channel 수정", description = "공개 채널의 이름/설명을 수정합니다.")
  ResponseEntity<ChannelDTO> updateChannel(
      @Parameter(description = "수정할 공개 채널 이름")
      @PathVariable String channelName,

      @Parameter(description = "공개 채널의 새 이름과 새로운 설명")
      @RequestBody PublicChannelUpdateRequest request
  );

  @Operation(summary = "Public Channel 전체 조회", description = "유저의 모든 공개 채널을 조회합니다.")
  ResponseEntity<List<ChannelDTO>> findAll(
      @Parameter(description = "조회할 유저 ID")
      @RequestParam("userId") UUID userId
  );

  @Operation(summary = "Channel 삭제", description = "채널을 삭제합니다.")
  void deleteChannel(
      @Parameter(description = "삭제할 채널 ID")
      @PathVariable UUID channelId
  );
}
