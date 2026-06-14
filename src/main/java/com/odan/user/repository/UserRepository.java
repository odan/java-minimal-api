package com.odan.user.repository;

import com.odan.user.domain.UserEntity;
import java.util.List;

public class UserRepository
{

    public List<UserEntity> findAll()
    {

        return List.of(
                createUser(1, "alice", "alice@example.com", "secret-1", true),
                createUser(2, "bob", "bob@example.com", "secret-2", false));
    }

    private UserEntity createUser(int id, String username, String email, String passwordHash, boolean internalAdminFlag)
    {
        var user = new UserEntity();
        user.id = id;
        user.username = username;
        user.email = email;
        user.passwordHash = passwordHash;
        user.internalAdminFlag = internalAdminFlag;

        return user;
    }
}
