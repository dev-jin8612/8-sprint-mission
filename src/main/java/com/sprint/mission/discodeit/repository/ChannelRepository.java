package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {

  @Query("""
    select distinct c
    from Channel c
    left join fetch c.readStatuses rs
    left join fetch rs.user
  """)
  List<Channel> findAllWithReadStatuses();

}
