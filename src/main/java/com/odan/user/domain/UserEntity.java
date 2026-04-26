package com.odan.user.domain;

public record UserEntity(int id, String username, String email, String passwordHash, boolean internalAdminFlag) {
}
