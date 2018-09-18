package com.wilson;

public class Timer {
	private long startTime = System.currentTimeMillis();
	
	public long getTime() {
		return System.currentTimeMillis() - startTime;
	}
}
