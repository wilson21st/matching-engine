package com.wilson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import com.wilson.util.HttpGetJson;

import lombok.SneakyThrows;

public class QuickTest {

	private final AtomicLong totalCount = new AtomicLong();
	private static final int MAX_RETRY = 1;

	public static void main(String[] args) {

		List<Task> tasks = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			int id = new Random().nextInt(50);
			String url = String.format("http://localhost:8080/api/workers/%d/jobs", id);
			tasks.add(new Task(url));
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
					String json = new HttpGetJson().sendAndReceive(task.getUrl());
					result.setResponse(json);
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
		String retry = task.isRetry() ? String.format(" retry=%d", task.getRetryNumber()) : "";
		System.out.println(String.format("id=%-6d time=%d%s url=%s response=%s", totalCount.incrementAndGet(),
				result.getTimer().getTime(), retry, task.getUrl(), result.getResponse()));
	}
}
