package com.IHG.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteExecuteMethod;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class Utilities {
	AppiumDriver<MobileElement> driver;
	public static String REPORT_LIB = "/Users/sreevatsa/Desktop/Perfecto/Automation/TestReports";
	public static String SCREENSHOTS_LIB = "/Users/sreevatsa/Desktop/Perfecto/Automation/TestReports/Screenshots";

	public static AppiumDriver<MobileElement> getAppiumDriver(String deviceId, String app, String platform)
			throws IOException {

		AppiumDriver<MobileElement> webdriver = null;

		DesiredCapabilities capabilities = new DesiredCapabilities("", "", Platform.ANY);

		// PerfectoDriver
		if (platform.equalsIgnoreCase("ios")) {
			capabilities.setCapability("bundleId", app);
			

		} else {
			//capabilities.setCapability("app-activity", app);
			capabilities.setCapability("appPackage", app);

		}

		capabilities.setCapability("user", "Praveenh@perfectomobile.com");
		capabilities.setCapability("password", "Welcome0604");
		capabilities.setCapability("deviceName", deviceId);
		capabilities.setCapability("noReset", false);
		capabilities.setCapability("automationName", "appium");
		PerfectoLabUtils.setExecutionIdCapability(capabilities, "partners.perfectomobile.com");

		// One liner to set persona
		// capabilities.setCapability("windTunnelPersona", persona);
		try {
			if (platform.equalsIgnoreCase("ios"))
				webdriver = new IOSDriver<MobileElement>(
						new URL("https://partners.perfectomobile.com" + "/nexperience/perfectomobile/wd/hub"),
						capabilities);
		
			else if(platform.equalsIgnoreCase("Android"))
				webdriver = new AndroidDriver<MobileElement>(
						new URL("https://partners.perfectomobile.com" + "/nexperience/perfectomobile/wd/hub"),
						capabilities);
			

		} catch (Exception e) {
			String ErrToRep = e.getMessage().substring(0, e.getMessage().indexOf("Command duration") - 1);
			System.out.println(ErrToRep);
			return (null);

		}

		return webdriver;

	}

	public static void startApp(String appName, AppiumDriver<WebElement> d) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", appName);
		d.executeScript("mobile:application:open", params);
	}

	public static void stopApp(String appName, AppiumDriver<WebElement> d) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", appName);
		d.executeScript("mobile:application:close", params);
	}

	public static void hideKeyboard(AppiumDriver<MobileElement> driver, String OS)
	{
		if (OS.contentEquals("iOS")) {
			driver.findElement(By.xpath("//UIAButton[@label='Done']")).click();

		} else {
			driver.hideKeyboard();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static String getScreenShot(AppiumDriver<WebElement> driver, String name, String deviceID) {
		String screenShotName = SCREENSHOTS_LIB + name + "_" + deviceID + ".png";
		driver = (AppiumDriver<WebElement>) new Augmenter().augment(driver);
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		try {
			FileUtils.copyFile(scrFile, new File(screenShotName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return screenShotName;
	}

	public static void downloadReport(RemoteWebDriver driver, String type, String fileName) throws IOException {
		try {
			String command = "mobile:report:download";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("type", "html");
			String report = (String) driver.executeScript(command, params);
			File reportFile = new File(getReprtName(fileName, true));
			BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(reportFile));
			byte[] reportBytes = OutputType.BYTES.convertFromBase64Png(report);
			output.write(reportBytes);
			output.close();
		} catch (Exception ex) {
			System.out.println("Got exception " + ex);
		}
	}

	public static String getReprtName(String repID, boolean withPath) {
		if (withPath) {
			return REPORT_LIB + "/rep_" + repID + ".html";
		} else {
			return "/rep_" + repID + ".html";
		}
	}

	public static void deviceHome(RemoteWebDriver driver2) {
		System.out.println("hitting device HOME key");
		Map<String, Object> params1 = new HashMap<String, Object>();
		driver2.executeScript("mobile:handset:ready", params1);
	}

	public static void openApplication(RemoteWebDriver driver, String AppName) {
		System.out.println("opening application: " + AppName);
		String command = "mobile:application:open";
		Map<String, Object> Parms = new HashMap<String, Object>();
		Parms.put("name", AppName);
		driver.executeScript(command, Parms);
	}

	public static void switchToContext(AppiumDriver<MobileElement> driver, String context) {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", context);
		executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
	}

	public void scrolltoXPath(AppiumDriver<MobileElement> driver,String xPath) {
		
		do {
			try {
				driver.findElement(By.xpath(xPath));
				break;

			} catch (Exception NoSuchElementException) {
				Dimension dimensions = driver.manage().window().getSize();
				Double screenHeightStart = dimensions.getHeight() * 0.5;
				int scrollStart = screenHeightStart.intValue();
				Double screenHeightEnd = dimensions.getHeight() * 0.2;
				int scrollEnd = screenHeightEnd.intValue();
				driver.swipe(10, scrollStart, 10, scrollEnd, 2000);
	
			}
			
		} while (true);
	}
	
	public void swipeDown(AppiumDriver<MobileElement> driver) {

				Dimension dimensions = driver.manage().window().getSize();
				Double screenHeightStart = dimensions.getHeight() * 0.5;
				int scrollStart = screenHeightStart.intValue();
				Double screenHeightEnd = dimensions.getHeight() * 0.2;
				int scrollEnd = screenHeightEnd.intValue();
				driver.swipe(10, scrollStart, 10, scrollEnd, 2000);
	}

	public static void enterTextfield(AppiumDriver<WebElement> driver,String label, String text)
	{
		Map<String, Object> params = new HashMap<>();
		params.put("label", label);
		params.put("text", text);
		params.put("label.direction", "above");
		params.put("label.offset", "7%");
		driver.executeScript("mobile:edit-text:set",params);
	}
	
	public static void imageCheck(AppiumDriver<MobileElement> driver, String filePath)
	{
		Map<String, Object> params2 = new HashMap<>();
		params2.put("content", filePath);
		params2.put("match", "bounded");
		params2.put("threshold", "90");
		params2.put("duration","10");
		Object result2 = driver.executeScript("mobile:checkpoint:image", params2);
	}
	
	public static void ocrTextCheck(AppiumDriver<MobileElement> driver, String text)
	{
		Map<String, Object> params1 = new HashMap<>();
		params1.put("content", text);
		Object result1 = driver.executeScript("mobile:checkpoint:text", params1);
	}
		
			public void imageCheck(String filePath)
		{
			Map<String, Object> params2 = new HashMap<>();
			params2.put("content",filePath );
			params2.put("image.top", "0");
			params2.put("image.height", "989");
			params2.put("image.left", "25");
			params2.put("image.width", "1402");
			params2.put("timeout", "10");
			params2.put("threshold", "90");
			params2.put("match", "similar");
			Object result2 = driver.executeScript("mobile:image:find", params2);
		}

		
	
}

