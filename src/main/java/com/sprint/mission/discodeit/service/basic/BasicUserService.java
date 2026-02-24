package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDTO;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.User.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.User.UserNotFoundException;
import com.sprint.mission.discodeit.exception.storage.StorageNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.S3Service;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Slf4j
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  private final S3Service s3Service;  // S3Service 추가

  @Value("${spring.profiles.active}")
  private String activeProfile;  // 현재 활성 프로파일 확인

  @Transactional
  @Override
  public UserDTO create(
      UserCreateRequest userCreateRequest,
      MultipartFile profile
  ) {

    Optional<BinaryContentCreateRequest> profileRequests =
        Optional.ofNullable(profile).flatMap(this::resolveProfileRequest);

    String username = userCreateRequest.username();
    String email = userCreateRequest.email();

    if (userRepository.existsByEmail(email)) {
      throw new UserAlreadyExistsException(email);
    }
    if (userRepository.existsByUsername(username)) {
      throw new UserAlreadyExistsException(username);
    }

    BinaryContent nullableProfile = profileRequests
        .map(profileRequest -> {
          String fileName = null;
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();

          if ("prod".equals(activeProfile)) {
            // AWS 환경: S3에 업로드
            fileName = s3Service.uploadFile(profile);
          } else {
            fileName = profileRequest.fileName();
          }

          BinaryContent binaryContent =
              new BinaryContent(fileName, (long) bytes.length, contentType);

          BinaryContent returnBinary = binaryContentRepository.save(binaryContent);

          if (!"prod".equals(activeProfile)) {
            binaryContentStorage.put(returnBinary.getId(), bytes);
          }

          return returnBinary;
        }).orElse(null);

    String password = userCreateRequest.password();
    User user = new User(username, email, password, nullableProfile);

    Instant now = Instant.now();
    UserStatus userStatus = new UserStatus(user, now);

    userRepository.save(user);
    return userMapper.toDTO(user);
  }

  @Override
  public UserDTO find(UUID userId) {
    return userRepository.findById(userId)
        .map(userMapper::toDTO)
        .orElseThrow(() -> new UserNotFoundException(userId));
  }

  @Override
  public List<UserDTO> findAll() {
    return userRepository.findAllWithProfileAndStatus().stream()
        .map(userMapper::toDTO)
        .toList();
  }

  @Transactional
  @Override
  public UserDTO update(
      UUID userId,
      UserUpdateRequest userUpdateRequest,
      MultipartFile profile
  ) {

    Optional<BinaryContentCreateRequest> profileRequests =
        Optional.ofNullable(profile).flatMap(this::resolveProfileRequest);

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

    String newUsername = userUpdateRequest.newUsername();
    String newEmail = userUpdateRequest.newEmail();

    if (userRepository.existsByEmail(newEmail)) {
      throw new UserAlreadyExistsException(newEmail);
    }
    if (userRepository.existsByUsername(newUsername)) {
      throw new UserAlreadyExistsException(newUsername);
    }

    BinaryContent nullableProfile = profileRequests
        .map(profileRequest -> {
          String fileName = null;
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();

          if ("prod".equals(activeProfile)) {
            // AWS 환경: S3에 업로드
            fileName = s3Service.uploadFile(profile);
          } else {
            fileName = profileRequest.fileName();
          }

          BinaryContent binaryContent =
              new BinaryContent(fileName, (long) bytes.length, contentType);

          BinaryContent returnBinary = binaryContentRepository.save(binaryContent);

          if (!"prod".equals(activeProfile)) {
            binaryContentStorage.put(returnBinary.getId(), bytes);
          }

          return returnBinary;
        }).orElse(null);

    String newPassword = userUpdateRequest.newPassword();
    user.update(newUsername, newEmail, newPassword, nullableProfile);
    
    return userMapper.toDTO(user);
  }

  @Transactional
  @Override
  public void delete(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new UserNotFoundException(userId);
    }

    userRepository.deleteById(userId);
  }


  private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile.isEmpty()) {
      return Optional.empty();
    } else {
      try {
        BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
            profileFile.getOriginalFilename(),
            profileFile.getContentType(),
            profileFile.getBytes()
        );
        return Optional.of(binaryContentCreateRequest);
      } catch (IOException e) {
        throw new StorageNotFoundException(e);
      }
    }
  }
}
