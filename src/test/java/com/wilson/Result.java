package com.wilson;

import lombok.Data;

@Data
public class Result {

	private String response;
	private Timer timer = new Timer();
	private boolean isException = false;
}
