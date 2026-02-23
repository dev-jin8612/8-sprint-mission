package com.sprint.mission.discodeit.service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
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
class ChannelRepositorySliceTest {

  @Autowired ChannelRepository channelRepository;
  @Autowired EntityManager em;

  private Channel savePublic(String name) {
    Channel c = channelRepository.save(new Channel(ChannelType.PUBLIC, name, "desc-" + name));
    em.flush();
    em.clear();
    return c;
  }

  private Channel savePrivate() {
    Channel c = channelRepository.save(new Channel(ChannelType.PRIVATE, null, null));
    em.flush();
    em.clear();
    return c;
  }
  @Test
  void findAllByTypeOrIdIn_success_returnsPublicPlusPrivateInList() {
    // given
    Channel pub1 = savePublic("pub1");
    Channel pub2 = savePublic("pub2");
    Channel pri1 = savePrivate();
    Channel pri2 = savePrivate();

    // pri1만 포함시키고 pri2는 제외
    List<UUID> subscribed = List.of(pri1.getId());

    // when
    List<Channel> result = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, subscribed);

    // then: 공개채널 2개 + pri1 1개 = 3개
    assertThat(result).hasSize(3);
    assertThat(result).extracting(Channel::getId)
        .contains(pub1.getId(), pub2.getId(), pri1.getId())
        .doesNotContain(pri2.getId());
  }

  @Test
  void findAllByTypeOrIdIn_failure_returnsOnlyPublic_whenPrivateListEmpty() {
    // given
    Channel pub1 = savePublic("pub1");
    Channel pub2 = savePublic("pub2");
    savePrivate();

    // when
    List<Channel> result = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, List.of());

    // then: 공개만 반환
    assertThat(result).extracting(Channel::getId)
        .contains(pub1.getId(), pub2.getId());
    assertThat(result).allMatch(c -> c.getType() == ChannelType.PUBLIC);
  }

  @Test
  void findAll_pagingSorting_success_returnsFirstPage_sortedByNameAsc() {
    // given
    // PRIVATE는 name이 null이라 정렬 컬럼으로 부적절할 수 있으니 PUBLIC만 저장
    savePublic("b");
    savePublic("a");
    savePublic("c");

    PageRequest pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "name"));

    // when
    Page<Channel> page = channelRepository.findAll(pageable);

    // then
    assertThat(page.getContent()).hasSize(2);
    assertThat(page.getContent().get(0).getName()).isEqualTo("a");
    assertThat(page.getContent().get(1).getName()).isEqualTo("b");
    assertThat(page.hasNext()).isTrue();
  }

  @Test
  void findAll_pagingSorting_failure_returnsEmptyPage_whenOutOfRange() {
    // given
    savePublic("a");

    PageRequest pageable = PageRequest.of(10, 2, Sort.by("name"));

    // when
    Page<Channel> page = channelRepository.findAll(pageable);

    // then
    assertThat(page.getContent()).isEmpty();
    assertThat(page.hasNext()).isFalse();
  }
}
