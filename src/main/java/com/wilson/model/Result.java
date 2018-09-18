package com.wilson.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result {
	
	private Job job;
	private long distance;
	
	public Result(Job job, long distance) {
		this.job = job;
		this.distance = distance;
	}
}
