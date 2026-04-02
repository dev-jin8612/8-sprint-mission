package com.sprint.mission.discodeit.sliceTest;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(JpaAuditTestConfig.class)
class UserRepositorySliceTest {

  @Autowired UserRepository userRepository;
  @Autowired EntityManager em;

  private User persistUser(String username, String email) {
    User user = userRepository.save(new User(username, email, "pw", null));
    em.flush();
    em.clear();
    return user;
  }

  @Test
  void existsByEmail_success_true_whenEmailExists() {
    // given
    persistUser("u1", "u1@test.com");

    // when
    boolean exists = userRepository.existsByEmail("u1@test.com");

    // then
    assertThat(exists).isTrue();
  }

  @Test
  void existsByEmail_failure_false_whenEmailNotExists() {
    // when
    boolean exists = userRepository.existsByEmail("none@test.com");

    // then
    assertThat(exists).isFalse();
  }

  @Test
  void existsByUsername_success_true_whenUsernameExists() {
    // given
    persistUser("u1", "u1@test.com");

    // when
    boolean exists = userRepository.existsByUsername("u1");

    // then
    assertThat(exists).isTrue();
  }

  @Test
  void existsByUsername_failure_false_whenUsernameNotExists() {
    // when
    boolean exists = userRepository.existsByUsername("none");

    // then
    assertThat(exists).isFalse();
  }

  @Test
  void findById_success_present_whenExists() {
    // given
    User saved = persistUser("u1", "u1@test.com");

    // when
    var found = userRepository.findById(saved.getId());

    // then
    assertThat(found).isPresent();
    assertThat(found.get().getEmail()).isEqualTo("u1@test.com");
  }

  @Test
  void findById_failure_empty_whenNotExists() {
    // when
    var found = userRepository.findById(UUID.randomUUID());

    // then
    assertThat(found).isEmpty();
  }

  @Test
  void findAllWithProfileAndStatus_failure_returnsEmpty_whenNoUsers() {
    // when
    List<User> users = userRepository.findAllWithProfileAndStatus();

    // then
    assertThat(users).isEmpty();
  }

  @Test
  void findAll_pagingSorting_success_returnsFirstPage_sortedByUsernameAsc() {
    // given
    persistUser("b", "b@test.com");
    persistUser("a", "a@test.com");
    persistUser("c", "c@test.com");

    PageRequest pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "username"));

    // when
    Page<User> page = userRepository.findAll(pageable);

    // then
    assertThat(page.getContent()).hasSize(2);
    assertThat(page.getContent().get(0).getUsername()).isEqualTo("a");
    assertThat(page.getContent().get(1).getUsername()).isEqualTo("b");
    assertThat(page.hasNext()).isTrue();
  }

  @Test
  void findAll_pagingSorting_failure_returnsEmptyPage_whenOutOfRange() {
    // given
    persistUser("a", "a@test.com");

    PageRequest pageable = PageRequest.of(10, 2, Sort.by("username"));

    // when
    Page<User> page = userRepository.findAll(pageable);

    // then
    assertThat(page.getContent()).isEmpty();
    assertThat(page.hasNext()).isFalse();
  }
}
