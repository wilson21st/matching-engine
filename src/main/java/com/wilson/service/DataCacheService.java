package com.wilson.service;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.wilson.config.AppConfig;
import com.wilson.model.Job;
import com.wilson.model.Worker;

@Service
@EnableScheduling
public class DataCacheService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataCacheService.class);

	private final Map<Long, Worker> workers = new ConcurrentHashMap<>();
	private final Set<Job> jobs = ConcurrentHashMap.newKeySet();
	private final RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private AppConfig appConfig;

	public Worker getWorker(long userId) {
		return workers.get(userId);
	}

	public Set<Job> getJobs() {
		return jobs;
	}

	@Scheduled(fixedDelayString = "${fixeddelay.in.milliseconds}")
	private void syncAll() {

		// Synchronous API requests to synchronize workers & jobs
		try {
			Worker[] newWorkers = restTemplate.getForObject(appConfig.getWorkersUrl(), Worker[].class);
			// Map them to a temporary map before applied to the shared concurrent map
			Map<Long, Worker> tempWorkers = Arrays.stream(newWorkers)
					.collect(Collectors.toMap(Worker::getUserId, Function.identity()));
			// Keep the cache data if no workers retrieved
			if (!tempWorkers.isEmpty()) {
				workers.clear();
				workers.putAll(tempWorkers);
			}

			Job[] newJobs = restTemplate.getForObject(appConfig.getJobsUrl(), Job[].class);
			// Keep the cache data if no jobs retrieved
			if (newJobs.length != 0) {
				jobs.clear();
				jobs.addAll(Arrays.asList(newJobs));
			}

		} catch (Exception e) {
			LOGGER.warn("Exception: ", e);
		}

		LOGGER.info("Sync completed: workers={} jobs={}", workers.size(), jobs.size());
	}
}
