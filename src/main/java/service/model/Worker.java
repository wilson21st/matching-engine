package service.model;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.api.client.util.Key;

import lombok.Getter;
import lombok.Setter;
import service.util.DoubleToStringSerializer;

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
