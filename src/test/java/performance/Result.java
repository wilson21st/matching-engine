package performance;

import lombok.Data;

@Data
public class Result {

	private Object response;
	private Timer timer = new Timer();
	private boolean isException = false;
}
