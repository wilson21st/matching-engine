package com.wilson.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wilson.model.Job;
import com.wilson.service.CrossMatchService;

@RestController
@RequestMapping("/api/workers/")
public class Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

	@Autowired
	Executor asyncExecutor;

	@Autowired
	CrossMatchService crossMatchService;

	@RequestMapping("/{userId}/jobs")
	public CompletableFuture<List<Job>> getJobs(@PathVariable("userId") long userId,
			@RequestParam(value = "limit", defaultValue = "3") int limit) {

		LOGGER.debug("getJobs: userId={} limit={}", userId, limit);

		return CompletableFuture.supplyAsync(() -> crossMatchService.crossMatch(userId, limit), asyncExecutor);
	}
}
