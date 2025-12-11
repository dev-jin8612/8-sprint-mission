//package com.sprint.mission.discodeit.service.basic;
//
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.service.UserService;
//
//import java.util.List;
//import java.util.UUID;
//
//public class BasicUserService implements UserService {
//    private UserRepository userService;
//
//    public BasicUserService(UserRepository userService) {
//        this.userService = userService;
//    }
//
//    @Override
//    public User create(User user) {
//        return userService.create(user);
//    }
//
//    @Override
//    public User update(UUID userid, String username) {
//        return userService.update(userid, username);
//    }
//
//    @Override
//    public void delete(UUID id) {
//        userService.delete(id);
//    }
//
//    @Override
//    public List<User> searchByName(List<String> name) {
//        return userService.searchByName(name);
//    }
//
//    @Override
//    public User findById(UUID id) {
//        return userService.findById(id);
//    }
//
//    @Override
//    public List<User> getUsers() {
//        return userService.getUsers();
//    }
//}
