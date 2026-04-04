package com.petcommunity.controller;

import com.petcommunity.dto.ApiResponse;
import com.petcommunity.dto.LoginRequest;
import com.petcommunity.dto.PostRequest;
import com.petcommunity.dto.RegisterRequest;
import com.petcommunity.entity.Post;
import com.petcommunity.entity.User;
import com.petcommunity.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import com.petcommunity.entity.Comment;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final UserService userService;
    private final PetService petService;
    private final PostService postService;
    private final CommentService commentService;

    // ====== 用户相关 ======

    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody RegisterRequest req) {
        try {
            User user = userService.register(req.getUsername(), req.getPassword(), req.getNickname());
            return ApiResponse.ok("注册成功", user);
        } catch (Exception e) {
            return ApiResponse.error(400, e.getMessage());
        }
    }

    @PostMapping("/login")
    public ApiResponse<User> login(@RequestBody LoginRequest req, HttpSession session) {
        try {
            User user = userService.login(req.getUsername(), req.getPassword());
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            return ApiResponse.ok("登录成功", user);
        } catch (Exception e) {
            return ApiResponse.error(401, e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpSession session) {
        session.invalidate();
        return ApiResponse.ok("已退出登录", null);
    }

    @GetMapping("/user/me")
    public ApiResponse<User> getCurrentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ApiResponse.error(401, "请先登录");
        }
        return userService.findById(userId)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.error(404, "用户不存在"));
    }

    @GetMapping("/user/{id}")
    public ApiResponse<User> getUser(@PathVariable Long id) {
        return userService.findById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.error(404, "用户不存在"));
    }

    // ====== 动态/帖子 ======

    @GetMapping("/feed")
    public ApiResponse<Page<Post>> getFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(postService.getFeed(page, size));
    }

    @GetMapping("/post/{id}")
    public ApiResponse<Post> getPost(@PathVariable Long id) {
        return postService.getById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.error(404, "帖子不存在"));
    }

    @PostMapping("/post")
    public ApiResponse<Post> createPost(@RequestBody PostRequest req, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ApiResponse.error(401, "请先登录");
        }
        Post post = postService.create(userId, req.getContent(), req.getImages(),
                req.getTopic(), req.getLocation());
        return ApiResponse.ok("发布成功", post);
    }

    @PostMapping("/post/{id}/like")
    public ApiResponse<Void> likePost(@PathVariable Long id) {
        postService.like(id);
        return ApiResponse.ok("点赞成功", null);
    }

    // ====== 评论 ======

    @GetMapping("/post/{id}/comments")
    public ApiResponse<List<Comment>> getComments(@PathVariable Long id) {
        return ApiResponse.ok(commentService.getByPostId(id));
    }

    @PostMapping("/post/{id}/comment")
    public ApiResponse<Comment> createComment(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ApiResponse.error(401, "请先登录");
        }
        String content = (String) body.get("content");
        Comment comment = commentService.create(id, userId, content);
        return ApiResponse.ok("评论成功", comment);
    }
}
