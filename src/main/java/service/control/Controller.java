package service.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import service.model.Job;
import service.model.Result;
import service.model.Worker;
import service.util.ComparatorBuilder;
import service.util.DataStore;
import service.util.Distance;

@Slf4j
@RestController
@RequestMapping("/api/workers/")
public class Controller {

	// define thread pool
	private final ForkJoinPool fastMatchPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * 2);

	@SneakyThrows
	@RequestMapping("/{userId}/jobs")
	public List<Result> getJobs(@PathVariable("userId") long userId,
			@RequestParam(value = "orderBy", defaultValue = "!job.billRate,distance,job.startDate") String[] orderBy,
			@RequestParam(value = "limit", defaultValue = "3") int limit) {

		Worker worker = DataStore.getInstance().getWorker(userId);
		Set<Job> jobs = DataStore.getInstance().getJobs();
		Set<Result> results = ConcurrentHashMap.newKeySet();

		fastMatchPool.submit(() -> {

			jobs.parallelStream().forEach(job -> {

				try {

					boolean checkCertificates = worker.getCertificates().containsAll(job.getRequiredCertificates());
					boolean checkDriverLicense = job.isDriverLicenseRequired() == false
							|| job.isDriverLicenseRequired() == worker.isHasDriversLicense();
					long distance = Distance.calculate(worker.getJobSearchAddress().getLatitude(),
							worker.getJobSearchAddress().getLongitude(), job.getLocation().getLatitude(),
							job.getLocation().getLongitude());
					boolean checkDistance = distance <= worker.getJobSearchAddress().getMaxJobDistance();

					if (checkCertificates && checkDriverLicense && checkDistance) {
						results.add(new Result(job, distance));
					}

				} catch (Exception e) {
					log.warn("Exception: ", e);
				}
			});
		}).get();

		// sort results
		Comparator<Result> comparator = ComparatorBuilder.build(orderBy);
		List<Result> sortedResults = new ArrayList<>(results);
		Collections.sort(sortedResults, comparator);

		// return by limit
		int cutoff = Math.min(sortedResults.size(), limit);
		sortedResults = sortedResults.subList(0, cutoff);
		return sortedResults;
	}
}
