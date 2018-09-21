package com.wilson.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wilson.model.Job;
import com.wilson.model.Worker;
import com.wilson.util.Distance;

import lombok.SneakyThrows;

@Service
public class CrossMatchService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CrossMatchService.class);

	@Autowired
	private DataCacheService dataCacheService;

	@SneakyThrows
	public List<Job> crossMatch(long userId, int limit) {

		Worker worker = dataCacheService.getWorker(userId);

		if (worker != null) {

			Set<Job> jobs = dataCacheService.getJobs();

			try {

				return jobs
						.stream()
						.filter(job -> {

							boolean checkCertificates = worker.getCertificates().containsAll(job.getRequiredCertificates());
							boolean checkLicense = !job.isDriverLicenseRequired() || worker.isHasDriversLicense();

							// a job will be selected if the following conditions are met:
							// certificates, driver license and distance

							if (checkCertificates && checkLicense) {
		
								long distance = Distance.calculate(worker.getJobSearchAddress().getLatitude(),
										worker.getJobSearchAddress().getLongitude(), job.getLocation().getLatitude(),
										job.getLocation().getLongitude());

								if (distance <= worker.getJobSearchAddress().getMaxJobDistance()) {
									return true;
								}
							}

							// not selected
							return false; })

						.limit(limit)
						.collect(Collectors.toList());

			} catch (Exception e) {

				LOGGER.warn("Exception: ", e);
			}
		}

		return new ArrayList<>();
	}
}
