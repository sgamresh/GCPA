package com.app.driver.manager;

import static io.appium.java_client.remote.MobileCapabilityType.APP;
import static io.appium.java_client.remote.MobileCapabilityType.AUTOMATION_NAME;
import static io.appium.java_client.remote.MobileCapabilityType.DEVICE_NAME;
import static io.appium.java_client.remote.MobileCapabilityType.PLATFORM_VERSION;
import static io.appium.java_client.remote.MobileCapabilityType.UDID;
import static org.openqa.selenium.remote.CapabilityType.PLATFORM_NAME;

import com.app.config.Configuration;
import com.app.config.ConfigurationManager;
import com.app.driver.IDriver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;

public class IOSDriverManager implements IDriver {

	private AppiumDriver<MobileElement> driver;

	@Override
	public AppiumDriver<MobileElement> createInstance(String udid, String platformVersion) {
		try {
			Configuration configuration = ConfigurationManager.getConfiguration();
			DesiredCapabilities capabilities = new DesiredCapabilities();

			capabilities.setCapability(UDID, udid);
			capabilities.setCapability(PLATFORM_VERSION, platformVersion);
			capabilities.setCapability(DEVICE_NAME, "iPhone 14");
			capabilities.setCapability(PLATFORM_NAME, MobilePlatform.IOS);
			capabilities.setCapability(AUTOMATION_NAME, "XCUITest");
			capabilities.setCapability("xcodeOrgId", "NANANA");
			capabilities.setCapability("bundleId", "NANANA");
			capabilities.setCapability("xcodeSigningId", "iPhone Developer");
			capabilities.setCapability("useJSONSource", true);
			capabilities.setCapability("usePrebuiltWDA", false);

			if (Boolean.TRUE.equals(configuration.installApp())) {
				capabilities.setCapability(APP, new File(configuration.iosAppPath()).getAbsolutePath());
			} else {
				capabilities.setCapability(IOSMobileCapabilityType.APP_NAME, configuration.iosAppName());
			}

			driver = new IOSDriver<>(new URL(gridUrl()), capabilities);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println("Failed to initiate the tests for the IOS device");
		} catch (WebDriverException wes) {
			wes.printStackTrace();
			System.out.println("Failed to initiate the tests for the IOS device due to WebDriverException.");
		}

		catch (Exception es) {
			es.printStackTrace();
			System.out.println("Failed to initiate the tests for the IOS device");
		}

		return driver;
	}
}
