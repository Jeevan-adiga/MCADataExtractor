package auto.framework.drivers;

import org.testng.ITestResult;

public interface DriverReportListener {
	
	public void setTestStatus(ITestResult tr, int status);

}
