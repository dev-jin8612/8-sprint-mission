package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  List<Message> findAllByChannelId(UUID channelIds);

  @Query("""
    select m.channel.id, max(m.createdAt)
    from Message m
    where m.channel.id in :channelIds
    group by m.channel.id
  """)
  List<Object[]> findLastMessageTimes(List<UUID> channelIds);

  void deleteAllByChannelId(UUID channelId);

  @Query("""
      select m from Message m
      join fetch m.channel
      where m.id = :id
      """)
  Optional<Message> findWithChannel(UUID id);
}
