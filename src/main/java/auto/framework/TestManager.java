package auto.framework;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.Reporter;

public class TestManager implements IInvokedMethodListener {

	@Override
	public void beforeInvocation(final IInvokedMethod method, final ITestResult testResult) {

	}

	@Override
	public void afterInvocation(final IInvokedMethod method, final ITestResult testResult) {

	}

	public static void sleep(final long millis){
		try{ Thread.sleep(millis); }
		catch(final InterruptedException e) {}
	}

	public static void sleep(final long millis, final int nanos){
		try{ Thread.sleep(millis, nanos); }
		catch(final InterruptedException e) {}
	}

	public static class Preferences {

		// envi > testng > config > default

		public static String getPreference(final String property){
			return getPreference(property, null);
		}

		public static String getPreference(final String property, final String defaultValue){
			
			if( System.getProperties().containsKey( "env."+property ) ){
				return System.getProperty( "env."+property );
			}
			if( System.getProperties().containsKey(property) ){
				return System.getProperty( property );
			}
			try {
				String parameterValue;
				if( (parameterValue=Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter(property))!=null ){
					return parameterValue;
				}
			} catch(final NullPointerException e){}
			try {
				String parameterValue;
				if( (parameterValue=ReportLog.currentSuite.getParameter(property))!=null ){
					return parameterValue;
				}
			} catch(final NullPointerException e){}
			String configValue;
			if( (configValue=readConfig(property))!=null ){
				return configValue;
			}
			
			// to ready from selenium.test_environment system property
			if( (configValue=readTestEnvironmentConfig(property))!=null ){
				return configValue;
			}
			
			// to ready from environmentRun system property
			if( (configValue=readEnvironmentConfig(property))!=null ){
				return configValue;
			}
			
			if( System.getenv().containsKey( property ) ){
				return System.getenv( property );
			}
			return defaultValue;
		}
		
		public static String selectEnvironment(String property, String defaultValue) {
			if( System.getProperties().containsKey( "env."+property ) ){
				return System.getProperty( "env."+property );
			}
			if( System.getProperties().containsKey(property) ){
				return System.getProperty( property );
			}
			try {
				String parameterValue;
				if( (parameterValue=Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getParameter(property))!=null ){
					return parameterValue;
				}
			} catch(final NullPointerException e){}
			try {
				String parameterValue;
				if( (parameterValue=ReportLog.currentSuite.getParameter(property))!=null ){
					return parameterValue;
				}
			} catch(final NullPointerException e){}
			String configValue;
			if( (configValue=readConfig(property))!=null ){
				return configValue;
			}
			if( System.getenv().containsKey( property ) ){
				return System.getenv( property );
			}
			return defaultValue;
		}

		private static String readConfig(final String property) {

			final String configPath = "./src/test/resources/config/defaults.properties";
			final File configFile = new File( configPath );
			if(configFile.exists()){
				try {
					final FileInputStream fileInput = new FileInputStream(configFile);
					final Properties properties = new Properties();
					properties.load(fileInput);
					return properties.getProperty(property);
				} catch (final Exception e) {
				}
			}
			return null;
		}

		private static String readTestEnvironmentConfig(final String property) {

			String environmentRun = selectEnvironment("selenium.test_environment", null);
			if(environmentRun == null){
				return null;
			}
			
			environmentRun = environmentRun.toLowerCase();
			final String configPath = "./src/test/resources/config/"+environmentRun+".properties";
			final File configFile = new File( configPath );
			if(configFile.exists()){
				try {
					final FileInputStream fileInput = new FileInputStream(configFile);
					final Properties properties = new Properties();
					properties.load(fileInput);
					return properties.getProperty(property);
				} catch (final Exception e) {
				}
			}
			return null;
		}
		
		private static String readEnvironmentConfig(final String property) {
			
			String environmentRun = selectEnvironment("environmentRun",null);
			if(environmentRun == null){
				return null;
			}
			
			environmentRun = environmentRun.toLowerCase();
			final String configPath = "./src/test/resources/config/"+environmentRun+".properties";
			final File configFile = new File( configPath );
			if(configFile.exists()){
				try {
					final FileInputStream fileInput = new FileInputStream(configFile);
					final Properties properties = new Properties();
					properties.load(fileInput);
					return properties.getProperty(property);
				} catch (final Exception e) {
				}
			}
			return null;
		}
		
	}

}
