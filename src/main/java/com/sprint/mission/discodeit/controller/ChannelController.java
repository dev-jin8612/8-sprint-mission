package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.data.ChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/channels")
@Tag(name = "Channel API", description = "Channel 관련 API")
public class ChannelController {

  private final ChannelService channelService;

  // 공개 채널 생성
  @PostMapping("/public")
  @Operation(summary = "Public Channel 생성", description = "공개 채널을 생성합니다.")
  public ResponseEntity<Channel> createPublicChannel(
      @Parameter(description = "채널의 이름과 설명이 들어가 있습니다.")
      @RequestBody PublicChannelCreateRequest channelCreateRequest
  ) {
    if (channelCreateRequest.name() == null) {
      throw new IllegalArgumentException("Channel name is null");
    }
    Channel channel = channelService.createPublic(channelCreateRequest);
    log.info(channel.getName() + "채널 생성까지는 성공");
    return ResponseEntity.ok(channel);
  }

  // 비공개 채널 생성
  @PostMapping("/private")
  @Operation(summary = "Private Channel 생성", description = "비공개 채널을 생성합니다.")
  public ResponseEntity<Channel> createPrivateChannel(
      @Parameter(description = "초대할 사람들의 UUID가 들어가 있습니다.")
      @RequestBody PrivateChannelCreateRequest channelCreateRequest) {
    Channel channel = channelService.createPrivate(channelCreateRequest);
    log.info("비공개 채널 생성까지는 성공");
    return ResponseEntity.ok(channel);
  }

  // 채널 수정
  @PutMapping("/{channelName}")
  @Operation(summary = "Public Channel 수정", description = "공개 채널을 이름/설명을 수정합니다.")
  public ResponseEntity<ChannelDTO> updateChannel(
      @Parameter(description = "찾을려는 공개 채널 이름")
      @PathVariable String channelName,
      @Parameter(description = "공개 채널의 새이름과 새로운 설명이 들어 있습니다.")
      @RequestBody PublicChannelUpdateRequest request
  ) {
    ChannelDTO channel = channelService.findByName(channelName);
    channelService.update(channel.id(), request);

    log.info(channel.name() + "에서 " + request.newName() + " 수정까지는 성공");
    return ResponseEntity.ok(channel);
  }

  // 전체 조회
  @GetMapping
  @Operation(summary = "Public Channel 전체 조회", description = "유저의 모든 공개 채널을 조회합니다.")
  public ResponseEntity<List<ChannelDTO>> findAll(
      @Parameter(description = "조회할 유저ID를 입력합니다.")
      @RequestParam("userId") UUID userId
  ) {
    List<ChannelDTO> channels = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(channels);
  }

  // 채널 삭제
  @DeleteMapping("/{channelId}")
  @Operation(summary = "Channel 삭제", description = "채널을 삭제합니다.")
  public void deleteChannel(
      @Parameter(description = "삭제할 채널ID를 입력합니다.")
      @PathVariable UUID channelId
  ) {
    channelService.delete(channelId);
    log.info("삭제까지는 성공");
  }
}
