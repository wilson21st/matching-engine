package service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result {
	
	private Job job;
	private double distance;
	
	public Result(Job job, double distance) {
		this.job = job;
		this.distance = distance;
	}
}
