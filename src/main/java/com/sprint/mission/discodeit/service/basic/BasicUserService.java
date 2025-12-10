//package com.sprint.mission.discodeit.service.basic;
//
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.repository.UserRepository;
//
//import java.util.List;
//import java.util.UUID;
//
//public class BasicUserService implements UserRepository {
//    private UserRepository userService;
//
//    public BasicUserService(UserRepository userService) {
//        this.userService = userService;
//    }
//
//    @Override
//    public void addUser(User user) {
//        userService.addUser(user);
//    }
//
//    @Override
//    public void updateUser(UUID userId, String username) {
//        userService.updateUser(userId, username);
//    }
//
//    @Override
//    public void deleteUser(UUID userId) {
//        userService.deleteUser(userId);
//    }
//
//    @Override
//    public List<User> search(String name) {
//        return userService.search(name);
//    }
//
//    @Override
//    public void searchUser(String name) {
//        userService.searchUser(name);
//    }
//
//    @Override
//    public void searchUserS(List<String> names) {
//        userService.searchUserS(names);
//    }
//
//    @Override
//    public void searchUpdateUser() {
//        userService.searchUpdateUser();
//    }
//
//    @Override
//    public List<User> getUsers() {
//        return userService.getUsers();
//    }
//}
