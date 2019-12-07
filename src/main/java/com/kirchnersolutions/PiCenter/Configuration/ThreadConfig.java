package com.kirchnersolutions.PiCenter.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.annotation.ApplicationScope;

/**

 * Created by gkatzioura on 4/26/17.

 */

@Configuration
public class ThreadConfig {

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {


        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(3);

        executor.setMaxPoolSize(20);

        executor.setWaitForTasksToCompleteOnShutdown(true);

        executor.setKeepAliveSeconds(10);

        executor.setThreadNamePrefix("picenter_task_executor_thread");

        executor.initialize();

        return executor;

    }

}