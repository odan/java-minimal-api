package com.odan.config;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.odan.health.handler.HealthHandler;
import com.odan.home.handler.HomeHandler;
import com.odan.user.handler.GetUsersHandler;
import com.odan.user.mapper.UserMapper;
import com.odan.user.repository.UserRepository;
import com.odan.user.service.UserService;

public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Configuration.class).in(Singleton.class);

        bind(HomeHandler.class).in(Singleton.class);
        bind(HealthHandler.class).in(Singleton.class);
        bind(GetUsersHandler.class).in(Singleton.class);

        bind(UserService.class).in(Singleton.class);
        bind(UserMapper.class).in(Singleton.class);
        bind(UserRepository.class).in(Singleton.class);
    }
}