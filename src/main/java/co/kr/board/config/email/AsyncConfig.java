package co.kr.board.config.email;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
    private static int TASK_CORE_POOL_SIZE = 2;//기본쓰레드사이즈
    private static int TASK_MAX_POOL_SIZE = 4;//최대쓰레드사이즈
    private static int TASK_QUEUE_CAPACITY = 0;//Max쓰레드가 동작하는 경우 대기하는 큐 사이즈

    @Bean(name="executor")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        setExecutor(executor);
        return executor;
    }
    public void setExecutor(ThreadPoolTaskExecutor executor) {
        executor.setCorePoolSize(TASK_CORE_POOL_SIZE);
        executor.setMaxPoolSize(TASK_MAX_POOL_SIZE);
        executor.setQueueCapacity(TASK_QUEUE_CAPACITY);
        executor.initialize();
    }
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}
