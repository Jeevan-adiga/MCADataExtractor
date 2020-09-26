package auto.framework.drivers;

import org.testng.ITestResult;

public interface DriverReportListener {
	
	void setTestStatus(ITestResult tr, int status);

}
