package auto.framework.drivers;

import auto.framework.TestManager;

public enum DriverSetting {

	//Jenkins
	JOB_NAME("jenkins.jobName", "JOB_NAME"),
	BUILD_NUMBER("jenkins.buildNumber", "BUILD_NUMBER"),	
	//Firefox
	FIREFOX_BINARY("webdriver.firefox.binary", "firefox_binary"),
	
	//CrossBrowserTesting
	CBTESTING_USERNAME("cbtesting.username", "CBTESTING_USERNAME", "CBTESTING_USER_NAME"),
	CBTESTING_ACCESS_KEY("cbtesting.accessKey", "CBTESTING_ACCESS_KEY", "CBTESTING_API_KEY"),
	
	//Sauce
	SAUCE_USERNAME("saucelabs.username", "SAUCE_USERNAME", "SAUCE_USER_NAME"),
	SAUCE_ACCESS_KEY("saucelabs.accessKey", "SAUCE_ACCESS_KEY", "SAUCE_API_KEY"),
	
	SAUCE_TIMEOUTS_MAX("saucelabs.timeouts.maxDuration"),
	SAUCE_TIMEOUTS_IDLE("saucelabs.timeouts.idle"),
	SAUCE_TIMEOUTS_COMMAND("saucelabs.timeouts.command"),
	SAUCE_CONNECT_FORCE_START("saucelabs.connect.forcestart"),
	
	SELENIUM_HOST("saucelabs.host", "SELENIUM_HOST"),
	SELENIUM_PORT("saucelabs.port", "SELENIUM_PORT"),
	
	//Browser Settings
	PLATFORM("platform", "SELENIUM_PLATFORM"),
	SCREEN_RESOLUTION("screenResolution", "SELENIUM_RESOLUTION"),
	@Deprecated BROWSER_OVERRIDE(""),
	APPLITOOLS_EYES_API_KEY("applitools.eyes.apiKey", "APPLITOOLS_EYES_API_KEY"),
	BROWSER("browser", "SELENIUM_BROWSER"),
	VERSION("browserVersion", "SELENIUM_VERSION"),
	
	//Suite Details
	SUITE_TAGS("suite.tags"),
	
	//Misc
	EXECUTOR("webdriver.executor","SELENIUM_EXECUTOR"),
	
	;
	
	private final String[] keys;
	
	private DriverSetting(String ...keys){
		this.keys = keys;
	}
	
	public String getValue(){
		for(String key: keys){
			String value = TestManager.Preferences.getPreference(key);
			if(value!=null) return value;
		}
		return null;
	}

}
