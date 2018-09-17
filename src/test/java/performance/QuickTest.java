package performance;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.LongAdder;

import com.google.gson.reflect.TypeToken;

import lombok.SneakyThrows;
import service.util.HttpGetJson;

public class QuickTest {

	private final LongAdder totalCount = new LongAdder();
	private static final int MAX_RETRY = 2;

	public static void main(String[] args) {

		List<Task> tasks = new ArrayList<>();
		Type type = new TypeToken<Object>() {
		}.getType();
		for (int i = 0; i < 500000; i++) {
			int id = new Random().nextInt(50);
			String url = String.format("http://localhost:8080/api/workers/%d/jobs?limit=50", id);
			tasks.add(new Task(url, type));
		}

		QuickTest test = new QuickTest();
		test.executeAll(tasks);
	}

	@SneakyThrows
	private void executeAll(List<Task> tasks) {

		tasks.parallelStream().forEach(task -> {
			do {
				Result result = new Result();
				try {
					Object response = HttpGetJson.sendAndReceive(task.getUrl(), task.getType());
					result.setResponse(response);
				} catch (Exception e) {
					result.setException(true);
					result.setResponse(e.getMessage());
				} finally {
					record(task, result);
					int retryNumber = task.getRetryNumber();
					task.setRetry(false);
					if (result.isException() && retryNumber < MAX_RETRY) {
						task.setRetry(true);
						task.setRetryNumber(retryNumber + 1);
					}
				}
			} while (task.isRetry());
		});

		System.out.println("Done.");
	}

	private void record(Task task, Result result) {
		synchronized (totalCount) {
			totalCount.increment();
			String retry = task.isRetry() ? String.format(" retry=%d", task.getRetryNumber()) : "";
			System.out.println(String.format("id=%-6d time=%d%s url=%s response=%s", totalCount.longValue(),
					result.getTimer().getTime(), retry, task.getUrl(), result.getResponse()));
		}
	}
}
