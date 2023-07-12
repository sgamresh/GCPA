package com.app.mobile;

import org.testng.annotations.Test;
import com.app.base.BaseTest;

public class VerifyOnboardingModuleUserflows extends BaseTest {

	@Test
	public void verifyLaunchAnyAppTest() {

		System.out.println("Launching the App");
		
		Webdriver.get("https://www.bangalorejobseekers.com/");
		try {
			Thread.sleep(5000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		event.log(driver, "INFO", "Launching the App");

		event.log(driver, "INFO", "App Must Have Launched");

		
		
		

	}
}
