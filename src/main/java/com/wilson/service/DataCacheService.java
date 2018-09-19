package com.wilson.service;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.wilson.model.Job;
import com.wilson.model.Worker;

@Service
@Configuration
@EnableScheduling
public class DataCacheService {

	private static final String API_GET_WORKERS = "http://test.swipejobs.com/api/workers";
	private static final String API_GET_JOBS = "http://test.swipejobs.com/api/jobs";

	private static final Logger LOGGER = LoggerFactory.getLogger(DataCacheService.class);

	private final Map<Long, Worker> workers = new ConcurrentHashMap<>();
	private final Set<Job> jobs = ConcurrentHashMap.newKeySet();

	private final RestTemplate restTemplate = new RestTemplate();

	public Worker getWorker(long userId) {
		return workers.get(userId);
	}

	public Set<Job> getJobs() {
		return jobs;
	}

	@Bean
	public TaskScheduler taskScheduler() {
		return new ConcurrentTaskScheduler();
	}

	@Scheduled(fixedDelay = 60000)
	private void synchronize() {

		try {

			// synchronous API request to get all workers
			Worker[] newWorkers = restTemplate.getForObject(API_GET_WORKERS, Worker[].class);
			// map them to a temporary map
			Map<Long, Worker> tempWorkers = Arrays
					.stream(newWorkers)
					.parallel()
					.collect(Collectors.toMap(Worker::getUserId, Function.identity()));
			workers.clear();
			workers.putAll(tempWorkers);
			LOGGER.info("Workers fetched: size={}", workers.size());

			// synchronous API request to get all jobs
			Job[] newJobs = restTemplate.getForObject(API_GET_JOBS, Job[].class);
			jobs.clear();
			jobs.addAll(Arrays.asList(newJobs));
			LOGGER.info("Jobs fetched: size={}", jobs.size());

		} catch (Exception e) {

			LOGGER.warn("Exception: ", e);
		}
	}
}
