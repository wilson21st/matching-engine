package com.wilson.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wilson.model.Result;
import com.wilson.service.CrossMatchService;

@RestController
@RequestMapping("/api/workers/")
public class Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

	@Autowired
	CrossMatchService crossMatchService;

	@RequestMapping("/{userId}/jobs")
	public List<Result> getJobs(@PathVariable("userId") long userId,
			@RequestParam(value = "orderBy", defaultValue = "!job.billRate,distance,job.startDate") String[] orderBy,
			@RequestParam(value = "limit", defaultValue = "3") int limit) {

		LOGGER.debug("getJobs: userId={} orderBy={} limit={}", userId, orderBy, limit);
		return crossMatchService.crossMatch(userId, orderBy, limit);
	}
}
