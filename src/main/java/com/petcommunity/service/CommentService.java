package com.petcommunity.service;

import com.petcommunity.entity.Comment;
import com.petcommunity.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;

    public List<Comment> getByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdOrderByCreateTimeAsc(postId);
        comments.forEach(c -> {
            userService.findById(c.getUserId()).ifPresent(user -> {
                c.setUsername(user.getNickname());
                c.setUserAvatar(user.getAvatar());
            });
        });
        return comments;
    }

    @Transactional
    public Comment create(Long postId, Long userId, String content) {
        Comment comment = Comment.builder()
                .postId(postId)
                .userId(userId)
                .content(content)
                .build();
        Comment saved = commentRepository.save(comment);
        // 更新评论数
        postService.getById(postId).ifPresent(post -> {
            post.setCommentsCount(post.getCommentsCount() + 1);
            postService.getById(postId);
        });
        return saved;
    }
}
