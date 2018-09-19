package com.wilson.model;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wilson.util.DoubleToStringSerializer;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Job {

	@Setter
	@Getter
	public static class Location {
		@JsonSerialize(using = DoubleToStringSerializer.class)
		private double longitude;
		@JsonSerialize(using = DoubleToStringSerializer.class)
		private double latitude;
	}

	private List<String> requiredCertificates;
	private boolean driverLicenseRequired;
	private Location location;
	private String billRate;
	private int workersRequired;
	private String startDate;
	private String about;
	private String jobTitle;
	private String company;
	private String guid;
	private long jobId;
}
