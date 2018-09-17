package performance;

import java.lang.reflect.Type;

import lombok.Data;

@Data
public class Task {

	private String url;
	private Type type;

	private boolean retry = false;
	private int retryNumber = 0;

	public Task(String url, Type type) {
		this.url = url;
		this.type = type;
	}
}
