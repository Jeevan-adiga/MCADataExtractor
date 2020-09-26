package auto.framework.drivers;

import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DriverSettings {

	// TODO
	public static class BrowserInfo {
		
		public String browserType;
		public String browserVersion;
		public Boolean debugMode;
		
		public BrowserInfo(String browserInfoString){
			String[] browserInfo = browserInfoString.split("/");
			browserType = normalizeBrowserName(browserInfo[0].trim());
			if(browserInfo.length>1){
				browserVersion = browserInfo[1].trim().replaceFirst(" *\\.$", "");
			}
			debugMode = browserInfoString.contains("-debug");
		}
		
		private static String normalizeBrowserName(String browserName){
			if(browserName==null) return null;
			switch(browserName.trim().toLowerCase()){
			case "chrome":
				browserName = BrowserType.CHROME;
				break;
			case "internet explorer":
			case "ie":
				browserName = BrowserType.IEXPLORE;
				break;
			case "safari":
				browserName = BrowserType.SAFARI;
				break;
			case "firefox":
				browserName = BrowserType.FIREFOX;
				break;
			case "firefox -debug":
				browserName = BrowserType.FIREFOX;
				break;
			}
			return browserName;
		}
		
	}
	
	public static String getProperty(DriverSetting setting){
		return setting.getValue();
	}
	
	public static String getProperty(DriverSetting setting, String defValue){
		String value = setting.getValue();
		return value!=null? value : defValue;
	}

	public static DesiredCapabilities getDesiredCapabilities(){
		
		DesiredCapabilities capabilities = new DesiredCapabilities();
		
		BrowserInfo browserInfo = new BrowserInfo( useNotNull( getProperty( DriverSetting.BROWSER ), BrowserType.FIREFOX) );
		
		capabilities.setBrowserName(browserInfo.browserType);
		if(browserInfo.browserVersion!=null){
			capabilities.setVersion( browserInfo.browserVersion );
		}
		//capabilities.setBrowserName( normalizeBrowserName( useNotNull( getProperty( DriverSetting.BROWSER ), BrowserType.FIREFOX) ) );
		
		String property;
		//TODO:
//		if((property=getProperty(DriverSetting.VERSION))!=null && getProperty(DriverSetting.BROWSER_OVERRIDE)==null){
//			capabilities.setVersion( property );
//		}
		if((property=getProperty(DriverSetting.PLATFORM))!=null && getProperty(DriverSetting.BROWSER_OVERRIDE)==null){
			capabilities.setCapability( CapabilityType.PLATFORM, property );
			//Platform.WIN8
		}
		if((property=getProperty(DriverSetting.SCREEN_RESOLUTION))!=null){
			capabilities.setCapability( "screenResolution", property );
		}
		
		return capabilities;
	}
	
	protected static String useNotNull(String primaryText, String altText){
		if(primaryText!=null) return primaryText;
		return altText;
	}

}
