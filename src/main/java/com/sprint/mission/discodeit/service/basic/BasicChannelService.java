package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelReqeust;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    //
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public Channel create(PublicChannelCreateRequest request) {
        String name = request.name();
        String description = request.description();
        Channel channel = new Channel(ChannelType.PUBLIC, name, description);

        return channelRepository.save(channel);
    }

    @Override
    public Channel create(PrivateChannelCreateRequest request) {
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        Channel createdChannel = channelRepository.save(channel);

        request.participantIds().stream()
                .map(userId -> new ReadStatus(userId, createdChannel.getId(), Instant.MIN))
                .forEach(readStatusRepository::save);

        return createdChannel;
    }

    @Override
    public ChannelReqeust find(UUID channelId) {
        return channelRepository.findById(channelId)
                .map(this::toDto)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    }

    @Override
    public List<ChannelReqeust> findAllByUserId(UUID userId) {
        List<UUID> mySubscribedChannelIds =
                readStatusRepository.findAllByUserId(userId).stream()
                        .map(ReadStatus::getChannelId)
                        .toList();

        return channelRepository.findAll().stream()
                .filter(channel ->
                        channel.getType().equals(ChannelType.PUBLIC)
                                || mySubscribedChannelIds.contains(channel.getId())
                )
                .map(this::toDto)
                .toList();
    }

    @Override
    public ChannelReqeust findByName(String channelName) {
        // 나중에는 방 개설자인지도 확인하는게 필요할것 같다.

        ChannelReqeust channelReqeust =
                channelRepository.findAll().stream()
                        .filter(channel ->
                                // private 채널을 이름이 없으니 없으면 넘어가게 만들기
                                channel.getName() != null &&
                                        !channel.getName().isEmpty() &&
                                        channel.getName().equals(channelName)
                        )
                        .map(this::toDto).findFirst().orElse(null);

        return channelReqeust;
    }

    @Override
    public Channel update(UUID channelId, PublicChannelUpdateRequest request) {
        String newName = request.newName();
        String newDescription = request.newDescription();

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));

        if (channel.getType().equals(ChannelType.PRIVATE)) {
            throw new IllegalArgumentException("Private channel cannot be updated");
        }

        channel.update(newName, newDescription);
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));

        messageRepository.deleteAllByChannelId(channel.getId());
        readStatusRepository.deleteAllByChannelId(channel.getId());

        channelRepository.deleteById(channelId);
    }

    private ChannelReqeust toDto(Channel channel) {
        Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId())
                .stream()
                .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
                .map(Message::getCreatedAt)
                .limit(1)
                .findFirst()
                .orElse(Instant.MIN);

        List<UUID> participantIds = new ArrayList<>();
        if (channel.getType().equals(ChannelType.PRIVATE)) {
            readStatusRepository.findAllByChannelId(channel.getId())
                    .stream()
                    .map(ReadStatus::getUserId)
                    .forEach(participantIds::add);
        }

        return new ChannelReqeust(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                participantIds,
                lastMessageAt
        );
    }
}
