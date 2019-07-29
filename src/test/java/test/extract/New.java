package test.extract;

import org.testng.annotations.Test;

import com.mca.beans.ProvideNextDin;
import auto.framework.TestBase;

@Test(invocationCount = 3)
public class New extends TestBase {
	
	int waitTime = 30;
//	String dIn = "00870832";
	String errorPath = "./target/errorLog.log";
	String resultsPath = "./target/result.log";
	
	public void extractDataFromMCA() throws InterruptedException {
		
		String dIn = ProvideNextDin.getInstance();
		System.out.println(dIn);		
		
	}
	
}
