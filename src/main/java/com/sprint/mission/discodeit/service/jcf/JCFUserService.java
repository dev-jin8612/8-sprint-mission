package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class JCFUserService implements UserService {
    final List<User> users;

    public JCFUserService(List<User> users) {
        this.users = new ArrayList<>(users);
    }

    // 유저 추가
    @Override
    public void addUser(User user) {
        users.add(user);
    }

    // 유저 수정
    @Override
    public void updateUser(String user, String username) {
        User found = search(user);

        if (found != null) {
            System.out.println(found.getUserName() + "님을 " + username + "님으로 업데이트 되었습니다.");
            found.updateName(username);
        } else if (username != null) {
            System.out.println("입력이 잘못 되었습니다.");
        }

    }

    // 유저 삭제
    @Override
    public void deleteUser(String name) {
        User user = search(name);

        if (user != null) {
            System.out.println(user.getUserName() + "유저를 삭제했습니다.");
            users.remove(user);
        } else {
            System.out.println("없거나 삭제된 유저입니다.");
        }
    }

    // 유저 찾아서 객체 넘기기
    @Override
    public User search(String name) {
//        이거는 여러명 검색을 위한 수정도 필요할 듯
//        아무래도 단체용은 따로 하는게 좋을것 같다.
        User user = users.stream().filter(u -> {
            return u.getUserName().equals(name);
        }).collect(Collectors.toList()).get(0);


        return user;
    }

    // 유저 찾기
    @Override
    public void searchUser(String name) {
        User user = search(name);

        if (user != null) {
            System.out.println(user.getUserName() + "님이 존재합니다.");
        } else {
            System.out.println("없는 존재입니다.");
        }
    }

    // 유저'들' 찾기
    @Override
    public void searchUserS(List<String> names) {
        // 여기서 여러번 부르는 것보다는
        // serach에서 list로 보내주는게 좋을거 같기는 한데
        // 일단 이렇게만
        names.forEach(name -> {
            User user = search(name);
            System.out.println(user.getUserName() + "님이 존재합니다.");
        });
    }

    // 업데이트가 된적 있는 유저 탐색
    @Override
    public void searchUpdateUser() {
        AtomicBoolean notNull= new AtomicBoolean(false);

        users.forEach(u -> {
            if (u.getCreated() != u.getUpdated()) {
                System.out.println("업데이트된 유저: " + u.getUserName());
                notNull.set(true);
            }
        });

        if(notNull.get() ==false){
            System.out.println("업데이트된 유저가 없습니다.");
        }
    }
}