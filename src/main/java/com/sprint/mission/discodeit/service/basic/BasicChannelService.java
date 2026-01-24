package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.data.ChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;

  @Override
  public Channel createPublic(PublicChannelCreateRequest request) {
    Channel channel = new Channel(
        ChannelType.PUBLIC,
        request.name(),
        request.description()
    );

    return channelRepository.save(channel);
  }

  @Override
  public Channel createPrivate(PrivateChannelCreateRequest request) {
    Channel createdChannel = channelRepository.save(
        new Channel(ChannelType.PRIVATE, null, null)
    );

//    User user = userRepository.

    request.participantIds().stream()
        .map(userId -> {
          User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
          return new ReadStatus(user, createdChannel, createdChannel.getCreatedAt());
        })
        .forEach(readStatusRepository::save);

    return createdChannel;
  }

  @Override
  public ChannelDTO find(UUID channelId) {
    return channelRepository.findById(channelId)
        .map(this::toDto)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " not found"));
  }

  @Override
  public List<ChannelDTO> findAllByUserId(UUID userId) {

    List<ReadStatus> myReadStatuses =
        readStatusRepository.findAllByUserIdWithChannel(userId);

    List<UUID> subscribedChannelIds =
        myReadStatuses.stream()
            .map(rs -> rs.getChannel().getId())
            .toList();

    List<Channel> channels =
        channelRepository.findAll().stream()
            .filter(c ->
                c.getType() == ChannelType.PUBLIC ||
                    subscribedChannelIds.contains(c.getId())
            )
            .toList();

    return toDto(channels);
  }


  @Override
  public ChannelDTO findByName(String channelName) {
    // 나중에는 방 개설자인지도 확인하는게 필요할것 같다.

    ChannelDTO channelDTO =
        channelRepository.findAll().stream()
            .filter(channel ->
                // private 채널을 이름이 없으니 없으면 넘어가게 만들기
                channel.getName() != null &&
                    !channel.getName().isEmpty() &&
                    channel.getName().equals(channelName)
            )
            .map(this::toDto).findFirst().orElse(null);

    return channelDTO;
  }

  @Override
  public Channel update(UUID channelId, PublicChannelUpdateRequest request) {
    String newName = request.newName();
    String newDescription = request.newDescription();

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " not found"));

    if (channel.getType().equals(ChannelType.PRIVATE)) {
      throw new IllegalArgumentException("Private channel cannot be updated");
    }

    channel.update(newName, newDescription);
    return channelRepository.save(channel);
  }

  @Override
  public void delete(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " not found"));

    messageRepository.deleteAllByChannelId(channel.getId());
    readStatusRepository.deleteAllByChannelId(channel.getId());

    channelRepository.deleteById(channelId);
  }

  private List<ChannelDTO> toDto(List<Channel> channels) {

    List<UUID> channelIds = channels.stream()
        .map(Channel::getId)
        .toList();

    Map<UUID, Instant> lastMessageMap =
        messageRepository.findLastMessageTimes(channelIds).stream()
            .collect(Collectors.toMap(
                row -> (UUID) row[0],
                row -> (Instant) row[1]
            ));

    Map<UUID, List<UUID>> participantsMap =
        readStatusRepository.findAllByChannelIds(channelIds).stream()
            .collect(Collectors.groupingBy(
                rs -> rs.getChannel().getId(),
                Collectors.mapping(rs -> rs.getUser().getId(), Collectors.toList())
            ));

    return channels.stream()
        .map(channel -> new ChannelDTO(
            channel.getId(),
            channel.getType(),
            channel.getName(),
            channel.getDescription(),
            participantsMap.getOrDefault(channel.getId(), List.of()),
            lastMessageMap.getOrDefault(channel.getId(), Instant.MIN)
        ))
        .toList();
  }

  private ChannelDTO toDto(Channel channel) {
    Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId()).stream()
        .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
        .map(Message::getCreatedAt)
        .limit(1)
        .findFirst()
        .orElse(Instant.MIN);

    List<UUID> participantIds = new ArrayList<>();
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      readStatusRepository.findAllByChannelId(channel.getId()).stream()
          .map(readStatus -> readStatus.getChannel().getId())
          .forEach(participantIds::add);
    }

    return new ChannelDTO(
        channel.getId(),
        channel.getType(),
        channel.getName(),
        channel.getDescription(),
        participantIds,
        lastMessageAt
    );
  }

}
