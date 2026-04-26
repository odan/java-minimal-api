package com.odan.user.mapper;

import com.odan.user.domain.UserEntity;
import com.odan.user.dto.UserResponse;

public class UserMapper {

    public UserResponse toResponse(UserEntity user) {

        return new UserResponse(user.id(), user.username(), user.email());
    }
}