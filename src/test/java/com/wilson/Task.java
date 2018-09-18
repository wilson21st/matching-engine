package com.wilson;

import lombok.Data;

@Data
public class Task {

	private String url;

	private boolean retry = false;
	private int retryNumber = 0;

	public Task(String url) {
		this.url = url;
	}
}
