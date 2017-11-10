package com.IHG.tests;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.IHG.pageObjects.*;
import com.IHG.utils.Utilities;
import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class Scenario6 {

	AppiumDriver<MobileElement> driver;
	ReportiumClient reportiumClient;
	HomeScreenPageObject homeScreenPageObject;
	
	@Parameters({ "deviceID", "appName", "mobileOS"})
	@BeforeTest
	public void beforeTest(String deviceID, String appName, String mobileOS) throws IOException {
		driver = Utilities.getAppiumDriver(deviceID, appName, mobileOS);
		homeScreenPageObject = new HomeScreenPageObject(driver);
		
		
	
		// Reporting client. For more details, see
		// https://github.com/perfectocode/samples/wiki/reporting
		PerfectoExecutionContext perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
				.withProject(new Project("IHG", "1.0")).withJob(new Job("Job1", 45)).withContextTags("Smoke")
				.withWebDriver(driver).build();
		reportiumClient = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);
	
	}

	@Parameters({ "mobileOS" })
	@Test
	public void scenario6(String mobileOS){
		try {
			reportiumClient.testStart("IHG Demo", new TestContext("IHG", "smoketest"));
					// this is a logical step for reporting
			
			
			driver.getContext();
			System.out.println(driver.getContextHandles().size());
			
			Set<String> a = driver.getContextHandles();
			for(String b:a)
				System.out.println(b);
			driver.context("WEBVIEW");
			System.out.println(driver.getPageSource());
			homeScreenPageObject.enterUsername();
			
			driver.context("WEBVIEW");
			homeScreenPageObject.enterPassword();
			
			homeScreenPageObject.loginButtoClick();
			
			homeScreenPageObject.clickGetlocation();
			
			driver.context("NATIVE_APP");
			homeScreenPageObject.allowGeoLocation();
			
			
			reportiumClient.testStop(TestResultFactory.createSuccess());
		} catch (Exception e) {
			reportiumClient.testStop(TestResultFactory.createFailure(e.getMessage(), e));
			e.printStackTrace();
		}
	}

	@AfterTest
	public void afterTest() {
		try {
			// driver.resetApp();
			driver.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		driver.quit();
	}
}
