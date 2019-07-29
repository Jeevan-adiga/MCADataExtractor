package auto.framework.drivers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.saucelabs.ci.sauceconnect.AbstractSauceTunnelManager.SauceConnectException;
import com.saucelabs.ci.sauceconnect.SauceConnectFourManager;
import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.saucerest.SauceREST;

import auto.framework.TestManager;
import auto.framework.WebManager;

public class SauceLabsDriver extends RemoteWebDriver implements DriverReportListener {
	
	protected final SauceREST sauceRest;
	protected final SessionId sessionID;
	
	protected static URL getSauceUrl(String USERNAME, String ACCESS_KEY, String HOST){
		try {
			System.err.println("Connecting to http://" + USERNAME + ":****@"+HOST+"/wd/hub");
			return new URL("http://" + USERNAME + ":" + ACCESS_KEY + "@"+HOST+"/wd/hub");
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public SauceLabsDriver(SauceOnDemandAuthentication auth,String host, Capabilities desiredCapabilities,
			Capabilities requiredCapabilities) {
		super(getSauceUrl(auth.getUsername(),auth.getAccessKey(),host), desiredCapabilities);
		Proxy proxy = WebManager.getProxySettings();
		if(proxy.getProxyType().equals(ProxyType.MANUAL)){
			String[] proxyParts = proxy.getHttpProxy().split(":");
			String proxyHost = proxyParts[0];
			String proxyPort = proxyParts[1];
			System.setProperty("http.proxyHost", proxyHost);
	        System.setProperty("https.proxyHost", proxyHost);
	        System.setProperty("http.proxyPort", proxyPort);
	        System.setProperty("https.proxyPort", proxyPort);
	        System.out.println("Using proxy "+proxyHost+":"+proxyPort);
		}
		sauceRest = new SauceREST(auth.getUsername(),auth.getAccessKey());
		sessionID = getSessionId();//sauceRest.getPublicJobLink(getSessionId().toString());
		setFileDetector(new LocalFileDetector());
	}
	
	protected static DesiredCapabilities getFirefox(){
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
    	Proxy proxy = WebManager.getProxySettings();
    	String noProxy = proxy.getNoProxy();
    	proxy.setNoProxy(noProxy!=null ? noProxy.replace(";", ",") : noProxy);
    	capabilities.setCapability(CapabilityType.PROXY, proxy);
    	return capabilities;
	}
	
	public static SauceOnDemandAuthentication getAuth(){
		String username = DriverSettings.getProperty( DriverSetting.SAUCE_USERNAME );
		String accessKey = DriverSettings.getProperty( DriverSetting.SAUCE_ACCESS_KEY );
		SauceOnDemandAuthentication auth = new SauceOnDemandAuthentication(username, accessKey);
		return auth;
	}
	
	public static Process sauceConnect(){
		SauceConnectFourManager sauceMngr = new SauceConnectFourManager();
		SauceOnDemandAuthentication auth = SauceLabsDriver.getAuth();
		String args = "";
		Proxy proxy = WebManager.getProxySettings();
		if(proxy.getProxyType().equals(ProxyType.MANUAL)){
			String proxyHttp = proxy.getHttpProxy();
			args = "-p, --proxy \""+proxyHttp+"\" --proxy-tunnel";
			//TODO
			args += " --no-ssl-bump-domains *.att.com";
		}
		try {
			return sauceMngr.openConnection(auth.getUsername(), auth.getAccessKey(), 4445, null, args, System.out, false, null);
		} catch (SauceConnectException e) {
			return null;
		}
	}
	
	public static SauceLabsDriver trial() {
		//set authentication
//		String username = DriverSettings.getProperty( DriverSetting.SAUCE_USERNAME );
//		String accessKey = DriverSettings.getProperty( DriverSetting.SAUCE_ACCESS_KEY );
//		SauceOnDemandAuthentication auth = new SauceOnDemandAuthentication(username, accessKey);
		SauceOnDemandAuthentication auth = getAuth();
		//set host
		String configHost = DriverSettings.getProperty( DriverSetting.SELENIUM_HOST );
		String configPort = DriverSettings.getProperty( DriverSetting.SELENIUM_PORT );
		String host;
		if(configHost==null||configPort==null){
			host = "ondemand.saucelabs.com:80";
		} else {
			if(configHost.equalsIgnoreCase("ondemand.saucelabs.com")){
				host = configHost+":80";
			} else {
				host = "localhost:"+configPort;
			}
		}
		//set capabilities
		DesiredCapabilities capabilities = DriverSettings.getDesiredCapabilities();
		//capabilities.setCapability("seleniumVersion", "2.45.0");
		
		capabilities.setCapability("maxDuration", Integer.valueOf( DriverSettings.getProperty( DriverSetting.SAUCE_TIMEOUTS_MAX, "10800" ) ) );
		capabilities.setCapability("commandTimeout", Integer.valueOf( DriverSettings.getProperty( DriverSetting.SAUCE_TIMEOUTS_COMMAND, "600" ) ) );
		capabilities.setCapability("idleTimeout", Integer.valueOf( DriverSettings.getProperty( DriverSetting.SAUCE_TIMEOUTS_IDLE, "90" ) ) );
		//capabilities.setCapability("screen-resolution","1024x768"); //TODO
		
		ITestResult tr = Reporter.getCurrentTestResult();
		Map<String, Object> updates = new HashMap<String, Object>();
		pushUpdates(updates);
		capabilities.setCapability("name", tr.getTestContext().getName());
		for(String key: updates.keySet()){
			Object value = updates.get(key);
			if(value instanceof JSONObject || value instanceof JSONArray){
				continue;
			}
			capabilities.setCapability(key, value);
		}
		//TODO Handle Multiple Browsers here
		SauceLabsDriver driver = new SauceLabsDriver(auth, host, capabilities, null);
		return driver;
	}

	@Override
	public void setTestStatus(ITestResult tr, int status) {
		Map<String, Object> updates = new HashMap<String, Object>();
		switch(status){
			case ITestResult.SUCCESS:
			case ITestResult.FAILURE:
				updates.put("passed", status!=ITestResult.FAILURE);
			case ITestResult.STARTED:
				updates.put("name", tr.getTestContext().getName());
				pushUpdates(updates);
		        sauceRest.updateJobInfo(sessionID.toString(), updates);
		        //System.err.println("["+updates.get("name")+"] update: "+getSessionId()+" | "+status);
		        break;
			default:
		        //System.err.println("["+updates.get("name")+"] update: "+getSessionId()+" | "+status);
		        break;
		}
	}
	
	public static Map<String, Object> pushUpdates(Map<String, Object> updates){
		String jobName = DriverSettings.getProperty( DriverSetting.JOB_NAME );
		String buildNumber = DriverSettings.getProperty( DriverSetting.BUILD_NUMBER );
		String suiteTags = DriverSettings.getProperty( DriverSetting.SUITE_TAGS );
		if(jobName!=null && buildNumber!=null){
			//System.err.println("Build: " +jobName+" #"+buildNumber);
			updates.put("build", jobName+" #"+buildNumber);
		}
		if(suiteTags!=null){
			try {
				JSONArray tags  = new JSONArray(suiteTags.split(","));
				updates.put("tags", tags);
			} catch (JSONException e) {
			}
		}
		JSONObject customData = new JSONObject();
		try {
			String app = TestManager.Preferences.getPreference("test.details.app");
			if(app!=null){
				customData.append("Application", app);
				updates.put("customData", customData);
			}
		} catch (JSONException e) {
		}
		return updates;
	}

}
