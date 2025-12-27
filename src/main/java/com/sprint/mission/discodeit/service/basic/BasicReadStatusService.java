package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicReadStatusService implements ReadStatusService {
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatus create(ReadStatusCreateRequest request) {
        UUID userId = request.userId();
        UUID channelId = request.channelId();

        if (userRepository.findById(userId) == null) {
            throw new NoSuchElementException("User with id " + userId + " does not exist");
        }
        if (channelRepository.findById(channelId)==null) {
            throw new NoSuchElementException("Channel with id " + channelId + " does not exist");
        }

        if (readStatusRepository.findAll().get(userId)!=null) {
            throw new IllegalArgumentException("ReadStatus with userId " + userId + " and channelId " + channelId + " already exists");
        }

        Instant lastReadAt = request.lastReadAt();
        ReadStatus readStatus = new ReadStatus(channelId,userId, lastReadAt);
        return readStatusRepository.create(readStatus);
    }

    @Override
    public ReadStatus find(UUID readStatusId) {
        return readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new NoSuchElementException("ReadStatus with id " + readStatusId + " not found"));
    }

    @Override
    public ReadStatus findAllByUserId(UUID userId) {
        return readStatusRepository.findAll().get(userId);
    }

    @Override
    public ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest request) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
                .orElseThrow(() -> new NoSuchElementException("ReadStatus with id " + readStatusId + " not found"));
        readStatus.update();
        return readStatusRepository.create(readStatus);
    }

    @Override
    public void delete(UUID readStatusId) {
        if (readStatusRepository.findById(readStatusId) ==null) {
            throw new NoSuchElementException("ReadStatus with id " + readStatusId + " not found");
        }
        readStatusRepository.delete(readStatusId);
    }
}
