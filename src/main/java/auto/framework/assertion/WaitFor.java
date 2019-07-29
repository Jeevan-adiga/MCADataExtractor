package auto.framework.assertion;

import java.util.concurrent.TimeUnit;

public class WaitFor {

	public static class Timeout {
		
		private final long value;
		private final TimeUnit unit;
		
		public Timeout(long value, TimeUnit unit){
			this.value = value;
			this.unit = unit;
		}
		
		public long getValue(){
			return value;
		}
		
		public TimeUnit getUnit(){
			return unit;
		}
		
	}
	
	public static Timeout microseconds(long timeoutInMicroseconds){
		return new Timeout(timeoutInMicroseconds, TimeUnit.MICROSECONDS);
	}
	
	public static Timeout milliseconds(long timeoutInMilliseconds){
		return new Timeout(timeoutInMilliseconds, TimeUnit.MILLISECONDS);
	}
	
	public static Timeout seconds(long timeoutInSeconds){
		return new Timeout(timeoutInSeconds, TimeUnit.SECONDS);
	}
	
	public static Timeout minutes(long timeoutInMinutes){
		return new Timeout(timeoutInMinutes, TimeUnit.MINUTES);
	}
	
}
