package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.MessageDTO;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.Channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.Message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.User.UserNotFoundException;
import com.sprint.mission.discodeit.exception.storage.StorageNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.S3Service;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Slf4j
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final MessageMapper messageMapper;
  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentRepository binaryContentRepository;
  private final PageResponseMapper pageResponseMapper;

  private final S3Service s3Service;  // S3Service 추가

  @Value("${spring.profiles.active}")
  private String activeProfile;  // 현재 활성 프로파일 확인

  @Transactional
  @Override
  public MessageDTO create(
      MessageCreateRequest messageCreateRequest,
      List<MultipartFile> files
  ) {
    UUID channelId = messageCreateRequest.channelId();
    UUID authorId = messageCreateRequest.authorId();

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelNotFoundException(channelId));

    User author = userRepository.findById(authorId)
        .orElseThrow(() -> new UserNotFoundException(authorId));

    List<BinaryContent> attachments = toAttachmentRequests(files).stream()
        .map(this::saveAndStore)
        .toList();

    Message message = new Message(messageCreateRequest.content(), channel, author, attachments);
    Message saved = messageRepository.save(message);
    return messageMapper.toDTO(saved);
  }

  private List<BinaryContentCreateRequest> toAttachmentRequests(List<MultipartFile> files) {
    if (files == null || files.isEmpty()) {
      return List.of();
    }

    return files.stream()
        .filter(file -> file != null && !file.isEmpty())
        .map(this::toCreateRequest)
        .toList();
  }

  private BinaryContentCreateRequest toCreateRequest(MultipartFile file) {
    try {

      // AWS 환경: S3에 업로드
      if ("prod".equals(activeProfile)) {
        s3Service.uploadFile(file);
      }

      return new BinaryContentCreateRequest(
          file.getOriginalFilename(),
          file.getContentType(),
          file.getBytes()
      );
    } catch (IOException e) {
      throw new StorageNotFoundException(e);
    }
  }

  private BinaryContent saveAndStore(BinaryContentCreateRequest req) {
    byte[] bytes = req.bytes();

    BinaryContent binaryContent = new BinaryContent(
        req.fileName(),
        (long) bytes.length,
        req.contentType()
    );

    BinaryContent saved = binaryContentRepository.save(binaryContent);

    if (!"prod".equals(activeProfile)) {
      binaryContentStorage.put(saved.getId(), bytes);
    }

    return saved;
  }


  @Transactional(readOnly = true)
  @Override
  public MessageDTO find(UUID messageId) {
    return messageRepository.findById(messageId)
        .map(messageMapper::toDTO)
        .orElseThrow(() -> new MessageNotFoundException(messageId));
  }

  @Transactional(readOnly = true)
  @Override
  public PageResponse<MessageDTO> findAllByChannelId(UUID channelId, Instant createAt,
      Pageable pageable) {
    Slice<MessageDTO> slice =
        messageRepository.findAllByChannelIdWithAuthor(
                channelId, Optional.ofNullable(createAt).orElse(Instant.now()), pageable)
            .map(messageMapper::toDTO);

    Instant nextCursor = null;
    if (!slice.getContent().isEmpty()) {
      nextCursor = slice.getContent().get(slice.getContent().size() - 1).createdAt();
    }

    return pageResponseMapper.fromSlice(slice, nextCursor);
  }

  @Transactional
  @Override
  public MessageDTO update(UUID messageId, MessageUpdateRequest request) {
    String newContent = request.newContent();

    Message message = messageRepository.findById(messageId).orElseThrow(
        () -> new MessageNotFoundException(messageId));

    message.update(newContent);
    return messageMapper.toDTO(message);
  }

  @Transactional
  @Override
  public void delete(UUID messageId) {
    if (!messageRepository.existsById(messageId)) {
      throw new MessageNotFoundException(messageId);
    }

    messageRepository.deleteById(messageId);
  }
}
