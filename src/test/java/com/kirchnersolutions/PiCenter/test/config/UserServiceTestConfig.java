package com.kirchnersolutions.PiCenter.test.config;

import com.kirchnersolutions.PiCenter.servers.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserServiceTestConfig {

    @Bean
    UserService userService() throws Exception{
        return new UserService();
    }

}
