package service.util;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.google.gson.reflect.TypeToken;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import service.model.Job;
import service.model.Worker;

@Slf4j
public class DataStore extends TimerTask {

	private static final DataStore INSTANCE = new DataStore();
	private final Semaphore semaphore = new Semaphore(1);

	private final Map<Long, Worker> workers = new ConcurrentHashMap<>();
	private final Set<Job> jobs = ConcurrentHashMap.newKeySet();

	private final Type workersType = new TypeToken<List<Worker>>() {
	}.getType();
	private final Type jobsType = new TypeToken<List<Job>>() {
	}.getType();

	private boolean isStarted = false;

	public static DataStore getInstance() {
		return INSTANCE;
	}

	public Worker getWorker(long userId) {
		return workers.get(userId);
	}

	public Set<Job> getJobs() {
		return jobs;
	}

	public void start(long secs) {
		if (isStarted == false) {
			long milliseconds = TimeUnit.SECONDS.toMillis(secs);
			new Timer().schedule(this, 0, milliseconds);
			isStarted = true;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@SneakyThrows
	public void run() {

		try {
			// synchronous API requests to get all workers
			List<Worker> newWorkers = (List<Worker>) HttpGetJson.sendAndReceive("http://test.swipejobs.com/api/workers",
					workersType);

			// protected semaphore but this method is designed to be used by single thread
			if (semaphore.tryAcquire()) {
				workers.clear();
				semaphore.release();
			}

			// map into key=id,value=worker
			newWorkers.parallelStream().forEach(newWorker -> {
				workers.compute(newWorker.getUserId(), (k, v) -> {
					return newWorker;
				});
			});

			log.info("Workers fetched: size={}", workers.size());

			// synchronous API requests to get all jobs
			List<Job> newJobs = (List<Job>) HttpGetJson.sendAndReceive("http://test.swipejobs.com/api/jobs", jobsType);

			// protected semaphore but this method is designed to be used by single thread
			if (semaphore.tryAcquire()) {
				jobs.clear();
				semaphore.release();
			}

			// stored in set given by concurrent map
			jobs.addAll(newJobs);

			log.info("Jobs fetched: size={}", jobs.size());

		} catch (Exception e) {

			log.warn("Exception: ", e);
		}
	}
}
