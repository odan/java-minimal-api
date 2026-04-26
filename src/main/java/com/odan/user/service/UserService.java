package com.odan.user.service;

import com.google.inject.Inject;
import com.odan.user.dto.UserResponse;
import com.odan.user.mapper.UserMapper;
import com.odan.user.repository.UserRepository;

import java.util.List;

public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Inject
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserResponse> getUsers() {

        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }
}