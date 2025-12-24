package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.channel.ChannelUpdateReqeust;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Repository
@ConditionalOnProperty(
        name = "discodeit.repository.type",
        havingValue = "file",
        matchIfMissing = true
)
public class FileChannelRepository extends SaveLoadHelper implements ChannelRepository {
    private final Path directory;
    private final Path file;
    private Map<UUID, Channel> channels;

    public FileChannelRepository(
            @Value("${discodeit.repository.file-directory}") String dir
    ) {
        this.directory = Paths.get(dir);
        this.file = directory.resolve("ch.ser");
        init(directory);
        channels = load(file);
    }

    @Override
    public Channel create(Channel channel) {
        channels.put(channel.getId(), channel);
        save(file, channels);
        return channel;
    }

    @Override
    public Channel update(ChannelUpdateReqeust dto) {
        Channel channel = Optional.ofNullable(channels.get(dto.chId()))
                .orElseThrow(() -> new NoSuchElementException("채널이 없습니다."));

        channel.update(dto.name(), dto.type());
        save(file, channels);
        return channel;
    }

    @Override
    public void delete(UUID channelId) {

        channels.remove(channelId);
        save(file, channels);
    }

    @Override
    public List<Channel> searchByName(List<String> name) {
        List<Channel> ch = channels.values().stream()
                .filter(cha ->
                        name.stream().anyMatch(na -> cha.getName().contains(na))
                ).toList();

        return Optional.ofNullable(ch)
                .orElse(null);
    }

    @Override
    public Channel findById(UUID id) {
        return Optional.ofNullable(channels.get(id))
                .orElse(null);
    }

    @Override
    public Map<UUID, Channel> getChannelList() {
        return Optional.ofNullable(channels)
                .orElse(null);
    }
}
