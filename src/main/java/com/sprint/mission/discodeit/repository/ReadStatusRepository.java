package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  List<ReadStatus> findAllByChannelId(UUID channelId);

  List<ReadStatus> findAllByUserId(UUID userId);

  void deleteAllByChannelId(UUID channelId);


  @Query("""
    select rs
    from ReadStatus rs
    join fetch rs.channel
    where rs.user.id = :userId
  """)
  List<ReadStatus> findAllByUserIdWithChannel(UUID userId);

  @Query("""
    select rs
    from ReadStatus rs
    join fetch rs.user
    where rs.channel.id in :channelIds
  """)
  List<ReadStatus> findAllByChannelIds(List<UUID> channelIds);
}
