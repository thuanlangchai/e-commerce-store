package com.minhthuan.web_store.domain.store.user;


import com.minhthuan.web_store.common.response.ApiResponse;
import com.minhthuan.web_store.domain.store.user.dto.UserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user/users")
    public ResponseEntity<ApiResponse<?>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> createUser(@RequestBody @Valid UserRequest userRequest) {
        return ResponseEntity.ok(userService.createUser(userRequest));
    }

    @PutMapping("/admin/update-user/{userId}")
    public ResponseEntity<ApiResponse<?>> updateUser(@PathVariable long userId, @RequestBody @Valid UserRequest userRequest) {
        return ResponseEntity.ok(userService.updateUser(userId, userRequest));
    }

    @DeleteMapping("/admin/delete-user/{userId}")
    public ResponseEntity<ApiResponse<?>> deleteUser(@PathVariable long userId) {
        return ResponseEntity.ok(userService.deleteUser(userId));
    }

}
