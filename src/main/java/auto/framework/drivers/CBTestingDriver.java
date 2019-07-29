package auto.framework.drivers;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;

import auto.framework.WebManager;

import com.saucelabs.common.SauceOnDemandAuthentication;

public class CBTestingDriver extends RemoteWebDriver implements DriverReportListener {

	protected static URL getCBTestingUrl(String USERNAME, String ACCESS_KEY, String HOST){
		try {
			System.err.println("Connecting to http://" + USERNAME + ":****@"+HOST+"/wd/hub");
			return new URL("http://" + USERNAME + ":" + ACCESS_KEY + "@"+HOST+"/wd/hub");
		} catch (MalformedURLException e) {
			return null;
		}
	}
	
	public CBTestingDriver(SauceOnDemandAuthentication auth,String host, Capabilities desiredCapabilities,
			Capabilities requiredCapabilities) {
		super(getCBTestingUrl(auth.getUsername(),auth.getAccessKey(),host), desiredCapabilities);
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
	}
	
	public static CBTestingDriver trial() {
		//set authentication
		String username = DriverSettings.getProperty( DriverSetting.CBTESTING_USERNAME );
		String accessKey = DriverSettings.getProperty( DriverSetting.CBTESTING_ACCESS_KEY );
		SauceOnDemandAuthentication auth = new SauceOnDemandAuthentication(username, accessKey);
		//set host
		String host = "hub.crossbrowsertesting.com:80";
		//set capabilities
		DesiredCapabilities capabilities = DriverSettings.getDesiredCapabilities();
		
		return new CBTestingDriver(auth, host, capabilities, null);
	}
	
	@Override
	public void setTestStatus(ITestResult tr, int status) {
		
	}
	
	protected static DesiredCapabilities getDesiredCapabilities(){
		DesiredCapabilities caps = new DesiredCapabilities();
		
		//different capability names :\
	    caps.setCapability("name", "Selenium Test Example");
	    caps.setCapability("build", "1.0");
	    caps.setCapability("browser_api_name", "FF41");
	    caps.setCapability("os_api_name", "Win7x64-C1");
	    caps.setCapability("screen_resolution", "1024x768");
	    caps.setCapability("record_video", "true");
	    caps.setCapability("record_network", "true");
	    caps.setCapability("record_snapshot", "true");
	    return caps;
	}
	

}
