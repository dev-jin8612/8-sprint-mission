package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.storage.local.LocalBinaryContentStorage;
import com.sprint.mission.discodeit.storage.prod.S3BinaryContentStorage;
import java.nio.file.Path;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class StorageConfig {

  @Bean
  @ConditionalOnProperty(prefix = "discodeit.storage", name = "type", havingValue = "local", matchIfMissing = true)
  public BinaryContentStorage localBinaryContentStorage(StorageProperties props) {
    return new LocalBinaryContentStorage(Path.of(props.local().rootPath()));
  }

  @Bean
  @ConditionalOnProperty(prefix = "discodeit.storage", name = "type", havingValue = "s3")
  public BinaryContentStorage s3BinaryContentStorage(StorageProperties props) {
    return new S3BinaryContentStorage(props.s3());
  }
}