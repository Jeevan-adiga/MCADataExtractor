package auto.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.safari.SafariDriver;

import auto.framework.drivers.DriverSettings;

public class WebManager {

    private static ThreadLocal<WebDriver> webDriver = new InheritableThreadLocal<WebDriver>();
    private static Boolean debugMode = false;
    public static String current_Dir=System.getProperty("user.dir");
	
    public static WebDriver getDriver() {
        return webDriver.get();
    }
    
    public static RemoteWebDriver getRemoteDriver() {
    	WebDriver driver = WebManager.getDriver();
    	return (RemoteWebDriver) driver;
    }
 
        
    public static void endDriver() throws InterruptedException{
    	if(!debugMode){
	    	WebDriver driver = WebManager.getDriver();
	    	if(driver!=null)
				driver.quit();    		    	
    	}
    }
    
    public static Capabilities getCapabilities(){
		return getRemoteDriver().getCapabilities();
    }
    
    public static SessionId getSessionId(){
		return getRemoteDriver().getSessionId();
    }
    
    private static String getBrowserName(WebDriver driver){
    	Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
		String browserName = caps.getBrowserName();
    	return browserName;
    }
    
    public static String getBrowserName() {
    	return getBrowserName(WebManager.getDriver());
    }
    
    public static WebDriver startDriver() throws IOException{
    	return startDriver(getBrowserName());
    }
    
    public static WebDriver startDriver(String browserName) throws IOException {
    	return startDriver(browserName, false);
    }
    
    /** 
     * Experimental. Do not use.
     */
    @Deprecated protected static WebDriver startLocalDriver(String browserName) throws IOException {
    	return startDriver(browserName, true);
    }
    
    protected static WebDriver startDriver(String browserName, Boolean runLocal) throws IOException {
    	
    	if(browserName==null) {
    		browserName="firefox";
    	}

    	String forceBrowser = TestManager.Preferences.getPreference("browser.override","false");
    	if(!forceBrowser.trim().equalsIgnoreCase("false")){
    		browserName = forceBrowser;
    	}
    	
    	System.err.println("Create Driver: "+browserName);
    	System.setProperty("webdriver.reap_profile", "true");
    	
    	WebDriver driver = null;
    	
    	try {
//    		if(!runLocal && (System.getenv("SAUCE_USERNAME")!=null || ("saucelabs").equalsIgnoreCase(DriverSettings.getProperty(DriverSetting.EXECUTOR)))){
//    			if(Boolean.valueOf(DriverSettings.getProperty(DriverSetting.SAUCE_CONNECT_FORCE_START))) SauceLabsDriver.sauceConnect();
//    			driver = SauceLabsDriver.trial();
//    		} else if(!runLocal && ("cbtesting").equalsIgnoreCase(DriverSettings.getProperty(DriverSetting.EXECUTOR))){
//    			driver = CBTestingDriver.trial();
//    		}
//    		else {
    			DriverSettings.BrowserInfo browserInfo = new DriverSettings.BrowserInfo(browserName);
    			switch(browserInfo.browserType.trim().toLowerCase()){
		    		case "chrome":
		    			driver = setup_chrome();
		    			break;
		    		case "ie":
		    		case BrowserType.IEXPLORE:
		    			driver = setup_IE();
		    			break;
		    		case "firefox":
		    			driver = browserInfo.debugMode? setup_firefoxDebug() : setup_firefox();
		    			break;
		    		case "safari":
		    			driver = setupSafari();
		    			break;
		    		default:
		    			driver = setup_firefox();
		    			break;
    			}
//    		}
    	} catch(Error error){
    		System.out.println("Error: "+error.getMessage());
    		throw error;
    	}

    	Timeouts timeouts = driver.manage().timeouts();
    	Window window = driver.manage().window();
    	try {
	    	timeouts.pageLoadTimeout(Preferences.pageLoadTimeOut(), TimeUnit.SECONDS);
	    	timeouts.implicitlyWait(Preferences.implicitlyWait(), TimeUnit.SECONDS);
	    	timeouts.setScriptTimeout(Preferences.setScriptTimeout(), TimeUnit.SECONDS);
    	} catch(WebDriverException e){}
    	try {
    		driver.switchTo().activeElement().sendKeys(Keys.chord(Keys.CONTROL,"0")); //zoom to 100%
    	} catch(WebDriverException e){}
    	try {
    		if(Preferences.maximize()) window.maximize();
    	} catch(WebDriverException e){}
    	
    	return setDriver(driver);
    }
    
    public static WebDriver setDriver(String browserName, String profile) throws IOException {
    	return startDriver(browserName);
    }
 
    public static WebDriver setDriver(WebDriver driver) {
    	try {
    		assert(driver!=null);
    		webDriver.set(driver);
   
    	} catch(Error error){
    		throw new Error("Driver is null");
    	}
        
        return getDriver();
    }
     
    public static Proxy getProxySettings(){
    	Proxy proxy = new org.openqa.selenium.Proxy();
    	
    	String proxyType = TestManager.Preferences.getPreference("webdriver.proxy.proxyType", "unspecified").toLowerCase();
    	
    	switch(proxyType){
    	case "direct":
    		proxy.setProxyType(ProxyType.DIRECT);
        	break;
    	case "pac":
    		proxy.setProxyType(ProxyType.PAC)
    			.setProxyAutoconfigUrl(TestManager.Preferences.getPreference("webdriver.proxy.proxyAutoconfigUrl"));
    		break;
    	case "manual":
    		String allProxy, ftpProxy, httpProxy, sslProxy, socksProxy, socksUsername, socksPassword, noProxy;
    		allProxy = TestManager.Preferences.getPreference("webdriver.proxy.allProxy",null);
    		if(allProxy!=null){
    			ftpProxy = httpProxy = sslProxy = socksProxy = allProxy;
    		} else {
	    		ftpProxy = TestManager.Preferences.getPreference("webdriver.proxy.ftpProxy","");
	    		httpProxy = TestManager.Preferences.getPreference("webdriver.proxy.httpProxy","");
	    		sslProxy = TestManager.Preferences.getPreference("webdriver.proxy.sslProxy","");
	    		socksProxy = TestManager.Preferences.getPreference("webdriver.proxy.socksProxy","");
    		}
    		socksUsername = TestManager.Preferences.getPreference("webdriver.proxy.socksUsername","");
    		socksPassword = TestManager.Preferences.getPreference("webdriver.proxy.socksPassword","");
    		noProxy = TestManager.Preferences.getPreference("webdriver.proxy.noProxy","");
        	proxy.setProxyType(ProxyType.MANUAL)
        		.setNoProxy(noProxy)
        		.setHttpProxy(httpProxy)
	    	    .setFtpProxy(ftpProxy)
	    	    .setSslProxy(sslProxy);
        	proxy.setSocksProxy(socksProxy)
	        	.setSocksUsername(socksUsername).setSocksPassword(socksPassword);
        	break;
    	case "system":
    		proxy.setProxyType(ProxyType.SYSTEM);
    		break;
    	default:
    		proxy.setProxyType(ProxyType.UNSPECIFIED);
    		break;
    	}
    	return proxy;
    }
    
	private static WebDriver setupSafari() {
	 	return new SafariDriver();
 	}
    
	private static WebDriver setup_IE() throws MalformedURLException {
		
		WebDriver edriver;
		Boolean usingGrid = Boolean.parseBoolean(TestManager.Preferences.getPreference("selenium.grid"));
		String remoteHost = TestManager.Preferences.getPreference("selenium.rc_host", "NONE");
		Integer remotePort = Integer.parseInt(TestManager.Preferences.getPreference("selenium.rc_port", "4444"));
		
		if (!usingGrid) {
			File file = determineDriverLocationIE();
			if (!file.exists()) {
				ReportLog.failed("IE Driver not found");
			}
			String driverPath = file.getAbsolutePath();
			System.setProperty("webdriver.ie.driver", driverPath);
		}

		DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
//    	capabilities.setCapability(InternetExplorerDriver.FORCE_CREATE_PROCESS, true);
    	capabilities.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false); //test (orig=false)
    	capabilities.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, false); //test
    	capabilities.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, false); //test
    	capabilities.setCapability(InternetExplorerDriver.IE_SWITCHES, "-private");
    	capabilities.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL, "about:blank");
    	capabilities.setCapability(InternetExplorerDriver.SILENT, true);
    	capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
    	capabilities.setCapability("ignoreZoomSetting", true);
		capabilities.setCapability("ignoreProtectedModeSettings", true);
		capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		capabilities.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
		InternetExplorerOptions options = new InternetExplorerOptions(capabilities);
		
		if (usingGrid) {
			edriver = new RemoteWebDriver(new URL("http://" + remoteHost + ":" + remotePort + "/wd/hub"), options);
		} else {
			edriver = new InternetExplorerDriver(options);
		}
		return edriver;
	}
		
	private static WebDriver setup_chrome() throws MalformedURLException {
		
		WebDriver cdriver ;		
		Boolean usingGrid = Boolean.parseBoolean(TestManager.Preferences.getPreference("selenium.grid"));
		String remoteHost = TestManager.Preferences.getPreference("selenium.rc_host", "NONE");
		Integer remotePort = Integer.parseInt(TestManager.Preferences.getPreference("selenium.rc_port", "4444"));
		
		if(!usingGrid){
			File file = determineDriverLocationChrome();
			if(!file.exists()) {
				ReportLog.assertFailed("Chrome Driver not found");
			}
			String driverPath = file.getAbsolutePath();
			System.setProperty("webdriver.chrome.driver", driverPath);
		}
		
		ChromeOptions options = new ChromeOptions();
		options.setCapability("ignoreZoomSetting", true);
		options.setCapability("ignoreProtectedModeSettings", true);
		options.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
		
		if(Boolean.parseBoolean(TestManager.Preferences.getPreference("headless", "false"))) {
			options.addArguments("--headless");
		}
		
		if (usingGrid) {
			cdriver = new RemoteWebDriver(new URL("http://" + remoteHost + ":"+ remotePort + "/wd/hub"), options);
		} else {
			cdriver = new ChromeDriver(options);
		}
		return cdriver;
	}

	private static WebDriver setup_firefox() throws MalformedURLException {
		
		WebDriver fdriver = null ;
    	System.setProperty("webdriver.firefox.useExisting", "false");
    	System.setProperty("webdriver.reap_profile", "false");
    	
		Boolean usingGrid = Boolean.parseBoolean(TestManager.Preferences.getPreference("selenium.grid"));
		String remoteHost = TestManager.Preferences.getPreference("selenium.rc_host", "NONE");
		Integer remotePort = Integer.parseInt(TestManager.Preferences.getPreference("selenium.rc_port", "4444"));
    	
		if(!usingGrid){
			File file = determineDriverLocationFirefox();
			if(!file.exists()) {
				ReportLog.assertFailed("Firefox Gecko Driver not found");
			}	
			String driverPath = file.getAbsolutePath();
			System.setProperty("webdriver.gecko.driver", driverPath);
		}
//		FirefoxProfile profile = new FirefoxProfile();
		DesiredCapabilities capability = DesiredCapabilities.firefox();
//		capability.setCapability("marionette", true);
//		capability.setCapability(FirefoxDriver.PROFILE, profile);
		
//		capability.setCapability("ignoreZoomSetting", true);
//		capability.setCapability("ignoreProtectedModeSettings", true);
//		capability.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
		FirefoxOptions options = new FirefoxOptions(capability);
		
		if (usingGrid) {
			fdriver = new RemoteWebDriver(new URL("http://" + remoteHost + ":"+ remotePort + "/wd/hub"), capability);
		} else {
			fdriver = new FirefoxDriver(options);
		}

		return fdriver;
	}
	
	private static WebDriver setup_firefoxDebug() throws MalformedURLException {
		debugMode = true;

		System.setProperty("webdriver.firefox.useExisting", "true");
		System.setProperty("webdriver.reap_profile", "false");
		Boolean isRunning = false;
		try {
			final Socket socket = new Socket();
			socket.connect(new InetSocketAddress("localhost", 7055));
			socket.close();
			isRunning = true;
		} catch (final IOException io) {
			// io.printStackTrace();
		}
		if (!isRunning) {
			return setup_firefox();
		} else {
			try {
				final WebDriver existingWebDriver = new RemoteWebDriver(new URL("http://localhost:7055/hub"),
						DesiredCapabilities.firefox());
				System.out.println("Using existing..");
				return existingWebDriver;

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private static File determineDriverLocationIE(){

		String osName = System.getProperty("os.name", "ERROR").toLowerCase();
		osName = osName.contains("win") ? "win" 
				: osName.contains("mac") ? "mac" 
						: osName.contains("nux") ? "linux" : "win";

		if(osName.contains("win")) {
			osName = (osName + (System.getProperty("os.arch", "ERROR").contains("64") ? "x64" : "x32")).replace("x", "");
		}		         
		String filename =osName+ "_IEDriverServer" + (System.getProperty("os.name", "ERROR").toLowerCase().contains("win") ? ".exe" : "");
		return new File(current_Dir+File.separator+"src"+
				File.separator+"main"+File.separator+"resources"+ 
				File.separator+ "webdriver" + File.separator + filename);
	
	}
	
	 
	private static File determineDriverLocationChrome() {

		String osName = System.getProperty("os.name", "ERROR").toLowerCase();
		osName = osName.contains("win") ? "win"
				: osName.contains("mac") ? "mac" : osName.contains("nux") ? "linux" : "win";
		System.out.println("osName:" + osName);
		if (osName.contains("mac")) {
			return new File(current_Dir + File.separator + "src" + File.separator + "main" + File.separator
					+ "resources" + File.separator + "webdriver" + File.separator + "mac_chromedriver");
		} else {
			return new File(current_Dir + File.separator + "src" + File.separator + "main" + File.separator
					+ "resources" + File.separator + "webdriver" + File.separator + "chromedriver.exe");
		}
	}
	
	private static File determineDriverLocationFirefox() {

		String osName = System.getProperty("os.name", "ERROR").toLowerCase();
		osName = osName.contains("win") ? "win" 
				: osName.contains("mac") ? "mac" 
						: osName.contains("nux") ? "linux" : "win";

		if(osName.contains("win")) {
			osName = (osName + (System.getProperty("os.arch", "ERROR").contains("64") ? "x64" : "x32")).replace("x", "");
			String filename =osName+ "_geckodriver" + (System.getProperty("os.name", "ERROR").toLowerCase().contains("win") ? ".exe" : "");
			return new File(current_Dir+File.separator+"src"+
					File.separator+"main"+File.separator+"resources"+ 
					File.separator+ "webdriver" + File.separator + filename);
		} else {
			return new File(current_Dir + File.separator + "src" + File.separator + "main" 
					+ File.separator + "resources" + File.separator + "webdriver" 
					+ File.separator + "mac_geckodriver");
		}		         

	}
       
//    private static WebDriver newInternetExplorerDriver() throws IOException{
//    	try {
//    		String arch = System.getenv("PROCESSOR_ARCHITECTURE");
//    		String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");
//
//    		String realArch = arch.endsWith("64")
//    		                  || wow64Arch != null && wow64Arch.endsWith("64")
//    		                      ? "64" : "32";
//    		
//    		String ieDriverPath;
//    	
//    		switch(realArch){
//    		case "64":
//    			ieDriverPath = JarFileToLocal.copyTmp("/webdriver/IEDriverServer_64.exe").getCanonicalPath();
//    			break;
//    		case "32":
//    		default:
//    			ieDriverPath = JarFileToLocal.copyTmp("/webdriver/IEDriverServer_32.exe").getCanonicalPath();
//    			break;
//    		}
//    		
//    		//String ieDriverPath = JarFileToLocal.copyTmp("/webdriver/IEDriverServer.exe").getCanonicalPath();//WebManager.class.getClass().getResource("/webdriver/IEDriverServer.exe").getFile().replace("%20", " ");
//    		System.setProperty("webdriver.ie.driver",ieDriverPath);
//    	} catch(Error error){
//    		throw new Error("IE Driver not found");
//    	}
//		
//    	DesiredCapabilities capabilities = getIECapabilities();
//    	
//    	//capabilities.setCapability(InternetExplorerDriver.LOG_LEVEL, "FATAL");
//    
//    	//HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Internet Explorer\Main\FeatureControl\FEATURE_BFCACHE
//    	WinRegistry.add(WinRegistry.HKEY_CURRENT_USER, "software\\microsoft\\internet explorer\\main\\FeatureControl\\FEATURE_BFCACHE", "iexplore.exe", WinRegistry.REG_DWORD, 0);
//    	//HKEY_LOCAL_MACHINE\SOFTWARE\Wow6432Node\Microsoft\Internet Explorer\Main\FeatureControl\FEATURE_BFCACHE
//    	WinRegistry.add(WinRegistry.HKEY_CURRENT_USER, "software\\wow6432node\\microsoft\\internet explorer\\main\\FeatureControl\\FEATURE_BFCACHE", "iexplore.exe", WinRegistry.REG_DWORD, 0);
//    	
//    	Proxy proxy = getProxySettings();
//		
//		capabilities.setCapability(CapabilityType.PROXY, proxy);
//    	
//		WebDriver driver;
//		
//    	try {
//    		driver = new InternetExplorerDriver(capabilities);
//    	} catch(SessionNotFoundException e){
//    		if(e.getMessage().contains("TabProcGrowth")){
//    			System.err.println("Setting TabProcGrowth to 0");
//    			//reg add "hkcu\software\microsoft\internet explorer\main" /v TabProcGrowth /t reg_dword /d 0 /f
//    			WinRegistry.add(WinRegistry.HKEY_CURRENT_USER, "software\\microsoft\\internet explorer\\main", "TabProcGrowth", WinRegistry.REG_DWORD, 0);
//    		}
//    		driver = new InternetExplorerDriver(capabilities);
//    	}
//    	WinRegistry.add(WinRegistry.HKEY_CURRENT_USER, "software\\microsoft\\windows\\currentversion\\internet settings", "ProxyOverride", WinRegistry.REG_SZ, proxy.getNoProxy());
//    	return driver;
//	}
    
//  private static WebDriver newFirefoxDriver(){    	
//	System.setProperty("webdriver.firefox.useExisting", "false");
//	System.setProperty("webdriver.reap_profile", "false");
//	String userAgent= TestManager.Preferences.getPreference("UserAgent");
//	
////	ProfilesIni profile = new ProfilesIni();
////	FirefoxProfile myprofile = profile.getProfile("default");
////	
////	if (userAgent!=null) {
////		myprofile.setPreference("general.useragent.override", UserAgentList.valueOf(userAgent).toString()); // here, the user-agent is 'Yahoo Slurp'
////	}
//	
//	/*File ffPath = new File(DriverSettings.getProperty(DriverSetting.FIREFOX_BINARY, "C:\\Mozilla Firefox\\firefox.exe"));
//	FirefoxBinary binary;
//	if(ffPath.exists()){//test
//    	System.out.println("Loading custom driver for Jenkins..");
//		binary = new FirefoxBinary(ffPath);
//	}else{
//		binary = new FirefoxBinary();
//	}*/
//	DesiredCapabilities capabilities = DesiredCapabilities.firefox();
//	/*	Proxy proxy = getProxySettings();
//	if(proxy.getProxyType().equals(ProxyType.MANUAL)){
//    	String noProxy = proxy.getNoProxy();
//    	proxy.setNoProxy(noProxy!=null ? noProxy.replace(";", ",") : noProxy);
//	}
//	capabilities.setCapability(CapabilityType.PROXY, proxy);*/
//	//return new FirefoxDriver(binary,myprofile,null,capabilities);
//	//return new FirefoxDriver(myprofile);
//	Boolean usingGrid = Boolean.parseBoolean(TestManager.Preferences.getPreference("selenium.grid"));
//	String remoteHost = TestManager.Preferences.getPreference("selenium.rc_host", "NONE");
//	Integer remotePort = Integer.parseInt(TestManager.Preferences.getPreference("selenium.rc_port", "4444"));
//	
//	WebDriver driver = null;
//	if(usingGrid){
//		try {
//			driver = new RemoteWebDriver(new URL("http://" + remoteHost + ":"+ remotePort + "/wd/hub"), DesiredCapabilities.firefox());
//			return driver;
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	} else {
//		driver =  new FirefoxDriver();
//	}
//	return driver;
//}
	
//  private static WebDriver newFirefoxDebugDriver(){
//	debugMode=true;
//	
//	System.setProperty("webdriver.firefox.useExisting", "true");
//	System.setProperty("webdriver.reap_profile", "false");
//	Boolean isRunning = false;
//	try {
//		final Socket socket = new Socket();
//		socket.connect(new InetSocketAddress("localhost",7055));
//		socket.close();
//		isRunning = true;
//	} catch(final IOException io){
//		//io.printStackTrace();
//	}
//	if(!isRunning){
//		return newFirefoxDriver();
//	} else {
//		try {
//			final WebDriver existingWebDriver = new RemoteWebDriver(new URL("http://localhost:7055/hub"), DesiredCapabilities.firefox());
//			System.out.println("Using existing..");
//			return existingWebDriver;
//			
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	return null;
//} 
	
//    private static WebDriver newChromeDriver(){
//    	
//    	String chromeDriverPath;
//    	
//    	try {
//    		chromeDriverPath = JarFileToLocal.copyTmp("/webdriver/chromedriver.exe").getCanonicalPath();
//    		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
//    	} catch(Error | IOException error){
//    		throw new Error("Chrome Driver not found");
//    	}
//    	
//    	System.setProperty("webdriver.chrome.driver", chromeDriverPath);
//    	
//
//		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
//		
//		ChromeOptions options = new ChromeOptions();
//	    options.addArguments("test-type");
//		
//		capabilities.setCapability(CapabilityType.PROXY, getProxySettings());
//		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
//    	
//		return new ChromeDriver(capabilities);
//    	
//	}
    
    //will make a separate class for this
    protected static class Configuration {
    	
    	public static String readProperty(String property) {
    		String configPath = "./src/test/resources/config/defaults.properties";
    		File configFile = new File( configPath );
    		if(configFile.exists()){
    			try {
    				FileInputStream fileInput = new FileInputStream(configFile);
        			Properties properties = new Properties();
					properties.load(fileInput);
					return properties.getProperty(property);
				} catch (Exception e) {
				}
    		}
    		return null;
    	}
    	
    }
    
    
    protected static class Preferences {
    	
    	public static long pageLoadTimeOut(){
    		return Long.valueOf(
    			TestManager.Preferences.getPreference("webdriver.timeouts.pageLoadTimeOut","-1")
    		);
    	}
    	
    	public static long implicitlyWait(){
    		return Long.valueOf(
    			TestManager.Preferences.getPreference("webdriver.timeouts.implicitlyWait","2")
    		);
    	}
    	
    	public static long setScriptTimeout(){
    		return Long.valueOf(
    			TestManager.Preferences.getPreference("webdriver.timeouts.setScriptTimeout","-1")
    		);
    	}
    	
    	
    	
    	public static boolean maximize(){
    		return Boolean.valueOf(
    			TestManager.Preferences.getPreference("webdriver.window.maximize","false")
    		);
    	}
    	
    }
    
    
    protected static class JarFileToLocal {
    	
    	public static File copyTmp(String jarPath) throws IOException {
    		//String fileName = new File(jarPath).getName();
    		File tmpDir = FileUtils.getTempDirectory();
    		File file = new File(tmpDir,jarPath);
    		if(file.exists()) return file;
    		
    		File fileDir = file.getParentFile();
    		
    		if(fileDir.exists()||fileDir.mkdir()){
    			InputStream stream = JarFileToLocal.class.getResourceAsStream(jarPath);
    			if (stream == null) {
    		        //warning
    		    }
    		    OutputStream resStreamOut = null;
    		    int readBytes;
    		    byte[] buffer = new byte[4096];
    		    try {
    		        resStreamOut = new FileOutputStream(file);
    		        while ((readBytes = stream.read(buffer)) > 0) {
    		            resStreamOut.write(buffer, 0, readBytes);
    		        }
    		    } catch (IOException e1) {
    		        e1.printStackTrace();
    		    } finally {
    		        stream.close();
    		        if(resStreamOut!=null) resStreamOut.close();
    		    }
    		}
    		return file;
    	}
    	
    }
    
    
	public static class WinRegistry {
		
		//public static void 
		
		public static final String HKEY_CLASSES_ROOT = "hkcr";
		public static final String HKEY_LOCAL_MACHINE = "hklm";
		public static final String HKEY_CURRENT_USER = "hkcu";
		public static final String HKEY_USERS = "hku";
		public static final String HKEY_CURRENT_CONFIG = "hkcc";
		
		public static final String REG_DWORD = "reg_dword";
		public static final String REG_SZ = "reg_sz";
		
		//reg add "hkcu\software\microsoft\internet explorer\main" /v TabProcGrowth /t reg_dword /d 0 /f
		
		//reg add KeyName [/v EntryName|/ve] [/t DataType] [/s separator] [/d value] [/f]
		public static String add(String subtree, String keyName, String entryName, String dataType, Object value){
			try{
				
				String command = "reg add " + 
	                    '"' + subtree+ "\\"+ keyName + "\" /v " + entryName + " /t " + dataType + " /d " + value.toString() + " /f";
				
				//System.err.println(command);
				
				Process process = Runtime.getRuntime().exec(command);
				StreamReader reader = new StreamReader(process.getInputStream());
	            reader.start();
	            process.waitFor();
	            reader.join();
	            String output = reader.getResult();
	
	            // Output has the following format:
	            // \n<Version information>\n\n<key>\t<registry type>\t<value>
	            if( ! output.contains("\t")){
	                    return null;
	            }
	
	            // Parse out the value
	            //String[] parsed = output.split("\t");
	            return String.valueOf(process.exitValue());
	            //return parsed[parsed.length-1];	
				//return command;
	        }
	        catch (Exception e) {
	            return null;
	        }
		}
		
		static class StreamReader extends Thread {
	        private InputStream is;
	        private StringWriter sw= new StringWriter();

	        public StreamReader(InputStream is) {
	            this.is = is;
	        }

	        public void run() {
	            try {
	                int c;
	                while ((c = is.read()) != -1)
	                    sw.write(c);
	            }
	            catch (IOException e) { 
	        }
	        }

	        public String getResult() {
	            return sw.toString();
	        }
	    }
		
	}
	
    
}



	
