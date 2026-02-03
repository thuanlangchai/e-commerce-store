package com.minhthuan.web_store.domain.store.user;


import com.minhthuan.web_store.domain.store.user.dto.UserRequest;
import com.minhthuan.web_store.domain.store.user.dto.UserResponse;
import com.minhthuan.web_store.domain.store.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest userRequest);
    void updateUser(@MappingTarget User user, UserRequest userRequest);
    UserResponse toUserResponse(User user);
    List<UserResponse> toUserResponseList(List<User> users);
}
