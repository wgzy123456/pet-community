package com.petcommunity.service;

import com.petcommunity.entity.User;
import com.petcommunity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User register(String username, String password, String nickname) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("用户名已存在");
        }
        User user = User.builder()
                .username(username)
                .password(password) // 生产环境需要加密
                .nickname(nickname != null ? nickname : username)
                .avatar("https://api.dicebear.com/7.x/avataaars/svg?seed=" + username)
                .level(1)
                .build();
        return userRepository.save(user);
    }

    public User login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(u -> u.getPassword().equals(password))
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));
    }

    @Transactional
    public User updateProfile(Long userId, String nickname, String city, String phone, String email) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        if (nickname != null) user.setNickname(nickname);
        if (city != null) user.setCity(city);
        if (phone != null) user.setPhone(phone);
        if (email != null) user.setEmail(email);
        user.setUpdateTime(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Transactional
    public void incrementPosts(Long userId) {
        userRepository.findById(userId).ifPresent(u -> {
            u.setPostsCount(u.getPostsCount() + 1);
            userRepository.save(u);
        });
    }
}
