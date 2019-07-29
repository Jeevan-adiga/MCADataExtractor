package auto.framework;

import java.util.HashMap;
import java.util.Map;

/**
 * A very basic singleton used to pass configuration parameters from anywhere 
 * within the (selenium web-driver) process.
 */
public class RetryConfiguration {

    private static RetryConfiguration instance = null;
    private int maxRetries = Integer.parseInt(TestManager.Preferences.getPreference("test.maxRetry", "0"));
    private Map<String, Integer> retryLookupMap = new HashMap<String, Integer>();
    private Map<String, Integer> retryStatusMap = new HashMap<String, Integer>();

    private RetryConfiguration() {
    }

    public static synchronized RetryConfiguration getInstance() {
        if(instance == null) {
            instance = new RetryConfiguration();
        }
        return instance;
    }

    /**
     * Retrieves what can be considered "global" defaults.  This can be 
     * over-ridden with setMaxRetries or by stuffing a custom value into the 
     * retryLookupMap container.
     * 
     * @return
     */
    public synchronized int getMaxRetries() {
        return maxRetries;
    }

    /**
     * Sets the total number of retries - essentially these are default values.
     * 
     * @param maxRetries
     */
    public synchronized void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
        if(maxRetries < 0) this.maxRetries = 0;
    }
    
    /**
     * We anticipate some rare situations where there is a specific number of 
     * retries for just one test.  Be sure to index by testname.
     * 
     * @warning This is not thread-safe, lock it before you use just to be sure.
     * @return
     */
    public Map<String, Integer> getRetryLookupMap() {
        return retryLookupMap;
    }
    
    /**
     * A thread-safe way to look-up the retry count.
     * 
     * @param testName
     * @return null if no entry exists for the given test name
     */
    public synchronized Integer lookupTestRetry(String testName) {
        if(retryLookupMap.containsKey(testName)) {
            return retryLookupMap.get(testName).intValue();
        }
        return null;
    }
    
    /**
     * A thread-safe way to set a test's retry count.
     * 
     * @param testName
     * @param retry
     */
    public synchronized void setTestRetry(String testName, int retry) {
        retryLookupMap.put(testName, retry);
    }
    
    public synchronized void incrementRetryStatus(String testName) {
        int value = 1;
        if(retryStatusMap.containsKey(testName)) {
            value = retryStatusMap.get(testName).intValue() + 1;
        }
        retryStatusMap.put(testName, value);
    }
    
    public synchronized boolean isTestRetrying(String testName) {
        if(retryStatusMap.containsKey(testName)) {
            return retryStatusMap.get(testName).intValue() > 0;
        }
        return false;
    }
}
