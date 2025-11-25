package com.sprint.mission.discodeit.service;

import java.util.List;

public interface Serach {
    /*
     채널의 경우 serach를 메세지,유저,방 등 여러 번 사용하니
     오버 로딩으로 여러번 선언해서 사용하기
    */

    /*
    serach를 현재 객체를 주고, 또 뭐를 찾을지 알려주는
    필드의 값(아마 객체list)도 줘서 2개의 매개 변수가 최소한 필요할 듯
    */
    Object search(Object selfObject, List<Object> serachListObject);
}
