package com.app.utils;

import java.io.ByteArrayInputStream;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import com.app.driver.StatusType;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;

public class Event {

	/**
	 * Logs using Appium driver
	 *
	 * @param Webdriver
	 * @param stats
	 * @param message
	 */
	public void log(RemoteWebDriver driver, String stats, String message) {
		StatusType status = StatusType.valueOf(stats);
		switch (status) {
		case FAIL: {
			System.err.println("[FAIL]: " + message);
			Allure.step(message, Status.FAILED);
			Screenshot(driver,message.toUpperCase());
			Assert.fail("Failed due to: " + message);
			break;
		}

		case PASS: {
			System.out.println("[PASS]: " + message);
			Allure.step(message, Status.PASSED);
			break;
		}

		case INFO: {
			System.out.println("[INFO]: " + message);
			Allure.step(message);
			break;
		}

		case SKIP: {
			System.out.println("[SKIP]: " + message);
			Allure.step(message, Status.SKIPPED);
			break;
		}
		case ERROR: {
			System.err.println("[ERROR]: " + message);
			Allure.step(message, Status.BROKEN);
			Screenshot(driver,message.toUpperCase());
			break;
		}
		case WARNING: {
			System.err.println("[WARNING]: " + message);
			Allure.step(message, Status.BROKEN);
			if(driver != null){
				Screenshot(driver,message.toUpperCase());
			}
			break;
		}
		case DEBUG: {
			System.err.println("[DEBUG]: " + message);
			Allure.step(message, Status.SKIPPED);
			break;
		}
		default: {
			System.out.println("case not implemented for: " + status);
			break;
		}

		}
	}

	/**
	 * Logs using Appium driver
	 *
	 * @param driver
	 * @param stats
	 * @param message
	 */

	public void log(AppiumDriver<?> driver, String stats, String message) {
		StatusType status = StatusType.valueOf(stats);
		switch (status) {
		case FAIL: {
			System.err.println("[FAIL]: " + message);
			Allure.step(message, Status.FAILED);
			Screenshot(driver,message.toUpperCase());
			Assert.fail("Failed due to: " + message);
			break;
		}

		case PASS: {
			System.out.println("[PASS]: " + message);
			Allure.step(message, Status.PASSED);
			break;
		}

		case INFO: {
			System.out.println("[INFO]: " + message);
			Allure.step(message);
			break;
		}
		case SKIP: {
			System.out.println("[SKIP]: " + message);
			Allure.step(message, Status.SKIPPED);
			
			break;
		}
		case ERROR: {
			System.err.println("[ERROR]: " + message);
			Allure.step(message, Status.BROKEN);
			Screenshot(driver,message.toUpperCase());
			break;
		}
		case WARNING: {
			System.err.println("[WARNING]: " + message);
			Allure.step(message, Status.BROKEN);
			if(driver != null){
				Screenshot(driver,message.toUpperCase());
			}
 			break;
		}
		case DEBUG: {
			System.err.println("[DEBUG]: " + message);
			Allure.step(message, Status.SKIPPED);
			break;
		}
		default: {
			System.out.println("case not implemented for: " + status);
			break;
		}
		}
	}

	public void Wait(int a) {
		try {
			Thread.sleep(a);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void log(String stats, String message) {

		StatusType status = StatusType.valueOf(stats);
		switch (status) {
		case FAIL: {
			System.err.println("[FAIL]: " + message);
			Allure.step(message, Status.FAILED);
			Assert.fail("Failed due to: " + message);
			break;
		}

		case PASS: {
			System.out.println("[PASS]: " + message);
			Allure.step(message, Status.PASSED);
			break;
		}

		case INFO: {
			System.out.println("[INFO]: " + message);
			Allure.step(message);
			break;
		}
		case SKIP: {
			System.out.println("[SKIP]: " + message);
			Allure.step(message, Status.SKIPPED);
			break;
		}
		case ERROR: {
			System.err.println("[ERROR]: " + message);
			Allure.step(message, Status.BROKEN);
			break;
		}
		case WARNING: {
			System.err.println("[WARNING]: " + message);
			Allure.step(message, Status.BROKEN);
			break;
		}
		case DEBUG: {
			System.err.println("[DEBUG]: " + message);
			Allure.step(message, Status.BROKEN);
			break;
		}
		default: {
			System.out.println("case not implemented for: " + status);
			break;
		}
		}

	}

	public void printStackTrace(Exception e) {
//		e.printStackTrace();

	}
	
	
	
	@Step
    public void Screenshot(AppiumDriver<?> driver, String status) {
        try {
			Allure.addAttachment("Screenshot_"+status,
			        new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
		} catch (Exception e) {
			Allure.step("Unable to Take Screenshot due to Exception", Status.SKIPPED);
		}
    }

	@Step
	public void attachAPIResponse(String response){
		Allure.addAttachment("API Response", response);
	}
	
	
	@Step
    public void Screenshot(RemoteWebDriver driver,String status) {
        try {
			Allure.addAttachment("Screenshot_"+status,
			        new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
		} catch (Exception e) {
			Allure.step("Unable to Take Screenshot due to Exception", Status.SKIPPED);
		}
    }

	public void hideKeyboard(String platform, AppiumDriver<MobileElement> driver) {
		if (platform.equalsIgnoreCase("Android")) {
			driver.hideKeyboard();
		}
		
	}

}
