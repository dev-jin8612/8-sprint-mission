package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.MessageDTO;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.user.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage storage;
  private final MessageMapper  messageMapper;


  @Override
  public Message create(
      MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests
  ) {
    Channel channel = channelRepository.findById(messageCreateRequest.channelId()).orElse(null);
    User user = userRepository.findById(messageCreateRequest.authorId()).orElse(null);

    if (!channelRepository.existsById(channel.getId())) {
      throw new NoSuchElementException("Channel with id " + channel.getId() + " does not exist");
    }
    if (!userRepository.existsById(messageCreateRequest.authorId())) {
      throw new NoSuchElementException(
          "Author with id " + messageCreateRequest.authorId() + " does not exist");
    }

    List<BinaryContent> attachmentIds = binaryContentCreateRequests.stream()
        .map(attachmentRequest -> {
          byte[] bytes = attachmentRequest.bytes();

          BinaryContent binaryContent =
              binaryContentRepository.save(new BinaryContent(
                  attachmentRequest.fileName(),
                  (long) bytes.length,
                  attachmentRequest.contentType()
              ));

          storage.put(binaryContent.getId(), bytes);
          return binaryContent;
        }).toList();

    return messageRepository.save(new Message(
        messageCreateRequest.content(),
        channel, user, attachmentIds
    ));
  }

  @Override
  @Transactional(readOnly = true)
  public MessageDTO find(UUID messageId) {
    Message message= messageRepository.findById(messageId).orElseThrow(
        () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    return messageMapper.toDto(message);
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    return messageRepository.findAllByChannelId(channelId).stream().toList();
  }

  @Override
  public Message update(UUID messageId, MessageUpdateRequest request) {
    String newContent = request.newContent();
    Message message = messageRepository.findById(messageId).orElseThrow(
        () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    message.update(newContent);
    return messageRepository.save(message);
  }

  @Override
  public void delete(UUID messageId) {
    Message message = messageRepository.findById(messageId).orElseThrow(
        () -> new NoSuchElementException("Message with id " + messageId + " not found"));

    message.getAttachmentIds()
        .forEach(binaryContent -> binaryContentRepository.delete(binaryContent));

    messageRepository.deleteById(messageId);
  }
}
