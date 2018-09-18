package com.wilson.model;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.api.client.util.Key;
import com.wilson.util.DoubleToStringSerializer;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Job {

	@Setter
	@Getter
	public static class Location {
		@Key
		@JsonSerialize(using = DoubleToStringSerializer.class)
		private double longitude;
		@Key
		@JsonSerialize(using = DoubleToStringSerializer.class)
		private double latitude;
	}

	@Key
	private List<String> requiredCertificates;
	@Key
	private boolean driverLicenseRequired;
	@Key
	private Location location;
	@Key
	private String billRate;
	@Key
	private int workersRequired;
	@Key
	private String startDate;
	@Key
	private String about;
	@Key
	private String jobTitle;
	@Key
	private String company;
	@Key
	private String guid;
	@Key
	private long jobId;
}
