package service.model;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.api.client.util.Key;

import lombok.Getter;
import lombok.Setter;
import service.util.DoubleToStringSerializer;

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
