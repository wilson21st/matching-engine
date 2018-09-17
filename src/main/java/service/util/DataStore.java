package service.util;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.gson.reflect.TypeToken;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import service.model.Job;
import service.model.Worker;

@Slf4j
public class DataStore extends TimerTask {

	private static final DataStore INSTANCE = new DataStore();
	
	private static final String API_GET_WORKERS = "http://test.swipejobs.com/api/workers";
	private static final String API_GET_JOBS = "http://test.swipejobs.com/api/jobs";

	private final Type workersType = new TypeToken<List<Worker>>() {
	}.getType();
	private final Type jobsType = new TypeToken<Set<Job>>() {
	}.getType();
	
	private final Map<Long, Worker> workers = new ConcurrentHashMap<>();
	private final Set<Job> jobs = ConcurrentHashMap.newKeySet();

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
			List<Worker> newWorkers = (List<Worker>) HttpGetJson.sendAndReceive(API_GET_WORKERS, workersType);
			// map them to a temporary map
			Map<Long, Worker> tempWorkers = newWorkers.parallelStream()
					.collect(Collectors.toMap(Worker::getUserId, Function.identity()));
			workers.clear();
			workers.putAll(tempWorkers);
			log.info("Workers fetched: size={}", workers.size());

			// synchronous API requests to get all jobs
			Set<Job> newJobs = (Set<Job>) HttpGetJson.sendAndReceive(API_GET_JOBS, jobsType);
			jobs.clear();
			jobs.addAll(newJobs);
			log.info("Jobs fetched: size={}", jobs.size());

		} catch (Exception e) {

			log.warn("Exception: ", e);
		}
	}
}
