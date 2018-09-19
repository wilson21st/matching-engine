package com.wilson.model;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wilson.util.DoubleToStringSerializer;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Worker {

	@Setter
	@Getter
	public static class JobSearchAddress {
		@JsonSerialize(using = DoubleToStringSerializer.class)
		private double longitude;
		@JsonSerialize(using = DoubleToStringSerializer.class)
		private double latitude;
		private long maxJobDistance;
	}
	
	private long userId;
	private List<String> certificates;
	private boolean hasDriversLicense;
	private JobSearchAddress jobSearchAddress;
}
