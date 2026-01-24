package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ChannelApi;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.data.ChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/channels")
public class ChannelController implements ChannelApi {

  private final ChannelService channelService;

  @Override
  @PostMapping("/public")
  public ResponseEntity<Channel> createPublicChannel(
      @RequestBody PublicChannelCreateRequest channelCreateRequest
  ) {
    if (channelCreateRequest.name() == null) {
      throw new IllegalArgumentException("Channel name is null");
    }
    Channel channel = channelService.createPublic(channelCreateRequest);
    log.info("{} 채널 생성까지는 성공", channel.getName());
    return ResponseEntity.ok(channel);
  }

  @Override
  @PostMapping("/private")
  public ResponseEntity<Channel> createPrivateChannel(
      @RequestBody PrivateChannelCreateRequest channelCreateRequest
  ) {
    Channel channel = channelService.createPrivate(channelCreateRequest);
    log.info("비공개 채널 생성까지는 성공");
    return ResponseEntity.ok(channel);
  }

  @Override
  @PutMapping("/{channelName}")
  public ResponseEntity<ChannelDTO> updateChannel(
      @PathVariable String channelName,
      @RequestBody PublicChannelUpdateRequest request
  ) {
    ChannelDTO channel = channelService.findByName(channelName);
    channelService.update(channel.id(), request);

    log.info("{} → {} 수정까지는 성공", channel.name(), request.newName());
    return ResponseEntity.ok(channel);
  }

  @Override
  @GetMapping
  public ResponseEntity<List<ChannelDTO>> findAll(
      @RequestParam("userId") UUID userId
  ) {
    List<ChannelDTO> channels = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(channels);
  }

  @Override
  @DeleteMapping("/{channelId}")
  public void deleteChannel(@PathVariable UUID channelId) {
    channelService.delete(channelId);
    log.info("삭제까지는 성공");
  }
}
