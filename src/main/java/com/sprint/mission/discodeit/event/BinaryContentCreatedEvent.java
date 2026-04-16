package com.sprint.mission.discodeit.event;

public record BinaryContentCreatedEvent(
        // TODO 여기 제작 필요

) {
    public static BinaryContentCreatedEvent create(){
        return new BinaryContentCreatedEvent();
    }
}
