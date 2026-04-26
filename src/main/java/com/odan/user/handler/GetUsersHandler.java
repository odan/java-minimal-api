package com.odan.user.handler;

import com.google.inject.Inject;
import com.odan.user.service.UserService;
import io.javalin.http.Context;

public class GetUsersHandler {

    private final UserService userService;

    @Inject
    public GetUsersHandler(UserService userService) {
        this.userService = userService;
    }

    public void handle(Context ctx) {
        ctx.json(userService.getUsers());
    }
}
