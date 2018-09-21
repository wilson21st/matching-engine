package com.wilson.config;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.Getter;

@Configuration
public class AppConfig {

	@Getter
	@Value("${jobs.url}")
	private String jobsUrl;

	@Getter
	@Value("${workers.url}")
	private String workersUrl;
	
	@Getter
	@Value("${jobs.replicate.number:1}")
	private long jobsReplicateNumber;

	@Bean
	public TaskScheduler taskScheduler() {
		return new ConcurrentTaskScheduler();
	}

	@Bean
	public Executor asyncExecutor(@Value("${async.core.pool.size:0}") int corePoolSize) {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize == 0 ? Runtime.getRuntime().availableProcessors() : corePoolSize);
		executor.initialize();
		return executor;
	}
}
