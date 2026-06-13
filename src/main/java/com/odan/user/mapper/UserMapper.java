package com.odan.user.mapper;

import com.odan.user.domain.UserEntity;
import com.odan.user.dto.UserResponse;

public class UserMapper
{

    public UserResponse toResponse(UserEntity user)
    {
        var response = new UserResponse();
        response.id = user.id;
        response.username = user.username;
        response.email = user.email;

        return response;
    }
}
