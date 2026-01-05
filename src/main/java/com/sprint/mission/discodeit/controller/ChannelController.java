package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.data.ChannelResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
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
@RequestMapping("/channel")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  // 공개 채널 생성
  @PostMapping("/public")
  public ResponseEntity<Channel> createPublicChannel(
      @RequestBody PublicChannelCreateRequest channelCreateRequest) {
    if (channelCreateRequest.name() == null) {
      throw new IllegalArgumentException("Channel name is null");
    }

    Channel channel = channelService.create(channelCreateRequest);
    log.info(channel.getName() + "채널 생성까지는 성공");
    return ResponseEntity.ok(channel);
  }

  // 비공개 채널 생성
  @PostMapping("/private")
  public ResponseEntity<Channel> createPrivateChannel(
      @RequestBody PrivateChannelCreateRequest channelCreateRequest) {
    Channel channel = channelService.create(channelCreateRequest);
    log.info("비공개 채널 생성까지는 성공");
    return ResponseEntity.ok(channel);
  }

  // 채널 수정
  @PutMapping("/{channelName}")
  public ResponseEntity<ChannelResponse> updateChannel(
      @PathVariable String channelName,
      @RequestBody PublicChannelUpdateRequest request
  ) {
    ChannelResponse channel = channelService.findByName(channelName);
    channelService.update(channel.id(), request);

    log.info(channel.name() + "에서 " + request.newName() + " 수정까지는 성공");
    return ResponseEntity.ok(channel);
  }

  // 전체 조회
  @GetMapping
  public ResponseEntity<List<ChannelResponse>> findAll(@RequestParam("userId") UUID userId) {
    List<ChannelResponse> channels = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(channels);
  }

  // 채널 삭제
  @DeleteMapping("/{channelId}")
  public void deleteChannel(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    log.info("삭제까지는 성공");
  }
}
