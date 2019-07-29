package auto.framework;

import org.testng.IRetryAnalyzer;
import org.testng.ITestContext;
import org.testng.ITestResult;

/**
 * TODO: We can further improve/expand this by detecting how fatal the 
 * failing-error was.  This can possibly save time by only retrying with  
 * well-known and intermittent issues.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    int retryAttempts = 0;

    public RetryAnalyzer() {
    }

    @Override
    public boolean retry(ITestResult result) {
        
    	System.out.println("result.getStatus():"+result.getStatus());
    	System.out.println("result.isSuccess():"+result.isSuccess());
        // If the test is successful then we can go ahead and skip the retry.
        if(result == null || result.isSuccess() || result.getStatus() == ITestResult.FAILURE) {
            return false;
        }
        
//        // to deal with soft assert
//        try{
//			ReportLog.assertAll();
//		} catch(AssertionError error){
//	    	System.out.println("AssertionError");
//	    	error.printStackTrace();
//			return false;
//		}
        
        ITestContext testContext = result.getTestContext();
        RetryConfiguration config = RetryConfiguration.getInstance();
        
        String testName = (result.getMethod() != null) ? 
                                result.getMethod().getMethodName() : 
                                result.getName();
        
        Integer number = 
                (testContext != null && testName != null) ?
                        config.lookupTestRetry(testName) : null;
        
        int maxAttempts = (number == null) ? 
                config.getMaxRetries() : number.intValue();
                
        if(++retryAttempts > maxAttempts) {
            return false;
        }
        
        config.incrementRetryStatus(testName);
        
        ReportLog.addInfo("Retrying " + result.getName() + " [" + retryAttempts + "/" + maxAttempts + "]");
        
        // This might not be needed - Anyway, we're going to let other threads
        // catch up before we kick off another iteration.
        TestManager.sleep(1500);
        
        return true;
    }

}
