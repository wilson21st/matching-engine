package com.wilson.model;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.api.client.util.Key;
import com.wilson.util.DoubleToStringSerializer;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Worker {

	@Setter
	@Getter
	public static class JobSearchAddress {
		@Key
		@JsonSerialize(using = DoubleToStringSerializer.class)
		private double longitude;
		@Key
		@JsonSerialize(using = DoubleToStringSerializer.class)
		private double latitude;
		@Key
		private long maxJobDistance;
	}
	
	@Key
	private long userId;
	@Key
	private List<String> certificates;
	@Key
	private boolean hasDriversLicense;
	@Key
	private JobSearchAddress jobSearchAddress;
}
