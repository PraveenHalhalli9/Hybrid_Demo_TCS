package com.IHG.pageObjects;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Keys;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.IHG.utils.Utilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;

public class HomeScreenPageObject {

	AppiumDriver<MobileElement> driver;

	public HomeScreenPageObject(AppiumDriver<MobileElement> driver) {
		super();
		this.driver = driver;
		PageFactory.initElements(new AppiumFieldDecorator(driver, 30, TimeUnit.SECONDS), this);

	}

	@FindBy(xpath = "//INPUT[@type='email']")
	MobileElement usernameEditField;
	
	@FindBy(xpath = "//INPUT[@type=\"password\"]")
	MobileElement passwordFeild;
	
	@FindBy(xpath = "//*[@type=\"submit\"]")
	MobileElement loginButton;
	
	@FindBy(xpath="//*[text()=\"Get Location\"]")
	MobileElement getLocationButton;
	
	@AndroidFindBy(xpath="//*[@resource-id=\"com.android.packageinstaller:id/permission_allow_button\"]")
	MobileElement allowOption;
	
	
	public void enterUsername()
	{
		
		
		usernameEditField.click();
		driver.getKeyboard().sendKeys("tcsdemo@tcs.com");
	}
	
	public void enterPassword()
	{
		//Utilities.switchToContext(driver, "WEBVIEW");
		passwordFeild.sendKeys("tcs123");
		passwordFeild.sendKeys(Keys.ENTER);
		
	}
	
	public void loginButtoClick()
	{
		loginButton.click();
	}
	
   public void clickGetlocation(){
	   
	   getLocationButton.click();
	   
   }
   
   public void allowGeoLocation(){
	   
	   allowOption.click();
	   
   }
}
