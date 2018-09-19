package com.wilson.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wilson.model.Job;
import com.wilson.model.Result;
import com.wilson.model.Worker;
import com.wilson.util.ComparatorFactory;
import com.wilson.util.Distance;

@Service
public class CrossMatchService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CrossMatchService.class);

	@Autowired
	private DataCacheService dataCacheService;

	// define thread pool
	private final ForkJoinPool fastMatchPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * 2);

	public List<Result> crossMatch(long userId, String[] orderBy, int limit) {

		Worker worker = dataCacheService.getWorker(userId);

		if (worker == null) {
			return new ArrayList<>();
		}

		Set<Job> jobs = dataCacheService.getJobs();
		Set<Result> results = ConcurrentHashMap.newKeySet();

		try {
			fastMatchPool.submit(() -> {

				jobs.parallelStream().forEach(job -> {

					try {

						long distance = Distance.calculate(worker.getJobSearchAddress().getLatitude(),
								worker.getJobSearchAddress().getLongitude(), job.getLocation().getLatitude(),
								job.getLocation().getLongitude());

						// a job will be selected if the following criteria are met:
						// certificates, driver license and distance
						boolean checkCertificates = worker.getCertificates().containsAll(job.getRequiredCertificates());
						boolean checkLicense = !job.isDriverLicenseRequired() || worker.isHasDriversLicense();
						boolean checkDistance = distance <= worker.getJobSearchAddress().getMaxJobDistance();

						if (checkCertificates && checkLicense && checkDistance) {
							results.add(new Result(job, distance));
						}

					} catch (Exception e) {

						LOGGER.warn("Exception: ", e);
					}
				});
			}).get();

		} catch (Exception e) {

			LOGGER.warn("Exception: ", e);
		}

		// sort results then apply limit
		return results
				.stream()
				.sorted(ComparatorFactory.build(orderBy))
				.limit(limit)
				.collect(Collectors.toList());
	}
}
