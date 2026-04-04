package com.petcommunity.controller;

import com.petcommunity.dto.ApiResponse;
import com.petcommunity.entity.Pet;
import com.petcommunity.entity.User;
import com.petcommunity.service.PetService;
import com.petcommunity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;
    private final UserService userService;

    @GetMapping("/user/{userId}/pets")
    public ApiResponse<List<Pet>> getUserPets(@PathVariable Long userId) {
        return ApiResponse.ok(petService.getByUserId(userId));
    }

    @PostMapping("/pet")
    public ApiResponse<Pet> addPet(@RequestBody Pet pet, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ApiResponse.error(401, "请先登录");
        }
        pet.setUserId(userId);
        return ApiResponse.ok("添加成功", petService.save(pet));
    }

    @PutMapping("/pet/{id}")
    public ApiResponse<Pet> updatePet(@PathVariable Long id, @RequestBody Pet petData, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ApiResponse.error(401, "请先登录");
        }
        return petService.getById(id)
                .filter(p -> p.getUserId().equals(userId))
                .map(p -> {
                    if (petData.getName() != null) p.setName(petData.getName());
                    if (petData.getType() != null) p.setType(petData.getType());
                    if (petData.getBreed() != null) p.setBreed(petData.getBreed());
                    if (petData.getGender() != null) p.setGender(petData.getGender());
                    if (petData.getBirthDate() != null) p.setBirthDate(petData.getBirthDate());
                    if (petData.getAvatar() != null) p.setAvatar(petData.getAvatar());
                    if (petData.getBio() != null) p.setBio(petData.getBio());
                    return ApiResponse.ok("更新成功", petService.save(p));
                })
                .orElse(ApiResponse.error(403, "无权限或宠物不存在"));
    }

    @DeleteMapping("/pet/{id}")
    public ApiResponse<Void> deletePet(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ApiResponse.error(401, "请先登录");
        }
        petService.getById(id)
                .filter(p -> p.getUserId().equals(userId))
                .ifPresentOrElse(
                        p -> petService.delete(id),
                        () -> { throw new RuntimeException("无权限或宠物不存在"); }
                );
        return ApiResponse.ok("删除成功", null);
    }
}
