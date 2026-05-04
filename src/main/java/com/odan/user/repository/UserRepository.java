package com.odan.user.repository;

import com.odan.user.domain.UserEntity;
import java.util.List;

public class UserRepository {

    public List<UserEntity> findAll() {

        return List.of(new UserEntity(1, "alice", "alice@example.com", "secret-1", true),
                new UserEntity(2, "bob", "bob@example.com", "secret-2", false));
    }
}
