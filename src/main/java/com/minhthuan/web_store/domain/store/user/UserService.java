package com.minhthuan.web_store.domain.store.user;

import com.minhthuan.web_store.common.excpetion.CustomException;
import com.minhthuan.web_store.common.excpetion.ErrorCode;
import com.minhthuan.web_store.common.response.ApiResponse;
import com.minhthuan.web_store.domain.store.cart.CartRepository;
import com.minhthuan.web_store.domain.store.cart.entity.Cart;
import com.minhthuan.web_store.domain.store.user.dto.UserRequest;
import com.minhthuan.web_store.domain.store.user.dto.UserResponse;
import com.minhthuan.web_store.domain.store.user.entity.Role;
import com.minhthuan.web_store.domain.store.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;

    public ApiResponse<UserResponse> createUser(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_EXISTED);
        }
        User user = userMapper.toUser(userRequest);
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserResponse userResponse = userMapper.toUserResponse(userRepository.save(user));

        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);

        return new ApiResponse<>(userResponse, "User created successfully");
    }

    public ApiResponse<UserResponse> updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user, userRequest);
        userRepository.save(user);
        UserResponse userResponse = userMapper.toUserResponse(user);
        return new ApiResponse<>(userResponse, "User updated successfully");
    }

    public ApiResponse<UserResponse> deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
        UserResponse userResponse = userMapper.toUserResponse(user);
        return new ApiResponse<>(userResponse, "User deleted successfully");
    }

    public ApiResponse<List<UserResponse>> findAllUsers() {
        List<User> listUser = userRepository.findAll();
        List<UserResponse> listUserResponse = userMapper.toUserResponseList(listUser);
        return new ApiResponse<>(listUserResponse, "All users retrieved successfully");
    }

}
