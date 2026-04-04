package com.petcommunity.service;

import com.petcommunity.entity.Post;
import com.petcommunity.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public Page<Post> getFeed(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByOrderByCreateTimeDesc(pageable);
        // 补充用户信息
        posts.forEach(post -> {
            userService.findById(post.getUserId()).ifPresent(user -> {
                post.setUsername(user.getNickname());
                post.setUserAvatar(user.getAvatar());
            });
        });
        return posts;
    }

    public Optional<Post> getById(Long id) {
        return postRepository.findById(id).map(post -> {
            userService.findById(post.getUserId()).ifPresent(user -> {
                post.setUsername(user.getNickname());
                post.setUserAvatar(user.getAvatar());
            });
            return post;
        });
    }

    public Post create(Long userId, String content, String images, String topic, String location) {
        Post post = Post.builder()
                .userId(userId)
                .content(content)
                .images(images)
                .topic(topic)
                .location(location)
                .likesCount(0)
                .commentsCount(0)
                .build();
        Post saved = postRepository.save(post);
        userService.incrementPosts(userId);
        return saved;
    }

    @Transactional
    public void like(Long postId) {
        postRepository.findById(postId).ifPresent(post -> {
            post.setLikesCount(post.getLikesCount() + 1);
            postRepository.save(post);
            // 更新用户获赞数
            userService.findById(post.getUserId()).ifPresent(user -> {
                user.setLikesCount(user.getLikesCount() + 1);
                userService.findById(user.getId()); // re-fetch to avoid detached entity
            });
        });
    }
}
